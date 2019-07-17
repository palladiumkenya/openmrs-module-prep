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
import org.openmrs.Patient;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.kenyacore.calculation.AbstractPatientCalculation;
import org.openmrs.module.kenyacore.calculation.Calculations;
import org.openmrs.module.kenyacore.calculation.BooleanResult;
import org.openmrs.module.kenyacore.calculation.Filters;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Calculates whether patients are eligible for the Prep program
 */
public class EligibleForPrepProgramCalculation extends AbstractPatientCalculation {
	
	/**
	 * @see org.openmrs.calculation.patient.PatientCalculation#evaluate(Collection, Map,
	 *      org.openmrs.calculation.patient.PatientCalculationContext)
	 */
	@Override
	public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> params,
	        PatientCalculationContext context) {
		PatientService patientService = Context.getPatientService();
		Concept weight = Context.getConceptService().getConcept(5089);
		Concept htsTestResults = Context.getConceptService().getConcept(159427);
		Concept willingToTakePrep = Context.getConceptService().getConcept(165094);
		
		CalculationResultMap currentWeight = Calculations.lastObs(weight, cohort, context);
		CalculationResultMap currentTestResults = Calculations.lastObs(htsTestResults, cohort, context);
		CalculationResultMap currentWillingTotakePrep = Calculations.lastObs(willingToTakePrep, cohort, context);
		
		CalculationResultMap ret = new CalculationResultMap();
		Set<Integer> alive = Filters.alive(cohort, context);
		
		for (int ptId : cohort) {
			Patient patient = patientService.getPatient(ptId);
			boolean enrollPatientOnPrep = false;
			boolean eligible = alive.contains(ptId);
			
			// check client weight is over 35Kg,willing to take PreP and hiv test result is negative
			
			Obs weightCurrentObs = EmrCalculationUtils.obsResultForPatient(currentWeight, ptId);
			Obs testResultsCurrentObs = EmrCalculationUtils.obsResultForPatient(currentTestResults, ptId);
			Obs willingForPrepCurrentObs = EmrCalculationUtils.obsResultForPatient(currentWillingTotakePrep, ptId);
			
			if (weightCurrentObs != null && testResultsCurrentObs != null && willingForPrepCurrentObs != null) {
				if (eligible && patient.getAge() >= 15 && weightCurrentObs.getValueNumeric().intValue() >= 35
				        && testResultsCurrentObs.getValueCoded().getConceptId().equals(664)
				        && willingForPrepCurrentObs.getValueCoded().getConceptId().equals(1065)) {
					enrollPatientOnPrep = true;
				}
			}
			
			ret.put(ptId, new BooleanResult(enrollPatientOnPrep, this, context));
			
		}
		
		return ret;
	}
	
}
