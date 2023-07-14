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

import org.openmrs.Patient;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Form;
import org.openmrs.Obs;
import org.openmrs.Concept;
import org.openmrs.Program;
import org.openmrs.Visit;
import org.openmrs.api.VisitService;
import org.openmrs.api.EncounterService;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.kenyacore.calculation.AbstractPatientCalculation;
import org.openmrs.module.kenyacore.calculation.BooleanResult;
import org.openmrs.module.kenyacore.calculation.Calculations;
import org.openmrs.module.kenyacore.calculation.Filters;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.prep.metadata.PrepMetadata;
import org.openmrs.module.prep.util.EmrUtils;

import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.lang.time.DateUtils;

/**
 * Checks if a patient is negative, not enrolled and no initial hts encounter
 */
public class PatientsEligibleForPrepFollowUpCalculation extends AbstractPatientCalculation {
	
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
		
		Concept nextAppointment = Context.getConceptService().getConcept(5096);
		CalculationResultMap nextAppointmentLastObs = Calculations.lastObs(nextAppointment, cohort, context);
		Concept prepTreatmentPlan = Context.getConceptService().getConcept(165109);
		Concept restartPrepConcept = Context.getConceptService().getConcept(162904);
		int diffInMonthsBtwnFollowUpEnc = 0;
		int diffInMonthsBtwnMonthlyRefillEnc = 0;
		int diffInMonthBtwnLastFollowupEncAndLastRefil = 0;
		
		Set<Integer> alive = Filters.alive(cohort, context);
		Program prepProgram = MetadataUtils.existing(Program.class, PrepMetadata._Program.PREP);
		Set<Integer> inPrepProgram = Filters.inProgram(prepProgram, alive, context);
		
		CalculationResultMap ret = new CalculationResultMap();
		for (Integer ptId : cohort) {
			Patient patient = patientService.getPatient(ptId);
			boolean showFollowUpForm = false;
			Obs nextAppointmentObs = EmrCalculationUtils.obsResultForPatient(nextAppointmentLastObs, ptId);
			if (inPrepProgram.contains(ptId)) {
				// last followup encounter
				Encounter lastFollowUp = EmrUtils.lastEncounter(patient, Context.getEncounterService()
				        .getEncounterTypeByUuid(PrepMetadata._EncounterType.PREP_CONSULTATION), Context.getFormService()
				        .getFormByUuid(PrepMetadata._Form.PREP_CONSULTATION_FORM));
				
				Encounter previousFollowupEncounter = previousEncounter(patient, Context.getEncounterService()
				        .getEncounterTypeByUuid(PrepMetadata._EncounterType.PREP_CONSULTATION), Context.getFormService()
				        .getFormByUuid(PrepMetadata._Form.PREP_CONSULTATION_FORM));
				if (lastFollowUp != null) {
					for (Obs o : lastFollowUp.getObs()) {
						if (o.getConcept().getConceptId() == 165109) {
							CalculationResultMap restarts = Calculations.lastObs(prepTreatmentPlan, cohort, context);
							Obs restartValue = EmrCalculationUtils.obsResultForPatient(restarts, ptId);
							if (restartValue != null && restartValue.getValueCoded() == restartPrepConcept) {
								showFollowUpForm = true;
							}
						}
						
					}
				}
				
				Date visistDate = null;
				
				List<Visit> activeVisit = visitService.getActiveVisitsByPatient(patient);
				
				if (activeVisit.size() > 0) {
					for (Visit v : activeVisit) {
						visistDate = v.getStartDatetime();
						
					}
				}
				
				Encounter previousMonthlyRefill = previousEncounter(patient, Context.getEncounterService()
				        .getEncounterTypeByUuid(PrepMetadata._EncounterType.PREP_MONTHLY_REFILL), Context.getFormService()
				        .getFormByUuid(PrepMetadata._Form.PREP_MONTHLY_REFILL_FORM));
				
				Encounter lastMonthlyRefillEncounter = EmrUtils.lastEncounter(patient, Context.getEncounterService()
				        .getEncounterTypeByUuid(PrepMetadata._EncounterType.PREP_MONTHLY_REFILL), Context.getFormService()
				        .getFormByUuid(PrepMetadata._Form.PREP_MONTHLY_REFILL_FORM));
				
				if (lastFollowUp != null && previousFollowupEncounter != null) {
					diffInMonthsBtwnFollowUpEnc = (lastFollowUp.getEncounterDatetime().getMonth() - previousFollowupEncounter
					        .getEncounterDatetime().getMonth())
					        + ((lastFollowUp.getEncounterDatetime().getYear() - previousFollowupEncounter
					                .getEncounterDatetime().getYear()) * 12);
				}
				
				if (lastMonthlyRefillEncounter != null && previousMonthlyRefill != null) {
					diffInMonthsBtwnMonthlyRefillEnc = (lastMonthlyRefillEncounter.getEncounterDatetime().getMonth() - previousMonthlyRefill
					        .getEncounterDatetime().getMonth())
					        + ((lastMonthlyRefillEncounter.getEncounterDatetime().getYear() - previousMonthlyRefill
					                .getEncounterDatetime().getYear()) * 12);
				}
				
				Encounter firstMonthlyEncounter = EmrUtils.firstEncounter(patient, Context.getEncounterService()
				        .getEncounterTypeByUuid(PrepMetadata._EncounterType.PREP_MONTHLY_REFILL));
				
				//	check there is follow up form
				List<Encounter> numOfFolloupEncounters = encounterService.getEncounters(Context.getPatientService()
				        .getPatient(ptId), null, null, null, Collections.singleton(Context.getFormService().getFormByUuid(
				    PrepMetadata._Form.PREP_CONSULTATION_FORM)), Arrays.asList(Context.getEncounterService()
				        .getEncounterTypeByUuid(PrepMetadata._EncounterType.PREP_CONSULTATION)), null, null, null, false);
				
				List<Encounter> prepInitialEncounter = encounterService
				        .getEncounters(
				            Context.getPatientService().getPatient(ptId),
				            null,
				            null,
				            null,
				            null,
				            Arrays.asList(Context.getEncounterService().getEncounterTypeByUuid(
				                PrepMetadata._EncounterType.PREP_INITIAL_FOLLOWUP)), null, null, null, false);
				
				if (firstMonthlyEncounter != null
				        && visistDate != null
				        && numOfFolloupEncounters.size() < 2
				        && lastMonthlyRefillEncounter != null
				        && !DATE_FORMAT.format(lastMonthlyRefillEncounter.getEncounterDatetime()).equalsIgnoreCase(
				            DATE_FORMAT.format(visistDate))) {
					showFollowUpForm = true;
				}
				
				List<Encounter> enrollmentEncounters = encounterService.getEncounters(Context.getPatientService()
				        .getPatient(ptId), null, null, null, null, Arrays.asList(Context.getEncounterService()
				        .getEncounterTypeByUuid(PrepMetadata._EncounterType.PREP_ENROLLMENT)), null, null, null, false);
				
				List<Encounter> monthlyRefillEncounters = encounterService.getEncounters(Context.getPatientService()
				        .getPatient(ptId), null, null, null, null, Arrays.asList(Context.getEncounterService()
				        .getEncounterTypeByUuid(PrepMetadata._EncounterType.PREP_MONTHLY_REFILL)), null, null, null, false);
				
				if (nextAppointmentObs != null && visistDate != null && lastMonthlyRefillEncounter != null) {
					Date missAppointmentBySevenDays = DateUtils.addDays(nextAppointmentObs.getValueDate(), 7);
					if (visistDate.after(missAppointmentBySevenDays)
					        && !DATE_FORMAT.format(lastMonthlyRefillEncounter.getEncounterDatetime()).equalsIgnoreCase(
					            DATE_FORMAT.format(visistDate))) {
						showFollowUpForm = true;
					}
				}
				
				if (monthlyRefillEncounters.size() > 1
				        && monthlyRefillEncounters.size() % 2 != 0
				        && diffInMonthsBtwnFollowUpEnc != 3
				        && diffInMonthsBtwnMonthlyRefillEnc == 1
				        && lastMonthlyRefillEncounter != null
				        && visistDate != null
				        && !DATE_FORMAT.format(lastMonthlyRefillEncounter.getEncounterDatetime()).equalsIgnoreCase(
				            DATE_FORMAT.format(visistDate))) {
					showFollowUpForm = true;
				}
				
				if (lastMonthlyRefillEncounter != null && lastFollowUp != null) {
					diffInMonthBtwnLastFollowupEncAndLastRefil = (lastMonthlyRefillEncounter.getEncounterDatetime()
					        .getMonth() - lastFollowUp.getEncounterDatetime().getMonth())
					        + ((lastMonthlyRefillEncounter.getEncounterDatetime().getYear() - lastFollowUp
					                .getEncounterDatetime().getYear()) * 12);
				}
				if (diffInMonthBtwnLastFollowupEncAndLastRefil == 2
				        && lastMonthlyRefillEncounter != null
				        && visistDate != null
				        && !DATE_FORMAT.format(lastMonthlyRefillEncounter.getEncounterDatetime()).equalsIgnoreCase(
				            DATE_FORMAT.format(visistDate))) {
					showFollowUpForm = true;
				}
				
				if (monthlyRefillEncounters.size() > 1
				        && monthlyRefillEncounters.size() % 2 != 0
				        && diffInMonthsBtwnMonthlyRefillEnc != 1
				        && lastMonthlyRefillEncounter != null
				        && visistDate != null
				        && !DATE_FORMAT.format(lastMonthlyRefillEncounter.getEncounterDatetime()).equalsIgnoreCase(
				            DATE_FORMAT.format(visistDate))) {
					showFollowUpForm = true;
				}
				
				if (enrollmentEncounters.size() > 0
				        && numOfFolloupEncounters.size() == 0
				        && prepInitialEncounter.size() > 0
				        && visistDate != null
				        && !DATE_FORMAT.format(prepInitialEncounter.get(0).getEncounterDatetime()).equalsIgnoreCase(
				            DATE_FORMAT.format(visistDate))) {
					showFollowUpForm = true;
				}
			}
			
			ret.put(ptId, new BooleanResult(showFollowUpForm, this));
		}
		return ret;
	}
	
}
