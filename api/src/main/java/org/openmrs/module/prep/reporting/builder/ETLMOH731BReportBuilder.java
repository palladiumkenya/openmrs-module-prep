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

import org.openmrs.module.kenyacore.report.ReportDescriptor;
import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.kenyacore.report.builder.AbstractReportBuilder;
import org.openmrs.module.kenyacore.report.builder.Builds;
import org.openmrs.module.kenyaemr.reporting.ColumnParameters;
import org.openmrs.module.kenyaemr.reporting.EmrReportingUtils;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.prep.reporting.library.ETLReports.MOH731B.ETLMoh731BIndicatorLibrary;
import org.openmrs.module.prep.reporting.library.shared.common.CommonPrEPDimensionLibrary;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Report builder for ETL MOH 731 for Green Card
 */
@Component
@Builds({ "kenyaemr.prep.prep.report.moh731B" })
public class ETLMOH731BReportBuilder extends AbstractReportBuilder {
	
	@Autowired
	private CommonPrEPDimensionLibrary commonDimensions;
	
	@Autowired
	private ETLMoh731BIndicatorLibrary moh731BIndicators;
	
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	
	ColumnParameters m_15_to_19 = new ColumnParameters(null, "15-19, Male", "gender=M|age=15-19");
	
	ColumnParameters f_15_to_19 = new ColumnParameters(null, "15-19, Female", "gender=F|age=15-19");
	
	ColumnParameters m_20_to_24 = new ColumnParameters(null, "20-24, Male", "gender=M|age=20-24");
	
	ColumnParameters f_20_to_24 = new ColumnParameters(null, "20-24, Female", "gender=F|age=20-24");
	
	ColumnParameters m_25_to_30 = new ColumnParameters(null, "25-30, Male", "gender=M|age=25-30");
	
	ColumnParameters f_25_to_30 = new ColumnParameters(null, "25-30, Female", "gender=F|age=25-30");
	
	ColumnParameters m_30_and_above = new ColumnParameters(null, "30+, Male", "gender=M|age=30+");
	
	ColumnParameters f_30_and_above = new ColumnParameters(null, "30+, Female", "gender=F|age=30+");
	
	ColumnParameters allFemales = new ColumnParameters(null, "Females", "gender=F");
	
	ColumnParameters allMales = new ColumnParameters(null, "Males", "gender=M");
	
	ColumnParameters colTotal = new ColumnParameters(null, "Total", "");
	
	List<ColumnParameters> ageAndSexDisaggregation = Arrays.asList(m_15_to_19, f_15_to_19, m_20_to_24, f_20_to_24,
	    m_25_to_30, f_25_to_30, m_30_and_above, f_30_and_above);
	
	List<ColumnParameters> femaleAgeDisaggregation = Arrays.asList(f_15_to_19, f_20_to_24, f_25_to_30, f_30_and_above);
	
	List<ColumnParameters> maleAgeDisaggregation = Arrays.asList(m_15_to_19, m_20_to_24, m_25_to_30, m_30_and_above);
	
	List<ColumnParameters> sexDisaggregation = Arrays.asList(allFemales, allMales);
	
	@Override
	protected List<Parameter> getParameters(ReportDescriptor reportDescriptor) {
		return Arrays.asList(new Parameter("startDate", "Start Date", Date.class), new Parameter("endDate", "End Date",
		        Date.class), new Parameter("dateBasedReporting", "", String.class));
	}
	
	@Override
	protected List<Mapped<DataSetDefinition>> buildDataSets(ReportDescriptor reportDescriptor,
	        ReportDefinition reportDefinition) {
		return Arrays.asList(ReportUtils.map(prepSummaryDatasetDefinition(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Creates the dataset for section #1 - #8: PrEP summary reporting tool
	 * 
	 * @return the dataset
	 */
	protected DataSetDefinition prepSummaryDatasetDefinition() {
		CohortIndicatorDataSetDefinition cohortDsd = new CohortIndicatorDataSetDefinition();
		cohortDsd.setName("1");
		cohortDsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cohortDsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cohortDsd.addDimension("age", ReportUtils.map(commonDimensions.prepAgeGroups(), "onDate=${endDate}"));
		cohortDsd.addDimension("gender", ReportUtils.map(commonDimensions.gender()));
		String indParams = "startDate=${startDate},endDate=${endDate}";
		
		// 1.0 PrEP summary report
		EmrReportingUtils.addRow(cohortDsd, "HV01", "Eligible for PrEP-General Population",
		    ReportUtils.map(moh731BIndicators.eligibleForPrEPGP(), indParams), ageAndSexDisaggregation,
		    Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08"));
		EmrReportingUtils.addRow(cohortDsd, "HV01", "Eligible for PrEP-MSM",
		    ReportUtils.map(moh731BIndicators.eligibleForPrEPMSM(), indParams), maleAgeDisaggregation,
		    Arrays.asList("01", "02", "03", "04"));
		EmrReportingUtils.addRow(cohortDsd, "HV01", "Eligible for PrEP-FSW",
		    ReportUtils.map(moh731BIndicators.eligibleForPrEPFSW(), indParams), femaleAgeDisaggregation,
		    Arrays.asList("01", "02", "03", "04"));
		EmrReportingUtils.addRow(cohortDsd, "HV01", "Eligible for PrEP-PWID",
		    ReportUtils.map(moh731BIndicators.eligibleForPrEPPWID(), indParams), ageAndSexDisaggregation,
		    Arrays.asList("25", "26", "27", "28", "29", "30", "31", "32"));
		EmrReportingUtils.addRow(cohortDsd, "HV01", "Eligible for PrEP-Discordant Couple",
		    ReportUtils.map(moh731BIndicators.eligibleForPrEPDiscordant(), indParams), ageAndSexDisaggregation,
		    Arrays.asList("33", "34", "35", "36", "37", "38", "39", "40"));
		EmrReportingUtils.addRow(cohortDsd, "HV01", "Total Eligible for PrEP",
		    ReportUtils.map(moh731BIndicators.eligibleForPrEP(), indParams), sexDisaggregation, Arrays.asList("01", "02"));
		
		//2.0 Number newly initiated on PrEP
		EmrReportingUtils.addRow(cohortDsd, "HV02", "Initiated on PrEP-General Population",
		    ReportUtils.map(moh731BIndicators.newlyOnPrePGP(), indParams), ageAndSexDisaggregation,
		    Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08"));
		EmrReportingUtils.addRow(cohortDsd, "HV02", "Initiated on PrEP-MSM",
		    ReportUtils.map(moh731BIndicators.newOnPrEPMSM(), indParams), maleAgeDisaggregation,
		    Arrays.asList("01", "02", "03", "04"));
		EmrReportingUtils.addRow(cohortDsd, "HV02", "Initiated on PrEP-FSW",
		    ReportUtils.map(moh731BIndicators.newOnPrEPFSW(), indParams), femaleAgeDisaggregation,
		    Arrays.asList("01", "02", "03", "04"));
		EmrReportingUtils.addRow(cohortDsd, "HV02", "Initiated on PrEP-PWID",
		    ReportUtils.map(moh731BIndicators.newOnPrEPPWID(), indParams), ageAndSexDisaggregation,
		    Arrays.asList("25", "26", "27", "28", "29", "30", "31", "32"));
		EmrReportingUtils.addRow(cohortDsd, "HV02", "Initiated on PrEP-Discordant Couple",
		    ReportUtils.map(moh731BIndicators.newOnPrEPDiscordant(), indParams), ageAndSexDisaggregation,
		    Arrays.asList("33", "34", "35", "36", "37", "38", "39", "40"));
		EmrReportingUtils.addRow(cohortDsd, "HV02", "Total initiated on PrEP",
		    ReportUtils.map(moh731BIndicators.newOnPrEP(), indParams), sexDisaggregation, Arrays.asList("01", "02"));
		
		//3.0 Number continuing PrEP - Refill
		EmrReportingUtils.addRow(cohortDsd, "HV03", "PrEP refill-General Population",
		    ReportUtils.map(moh731BIndicators.refillOnPrEPGP(), indParams), ageAndSexDisaggregation,
		    Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08"));
		EmrReportingUtils.addRow(cohortDsd, "HV03", "PrEP refill-MSM",
		    ReportUtils.map(moh731BIndicators.refillingPrEPMSM(), indParams), maleAgeDisaggregation,
		    Arrays.asList("01", "02", "03", "04"));
		EmrReportingUtils.addRow(cohortDsd, "HV03", "PrEP refill-FSW",
		    ReportUtils.map(moh731BIndicators.refillingPrEPFSW(), indParams), femaleAgeDisaggregation,
		    Arrays.asList("01", "02", "03", "04"));
		EmrReportingUtils.addRow(cohortDsd, "HV03", "PrEP refill-PWID",
		    ReportUtils.map(moh731BIndicators.refillingPrEPPWID(), indParams), ageAndSexDisaggregation,
		    Arrays.asList("25", "26", "27", "28", "29", "30", "31", "32"));
		EmrReportingUtils.addRow(cohortDsd, "HV03", "PrEP refill-Discordant Couple",
		    ReportUtils.map(moh731BIndicators.refillingPrEPDiscordant(), indParams), ageAndSexDisaggregation,
		    Arrays.asList("33", "34", "35", "36", "37", "38", "39", "40"));
		EmrReportingUtils.addRow(cohortDsd, "HV03", "Total PrEP refill",
		    ReportUtils.map(moh731BIndicators.refillOnPrEPTotal(), indParams), sexDisaggregation, Arrays.asList("01", "02"));
		
		//4.0 Number restarting PrEP
		EmrReportingUtils.addRow(cohortDsd, "HV04", "Restarting PrEP-General Population",
		    ReportUtils.map(moh731BIndicators.restartingPrEPGP(), indParams), ageAndSexDisaggregation,
		    Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08"));
		EmrReportingUtils.addRow(cohortDsd, "HV04", "Restarting PrEP-MSM",
		    ReportUtils.map(moh731BIndicators.restartingPrEPMSM(), indParams), maleAgeDisaggregation,
		    Arrays.asList("01", "02", "03", "04"));
		EmrReportingUtils.addRow(cohortDsd, "HV04", "Restarting PrEP-FSW",
		    ReportUtils.map(moh731BIndicators.restartingPrEPFSW(), indParams), femaleAgeDisaggregation,
		    Arrays.asList("01", "02", "03", "04"));
		EmrReportingUtils.addRow(cohortDsd, "HV04", "Restarting PrEP-PWID",
		    ReportUtils.map(moh731BIndicators.restartingPrEPPWID(), indParams), ageAndSexDisaggregation,
		    Arrays.asList("25", "26", "27", "28", "29", "30", "31", "32"));
		EmrReportingUtils.addRow(cohortDsd, "HV04", "Restarting PrEP-Discordant Couple",
		    ReportUtils.map(moh731BIndicators.restartingPrEPDiscordant(), indParams), ageAndSexDisaggregation,
		    Arrays.asList("33", "34", "35", "36", "37", "38", "39", "40"));
		EmrReportingUtils.addRow(cohortDsd, "HV04", "Total Restarting PrEP",
		    ReportUtils.map(moh731BIndicators.restartingPrEPTotal(), indParams), sexDisaggregation,
		    Arrays.asList("01", "02"));
		
		//5.0 Number currently on PrEP (New + Refill + Restart)
		EmrReportingUtils.addRow(cohortDsd, "HV05", "Current on PrEP-General Population",
		    ReportUtils.map(moh731BIndicators.currentlyOnPrEPGP(), indParams), ageAndSexDisaggregation,
		    Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08"));
		EmrReportingUtils.addRow(cohortDsd, "HV05", "Current on PrEP-MSM",
		    ReportUtils.map(moh731BIndicators.currentlyOnPrEPMSM(), indParams), maleAgeDisaggregation,
		    Arrays.asList("01", "02", "03", "04"));
		EmrReportingUtils.addRow(cohortDsd, "HV05", "Current on PrEP-FSW",
		    ReportUtils.map(moh731BIndicators.currentlyOnPrEPFSW(), indParams), femaleAgeDisaggregation,
		    Arrays.asList("01", "02", "03", "04"));
		EmrReportingUtils.addRow(cohortDsd, "HV05", "Current on PrEP-PWID",
		    ReportUtils.map(moh731BIndicators.currentlyOnPrEPPWID(), indParams), ageAndSexDisaggregation,
		    Arrays.asList("25", "26", "27", "28", "29", "30", "31", "32"));
		EmrReportingUtils.addRow(cohortDsd, "HV05", "Current on PrEP-Discordant Couple",
		    ReportUtils.map(moh731BIndicators.currentlyOnPrEPDiscordant(), indParams), ageAndSexDisaggregation,
		    Arrays.asList("33", "34", "35", "36", "37", "38", "39", "40"));
		EmrReportingUtils
		        .addRow(cohortDsd, "HV05", "Total Current on PrEP",
		            ReportUtils.map(moh731BIndicators.currentOnPrEPTotal(), indParams), sexDisaggregation,
		            Arrays.asList("01", "02"));
		
		//6.0 Number tested HIV+ while on PrEP
		EmrReportingUtils.addRow(cohortDsd, "HV06", "Sero-converted-General Population",
		    ReportUtils.map(moh731BIndicators.seroconvertedOnPrEPGP(), indParams), ageAndSexDisaggregation,
		    Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08"));
		EmrReportingUtils.addRow(cohortDsd, "HV06", "Sero-converted-MSM",
		    ReportUtils.map(moh731BIndicators.seroconvertedOnPrEPMSM(), indParams), maleAgeDisaggregation,
		    Arrays.asList("01", "02", "03", "04"));
		EmrReportingUtils.addRow(cohortDsd, "HV06", "Sero-converted-FSW",
		    ReportUtils.map(moh731BIndicators.seroconvertedOnPrEPFSW(), indParams), femaleAgeDisaggregation,
		    Arrays.asList("01", "02", "03", "04"));
		EmrReportingUtils.addRow(cohortDsd, "HV06", "Sero-converted-PWID",
		    ReportUtils.map(moh731BIndicators.seroconvertedOnPrEPPWID(), indParams), ageAndSexDisaggregation,
		    Arrays.asList("25", "26", "27", "28", "29", "30", "31", "32"));
		EmrReportingUtils.addRow(cohortDsd, "HV06", "Sero-converted-Discordant Couple",
		    ReportUtils.map(moh731BIndicators.seroconvertedOnPrEPDiscordant(), indParams), ageAndSexDisaggregation,
		    Arrays.asList("33", "34", "35", "36", "37", "38", "39", "40"));
		EmrReportingUtils.addRow(cohortDsd, "HV06", "Total Sero-converted",
		    ReportUtils.map(moh731BIndicators.seroConvertedOnPrEP(), indParams), sexDisaggregation,
		    Arrays.asList("01", "02"));
		
		//7.0 Number diagnosed with STI
		EmrReportingUtils.addRow(cohortDsd, "HV07", "Diagnosed with STI-General Population",
		    ReportUtils.map(moh731BIndicators.diagnosedWithSTIGP(), indParams), ageAndSexDisaggregation,
		    Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08"));
		EmrReportingUtils.addRow(cohortDsd, "HV07", "Diagnosed with STI-MSM",
		    ReportUtils.map(moh731BIndicators.diagnosedWithSTIMSM(), indParams), maleAgeDisaggregation,
		    Arrays.asList("01", "02", "03", "04"));
		EmrReportingUtils.addRow(cohortDsd, "HV07", "Diagnosed with STI-FSW",
		    ReportUtils.map(moh731BIndicators.diagnosedWithSTIFSW(), indParams), femaleAgeDisaggregation,
		    Arrays.asList("01", "02", "03", "04"));
		EmrReportingUtils.addRow(cohortDsd, "HV07", "Diagnosed with STI-PWID",
		    ReportUtils.map(moh731BIndicators.diagnosedWithSTIPWID(), indParams), ageAndSexDisaggregation,
		    Arrays.asList("25", "26", "27", "28", "29", "30", "31", "32"));
		EmrReportingUtils.addRow(cohortDsd, "HV07", "Diagnosed with STI-Discordant Couple",
		    ReportUtils.map(moh731BIndicators.diagnosedWithSTIDiscordant(), indParams), ageAndSexDisaggregation,
		    Arrays.asList("33", "34", "35", "36", "37", "38", "39", "40"));
		EmrReportingUtils.addRow(cohortDsd, "HV07", "Total Diagnosed with STI",
		    ReportUtils.map(moh731BIndicators.diagnosedWithSTI(), indParams), sexDisaggregation, Arrays.asList("01", "02"));
		
		//8.0 Number Discontinued PrEP
		EmrReportingUtils.addRow(cohortDsd, "HV08", "Discontinued from PrEP-General Population",
		    ReportUtils.map(moh731BIndicators.discontinuedPrEPGP(), indParams), ageAndSexDisaggregation,
		    Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08"));
		EmrReportingUtils.addRow(cohortDsd, "HV08", "Discontinued from PrEP-MSM",
		    ReportUtils.map(moh731BIndicators.discontinuedPrEPMSM(), indParams), maleAgeDisaggregation,
		    Arrays.asList("01", "02", "03", "04"));
		EmrReportingUtils.addRow(cohortDsd, "HV08", "Discontinued from PrEP-FSW",
		    ReportUtils.map(moh731BIndicators.discontinuedPrEPFSW(), indParams), femaleAgeDisaggregation,
		    Arrays.asList("01", "02", "03", "04"));
		EmrReportingUtils.addRow(cohortDsd, "HV08", "Discontinued from PrEP-PWID",
		    ReportUtils.map(moh731BIndicators.discontinuedPrEPPWID(), indParams), ageAndSexDisaggregation,
		    Arrays.asList("25", "26", "27", "28", "29", "30", "31", "32"));
		EmrReportingUtils.addRow(cohortDsd, "HV08", "Discontinued from PrEP-Discordant Couple",
		    ReportUtils.map(moh731BIndicators.discontinuedPrEPDiscordant(), indParams), ageAndSexDisaggregation,
		    Arrays.asList("33", "34", "35", "36", "37", "38", "39", "40"));
		EmrReportingUtils.addRow(cohortDsd, "HV08", "Total Discontinued from PrEP",
		    ReportUtils.map(moh731BIndicators.discontinuedPrEP(), indParams), sexDisaggregation, Arrays.asList("01", "02"));
		
		return cohortDsd;
		
	}
}
