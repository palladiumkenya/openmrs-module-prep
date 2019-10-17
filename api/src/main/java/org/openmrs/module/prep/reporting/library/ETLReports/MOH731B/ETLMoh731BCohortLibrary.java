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

import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
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
	public CohortDefinition eligibleForPreEPGP() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select distinct e.patient_id\n" + "               from kenyaemr_etl.etl_prep_enrolment e\n"
		        + "               inner join kenyaemr_etl.etl_hts_test t\n"
		        + "               on e.patient_id = t.patient_id\n"
		        + "               where t.population_type = 'General Population'\n"
		        + "               and date(e.visit_date) between date(:startDate) and date(:endDate);";
		cd.setName("eligibleForPreEPGP");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("GP eligible for PrEP");
		
		return cd;
	}
	
	public CohortDefinition eligibleForPreEPMSM() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select distinct e.patient_id\n" + "from kenyaemr_etl.etl_prep_enrolment e\n"
		        + "inner join kenyaemr_etl.etl_hts_test t\n" + "on e.patient_id = t.patient_id\n"
		        + "where t.key_population_type = 'Men who have sex with men'\n"
		        + "and date(e.visit_date) between date(:startDate) and date(:endDate);";
		cd.setName("eligibleForPreEPMSM");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("MSM eligible for PrEP");
		
		return cd;
	}
	
	public CohortDefinition eligibleForPreEPFSW() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select distinct e.patient_id\n" + "from kenyaemr_etl.etl_prep_enrolment e\n"
		        + "  inner join kenyaemr_etl.etl_hts_test t\n" + "    on e.patient_id = t.patient_id\n"
		        + "where t.key_population_type = 'Female sex Worker'\n"
		        + "      and date(e.visit_date) between date(:startDate) and date(:endDate);";
		cd.setName("eligibleForPreEPFSW");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("FSW eligible for PrEP");
		
		return cd;
	}
	
	public CohortDefinition eligibleForPreEPPWID() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select distinct e.patient_id\n" + "from kenyaemr_etl.etl_prep_enrolment e\n"
		        + "  inner join kenyaemr_etl.etl_hts_test t\n" + "    on e.patient_id = t.patient_id\n"
		        + "where t.key_population_type = 'People who inject drugs'\n"
		        + "      and date(e.visit_date) between date(:startDate) and date(:endDate);";
		cd.setName("eligibleForPreEPPWID");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("PWID eligible for PrEP");
		
		return cd;
	}
	
	public CohortDefinition eligibleForPreEPDiscordant() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select distinct e.patient_id\n" + "from kenyaemr_etl.etl_prep_enrolment e\n"
		        + " inner join kenyaemr_etl.etl_hts_test t\n" + "on e.patient_id = t.patient_id\n"
		        + "where t.couple_discordant = 'Yes'\n"
		        + " and date(e.visit_date) between date(:startDate) and date(:endDate);";
		cd.setName("eligibleForPreEPDiscordant");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("Discordant eligible for PrEP");
		
		return cd;
	}
	
	public CohortDefinition newlyOnPreEPGP() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select e.patient_id from kenyaemr_etl.etl_prep_enrolment e\n"
		        + "                           inner join (select max(t.patient_id) patient_id from  kenyaemr_etl.etl_hts_test t where  t.population_type = 'General Population' group by t.patient_id) tst on tst.patient_id = e.patient_id\n"
		        + "where e.patient_type = 'New Patient' and date(e.visit_date) between date(:startDate) and date(:endDate);";
		cd.setName("newlyOnPreEPGP");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("GP newly on PrEP");
		
		return cd;
	}
	
	public CohortDefinition newlyOnPreEPMSM() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select e.patient_id from kenyaemr_etl.etl_prep_enrolment e\n"
		        + "                           inner join (select max(t.patient_id) patient_id from  kenyaemr_etl.etl_hts_test t where t.key_population_type ='Men who have sex with men' group by t.patient_id) tst on tst.patient_id = e.patient_id\n"
		        + "where e.patient_type = 'New Patient' and date(e.visit_date) between date(:startDate) and date(:endDate);";
		cd.setName("newlyOnPreEPMSM");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("MSM newly on PrEP");
		
		return cd;
	}
	
	public CohortDefinition newlyOnPreEPFSW() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select e.patient_id from kenyaemr_etl.etl_prep_enrolment e\n"
		        + "                           inner join (select max(t.patient_id) patient_id from  kenyaemr_etl.etl_hts_test t where t.key_population_type ='Female sex Worker' group by t.patient_id) tst on tst.patient_id = e.patient_id\n"
		        + "where e.patient_type = 'New Patient' and date(e.visit_date) between date(:startDate) and date(:endDate);";
		cd.setName("newlyOnPreEPFSW");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("FSW newly on PrEP");
		
		return cd;
	}
	
	public CohortDefinition newlyOnPreEPPWID() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select e.patient_id from kenyaemr_etl.etl_prep_enrolment e\n"
		        + "                           inner join (select max(t.patient_id) patient_id from  kenyaemr_etl.etl_hts_test t where t.key_population_type ='People who inject drugs' group by t.patient_id) tst on tst.patient_id = e.patient_id\n"
		        + "where e.patient_type = 'New Patient' and date(e.visit_date) between date(:startDate) and date(:endDate);";
		cd.setName("newlyOnPreEPPWID");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("PWID newly on PrEP");
		
		return cd;
	}
	
	public CohortDefinition newlyOnPreEPDiscordant() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select e.patient_id from kenyaemr_etl.etl_prep_enrolment e\n"
		        + "                           inner join (select max(t.patient_id) patient_id from  kenyaemr_etl.etl_hts_test t where t.couple_discordant = 'Yes' group by t.patient_id) tst on tst.patient_id = e.patient_id\n"
		        + "where e.patient_type = 'New Patient' and date(e.visit_date) between date(:startDate) and date(:endDate);";
		cd.setName("newlyOnPreEPDiscordant");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("Discordant newly on PrEP");
		
		return cd;
	}
	
	public CohortDefinition refillOnPreEPGP() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select r.patient_id from kenyaemr_etl.etl_prep_monthly_refill r\n"
		        + "                inner join (select max(t.patient_id) patient_id from kenyaemr_etl.etl_hts_test t where  t.population_type = 'General Population' group by t.patient_id) tst on tst.patient_id = r.patient_id\n"
		        + "where r.prescribed_prep_today ='Yes' and date(r.visit_date) between date(:startDate) and date(:endDate);";
		cd.setName("refillOnPreEPGP");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("GP refill on PrEP");
		
		return cd;
	}
	
	public CohortDefinition refillOnPreEPMSM() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select r.patient_id from kenyaemr_etl.etl_prep_monthly_refill r\n"
		        + "                           inner join (select max(t.patient_id) patient_id from kenyaemr_etl.etl_hts_test t where  t.key_population_type = 'Men who have sex with men' group by t.patient_id) tst on tst.patient_id = r.patient_id\n"
		        + "where r.prescribed_prep_today ='Yes' and date(r.visit_date) between date(:startDate) and date(:endDate);";
		cd.setName("refillOnPreEPMSM");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("MSM refill on PrEP");
		
		return cd;
	}
	
	public CohortDefinition refillOnPreEPFSW() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select r.patient_id from kenyaemr_etl.etl_prep_monthly_refill r\n"
		        + "                           inner join (select max(t.patient_id) patient_id from kenyaemr_etl.etl_hts_test t where  t.key_population_type = 'Female sex Worker' group by t.patient_id) tst on tst.patient_id = r.patient_id\n"
		        + "where r.prescribed_prep_today ='Yes' and date(r.visit_date) between date(:startDate) and date(:endDate);";
		cd.setName("refillOnPreEPFSW");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("FSW refill on PrEP");
		
		return cd;
	}
	
	public CohortDefinition refillOnPreEPPWID() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select r.patient_id from kenyaemr_etl.etl_prep_monthly_refill r\n"
		        + "                           inner join (select max(t.patient_id) patient_id from kenyaemr_etl.etl_hts_test t where  t.key_population_type = 'People who inject drugs' group by t.patient_id) tst on tst.patient_id = r.patient_id\n"
		        + "where r.prescribed_prep_today ='Yes' and date(r.visit_date) between date(:startDate) and date(:endDate);";
		cd.setName("refillOnPreEPPWID");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("PWID refill on PrEP");
		
		return cd;
	}
	
	public CohortDefinition refillOnPreEPDiscordant() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select r.patient_id from kenyaemr_etl.etl_prep_monthly_refill r\n"
		        + "                           inner join (select max(t.patient_id) patient_id from kenyaemr_etl.etl_hts_test t where t.couple_discordant = 'Yes' group by t.patient_id) tst on tst.patient_id = r.patient_id\n"
		        + "where r.prescribed_prep_today ='Yes' and date(r.visit_date) between date(:startDate) and date(:endDate);";
		cd.setName("refillOnPreEPDiscordant");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("Discordant refill on PrEP");
		
		return cd;
	}
	
	public CohortDefinition restartOnPreEPGP() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select e.patient_id from kenyaemr_etl.etl_prep_enrolment e\n"
		        + "                           inner join (select max(t.patient_id) patient_id from  kenyaemr_etl.etl_hts_test t where  t.population_type = 'General Population' group by t.patient_id) tst on tst.patient_id = e.patient_id\n"
		        + "where e.patient_type = 'Re-enrollment(Re-activation)' and date(e.visit_date) between date(:startDate) and date(:endDate);";
		cd.setName("restartOnPreEPGP");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("GP restart on PrEP");
		
		return cd;
	}
	
	public CohortDefinition restartOnPreEPMSM() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select e.patient_id from kenyaemr_etl.etl_prep_enrolment e\n"
		        + "                           inner join (select max(t.patient_id) patient_id from  kenyaemr_etl.etl_hts_test t where t.key_population_type ='Men who have sex with men' group by t.patient_id) tst on tst.patient_id = e.patient_id\n"
		        + "where e.patient_type = 'Re-enrollment(Re-activation)' and date(e.visit_date) between date(:startDate) and date(:endDate);";
		cd.setName("restartOnPreEPMSM");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("MSM restart on PrEP");
		
		return cd;
	}
	
	public CohortDefinition restartOnPreEPFSW() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select e.patient_id from kenyaemr_etl.etl_prep_enrolment e\n"
		        + "                           inner join (select max(t.patient_id) patient_id from  kenyaemr_etl.etl_hts_test t where t.key_population_type ='Female sex Worker' group by t.patient_id) tst on tst.patient_id = e.patient_id\n"
		        + "where e.patient_type = 'Re-enrollment(Re-activation)' and date(e.visit_date) between date(:startDate) and date(:endDate);";
		cd.setName("restartOnPreEPFSW");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("FSW restart on PrEP");
		
		return cd;
	}
	
	public CohortDefinition restartOnPreEPPWID() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select e.patient_id from kenyaemr_etl.etl_prep_enrolment e\n"
		        + "                           inner join (select max(t.patient_id) patient_id from  kenyaemr_etl.etl_hts_test t where t.key_population_type ='People who inject drugs' group by t.patient_id) tst on tst.patient_id = e.patient_id\n"
		        + "where e.patient_type = 'Re-enrollment(Re-activation)' and date(e.visit_date) between date(:startDate) and date(:endDate);";
		cd.setName("restartOnPreEPPWID");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("PWID restart on PrEP");
		
		return cd;
	}
	
	public CohortDefinition restartOnPreEPDiscordant() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select e.patient_id from kenyaemr_etl.etl_prep_enrolment e\n"
		        + "                           inner join (select max(t.patient_id) patient_id from  kenyaemr_etl.etl_hts_test t where t.couple_discordant = 'Yes' group by t.patient_id) tst on tst.patient_id = e.patient_id\n"
		        + "where e.patient_type = 'Re-enrollment(Re-activation)' and date(e.visit_date) between date(:startDate) and date(:endDate);";
		cd.setName("restartOnPreEPDiscordant");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("Discordant restart on PrEP");
		
		return cd;
	}
	
	public CohortDefinition currentOnPreEPGP() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select e.patient_id from kenyaemr_etl.etl_prep_enrolment e\n"
		        + "                           inner join (select max(t.patient_id) patient_id from  kenyaemr_etl.etl_hts_test t where t.population_type ='General Population' group by t.patient_id) tst on tst.patient_id = e.patient_id\n"
		        + "left outer join kenyaemr_etl.etl_prep_monthly_refill r on e.patient_id = r.patient_id\n"
		        + "where (e.patient_type in ('New Patient','Re-enrollment(Re-activation)') or r.prescribed_prep_today = 'Yes') and (date(e.visit_date) between date(:startDate) and date(:endDate) or date(r.visit_date) between date(:startDate) and date(:endDate));";
		cd.setName("currentOnPreEPGP");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("GP current on PrEP");
		
		return cd;
	}
	
	public CohortDefinition currentOnPreEPMSM() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select e.patient_id from kenyaemr_etl.etl_prep_enrolment e\n"
		        + "                           inner join (select max(t.patient_id) patient_id from  kenyaemr_etl.etl_hts_test t where t.key_population_type ='Men who have sex with men' group by t.patient_id) tst on tst.patient_id = e.patient_id\n"
		        + "                           left outer join kenyaemr_etl.etl_prep_monthly_refill r on e.patient_id = r.patient_id\n"
		        + "where (e.patient_type in ('New Patient','Re-enrollment(Re-activation)') or r.prescribed_prep_today = 'Yes') and (date(e.visit_date) between date(:startDate) and date(:endDate) or date(r.visit_date) between date(:startDate) and date(:endDate));";
		cd.setName("currentOnPreEPMSM");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("MSM current on PrEP");
		
		return cd;
	}
	
	public CohortDefinition currentOnPreEPFSW() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select e.patient_id from kenyaemr_etl.etl_prep_enrolment e\n"
		        + "                           inner join (select max(t.patient_id) patient_id from  kenyaemr_etl.etl_hts_test t where t.key_population_type ='Female sex Worker' group by t.patient_id) tst on tst.patient_id = e.patient_id\n"
		        + "                           left outer join kenyaemr_etl.etl_prep_monthly_refill r on e.patient_id = r.patient_id\n"
		        + "where (e.patient_type in ('New Patient','Re-enrollment(Re-activation)') or r.prescribed_prep_today = 'Yes') and (date(e.visit_date) between date(:startDate) and date(:endDate) or date(r.visit_date) between date(:startDate) and date(:endDate));";
		cd.setName("currentOnPreEPFSW");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("FSW current on PrEP");
		
		return cd;
	}
	
	public CohortDefinition currentOnPreEPPWID() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select e.patient_id from kenyaemr_etl.etl_prep_enrolment e\n"
		        + "                           inner join (select max(t.patient_id) patient_id from  kenyaemr_etl.etl_hts_test t where t.key_population_type ='People who inject drugs' group by t.patient_id) tst on tst.patient_id = e.patient_id\n"
		        + "                           left outer join kenyaemr_etl.etl_prep_monthly_refill r on e.patient_id = r.patient_id\n"
		        + "where (e.patient_type in ('New Patient','Re-enrollment(Re-activation)') or r.prescribed_prep_today = 'Yes') and (date(e.visit_date) between date(:startDate) and date(:endDate) or date(r.visit_date) between date(:startDate) and date(:endDate));";
		cd.setName("currentOnPreEPPWID");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("PWID current on PrEP");
		
		return cd;
	}
	
	public CohortDefinition currentOnPreEPDiscordant() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select e.patient_id from kenyaemr_etl.etl_prep_enrolment e\n"
		        + "                           inner join (select max(t.patient_id) patient_id from  kenyaemr_etl.etl_hts_test t where t.couple_discordant = 'Yes' group by t.patient_id) tst on tst.patient_id = e.patient_id\n"
		        + "                           left outer join kenyaemr_etl.etl_prep_monthly_refill r on e.patient_id = r.patient_id\n"
		        + "where (e.patient_type in ('New Patient','Re-enrollment(Re-activation)') or r.prescribed_prep_today = 'Yes') and (date(e.visit_date) between date(:startDate) and date(:endDate) or date(r.visit_date) between date(:startDate) and date(:endDate));";
		cd.setName("currentOnPreEPDiscordant");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("Discordant current on PrEP");
		
		return cd;
	}
	
	public CohortDefinition positiveOnPreEPGP() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select e.patient_id from kenyaemr_etl.etl_prep_enrolment e\n"
		        + "                           inner join (select max(t.patient_id) patient_id, max(date(t.visit_date)) test_date from kenyaemr_etl.etl_hts_test t where t.population_type ='General Population' group by t.patient_id) tst on tst.patient_id = e.patient_id\n"
		        + "                           left outer join kenyaemr_etl.etl_prep_monthly_refill r on e.patient_id = r.patient_id\n"
		        + "left outer join kenyaemr_etl.etl_prep_discontinuation d on e.patient_id = d.patient_id\n"
		        + "where date(tst.test_date) > date(e.visit_date) and date(tst.test_date) < ifnull(date(d.visit_date),now()) and tst.test_date between date(:startDate) and date(:endDate);";
		cd.setName("positiveOnPreEPGP");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("GP positive on PrEP");
		
		return cd;
	}
	
	public CohortDefinition positiveOnPreEPMSM() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select e.patient_id from kenyaemr_etl.etl_prep_enrolment e\n"
		        + "                                                                                                             inner join (select max(t.patient_id) patient_id, max(date(t.visit_date)) test_date from kenyaemr_etl.etl_hts_test t where t.key_population_type ='Men who have sex with men' group by t.patient_id) tst on tst.patient_id = e.patient_id\n"
		        + "                                                                                                             left outer join kenyaemr_etl.etl_prep_monthly_refill r on e.patient_id = r.patient_id\n"
		        + "                                                                                                             left outer join kenyaemr_etl.etl_prep_discontinuation d on e.patient_id = d.patient_id\n"
		        + "where date(tst.test_date) > date(e.visit_date) and date(tst.test_date) < ifnull(date(d.visit_date),now()) and tst.test_date between date(:startDate) and date(:endDate);";
		cd.setName("positiveOnPreEPMSM");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("MSM positive on PrEP");
		
		return cd;
	}
	
	public CohortDefinition positiveOnPreEPFSW() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select e.patient_id from kenyaemr_etl.etl_prep_enrolment e\n"
		        + "                                                                                                             inner join (select max(t.patient_id) patient_id, max(date(t.visit_date)) test_date from kenyaemr_etl.etl_hts_test t where t.key_population_type ='Female sex Worker' group by t.patient_id) tst on tst.patient_id = e.patient_id\n"
		        + "                                                                                                             left outer join kenyaemr_etl.etl_prep_monthly_refill r on e.patient_id = r.patient_id\n"
		        + "                                                                                                             left outer join kenyaemr_etl.etl_prep_discontinuation d on e.patient_id = d.patient_id\n"
		        + "where date(tst.test_date) > date(e.visit_date) and date(tst.test_date) < ifnull(date(d.visit_date),now()) and tst.test_date between date(:startDate) and date(:endDate);";
		cd.setName("positiveOnPreEPFSW");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("FSW positive on PrEP");
		
		return cd;
	}
	
	public CohortDefinition positiveOnPreEPPWID() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select e.patient_id from kenyaemr_etl.etl_prep_enrolment e\n"
		        + "                                                                                                             inner join (select max(t.patient_id) patient_id, max(date(t.visit_date)) test_date from kenyaemr_etl.etl_hts_test t where t.key_population_type ='People who inject drugs' group by t.patient_id) tst on tst.patient_id = e.patient_id\n"
		        + "                                                                                                             left outer join kenyaemr_etl.etl_prep_monthly_refill r on e.patient_id = r.patient_id\n"
		        + "                                                                                                             left outer join kenyaemr_etl.etl_prep_discontinuation d on e.patient_id = d.patient_id\n"
		        + "where date(tst.test_date) > date(e.visit_date) and date(tst.test_date) < ifnull(date(d.visit_date),now()) and tst.test_date between date(:startDate) and date(:endDate);";
		cd.setName("positiveOnPreEPPWID");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("PWID positive on PrEP");
		
		return cd;
	}
	
	public CohortDefinition positiveOnPreEPDiscordant() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select e.patient_id from kenyaemr_etl.etl_prep_enrolment e\n"
		        + "                                                                                                             inner join (select max(t.patient_id) patient_id, max(date(t.visit_date)) test_date from kenyaemr_etl.etl_hts_test t where  t.couple_discordant = 'Yes' group by t.patient_id) tst on tst.patient_id = e.patient_id\n"
		        + "                                                                                                             left outer join kenyaemr_etl.etl_prep_monthly_refill r on e.patient_id = r.patient_id\n"
		        + "                                                                                                             left outer join kenyaemr_etl.etl_prep_discontinuation d on e.patient_id = d.patient_id\n"
		        + "where date(tst.test_date) > date(e.visit_date) and date(tst.test_date) < ifnull(date(d.visit_date),now()) and tst.test_date between date(:startDate) and date(:endDate);";
		cd.setName("positiveOnPreEPDiscordant");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("Discordant positive on PrEP");
		
		return cd;
	}
	
	public CohortDefinition stiDiagnosedOnPreEPGP() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select max(f.patient_id) patient_id from kenyaemr_etl.etl_prep_followup f\n"
		        + "                                           inner join (select max(t.patient_id) patient_id, max(date(t.visit_date)) test_date from kenyaemr_etl.etl_hts_test t where t.population_type ='General Population' group by t.patient_id) tst on tst.patient_id = f.patient_id\n"
		        + "where concat(f.genital_ulcer_desease,vaginal_discharge,cervical_discharge,f.pid,f.urethral_discharge,f.anal_discharge,f.other_sti_symptoms) is not null\n"
		        + "and f.visit_date between date(:startDate) and date(:endDate)\n" + "group by f.patient_id;";
		cd.setName("stiDiagnosedOnPreEPGP");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("GP STI disgnosed on PrEP");
		
		return cd;
	}
	
	public CohortDefinition stiDiagnosedOnPreEPMSM() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select max(f.patient_id) patient_id from kenyaemr_etl.etl_prep_followup f\n"
		        + "                                           inner join (select max(t.patient_id) patient_id, max(date(t.visit_date)) test_date from kenyaemr_etl.etl_hts_test t where t.key_population_type ='Men who have sex with men' group by t.patient_id) tst on tst.patient_id = f.patient_id\n"
		        + "where concat(f.genital_ulcer_desease,vaginal_discharge,cervical_discharge,f.pid,f.urethral_discharge,f.anal_discharge,f.other_sti_symptoms) is not null\n"
		        + "  and f.visit_date between date(:startDate) and date(:endDate)\n" + "group by f.patient_id;";
		cd.setName("stiDiagnosedOnPreEPMSM");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("MSM STI disgnosed on PrEP");
		
		return cd;
	}
	
	public CohortDefinition stiDiagnosedOnPreEPFSW() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select max(f.patient_id) patient_id from kenyaemr_etl.etl_prep_followup f\n"
		        + "                                           inner join (select max(t.patient_id) patient_id, max(date(t.visit_date)) test_date from kenyaemr_etl.etl_hts_test t where t.key_population_type ='Female sex Worker' group by t.patient_id) tst on tst.patient_id = f.patient_id\n"
		        + "where concat(f.genital_ulcer_desease,vaginal_discharge,cervical_discharge,f.pid,f.urethral_discharge,f.anal_discharge,f.other_sti_symptoms) is not null\n"
		        + "  and f.visit_date between date(:startDate) and date(:endDate)\n" + "group by f.patient_id;";
		cd.setName("stiDiagnosedOnPreEPFSW");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("FSW STI disgnosed on PrEP");
		
		return cd;
	}
	
	public CohortDefinition stiDiagnosedOnPreEPPWID() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select max(f.patient_id) patient_id from kenyaemr_etl.etl_prep_followup f\n"
		        + "                                           inner join (select max(t.patient_id) patient_id, max(date(t.visit_date)) test_date from kenyaemr_etl.etl_hts_test t where t.key_population_type ='People who inject drugs' group by t.patient_id) tst on tst.patient_id = f.patient_id\n"
		        + "where concat(f.genital_ulcer_desease,vaginal_discharge,cervical_discharge,f.pid,f.urethral_discharge,f.anal_discharge,f.other_sti_symptoms) is not null\n"
		        + "  and f.visit_date between date(:startDate) and date(:endDate)\n" + "group by f.patient_id;";
		cd.setName("stiDiagnosedOnPreEPPWID");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("PWID STI disgnosed on PrEP");
		
		return cd;
	}
	
	public CohortDefinition stiDiagnosedOnPreEPDiscordant() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select max(f.patient_id) patient_id from kenyaemr_etl.etl_prep_followup f\n"
		        + "                                           inner join (select max(t.patient_id) patient_id, max(date(t.visit_date)) test_date from kenyaemr_etl.etl_hts_test t where t.couple_discordant = 'Yes' group by t.patient_id) tst on tst.patient_id = f.patient_id\n"
		        + "where concat(f.genital_ulcer_desease,vaginal_discharge,cervical_discharge,f.pid,f.urethral_discharge,f.anal_discharge,f.other_sti_symptoms) is not null\n"
		        + "  and f.visit_date between date(:startDate) and date(:endDate)\n" + "group by f.patient_id;";
		cd.setName("stiDiagnosedOnPreEPDiscordant");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("Discordant STI disgnosed on PrEP");
		
		return cd;
	}
	
	public CohortDefinition discontinuedPreEPGP() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select max(d.patient_id) patient_id from kenyaemr_etl.etl_prep_discontinuation d\n"
		        + "                                           inner join (select max(t.patient_id) patient_id, max(date(t.visit_date)) test_date from kenyaemr_etl.etl_hts_test t where t.population_type ='General Population' group by t.patient_id) tst on tst.patient_id = d.patient_id\n"
		        + "where d.visit_date between date(:startDate) and date(:endDate)\n" + "group by d.patient_id;";
		cd.setName("stiDiagnosedOnPreEPGP");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("GP Discontinued PrEP");
		
		return cd;
	}
	
	public CohortDefinition discontinuedPreEPMSM() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select max(d.patient_id) patient_id from kenyaemr_etl.etl_prep_discontinuation d\n"
		        + "                                           inner join (select max(t.patient_id) patient_id, max(date(t.visit_date)) test_date from kenyaemr_etl.etl_hts_test t where t.key_population_type ='Men who have sex with men' group by t.patient_id) tst on tst.patient_id = d.patient_id\n"
		        + "where d.visit_date between date(:startDate) and date(:endDate)\n" + "group by d.patient_id;";
		cd.setName("discontinuedPreEPMSM");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("MSM Discontinued PrEP");
		
		return cd;
	}
	
	public CohortDefinition discontinuedPreEPFSW() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select max(d.patient_id) patient_id from kenyaemr_etl.etl_prep_discontinuation d\n"
		        + "                                           inner join (select max(t.patient_id) patient_id, max(date(t.visit_date)) test_date from kenyaemr_etl.etl_hts_test t where t.key_population_type ='Female sex Worker' group by t.patient_id) tst on tst.patient_id = d.patient_id\n"
		        + "where d.visit_date between date(:startDate) and date(:endDate)\n" + "group by d.patient_id;";
		cd.setName("discontinuedPreEPFSW");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("FSW Discontinued PrEP");
		
		return cd;
	}
	
	public CohortDefinition discontinuedPreEPPWID() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select max(d.patient_id) patient_id from kenyaemr_etl.etl_prep_discontinuation d\n"
		        + "                                           inner join (select max(t.patient_id) patient_id, max(date(t.visit_date)) test_date from kenyaemr_etl.etl_hts_test t where t.key_population_type ='People who inject drugs' group by t.patient_id) tst on tst.patient_id = d.patient_id\n"
		        + "where d.visit_date between date(:startDate) and date(:endDate)\n" + "group by d.patient_id;";
		cd.setName("discontinuedPreEPPWID");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("PWID Discontinued PrEP");
		
		return cd;
	}
	
	public CohortDefinition discontinuedPreEPDiscordant() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select max(d.patient_id) patient_id from kenyaemr_etl.etl_prep_discontinuation d\n"
		        + "                                           inner join (select max(t.patient_id) patient_id, max(date(t.visit_date)) test_date from kenyaemr_etl.etl_hts_test t where t.couple_discordant = 'Yes' group by t.patient_id) tst on tst.patient_id = d.patient_id\n"
		        + "where d.visit_date between date(:startDate) and date(:endDate)\n" + "group by d.patient_id;";
		cd.setName("discontinuedPreEPDiscordant");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("Discordant Discontinued PrEP");
		
		return cd;
	}
}
