/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.prep.fragment.controller.program.prep;

import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.PatientProgram;
import org.openmrs.module.prep.wrapper.EncounterWrapper;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.openmrs.api.context.Context;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Prep Program completion fragment
 */
public class PrepCompletionSummaryFragmentController {
	
	public String controller(@FragmentParam("patientProgram") PatientProgram enrollment,
	        @FragmentParam("showClinicalData") boolean showClinicalData,
	        @FragmentParam(value = "encounter", required = false) Encounter encounter, FragmentModel model) {
		Map<String, Object> dataPoints = new LinkedHashMap<String, Object>();
		
		dataPoints.put("Completed", enrollment.getDateCompleted());
		
		if (showClinicalData && enrollment.getOutcome() != null) {
			dataPoints.put("Outcome", enrollment.getOutcome());
		}
		
		if (encounter != null) {
			EncounterWrapper wrapper = new EncounterWrapper(encounter);
			
			Obs reasonObs = wrapper.firstObs(Context.getConceptService().getConcept(161555));
			if (reasonObs != null) {
				dataPoints.put("Reason", reasonObs.getValueCoded());
			}
		}
		
		model.put("dataPoints", dataPoints);
		return "view/dataPoints";
		
	}
}
