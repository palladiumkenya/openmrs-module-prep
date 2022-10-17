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
import org.openmrs.module.prep.reporting.cohort.definition.CurrentlyOnPrepCohortDefinition;
import org.openmrs.module.reporting.cohort.EvaluatedCohort;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.evaluator.CohortDefinitionEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * Evaluator for currentlyOnPrepCohortDefinition
 */
@Handler(supports = { CurrentlyOnPrepCohortDefinition.class })
public class CurrentlyOnPrepCohortDefinitionEvaluator implements CohortDefinitionEvaluator {
	
	private final Log log = LogFactory.getLog(this.getClass());
	
	@Autowired
	EvaluationService evaluationService;
	
	@Override
	public EvaluatedCohort evaluate(CohortDefinition cohortDefinition, EvaluationContext context) throws EvaluationException {
		
		CurrentlyOnPrepCohortDefinition definition = (CurrentlyOnPrepCohortDefinition) cohortDefinition;
		
		if (definition == null)
			return null;
		
		Cohort newCohort = new Cohort();
		
		String qry = "select a.patient_id\n"
		        + "from (select e.patient_id,\n"
		        + "max(e.visit_date) as latest_enrollment_date,\n"
		        + "f.latest_fup_date,\n"
		        + "greatest(ifnull(f.latest_fup_app_date,'0000-00-00'),ifnull(latest_refill_app_date,'0000-00-00')) as latest_appointment_date,\n"
		        + "greatest(ifnull(latest_fup_date,'0000-00-00'),ifnull(latest_refill_visit_date,'0000-00-00')) as latest_visit_date,\n"
		        + "r.latest_refill_visit_date,\n" + "f.latest_fup_app_date,\n" + "r.latest_refill_app_date,\n"
		        + "d.latest_disc_date,\n" + "d.disc_patient\n" + "from kenyaemr_etl.etl_prep_enrolment e\n" + "left join\n"
		        + "(select f.patient_id,\n"
		        + " max(f.visit_date)                                      as latest_fup_date,\n"
		        + " mid(max(concat(f.visit_date, f.appointment_date)), 11) as latest_fup_app_date\n"
		        + " from kenyaemr_etl.etl_prep_followup f\n" + " where f.visit_date <= date(:endDate)\n"
		        + " group by f.patient_id) f on e.patient_id = f.patient_id\n" + "left join (select r.patient_id,\n"
		        + " max(r.visit_date)                                      as latest_refill_visit_date,\n"
		        + " mid(max(concat(r.visit_date, r.next_appointment)), 11) as latest_refill_app_date\n"
		        + " from kenyaemr_etl.etl_prep_monthly_refill r\n" + " where r.visit_date <= date(:endDate)\n"
		        + " group by r.patient_id) r on e.patient_id = r.patient_id\n"
		        + "left join (select patient_id as disc_patient,\n"
		        + "max(d.visit_date)                                        as latest_disc_date,\n"
		        + "mid(max(concat(d.visit_date, d.discontinue_reason)), 11) as latest_disc_reason\n"
		        + " from kenyaemr_etl.etl_prep_discontinuation d\n" + " where d.visit_date <= date(:endDate)\n"
		        + " group by patient_id\n"
		        + " having latest_disc_date <= date(:endDate)) d on e.patient_id = d.disc_patient\n"
		        + "group by e.patient_id having\n"
		        + "timestampdiff(DAY, date(latest_appointment_date), date(:endDate)) <= 7\n"
		        + "and date(latest_appointment_date) >= date(latest_visit_date)\n"
		        + "and ((latest_enrollment_date >= d.latest_disc_date\n"
		        + "and latest_appointment_date > d.latest_disc_date)\n" + " or d.disc_patient is null)\n" + " ) a;";
		
		SqlQueryBuilder builder = new SqlQueryBuilder();
		builder.append(qry);
		Date startDate = (Date) context.getParameterValue("startDate");
		Date endDate = (Date) context.getParameterValue("endDate");
		builder.addParameter("startDate", startDate);
		builder.addParameter("endDate", endDate);
		
		List<Integer> ptIds = evaluationService.evaluateToList(builder, Integer.class, context);
		newCohort.setMemberIds(new HashSet<Integer>(ptIds));
		return new EvaluatedCohort(newCohort, definition, context);
	}
}
