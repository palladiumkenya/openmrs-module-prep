/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.prep.fragment.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.*;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.ContextAuthenticationException;
import org.openmrs.calculation.result.CalculationResult;
import org.openmrs.module.prep.EmrConstants;
//import org.openmrs.module.prep.Metadata;
import static org.openmrs.module.prep.metadata.PrepMetadata._Program.PREP;

import org.openmrs.module.kenyaui.KenyaUiUtils;
import org.openmrs.module.kenyaui.annotation.AppAction;
import org.openmrs.module.kenyaui.annotation.PublicAction;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.action.SuccessResult;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Fragment actions generally useful for KenyaEMR
 */
public class EmrUtilsFragmentController {
	
	protected static final Log log = LogFactory.getLog(EmrUtilsFragmentController.class);
	
	/**
	 * Checks if current user session is authenticated
	 * 
	 * @return simple object {authenticated: true/false}
	 */
	@PublicAction
	public SimpleObject isAuthenticated() {
		return SimpleObject.create("authenticated", Context.isAuthenticated());
	}
	
	/**
	 * Attempt to authenticate current user session with the given credentials
	 * 
	 * @param username the username
	 * @param password the password
	 * @return simple object {authenticated: true/false}
	 */
	@PublicAction
	public SimpleObject authenticate(@RequestParam(value = "username", required = false) String username,
	        @RequestParam(value = "password", required = false) String password) {
		try {
			Context.authenticate(username, password);
		}
		catch (ContextAuthenticationException ex) {
			// do nothing
		}
		return isAuthenticated();
	}
	
	/**
	 * Checks whether provided identifier(s) is already assigned
	 * 
	 * @return simple object with statuses for the different identifiers
	 */
	public SimpleObject identifierExists(@RequestParam(value = "upn", required = false) String upn,
	        @RequestParam(value = "clinicNumber", required = false) String clinicNumber,
	        @RequestParam(value = "nhifNumber", required = false) String nhifNumber) {
		boolean upnExists = false;
		boolean clinicNoExists = false;
		boolean nhifNoExists = false;
		PatientService patientService = Context.getPatientService();
		
		if (upn != null && upn != "") {
			List<Patient> patientsFound = patientService.getPatients(null, upn.trim(),
			    Arrays.asList(MetadataUtils.existing(PatientIdentifierType.class, "ac64e5cb-e3e2-4efa-9060-0dd715a843a1")),
			    true);
			if (patientsFound.size() > 0)
				upnExists = true;
		}
		if (clinicNumber != null && clinicNumber != "") {
			List<Patient> patientsFound = patientService.getPatients(null, clinicNumber.trim(),
			    Arrays.asList(MetadataUtils.existing(PatientIdentifierType.class, "b4d66522-11fc-45c7-83e3-39a1af21ae0d")),
			    true);
			if (patientsFound.size() > 0)
				clinicNoExists = true;
		}
		if (nhifNumber != null && nhifNumber != "") {
			List<Patient> patientsFound = patientService.getPatients(null, clinicNumber.trim(),
			    Arrays.asList(MetadataUtils.existing(PatientIdentifierType.class, "09ebf4f9-b673-4d97-b39b-04f94088ba64")),
			    true);
			if (patientsFound.size() > 0)
				clinicNoExists = true;
		}
		return SimpleObject.create("upnExists", upnExists, "clinicNumberExists", clinicNoExists, "nhifNumberExists",
		    nhifNoExists);
	}
	
	/**
	 * Checks whether provided identifier(s) is already assigned
	 * 
	 * @return simple object with statuses for the different identifiers Uses Next appointments for
	 *         HIV greencard Triage and HIV consultation
	 */
	public SimpleObject clientsBookedForHivConsultationOnDate(@RequestParam(value = "appointmentDate") String tca) {
		
		System.out.println("Date passed from browser: " + tca);
		String appointmentQuery = "select count(patient_id) as bookings\n"
		        + "from (\n"
		        + "select\n"
		        + "e.patient_id\n"
		        + "from encounter e\n"
		        + "inner join\n"
		        + "(\n"
		        + "\tselect encounter_type_id, uuid, name from encounter_type where uuid in('a0034eee-1940-4e35-847f-97537a35d05e','d1059fb9-a079-4feb-a749-eedd709ae542', '465a92f2-baf8-42e9-9612-53064be868e8')\n"
		        + ") et on et.encounter_type_id=e.encounter_type\n"
		        + "inner join obs o on o.encounter_id=e.encounter_id and o.voided=0\n"
		        + "\tand o.concept_id in (5096) and date(o.value_datetime) = date('" + tca + "')\n" + "where e.voided=0\n"
		        + "group by e.patient_id\n" + ")t;";
		
		Long bookings = (Long) Context.getAdministrationService().executeSQL(appointmentQuery, true).get(0).get(0);
		
		return SimpleObject.create("bookingsOnDate", bookings);
		
	}
	
	/**
	 * Checks whether provided identifier(s) is already assigned
	 * 
	 * @return simple object with statuses for the different identifiers Uses Next appointments for
	 *         MCH consultation and CWC consulation
	 */
	public SimpleObject clientsBookedForMchConsultationOnDate(@RequestParam(value = "appointmentDate") String tca) {
		
		System.out.println("Date passed from browser: " + tca);
		String appointmentQuery = "select count(patient_id) as bookings\n"
		        + "from (\n"
		        + "select\n"
		        + "e.patient_id\n"
		        + "from encounter e\n"
		        + "inner join\n"
		        + "(\n"
		        + "\tselect encounter_type_id, uuid, name from encounter_type where uuid in('c6d09e05-1f25-4164-8860-9f32c5a02df0','bcc6da85-72f2-4291-b206-789b8186a021')\n"
		        + ") et on et.encounter_type_id=e.encounter_type\n"
		        + "inner join obs o on o.encounter_id=e.encounter_id and o.voided=0\n"
		        + "\tand o.concept_id in (5096) and date(o.value_datetime) = date('" + tca + "')\n" + "where e.voided=0\n"
		        + "group by e.patient_id\n" + ")t;";
		
		Long bookings = (Long) Context.getAdministrationService().executeSQL(appointmentQuery, true).get(0).get(0);
		
		return SimpleObject.create("bookingsOnDate", bookings);
	}
	
	/**
	 * Checks whether provided identifier(s) is already assigned
	 * 
	 * @return simple object with statuses for the different identifiers Uses last recorded lot
	 *         number to prepopulate lot numbers in HTS tests
	 */
	public SimpleObject lastLotNumberUsedForHTSTesting() {
		
		String getLastLotNumberQuery = "select\n"
		        + "  max(if(o.concept_id=164964,trim(o.value_text),null)) as lot_no,\n"
		        + "  max(if(o.concept_id=162502,date(o.value_datetime),null)) as expiry_date\n"
		        + "from openmrs.obs o\n"
		        + "  inner join openmrs.encounter e on e.encounter_id = o.encounter_id\n"
		        + "  inner join openmrs.form f on f.form_id=e.form_id and f.uuid in ('72aa78e0-ee4b-47c3-9073-26f3b9ecc4a7')\n"
		        + "where o.concept_id in (164962,164964, 162502) and o.voided=0\n"
		        + "group by e.encounter_id ORDER BY e.encounter_id DESC limit 1 ;";
		
		Long lastLotNumber = (Long) Context.getAdministrationService().executeSQL(getLastLotNumberQuery, true).get(0).get(0);
		
		return SimpleObject.create("lastLotNumber", lastLotNumber);
		
	}
	
	/**
	 * Voids the given relationship
	 * 
	 * @param relationship the relationship
	 * @param reason the reason for voiding
	 * @return the simplified visit
	 */
	public SuccessResult voidRelationship(@RequestParam("relationshipId") Relationship relationship,
	        @RequestParam("reason") String reason) {
		Context.getPersonService().voidRelationship(relationship, reason);
		return new SuccessResult("Relationship voided");
	}
	
	/**
	 * Calculates an estimated birthdate from an age value
	 * 
	 * @param now the current time reference
	 * @param age the age
	 * @return the ISO8601 formatted birthdate
	 */
	public SimpleObject birthdateFromAge(@RequestParam(value = "age") Integer age,
	        @RequestParam(value = "now", required = false) Date now, @SpringBean KenyaUiUtils kenyaui) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(now != null ? now : new Date());
		cal.add(Calendar.YEAR, -age);
		return SimpleObject.create("birthdate", kenyaui.formatDateParam(cal.getTime()));
	}
}
