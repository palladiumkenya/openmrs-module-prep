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
import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.EncounterType;
import org.openmrs.Form;
import org.openmrs.Visit;
import org.openmrs.Obs;
import org.openmrs.Concept;
import org.openmrs.api.EncounterService;
import org.openmrs.api.PatientService;
import org.openmrs.api.VisitService;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.kenyacore.calculation.AbstractPatientCalculation;
import org.openmrs.module.kenyacore.calculation.BooleanResult;
import org.openmrs.module.kenyacore.calculation.Calculations;
import org.openmrs.module.prep.metadata.PrepMetadata;
import org.openmrs.module.prep.util.EmrUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Checks if a patient is negative, not enrolled and no initial hts encounter
 */
public class PatientsEligibleForPrepMonthlyRefillCalculation extends AbstractPatientCalculation {
	
	static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy");
	
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
		VisitService visitService = Context.getVisitService();
		
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
			int diffBetweenFollowupEncounters = 0;
			Integer missAppointmentBySevenDays = 0;
			int diffBetweenMonthlyRefillEncounters = 0;
			int diffInMonthBtwnLastFollowupEncAndLastRefil = 0;
			
			Encounter firstMonthlyEncounter = EmrUtils.firstEncounter(patient, Context.getEncounterService()
			        .getEncounterTypeByUuid(PrepMetadata._EncounterType.PREP_MONTHLY_REFILL));
			if (firstMonthlyEncounter != null) {
				firstMonthlyRefill = 1;
			} else {
				firstMonthlyRefill = 0;
			}
			
			//Handle previous encounter
			
			Encounter previousMonthlyRefillEnc = previousEncounter(patient, Context.getEncounterService()
			        .getEncounterTypeByUuid(PrepMetadata._EncounterType.PREP_MONTHLY_REFILL), Context.getFormService()
			        .getFormByUuid(PrepMetadata._Form.PREP_MONTHLY_REFILL_FORM));
			
			Encounter previousFollowupEnc = previousEncounter(patient,
			    Context.getEncounterService().getEncounterTypeByUuid(PrepMetadata._EncounterType.PREP_CONSULTATION), Context
			            .getFormService().getFormByUuid(PrepMetadata._Form.PREP_CONSULTATION_FORM));
			
			Encounter lastMonthlyRefillEncounter = EmrUtils.lastEncounter(patient, Context.getEncounterService()
			        .getEncounterTypeByUuid(PrepMetadata._EncounterType.PREP_MONTHLY_REFILL), Context.getFormService()
			        .getFormByUuid(PrepMetadata._Form.PREP_MONTHLY_REFILL_FORM));
			
			Encounter lastFollowUp = EmrUtils.lastEncounter(patient,
			    Context.getEncounterService().getEncounterTypeByUuid(PrepMetadata._EncounterType.PREP_CONSULTATION), Context
			            .getFormService().getFormByUuid(PrepMetadata._Form.PREP_CONSULTATION_FORM));
			
			Date visistDate = null;
			List<Visit> activeVisit = visitService.getActiveVisitsByPatient(patient);
			
			if (activeVisit.size() > 0) {
				for (Visit v : activeVisit) {
					System.out.println("visistDate" + v.getStartDatetime());
					visistDate = v.getStartDatetime();
				}
			}
			
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
			
			if (lastFollowUp != null && previousFollowupEnc != null) {
				diffBetweenFollowupEncounters = (lastFollowUp.getEncounterDatetime().getMonth() - previousFollowupEnc
				        .getEncounterDatetime().getMonth())
				        + ((lastFollowUp.getEncounterDatetime().getYear() - previousFollowupEnc.getEncounterDatetime()
				                .getYear()) * 12);
			}
			
			Obs nextAppointmentObs = EmrCalculationUtils.obsResultForPatient(nextAppointmentLastObs, ptId);
			if (nextAppointmentObs != null && visistDate != null) {
				if (visistDate.before(DateUtils.addDays(nextAppointmentObs.getValueDate(), 7))) {
					missAppointmentBySevenDays = 0;
				} else {
					missAppointmentBySevenDays = 1;
				}
				
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
			
			if (lastMonthlyRefillEncounter != null && previousMonthlyRefillEnc != null) {
				diffBetweenMonthlyRefillEncounters = (lastMonthlyRefillEncounter.getEncounterDatetime().getMonth() - previousMonthlyRefillEnc
				        .getEncounterDatetime().getMonth())
				        + ((lastMonthlyRefillEncounter.getEncounterDatetime().getYear() - previousMonthlyRefillEnc
				                .getEncounterDatetime().getYear()) * 12);
			}
			//
			if (lastFollowUp != null && lastMonthlyRefillEncounter != null) {
				diffInMonthBtwnLastFollowupEncAndLastRefil = (lastFollowUp.getEncounterDatetime().getMonth() - lastMonthlyRefillEncounter
				        .getEncounterDatetime().getMonth())
				        + ((lastFollowUp.getEncounterDatetime().getYear() - lastMonthlyRefillEncounter
				                .getEncounterDatetime().getYear()) * 12);
			}
			
			if (diffInMonthBtwnLastFollowupEncAndLastRefil == 1
			        && setRestartWhenFollowup == 0
			        && missAppointmentBySevenDays == 0
					&& visistDate != null
			        && !DATE_FORMAT.format(lastFollowUp.getEncounterDatetime()).equalsIgnoreCase(
			            DATE_FORMAT.format(visistDate))) {
				showMonthlyRefillForm = true;
			}
			
			if (visistDate != null
			        && setRestartWhenFollowup == 0
			        && missAppointmentBySevenDays == 0
			        && lastFollowUp != null
			        && diffBetweenFollowupEncounters == 3
			        && diffBetweenMonthlyRefillEncounters != 1
			        && !DATE_FORMAT.format(lastFollowUp.getEncounterDatetime()).equalsIgnoreCase(
			            DATE_FORMAT.format(visistDate))) {
				showMonthlyRefillForm = true;
			}
			
			if (firstMonthlyEncounter != null
			        && visistDate != null
			        && setRestartWhenFollowup == 0
			        && followupEncounters.size() >= 2
			        && lastFollowUp != null
			        && missAppointmentBySevenDays == 0
			        && (diffBetweenMonthlyRefillEncounters != 1 || diffBetweenMonthlyRefillEncounters > 1)
			        && !DATE_FORMAT.format(lastFollowUp.getEncounterDatetime()).equalsIgnoreCase(
			            DATE_FORMAT.format(visistDate))) {
				showMonthlyRefillForm = true;
				
			}
			
			if (enrollmentEncounters.size() > 0
			        && followupEncounters.size() > 0
			        && setRestartWhenFollowup == 0
			        && firstMonthlyRefill == 0
			        && lastFollowUp != null
			        && visistDate != null
			        && !DATE_FORMAT.format(lastFollowUp.getEncounterDatetime()).equalsIgnoreCase(
			            DATE_FORMAT.format(visistDate))) {
				showMonthlyRefillForm = true;
			}
			
			ret.put(ptId, new BooleanResult(showMonthlyRefillForm, this));
		}
		return ret;
	}
	
}
