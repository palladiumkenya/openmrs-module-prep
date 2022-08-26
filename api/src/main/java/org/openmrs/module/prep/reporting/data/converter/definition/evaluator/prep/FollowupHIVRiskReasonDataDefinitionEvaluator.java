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
import org.openmrs.module.prep.reporting.data.converter.definition.prep.FollowupHIVRiskDataDefinition;
import org.openmrs.module.prep.reporting.data.converter.definition.prep.FollowupHIVRiskReasonDataDefinition;
import org.openmrs.module.reporting.data.encounter.EvaluatedEncounterData;
import org.openmrs.module.reporting.data.encounter.definition.EncounterDataDefinition;
import org.openmrs.module.reporting.data.encounter.evaluator.EncounterDataEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Evaluates Encounter DataDefinition
 */
@Handler(supports = FollowupHIVRiskReasonDataDefinition.class, order = 50)
public class FollowupHIVRiskReasonDataDefinitionEvaluator implements EncounterDataEvaluator {
	
	@Autowired
	private EvaluationService evaluationService;
	
	public EvaluatedEncounterData evaluate(EncounterDataDefinition definition, EvaluationContext context)
	        throws EvaluationException {
		EvaluatedEncounterData c = new EvaluatedEncounterData(definition, context);
		
		String qry = "select pf.encounter_id,\n"
		        + "concat_ws(',',(case ba.sexual_partner_hiv_status when 'HIV Positive' then 'Partner of Known HIV+ status'\n"
		        + "                                   when 'Unknown' then 'Partner of Unknown HIV status' else '' end),\n"
		        + "              (case ba.sex_with_multiple_partners when 'Yes' then 'Multiple Partners' else '' end),\n"
		        + "    (case ba.ipv_gbv when 'Yes' then 'Ongoing IPV/GBV' else '' end),\n"
		        + "    (case ba.transactional_sex when 'Yes' then 'Engaging in transactional sex' else '' end),\n"
		        + "    (case ba.recent_sti_infected when 'Yes' then 'Recent STI in the past 6 months' else '' end),\n"
		        + "    (case ba.recurrent_pep_use when 'Yes' then 'Recurrent use of PEP' else '' end),\n"
		        + "    (case ba.recurrent_sex_under_influence when 'Yes' then 'Sex Under Influence' else '' end),\n"
		        + "    (case ba.inconsistent_no_condom_use when 'Yes' then 'Inconsistent or no condom use' else '' end),\n"
		        + "    (case ba.sharing_drug_needles when 'Yes' then 'Sharing drug needles' else '' end),\n"
		        + "    (case ba.other_reasons when 'Yes' then 'Other reasons' else '' end)) as risk_reason\n"
		        + "from kenyaemr_etl.etl_prep_behaviour_risk_assessment ba\n"
		        + "inner join kenyaemr_etl.etl_prep_followup pf on pf.patient_id = ba.patient_id and pf.visit_date = ba.visit_date;";
		
		SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
		queryBuilder.append(qry);
		Map<Integer, Object> data = evaluationService.evaluateToMap(queryBuilder, Integer.class, Object.class, context);
		c.setData(data);
		return c;
	}
}
