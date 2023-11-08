/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.prep.fragment.controller.program;

import org.apache.commons.lang.time.DateUtils;
import org.openmrs.Concept;
import org.openmrs.Form;
import org.openmrs.Obs;
import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.Program;
import org.openmrs.Visit;
import org.openmrs.PatientProgram;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.ConceptService;
import org.openmrs.api.PatientService;
import org.openmrs.api.ProgramWorkflowService;
import org.openmrs.api.VisitService;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.result.CalculationResult;
import org.openmrs.module.kenyacore.form.FormManager;
import org.openmrs.module.kenyacore.program.ProgramManager;
import org.openmrs.module.kenyaemr.metadata.HivMetadata;
import org.openmrs.module.kenyaemr.metadata.MchMetadata;
import org.openmrs.module.kenyaemr.util.HtsConstants;
import org.openmrs.module.kenyaui.KenyaUiUtils;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.prep.calculation.library.prep.EmrCalculationUtils;
import org.openmrs.module.prep.calculation.library.prep.LastCreatinineResultsCalculation;
import org.openmrs.module.prep.calculation.library.prep.LastHtsResultsCalculation;
import org.openmrs.module.prep.calculation.library.prep.LastWeightCalculation;
import org.openmrs.module.prep.calculation.library.prep.NextAppointmentCalculation;
import org.openmrs.module.prep.calculation.library.prep.WillingnessToStartPrepCalculation;
import org.openmrs.module.prep.metadata.PrepMetadata;
import org.openmrs.module.prep.util.EmrUtils;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.openmrs.ui.framework.page.PageRequest;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Patient program history fragment
 */
public class PrepEligibilitySummaryFragmentController {
	
	private AdministrationService administrationService;
	
	static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy");
	
	VisitService visitService = Context.getVisitService();
	
	public void controller(FragmentModel model, @FragmentParam("patient") Patient patient, UiUtils ui,
	        PageRequest pageRequest, @SpringBean ProgramManager programManager, @SpringBean FormManager formManager,
	        @SpringBean KenyaUiUtils kenyaUi) {
		
		Double weight = null;
		String htsResults = null;
		String willingnessToTakePrep = null;
		Long htsInitialValidPeriod = null;
		Long htsMnchValidPeriod = null;
		Double creatinine = null;
		String creatinineNoResult = "";
		Date appointment = null;
		Date visistDate = null;
		int missAppointmentBySevenDays = 0;
		ProgramWorkflowService service = Context.getProgramWorkflowService();
		ConceptService cs = Context.getConceptService();
		
		CalculationResult weightResults = EmrCalculationUtils.evaluateForPatient(LastWeightCalculation.class, null, patient);
		if (weightResults != null && weightResults.getValue() != null) {
			weight = ((Obs) weightResults.getValue()).getValueNumeric();
		}
		
		CalculationResult htsRes = EmrCalculationUtils.evaluateForPatient(LastHtsResultsCalculation.class, null, patient);
		if (htsRes != null && htsRes.getValue() != null) {
			htsResults = ((Obs) htsRes.getValue()).getValueCoded().getName().toString();
		}
		
		CalculationResult WillingnessRes = EmrCalculationUtils.evaluateForPatient(WillingnessToStartPrepCalculation.class,
		    null, patient);
		if (WillingnessRes != null && WillingnessRes.getValue() != null) {
			willingnessToTakePrep = ((Obs) WillingnessRes.getValue()).getValueCoded().getName().toString();
		}
		CalculationResult creatinineRes = EmrCalculationUtils.evaluateForPatient(LastCreatinineResultsCalculation.class,
		    null, patient);
		if (creatinineRes != null && creatinineRes.getValue() != null) {
			creatinine = ((Obs) creatinineRes.getValue()).getValueNumeric();
			creatinineNoResult = "";
		} else {
			creatinineNoResult = "No result";
		}
		
		administrationService = Context.getAdministrationService();
		Integer prepWeightCriteria = Integer.parseInt(administrationService.getGlobalProperty("prep.weight"));
		Integer prepAgeCriteria = Integer.parseInt(administrationService.getGlobalProperty("prep.age"));
		Integer prepHtsInitialCriteria = Integer.parseInt(administrationService.getGlobalProperty("prep.htsInitialPeriod"));
		Integer prepCreatinineCriteria = 50;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date currentDate = new Date();
		
		Encounter lastHtsInitialEncounterBeforPrepInitiation = EmrUtils.lastEncounter(patient, Context.getEncounterService()
		        .getEncounterTypeByUuid("9c0a7a57-62ff-4f75-babe-5835b0e921b7"),
		    Context.getFormService().getFormByUuid("402dc5d7-46da-42d4-b2be-f43ea4ad87b0"));
		if (lastHtsInitialEncounterBeforPrepInitiation != null) {
			Date encounterDate = lastHtsInitialEncounterBeforPrepInitiation.getEncounterDatetime();
			long difference = currentDate.getTime() - encounterDate.getTime();
			htsInitialValidPeriod = difference / (24 * 60 * 60 * 1000);
		}
		
		Long validPeriod = new Long(0);
		Encounter lastHtsRetestEncounterBeforPrepInitiation = EmrUtils.lastEncounter(patient, Context.getEncounterService()
		        .getEncounterTypeByUuid("9c0a7a57-62ff-4f75-babe-5835b0e921b7"),
		    Context.getFormService().getFormByUuid("b08471f6-0892-4bf7-ab2b-bf79797b8ea4"));
		if (lastHtsRetestEncounterBeforPrepInitiation != null) {
			
			htsInitialValidPeriod = validPeriod;
		}
		
		Long checkEnrolled = new Long(0);
		Encounter lastPrepInitiation = EmrUtils.lastEncounter(patient,
		    Context.getEncounterService().getEncounterTypeByUuid(PrepMetadata._EncounterType.PREP_ENROLLMENT), Context
		            .getFormService().getFormByUuid(PrepMetadata._Form.PREP_ENROLLMENT_FORM));
		if (lastPrepInitiation != null) {
			htsInitialValidPeriod = checkEnrolled;
		}
		
		List<Visit> activeVisit = visitService.getActiveVisitsByPatient(patient);
		if (activeVisit.size() > 0) {
			for (Visit v : activeVisit) {
				visistDate = v.getStartDatetime();
				if (!DATE_FORMAT.format(v.getStartDatetime()).equalsIgnoreCase(DATE_FORMAT.format(currentDate))) {
					htsInitialValidPeriod = validPeriod;
					
				}
			}
		}
		//  Check new Tested HIV- clients in MCH module
		Form antenatalVisitForm = MetadataUtils.existing(Form.class, MchMetadata._Form.MCHMS_ANTENATAL_VISIT);
		Form matVisitForm = MetadataUtils.existing(Form.class, MchMetadata._Form.MCHMS_DELIVERY);
		Form pncVisitForm = MetadataUtils.existing(Form.class, MchMetadata._Form.MCHMS_POSTNATAL_VISIT);
		Concept htsFinalTestQuestion = cs.getConcept(HtsConstants.HTS_FINAL_TEST_CONCEPT_ID);
		Concept htsNegativeResult = cs.getConcept(HtsConstants.HTS_NEGATIVE_RESULT_CONCEPT_ID);
		
		List<Encounter> mnchHtsEncounters = Context.getEncounterService().getEncounters(patient, null, null, null,
		    Arrays.asList(antenatalVisitForm, matVisitForm, pncVisitForm), null, null, null, null, false);
		
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
		
		Program prepProgram = MetadataUtils.existing(Program.class, PrepMetadata._Program.PREP);
		List<PatientProgram> prePprograms = service.getPatientPrograms(patient, prepProgram, null, null, null, null, true);
		
		CalculationResult nextAppointmentObs = EmrCalculationUtils.evaluateForPatient(NextAppointmentCalculation.class,
		    null, patient);
		if (nextAppointmentObs != null && nextAppointmentObs.getValue() != null && visistDate != null
		        && prePprograms.size() > 0) {
			appointment = ((Obs) nextAppointmentObs.getValue()).getValueDate();
			if (visistDate.before(DateUtils.addDays(appointment, 7))) {
				missAppointmentBySevenDays = 0;
			} else {
				missAppointmentBySevenDays = 1;
				
			}
		}
		
		Program hivProgram = MetadataUtils.existing(Program.class, HivMetadata._Program.HIV);
		List<PatientProgram> programs = service.getPatientPrograms(patient, hivProgram, null, null, null, null, true);
		if (programs.size() > 0) {
			htsResults = "POSITIVE";
		}
		
		model.addAttribute("prepWeightCriteria", prepWeightCriteria);
		model.addAttribute("prepAgeCriteria", prepAgeCriteria);
		model.addAttribute("htsInitialValidPeriod", htsInitialValidPeriod);
		model.addAttribute("htsMnchValidPeriod", htsMnchValidPeriod);
		model.addAttribute("prepHtsInitialCriteria", prepHtsInitialCriteria);
		model.addAttribute("prepCreatinineCriteria", prepCreatinineCriteria);
		
		model.addAttribute("htsResult", htsResults);
		model.addAttribute("weight", weight);
		model.addAttribute("age", patient.getAge());
		model.addAttribute("willingnessToTakePrep", willingnessToTakePrep);
		model.addAttribute("creatinine", creatinine);
		model.addAttribute("creatinineNoResult", creatinineNoResult);
		model.addAttribute("missAppointmentBySevenDays", missAppointmentBySevenDays);
	}
	
}
