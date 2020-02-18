/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.prep.form.velocity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.*;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.result.CalculationResult;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.kenyaemr.Dictionary;
import org.openmrs.module.kenyaemr.calculation.EmrCalculationUtils;
import org.openmrs.module.prep.calculation.library.prep.LabaratoryVelocityCalculation;
import org.openmrs.module.prep.calculation.library.prep.PrepVelocityCalculation;
import org.openmrs.module.reporting.common.DateUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Velocity functions for adding logic to HTML forms
 */
public class PrepVelocityFunctions {
	
	private FormEntrySession session;
	
	protected static final Log log = LogFactory.getLog(PrepVelocityFunctions.class);
	
	/**
	 * Constructs a new functions provider
	 * 
	 * @param session the form entry session
	 */
	public PrepVelocityFunctions(FormEntrySession session) {
		this.session = session;
	}
	
	/**
	 * Fetches a concept from its identifier
	 * 
	 * @param conceptIdentifier the concept identifier
	 * @return the concept
	 * @throws org.openmrs.module.metadatadeploy.MissingMetadataException if no such concept exists
	 */
	public Concept getConcept(String conceptIdentifier) {
		return Dictionary.getConcept(conceptIdentifier);
	}
	
	public String LabaratoryVelocityCalculation() {
		
		CalculationResult labVelocity = EmrCalculationUtils.evaluateForPatient(LabaratoryVelocityCalculation.class, null,
		    session.getPatient());
		return (String) labVelocity.getValue();
		
	}
	
	public String PrepVelocityCalculation() {
		
		CalculationResult prepVelocity = EmrCalculationUtils.evaluateForPatient(PrepVelocityCalculation.class, null,
		    session.getPatient());
		return (String) prepVelocity.getValue();
		
	}
	
	/**
	 * Fetches a global property value by property name
	 * 
	 * @param name the property name
	 * @return the global property value
	 */
	public String getGlobalProperty(String name) {
		return Context.getAdministrationService().getGlobalProperty(name);
	}
	
	/**
	 * Gets all of the obs with the given concept for the current patient
	 * 
	 * @param conceptIdentifier the concept identifier
	 * @return the list of obs
	 */
	public String location() {
		AdministrationService administrationService = Context.getAdministrationService();
		GlobalProperty globalProperty = administrationService.getGlobalPropertyObject("kenyaemr.defaultLocation");
		if (globalProperty.getValue() != null) {
			return ((Location) globalProperty.getValue()).getName();
		}
		return "Unknown Location";
	}
	
	public List<Obs> allObs(String conceptIdentifier) {
		if (session.getPatient() == null)
			return new ArrayList<Obs>();
		
		Patient p = session.getPatient();
		if (p == null)
			return new ArrayList<Obs>();
		else
			return Context.getObsService().getObservationsByPersonAndConcept(p, getConcept(conceptIdentifier));
	}
	
	/**
	 * Gets the latest obs with the given concept for the current patient
	 * 
	 * @param conceptIdentifier the concept identifier
	 * @return the most recent obs
	 */
	public Obs latestObs(String conceptIdentifier) {
		List<Obs> obs = allObs(conceptIdentifier);
		if (obs == null || obs.isEmpty())
			return null;
		else
			return obs.get(0);
	}
	
	/**
	 * Gets the earliest obs with the given concept for the current patient
	 * 
	 * @param conceptIdentifier the concept identifier
	 * @return the earliest obs
	 */
	public Obs earliestObs(String conceptIdentifier) {
		List<Obs> obs = allObs(conceptIdentifier);
		if (obs == null || obs.isEmpty())
			return null;
		else
			return obs.get(obs.size() - 1);
	}
	
	/**
	 * Looks for an obs on the same calendar day as today, that is not in the same encounter being
	 * edited (if any)
	 * 
	 * @param conceptIdentifier the obs's concept id, mapping or UUID
	 * @return the obs
	 */
	public Obs obsToday(String conceptIdentifier) {
		Encounter toSkip = session.getEncounter();
		List<Person> p = Collections.singletonList((Person) session.getPatient());
		Concept concept = Dictionary.getConcept(conceptIdentifier);
		Date startOfDay = DateUtil.getStartOfDay(new Date());
		List<Obs> candidates = Context.getObsService().getObservations(p, null, Collections.singletonList(concept), null,
		    null, null, null, null, null, startOfDay, null, false);
		for (Obs candidate : candidates) {
			if (toSkip == null || candidate.getEncounter() == null || !candidate.getEncounter().equals(toSkip)) {
				return candidate;
			}
		}
		return null;
	}
}
