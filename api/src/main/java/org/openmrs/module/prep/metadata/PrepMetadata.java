/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.prep.metadata;

import org.openmrs.PatientIdentifierType;
import org.openmrs.module.metadatadeploy.bundle.AbstractMetadataBundle;
import org.simpleframework.xml.util.Dictionary;
import org.springframework.stereotype.Component;

import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.*;

/**
 * PREP metadata bundle
 */
@Component
public class PrepMetadata extends AbstractMetadataBundle {
	
	public static final class _EncounterType {
		
		public static final String PREP_CONSULTATION = "c4a2be28-6673-4c36-b886-ea89b0a42116";
		
		public static final String PREP_DISCONTINUATION = "3c37b7a0-9f83-4fdc-994f-17308c22b423";
		
		public static final String PREP_ENROLLMENT = "35468fe8-a889-4cd4-9b35-27ac98bdd750";
	}
	
	public static final class _Form {
		
		public static final String PREP_ENROLLMENT_FORM = "d5ca78be-654e-4d23-836e-a934739be555";
		
		public static final String PREP_DISCONTINUATION_FORM = "467c4cc3-25eb-4330-9cf6-e41b9b14cc10";
		
		public static final String PREP_CONSULTATION_FORM = "ee3e2017-52c0-4a54-99ab-ebb542fb8984";
		
		public static final String PREP_PROGRESS_NOTE_FORM = "c48ed2a2-0a0f-4f4e-9fed-a79ca3e1a9b9";
	}
	
	public static final class _PatientIdentifierType {
		
		public static final String NHIF_NUMBER = "09ebf4f9-b673-4d97-b39b-04f94088ba64";
		
		public static final String PREP_UNIQUE_NUMBER = "ac64e5cb-e3e2-4efa-9060-0dd715a843a1";
	}
	
	public static final class _Program {
		
		public static final String PREP = "214cad1c-bb62-4d8e-b927-810a046daf62";
	}
	
	public static final class _Concept {
		
		public static final String PREP = "1115e622-3cba-4161-9f7a-a11a42cc3968";
	}
	
	/**
	 * @see org.openmrs.module.metadatadeploy.bundle.AbstractMetadataBundle#install()
	 */
	@Override
	public void install() {
		///////////////////////////// PREP services ////////////////////////////////
		
		install(encounterType("PREP Enrollment", "Enrollment of client onto PREP program", _EncounterType.PREP_ENROLLMENT));
		install(encounterType("PREP Consultation", "Collection of client data during PREP visit",
		    _EncounterType.PREP_CONSULTATION));
		install(encounterType("PREP Client Discontinuation", "Discontinuation of client from PREP program",
		    _EncounterType.PREP_DISCONTINUATION));
		
		install(form("PREP Client Initiation", "PREP Enrollment form", _EncounterType.PREP_ENROLLMENT, "1.0",
		    _Form.PREP_ENROLLMENT_FORM));
		install(form("PREP Client Discontinuation", "PREP discontinuation form", _EncounterType.PREP_DISCONTINUATION, "1.0",
		    _Form.PREP_DISCONTINUATION_FORM));
		install(form("PREP Follow Up", "PREP follow up form", _EncounterType.PREP_CONSULTATION, "1.0",
		    _Form.PREP_CONSULTATION_FORM));
		
		install(form("PREP Progress Notes", "PREP Progress Notes", _EncounterType.PREP_CONSULTATION, "1.0",
		    _Form.PREP_PROGRESS_NOTE_FORM));
		
		install(patientIdentifierType("NHIF Number", "PREP client Insurance number ", null, null, null,
		    PatientIdentifierType.LocationBehavior.NOT_USED, false, _PatientIdentifierType.NHIF_NUMBER));
		
		install(patientIdentifierType("PREP Unique Number", "Unique Number assigned to PREP client upon enrollment", null,
		    null, null, PatientIdentifierType.LocationBehavior.NOT_USED, false, _PatientIdentifierType.PREP_UNIQUE_NUMBER));
		
		install(program("PREP", "Pre exposure prophylaxis program", _Concept.PREP, _Program.PREP));
		
	}
}
