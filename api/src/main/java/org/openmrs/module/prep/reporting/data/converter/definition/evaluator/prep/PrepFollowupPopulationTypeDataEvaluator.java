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
import org.openmrs.module.prep.reporting.data.converter.definition.prep.PrepFollowUpPopulatonTypeDataDefinition;
import org.openmrs.module.reporting.data.encounter.EvaluatedEncounterData;
import org.openmrs.module.reporting.data.encounter.definition.EncounterDataDefinition;
import org.openmrs.module.reporting.data.encounter.evaluator.EncounterDataEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Map;

/**
 * Evaluates Population type Data Definition from Prep enrollment
 */
@Handler(supports = PrepFollowUpPopulatonTypeDataDefinition.class, order = 50)
public class PrepFollowupPopulationTypeDataEvaluator implements EncounterDataEvaluator {
	
	@Autowired
	private EvaluationService evaluationService;
	
	public EvaluatedEncounterData evaluate(EncounterDataDefinition definition, EvaluationContext context)
	        throws EvaluationException {
		EvaluatedEncounterData c = new EvaluatedEncounterData(definition, context);
		
		String qry = "select v.encounter_id,\n" + "ifnull(mid(max(concat(e.visit_date, (case e.kp_type\n"
		        + "        when 105 then 'People who inject drugs'\n" + "        when 160666 then 'People who use drugs'\n"
		        + "        when 159674 then 'Fisher folk'\n" + "        when 162198 then 'Truck driver'\n"
		        + "        when 162277 then 'People in prison'\n" + "        when 160578 then 'Men who has sex with men'\n"
		        + "        when 165084 then 'Male sex worker'\n"
		        + "        when 160579 then 'Female sex Worker' end), '')), 11),\n"
		        + "mid(max(concat(e.visit_date, (case e.population_type\n"
		        + "        when 164928 then 'General Population'\n" + "        when 6096 then 'Discordant Couple'\n"
		        + "        when 164929 then 'Key Population' end), '')), 11)) as population_type\n"
		        + "from kenyaemr_etl.etl_prep_followup v\n"
		        + "inner join kenyaemr_etl.etl_prep_enrolment e on e.patient_id = v.patient_id\n"
		        + "where v.form = 'prep-consultation'\n" + "and v.visit_date between date(:startDate) and date(:endDate)\n"
		        + "group by encounter_id\n" + "UNION\n" + "select r.encounter_id,\n"
		        + "ifnull(mid(max(concat(e.visit_date, (case e.kp_type\n"
		        + "        when 105 then 'People who inject drugs'\n" + "        when 160666 then 'People who use drugs'\n"
		        + "        when 159674 then 'Fisher folk'\n" + "        when 162198 then 'Truck driver'\n"
		        + "        when 162277 then 'People in prison'\n" + "        when 160578 then 'Men who has sex with men'\n"
		        + "        when 165084 then 'Male sex worker'\n"
		        + "        when 160579 then 'Female sex Worker' end), '')), 11),\n"
		        + "mid(max(concat(e.visit_date, (case e.population_type\n"
		        + "        when 164928 then 'General Population'\n" + "        when 6096 then 'Discordant Couple'\n"
		        + "        when 164929 then 'Key Population' end), '')), 11)) as population_type\n"
		        + "from kenyaemr_etl.etl_prep_monthly_refill r\n"
		        + "inner join kenyaemr_etl.etl_prep_enrolment e on e.patient_id = r.patient_id\n"
		        + "where r.visit_date between date(:startDate) and date(:endDate)\n" + "group by encounter_id;";
		
		SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
		Date startDate = (Date) context.getParameterValue("startDate");
		Date endDate = (Date) context.getParameterValue("endDate");
		queryBuilder.addParameter("startDate", startDate);
		queryBuilder.addParameter("endDate", endDate);
		queryBuilder.append(qry);
		Map<Integer, Object> data = evaluationService.evaluateToMap(queryBuilder, Integer.class, Object.class, context);
		c.setData(data);
		return c;
	}
	
}
