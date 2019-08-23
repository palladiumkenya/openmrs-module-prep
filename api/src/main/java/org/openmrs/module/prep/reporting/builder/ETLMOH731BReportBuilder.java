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
import org.openmrs.module.prep.reporting.ColumnParameters;
import org.openmrs.module.prep.reporting.EmrReportingUtils;
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
@Builds({ "kenyaemr.prep.common.report.moh731B" })
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
	
	ColumnParameters colTotal = new ColumnParameters(null, "Total", "");
	
	List<ColumnParameters> standardDisaggregationAgeAndSex = Arrays.asList(m_15_to_19, f_15_to_19, m_20_to_24, f_20_to_24,
	    m_25_to_30, f_25_to_30, m_30_and_above, f_30_and_above);
	
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
		
		// 1.0 Prep summary report
		EmrReportingUtils.addRow(cohortDsd, "HV01", "General Population",
		    ReportUtils.map(moh731BIndicators.eligibleForPreEPGP(), indParams), standardDisaggregationAgeAndSex,
		    Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08"));
		EmrReportingUtils.addRow(cohortDsd, "HV01", "MSM",
		    ReportUtils.map(moh731BIndicators.eligibleForPreEPMSM(), indParams), standardDisaggregationAgeAndSex,
		    Arrays.asList("09", "10", "11", "12", "13", "14", "15", "16"));
		EmrReportingUtils.addRow(cohortDsd, "HV01", "FSW",
		    ReportUtils.map(moh731BIndicators.eligibleForPreEPFSW(), indParams), standardDisaggregationAgeAndSex,
		    Arrays.asList("17", "18", "19", "20", "21", "22", "23", "24"));
		EmrReportingUtils.addRow(cohortDsd, "HV01", "PWID",
		    ReportUtils.map(moh731BIndicators.eligibleForPreEPPWID(), indParams), standardDisaggregationAgeAndSex,
		    Arrays.asList("25", "26", "27", "28", "29", "30", "31", "32"));
		EmrReportingUtils.addRow(cohortDsd, "HV01", "Discordant Couple",
		    ReportUtils.map(moh731BIndicators.eligibleForPreEPDiscordant(), indParams), standardDisaggregationAgeAndSex,
		    Arrays.asList("33", "34", "35", "36", "37", "38", "39", "40"));
		
		return cohortDsd;
		
	}
}
