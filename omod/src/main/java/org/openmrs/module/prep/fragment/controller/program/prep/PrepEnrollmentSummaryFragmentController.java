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
import org.openmrs.api.context.Context;
import org.openmrs.module.prep.wrapper.EncounterWrapper;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Prep Program enrollment fragment
 */
public class PrepEnrollmentSummaryFragmentController {
	
	public String controller(@FragmentParam("patientProgram") PatientProgram patientProgram,
	        @FragmentParam("showClinicalData") boolean showClinicalData,
	        @FragmentParam(value = "encounter", required = false) Encounter encounter, FragmentModel model) {
		
		Map<String, Object> dataPoints = new LinkedHashMap<String, Object>();
		dataPoints.put("Enrolled", patientProgram.getDateEnrolled());
		
		if (encounter != null) {
			EncounterWrapper wrapper = new EncounterWrapper(encounter);
			
			Obs o = wrapper.firstObs(Context.getConceptService().getConcept(164932));
			if (o != null) {
				dataPoints.put("Entry point", o.getValueCoded());
			}
		}
		
		model.put("dataPoints", dataPoints);
		return "view/dataPoints";
	}
}
