/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.prep.reporting.cohort.definition.evaluator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.annotation.Handler;
import org.openmrs.module.prep.reporting.cohort.definition.PrEPFollowupRegisterCohortDefinition;
import org.openmrs.module.reporting.common.ObjectUtil;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.openmrs.module.reporting.query.encounter.EncounterQueryResult;
import org.openmrs.module.reporting.query.encounter.definition.EncounterQuery;
import org.openmrs.module.reporting.query.encounter.evaluator.EncounterQueryEvaluator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * Evaluator for PrEP Followup Register Cohort
 */
@Handler(supports = { PrEPFollowupRegisterCohortDefinition.class })
public class PrEPFollowupRegisterCohortDefinitionEvaluator implements EncounterQueryEvaluator {
	
	private final Log log = LogFactory.getLog(this.getClass());
	
	@Autowired
	EvaluationService evaluationService;
	
	public EncounterQueryResult evaluate(EncounterQuery definition, EvaluationContext context) throws EvaluationException {
		context = ObjectUtil.nvl(context, new EvaluationContext());
		EncounterQueryResult queryResult = new EncounterQueryResult(definition, context);
		
		String qry = "select v.encounter_id,\n" + "coalesce(mid(max(concat(e.visit_date, (case e.kp_type\n"
		        + "          when 105 then 'People who inject drugs'\n"
		        + "          when 160666 then 'People who use drugs'\n" + "          when 159674 then 'Fisher folk'\n"
		        + "          when 162198 then 'Truck driver'\n" + "          when 162277 then 'People in prison'\n"
		        + "          when 160578 then 'Men who has sex with men'\n"
		        + "          when 165084 then 'Male sex worker'\n"
		        + "          when 160579 then 'Female sex Worker' end), null)), 11),\n"
		        + "mid(max(concat(e.visit_date, (case e.population_type\n"
		        + "          when 164928 then 'General Population'\n" + "          when 6096 then 'Discordant Couple'\n"
		        + "          when 164929 then 'Key Population' end), '')), 11)) as population_type\n"
		        + "from kenyaemr_etl.etl_prep_followup v\n"
		        + "inner join kenyaemr_etl.etl_prep_enrolment e on e.patient_id = v.patient_id\n"
		        + "where v.form = 'prep-consultation'\n" + "and v.visit_date between date(:startDate) and date(:endDate)\n"
		        + "group by encounter_id\n" + "UNION\n" + "select r.encounter_id,\n"
		        + "coalesce(mid(max(concat(e.visit_date, (case e.kp_type\n"
		        + "          when 105 then 'People who inject drugs'\n"
		        + "          when 160666 then 'People who use drugs'\n" + "          when 159674 then 'Fisher folk'\n"
		        + "          when 162198 then 'Truck driver'\n" + "          when 162277 then 'People in prison'\n"
		        + "          when 160578 then 'Men who has sex with men'\n"
		        + "          when 165084 then 'Male sex worker'\n"
		        + "          when 160579 then 'Female sex Worker' end), null)), 11),\n"
		        + "mid(max(concat(e.visit_date, (case e.population_type\n"
		        + "          when 164928 then 'General Population'\n" + "          when 6096 then 'Discordant Couple'\n"
		        + "          when 164929 then 'Key Population' end), '')), 11)) as population_type\n"
		        + "from kenyaemr_etl.etl_prep_monthly_refill r\n"
		        + "inner join kenyaemr_etl.etl_prep_enrolment e on e.patient_id = r.patient_id\n"
		        + "where r.visit_date between date(:startDate) and date(:endDate)\n" + "group by encounter_id;";
		
		SqlQueryBuilder builder = new SqlQueryBuilder();
		builder.append(qry);
		Date startDate = (Date) context.getParameterValue("startDate");
		Date endDate = (Date) context.getParameterValue("endDate");
		builder.addParameter("endDate", endDate);
		builder.addParameter("startDate", startDate);
		
		List<Integer> results = evaluationService.evaluateToList(builder, Integer.class, context);
		queryResult.getMemberIds().addAll(results);
		return queryResult;
	}
}
