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

import org.apache.commons.lang.time.DateUtils;
import org.openmrs.*;
import org.openmrs.api.EncounterService;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.kenyacore.calculation.AbstractPatientCalculation;
import org.openmrs.module.kenyacore.calculation.BooleanResult;
import org.openmrs.module.kenyacore.calculation.Calculations;
import org.openmrs.module.kenyaemr.util.EmrUtils;
import org.openmrs.module.prep.metadata.PrepMetadata;

import java.util.*;

/**
 * Checks if a patient is negative, not enrolled and no initial hts encounter
 */
public class PatientsEligibleForPrepMonthlyRefillCalculation extends AbstractPatientCalculation {
	
	public static Encounter previousEncounter(Patient patient, EncounterType type, Form form) {
		List<Encounter> encounters = Context.getEncounterService().getEncounters(patient, null, null, null,
		    Collections.singleton(form), Collections.singleton(type), null, null, null, false);
		return encounters.size() > 1 ? encounters.get(encounters.size() - 2) : null;
	}
	
	@Override
	public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> parameterValues,
	        PatientCalculationContext context) {
		
		EncounterService encounterService = Context.getEncounterService();
		PatientService patientService = Context.getPatientService();
		;
		Concept restart = Context.getConceptService().getConcept(165109);
		Concept restartCon = Context.getConceptService().getConcept(162904);
		Integer setRestartWhenFollowup = 0;
		Concept nextAppointment = Context.getConceptService().getConcept(5096);
		CalculationResultMap nextAppointmentLastObs = Calculations.lastObs(nextAppointment, cohort, context);
		
		CalculationResultMap ret = new CalculationResultMap();
		for (Integer ptId : cohort) {
			Patient patient = patientService.getPatient(ptId);
			boolean showMonthlyRefillForm = false;
			Integer firstMonthlyRefill = 0;
			
			Encounter firstMonthlyEncounter = EmrUtils.firstEncounter(patient, Context.getEncounterService()
			        .getEncounterTypeByUuid(PrepMetadata._EncounterType.PREP_MONTHLY_REFILL));
			if (firstMonthlyEncounter != null) {
				firstMonthlyRefill = 1;
			} else {
				firstMonthlyRefill = 0;
			}
			
			//Handle previous encounter
			
			Encounter checkPrevious = previousEncounter(patient,
			    Context.getEncounterService().getEncounterTypeByUuid(PrepMetadata._EncounterType.PREP_MONTHLY_REFILL),
			    Context.getFormService().getFormByUuid(PrepMetadata._Form.PREP_MONTHLY_REFILL_FORM));
			
			Encounter checkPreviousFollowup = previousEncounter(patient, Context.getEncounterService()
			        .getEncounterTypeByUuid(PrepMetadata._EncounterType.PREP_CONSULTATION), Context.getFormService()
			        .getFormByUuid(PrepMetadata._Form.PREP_CONSULTATION_FORM));
			
			Encounter lastMonthlyRefillEncounter = EmrUtils.lastEncounter(patient, Context.getEncounterService()
			        .getEncounterTypeByUuid(PrepMetadata._EncounterType.PREP_MONTHLY_REFILL), Context.getFormService()
			        .getFormByUuid(PrepMetadata._Form.PREP_MONTHLY_REFILL_FORM));
			
			Encounter lastFollowUp = EmrUtils.lastEncounter(patient,
			    Context.getEncounterService().getEncounterTypeByUuid(PrepMetadata._EncounterType.PREP_CONSULTATION), Context
			            .getFormService().getFormByUuid(PrepMetadata._Form.PREP_CONSULTATION_FORM));
			if (lastFollowUp != null) {
				for (Obs o : lastFollowUp.getObs()) {
					if (o.getConcept().getConceptId() == 165109) {
						CalculationResultMap restarts = Calculations.lastObs(restart, cohort, context);
						Obs restartValue = EmrCalculationUtils.obsResultForPatient(restarts, ptId);
						if (restartValue != null) {
							if (restartValue.getValueCoded() == restartCon) {
								setRestartWhenFollowup = 1;
							} else {
								setRestartWhenFollowup = 0;
							}
							
						}
					}
					
				}
			}
			int diffBetweenFollowupEncounters = 0;
			if (lastFollowUp != null && checkPrevious != null) {
				diffBetweenFollowupEncounters = (lastFollowUp.getEncounterDatetime().getMonth() - checkPreviousFollowup
				        .getEncounterDatetime().getMonth())
				        + ((lastFollowUp.getEncounterDatetime().getYear() - checkPreviousFollowup.getEncounterDatetime()
				                .getYear()) * 12);
			}
			Date today = new Date();
			Integer missAppointmentBySevenDays = 0;
			
			Obs nextAppointmentObs = EmrCalculationUtils.obsResultForPatient(nextAppointmentLastObs, ptId);
			if (nextAppointmentObs != null) {
				if (today.before(DateUtils.addDays(nextAppointmentObs.getValueDate(), 7))) {
					missAppointmentBySevenDays = 0;
				} else {}
				missAppointmentBySevenDays = 1;
			}
			
			List<Encounter> followupEncounters = encounterService.getEncounters(
			    Context.getPatientService().getPatient(ptId),
			    null,
			    null,
			    null,
			    null,
			    Arrays.asList(Context.getEncounterService().getEncounterTypeByUuid(
			        PrepMetadata._EncounterType.PREP_CONSULTATION)), null, null, null, false);
			
			List<Encounter> enrollmentEncounters = encounterService.getEncounters(
			    Context.getPatientService().getPatient(ptId),
			    null,
			    null,
			    null,
			    null,
			    Arrays.asList(Context.getEncounterService().getEncounterTypeByUuid(
			        PrepMetadata._EncounterType.PREP_ENROLLMENT)), null, null, null, false);
			
			int diffBetweenMonthlyRefillEncounters = 0;
			if (lastMonthlyRefillEncounter != null && checkPrevious != null) {
				diffBetweenMonthlyRefillEncounters = (lastMonthlyRefillEncounter.getEncounterDatetime().getMonth() - checkPrevious
				        .getEncounterDatetime().getMonth())
				        + ((lastMonthlyRefillEncounter.getEncounterDatetime().getYear() - checkPrevious
				                .getEncounterDatetime().getYear()) * 12);
			}
			if (firstMonthlyEncounter != null && setRestartWhenFollowup == 0 && followupEncounters.size() >= 2
			        && (diffBetweenMonthlyRefillEncounters != 1 || diffBetweenMonthlyRefillEncounters > 1)) {
				showMonthlyRefillForm = true;
				
			}
			
			if (diffBetweenFollowupEncounters == 3) {
				showMonthlyRefillForm = true;
			}
			
			if (enrollmentEncounters.size() > 0 && followupEncounters.size() > 0 && setRestartWhenFollowup == 0
			        && missAppointmentBySevenDays == 0 && firstMonthlyRefill == 0 && lastFollowUp != null
			        && !DateUtils.isSameDay(lastFollowUp.getEncounterDatetime(), today)) {
				showMonthlyRefillForm = true;
			}
			
			ret.put(ptId, new BooleanResult(showMonthlyRefillForm, this));
		}
		return ret;
	}
	
}
