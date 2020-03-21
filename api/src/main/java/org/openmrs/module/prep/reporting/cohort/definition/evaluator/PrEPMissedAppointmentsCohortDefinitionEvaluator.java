/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.prep.reporting.cohort.definition.evaluator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Cohort;
import org.openmrs.annotation.Handler;
import org.openmrs.module.prep.reporting.cohort.definition.PrEPMissedAppointmentsCohortDefinition;
import org.openmrs.module.reporting.cohort.EvaluatedCohort;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.evaluator.CohortDefinitionEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;

/**
 * Evaluator for clients who have missed their appointments
 */
@Handler(supports = { PrEPMissedAppointmentsCohortDefinition.class })
public class PrEPMissedAppointmentsCohortDefinitionEvaluator implements CohortDefinitionEvaluator {
	
	private final Log log = LogFactory.getLog(this.getClass());
	
	@Autowired
	EvaluationService evaluationService;
	
	@Override
	public EvaluatedCohort evaluate(CohortDefinition cohortDefinition, EvaluationContext context) throws EvaluationException {
		PrEPMissedAppointmentsCohortDefinition definition = (PrEPMissedAppointmentsCohortDefinition) cohortDefinition;
		
		if (definition == null)
			return null;
		
		Cohort newCohort = new Cohort();
		String qry = "select  m.patient_id\n" + " from (\n"
		        + "   select f.patient_id, latest_vis_date, latest_tca, date_discontinued, disc_patient\n" + "   from (\n"
		        + "   select t.patient_id,\n" + "    mid(max(concat(t.visit_date, t.appointment_date)), 11) latest_tca,\n"
		        + "    max(t.visit_date) as latest_vis_date,\n" + "    d.patient_id      as disc_patient,\n"
		        + "    max(d.visit_date) as date_discontinued\n" + "    from (\n"
		        + "     (select patient_id, visit_date, appointment_date\n"
		        + "       from kenyaemr_etl.etl_prep_followup fup\n" + "      )\n" + "     union\n" + "     (\n"
		        + "      select patient_id, visit_date, next_appointment as appointment_date\n"
		        + "       from kenyaemr_etl.etl_prep_monthly_refill\n" + "       )\n" + "      ) t\n"
		        + "    left outer JOIN\n" + "     (select patient_id, visit_date\n"
		        + "       from kenyaemr_etl.etl_prep_discontinuation\n" + "       where date(visit_date) <= curdate()\n"
		        + "       group by patient_id -- check if this line is necessary\n"
		        + "      ) d on d.patient_id = t.patient_id\n" + "      group by t.patient_id\n" + " ) f\n"
		        + " JOIN kenyaemr_etl.etl_patient_demographics pd ON f.patient_id = pd.patient_id\n"
		        + " JOIN kenyaemr_etl.etl_prep_enrolment e on f.patient_id = e.patient_id\n"
		        + " where latest_vis_date <= curdate()\n" + " GROUP BY f.patient_id\n" + " having (\n"
		        + "  (((date(latest_tca) < curdate()) and (date(latest_vis_date) < date(latest_tca))))\n"
		        + "   and ((date(latest_tca) > date(date_discontinued)\n"
		        + "   and date(latest_vis_date) > date(date_discontinued)) or disc_patient is null)\n" + " )) m;";
		
		SqlQueryBuilder builder = new SqlQueryBuilder();
		builder.append(qry);
		List<Integer> ptIds = evaluationService.evaluateToList(builder, Integer.class, context);
		newCohort.setMemberIds(new HashSet<Integer>(ptIds));
		return new EvaluatedCohort(newCohort, definition, context);
	}
	
}
