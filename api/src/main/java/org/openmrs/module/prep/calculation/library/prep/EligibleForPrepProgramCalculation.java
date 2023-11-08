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
import org.openmrs.Form;
import org.openmrs.Patient;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Visit;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.ConceptService;
import org.openmrs.api.PatientService;
import org.openmrs.api.VisitService;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResult;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.kenyacore.calculation.AbstractPatientCalculation;
import org.openmrs.module.kenyacore.calculation.Calculations;
import org.openmrs.module.kenyacore.calculation.BooleanResult;
import org.openmrs.module.kenyacore.calculation.Filters;
import org.openmrs.module.kenyaemr.metadata.MchMetadata;
import org.openmrs.module.kenyaemr.util.HtsConstants;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.prep.util.EmrUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Calculates whether patients are eligible for the Prep program
 */
public class EligibleForPrepProgramCalculation extends AbstractPatientCalculation {
	
	private AdministrationService administrationService;
	
	Long htsInitialValidPeriod = null;
	
	Long htsMnchValidPeriod = null;
	
	String createnine = null;
	
	static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy");
	
	/**
	 * @see org.openmrs.calculation.patient.PatientCalculation#evaluate(Collection, Map,
	 *      org.openmrs.calculation.patient.PatientCalculationContext)
	 */
	@Override
	public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> params,
	        PatientCalculationContext context) {
		PatientService patientService = Context.getPatientService();
		ConceptService cs = Context.getConceptService();
		VisitService visitService = Context.getVisitService();
		Concept weight = Context.getConceptService().getConcept(5089);
		Concept htsTestResults = Context.getConceptService().getConcept(159427);
		Concept willingToTakePrep = Context.getConceptService().getConcept(165094);
		Concept creatinine = Context.getConceptService().getConcept(790);
		administrationService = Context.getAdministrationService();
		Integer prepWeightCriteria = Integer.parseInt(administrationService.getGlobalProperty("prep.weight"));
		Integer prepAgeCriteria = Integer.parseInt(administrationService.getGlobalProperty("prep.age"));
		Integer prepHtsInitialCriteria = Integer.parseInt(administrationService.getGlobalProperty("prep.htsInitialPeriod"));
		Integer creatinineCriteria = 50;
		CalculationResultMap currentWeight = Calculations.lastObs(weight, cohort, context);
		CalculationResultMap currentTestResults = Calculations.lastObs(htsTestResults, cohort, context);
		CalculationResultMap currentWillingTotakePrep = Calculations.lastObs(willingToTakePrep, cohort, context);
		CalculationResultMap currentCreatinine = Calculations.lastObs(creatinine, cohort, context);
		
		CalculationResultMap ret = new CalculationResultMap();
		Set<Integer> alive = Filters.alive(cohort, context);
		
		for (int ptId : cohort) {
			Patient patient = patientService.getPatient(ptId);
			boolean enrollPatientOnPrep = false;
			boolean eligible = alive.contains(ptId);
			
			// check client weight is over 35Kg,willing to take PreP and hiv test result is negative
			Date currentDate = new Date();
			
			Obs weightCurrentObs = EmrCalculationUtils.obsResultForPatient(currentWeight, ptId);
			Obs testResultsCurrentObs = EmrCalculationUtils.obsResultForPatient(currentTestResults, ptId);
			Obs willingForPrepCurrentObs = EmrCalculationUtils.obsResultForPatient(currentWillingTotakePrep, ptId);
			Obs creatinineCurrentObs = EmrCalculationUtils.obsResultForPatient(currentCreatinine, ptId);
			
			//  Check new Tested HIV- clients in HTS module
			Encounter lastHtsInitialEncounterBeforPrepInitiation = EmrUtils.lastEncounter(patient, Context
			        .getEncounterService().getEncounterTypeByUuid("9c0a7a57-62ff-4f75-babe-5835b0e921b7"), Context
			        .getFormService().getFormByUuid("402dc5d7-46da-42d4-b2be-f43ea4ad87b0"));
			if (lastHtsInitialEncounterBeforPrepInitiation != null) {
				Date encounterDate = lastHtsInitialEncounterBeforPrepInitiation.getEncounterDatetime();
				long difference = currentDate.getTime() - encounterDate.getTime();
				htsInitialValidPeriod = difference / (24 * 60 * 60 * 1000);
			}
			
			Long validPeriod = new Long(0);
			Encounter lastHtsRetestEncounterBeforPrepInitiation = EmrUtils.lastEncounter(patient, Context
			        .getEncounterService().getEncounterTypeByUuid("9c0a7a57-62ff-4f75-babe-5835b0e921b7"), Context
			        .getFormService().getFormByUuid("b08471f6-0892-4bf7-ab2b-bf79797b8ea4"));
			CalculationResult creatinineRes = EmrCalculationUtils.evaluateForPatient(LastCreatinineResultsCalculation.class,
			    null, patient);
			if (lastHtsRetestEncounterBeforPrepInitiation != null) {
				
				htsInitialValidPeriod = validPeriod;
			}
			
			//  Check new Tested HIV- clients in MCH module
			Form antenatalVisitForm = MetadataUtils.existing(Form.class, MchMetadata._Form.MCHMS_ANTENATAL_VISIT);
			Form matVisitForm = MetadataUtils.existing(Form.class, MchMetadata._Form.MCHMS_DELIVERY);
			Form pncVisitForm = MetadataUtils.existing(Form.class, MchMetadata._Form.MCHMS_POSTNATAL_VISIT);
			Concept htsFinalTestQuestion = cs.getConcept(HtsConstants.HTS_FINAL_TEST_CONCEPT_ID);
			Concept htsNegativeResult = cs.getConcept(HtsConstants.HTS_NEGATIVE_RESULT_CONCEPT_ID);
			
			List<Encounter> mnchHtsEncounters = Context.getEncounterService().getEncounters(patientService.getPatient(ptId),
			    null, null, null, Arrays.asList(antenatalVisitForm, matVisitForm, pncVisitForm), null, null, null, null,
			    false);
			
			Encounter lastMnchEncounterBeforPrepInitiation = null;
			if (mnchHtsEncounters.size() > 0) {
				// in case there are more than one, we pick the last one
				lastMnchEncounterBeforPrepInitiation = mnchHtsEncounters.get(mnchHtsEncounters.size() - 1);
				boolean patientHasNegativeMnchResult = lastMnchEncounterBeforPrepInitiation != null ? org.openmrs.module.kenyaemr.util.EmrUtils
				        .encounterThatPassCodedAnswer(lastMnchEncounterBeforPrepInitiation, htsFinalTestQuestion,
				            htsNegativeResult) : false;
				if (patientHasNegativeMnchResult) {
					Date encounterDate = lastMnchEncounterBeforPrepInitiation.getEncounterDatetime();
					long difference = currentDate.getTime() - encounterDate.getTime();
					htsMnchValidPeriod = difference / (24 * 60 * 60 * 1000);
				}
			}
			
			if ((creatinineRes.getValue()) == null) {
				createnine = "No result";
				
			} else {
				createnine = "Results available";
			}
			List<Visit> activeVisit = visitService.getActiveVisitsByPatient(patient);
			
			if (activeVisit.size() > 0) {
				for (Visit v : activeVisit) {
					if (!DATE_FORMAT.format(v.getStartDatetime()).equalsIgnoreCase(DATE_FORMAT.format(currentDate))) {
						
						if (weightCurrentObs != null && testResultsCurrentObs != null && willingForPrepCurrentObs != null) {
							htsInitialValidPeriod = validPeriod;
							if (eligible
							        && patient.getAge() >= prepAgeCriteria
							        && weightCurrentObs.getValueNumeric().intValue() >= prepWeightCriteria
							        && testResultsCurrentObs.getValueCoded().getConceptId().equals(664)
							        && willingForPrepCurrentObs.getValueCoded().getConceptId().equals(1065)
							        && ((htsInitialValidPeriod != null && htsInitialValidPeriod <= prepHtsInitialCriteria) || (htsMnchValidPeriod != null && htsMnchValidPeriod <= prepHtsInitialCriteria))
							        && ((creatinineCurrentObs != null && creatinineCurrentObs.getValueNumeric().intValue() >= creatinineCriteria) || createnine
							                .equalsIgnoreCase("No result"))) {
								enrollPatientOnPrep = true;
							}
						}
						
					} else {
						
						if (weightCurrentObs != null && testResultsCurrentObs != null && willingForPrepCurrentObs != null
						        && (htsInitialValidPeriod != null || htsMnchValidPeriod != null)) {
							if (eligible
							        && patient.getAge() >= prepAgeCriteria
							        && weightCurrentObs.getValueNumeric().intValue() >= prepWeightCriteria
							        && testResultsCurrentObs.getValueCoded().getConceptId().equals(664)
							        && willingForPrepCurrentObs.getValueCoded().getConceptId().equals(1065)
							        && ((htsInitialValidPeriod != null && htsInitialValidPeriod <= prepHtsInitialCriteria) || (htsMnchValidPeriod != null && htsMnchValidPeriod <= prepHtsInitialCriteria))
							        && ((creatinineCurrentObs != null && creatinineCurrentObs.getValueNumeric().intValue() >= creatinineCriteria) || createnine
							                .equalsIgnoreCase("No result"))) {
								enrollPatientOnPrep = true;
							}
						}
					}
					
				}
			}
			
			ret.put(ptId, new BooleanResult(enrollPatientOnPrep, this, context));
			
		}
		
		return ret;
	}
	
}
