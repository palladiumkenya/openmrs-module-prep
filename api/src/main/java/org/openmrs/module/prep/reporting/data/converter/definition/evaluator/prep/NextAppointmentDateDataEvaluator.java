/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.prep.reporting.data.converter.definition.evaluator.prep;

import org.openmrs.annotation.Handler;
import org.openmrs.module.prep.reporting.data.converter.definition.prep.NextAppointmentDateDataDefinition;
import org.openmrs.module.reporting.data.person.EvaluatedPersonData;
import org.openmrs.module.reporting.data.person.definition.PersonDataDefinition;
import org.openmrs.module.reporting.data.person.evaluator.PersonDataEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Map;

/**
 * Evaluates Next appointment Date Data Definition
 */
@Handler(supports = NextAppointmentDateDataDefinition.class, order = 50)
public class NextAppointmentDateDataEvaluator implements PersonDataEvaluator {
	
	@Autowired
	private EvaluationService evaluationService;
	
	public EvaluatedPersonData evaluate(PersonDataDefinition definition, EvaluationContext context)
	        throws EvaluationException {
		EvaluatedPersonData c = new EvaluatedPersonData(definition, context);
		
		String qry = "select e.patient_id,\n"
		        + "       greatest(f.next_clinical_appointment, r.next_refill_appointment) as visit_date\n"
		        + "from kenyaemr_etl.etl_prep_enrolment e\n"
		        + "         left join (select f.patient_id,\n"
		        + "                           mid(max(concat(date(f.visit_date), date(f.appointment_date))),\n"
		        + "                               11) as next_clinical_appointment\n"
		        + "                    from kenyaemr_etl.etl_prep_followup f\n"
		        + "                    where date(f.visit_date) <= date(:endDate)\n"
		        + "                    group by f.patient_id) f on e.patient_id = f.patient_id\n"
		        + "         left join (select r.patient_id,\n"
		        + "                           mid(max(concat(date(r.visit_date), date(next_appointment))), 11) as next_refill_appointment\n"
		        + "                    from kenyaemr_etl.etl_prep_monthly_refill r\n"
		        + "                    where date(r.visit_date) <= date(:endDate)\n"
		        + "                    group by r.patient_id) r on e.patient_id = r.patient_id\n"
		        + "where date(e.visit_date) <= date(:endDate)\n" + "group by e.patient_id;";
		
		SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
		queryBuilder.append(qry);
		Date endDate = (Date) context.getParameterValue("endDate");
		queryBuilder.addParameter("endDate", endDate);
		Map<Integer, Object> data = evaluationService.evaluateToMap(queryBuilder, Integer.class, Object.class, context);
		c.setData(data);
		return c;
	}
}
