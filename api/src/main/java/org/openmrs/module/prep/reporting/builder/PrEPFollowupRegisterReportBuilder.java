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
import org.openmrs.module.kenyacore.report.ReportDescriptor;
import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.kenyacore.report.builder.AbstractReportBuilder;
import org.openmrs.module.kenyacore.report.builder.Builds;
import org.openmrs.module.kenyaemr.metadata.HivMetadata;
import org.openmrs.module.kenyaemr.reporting.ColumnParameters;
import org.openmrs.module.kenyaemr.reporting.data.converter.IdentifierConverter;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.prep.metadata.PrepMetadata;
import org.openmrs.module.prep.reporting.cohort.definition.PrEPFollowupRegisterCohortDefinition;
import org.openmrs.module.prep.reporting.cohort.definition.PrEPInitiationRegisterCohortDefinition;
import org.openmrs.module.prep.reporting.data.converter.definition.evaluator.prep.FollowupSTIDataDefinitionEvaluator;
import org.openmrs.module.prep.reporting.data.converter.definition.prep.*;
import org.openmrs.module.prep.reporting.library.ETLReports.MOH731B.ETLMoh731BIndicatorLibrary;
import org.openmrs.module.prep.reporting.library.shared.common.CommonPrEPDimensionLibrary;
import org.openmrs.module.reporting.common.SortCriteria;
import org.openmrs.module.reporting.data.DataDefinition;
import org.openmrs.module.reporting.data.converter.BirthdateConverter;
import org.openmrs.module.reporting.data.converter.DataConverter;
import org.openmrs.module.reporting.data.converter.DateConverter;
import org.openmrs.module.reporting.data.converter.ObjectFormatter;
import org.openmrs.module.reporting.data.encounter.definition.EncounterDatetimeDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.ConvertedPatientDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientIdentifierDataDefinition;
import org.openmrs.module.reporting.data.person.definition.*;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.EncounterDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
@Builds({ "kenyaemr.prep.prep.report.prepFollowupRegister" })
public class PrEPFollowupRegisterReportBuilder extends AbstractReportBuilder {
	
	@Autowired
	private CommonPrEPDimensionLibrary commonDimensions;
	
	@Autowired
	private ETLMoh731BIndicatorLibrary moh731BIndicators;
	
	public static final String DATE_FORMAT = "dd/MM/yyyy";
	
	@SuppressWarnings("unchecked")
	@Override
	protected List<Mapped<DataSetDefinition>> buildDataSets(ReportDescriptor descriptor, ReportDefinition report) {
		return Arrays.asList(ReportUtils.map(prepFollowUpDataSetDefinition(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	@Override
	protected List<Parameter> getParameters(ReportDescriptor reportDescriptor) {
		return Arrays.asList(new Parameter("startDate", "Start Date", Date.class), new Parameter("endDate", "End Date",
		        Date.class), new Parameter("dateBasedReporting", "", String.class));
	}
	
	protected DataSetDefinition prepFollowUpDataSetDefinition() {
		
		EncounterDataSetDefinition dsd = new EncounterDataSetDefinition("PrEPFollowupRegister");
		dsd.addSortCriteria("DOBAndAge", SortCriteria.SortDirection.DESC);
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		String paramMapping = "startDate=${startDate},endDate=${endDate}";
		
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		
		PatientIdentifierType upn = MetadataUtils.existing(PatientIdentifierType.class,
		    PrepMetadata._PatientIdentifierType.PREP_UNIQUE_NUMBER);
		PatientIdentifierType cccNumber = MetadataUtils.existing(PatientIdentifierType.class,
		    HivMetadata._PatientIdentifierType.UNIQUE_PATIENT_NUMBER);
		PatientIdentifierType nhif = MetadataUtils.existing(PatientIdentifierType.class,
		    PrepMetadata._PatientIdentifierType.NHIF_NUMBER);
		DataConverter identifierFormatter = new ObjectFormatter("{identifier}");
		
		DataDefinition identifierDef = new ConvertedPatientDataDefinition("identifier", new PatientIdentifierDataDefinition(
		        upn.getName(), upn), identifierFormatter);
		
		DataDefinition identifierCCCDef = new ConvertedPatientDataDefinition("identifier",
		        new PatientIdentifierDataDefinition(cccNumber.getName(), cccNumber), identifierFormatter);
		
		DataConverter nameFormatter = new ObjectFormatter("{familyName}, {givenName} {middleName}");
		DataDefinition nameDef = new ConvertedPersonDataDefinition("name", new PreferredNameDataDefinition(), nameFormatter);
		dsd.addColumn("id", new PersonIdDataDefinition(), "");
		//dsd.addColumn("Serial Number", new SerialNumberDataDefinition(), "");
		dsd.addColumn("Name", nameDef, "");
		dsd.addColumn("Client Unique ID", identifierDef, "");
		dsd.addColumn("Sex", new GenderDataDefinition(), "");
		dsd.addColumn("DOB", new BirthdateDataDefinition(), "", new BirthdateConverter(DATE_FORMAT));
		dsd.addColumn("Age", new AgeDataDefinition(), "");
		dsd.addColumn("Population Type", new PrepFollowUpPopulatonTypeDataDefinition(), paramMapping);
		dsd.addColumn("Visit month", new PrEPVisitMonthDataDefinition(), "");
		dsd.addColumn("Visit Date", new EncounterDatetimeDataDefinition(), "", new DateConverter(DATE_FORMAT));
		dsd.addColumn("HTS Done", new FollowupHTSDoneDataDefinition(), "");
		dsd.addColumn("HTS Results", new FollowupHTSResultsDataDefinition(), "");
		dsd.addColumn("STI Screened", new FollowupSTIDataDefinition(), "");
		dsd.addColumn("STI Diagnosis", new FollowupSTIDiagnosisDataDefinition(), "");
		dsd.addColumn("At risk of HIV infection", new FollowupHIVRiskDataDefinition(), "");
		dsd.addColumn("Reason for HIV Risk", new FollowupHIVRiskReasonDataDefinition(), "");
		dsd.addColumn("Adherence", new FollowupAdherenceDataDefinition(), "");
		dsd.addColumn("Reason for poor adherence", new FollowupAdherenceReasonDataDefinition(), "");
		dsd.addColumn("Received Adherence Counselling", new FollowupAdherenceCounsellingDataDefinition(), "");
		dsd.addColumn("Condoms issued", new FollowupCondomsIssuedDataDefinition(), "");
		dsd.addColumn("PrEP Status", new FollowupPrEPStatusDataDefinition(), "");
		dsd.addColumn("Discontinuation Reasons", new FollowupDiscontinueReasonDataDefinition(), "");
		//Turned HIV Positive while on PrEP
		dsd.addColumn("Linked to Care", new LinkedToCareDataDefinition(), "");
		dsd.addColumn("CCC Number", identifierCCCDef, "");
		dsd.addColumn("Remarks", new FollowupPrEPRemarksDataDefinition(), "");
		
		PrEPFollowupRegisterCohortDefinition cd = new PrEPFollowupRegisterCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		dsd.addRowFilter(cd, paramMapping);
		
		return dsd;
	}
}
