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
		
		String qry = "select  m.patient_id,m.latest_tca  from (\n"
		        + "         select f.patient_id, latest_vis_date, latest_tca, date_discontinued, disc_patient   from (\n"
		        + "         select t.patient_id,    max(t.visit_date) as visit_date,    mid(max(concat(t.visit_date, t.latest_tca)), 11) as latest_tca,\n"
		        + "          max(t.visit_date) as latest_vis_date,    d.patient_id      as disc_patient,\n"
		        + "          d.disc_date as date_discontinued    from (\n"
		        + "           (select patient_id, max(visit_date) as visit_date, mid(max(concat(visit_date, appointment_date)), 11) as latest_tca\n"
		        + "             from kenyaemr_etl.etl_prep_followup group by   patient_id  )     union     (\n"
		        + "            select patient_id, max(visit_date), mid(max(concat(visit_date, next_appointment)), 11) as latest_tca\n"
		        + "             from kenyaemr_etl.etl_prep_monthly_refill group by patient_id         )      ) t\n"
		        + "          left outer JOIN     (select patient_id, max(visit_date) as disc_date\n"
		        + "             from kenyaemr_etl.etl_prep_discontinuation       where date(visit_date) <= curdate()\n"
		        + "             group by patient_id\n"
		        + "            ) d on d.patient_id = t.patient_id      group by t.patient_id ) f\n"
		        + "       JOIN kenyaemr_etl.etl_patient_demographics pd ON f.patient_id = pd.patient_id\n"
		        + "       JOIN kenyaemr_etl.etl_prep_enrolment e on f.patient_id = e.patient_id\n"
		        + "       where latest_vis_date <= curdate() GROUP BY f.patient_id having (\n"
		        + "        (((timestampdiff(DAY, date(latest_tca), date(curdate())) >= 7) and  (date(latest_vis_date) < date(latest_tca))))\n"
		        + "         and ((date(latest_tca) > date(date_discontinued)\n"
		        + "         and date(latest_vis_date) > date(date_discontinued)) or disc_patient is null) )) m;";
		
		SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
		queryBuilder.append(qry);
		Date endDate = (Date) context.getParameterValue("endDate");
		queryBuilder.addParameter("endDate", endDate);
		Map<Integer, Object> data = evaluationService.evaluateToMap(queryBuilder, Integer.class, Object.class, context);
		c.setData(data);
		return c;
	}
}
