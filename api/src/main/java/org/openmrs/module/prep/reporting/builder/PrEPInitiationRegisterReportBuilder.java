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
import org.openmrs.module.kenyaemr.reporting.ColumnParameters;
import org.openmrs.module.kenyaemr.reporting.EmrReportingUtils;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.prep.metadata.PrepMetadata;
import org.openmrs.module.prep.reporting.cohort.definition.PrEPInitiationRegisterCohortDefinition;
import org.openmrs.module.prep.reporting.data.converter.definition.prep.*;
import org.openmrs.module.prep.reporting.library.ETLReports.MOH731B.ETLMoh731BIndicatorLibrary;
import org.openmrs.module.prep.reporting.library.shared.common.CommonPrEPDimensionLibrary;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.common.SortCriteria;
import org.openmrs.module.reporting.data.DataDefinition;
import org.openmrs.module.reporting.data.converter.BirthdateConverter;
import org.openmrs.module.reporting.data.converter.DataConverter;
import org.openmrs.module.reporting.data.converter.ObjectFormatter;
import org.openmrs.module.reporting.data.patient.definition.ConvertedPatientDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientIdentifierDataDefinition;
import org.openmrs.module.reporting.data.person.definition.*;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.PatientDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
@Builds({ "kenyaemr.prep.prep.report.prepInitiationRegister" })
public class PrEPInitiationRegisterReportBuilder extends AbstractHybridReportBuilder {
	
	@Autowired
	private CommonPrEPDimensionLibrary commonDimensions;
	
	@Autowired
	private ETLMoh731BIndicatorLibrary moh731BIndicators;
	
	public static final String DATE_FORMAT = "dd/MM/yyyy";
	
	ColumnParameters m_15_to_19 = new ColumnParameters(null, "15-19, Male", "gender=M|age=15-19");
	
	ColumnParameters f_15_to_19 = new ColumnParameters(null, "15-19, Female", "gender=F|age=15-19");
	
	ColumnParameters m_20_to_24 = new ColumnParameters(null, "20-24, Male", "gender=M|age=20-24");
	
	ColumnParameters f_20_to_24 = new ColumnParameters(null, "20-24, Female", "gender=F|age=20-24");
	
	ColumnParameters m_25_to_30 = new ColumnParameters(null, "25-30, Male", "gender=M|age=25-30");
	
	ColumnParameters f_25_to_30 = new ColumnParameters(null, "25-30, Female", "gender=F|age=25-30");
	
	ColumnParameters m_30_and_above = new ColumnParameters(null, "30+, Male", "gender=M|age=30+");
	
	ColumnParameters f_30_and_above = new ColumnParameters(null, "30+, Female", "gender=F|age=30+");
	
	ColumnParameters colTotal = new ColumnParameters(null, "Total", "");
	
	List<ColumnParameters> ageAndSexDisaggregation = Arrays.asList(m_15_to_19, f_15_to_19, m_20_to_24, f_20_to_24,
	    m_25_to_30, f_25_to_30, m_30_and_above, f_30_and_above, colTotal);
	
	@Override
	protected Mapped<CohortDefinition> buildCohort(HybridReportDescriptor descriptor, PatientDataSetDefinition dsd) {
		return allClientsCohort();
	}
	
	protected Mapped<CohortDefinition> allClientsCohort() {
		CohortDefinition cd = new PrEPInitiationRegisterCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setName("PrEPCohortRegister");
		return ReportUtils.map(cd, "startDate=${startDate},endDate=${endDate}");
	}
	
	@Override
	protected List<Mapped<DataSetDefinition>> buildDataSets(ReportDescriptor descriptor, ReportDefinition report) {
		
		PatientDataSetDefinition allClients = prepDataSetDefinition();
		allClients.addRowFilter(allClientsCohort());
		//allPatients.addRowFilter(buildCohort(descriptor));
		DataSetDefinition allPatientsDSD = allClients;
		
		return Arrays.asList(ReportUtils.map(allPatientsDSD, "startDate=${startDate},endDate=${endDate}"),
		    ReportUtils.map(prEPRegisterSummaryDataSet(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	@Override
	protected List<Parameter> getParameters(ReportDescriptor reportDescriptor) {
		return Arrays.asList(new Parameter("startDate", "Start Date", Date.class), new Parameter("endDate", "End Date",
		        Date.class), new Parameter("dateBasedReporting", "", String.class));
	}
	
	protected PatientDataSetDefinition prepDataSetDefinition() {
		
		PatientDataSetDefinition dsd = new PatientDataSetDefinition("PrEPInitiationRegister");
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
		
		DataConverter nameFormatter = new ObjectFormatter("{familyName}, {givenName} {middleName}");
		DataDefinition nameDef = new ConvertedPersonDataDefinition("name", new PreferredNameDataDefinition(), nameFormatter);
		dsd.addColumn("id", new PersonIdDataDefinition(), "");
		dsd.addColumn("Serial Number", new SerialNumberDataDefinition(), "");
		dsd.addColumn("Name", nameDef, "");
		dsd.addColumn("Visit Date", new PrEPVisitDateDataDefinition(), "");
		dsd.addColumn("Client Unique ID", identifierDef, "");
		dsd.addColumn("Sex", new GenderDataDefinition(), "");
		dsd.addColumn("DOB", new BirthdateDataDefinition(), "", new BirthdateConverter(DATE_FORMAT));
		dsd.addColumn("Age", new AgeDataDefinition(), "");
		dsd.addColumn("Population Type", new PrEPHTSPopulationTypeDataDefinition(), "");
		dsd.addColumn("Assessed", new AssessedDataDefinition(), "");
		dsd.addColumn("Eligible", new EligibleDataDefinition(), "");
		dsd.addColumn("PrEP Initiation Date", new InitiationDateDataDefinition(), "");
		dsd.addColumn("PrEP Eligibility Reason at Entry", new EligibilityReasonAtEntryDataDefinition(), "");
		dsd.addColumn("HTS", new HTSDataDefinition(), "");
		dsd.addColumn("STI Screened/Results", new STIDataDefinition(), "");
		dsd.addColumn("Creatinine Clearance/Results", new CreatinineDataDefinition(), "");
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
	
	protected DataSetDefinition prEPRegisterSummaryDataSet() {
		CohortIndicatorDataSetDefinition cohortDsd = new CohortIndicatorDataSetDefinition();
		cohortDsd.setName("cohortIndicator");
		cohortDsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cohortDsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cohortDsd.addDimension("age", ReportUtils.map(commonDimensions.prepAgeGroups(), "onDate=${endDate}"));
		cohortDsd.addDimension("gender", ReportUtils.map(commonDimensions.gender()));
		String indParams = "startDate=${startDate},endDate=${endDate}";
		
		EmrReportingUtils.addRow(cohortDsd, "numberAssessedForHIVRisk", "Number Assessed for HIV risk",
		    ReportUtils.map(moh731BIndicators.assessedForHIVRisk(), indParams), ageAndSexDisaggregation,
		    Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09"));
		EmrReportingUtils.addRow(cohortDsd, "numberEligibleForPrEP", "Number Eligible for PrEP",
		    ReportUtils.map(moh731BIndicators.eligibleForPrEP(), indParams), ageAndSexDisaggregation,
		    Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09"));
		EmrReportingUtils.addRow(cohortDsd, "numberInitiatedOnPrEP", "Number initiated (New) on PrEP",
		    ReportUtils.map(moh731BIndicators.initiatedOnPrEP(), indParams), ageAndSexDisaggregation,
		    Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09"));
		EmrReportingUtils.addRow(cohortDsd, "numberContinuingOnPrEP", "Number continuing (Refill) on PrEP",
		    ReportUtils.map(moh731BIndicators.continuingOnPrEP(), indParams), ageAndSexDisaggregation,
		    Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09"));
		EmrReportingUtils.addRow(cohortDsd, "numberRestartingPrEP", "Number restarting (Restart) PrEP",
		    ReportUtils.map(moh731BIndicators.restartingPrEPTotal(), indParams), ageAndSexDisaggregation,
		    Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09"));
		EmrReportingUtils.addRow(cohortDsd, "numberCurrentOnPrEP", "Number current on PrEP(New+Refill+Restart)",
		    ReportUtils.map(moh731BIndicators.currentOnPrEP(), indParams), ageAndSexDisaggregation,
		    Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09"));
		EmrReportingUtils.addRow(cohortDsd, "numberRetestedPositiveOnPrEP", "Number retested HIV Positive while on PrEP",
		    ReportUtils.map(moh731BIndicators.retestedPositiveOnPrEP(), indParams), ageAndSexDisaggregation,
		    Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09"));
		EmrReportingUtils.addRow(cohortDsd, "numberDiagnosedWithSTI", "Number diagnosed with STI",
		    ReportUtils.map(moh731BIndicators.diagnosedWithSTI(), indParams), ageAndSexDisaggregation,
		    Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09"));
		EmrReportingUtils.addRow(cohortDsd, "numberDiscontinuedPrEP", "Number discontinued PrEP",
		    ReportUtils.map(moh731BIndicators.discontinuedPrEP(), indParams), ageAndSexDisaggregation,
		    Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09"));
		
		return cohortDsd;
	}
}
