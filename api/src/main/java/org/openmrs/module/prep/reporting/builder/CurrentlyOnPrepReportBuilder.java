/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.prep.reporting.builder;

import org.openmrs.PatientIdentifierType;
import org.openmrs.module.kenyacore.report.HybridReportDescriptor;
import org.openmrs.module.kenyacore.report.ReportDescriptor;
import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.kenyacore.report.builder.AbstractHybridReportBuilder;
import org.openmrs.module.kenyacore.report.builder.Builds;
import org.openmrs.module.kenyaemr.metadata.HivMetadata;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.prep.metadata.PrepMetadata;
import org.openmrs.module.prep.reporting.cohort.definition.CurrentlyOnPrepCohortDefinition;
import org.openmrs.module.prep.reporting.data.converter.definition.prep.CurrentOnPrepVisitMonthDataDefinition;
import org.openmrs.module.prep.reporting.data.converter.definition.prep.FollowupPrEPRemarksDataDefinition;
import org.openmrs.module.prep.reporting.data.converter.definition.prep.LinkedToCareDataDefinition;
import org.openmrs.module.prep.reporting.data.converter.definition.prep.PrepPopulatonTypeDataDefinition;
import org.openmrs.module.prep.reporting.data.converter.definition.prep.PrepEnrollmentDateDataDefinition;
import org.openmrs.module.prep.reporting.data.converter.definition.prep.PrepPatientTypeDataDefinition;
import org.openmrs.module.prep.reporting.data.converter.definition.prep.PrEPVisitDateDataDefinition;
import org.openmrs.module.prep.reporting.data.converter.definition.prep.NextAppointmentDateDataDefinition;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.data.DataDefinition;
import org.openmrs.module.reporting.data.converter.BirthdateConverter;
import org.openmrs.module.reporting.data.converter.DataConverter;
import org.openmrs.module.reporting.data.converter.ObjectFormatter;
import org.openmrs.module.reporting.data.patient.definition.ConvertedPatientDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientIdentifierDataDefinition;
import org.openmrs.module.reporting.data.person.definition.*;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.PatientDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Builds({ "kenyaemr.prep.prep.report.CurrentlyOnPrepLineList" })
public class CurrentlyOnPrepReportBuilder extends AbstractHybridReportBuilder {
	
	public static final String DATE_FORMAT = "dd/MM/yyyy";
	
	@Override
	protected void addColumns(HybridReportDescriptor report, PatientDataSetDefinition dsd) {
	}
	
	@Override
	protected Mapped<CohortDefinition> buildCohort(HybridReportDescriptor descriptor, PatientDataSetDefinition dsd) {
		return null;
	}
	
	protected Mapped<CohortDefinition> allClientsCohort() {
		CohortDefinition cd = new CurrentlyOnPrepCohortDefinition();
		cd.setName("Clients currently on PrEP");
		return ReportUtils.map(cd, "");
	}
	
	@Override
	protected List<Mapped<DataSetDefinition>> buildDataSets(ReportDescriptor descriptor, ReportDefinition report) {
		
		PatientDataSetDefinition allVisits = currentlyOnPrepDataSetDefinition("currentlyOnPrep");
		allVisits.addRowFilter(allClientsCohort());
		DataSetDefinition allClientsDSD = allVisits;
		
		return Arrays.asList(ReportUtils.map(allClientsDSD, ""));
	}
	
	protected PatientDataSetDefinition currentlyOnPrepDataSetDefinition(String datasetName) {
		
		PatientDataSetDefinition dsd = new PatientDataSetDefinition(datasetName);
		PatientIdentifierType upn = MetadataUtils.existing(PatientIdentifierType.class,
		    PrepMetadata._PatientIdentifierType.PREP_UNIQUE_NUMBER);
		DataConverter identifierFormatter = new ObjectFormatter("{identifier}");
		DataDefinition identifierDef = new ConvertedPatientDataDefinition("identifier", new PatientIdentifierDataDefinition(
		        upn.getName(), upn), identifierFormatter);
		PatientIdentifierType cccNumber = MetadataUtils.existing(PatientIdentifierType.class,
		    HivMetadata._PatientIdentifierType.UNIQUE_PATIENT_NUMBER);
		
		DataDefinition identifierCCCDef = new ConvertedPatientDataDefinition("identifier",
		        new PatientIdentifierDataDefinition(cccNumber.getName(), cccNumber), identifierFormatter);
		
		DataConverter formatter = new ObjectFormatter("{familyName}, {givenName}");
		DataDefinition nameDef = new ConvertedPersonDataDefinition("name", new PreferredNameDataDefinition(), formatter);
		dsd.addColumn("id", new PersonIdDataDefinition(), "");
		dsd.addColumn("Name", nameDef, "");
		dsd.addColumn("PrEP No", identifierDef, "");
		dsd.addColumn("Sex", new GenderDataDefinition(), "", null);
		dsd.addColumn("DOB", new BirthdateDataDefinition(), "", new BirthdateConverter(DATE_FORMAT));
		dsd.addColumn("Age", new AgeDataDefinition(), "");
		dsd.addColumn("Population type", new PrepPopulatonTypeDataDefinition(), "");
		dsd.addColumn("Patient type", new PrepPatientTypeDataDefinition(), "");
		dsd.addColumn("Enrollment date", new PrepEnrollmentDateDataDefinition(), "");
		dsd.addColumn("Visit date", new PrEPVisitDateDataDefinition(), "");
		dsd.addColumn("Visit month", new CurrentOnPrepVisitMonthDataDefinition(), "");
		dsd.addColumn("Next appointment date", new NextAppointmentDateDataDefinition(), "");
		//Turned HIV Positive while on PrEP
		dsd.addColumn("Linked to Care", new LinkedToCareDataDefinition(), "");
		dsd.addColumn("CCC Number", identifierCCCDef, "");
		return dsd;
	}
}
