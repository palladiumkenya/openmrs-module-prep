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

import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.openmrs.module.kenyacore.report.ReportUtils.map;
import static org.openmrs.module.prep.reporting.EmrReportingUtils.cohortIndicator;

/**
 * Created by dev on 22/08/19.
 */

/**
 * Library of Prep related indicator definitions. All indicators require parameters ${startDate} and ${endDate}
 */
@Component
public class ETLMoh731BIndicatorLibrary {
    @Autowired
    private ETLMoh731BCohortLibrary moh731BCohorts;

    // 1.Number Eligible for PrEP

    /**
     * Number GP Eligible for PrEP
     * covers indicators HV01- - HV01-
     *
     * @return indicator
     */
    public CohortIndicator eligibleForPreEPGP() {
        return cohortIndicator("GP Individuals eligible for PrEP", map(moh731BCohorts.eligibleForPreEPGP(), "startDate=${startDate},endDate=${endDate}"));
    }
    /**
     * Number MSM Eligible for PrEP
     * covers indicators HV01- - HV01-
     *
     * @return indicator
     */
    public CohortIndicator eligibleForPreEPMSM() {
        return cohortIndicator("MSM Individuals eligible for PrEP", map(moh731BCohorts.eligibleForPreEPMSM(), "startDate=${startDate},endDate=${endDate}"));
    }

    /**
     * Number FSW Eligible for PrEP
     * covers indicators HV01- - HV01-
     *
     * @return indicator
     */
    public CohortIndicator eligibleForPreEPFSW() {
        return cohortIndicator("FSW Individuals eligible for PrEP", map(moh731BCohorts.eligibleForPreEPFSW(), "startDate=${startDate},endDate=${endDate}"));
    }

    /**
     * Number PWID Eligible for PrEP
     * covers indicators HV01- - HV01-
     *
     * @return indicator
     */
    public CohortIndicator eligibleForPreEPPWID() {
        return cohortIndicator("PWID Individuals eligible for PrEP", map(moh731BCohorts.eligibleForPreEPPWID(), "startDate=${startDate},endDate=${endDate}"));
    }

    /**
     * Number Discordant Eligible for PrEP
     * covers indicators HV01- - HV01-
     *
     * @return indicator
     */
    public CohortIndicator eligibleForPreEPDiscordant() {
        return cohortIndicator("Discordant Individuals eligible for PrEP", map(moh731BCohorts.eligibleForPreEPDiscordant(), "startDate=${startDate},endDate=${endDate}"));
    }

    // 2.Number Newly on PrEP
    /**
     * Number GP Newly on PrEP
     *
     * @return indicator
     */
    public CohortIndicator newlyOnPreEPGP() {
        return cohortIndicator("GP Individuals newly on PrEP", map(moh731BCohorts.newlyOnPreEPGP(), "startDate=${startDate},endDate=${endDate}"));
    }
    /**
     * Number MSM Newly for PrEP
     * covers indicators HV01- - HV01-
     *
     * @return indicator
     */
    public CohortIndicator newlyOnPreEPMSM() {
        return cohortIndicator("MSM Individuals newly on PrEP", map(moh731BCohorts.newlyOnPreEPMSM(), "startDate=${startDate},endDate=${endDate}"));
    }

    /**
     * Number FSW Newly on PrEP
     * covers indicators HV01- - HV01-
     *
     * @return indicator
     */
    public CohortIndicator newlyOnPreEPFSW() {
        return cohortIndicator("FSW Individuals newly on PrEP", map(moh731BCohorts.newlyOnPreEPFSW(), "startDate=${startDate},endDate=${endDate}"));
    }

    /**
     * Number PWID Newly on PrEP
     * covers indicators HV01- - HV01-
     *
     * @return indicator
     */
    public CohortIndicator newlyOnPreEPPWID() {
        return cohortIndicator("PWID Individuals newly on PrEP", map(moh731BCohorts.newlyOnPreEPPWID(), "startDate=${startDate},endDate=${endDate}"));
    }

    /**
     * Number Discordant newly on PrEP
     * covers indicators HV01- - HV01-
     *
     * @return indicator
     */
    public CohortIndicator newlyOnPreEPDiscordant() {
        return cohortIndicator("Discordant Individuals Newly on PrEP", map(moh731BCohorts.newlyOnPreEPDiscordant(), "startDate=${startDate},endDate=${endDate}"));
    }

    // 3.Number Refilling on PrEP
    /**
     * Number GP Refill on PrEP
     *
     * @return indicator
     */
    public CohortIndicator refillOnPreEPGP() {
        return cohortIndicator("GP Individuals refill on PrEP", map(moh731BCohorts.refillOnPreEPGP(), "startDate=${startDate},endDate=${endDate}"));
    }
    /**
     * Number MSM Refill on PrEP
     * covers indicators HV01- - HV01-
     *
     * @return indicator
     */
    public CohortIndicator refillOnPreEPMSM() {
        return cohortIndicator("MSM Individuals refill on PrEP", map(moh731BCohorts.refillOnPreEPMSM(), "startDate=${startDate},endDate=${endDate}"));
    }

    /**
     * Number FSW Refill on PrEP
     * covers indicators HV01- - HV01-
     *
     * @return indicator
     */
    public CohortIndicator refillOnPreEPFSW() {
        return cohortIndicator("FSW Individuals refill on PrEP", map(moh731BCohorts.refillOnPreEPFSW(), "startDate=${startDate},endDate=${endDate}"));
    }

    /**
     * Number PWID Refill on PrEP
     * covers indicators HV01- - HV01-
     *
     * @return indicator
     */
    public CohortIndicator refillOnPreEPPWID() {
        return cohortIndicator("PWID Individuals refill on PrEP", map(moh731BCohorts.refillOnPreEPPWID(), "startDate=${startDate},endDate=${endDate}"));
    }

    /**
     * Number Discordant Refill on PrEP
     * covers indicators HV01- - HV01-
     *
     * @return indicator
     */
    public CohortIndicator refillOnPreEPDiscordant() {
        return cohortIndicator("Discordant Individuals Refill on PrEP", map(moh731BCohorts.refillOnPreEPDiscordant(), "startDate=${startDate},endDate=${endDate}"));
    }

    // 4.Number Restart on PrEP
    /**
     * Number GP Restart on PrEP
     *
     * @return indicator
     */
    public CohortIndicator restartOnPreEPGP() {
        return cohortIndicator("GP Individuals restart on PrEP", map(moh731BCohorts.restartOnPreEPGP(), "startDate=${startDate},endDate=${endDate}"));
    }
    /**
     * Number MSM Restart on PrEP
     * covers indicators HV01- - HV01-
     *
     * @return indicator
     */
    public CohortIndicator restartOnPreEPMSM() {
        return cohortIndicator("MSM Individuals restart on PrEP", map(moh731BCohorts.restartOnPreEPMSM(), "startDate=${startDate},endDate=${endDate}"));
    }

    /**
     * Number FSW Restart on PrEP
     * covers indicators HV01- - HV01-
     *
     * @return indicator
     */
    public CohortIndicator restartOnPreEPFSW() {
        return cohortIndicator("FSW Individuals restart on PrEP", map(moh731BCohorts.restartOnPreEPFSW(), "startDate=${startDate},endDate=${endDate}"));
    }

    /**
     * Number PWID Restart on PrEP
     * covers indicators HV01- - HV01-
     *
     * @return indicator
     */
    public CohortIndicator restartOnPreEPPWID() {
        return cohortIndicator("PWID Individuals restart on PrEP", map(moh731BCohorts.restartOnPreEPPWID(), "startDate=${startDate},endDate=${endDate}"));
    }

    /**
     * Number Discordant Restart on PrEP
     * covers indicators HV01- - HV01-
     *
     * @return indicator
     */
    public CohortIndicator restartOnPreEPDiscordant() {
        return cohortIndicator("Discordant Individuals Restart on PrEP", map(moh731BCohorts.restartOnPreEPDiscordant(), "startDate=${startDate},endDate=${endDate}"));
    }
    // 5.Number Current on PrEP
    /**
     * Number GP Current on PrEP
     *
     * @return indicator
     */
    public CohortIndicator currentOnPreEPGP() {
        return cohortIndicator("GP Individuals current on PrEP", map(moh731BCohorts.currentOnPreEPGP(), "startDate=${startDate},endDate=${endDate}"));
    }
    /**
     * Number MSM Current on PrEP
     * covers indicators HV01- - HV01-
     *
     * @return indicator
     */
    public CohortIndicator currentOnPreEPMSM() {
        return cohortIndicator("MSM Individuals current on PrEP", map(moh731BCohorts.currentOnPreEPMSM(), "startDate=${startDate},endDate=${endDate}"));
    }

    /**
     * Number FSW Current on PrEP
     * covers indicators HV01- - HV01-
     *
     * @return indicator
     */
    public CohortIndicator currentOnPreEPFSW() {
        return cohortIndicator("FSW Individuals current on PrEP", map(moh731BCohorts.currentOnPreEPFSW(), "startDate=${startDate},endDate=${endDate}"));
    }

    /**
     * Number PWID Current on PrEP
     * covers indicators HV01- - HV01-
     *
     * @return indicator
     */
    public CohortIndicator currentOnPreEPPWID() {
        return cohortIndicator("PWID Individuals current on PrEP", map(moh731BCohorts.currentOnPreEPPWID(), "startDate=${startDate},endDate=${endDate}"));
    }

    /**
     * Number Discordant Current on PrEP
     * covers indicators HV01- - HV01-
     *
     * @return indicator
     */
    public CohortIndicator currentOnPreEPDiscordant() {
        return cohortIndicator("Discordant Individuals Current on PrEP", map(moh731BCohorts.currentOnPreEPDiscordant(), "startDate=${startDate},endDate=${endDate}"));
    }

    // 6.Number Positive while on PrEP
    /**
     * Number GP Positive while on PrEP
     *
     * @return indicator
     */
    public CohortIndicator positiveOnPreEPGP() {
        return cohortIndicator("GP Individuals positive on PrEP", map(moh731BCohorts.positiveOnPreEPGP(), "startDate=${startDate},endDate=${endDate}"));
    }
    /**
     * Number MSM Positive on PrEP
     * covers indicators HV01- - HV01-
     *
     * @return indicator
     */
    public CohortIndicator positiveOnPreEPMSM() {
        return cohortIndicator("MSM Individuals Positive on PrEP", map(moh731BCohorts.positiveOnPreEPMSM(), "startDate=${startDate},endDate=${endDate}"));
    }

    /**
     * Number FSW Positive on PrEP
     * covers indicators HV01- - HV01-
     *
     * @return indicator
     */
    public CohortIndicator positiveOnPreEPFSW() {
        return cohortIndicator("FSW Individuals Positive on PrEP", map(moh731BCohorts.positiveOnPreEPFSW(), "startDate=${startDate},endDate=${endDate}"));
    }

    /**
     * Number PWID Positive on PrEP
     * covers indicators HV01- - HV01-
     *
     * @return indicator
     */
    public CohortIndicator positiveOnPreEPPWID() {
        return cohortIndicator("PWID Individuals Positive on PrEP", map(moh731BCohorts.positiveOnPreEPPWID(), "startDate=${startDate},endDate=${endDate}"));
    }

    /**
     * Number Discordant Positive on PrEP
     * covers indicators HV01- - HV01-
     *
     * @return indicator
     */
    public CohortIndicator positiveOnPreEPDiscordant() {
        return cohortIndicator("Discordant Individuals Positive on PrEP", map(moh731BCohorts.positiveOnPreEPDiscordant(), "startDate=${startDate},endDate=${endDate}"));
    }
}


