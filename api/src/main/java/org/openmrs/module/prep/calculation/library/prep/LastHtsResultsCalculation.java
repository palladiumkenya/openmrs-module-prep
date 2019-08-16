/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.prep.calculation.library.prep;

import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.kenyacore.calculation.AbstractPatientCalculation;
import org.openmrs.module.kenyacore.calculation.Calculations;

import java.util.Collection;
import java.util.Map;

/**
 * Calculates the last date when cd4 count/cd4 % was taken
 */
public class LastHtsResultsCalculation extends AbstractPatientCalculation {
	
	@Override
	public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> parameterValues,
	        PatientCalculationContext context) {
		Concept htsResult = Context.getConceptService().getConcept(159427);
		CalculationResultMap currentHtsRes = Calculations.lastObs(htsResult, cohort, context);
		
		CalculationResultMap result = new CalculationResultMap();
		for (Integer ptId : cohort) {
			Obs htsResultObs = EmrCalculationUtils.obsResultForPatient(currentHtsRes, ptId);
			result.put(ptId, new SimpleResult(htsResultObs, this));
		}
		return result;
	}
}
