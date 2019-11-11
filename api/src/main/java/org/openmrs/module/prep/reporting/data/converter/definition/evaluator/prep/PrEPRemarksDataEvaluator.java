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
import org.openmrs.module.prep.reporting.data.converter.definition.prep.PrEPRemarksDataDefinition;
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
@Handler(supports = PrEPRemarksDataDefinition.class, order = 50)
public class PrEPRemarksDataEvaluator implements PersonDataEvaluator {
	
	@Autowired
	private EvaluationService evaluationService;
	
	public EvaluatedPersonData evaluate(PersonDataDefinition definition, EvaluationContext context)
	        throws EvaluationException {
		EvaluatedPersonData c = new EvaluatedPersonData(definition, context);
		
		String qry = "select e.patient_id, mid(greatest(ifnull(pf.max_cn,''),ifnull(mr.max_rem,''),ifnull(pn.max_notes,'')),11) as remarks\n"
		        + "from kenyaemr_etl.etl_prep_enrolment e\n"
		        + "       left outer join (select f.patient_id,f.visit_date,max(concat(f.visit_date, f.clinical_notes)) as max_cn from kenyaemr_etl.etl_prep_followup f group by patient_id) pf on e.patient_id =pf.patient_id\n"
		        + "       left outer join (select r.patient_id,r.visit_date,r.remarks, max(concat(r.visit_date, r.remarks)) as max_rem from kenyaemr_etl.etl_prep_monthly_refill r group by patient_id) mr on e.patient_id = mr.patient_id\n"
		        + "       left outer join (select n.patient_id,n.visit_date,n.notes,max(concat(n.visit_date, n.notes)) as max_notes from kenyaemr_etl.etl_progress_note n group by n.patient_id) pn on e.patient_id = pn.patient_id\n"
		        + "group by e.patient_id;";
		
		SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
		queryBuilder.append(qry);
		Map<Integer, Object> data = evaluationService.evaluateToMap(queryBuilder, Integer.class, Object.class, context);
		c.setData(data);
		return c;
	}
}
