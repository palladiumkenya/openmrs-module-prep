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
import org.openmrs.module.prep.reporting.data.converter.definition.prep.FollowupLinkedToCareDataDefinition;
import org.openmrs.module.prep.reporting.data.converter.definition.prep.LinkedToCareDataDefinition;
import org.openmrs.module.reporting.data.encounter.EvaluatedEncounterData;
import org.openmrs.module.reporting.data.encounter.definition.EncounterDataDefinition;
import org.openmrs.module.reporting.data.encounter.evaluator.EncounterDataEvaluator;
import org.openmrs.module.reporting.data.person.EvaluatedPersonData;
import org.openmrs.module.reporting.data.person.definition.PersonDataDefinition;
import org.openmrs.module.reporting.data.person.evaluator.PersonDataEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Evaluates EncounterDataDefinition
 */
@Handler(supports = FollowupLinkedToCareDataDefinition.class, order = 50)
public class FollowupLinkedToCareDataDefinitionEvaluator implements EncounterDataEvaluator {
	
	@Autowired
	private EvaluationService evaluationService;
	
	public EvaluatedEncounterData evaluate(EncounterDataDefinition definition, EvaluationContext context)
	        throws EvaluationException {
		EvaluatedEncounterData c = new EvaluatedEncounterData(definition, context);
		String qry = "select e.encounter_id,if((pd.discontine_reason = 'HIV test is positive' or mr.prep_discontinue_reasons = 'HIV test is positive' or ht.final_test_result='Positive') and (he.patient_id = e.patient_id or de.unique_patient_no is not null),concat_ws('\\\\r\\\\n','Yes',de.unique_patient_no),'No') as linkage_to_ccc\n"
		        + "from kenyaemr_etl.etl_prep_followup e\n"
		        + "left outer join (select d.patient_id as patient_id,d.discontinue_reason as discontine_reason from kenyaemr_etl.etl_prep_discontinuation d group by d.patient_id)pd on pd.patient_id = e.patient_id\n"
		        + "inner join kenyaemr_etl.etl_patient_demographics de on e.patient_id= de.patient_id\n"
		        + "left outer join (select  e.patient_id as patient_id from kenyaemr_etl.etl_hiv_enrollment e group by e.patient_id ) he on he.patient_id = e.patient_id\n"
		        + "left outer join (select r.patient_id as patient_id,r.prep_discontinue_reasons as prep_discontinue_reasons from kenyaemr_etl.etl_prep_monthly_refill r group by r.patient_id )mr on mr.patient_id = e.patient_id\n"
		        + "left outer join (select t.patient_id as patient_id,t.final_test_result from kenyaemr_etl.etl_hts_test t  group by t.patient_id) ht on ht.patient_id = e.patient_id group by e.patient_id;\n";
		SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
		queryBuilder.append(qry);
		Map<Integer, Object> data = evaluationService.evaluateToMap(queryBuilder, Integer.class, Object.class, context);
		c.setData(data);
		return c;
	}
}
