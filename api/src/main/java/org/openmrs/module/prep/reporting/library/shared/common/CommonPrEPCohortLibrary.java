/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.prep.reporting.library.shared.common;

import org.openmrs.Concept;
import org.openmrs.EncounterType;
import org.openmrs.Program;
import org.openmrs.api.PatientSetService;
import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.kenyacore.report.cohort.definition.CalculationCohortDefinition;
import org.openmrs.module.prep.calculation.library.prep.EligibleForPrepProgramCalculation;
import org.openmrs.module.reporting.cohort.definition.*;
import org.openmrs.module.reporting.common.DurationUnit;
import org.openmrs.module.reporting.common.SetComparator;
import org.openmrs.module.reporting.common.TimeQualifier;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

/**
 * Library of common cohort definitions
 */
@Component
public class CommonPrEPCohortLibrary {
	
	/**
	 * Patients who are female
	 * 
	 * @return the cohort definition
	 */
	public CohortDefinition females() {
		GenderCohortDefinition cd = new GenderCohortDefinition();
		cd.setName("females");
		cd.setFemaleIncluded(true);
		return cd;
	}
	
	/**
	 * Patients who are male
	 * 
	 * @return the cohort definition
	 */
	public CohortDefinition males() {
		GenderCohortDefinition cd = new GenderCohortDefinition();
		cd.setName("males");
		cd.setMaleIncluded(true);
		return cd;
	}
	
	/**
	 * Clients who are female sex workers
	 * 
	 * @return the cohort definition
	 */
	public CohortDefinition fsw() {
		GenderCohortDefinition cd = new GenderCohortDefinition();
		cd.setName("fsw");
		cd.setMaleIncluded(true);
		return cd;
	}
	
	/**
	 * Male Clients have sex with males
	 * 
	 * @return the cohort definition
	 */
	public CohortDefinition msm() {
		GenderCohortDefinition cd = new GenderCohortDefinition();
		cd.setName("msm");
		cd.setMaleIncluded(true);
		return cd;
	}
	
	/**
	 * Clients who are Male sex workers
	 * 
	 * @return the cohort definition
	 */
	public CohortDefinition msw() {
		GenderCohortDefinition cd = new GenderCohortDefinition();
		cd.setName("msw");
		cd.setMaleIncluded(true);
		return cd;
	}
	
	/**
	 * Clients who inject drugs
	 * 
	 * @return the cohort definition
	 */
	public CohortDefinition pwid() {
		GenderCohortDefinition cd = new GenderCohortDefinition();
		cd.setName("pwid");
		cd.setMaleIncluded(true);
		return cd;
	}
	
	/**
	 * Clients who use drugs
	 * 
	 * @return the cohort definition
	 */
	public CohortDefinition pwud() {
		GenderCohortDefinition cd = new GenderCohortDefinition();
		cd.setName("pwud");
		cd.setMaleIncluded(true);
		return cd;
	}
	
	/**
	 * Clients who are transgender
	 * 
	 * @return the cohort definition
	 */
	public CohortDefinition transgender() {
		GenderCohortDefinition cd = new GenderCohortDefinition();
		cd.setName("transgender");
		cd.setMaleIncluded(true);
		return cd;
	}
	
	/**
	 * Patients who at most maxAge years old on ${effectiveDate}
	 * 
	 * @return the cohort definition
	 */
	public CohortDefinition agedAtMost(int maxAge) {
		AgeCohortDefinition cd = new AgeCohortDefinition();
		cd.setName("aged at most " + maxAge);
		cd.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
		cd.setMaxAge(maxAge);
		return cd;
	}
	
	/**
	 * Patients who are at least minAge years old on ${effectiveDate}
	 * 
	 * @return the cohort definition
	 */
	public CohortDefinition agedAtLeast(int minAge) {
		AgeCohortDefinition cd = new AgeCohortDefinition();
		cd.setName("aged at least " + minAge);
		cd.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
		cd.setMinAge(minAge);
		return cd;
	}
	
	/**
	 * patients who are at least minAge years old and at most years old on ${effectiveDate}
	 * 
	 * @return CohortDefinition
	 */
	public CohortDefinition agedAtLeastAgedAtMost(int minAge, int maxAge) {
		AgeCohortDefinition cd = new AgeCohortDefinition();
		cd.setName("aged between " + minAge + " and " + maxAge + " years");
		cd.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
		cd.setMinAge(minAge);
		cd.setMaxAge(maxAge);
		return cd;
	}
	
	/**
	 * patients who are at least minAge months old and at most months old on ${effectiveDate}
	 * 
	 * @return CohortDefinition
	 */
	public CohortDefinition agedAtLeastAgedAtMostInMonths(int minAge, int maxAge) {
		AgeCohortDefinition cd = new AgeCohortDefinition();
		cd.setName("aged between " + minAge + " and " + maxAge + " years");
		cd.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
		cd.setMinAge(minAge);
		cd.setMaxAge(maxAge);
		cd.setMinAgeUnit(DurationUnit.MONTHS);
		cd.setMaxAgeUnit(DurationUnit.MONTHS);
		return cd;
	}
	
	/**
	 * Patients who are female and at least 18 years old on ${effectiveDate}
	 * 
	 * @return the cohort definition
	 */
	public CohortDefinition femalesAgedAtLeast18() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("females aged at least 18");
		cd.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
		cd.addSearch("females", ReportUtils.map(females()));
		cd.addSearch("agedAtLeast18", ReportUtils.map(agedAtLeast(18), "effectiveDate=${effectiveDate}"));
		cd.setCompositionString("females AND agedAtLeast18");
		return cd;
	}
	
	/**
	 * Patients who have an encounter between ${onOrAfter} and ${onOrBefore}
	 * 
	 * @param types the encounter types
	 * @return the cohort definition
	 */
	public CohortDefinition hasEncounter(EncounterType... types) {
		EncounterCohortDefinition cd = new EncounterCohortDefinition();
		cd.setName("has encounter between dates");
		cd.setTimeQualifier(TimeQualifier.ANY);
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		if (types.length > 0) {
			cd.setEncounterTypeList(Arrays.asList(types));
		}
		return cd;
	}
	
	/**
	 * Patients who have an obs between ${onOrAfter} and ${onOrBefore}
	 * 
	 * @param question the question concept
	 * @param answers the answers to include
	 * @return the cohort definition
	 */
	public CohortDefinition hasObs(Concept question, Concept... answers) {
		CodedObsCohortDefinition cd = new CodedObsCohortDefinition();
		cd.setName("has obs between dates");
		cd.setQuestion(question);
		cd.setOperator(SetComparator.IN);
		cd.setTimeModifier(PatientSetService.TimeModifier.ANY);
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		if (answers.length > 0) {
			cd.setValueList(Arrays.asList(answers));
		}
		return cd;
	}
	
	/**
	 * Patients who transferred in between ${onOrAfter} and ${onOrBefore}
	 * 
	 * @return the cohort definition
	 */
	/*public CohortDefinition transferredIn() {
		Concept transferInDate = Dictionary.getConcept(Dictionary.TRANSFER_IN_DATE);

		DateObsValueBetweenCohortDefinition cd = new DateObsValueBetweenCohortDefinition();
		cd.setName("transferred in between dates");
		cd.setQuestion(transferInDate);
		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		return cd;
	}*/
	
	/**
	 * Patients who transferred out between ${onOrAfter} and ${onOrBefore}
	 * 
	 * @return the cohort definition
	 */
	/*public CohortDefinition transferredOut() {
		Concept reasonForDiscontinue = Dictionary.getConcept(Dictionary.REASON_FOR_PROGRAM_DISCONTINUATION);
		Concept transferredOut = Dictionary.getConcept(Dictionary.TRANSFERRED_OUT);

		CodedObsCohortDefinition cd = new CodedObsCohortDefinition();
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.setName("transferred out between dates");
		cd.setTimeModifier(PatientSetService.TimeModifier.ANY);
		cd.setQuestion(reasonForDiscontinue);
		cd.setOperator(SetComparator.IN);
		cd.setValueList(Collections.singletonList(transferredOut));
		return cd;
	}*/
	
	/**
	 * Patients who were enrolled on the given programs between ${enrolledOnOrAfter} and
	 * ${enrolledOnOrBefore}
	 * 
	 * @param programs the programs
	 * @return the cohort definition
	 */
	public CohortDefinition enrolled(Program... programs) {
		ProgramEnrollmentCohortDefinition cd = new ProgramEnrollmentCohortDefinition();
		cd.setName("enrolled in program between dates");
		cd.addParameter(new Parameter("enrolledOnOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("enrolledOnOrBefore", "Before Date", Date.class));
		if (programs.length > 0) {
			cd.setPrograms(Arrays.asList(programs));
		}
		return cd;
	}
	
	/**
	 * Patients who were enrolled on the given programs (excluding transfers) between ${onOrAfter}
	 * and ${onOrBefore}
	 * 
	 * @param programs the programs
	 * @return the cohort definition
	 */
	public CohortDefinition enrolledExcludingTransfers(Program... programs) {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("enrolled excluding transfers in program between dates");
		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		cd.addSearch("enrolled",
		    ReportUtils.map(enrolled(programs), "enrolledOnOrAfter=${onOrAfter},enrolledOnOrBefore=${onOrBefore}"));
		//cd.addSearch("transferIn", ReportUtils.map(transferredIn(), "onOrBefore=${onOrBefore}"));
		cd.addSearch("completeProgram", ReportUtils.map(compltedProgram(), "completedOnOrBefore=${onOrBefore}"));
		cd.setCompositionString("enrolled AND NOT (transferIn OR completeProgram)");
		return cd;
	}
	
	/**
	 * Patients who were enrolled on the given programs (excluding transfers) on ${onOrBefore}
	 * 
	 * @param programs the programs
	 * @return the cohort definition
	 */
	public CohortDefinition enrolledExcludingTransfersOnDate(Program... programs) {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("enrolled excluding transfers in program on date in this facility");
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		cd.addSearch("enrolled", ReportUtils.map(enrolled(programs), "enrolledOnOrBefore=${onOrBefore}"));
		//cd.addSearch("transferIn", ReportUtils.map(transferredIn(), "onOrBefore=${onOrBefore}"));
		cd.setCompositionString("enrolled AND NOT transferIn");
		return cd;
		
	}
	
	/**
	 * Patients who are pregnant on ${onDate}
	 * 
	 * @return the cohort definition
	 */
	/*public CohortDefinition pregnant() {
		CalculationCohortDefinition cd = new CalculationCohortDefinition(new IsPregnantCalculation());
		cd.setName("pregnant on date");
		cd.addParameter(new Parameter("onDate", "On Date", Date.class));
		return cd;
	}*/
	
	/**
	 * Patients who are in the specified program on ${onDate}
	 * 
	 * @param program the program
	 * @return
	 */
	public CohortDefinition inProgram(Program program) {
		CalculationCohortDefinition cd = new CalculationCohortDefinition(new EligibleForPrepProgramCalculation());
		cd.setName("in " + program.getName() + " on date");
		cd.addParameter(new Parameter("onDate", "On Date", Date.class));
		cd.addCalculationParameter("program", program);
		return cd;
	}
	
	/**
	 * Patients who were dispensed the given medications between ${onOrAfter} and ${onOrBefore}
	 * 
	 * @param concepts the drug concepts
	 * @return the cohort definition
	 */
	public CohortDefinition medicationDispensed(Concept... concepts) {
		CodedObsCohortDefinition cd = new CodedObsCohortDefinition();
		cd.setName("dispensed medication between");
		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		cd.setTimeModifier(PatientSetService.TimeModifier.ANY);
		//cd.setQuestion(Dictionary.getConcept(Dictionary.MEDICATION_ORDERS));
		cd.setValueList(Arrays.asList(concepts));
		cd.setOperator(SetComparator.IN);
		return cd;
	}
	
	/**
	 * Patients who completed program ${onOrAfter} and ${onOrBefore}
	 * 
	 * @return the cohort definition
	 */
	public CohortDefinition compltedProgram() {
		ProgramEnrollmentCohortDefinition cd = new ProgramEnrollmentCohortDefinition();
		cd.setName("Those patients who completed program on date");
		cd.addParameter(new Parameter("completedOnOrBefore", "Complete Date", Date.class));
		//cd.setPrograms(Arrays.asList(MetadataUtils.existing(Program.class, KpMetadata._Program.HIV)));
		return cd;
	}
	
}
