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

import org.openmrs.Encounter;
import org.openmrs.Form;
import org.openmrs.Patient;
import org.openmrs.api.EncounterService;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.kenyacore.calculation.AbstractPatientCalculation;
import org.openmrs.module.kenyacore.calculation.BooleanResult;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.prep.metadata.PrepMetadata;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Checks if a patient is negative, not enrolled and no initial hts encounter
 */
public class PatientsEligibleForInitialFollowUpCalculation extends AbstractPatientCalculation {
	
	@Override
	public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> parameterValues,
	        PatientCalculationContext context) {
		
		EncounterService encounterService = Context.getEncounterService();
		PatientService patientService = Context.getPatientService();
		
		CalculationResultMap ret = new CalculationResultMap();
		for (Integer ptId : cohort) {
			Patient patient = patientService.getPatient(ptId);
			boolean noFollowUpHistory = false;
			List<Encounter> enrollmentEncounters = encounterService.getEncounters(
			    Context.getPatientService().getPatient(ptId),
			    null,
			    null,
			    null,
			    null,
			    Arrays.asList(Context.getEncounterService().getEncounterTypeByUuid(
			        PrepMetadata._EncounterType.PREP_ENROLLMENT)), null, null, null, false);
			
			List<Encounter> followupEncounters = encounterService.getEncounters(
			    Context.getPatientService().getPatient(ptId),
			    null,
			    null,
			    null,
			    null,
			    Arrays.asList(Context.getEncounterService().getEncounterTypeByUuid(
			        PrepMetadata._EncounterType.PREP_CONSULTATION)), null, null, null, false);
			List<Encounter> initialEncounters = encounterService.getEncounters(
			    Context.getPatientService().getPatient(ptId),
			    null,
			    null,
			    null,
			    null,
			    Arrays.asList(Context.getEncounterService().getEncounterTypeByUuid(
			        PrepMetadata._EncounterType.PREP_INITIAL_FOLLOWUP)), null, null, null, false);
			if (enrollmentEncounters.size() == 1 && followupEncounters.size() <= 0 && initialEncounters.size() < 1) {
				noFollowUpHistory = true;
			}
			
			ret.put(ptId, new BooleanResult(noFollowUpHistory, this));
		}
		return ret;
	}
	
}
