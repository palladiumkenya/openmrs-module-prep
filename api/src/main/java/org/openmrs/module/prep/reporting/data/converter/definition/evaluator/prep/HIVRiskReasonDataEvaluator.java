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
import org.openmrs.module.prep.reporting.data.converter.definition.prep.HIVRiskReasonDataDefinition;
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
 * Evaluates PersonDataDefinition
 */
@Handler(supports = HIVRiskReasonDataDefinition.class, order = 50)
public class HIVRiskReasonDataEvaluator implements PersonDataEvaluator {
	
	@Autowired
	private EvaluationService evaluationService;
	
	public EvaluatedPersonData evaluate(PersonDataDefinition definition, EvaluationContext context)
	        throws EvaluationException {
		EvaluatedPersonData c = new EvaluatedPersonData(definition, context);
		
		String qry = "select h.patient_id,concat_ws('\\r\\n',h.assessment_outcome,(concat_ws(',',COALESCE(if(sexual_partner_hiv_status='HIV Positive',1,null)),COALESCE(if(sex_with_multiple_partners='Yes',9,null)),\n"
		        + "                              COALESCE(if(transactional_sex='Yes',2,null)),COALESCE(if(recent_sti_infected='Yes',3,null)),\n"
		        + "                              COALESCE(if(recurrent_pep_use='Yes',4,null)),COALESCE(if(recurrent_sex_under_influence='Yes',5,null)),\n"
		        + "                              COALESCE(if(inconsistent_no_condom_use='Yes',6,null)),COALESCE(if(sharing_drug_needles='Yes',7,null)),COALESCE(if(ipv_gbv='Yes',10,null))))) as reason_for_eligibility\n"
		        + "from kenyaemr_etl.etl_prep_behaviour_risk_assessment h;";
		
		SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
		queryBuilder.append(qry);
		Map<Integer, Object> data = evaluationService.evaluateToMap(queryBuilder, Integer.class, Object.class, context);
		c.setData(data);
		return c;
	}
}
