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
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.prep.metadata.PrepMetadata;
import org.openmrs.module.prep.reporting.cohort.definition.PrEPRegisterCohortDefinition;
import org.openmrs.module.prep.reporting.data.converter.definition.prep.*;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.common.SortCriteria;
import org.openmrs.module.reporting.data.DataDefinition;
import org.openmrs.module.reporting.data.converter.DataConverter;
import org.openmrs.module.reporting.data.converter.ObjectFormatter;
import org.openmrs.module.reporting.data.patient.definition.ConvertedPatientDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientIdentifierDataDefinition;
import org.openmrs.module.reporting.data.person.definition.*;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.PatientDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
@Builds({ "kenyaemr.prep.common.report.prepRegister" })
public class PrEPRegisterReportBuilder extends AbstractHybridReportBuilder {
	
	public static final String DATE_FORMAT = "dd/MM/yyyy";
	
	@Override
	protected Mapped<CohortDefinition> buildCohort(HybridReportDescriptor descriptor, PatientDataSetDefinition dsd) {
		return allClientsCohort();
	}
	
	protected Mapped<CohortDefinition> allClientsCohort() {
		CohortDefinition cd = new PrEPRegisterCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setName("PrEP Cohort Register");
		return ReportUtils.map(cd, "startDate=${startDate},endDate=${endDate}");
	}
	
	@Override
	protected List<Mapped<DataSetDefinition>> buildDataSets(ReportDescriptor descriptor, ReportDefinition report) {
		
		PatientDataSetDefinition allClients = kpDataSetDefinition();
		allClients.addRowFilter(allClientsCohort());
		//allPatients.addRowFilter(buildCohort(descriptor));
		DataSetDefinition allPatientsDSD = allClients;
		
		return Arrays.asList(ReportUtils.map(allPatientsDSD, "startDate=${startDate},endDate=${endDate}"));
	}
	
	@Override
	protected List<Parameter> getParameters(ReportDescriptor reportDescriptor) {
		return Arrays.asList(new Parameter("startDate", "Start Date", Date.class), new Parameter("endDate", "End Date",
		        Date.class), new Parameter("dateBasedReporting", "", String.class));
	}
	
	protected PatientDataSetDefinition kpDataSetDefinition() {
		
		PatientDataSetDefinition dsd = new PatientDataSetDefinition("KPRegister");
		dsd.addSortCriteria("DOBAndAge", SortCriteria.SortDirection.DESC);
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		
		PatientIdentifierType upn = MetadataUtils.existing(PatientIdentifierType.class,
		    PrepMetadata._PatientIdentifierType.PREP_UNIQUE_NUMBER);
		PatientIdentifierType nhif = MetadataUtils.existing(PatientIdentifierType.class,
		    PrepMetadata._PatientIdentifierType.NHIF_NUMBER);
		DataConverter identifierFormatter = new ObjectFormatter("{identifier}");
		DataDefinition identifierDef = new ConvertedPatientDataDefinition("identifier", new PatientIdentifierDataDefinition(
		        upn.getName(), upn), identifierFormatter);
		
		DataConverter nameFormatter = new ObjectFormatter("{familyName}, {givenName}");
		DataDefinition nameDef = new ConvertedPersonDataDefinition("name", new PreferredNameDataDefinition(), nameFormatter);
		dsd.addColumn("id", new PersonIdDataDefinition(), "");
		dsd.addColumn("Serial Number", new SerialNumberDataDefinition(), "");
		dsd.addColumn("Name", nameDef, "");
		dsd.addColumn("Visit Date", new VisitDateDataDefinition(), "");
		dsd.addColumn("Client Unique ID", identifierDef, "");
		dsd.addColumn("Sex", new GenderDataDefinition(), "");
		dsd.addColumn("DOB", new BirthdateDataDefinition(), "");
		dsd.addColumn("Age", new AgeDataDefinition(), "");
		dsd.addColumn("Population Type", new PopulationTypeDataDefinition(), "");
		dsd.addColumn("Assessed", new AssessedDataDefinition(), "");
		dsd.addColumn("Eligible", new EligibleDataDefinition(), "");
		dsd.addColumn("PrEP Initiation Date", new InitiationDateDataDefinition(), "");
		dsd.addColumn("PrEP Eligibility Reason at Entry", new EligibilityReasonAtEntryDataDefinition(), "");
		dsd.addColumn("HTS", new HTSDataDefinition(), "");
		dsd.addColumn("STI Screened/Results", new STIDataDefinition(), "");
		dsd.addColumn("Creatinine Clearance/Results", new CreatinineDataDefinition(), "");
		dsd.addColumn("At Risk of HIV Infection", new HIVRiskDataDefinition(), "");
		dsd.addColumn("Reason for HIV Risk", new HIVRiskReasonDataDefinition(), "");
		dsd.addColumn("Adherence", new AdherenceDataDefinition(), "");
		dsd.addColumn("Received Adherence Counselling", new AdherenceCounsellingDataDefinition(), "");
		dsd.addColumn("PrEP Status", new PrEPStatusDataDefinition(), "");
		dsd.addColumn("Discontinuation Reasons", new DiscontinueReasonDataDefinition(), "");
		dsd.addColumn("Yearly Creatinine/Test Result", new YearlyCreatinineDataDefinition(), "");
		dsd.addColumn("HBVsAG Test", new HBVAgDataDefinition(), "");
		dsd.addColumn("Linked to Care", new LinkedToCareDataDefinition(), "");
		dsd.addColumn("Remarks", new PrEPRemarksDataDefinition(), "");
		
		return dsd;
	}
}
