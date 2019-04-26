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

import org.openmrs.module.metadatadeploy.bundle.AbstractMetadataBundle;
import org.simpleframework.xml.util.Dictionary;
import org.springframework.stereotype.Component;

import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.*;
import static org.openmrs.module.prep.metadata.PrepMetadata._Program.PREP;

/**
 * MCH metadata bundle
 */
@Component
public class PrepMetadata extends AbstractMetadataBundle {
	
	public static final class _EncounterType {
		
		public static final String PREP_CONSULTATION = "c4a2be28-6673-4c36-b886-ea89b0a42116";
		
		public static final String PREP_DISCONTINUATION = "3c37b7a0-9f83-4fdc-994f-17308c22b423";
		
		public static final String PREP_ENROLLMENT = "35468fe8-a889-4cd4-9b35-27ac98bdd750";
	}
	
	public static final class _Form {
		
		public static final String PREP_ENROLLMENT = "d5ca78be-654e-4d23-836e-a934739be555";
		
		public static final String PREP_DISCONTINUATION = "467c4cc3-25eb-4330-9cf6-e41b9b14cc10";
		
		public static final String PREP_FOLLOW_UP = "ee3e2017-52c0-4a54-99ab-ebb542fb8984";
	}
	
	public static final class _Program {
		
		public static final String PREP = "214cad1c-bb62-4d8e-b927-810a046daf62";
	}
	
	public static final class _Concept {
		
		public static final String PREP = "1086AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	}
	
	/**
	 * @see org.openmrs.module.metadatadeploy.bundle.AbstractMetadataBundle#install()
	 */
	@Override
	public void install() {
		///////////////////////////// MCH child services ////////////////////////////////
		
		install(encounterType("PREP Enrollment", "Enrollment of client onto PREP program", _EncounterType.PREP_ENROLLMENT));
		install(encounterType("PREP Consultation", "Collection of client data during PREP visit",
		    _EncounterType.PREP_CONSULTATION));
		install(encounterType("PREP Client Discontinuation", "Discontinuation of client from PREP program",
		    _EncounterType.PREP_DISCONTINUATION));
		
		install(form("PREP  Client Enrolment Form", "PREP Enrollment form", _EncounterType.PREP_ENROLLMENT, "1.0",
		    _Form.PREP_ENROLLMENT));
		install(form("PREP Client Discontinuation", "PREP discontinuation form", _EncounterType.PREP_DISCONTINUATION, "1.0",
		    _Form.PREP_DISCONTINUATION));
		install(form("PREP Follow Up", "PREP follow up form", _EncounterType.PREP_CONSULTATION, "1.0", _Form.PREP_FOLLOW_UP));
		
		install(program("PREP Services", "PREP Services", _Concept.PREP, _Program.PREP));
		
	}
}
