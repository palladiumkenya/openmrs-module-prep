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

import org.apache.commons.lang.time.DateUtils;

/**
 * Checks if a patient is negative, not enrolled and no initial hts encounter
 */
public class PatientsEligibleForPrepFollowUpCalculation extends AbstractPatientCalculation {
	
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
		Concept nextAppointment = Context.getConceptService().getConcept(5096);
		CalculationResultMap nextAppointmentLastObs = Calculations.lastObs(nextAppointment, cohort, context);
		Concept restart = Context.getConceptService().getConcept(165109);
		Concept restartCon = Context.getConceptService().getConcept(162904);
		Integer checklastFollowHasRestart = 0;
		
		CalculationResultMap ret = new CalculationResultMap();
		for (Integer ptId : cohort) {
			Patient patient = patientService.getPatient(ptId);
			boolean showFollowUpForm = false;
			Date today = new Date();
			Obs nextAppointmentObs = EmrCalculationUtils.obsResultForPatient(nextAppointmentLastObs, ptId);
			if (nextAppointmentObs != null) {
				Date missAppointmentBySevenDays = DateUtils.addDays(nextAppointmentObs.getValueDate(), 7);
				if (today.after(missAppointmentBySevenDays)) {
					showFollowUpForm = true;
				}
			}
			
			// last followup encounter
			Encounter lastFollowUp = EmrUtils.lastEncounter(patient,
			    Context.getEncounterService().getEncounterTypeByUuid(PrepMetadata._EncounterType.PREP_CONSULTATION), Context
			            .getFormService().getFormByUuid(PrepMetadata._Form.PREP_CONSULTATION_FORM));
			if (lastFollowUp != null) {
				for (Obs o : lastFollowUp.getObs()) {
					if (o.getConcept().getConceptId() == 165109) {
						CalculationResultMap restarts = Calculations.lastObs(restart, cohort, context);
						Obs restartValue = EmrCalculationUtils.obsResultForPatient(restarts, ptId);
						if (restartValue != null && restartValue.getValueCoded() == restartCon) {
							showFollowUpForm = true;
							//	checklastFollowHasRestart = 0;
						} else {
							checklastFollowHasRestart = 0;
						}
					}
					
				}
			}
			
			Encounter checkPrevious = previousEncounter(patient,
			    Context.getEncounterService().getEncounterTypeByUuid(PrepMetadata._EncounterType.PREP_MONTHLY_REFILL),
			    Context.getFormService().getFormByUuid(PrepMetadata._Form.PREP_MONTHLY_REFILL_FORM));
			
			Encounter lastMonthlyRefillEncounter = EmrUtils.lastEncounter(patient, Context.getEncounterService()
			        .getEncounterTypeByUuid(PrepMetadata._EncounterType.PREP_MONTHLY_REFILL), Context.getFormService()
			        .getFormByUuid(PrepMetadata._Form.PREP_MONTHLY_REFILL_FORM));
			int diff = 0;
			if (lastMonthlyRefillEncounter != null && checkPrevious != null) {
				diff = (lastMonthlyRefillEncounter.getEncounterDatetime().getMonth() - checkPrevious.getEncounterDatetime()
				        .getMonth())
				        + ((lastMonthlyRefillEncounter.getEncounterDatetime().getYear() - checkPrevious
				                .getEncounterDatetime().getYear()) * 12);
			}
			Encounter firstMonthlyEncounter = EmrUtils.firstEncounter(patient, Context.getEncounterService()
			        .getEncounterTypeByUuid(PrepMetadata._EncounterType.PREP_MONTHLY_REFILL));
			
			//	check there is follow up form
			List<Encounter> noFolloupEncounters = encounterService.getEncounters(Context.getPatientService()
			        .getPatient(ptId), null, null, null, null, Arrays.asList(Context.getEncounterService()
			        .getEncounterTypeByUuid(PrepMetadata._EncounterType.PREP_CONSULTATION)), null, null, null, false);
			
			List<Encounter> prepInitialEncounter = encounterService.getEncounters(
			    Context.getPatientService().getPatient(ptId),
			    null,
			    null,
			    null,
			    null,
			    Arrays.asList(Context.getEncounterService().getEncounterTypeByUuid(
			        PrepMetadata._EncounterType.PREP_INITIAL_FOLLOWUP)), null, null, null, false);
			
			if (firstMonthlyEncounter != null && noFolloupEncounters.size() < 2) {
				showFollowUpForm = true;
			}
			
			if (diff == 1) {
				showFollowUpForm = true;
			}
			List<Encounter> enrollmentEncounters = encounterService.getEncounters(
			    Context.getPatientService().getPatient(ptId),
			    null,
			    null,
			    null,
			    null,
			    Arrays.asList(Context.getEncounterService().getEncounterTypeByUuid(
			        PrepMetadata._EncounterType.PREP_ENROLLMENT)), null, null, null, false);
			//&& checklastFollowHasRestart == 0
			if (enrollmentEncounters.size() > 0 && noFolloupEncounters.size() == 0 && prepInitialEncounter.size() > 0) {
				showFollowUpForm = true;
			}
			
			ret.put(ptId, new BooleanResult(showFollowUpForm, this));
		}
		return ret;
	}
	
}
