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
import org.openmrs.api.AdministrationService;
import org.openmrs.module.metadatadeploy.bundle.AbstractMetadataBundle;
import org.springframework.stereotype.Component;

import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.*;

/**
 * PREP metadata bundle
 */
@Component
public class PrepMetadata extends AbstractMetadataBundle {
	public static final String GP_ENABLE_FORMS = "kenyaemr.enable.forms";

	public static final class _EncounterType {

		public static final String PREP_CONSULTATION = "c4a2be28-6673-4c36-b886-ea89b0a42116";

		public static final String PREP_DISCONTINUATION = "3c37b7a0-9f83-4fdc-994f-17308c22b423";

		public static final String PREP_ENROLLMENT = "35468fe8-a889-4cd4-9b35-27ac98bdd750";

		public static final String PREP_BEHAVIOR_RISK_ASSESSMENT = "6e5ec039-8d2a-4172-b3fb-ee9d0ba647b7";

		public static final String PREP_STI_SCREENING = "83610d13-d4fc-42c3-8c1d-a403cd6dd073";

		public static final String PREP_VMMC_SCREENING = "402c10a3-d419-4040-b5d8-bde0af646405";

		public static final String PREP_FERTILITY_INTENTIONS_SCREENING = "c4657c33-f252-4ba9-8a4f-b09ed0deda75";

		public static final String PREP_ALLERGIES_SCREENING = "119362fb-6af6-4462-9fb2-7a09c43c9874";

		public static final String PREP_CHRONIC_ILLNESS = "26bb869b-b569-4acd-b455-02c853e9f1e6";

		public static final String PREP_DRUG_REACTIONS = "d7cfa460-2944-11e9-b210-d663bd873d93";

		public static final String PREP_STATUS = "47c73adb-f9db-4c79-b582-e16064f9cee0";

		public static final String PREP_PREGNANCY_OUTCOMES = "5feffc6c-3194-43df-9b80-290054216c35";

		public static final String PREP_APPOINTMENT = "66609dee-3438-11e9-b210-d663bd873d93";

		public static final String PREP_HEPATITIS_SCREENING = "5c05a229-51b4-4b73-be13-0d93765a2a96";

		public static final String PREP_MONTHLY_REFILL = "291c0828-a216-11e9-a2a3-2a2ae2dbcce4";

		public static final String PREP_INITIAL_FOLLOWUP = "706a8b12-c4ce-40e4-aec3-258b989bf6d3";
	}

	public static final class _Form {

		public static final String PREP_ENROLLMENT_FORM = "d5ca78be-654e-4d23-836e-a934739be555";

		public static final String PREP_DISCONTINUATION_FORM = "467c4cc3-25eb-4330-9cf6-e41b9b14cc10";

		public static final String PREP_CONSULTATION_FORM = "ee3e2017-52c0-4a54-99ab-ebb542fb8984";

		public static final String PREP_BEHAVIOR_RISK_ASSESSMENT_FORM = "40374909-05fc-4af8-b789-ed9c394ac785";

		public static final String PREP_PROGRESS_NOTE_FORM = "c48ed2a2-0a0f-4f4e-9fed-a79ca3e1a9b9";

		public static final String PREP_STI_SCREENING_FORM = "d80d1c52-6a79-4c3d-b322-63eead834089";

		public static final String PREP_VMMC_SCREENING_FORM = "399588e5-c4af-47d0-bf8e-7d12a8c9c80a";

		public static final String PREP_FERTILITY_INTENTIONS_SCREENING_FORM = "df590414-1e27-4937-b81a-f1504b2d2aef";

		public static final String PREP_ALLERGIES_SCREENING_FORM = "cdad5adb-e352-4ecf-882d-b76b71be9c9d";

		public static final String PREP_CHRONIC_ILLNESS_FORM = "458a1a0a-fb8e-4a37-a836-d47e63673b60";

		public static final String PREP_DRUG_REACTIONS_FORM = "4464390d-025d-47bd-9619-64cb1d89a1da";

		public static final String PREP_STATUS_FORM = "0de4e6a4-96e7-4281-a229-5195bae4cb0b";

		public static final String PREP_PREGNANCY_OUTCOMES_FORM = "7699a58b-7022-43a7-87de-7d067ae572b9";

		public static final String PREP_APPOINTMENT_FORM = "7587529e-9d84-4947-953e-afe5643cc816";

		public static final String PREP_HEPATITIS_SCREENING_FORM = "c0151dfc-6097-4eb1-8226-484303dcdc88";

		public static final String PREP_MONTHLY_REFILL_FORM = "291c03c8-a216-11e9-a2a3-2a2ae2dbcce4";

		public static final String PREP_INITIAL_FOLLOWUP_FORM = "1bfb09fc-56d7-4108-bd59-b2765fd312b8";

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

		// Installing encounters
		install(encounterType("PrEP Enrollment", "Enrollment of client onto PrEP program",
				_EncounterType.PREP_ENROLLMENT));
		install(encounterType("PrEP Consultation", "Collection of client data during PREP visit",
				_EncounterType.PREP_CONSULTATION));
		install(encounterType("PrEP Behavior Risk Assessment", "Collection of client data for PrEP eligibility",
				_EncounterType.PREP_BEHAVIOR_RISK_ASSESSMENT));
		install(encounterType("PrEP Client Discontinuation", "Discontinuation of client from PrEP program",
				_EncounterType.PREP_DISCONTINUATION));
		install(encounterType("PrEP STI Screening", "Handles PrEP STI Screening", _EncounterType.PREP_STI_SCREENING));
		install(encounterType("PrEP VMMC Screening", "Handles PrEP VMMC Screening",
				_EncounterType.PREP_VMMC_SCREENING));
		install(encounterType("Fertility Intention Screening", "Handles KP Pregnancy and FP screening",
				_EncounterType.PREP_FERTILITY_INTENTIONS_SCREENING));
		install(encounterType("PrEP Allergies screening", "Handles PrEP Allergies screening",
				_EncounterType.PREP_ALLERGIES_SCREENING));
		install(encounterType("PrEP Chronic Illness Screening", "Handles PrEP Chronic Illness",
				_EncounterType.PREP_CHRONIC_ILLNESS));
		install(encounterType("PrEP Adverse drug reactions", "Handles PrEP Adverse drug reactions",
				_EncounterType.PREP_DRUG_REACTIONS));
		install(encounterType("PrEP Status", "Handles PREP Status Assessment", _EncounterType.PREP_STATUS));
		install(encounterType("PrEP Pregnancy Outcomes", "Handles PrEP Pregnancy Outcomes",
				_EncounterType.PREP_PREGNANCY_OUTCOMES));
		install(encounterType("PrEP Appointment creation", "Handles PrEP Appointment creation",
				_EncounterType.PREP_APPOINTMENT));
		install(encounterType("PrEP Monthly refill", "Handles PrEP Monthly refill",
				_EncounterType.PREP_MONTHLY_REFILL));
		install(encounterType("PrEP Initial", "Handles PrEP Initial encounter", _EncounterType.PREP_INITIAL_FOLLOWUP));

		// Installing forms
		boolean installForms = shouldInstallForms();
		if (installForms) {
			install(form("PrEP Behavior Risk Assessment in the last six months ", "PrEP Behavior Risk Assessment Form",
					_EncounterType.PREP_BEHAVIOR_RISK_ASSESSMENT, "1.0", _Form.PREP_BEHAVIOR_RISK_ASSESSMENT_FORM));
			install(form("PrEP INITIATION ", "PrEP Enrollment form", _EncounterType.PREP_ENROLLMENT, "1.0",
					_Form.PREP_ENROLLMENT_FORM));
			install(form("PrEP Client Discontinuation", "PrEP discontinuation form",
					_EncounterType.PREP_DISCONTINUATION, "1.0",
					_Form.PREP_DISCONTINUATION_FORM));
			install(form("PrEP Follow Up", "PrEP follow up form", _EncounterType.PREP_CONSULTATION, "1.0",
					_Form.PREP_CONSULTATION_FORM));
			install(form("PrEP Progress Notes", "PrEP Progress Notes", _EncounterType.PREP_CONSULTATION, "1.0",
					_Form.PREP_PROGRESS_NOTE_FORM));
			install(form("PrEP STI Screening", null, _EncounterType.PREP_STI_SCREENING, "1",
					_Form.PREP_STI_SCREENING_FORM));
			install(form("PrEP VMMC Screening", null, _EncounterType.PREP_VMMC_SCREENING, "1",
					_Form.PREP_VMMC_SCREENING_FORM));
			install(form("Fertility Intention Screening", null, _EncounterType.PREP_FERTILITY_INTENTIONS_SCREENING, "1",
					_Form.PREP_FERTILITY_INTENTIONS_SCREENING_FORM));
			install(form("Allergies Screening", null, _EncounterType.PREP_ALLERGIES_SCREENING, "1",
					_Form.PREP_ALLERGIES_SCREENING_FORM));
			install(form("Chronic Illness", null, _EncounterType.PREP_CHRONIC_ILLNESS, "1",
					_Form.PREP_CHRONIC_ILLNESS_FORM));
			install(form("Adverse Drug Reactions", null, _EncounterType.PREP_DRUG_REACTIONS, "1",
					_Form.PREP_DRUG_REACTIONS_FORM));
			install(form("PrEP Status", null, _EncounterType.PREP_STATUS, "1", _Form.PREP_STATUS_FORM));
			install(form("Pregnancy Outcomes", null, _EncounterType.PREP_PREGNANCY_OUTCOMES, "1",
					_Form.PREP_PREGNANCY_OUTCOMES_FORM));
			install(form("Appointment Creation", null, _EncounterType.PREP_APPOINTMENT, "1",
					_Form.PREP_APPOINTMENT_FORM));
			install(form("PrEP Monthly Refill Form", null, _EncounterType.PREP_MONTHLY_REFILL, "1",
					_Form.PREP_MONTHLY_REFILL_FORM));
			install(form("PrEP Initial Form", null, _EncounterType.PREP_INITIAL_FOLLOWUP, "1",
					_Form.PREP_INITIAL_FOLLOWUP_FORM));
		} else {
			logger.info("=== SKIPPING form installation because shouldInstallForms() returned false ===");
		}

		// Installing identifiers

		install(patientIdentifierType("NHIF Number", "PREP client Insurance number ", null, null, null,
				PatientIdentifierType.LocationBehavior.NOT_USED, false, _PatientIdentifierType.NHIF_NUMBER));
		install(patientIdentifierType("PREP Unique Number", "Unique Number assigned to PREP client upon enrollment",
				null,
				null, null, PatientIdentifierType.LocationBehavior.NOT_USED, false,
				_PatientIdentifierType.PREP_UNIQUE_NUMBER));

		// Installing program
		install(program("PrEP", "Pre exposure prophylaxis program", _Concept.PREP, _Program.PREP));

		// PrEP global properties
		install(globalProperty("prep.weight", "Weight limit for one to be eligible for PrEP", "30"));
		install(globalProperty("prep.age", "Age limit for one to be eligible for PrEP", "15"));
		install(globalProperty("prep.htsInitialPeriod",
				"Time limit in days for hts to be valid for initiation into PrEP",
				"3"));

	}

	private boolean shouldInstallForms() {
		AdministrationService administrationService = Context.getAdministrationService();
		org.openmrs.GlobalProperty gp = administrationService.getGlobalPropertyObject(GP_ENABLE_FORMS);
		if (gp == null || gp.getPropertyValue() == null) {
			// Default to true if property doesn't exist (backward compatibility)
			return true;
		}
		return gp.getPropertyValue().trim().equalsIgnoreCase("true");
	}
}
