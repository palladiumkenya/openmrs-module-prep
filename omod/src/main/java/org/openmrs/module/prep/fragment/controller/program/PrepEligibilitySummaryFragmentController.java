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

import org.openmrs.*;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.result.CalculationResult;
import org.openmrs.module.kenyacore.form.FormManager;
import org.openmrs.module.kenyacore.program.ProgramManager;
import org.openmrs.module.kenyaui.KenyaUiUtils;
import org.openmrs.module.prep.calculation.library.prep.EmrCalculationUtils;
import org.openmrs.module.prep.calculation.library.prep.LastHtsResultsCalculation;
import org.openmrs.module.prep.calculation.library.prep.LastWeightCalculation;
import org.openmrs.module.prep.calculation.library.prep.WillingnessToStartPrepCalculation;
import org.openmrs.module.prep.util.EmrUtils;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.openmrs.ui.framework.page.PageRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Patient program history fragment
 */
public class PrepEligibilitySummaryFragmentController {
	
	private AdministrationService administrationService;
	
	public void controller(FragmentModel model, @FragmentParam("patient") Patient patient, UiUtils ui,
	        PageRequest pageRequest, @SpringBean ProgramManager programManager, @SpringBean FormManager formManager,
	        @SpringBean KenyaUiUtils kenyaUi) {
		
		Double weight = null;
		String htsResults = null;
		String willingnessToTakePrep = null;
		Long htsInitialValidPeriod = null;
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
		administrationService = Context.getAdministrationService();
		Integer prepWeightCriteria = Integer.parseInt(administrationService.getGlobalProperty("prep.weight"));
		Integer prepAgeCriteria = Integer.parseInt(administrationService.getGlobalProperty("prep.age"));
		Integer prepHtsInitialCriteria = Integer.parseInt(administrationService.getGlobalProperty("prep.htsInitialPeriod"));
		
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
		model.addAttribute("prepWeightCriteria", prepWeightCriteria);
		model.addAttribute("prepAgeCriteria", prepAgeCriteria);
		model.addAttribute("htsInitialValidPeriod", htsInitialValidPeriod);
		model.addAttribute("prepHtsInitialCriteria", prepHtsInitialCriteria);
		
		model.addAttribute("htsResult", htsResults);
		model.addAttribute("weight", weight);
		model.addAttribute("age", patient.getAge());
		model.addAttribute("willingnessToTakePrep", willingnessToTakePrep);
	}
	
}
