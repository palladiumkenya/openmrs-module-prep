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
 * Library of cohort definitions used specifically in the MOH731 report based on ETL tables. It has incorporated green card components
 */
@Component


public class ETLMoh731BCohortLibrary {
    /**
     * TODO: Requires team review
     * @return
     */
    public CohortDefinition eligibleForPreEPGP(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select distinct e.patient_id\n" +
                "               from kenyaemr_etl.etl_prep_enrolment e\n" +
                "               left outer join kenyaemr_etl.etl_hts_test t\n" +
                "               on e.patient_id = t.patient_id\n" +
                "               where t.population_type = 'General Population'\n" +
                "               and date(e.visit_date) between date(:startDate) and date(:endDate);";
        cd.setName("eligibleForPreEPGP");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("GP eligible for PrEP");

        return cd;
    }

    public CohortDefinition eligibleForPreEPMSM(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select distinct e.patient_id\n" +
                "from kenyaemr_etl.etl_prep_enrolment e\n" +
                "left outer join kenyaemr_etl.etl_hts_test t\n" +
                "on e.patient_id = t.patient_id\n" +
                "where t.key_population_type = 'Men who have sex with men'\n" +
                "and date(e.visit_date) between date(:startDate) and date(:endDate);";
        cd.setName("eligibleForPreEPMSM");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("MSM eligible for PrEP");

        return cd;
    }

    public CohortDefinition eligibleForPreEPFSW(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select distinct e.patient_id\n" +
                "from kenyaemr_etl.etl_prep_enrolment e\n" +
                "  left outer join kenyaemr_etl.etl_hts_test t\n" +
                "    on e.patient_id = t.patient_id\n" +
                "where t.key_population_type = 'Female sex Worker'\n" +
                "      and date(e.visit_date) between date(:startDate) and date(:endDate);";
        cd.setName("eligibleForPreEPFSW");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("FSW eligible for PrEP");

        return cd;
    }

    public CohortDefinition eligibleForPreEPPWID(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select distinct e.patient_id\n" +
                "from kenyaemr_etl.etl_prep_enrolment e\n" +
                "  left outer join kenyaemr_etl.etl_hts_test t\n" +
                "    on e.patient_id = t.patient_id\n" +
                "where t.key_population_type = 'People who inject drugs'\n" +
                "      and date(e.visit_date) between date(:startDate) and date(:endDate);";
        cd.setName("eligibleForPreEPPWID");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("PWID eligible for PrEP");

        return cd;
    }

    public CohortDefinition eligibleForPreEPDiscordant(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select distinct e.patient_id\n" +
                "from kenyaemr_etl.etl_prep_enrolment e\n" +
                " left outer join kenyaemr_etl.etl_hts_test t\n" +
                "on e.patient_id = t.patient_id\n" +
                "where t.couple_discordant = 'Yes'\n" +
                " and date(e.visit_date) between date(:startDate) and date(:endDate);";
        cd.setName("eligibleForPreEPDiscordant");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("Discordant eligible for PrEP");

        return cd;
    }

    public CohortDefinition newlyOnPreEPGP(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select distinct e.patient_id\n" +
                "from kenyaemr_etl.etl_prep_enrolment e\n" +
                "  left outer join kenyaemr_etl.etl_prep_followup pf  on e.patient_id = pf.patient_id\n" +
                "  left outer join kenyaemr_etl.etl_hts_test t on e.patient_id = t.patient_id\n" +
                "where t.population_type = 'General Population' and pf.treatment_plan = 'Start'\n" +
                "      and date(e.visit_date) between date(:startDate) and date(:endDate);";
        cd.setName("newlyOnPreEPGP");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("GP newly on PrEP");

        return cd;
    }

    public CohortDefinition newlyOnPreEPMSM(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select distinct e.patient_id\n" +
                "from kenyaemr_etl.etl_prep_enrolment e\n" +
                "  left outer join kenyaemr_etl.etl_prep_followup pf  on e.patient_id = pf.patient_id\n" +
                "  left outer join kenyaemr_etl.etl_hts_test t on e.patient_id = t.patient_id\n" +
                "where t.key_population_type = 'Men who have sex with men' and pf.treatment_plan = 'Start'\n" +
                "      and date(e.visit_date) between date(:startDate) and date(:endDate);";
        cd.setName("newlyOnPreEPMSM");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("MSM newly on PrEP");

        return cd;
    }

    public CohortDefinition newlyOnPreEPFSW(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select distinct e.patient_id\n" +
                "from kenyaemr_etl.etl_prep_enrolment e\n" +
                "  left outer join kenyaemr_etl.etl_prep_followup pf  on e.patient_id = pf.patient_id\n" +
                "  left outer join kenyaemr_etl.etl_hts_test t on e.patient_id = t.patient_id\n" +
                "where t.key_population_type = 'Female sex Worker' and pf.treatment_plan = 'Start'\n" +
                "      and date(e.visit_date) between date(:startDate) and date(:endDate);";
        cd.setName("newlyOnPreEPFSW");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("FSW newly on PrEP");

        return cd;
    }

    public CohortDefinition newlyOnPreEPPWID(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select distinct e.patient_id\n" +
                "from kenyaemr_etl.etl_prep_enrolment e\n" +
                "  left outer join kenyaemr_etl.etl_prep_followup pf  on e.patient_id = pf.patient_id\n" +
                "  left outer join kenyaemr_etl.etl_hts_test t on e.patient_id = t.patient_id\n" +
                "where t.key_population_type = 'People who inject drugs' and pf.treatment_plan = 'Start'\n" +
                "      and date(e.visit_date) between date(:startDate) and date(:endDate);";
        cd.setName("newlyOnPreEPPWID");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("PWID newly on PrEP");

        return cd;
    }

    public CohortDefinition newlyOnPreEPDiscordant(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select distinct e.patient_id\n" +
                "from kenyaemr_etl.etl_prep_enrolment e\n" +
                "  left outer join kenyaemr_etl.etl_prep_followup pf  on e.patient_id = pf.patient_id\n" +
                "  left outer join kenyaemr_etl.etl_hts_test t on e.patient_id = t.patient_id\n" +
                "where t.couple_discordant = 'Yes' and pf.treatment_plan = 'Start'\n" +
                "      and date(e.visit_date) between date(:startDate) and date(:endDate);";
        cd.setName("newlyOnPreEPDiscordant");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("Discordant newly on PrEP");

        return cd;
    }

    public CohortDefinition refillOnPreEPGP(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select distinct e.patient_id\n" +
                "from kenyaemr_etl.etl_prep_enrolment e\n" +
                "left outer join kenyaemr_etl.etl_prep_monthly_refill mr  on e.patient_id = mr.patient_id\n" +
                "left outer join kenyaemr_etl.etl_hts_test t on e.patient_id = t.patient_id\n" +
                "where t.population_type = 'General Population' and mr.prep_status = 'Continue'\n" +
                "and date(e.visit_date) between date(:startDate) and date(:endDate);";
        cd.setName("refillOnPreEPGP");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("GP refill on PrEP");

        return cd;
    }

    public CohortDefinition refillOnPreEPMSM(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select distinct e.patient_id\n" +
                "from kenyaemr_etl.etl_prep_enrolment e\n" +
                "  left outer join kenyaemr_etl.etl_prep_monthly_refill mr  on e.patient_id = mr.patient_id\n" +
                "  left outer join kenyaemr_etl.etl_hts_test t on e.patient_id = t.patient_id\n" +
                "where t.key_population_type = 'Men who have sex with men' and mr.prep_status = 'Continue'\n" +
                "      and date(e.visit_date) between date(:startDate) and date(:endDate);";
        cd.setName("refillOnPreEPMSM");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("MSM refill on PrEP");

        return cd;
    }

    public CohortDefinition refillOnPreEPFSW(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select distinct e.patient_id\n" +
                "from kenyaemr_etl.etl_prep_enrolment e\n" +
                "  left outer join kenyaemr_etl.etl_prep_monthly_refill mr  on e.patient_id = mr.patient_id\n" +
                "  left outer join kenyaemr_etl.etl_hts_test t on e.patient_id = t.patient_id\n" +
                "where t.key_population_type = 'Female sex Worker' and mr.prep_status = 'Continue'\n" +
                "      and date(e.visit_date) between date(:startDate) and date(:endDate);";
        cd.setName("refillOnPreEPFSW");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("FSW refill on PrEP");

        return cd;
    }

    public CohortDefinition refillOnPreEPPWID(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select distinct e.patient_id\n" +
                "from kenyaemr_etl.etl_prep_enrolment e\n" +
                "  left outer join kenyaemr_etl.etl_prep_monthly_refill mr  on e.patient_id = mr.patient_id\n" +
                "  left outer join kenyaemr_etl.etl_hts_test t on e.patient_id = t.patient_id\n" +
                "where t.key_population_type = 'People who inject drugs' and mr.prep_status = 'Continue'\n" +
                "and date(e.visit_date) between date(:startDate) and date(:endDate);";
        cd.setName("refillOnPreEPPWID");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("PWID refill on PrEP");

        return cd;
    }

    public CohortDefinition refillOnPreEPDiscordant(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select distinct e.patient_id\n" +
                "from kenyaemr_etl.etl_prep_enrolment e\n" +
                "  left outer join kenyaemr_etl.etl_prep_monthly_refill mr  on e.patient_id = mr.patient_id\n" +
                "  left outer join kenyaemr_etl.etl_hts_test t on e.patient_id = t.patient_id\n" +
                "where t.couple_discordant = 'Yes' and mr.prep_status = 'Continue'\n" +
                "      and date(e.visit_date) between date(:startDate) and date(:endDate);";
        cd.setName("refillOnPreEPDiscordant");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("Discordant refill on PrEP");

        return cd;
    }

    public CohortDefinition restartOnPreEPGP(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select distinct e.patient_id\n" +
                "from kenyaemr_etl.etl_prep_enrolment e\n" +
                "  left outer join kenyaemr_etl.etl_prep_followup pf  on e.patient_id = pf.patient_id\n" +
                "  left outer join kenyaemr_etl.etl_hts_test t on e.patient_id = t.patient_id\n" +
                "where t.population_type = 'General Population' and pf.treatment_plan = 'Restart'\n" +
                "and date(e.visit_date) between date(:startDate) and date(:endDate);";
        cd.setName("restartOnPreEPGP");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("GP restart on PrEP");

        return cd;
    }

    public CohortDefinition restartOnPreEPMSM(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select distinct e.patient_id\n" +
                "from kenyaemr_etl.etl_prep_enrolment e\n" +
                "  left outer join kenyaemr_etl.etl_prep_followup pf  on e.patient_id = pf.patient_id\n" +
                "  left outer join kenyaemr_etl.etl_hts_test t on e.patient_id = t.patient_id\n" +
                "where t.key_population_type = 'Men who have sex with men' and pf.treatment_plan = 'Restart'\n" +
                " and date(e.visit_date) between date(:startDate) and date(:endDate);";
        cd.setName("restartOnPreEPMSM");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("MSM restart on PrEP");

        return cd;
    }

    public CohortDefinition restartOnPreEPFSW(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select distinct e.patient_id\n" +
                "from kenyaemr_etl.etl_prep_enrolment e\n" +
                "  left outer join kenyaemr_etl.etl_prep_followup pf  on e.patient_id = pf.patient_id\n" +
                "  left outer join kenyaemr_etl.etl_hts_test t on e.patient_id = t.patient_id\n" +
                "where t.key_population_type = 'Female sex Worker' and pf.treatment_plan = 'Restart'\n" +
                " and date(e.visit_date) between date(:startDate) and date(:endDate);";
        cd.setName("restartOnPreEPFSW");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("FSW restart on PrEP");

        return cd;
    }

    public CohortDefinition restartOnPreEPPWID(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select distinct e.patient_id\n" +
                "from kenyaemr_etl.etl_prep_enrolment e\n" +
                "  left outer join kenyaemr_etl.etl_prep_followup pf  on e.patient_id = pf.patient_id\n" +
                "  left outer join kenyaemr_etl.etl_hts_test t on e.patient_id = t.patient_id\n" +
                "where t.key_population_type = 'People who inject drugs' and pf.treatment_plan = 'Restart'\n" +
                "      and date(e.visit_date) between date(:startDate) and date(:endDate);";
        cd.setName("restartOnPreEPPWID");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("PWID restart on PrEP");

        return cd;
    }

    public CohortDefinition restartOnPreEPDiscordant(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select distinct e.patient_id\n" +
                "from kenyaemr_etl.etl_prep_enrolment e\n" +
                "  left outer join kenyaemr_etl.etl_prep_followup pf  on e.patient_id = pf.patient_id\n" +
                "  left outer join kenyaemr_etl.etl_hts_test t on e.patient_id = t.patient_id\n" +
                "where t.couple_discordant = 'Yes' and pf.treatment_plan = 'Restart'\n" +
                " and date(e.visit_date) between date(:startDate) and date(:endDate);";
        cd.setName("restartOnPreEPDiscordant");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("Discordant restart on PrEP");

        return cd;
    }

    public CohortDefinition currentOnPreEPGP(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select distinct e.patient_id\n" +
                "from kenyaemr_etl.etl_prep_enrolment e\n" +
                "  left outer join kenyaemr_etl.etl_prep_followup pf  on e.patient_id = pf.patient_id\n" +
                "  left outer join kenyaemr_etl.etl_prep_monthly_refill mr  on e.patient_id = mr.patient_id\n" +
                "  left outer join kenyaemr_etl.etl_hts_test t on e.patient_id = t.patient_id\n" +
                "where t.population_type = 'General Population' and (pf.treatment_plan in ('Start','Restart','Continue') or mr.prep_status = 'Continue')\n" +
                "      and date(e.visit_date) between date(:startDate) and date(:endDate);";
        cd.setName("currentOnPreEPGP");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("GP current on PrEP");

        return cd;
    }

    public CohortDefinition currentOnPreEPMSM(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select distinct e.patient_id\n" +
                "from kenyaemr_etl.etl_prep_enrolment e\n" +
                "  left outer join kenyaemr_etl.etl_prep_followup pf  on e.patient_id = pf.patient_id\n" +
                "  left outer join kenyaemr_etl.etl_prep_monthly_refill mr  on e.patient_id = mr.patient_id\n" +
                "  left outer join kenyaemr_etl.etl_hts_test t on e.patient_id = t.patient_id\n" +
                "where t.key_population_type = 'Men who have sex with men' and (pf.treatment_plan in ('Start','Restart','Continue') or mr.prep_status = 'Continue')\n" +
                "      and date(e.visit_date) between date(:startDate) and date(:endDate);";
        cd.setName("currentOnPreEPMSM");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("MSM current on PrEP");

        return cd;
    }

    public CohortDefinition currentOnPreEPFSW(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select distinct e.patient_id\n" +
                "from kenyaemr_etl.etl_prep_enrolment e\n" +
                "  left outer join kenyaemr_etl.etl_prep_followup pf  on e.patient_id = pf.patient_id\n" +
                "  left outer join kenyaemr_etl.etl_prep_monthly_refill mr  on e.patient_id = mr.patient_id\n" +
                "  left outer join kenyaemr_etl.etl_hts_test t on e.patient_id = t.patient_id\n" +
                "where t.key_population_type = 'Female sex Worker' and (pf.treatment_plan in ('Start','Restart','Continue') or mr.prep_status = 'Continue')\n" +
                "      and date(e.visit_date) between date(:startDate) and date(:endDate);";
        cd.setName("currentOnPreEPFSW");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("FSW current on PrEP");

        return cd;
    }

    public CohortDefinition currentOnPreEPPWID(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select distinct e.patient_id\n" +
                "from kenyaemr_etl.etl_prep_enrolment e\n" +
                "  left outer join kenyaemr_etl.etl_prep_followup pf  on e.patient_id = pf.patient_id\n" +
                "  left outer join kenyaemr_etl.etl_prep_monthly_refill mr  on e.patient_id = mr.patient_id\n" +
                "  left outer join kenyaemr_etl.etl_hts_test t on e.patient_id = t.patient_id\n" +
                "where t.key_population_type = 'People who inject drugs' and (pf.treatment_plan in ('Start','Restart','Continue') or mr.prep_status = 'Continue')\n" +
                " and date(e.visit_date) between date(:startDate) and date(:endDate);";
        cd.setName("currentOnPreEPPWID");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("PWID current on PrEP");

        return cd;
    }

    public CohortDefinition currentOnPreEPDiscordant(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select distinct e.patient_id\n" +
                "from kenyaemr_etl.etl_prep_enrolment e\n" +
                "  left outer join kenyaemr_etl.etl_prep_followup pf  on e.patient_id = pf.patient_id\n" +
                "  left outer join kenyaemr_etl.etl_prep_monthly_refill mr  on e.patient_id = mr.patient_id\n" +
                "  left outer join kenyaemr_etl.etl_hts_test t on e.patient_id = t.patient_id\n" +
                "where t.couple_discordant = 'Yes' and (pf.treatment_plan in ('Start','Restart','Continue') or mr.prep_status = 'Continue')\n" +
                "      and date(e.visit_date) between date(:startDate) and date(:endDate);";
        cd.setName("currentOnPreEPDiscordant");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("Discordant current on PrEP");

        return cd;
    }

    public CohortDefinition positiveOnPreEPGP(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select distinct e.patient_id\n" +
                "from kenyaemr_etl.etl_prep_enrolment e\n" +
                "  left outer join kenyaemr_etl.etl_hts_test t on e.patient_id = t.patient_id\n" +
                "where t.population_type = 'General Population'\n" +
                "and t.final_test_result = 'Positive'\n" +
                "and date(e.visit_date) between date(:startDate) and date(:endDate)\n" +
                "and t.visit_date >= e.visit_date ;";
        cd.setName("positiveOnPreEPGP");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("GP positive on PrEP");

        return cd;
    }

    public CohortDefinition positiveOnPreEPMSM(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select distinct e.patient_id\n" +
                "from kenyaemr_etl.etl_prep_enrolment e\n" +
                "  left outer join kenyaemr_etl.etl_hts_test t on e.patient_id = t.patient_id\n" +
                "where t.population_type = 'Men who have sex with men'\n" +
                "      and t.final_test_result = 'Positive'\n" +
                "      and date(e.visit_date) between date(:startDate) and date(:endDate)\n" +
                "      and t.visit_date >= e.visit_date ;";
        cd.setName("positiveOnPreEPMSM");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("MSM positive on PrEP");

        return cd;
    }

    public CohortDefinition positiveOnPreEPFSW(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select distinct e.patient_id\n" +
                "from kenyaemr_etl.etl_prep_enrolment e\n" +
                "  left outer join kenyaemr_etl.etl_hts_test t on e.patient_id = t.patient_id\n" +
                "where t.population_type = 'Female sex Worker'\n" +
                "      and t.final_test_result = 'Positive'\n" +
                "      and date(e.visit_date) between date(:startDate) and date(:endDate)\n" +
                "      and t.visit_date >= e.visit_date ;";
        cd.setName("positiveOnPreEPFSW");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("FSW positive on PrEP");

        return cd;
    }

    public CohortDefinition positiveOnPreEPPWID(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select distinct e.patient_id\n" +
                "from kenyaemr_etl.etl_prep_enrolment e\n" +
                "  left outer join kenyaemr_etl.etl_hts_test t on e.patient_id = t.patient_id\n" +
                "where t.population_type = 'People who inject drugs'\n" +
                "      and t.final_test_result = 'Positive'\n" +
                "      and date(e.visit_date) between date(:startDate) and date(:endDate)\n" +
                "      and t.visit_date >= e.visit_date ;";
        cd.setName("positiveOnPreEPPWID");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("PWID positive on PrEP");

        return cd;
    }

    public CohortDefinition positiveOnPreEPDiscordant(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select distinct e.patient_id\n" +
                "from kenyaemr_etl.etl_prep_enrolment e\n" +
                "  left outer join kenyaemr_etl.etl_hts_test t on e.patient_id = t.patient_id\n" +
                "where t.couple_discordant = 'Yes'\n" +
                "      and t.final_test_result = 'Positive'\n" +
                "      and date(e.visit_date) between date(:startDate) and date(:endDate)\n" +
                "      and t.visit_date >= e.visit_date ;";
        cd.setName("positiveOnPreEPDiscordant");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("Discordant positive on PrEP");

        return cd;
    }

}
