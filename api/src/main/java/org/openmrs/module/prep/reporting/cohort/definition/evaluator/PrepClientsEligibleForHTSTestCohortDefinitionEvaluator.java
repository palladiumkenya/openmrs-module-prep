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
import org.openmrs.module.prep.reporting.cohort.definition.PrepClientsEligibleForHTSTestCohortDefinition;
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
 * Evaluator for PrepClientsEligibleForHTSTestCohortDefinition
 */
@Handler(supports = { PrepClientsEligibleForHTSTestCohortDefinition.class })
public class PrepClientsEligibleForHTSTestCohortDefinitionEvaluator implements CohortDefinitionEvaluator {
	
	private final Log log = LogFactory.getLog(this.getClass());
	
	@Autowired
	EvaluationService evaluationService;
	
	@Override
	public EvaluatedCohort evaluate(CohortDefinition cohortDefinition, EvaluationContext context) throws EvaluationException {
		
		PrepClientsEligibleForHTSTestCohortDefinition definition = (PrepClientsEligibleForHTSTestCohortDefinition) cohortDefinition;
		
		if (definition == null)
			return null;
		
		Cohort newCohort = new Cohort();
		
		String qry = "select e.patient_id\n"
		        + "from (\n"
		        + "       select enr.patient_id,\n"
		        + "              d.patient_id as disc_patient ,\n"
		        + "              max(d.visit_date) as date_discontinued,\n"
		        + "              max(enr.visit_date) as enrollment_date,\n"
		        + "              max(t.visit_date) as latest_test_date,\n"
		        + "              mid(max(concat(t.visit_date,t.final_test_result)),11) as hiv_status\n"
		        + "       from kenyaemr_etl.etl_prep_enrolment enr\n"
		        + "         join kenyaemr_etl.etl_patient_demographics p on p.patient_id=enr.patient_id and p.voided=0 and p.dead=0\n"
		        + "         join kenyaemr_etl.etl_hts_test t on t.patient_id=enr.patient_id\n"
		        + "         left join  (select patient_id,visit_date from kenyaemr_etl.etl_prep_discontinuation) d on d.patient_id = enr.patient_id\n"
		        + "         group by patient_id\n"
		        + "         having (hiv_status = \"Negative\")\n"
		        + "                  and ((timestampdiff(MONTH,date(latest_test_date),date(:endDate)) > 3)\n"
		        + "                       or ((enrollment_date between date_sub(:endDate, interval 1 MONTH) and date(:endDate))\n"
		        + "                              and timestampdiff(MONTH,date(latest_test_date),date(:endDate)) > 1))\n"
		        + "                  and\n"
		        + "                   (disc_patient is null or date(enrollment_date) >= date(date_discontinued) ) )e;\n";
		
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
