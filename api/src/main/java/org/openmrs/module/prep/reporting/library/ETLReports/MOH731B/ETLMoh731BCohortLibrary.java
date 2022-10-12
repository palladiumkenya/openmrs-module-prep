/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.prep.reporting.library.ETLReports.MOH731B;

import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by dev on 1/14/17.
 */

/**
 * Library of cohort definitions used specifically in the MOH731 report based on ETL tables. It has
 * incorporated green card components
 */
@Component
public class ETLMoh731BCohortLibrary {
	
	/**
	 * TODO: Requires team review
	 * 
	 * @return
	 */
	
	/**
	 * General population clients in their last HTS test
	 * 
	 * @return
	 */
	public CohortDefinition generalPopulation() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select t.patient_id from kenyaemr_etl.etl_hts_test t where date(t.visit_date) between date(:startDate) and date(:endDate)\n"
		        + "group by t.patient_id\n"
		        + "having mid(max(concat(t.visit_date,t.population_type)),11) = 'General Population';";
		cd.setName("General Population");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("GP eligible for PrEP");
		
		return cd;
	}
	
	/*	public CohortDefinition eligibleForPrEPGP() {
			CompositionCohortDefinition cd = new CompositionCohortDefinition();
			CalculationCohortDefinition calc = new CalculationCohortDefinition(new EligibleForPrepProgramCalculation());
			calc.setName("eligibleForPrEP");
			calc.addParameter(new Parameter("endDate", "End Date", Date.class));
			cd.addParameter(new Parameter("endDate", "End date", Date.class));
			cd.addSearch("generalPopulation", ReportUtils.map(generalPopulation(), "endDate=${endDate}"));
			cd.addSearch("eligibleForPrEP", ReportUtils.map(calc, "endDate=${endDate}"));
			cd.setCompositionString("generalPopulation AND eligibleForPrEP");
			return cd;
			
		}*/
	
	/**
	 * MSM clients in their last HTS test
	 * 
	 * @return
	 */
	public CohortDefinition msm() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select t.patient_id from kenyaemr_etl.etl_hts_test t where date(t.visit_date) between date(:startDate) and date(:endDate)\n"
		        + "group by t.patient_id\n"
		        + "having mid(max(concat(t.visit_date,t.key_population_type)),11) = 'Men who have sex with men';";
		cd.setName("MSM");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.setDescription("MSM");
		
		return cd;
	}
	
	/**
	 * FSW clients in their last HTS test
	 * 
	 * @return
	 */
	public CohortDefinition fsw() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select t.patient_id from kenyaemr_etl.etl_hts_test t where date(t.visit_date) between date(:startDate) and date(:endDate)\n"
		        + "group by t.patient_id\n"
		        + "having mid(max(concat(t.visit_date,t.key_population_type)),11) = 'Female sex worker';";
		cd.setName("FSW");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("FSW");
		
		return cd;
	}
	
	/**
	 * People who inject drugs in their last HTS
	 * 
	 * @return
	 */
	public CohortDefinition pwid() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select t.patient_id from kenyaemr_etl.etl_hts_test t where date(t.visit_date) between date(:startDate) and date(:endDate)\n"
		        + "group by t.patient_id\n"
		        + "having mid(max(concat(t.visit_date,t.key_population_type)),11) = 'People who inject drugs';";
		cd.setName("PWID");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("PWID");
		
		return cd;
	}
	
	/**
	 * Discordant couple in their Last HTS test
	 * 
	 * @return
	 */
	public CohortDefinition discordantCoupleInHTS() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select t.patient_id from kenyaemr_etl.etl_hts_test t where date(t.visit_date) between date(:startDate) and date(:endDate)\n"
		        + "    group by t.patient_id\n"
		        + "    having mid(max(concat(t.visit_date,t.couple_discordant)),11) = 'Yes';";
		cd.setName("Discordant couple");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("Discordant Couple");
		
		return cd;
	}
	
	/**
	 * Client's current weight >= 35 kgs
	 * 
	 * @return
	 */
	public CohortDefinition currentWeight() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select t.patient_id from kenyaemr_etl.etl_patient_triage t where date(t.visit_date) between date(:startDate) and date(:endDate)\n"
		        + "group by t.patient_id\n" + "having mid(max(concat(t.visit_date,t.weight)),11) >= 35;";
		cd.setName("Current weight");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("Current weight");
		
		return cd;
	}
	
	/**
	 * Client's current Negative Current HIV test results (taken within the last 3 days). Considers
	 * initial test
	 * 
	 * @return
	 */
	public CohortDefinition currentNegativeInitialHIVTestResult() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select t.patient_id from kenyaemr_etl.etl_hts_test t where date(t.visit_date) between date(:startDate) and date(:endDate)\n"
		        + "group by t.patient_id\n"
		        + "having mid(max(concat(t.visit_date,t.final_test_result)),11) = 'Negative'\n"
		        + "   and mid(max(concat(t.visit_date,t.test_type)),11) = 1\n"
		        + "and timestampdiff(DAY,max(date(t.visit_date)),date(:endDate)) <=3;";
		cd.setName("Current Negative HIV test result");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("Current negative HIV test result");
		
		return cd;
	}
	
	/**
	 * Client's 15 years and above
	 * 
	 * @return
	 */
	public CohortDefinition currentAge() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select d.patient_id from kenyaemr_etl.etl_patient_demographics d\n"
		        + "where timestampdiff(YEAR,d.DOB,date(:endDate)) >= 15;";
		cd.setName("Aged 15 years and above");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("Aged 15 years and above");
		
		return cd;
	}
	
	/**
	 * Serum creatinine >=50 or test not done
	 * 
	 * @return
	 */
	public CohortDefinition serumCreatinine() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select a.patient_id\n"
		        + "from (select d.patient_id, x.lab_test, x.test_result, x.patient_id as test_done\n"
		        + "      from kenyaemr_etl.etl_patient_demographics d\n" + "               left join\n"
		        + "           (select x.patient_id,\n"
		        + "                   mid(max(concat(x.visit_date, x.lab_test)), 11)    as lab_test,\n"
		        + "                   mid(max(concat(x.visit_date, x.test_result)), 11) as test_result\n"
		        + "            from kenyaemr_etl.etl_laboratory_extract x\n"
		        + "            where date(x.visit_date) between date(:startDate) and date(:endDate)\n"
		        + "            group by x.patient_id) x on d.patient_id = x.patient_id group by d.patient_id\n"
		        + "      having (x.lab_test = 790 and x.test_result >= 50) or test_done is null)a;";
		cd.setName("Serum creatinine");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("Serum creatinine");
		
		return cd;
	}
	
	/**
	 * General population Clients eligible for PrEP
	 * 
	 * @return
	 */
	public CohortDefinition eligibleForPrEPGP() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		
		cd.addParameter(new Parameter("startDate", "Start date", Date.class));
		cd.addParameter(new Parameter("endDate", "End date", Date.class));
		cd.addSearch("generalPopulation", ReportUtils.map(generalPopulation(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("discordantCoupleInHTS",
		    ReportUtils.map(discordantCoupleInHTS(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("serumCreatinine", ReportUtils.map(serumCreatinine(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("currentAge", ReportUtils.map(currentAge(), "endDate=${endDate}"));
		cd.addSearch("currentWeight", ReportUtils.map(currentWeight(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("currentNegativeInitialHIVTestResult",
		    ReportUtils.map(currentNegativeInitialHIVTestResult(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("newlyOnPrEPGP", ReportUtils.map(newlyOnPrEPGP(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("restartOnPrEPGP", ReportUtils.map(restartOnPrEPGP(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("((generalPopulation AND NOT discordantCoupleInHTS) AND currentAge AND currentWeight AND currentNegativeInitialHIVTestResult AND serumCreatinine) OR restartOnPrEPGP OR newlyOnPrEPGP");
		return cd;
		
	}
	
	/**
	 * MSM Clients eligible for PrEP
	 * 
	 * @return
	 */
	public CohortDefinition eligibleForPrEPMSM() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		
		cd.addParameter(new Parameter("startDate", "Start date", Date.class));
		cd.addParameter(new Parameter("endDate", "End date", Date.class));
		cd.addSearch("msm", ReportUtils.map(msm(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("discordantCoupleInHTS",
		    ReportUtils.map(discordantCoupleInHTS(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("currentAge", ReportUtils.map(currentAge(), "endDate=${endDate}"));
		cd.addSearch("serumCreatinine", ReportUtils.map(serumCreatinine(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("currentWeight", ReportUtils.map(currentWeight(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("currentNegativeInitialHIVTestResult",
		    ReportUtils.map(currentNegativeInitialHIVTestResult(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("newlyOnPrEPMSM", ReportUtils.map(newlyOnPrEPMSM(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("restartOnPrEPMSM", ReportUtils.map(restartOnPrEPMSM(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("((msm AND NOT discordantCoupleInHTS) AND currentAge AND currentWeight AND currentNegativeInitialHIVTestResult AND serumCreatinine) OR newlyOnPrEPMSM OR restartOnPrEPMSM");
		return cd;
	}
	
	/**
	 * FSW Clients eligible for PrEP
	 * 
	 * @return
	 */
	public CohortDefinition eligibleForPrEPFSW() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		
		cd.addParameter(new Parameter("endDate", "End date", Date.class));
		cd.addParameter(new Parameter("startDate", "Start date", Date.class));
		cd.addSearch("fsw", ReportUtils.map(fsw(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("discordantCoupleInHTS",
		    ReportUtils.map(discordantCoupleInHTS(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("serumCreatinine", ReportUtils.map(serumCreatinine(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("currentAge", ReportUtils.map(currentAge(), "endDate=${endDate}"));
		cd.addSearch("currentWeight", ReportUtils.map(currentWeight(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("currentNegativeInitialHIVTestResult",
		    ReportUtils.map(currentNegativeInitialHIVTestResult(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("newlyOnPrEPFSW", ReportUtils.map(newlyOnPrEPFSW(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("restartOnPrEPFSW", ReportUtils.map(restartOnPrEPFSW(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("((fsw AND NOT discordantCoupleInHTS) AND currentAge AND currentWeight AND currentNegativeInitialHIVTestResult AND serumCreatinine) OR newlyOnPrEPFSW OR restartOnPrEPFSW");
		return cd;
	}
	
	/**
	 * PWID Clients eligible for PrEP
	 * 
	 * @return
	 */
	public CohortDefinition eligibleForPrEPPWID() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		
		cd.addParameter(new Parameter("startDate", "Start date", Date.class));
		cd.addParameter(new Parameter("endDate", "End date", Date.class));
		cd.addSearch("pwid", ReportUtils.map(pwid(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("discordantCoupleInHTS",
		    ReportUtils.map(discordantCoupleInHTS(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("currentAge", ReportUtils.map(currentAge(), "endDate=${endDate}"));
		cd.addSearch("serumCreatinine", ReportUtils.map(serumCreatinine(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("currentWeight", ReportUtils.map(currentWeight(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("currentNegativeInitialHIVTestResult",
		    ReportUtils.map(currentNegativeInitialHIVTestResult(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("newlyOnPrEPPWID", ReportUtils.map(newlyOnPrEPPWID(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("restartOnPrEPPWID", ReportUtils.map(restartOnPrEPPWID(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("((pwid AND NOT discordantCoupleInHTS) AND currentAge AND currentWeight AND currentNegativeInitialHIVTestResult AND serumCreatinine) OR newlyOnPrEPPWID OR restartOnPrEPPWID");
		return cd;
	}
	
	/**
	 * Discordant couple Clients eligible for PrEP
	 * 
	 * @return
	 */
	public CohortDefinition eligibleForPrEPDiscordant() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		
		cd.addParameter(new Parameter("startDate", "Start date", Date.class));
		cd.addParameter(new Parameter("endDate", "End date", Date.class));
		cd.addSearch("discordantCoupleInHTS",
		    ReportUtils.map(discordantCoupleInHTS(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("currentAge", ReportUtils.map(currentAge(), "endDate=${endDate}"));
		cd.addSearch("serumCreatinine", ReportUtils.map(serumCreatinine(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("currentWeight", ReportUtils.map(currentWeight(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("currentNegativeInitialHIVTestResult",
		    ReportUtils.map(currentNegativeInitialHIVTestResult(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("newlyOnPrEPDiscordant",
		    ReportUtils.map(newlyOnPrEPDiscordant(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("restartOnPreEPDiscordant",
		    ReportUtils.map(restartOnPreEPDiscordant(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("(discordantCoupleInHTS AND currentAge AND currentWeight AND currentNegativeInitialHIVTestResult AND serumCreatinine) OR newlyOnPrEPDiscordant OR restartOnPreEPDiscordant");
		return cd;
	}
	
	/**
	 * Clients eligible for PrEP
	 * 
	 * @return
	 */
	public CohortDefinition eligibleForPrEP() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		
		cd.addParameter(new Parameter("startDate", "Start date", Date.class));
		cd.addParameter(new Parameter("endDate", "End date", Date.class));
		cd.addSearch("eligibleForPrEPGP", ReportUtils.map(eligibleForPrEPGP(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("eligibleForPreEPMSM",
		    ReportUtils.map(eligibleForPrEPMSM(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("eligibleForPreEPFSW",
		    ReportUtils.map(eligibleForPrEPFSW(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("eligibleForPreEPPWID",
		    ReportUtils.map(eligibleForPrEPPWID(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("eligibleForPrEPDiscordant",
		    ReportUtils.map(eligibleForPrEPDiscordant(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("eligibleForPrEPGP OR eligibleForPreEPMSM OR eligibleForPreEPFSW OR eligibleForPreEPPWID OR eligibleForPrEPDiscordant");
		return cd;
	}
	
	/**
	 * Newly on PrEP
	 * 
	 * @return
	 */
	public CohortDefinition newlyOnPrEP() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select e.patient_id from kenyaemr_etl.etl_prep_enrolment e\n"
		        + "    where e.patient_type = 'New Patient'\n"
		        + "      and date(e.visit_date) between date(:startDate) and date(:endDate);";
		cd.setName("newlyOnPreEP");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("Newly on PrEP");
		
		return cd;
	}
	
	/**
	 * General Population Clients Newly enrolled in PrEP
	 * 
	 * @return
	 */
	public CohortDefinition newlyOnPrEPGP() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select e.patient_id from kenyaemr_etl.etl_prep_enrolment e\n"
		        + "    where e.patient_type = 'New Patient' and e.population_type = 164928\n"
		        + "      and date(e.visit_date) between date(:startDate) and date(:endDate);";
		cd.setName("newlyOnPreEPGP");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("GP newly on PrEP");
		
		return cd;
	}
	
	/**
	 * MSM clients Newly enrolled in PrEP
	 * 
	 * @return
	 */
	public CohortDefinition newlyOnPrEPMSM() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select e.patient_id from kenyaemr_etl.etl_prep_enrolment e\n"
		        + "where e.patient_type = 'New Patient' and e.population_type = 164929 and e.kp_type = 160578\n"
		        + "  and date(e.visit_date) between date(:startDate) and date(:endDate);";
		cd.setName("newlyOnPreEPMSM");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("MSM newly on PrEP");
		
		return cd;
	}
	
	/**
	 * FSW clients Newly enrolled in PrEP
	 * 
	 * @return
	 */
	public CohortDefinition newlyOnPrEPFSW() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select e.patient_id from kenyaemr_etl.etl_prep_enrolment e\n"
		        + "where e.patient_type = 'New Patient' and e.population_type = 164929 and e.kp_type = 160579\n"
		        + "  and date(e.visit_date) between date(:startDate) and date(:endDate);";
		cd.setName("newlyOnPreEPFSW");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("FSW newly on PrEP");
		
		return cd;
	}
	
	/**
	 * PWID Newly enrolled in PrEP
	 * 
	 * @return
	 */
	public CohortDefinition newlyOnPrEPPWID() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select e.patient_id from kenyaemr_etl.etl_prep_enrolment e\n"
		        + "where e.patient_type = 'New Patient' and e.population_type = 164929 and e.kp_type = 105\n"
		        + "  and date(e.visit_date) between date(:startDate) and date(:endDate);";
		cd.setName("newlyOnPreEPPWID");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("PWID newly on PrEP");
		
		return cd;
	}
	
	/**
	 * Newly enrolled on PrEP Discordant
	 * 
	 * @return
	 */
	public CohortDefinition newlyOnPrEPDiscordant() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select e.patient_id from kenyaemr_etl.etl_prep_enrolment e\n"
		        + "where e.patient_type = 'New Patient' and e.population_type = 6096\n"
		        + "  and date(e.visit_date) between date(:startDate) and date(:endDate);";
		cd.setName("newlyOnPreEPDiscordant");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("Discordant newly on PrEP");
		
		return cd;
	}
	
	/**
	 * Newly started on PrEP Counts everyone newly enrolled within the reporting period
	 * 
	 * @return
	 */
	public CohortDefinition newOnPrEP() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select e.patient_id from kenyaemr_etl.etl_prep_enrolment e\n"
		        + "where e.patient_type = 'New Patient'\n"
		        + "  and date(e.visit_date) between date(:startDate) and date(:endDate);";
		cd.setName("newOnPrEP");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("Discordant newly on PrEP");
		
		return cd;
	}
	
	/**
	 * Clients who had PrEP refill within the month
	 * 
	 * @return
	 */
	public CohortDefinition refillOnPrEP() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select r.patient_id from kenyaemr_etl.etl_prep_monthly_refill r\n"
		        + "    where r.prescribed_prep_today ='Yes' and date(r.visit_date) between date(:startDate) and date(:endDate);";
		cd.setName("refillOnPreP");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("refill on PrEP");
		
		return cd;
	}
	
	/**
	 * Clients who had PrEP follow-up within the month
	 * 
	 * @return
	 */
	public CohortDefinition followupOnPrEP() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select f.patient_id,f.visit_date\n" + "from kenyaemr_etl.etl_prep_followup f\n"
		        + "where date(f.visit_date) between date(:startDate) and date(:endDate);";
		cd.setName("followupOnPrEP");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("PrEP followup");
		
		return cd;
	}
	
	/**
	 * General population clients who had a PrEP refill within the period
	 * 
	 * @return
	 */
	public CohortDefinition refillOnPrEPGP() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("followupOnPrEP", ReportUtils.map(followupOnPrEP(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("refillOnPrEP", ReportUtils.map(refillOnPrEP(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("generalPopulation", ReportUtils.map(generalPopulation(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("(followupOnPrEP OR refillOnPrEP) AND generalPopulation");
		return cd;
	}
	
	/**
	 * PrEP refill MSM
	 * 
	 * @return
	 */
	public CohortDefinition refillOnPrEPMSM() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("followupOnPrEP", ReportUtils.map(followupOnPrEP(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("refillOnPrEP", ReportUtils.map(refillOnPrEP(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("msm", ReportUtils.map(msm(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("(followupOnPrEP OR refillOnPrEP) AND msm");
		return cd;
	}
	
	/**
	 * PrEP refill FSW
	 * 
	 * @return
	 */
	public CohortDefinition refillOnPrEPFSW() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("followupOnPrEP", ReportUtils.map(followupOnPrEP(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("refillOnPrEP", ReportUtils.map(refillOnPrEP(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("fsw", ReportUtils.map(fsw(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("(followupOnPrEP OR refillOnPrEP) AND fsw");
		return cd;
	}
	
	/**
	 * refill for People who inject drugs
	 * 
	 * @return
	 */
	public CohortDefinition refillOnPrEPPWID() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("followupOnPrEP", ReportUtils.map(followupOnPrEP(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("refillOnPrEP", ReportUtils.map(refillOnPrEP(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("pwid", ReportUtils.map(pwid(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("(followupOnPrEP OR refillOnPrEP) AND pwid");
		return cd;
	}
	
	/**
	 * Refill for client who is a discordant couple
	 * 
	 * @return
	 */
	public CohortDefinition refillOnPrEPDiscordant() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("followupOnPrEP", ReportUtils.map(followupOnPrEP(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("refillOnPrEP", ReportUtils.map(refillOnPrEP(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("discordantCoupleInHTS",
		    ReportUtils.map(discordantCoupleInHTS(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("(followupOnPrEP OR refillOnPrEP) AND discordantCoupleInHTS");
		return cd;
	}
	
	/**
	 * Individuals refilling PrEP within the reporting period
	 */
	public CohortDefinition refillOnPrEPTotal() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("refillOnPrEPDiscordant",
		    ReportUtils.map(refillOnPrEPDiscordant(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("refillOnPrEPFSW", ReportUtils.map(refillOnPrEPFSW(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("refillOnPrEPGP", ReportUtils.map(refillOnPrEPGP(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("refillOnPrEPPWID", ReportUtils.map(refillOnPrEPPWID(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("refillOnPrEPMSM", ReportUtils.map(refillOnPrEPMSM(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("refillOnPrEPDiscordant OR refillOnPrEPFSW OR refillOnPrEPGP OR refillOnPrEPPWID OR refillOnPrEPMSM");
		return cd;
	}
	
	/**
	 * Clients restarting PrEP
	 * 
	 * @return
	 **/
	public CohortDefinition restartingPrEP() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select e.patient_id from kenyaemr_etl.etl_prep_enrolment e\n"
		        + "     where e.patient_type = 'Re-enrollment(Re-activation)' and date(e.visit_date) between date(:startDate) and date(:endDate);";
		cd.setName("Restarting PrEP");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("Restarting PrEP");
		
		return cd;
	}
	
	/**
	 * General population restating PrEP
	 * 
	 * @return
	 */
	public CohortDefinition restartOnPrEPGP() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("restartingPrEP", ReportUtils.map(restartingPrEP(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("generalPopulation", ReportUtils.map(generalPopulation(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("generalPopulation AND restartingPrEP");
		return cd;
	}
	
	/**
	 * MSM restating PrEP
	 * 
	 * @return
	 */
	public CohortDefinition restartOnPrEPMSM() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("restartingPrEP", ReportUtils.map(restartingPrEP(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("msm", ReportUtils.map(msm(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("msm AND restartingPrEP");
		return cd;
	}
	
	/**
	 * FSW restarting PrEP
	 * 
	 * @return
	 */
	public CohortDefinition restartOnPrEPFSW() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("restartingPrEP", ReportUtils.map(restartingPrEP(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("fsw", ReportUtils.map(fsw(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("fsw AND restartingPrEP");
		return cd;
	}
	
	/**
	 * PWID restarting PrEP
	 * 
	 * @return
	 */
	public CohortDefinition restartOnPrEPPWID() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("restartingPrEP", ReportUtils.map(restartingPrEP(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("pwid", ReportUtils.map(pwid(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("pwid AND restartingPrEP");
		return cd;
	}
	
	/**
	 * Discordant couple restarting PrEP
	 * 
	 * @return
	 */
	public CohortDefinition restartOnPreEPDiscordant() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("restartingPrEP", ReportUtils.map(restartingPrEP(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("discordantCoupleInHTS",
		    ReportUtils.map(discordantCoupleInHTS(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("discordantCoupleInHTS AND restartingPrEP");
		return cd;
	}
	
	/**
	 * Total Restarting PrEP
	 */
	public CohortDefinition restartingPrEPTotal() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("restartOnPrEPGP", ReportUtils.map(restartOnPrEPGP(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("restartOnPrEPFSW", ReportUtils.map(restartOnPrEPFSW(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("restartOnPrEPMSM", ReportUtils.map(restartOnPrEPMSM(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("restartOnPrEPPWID", ReportUtils.map(restartOnPrEPPWID(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("restartOnPreEPDiscordant",
		    ReportUtils.map(restartOnPreEPDiscordant(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("restartOnPrEPGP OR restartOnPrEPFSW OR restartOnPrEPMSM OR restartOnPrEPPWID OR restartOnPreEPDiscordant");
		return cd;
	}
	
	/**
	 * General population currently on PrEP. Counts initiated on PrEP, had a PrEP refill or
	 * restarted PrEP within the reporting period
	 * 
	 * @return
	 */
	public CohortDefinition currentOnPrEPGP() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("restartingPrEP", ReportUtils.map(restartingPrEP(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("refillOnPrEPGP", ReportUtils.map(refillOnPrEPGP(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("newlyOnPrEPGP", ReportUtils.map(newlyOnPrEPGP(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("generalPopulation", ReportUtils.map(generalPopulation(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("newlyOnPrEPGP OR refillOnPrEPGP OR (restartingPrEP AND generalPopulation)");
		return cd;
	}
	
	/**
	 * MSM currently on PrEP. Counts initiated on PrEP, had a PrEP refill or restarted PrEP within
	 * the reporting period
	 * 
	 * @return
	 */
	public CohortDefinition currentOnPrEPMSM() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("restartingPrEP", ReportUtils.map(restartingPrEP(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("refillOnPrEPMSM", ReportUtils.map(refillOnPrEPMSM(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("newlyOnPrEPMSM", ReportUtils.map(newlyOnPrEPMSM(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("msm", ReportUtils.map(msm(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("newlyOnPrEPMSM OR refillOnPrEPMSM OR (restartingPrEP AND msm)");
		return cd;
	}
	
	/**
	 * FSW currently on PrEP. Counts initiated on PrEP, had a PrEP refill or restarted PrEP within
	 * the reporting period
	 * 
	 * @return
	 */
	public CohortDefinition currentOnPrEPFSW() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("restartingPrEP", ReportUtils.map(restartingPrEP(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("refillOnPrEPFSW", ReportUtils.map(refillOnPrEPFSW(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("newlyOnPrEPFSW", ReportUtils.map(newlyOnPrEPFSW(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("fsw", ReportUtils.map(fsw(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("newlyOnPrEPFSW OR refillOnPrEPFSW OR (restartingPrEP AND fsw)");
		return cd;
	}
	
	/**
	 * PWID currently on PrEP. Counts initiated on PrEP, had a PrEP refill or restarted PrEP within
	 * the reporting period
	 * 
	 * @return
	 */
	public CohortDefinition currentOnPrEPPWID() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("restartingPrEP", ReportUtils.map(restartingPrEP(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("refillOnPrEPPWID", ReportUtils.map(refillOnPrEPPWID(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("newlyOnPrEPPWID", ReportUtils.map(newlyOnPrEPPWID(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("pwid", ReportUtils.map(pwid(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("newlyOnPrEPPWID OR refillOnPrEPPWID OR (restartingPrEP AND pwid)");
		return cd;
	}
	
	/**
	 * Discordant couples currently on PrEP. Counts initiated on PrEP, had a PrEP refill or
	 * restarted PrEP within the reporting period
	 * 
	 * @return
	 */
	public CohortDefinition currentOnPrEPDiscordant() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("restartingPrEP", ReportUtils.map(restartingPrEP(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("refillOnPrEPDiscordant",
		    ReportUtils.map(refillOnPrEPDiscordant(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("newlyOnPrEPDiscordant",
		    ReportUtils.map(newlyOnPrEPDiscordant(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("discordantCoupleInHTS",
		    ReportUtils.map(discordantCoupleInHTS(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("newlyOnPrEPDiscordant OR refillOnPrEPDiscordant OR (restartingPrEP AND discordantCoupleInHTS)");
		return cd;
	}
	
	/**
	 * Tested HIV Positive in their last HTS test within the reporting period
	 * 
	 * @return
	 */
	public CohortDefinition testedHIVPositive() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select t.patient_id from kenyaemr_etl.etl_hts_test t where date(t.visit_date) between date(:startDate) and date(:endDate)\n"
		        + "group by t.patient_id\n"
		        + "having mid(max(concat(t.visit_date,t.test_type)),11) = 2 and mid(max(concat(t.visit_date,t.final_test_result)),11) = 'Positive';";
		cd.setName("HIV Positive");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("HIV Positive");
		
		return cd;
	}
	
	/**
	 * General population clients who sero-converted (tested HIV+) while on PrEP, within the
	 * reporting period
	 * 
	 * @return
	 */
	public CohortDefinition positiveOnPrEPGP() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("testedHIVPositive", ReportUtils.map(testedHIVPositive(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("currentOnPrEPGP", ReportUtils.map(currentOnPrEPGP(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("currentOnPrEPGP AND testedHIVPositive");
		return cd;
	}
	
	/**
	 * MSM clients who sero-converted (tested HIV+) while on PrEP, within the reporting period
	 * 
	 * @return
	 */
	public CohortDefinition positiveOnPrEPMSM() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("testedHIVPositive", ReportUtils.map(testedHIVPositive(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("currentOnPrEPMSM", ReportUtils.map(currentOnPrEPMSM(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("currentOnPrEPMSM AND testedHIVPositive");
		return cd;
	}
	
	/**
	 * FSW clients who sero-converted (tested HIV+) while on PrEP, within the reporting period
	 * 
	 * @return
	 */
	public CohortDefinition positiveOnPrEPFSW() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("testedHIVPositive", ReportUtils.map(testedHIVPositive(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("currentOnPrEPFSW", ReportUtils.map(currentOnPrEPFSW(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("currentOnPrEPFSW AND testedHIVPositive");
		return cd;
	}
	
	/**
	 * PWID clients who sero-converted (tested HIV+) while on PrEP, within the reporting period
	 * 
	 * @return
	 */
	public CohortDefinition positiveOnPrEPPWID() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("testedHIVPositive", ReportUtils.map(testedHIVPositive(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("currentOnPrEPPWID", ReportUtils.map(currentOnPrEPPWID(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("currentOnPrEPPWID AND testedHIVPositive");
		return cd;
	}
	
	/**
	 * Discordant couples who sero-converted (tested HIV+) while on PrEP, within the reporting
	 * period
	 * 
	 * @return
	 */
	public CohortDefinition positiveOnPrEPDiscordant() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("testedHIVPositive", ReportUtils.map(testedHIVPositive(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("currentOnPrEPDiscordant",
		    ReportUtils.map(currentOnPrEPDiscordant(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("currentOnPrEPDiscordant AND testedHIVPositive");
		return cd;
	}
	
	/**
	 * Total clients who sero-convrted while on PrEP
	 * 
	 * @return
	 */
	public CohortDefinition positiveOnPrEP() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("positiveOnPrEPGP", ReportUtils.map(positiveOnPrEPGP(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("positiveOnPrEPMSM", ReportUtils.map(positiveOnPrEPMSM(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("positiveOnPrEPFSW", ReportUtils.map(positiveOnPrEPFSW(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("positiveOnPrEPPWID",
		    ReportUtils.map(positiveOnPrEPPWID(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("positiveOnPrEPDiscordant",
		    ReportUtils.map(positiveOnPrEPDiscordant(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("positiveOnPrEPGP OR positiveOnPrEPMSM OR positiveOnPrEPFSW OR positiveOnPrEPPWID OR positiveOnPrEPDiscordant");
		return cd;
	}
	
	/**
	 * Clients diagnosed with STI within the reporting period
	 * 
	 * @return
	 */
	public CohortDefinition diagnosedWithSTI() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("stiDiagnosedOnPrEPGP",
		    ReportUtils.map(stiDiagnosedOnPrEPGP(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("stiDiagnosedOnPrEPMSM",
		    ReportUtils.map(stiDiagnosedOnPrEPMSM(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("stiDiagnosedOnPrEPFSW",
		    ReportUtils.map(stiDiagnosedOnPrEPFSW(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("stiDiagnosedOnPrEPPWID",
		    ReportUtils.map(stiDiagnosedOnPrEPPWID(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("stiDiagnosedOnPrEPDiscordant",
		    ReportUtils.map(stiDiagnosedOnPrEPDiscordant(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("stiDiagnosedOnPrEPGP OR stiDiagnosedOnPrEPMSM OR stiDiagnosedOnPrEPFSW OR stiDiagnosedOnPrEPPWID OR stiDiagnosedOnPrEPDiscordant");
		return cd;
	}
	
	/**
	 * All patient diagnosed with STI
	 * 
	 * @return
	 */
	public CohortDefinition diagnosedWithSTISql() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select patient_id from kenyaemr_etl.etl_prep_followup f\n"
		        + "where f.genital_ulcer_disease = 'GUD' or f.vaginal_discharge = 'VG'\n"
		        + "   or f.cervical_discharge = 'CD' or f.pid = 'PID' or f.urethral_discharge = 'UD'\n"
		        + "   or f.anal_discharge = 'AD' or f.other_sti_symptoms is not null\n"
		        + "    and date(f.visit_date) between date(:startDate) and date(:endDate);";
		cd.setName("stiDiagnosedInPrEP");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("PrEp clients disgnosed with STI");
		return cd;
	}
	
	/**
	 * General population clients diagnosed with STI
	 * 
	 * @return
	 */
	public CohortDefinition stiDiagnosedOnPrEPGP() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("diagnosedWithSTISql",
		    ReportUtils.map(diagnosedWithSTISql(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("currentOnPrEPGP", ReportUtils.map(currentOnPrEPGP(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("diagnosedWithSTISql AND currentOnPrEPGP");
		return cd;
	}
	
	/**
	 * MSM clients diagnosed with STI
	 * 
	 * @return
	 */
	public CohortDefinition stiDiagnosedOnPrEPMSM() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("diagnosedWithSTISql",
		    ReportUtils.map(diagnosedWithSTISql(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("currentOnPrEPMSM", ReportUtils.map(currentOnPrEPMSM(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("diagnosedWithSTISql AND currentOnPrEPMSM");
		return cd;
	}
	
	/**
	 * FSW clients diagnosed with STI
	 * 
	 * @return
	 */
	public CohortDefinition stiDiagnosedOnPrEPFSW() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("diagnosedWithSTISql",
		    ReportUtils.map(diagnosedWithSTISql(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("currentOnPrEPFSW", ReportUtils.map(currentOnPrEPFSW(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("diagnosedWithSTISql AND currentOnPrEPFSW");
		return cd;
	}
	
	/**
	 * PWID diagnosed with STI
	 * 
	 * @return
	 */
	public CohortDefinition stiDiagnosedOnPrEPPWID() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("diagnosedWithSTISql",
		    ReportUtils.map(diagnosedWithSTISql(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("currentOnPrEPPWID", ReportUtils.map(currentOnPrEPPWID(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("diagnosedWithSTISql AND currentOnPrEPPWID");
		return cd;
	}
	
	/**
	 * Discordant couples diagnosed with STI
	 * 
	 * @return
	 */
	public CohortDefinition stiDiagnosedOnPrEPDiscordant() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("diagnosedWithSTISql",
		    ReportUtils.map(diagnosedWithSTISql(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("currentOnPrEPDiscordant",
		    ReportUtils.map(currentOnPrEPDiscordant(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("diagnosedWithSTISql AND currentOnPrEPDiscordant");
		return cd;
	}
	
	/**
	 * Total clients diagnosed with STI
	 * 
	 * @return
	 */
	/*	public CohortDefinition stiDiagnosed() {
			CompositionCohortDefinition cd = new CompositionCohortDefinition();
			cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
			cd.addParameter(new Parameter("endDate", "End Date", Date.class));
			cd.addSearch("diagnosedWithSTI", ReportUtils.map(diagnosedWithSTI(), "startDate=${startDate},endDate=${endDate}"));
			cd.addSearch("currentOnPrEP", ReportUtils.map(currentOnPrEP(), "startDate=${startDate},endDate=${endDate}"));
			cd.setCompositionString("diagnosedWithSTI AND currentOnPrEP");
			return cd;
		}*/
	
	/**
	 * Missed PrEP Appointment by more than 7 days. Excludes those discontinued
	 * 
	 * @return
	 */
	public CohortDefinition missedPrEPAppointment() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select a.patient_id\n"
		        + "    from (select e.patient_id,\n"
		        + "         max(e.visit_date) as latest_enrollment_date,\n"
		        + "         f.latest_fup_date,\n"
		        + "         greatest(ifnull(f.latest_fup_app_date,'0000-00-00'),ifnull(latest_refill_app_date,'0000-00-00')) as latest_appointment_date,\n"
		        + "         greatest(ifnull(latest_fup_date,'0000-00-00'),ifnull(latest_refill_visit_date,'0000-00-00')) as latest_visit_date,\n"
		        + "         r.latest_refill_visit_date,\n"
		        + "         f.latest_fup_app_date,\n"
		        + "         r.latest_refill_app_date,\n"
		        + "         d.latest_disc_date,\n"
		        + "         d.disc_patient\n"
		        + "  from kenyaemr_etl.etl_prep_enrolment e\n"
		        + "           left join\n"
		        + "       (select f.patient_id,\n"
		        + "               max(f.visit_date)                                      as latest_fup_date,\n"
		        + "               mid(max(concat(f.visit_date, f.appointment_date)), 11) as latest_fup_app_date\n"
		        + "        from kenyaemr_etl.etl_prep_followup f\n"
		        + "       where f.visit_date <= date(:endDate)\n"
		        + "        group by f.patient_id) f on e.patient_id = f.patient_id\n"
		        + "           left join (select r.patient_id,\n"
		        + "                             max(r.visit_date)                                      as latest_refill_visit_date,\n"
		        + "                             mid(max(concat(r.visit_date, r.next_appointment)), 11) as latest_refill_app_date\n"
		        + "                      from kenyaemr_etl.etl_prep_monthly_refill r\n"
		        + "           where r.visit_date <= date(:endDate)\n"
		        + "                      group by r.patient_id) r on e.patient_id = r.patient_id\n"
		        + "           left join (select patient_id as disc_patient,\n"
		        + "                             max(d.visit_date)                                        as latest_disc_date,\n"
		        + "                             mid(max(concat(d.visit_date, d.discontinue_reason)), 11) as latest_disc_reason\n"
		        + "                      from kenyaemr_etl.etl_prep_discontinuation d\n"
		        + "           where d.visit_date <= date(:endDate)\n" + "                      group by patient_id\n"
		        + "                      having latest_disc_date <= date(:endDate)) d on e.patient_id = d.disc_patient\n"
		        + "  group by e.patient_id\n" + "  having date(latest_appointment_date) < date(:endDate)\n"
		        + "     and date(latest_appointment_date) > date(latest_visit_date)\n"
		        + "     and timestampdiff(DAY, date(latest_appointment_date), date(:endDate)) > 7\n"
		        + "     and ((latest_enrollment_date > d.latest_disc_date\n"
		        + "               and latest_appointment_date > d.latest_disc_date)\n" + "     or d.disc_patient is null)\n"
		        + ") a;";
		cd.setName("Missed PrEP appointment");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("Missed PrEP appointment");
		
		return cd;
	}
	
	/**
	 * Discontinued from PrEP for non-adherence to drugs
	 * 
	 * @return
	 */
	public CohortDefinition discontinuedPrEPNonAdherence() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select a.patient_id from (select patient_id,\n"
		        + "       max(d.visit_date)                                        as latest_disc_date,\n"
		        + "       mid(max(concat(d.visit_date, d.discontinue_reason)), 11) as latest_disc_reason\n"
		        + "from kenyaemr_etl.etl_prep_discontinuation d\n"
		        + "group by patient_id\n"
		        + "having latest_disc_date between date(:startDate) and date(:endDate) and latest_disc_reason ='Not adherent to PrEP')a;";
		cd.setName("Discontinued PrEP non-adherence");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("Discontinued PrEP non-adherence");
		
		return cd;
	}
	
	/**
	 * Discontinued from PrEP for non-adherence to drugs during monthly refill
	 * 
	 * @return
	 */
	public CohortDefinition discontinuedPrEPNonAdherenceDuringRefill() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select a.patient_id\n"
		        + "from (select patient_id,\n"
		        + "             max(r.visit_date)                                              as latest_refill_date,\n"
		        + "             mid(max(concat(r.visit_date, r.prep_discontinue_reasons)), 11) as prep_discontinue_reasons,\n"
		        + "             mid(max(concat(r.visit_date, r.prep_status)), 11)              as latest_prep_status\n"
		        + "      from kenyaemr_etl.etl_prep_monthly_refill r\n" + "      group by patient_id\n"
		        + "      having latest_refill_date between date(:startDate) and date(:endDate)\n"
		        + "         and latest_prep_status = 'Discontinue'\n"
		        + "         and prep_discontinue_reasons = 'Not adherent to PrEP') a;\n";
		cd.setName("Discontinued PrEP non-adherence during refill");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("Discontinued from PrEP for non-adherence to drugs during monthly refill");
		
		return cd;
	}
	
	/**
	 * Discontinued in Latest follow-up within the reporting period for Acute HIV signs or weight <
	 * 35 kgs
	 * 
	 * @return
	 */
	public CohortDefinition discontinuedPrEPUnderWeightOrAcuteHIVSigns() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select a.patient_id\n"
		        + "from (select patient_id,\n"
		        + "             max(f.visit_date)                                           as latest_fup_date,\n"
		        + "             mid(max(concat(f.visit_date, f.hiv_signs)), 11)             as accute_hiv_signs,\n"
		        + "             mid(max(concat(f.visit_date, f.prep_contraindications)), 11) as latest_contraindication,\n"
		        + "             mid(max(concat(f.visit_date, f.treatment_plan)), 11)        as latest_treatment_plan\n"
		        + "      from kenyaemr_etl.etl_prep_followup f\n"
		        + "      group by patient_id\n"
		        + "      having latest_fup_date between date(:startDate) and date(:endDate)\n"
		        + "         and (accute_hiv_signs = 'Yes' or latest_contraindication like ('%Less than 35ks and under 15 yrs%'))\n"
		        + "         and latest_treatment_plan = 'Discontinue') a;";
		cd.setName("discontinuedPrEPUnderWeightOrAcuteHIVSigns");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("Discontinued in Latest follow-up within the reporting period for Acute HIV signs or weight < 35 kgs");
		
		return cd;
	}
	
	/**
	 * General population clients who were discontinued from PrEP within the reporting period Counts
	 * clients who self discontinued PrEP (i.e. did not honour appointment by more than 7 days) or
	 * discontinued by clinician due to poor adherence, current weight being less than 35kgs or due
	 * to prevailing signs of acute HIV
	 * 
	 * @return
	 */
	public CohortDefinition discontinuedPrEPGP() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("generalPopulation", ReportUtils.map(generalPopulation(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("missedPrEPAppointment",
		    ReportUtils.map(missedPrEPAppointment(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("discontinuedPrEPNonAdherence",
		    ReportUtils.map(discontinuedPrEPNonAdherence(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("discontinuedPrEPNonAdherenceDuringRefill",
		    ReportUtils.map(discontinuedPrEPNonAdherenceDuringRefill(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("discontinuedPrEPUnderWeightOrAcuteHIVSigns",
		    ReportUtils.map(discontinuedPrEPUnderWeightOrAcuteHIVSigns(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("generalPopulation AND (missedPrEPAppointment OR discontinuedPrEPNonAdherence OR discontinuedPrEPUnderWeightOrAcuteHIVSigns OR discontinuedPrEPNonAdherenceDuringRefill)");
		return cd;
	}
	
	/**
	 * MSM clients who were discontinued from PrEP within the reporting period Counts clients who
	 * self discontinued PrEP (i.e. did not honour appointment by more than 7 days) or discontinued
	 * by clinician due to poor adherence, current weight being less than 35kgs or due to prevailing
	 * signs of acute HIV
	 * 
	 * @return
	 */
	public CohortDefinition discontinuedPrEPMSM() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("msm", ReportUtils.map(msm(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("missedPrEPAppointment",
		    ReportUtils.map(missedPrEPAppointment(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("discontinuedPrEPNonAdherence",
		    ReportUtils.map(discontinuedPrEPNonAdherence(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("discontinuedPrEPNonAdherenceDuringRefill",
		    ReportUtils.map(discontinuedPrEPNonAdherenceDuringRefill(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("discontinuedPrEPUnderWeightOrAcuteHIVSigns",
		    ReportUtils.map(discontinuedPrEPUnderWeightOrAcuteHIVSigns(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("msm AND (missedPrEPAppointment OR discontinuedPrEPNonAdherence OR discontinuedPrEPUnderWeightOrAcuteHIVSigns OR discontinuedPrEPNonAdherenceDuringRefill)");
		return cd;
	}
	
	/**
	 * FSW clients who were discontinued from PrEP within the reporting period Counts clients who
	 * self discontinued PrEP (i.e. did not honour appointment by more than 7 days) or discontinued
	 * by clinician due to poor adherence, current weight being less than 35kgs or due to prevailing
	 * signs of acute HIV
	 * 
	 * @return
	 */
	public CohortDefinition discontinuedPrEPFSW() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("fsw", ReportUtils.map(fsw(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("missedPrEPAppointment",
		    ReportUtils.map(missedPrEPAppointment(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("discontinuedPrEPNonAdherence",
		    ReportUtils.map(discontinuedPrEPNonAdherence(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("discontinuedPrEPNonAdherenceDuringRefill",
		    ReportUtils.map(discontinuedPrEPNonAdherenceDuringRefill(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("discontinuedPrEPUnderWeightOrAcuteHIVSigns",
		    ReportUtils.map(discontinuedPrEPUnderWeightOrAcuteHIVSigns(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("fsw AND (missedPrEPAppointment OR discontinuedPrEPNonAdherence OR discontinuedPrEPUnderWeightOrAcuteHIVSigns OR discontinuedPrEPNonAdherenceDuringRefill)");
		return cd;
	}
	
	/**
	 * PWID clients who were discontinued from PrEP within the reporting period Counts clients who
	 * self discontinued PrEP (i.e. did not honour appointment by more than 7 days) or discontinued
	 * by clinician due to poor adherence, current weight being less than 35kgs or due to prevailing
	 * signs of acute HIV
	 * 
	 * @return
	 */
	public CohortDefinition discontinuedPrEPPWID() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("pwid", ReportUtils.map(pwid(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("missedPrEPAppointment",
		    ReportUtils.map(missedPrEPAppointment(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("discontinuedPrEPNonAdherence",
		    ReportUtils.map(discontinuedPrEPNonAdherence(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("discontinuedPrEPNonAdherenceDuringRefill",
		    ReportUtils.map(discontinuedPrEPNonAdherenceDuringRefill(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("discontinuedPrEPUnderWeightOrAcuteHIVSigns",
		    ReportUtils.map(discontinuedPrEPUnderWeightOrAcuteHIVSigns(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("pwid AND (missedPrEPAppointment OR discontinuedPrEPNonAdherence OR discontinuedPrEPUnderWeightOrAcuteHIVSigns OR discontinuedPrEPNonAdherenceDuringRefill)");
		return cd;
	}
	
	/**
	 * Discordant clients who were discontinued from PrEP within the reporting period Counts clients
	 * who self discontinued PrEP (i.e. did not honour appointment by more than 7 days) or
	 * discontinued by clinician due to poor adherence, current weight being less than 35kgs or due
	 * to prevailing signs of acute HIV
	 * 
	 * @return
	 */
	public CohortDefinition discontinuedPrEPDiscordant() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("discordantCoupleInHTS",
		    ReportUtils.map(discordantCoupleInHTS(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("missedPrEPAppointment",
		    ReportUtils.map(missedPrEPAppointment(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("discontinuedPrEPNonAdherence",
		    ReportUtils.map(discontinuedPrEPNonAdherence(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("discontinuedPrEPNonAdherenceDuringRefill",
		    ReportUtils.map(discontinuedPrEPNonAdherenceDuringRefill(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("discontinuedPrEPUnderWeightOrAcuteHIVSigns",
		    ReportUtils.map(discontinuedPrEPUnderWeightOrAcuteHIVSigns(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("discordantCoupleInHTS AND (missedPrEPAppointment OR discontinuedPrEPNonAdherence OR discontinuedPrEPUnderWeightOrAcuteHIVSigns OR discontinuedPrEPNonAdherenceDuringRefill)");
		return cd;
	}
	
	public CohortDefinition assessedForHIVRisk() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select e.patient_id from kenyaemr_etl.etl_prep_enrolment e inner join  kenyaemr_etl.etl_prep_behaviour_risk_assessment ba  on e.patient_id = ba.patient_id\n"
		        + " group by e.patient_id;";
		cd.setName("assessedForHIVRisk");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("Number assessed for HIV risk");
		
		return cd;
	}
	
	public CohortDefinition initiatedOnPrEP() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select e.patient_id from kenyaemr_etl.etl_prep_enrolment e\n"
		        + "where e.patient_type  ='New Patient' and date(e.visit_date) between date(:startDate) and date(:endDate);";
		cd.setName("initiatedOnPrEP");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("Number initiated on PrEP");
		
		return cd;
	}
	
	public CohortDefinition continuingOnPrEP() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select r.patient_id from kenyaemr_etl.etl_prep_monthly_refill r\n"
		        + "where r.prescribed_prep_today ='Yes' and date(r.visit_date) between date(:startDate) and date(:endDate);";
		cd.setName("continuingOnPrEP");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("Number continuing PrEP");
		
		return cd;
	}
	
	/**
	 * Clients currently on PrEP
	 * 
	 * @return
	 */
	public CohortDefinition currentOnPrEP() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("restartingPrEP", ReportUtils.map(restartingPrEP(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("refillOnPrEP", ReportUtils.map(refillOnPrEP(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("newlyOnPrEP", ReportUtils.map(newlyOnPrEP(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("newlyOnPrEP OR refillOnPrEP OR restartingPrEP");
		return cd;
	}
	
	public CohortDefinition retestedPositiveOnPrEP() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select e.patient_id\n"
		        + "from kenyaemr_etl.etl_prep_enrolment e\n"
		        + "       left join (select mid(max(concat(d.visit_date, d.patient_id)),11) as patient_id,d.discontinue_reason as discontine_reason from kenyaemr_etl.etl_prep_discontinuation d group by d.patient_id)pd on pd.patient_id = e.patient_id\n"
		        + "       left join (select mid(max(concat(e.visit_date, e.patient_id)),11) as patient_id from kenyaemr_etl.etl_hiv_enrollment e group by e.patient_id ) he on he.patient_id = e.patient_id\n"
		        + "       left join (select mid(max(concat(r.visit_date, r.patient_id)),11) as patient_id,r.prep_discontinue_reasons as prep_discontinue_reasons from kenyaemr_etl.etl_prep_monthly_refill r group by r.patient_id )mr on mr.patient_id = e.patient_id\n"
		        + "       left join (select mid(max(concat(t.visit_date, t.patient_id)),11) as patient_id,t.final_test_result,max(t.visit_date) as visit_date from kenyaemr_etl.etl_hts_test t  group by t.patient_id) ht on ht.patient_id = e.patient_id\n"
		        + "where  pd.discontine_reason = \"HIV test is positive\" or mr.prep_discontinue_reasons = \"HIV test is positive\" or ht.final_test_result=\"Positive\" and date(e.visit_date) between  date(:startDate) and date(:endDate) group by e.patient_id;";
		cd.setName("retestedPositiveOnPrEP");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("Number retested HIV+ while on PrEP");
		
		return cd;
	}
	
	/**
	 * Clients discontinued from PrEP within the period
	 * 
	 * @return
	 */
	public CohortDefinition discontinuedPrEP() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("discontinuedPrEPGP",
		    ReportUtils.map(discontinuedPrEPGP(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("discontinuedPrEPMSM",
		    ReportUtils.map(discontinuedPrEPMSM(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("discontinuedPrEPFSW",
		    ReportUtils.map(discontinuedPrEPFSW(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("discontinuedPrEPPWID",
		    ReportUtils.map(discontinuedPrEPPWID(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("discontinuedPrEPDiscordant",
		    ReportUtils.map(discontinuedPrEPDiscordant(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("discontinuedPrEPGP OR discontinuedPrEPMSM OR discontinuedPrEPFSW OR discontinuedPrEPPWID OR discontinuedPrEPDiscordant");
		return cd;
	}
}
