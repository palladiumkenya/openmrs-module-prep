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
import static org.openmrs.module.kenyaemr.reporting.EmrReportingUtils.cohortIndicator;

/**
 * Created by dev on 22/08/19.
 */

/**
 * Library of Prep related indicator definitions. All indicators require parameters ${startDate} and
 * ${endDate}
 */
@Component
public class ETLMoh731BIndicatorLibrary {
	
	@Autowired
	private ETLMoh731BCohortLibrary moh731BCohorts;
	
	// 1.Number Eligible for PrEP
	
	/**
	 * Number GP Eligible for PrEP covers indicators HV01
	 * 
	 * @return indicator
	 */
	public CohortIndicator eligibleForPrEPGP() {
		return cohortIndicator("GP eligible for PrEP",
		    map(moh731BCohorts.eligibleForPrEPGP(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number MSM Eligible for PrEP covers indicators HV01- - HV01-
	 * 
	 * @return indicator
	 */
	public CohortIndicator eligibleForPrEPMSM() {
		return cohortIndicator("MSM Individuals eligible for PrEP",
		    map(moh731BCohorts.eligibleForPrEPMSM(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number FSW Eligible for PrEP covers indicators HV01- - HV01-
	 * 
	 * @return indicator
	 */
	public CohortIndicator eligibleForPrEPFSW() {
		return cohortIndicator("FSW Individuals eligible for PrEP",
		    map(moh731BCohorts.eligibleForPrEPFSW(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number PWID Eligible for PrEP covers indicators HV01- - HV01-
	 * 
	 * @return indicator
	 */
	public CohortIndicator eligibleForPrEPPWID() {
		return cohortIndicator("PWID Individuals eligible for PrEP",
		    map(moh731BCohorts.eligibleForPrEPPWID(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number Discordant Eligible for PrEP covers indicators HV01- - HV01-
	 * 
	 * @return indicator
	 */
	public CohortIndicator eligibleForPrEPDiscordant() {
		return cohortIndicator("Discordant Individuals eligible for PrEP",
		    map(moh731BCohorts.eligibleForPrEPDiscordant(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number Eligible for PrEP
	 * 
	 * @return indicator
	 */
	public CohortIndicator eligibleForPrEP() {
		return cohortIndicator("Discordant Individuals eligible for PrEP",
		    map(moh731BCohorts.eligibleForPrEP(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	// 2.Number Newly on PrEP
	/**
	 * Number GP Newly on PrEP HV02
	 * 
	 * @return indicator
	 */
	public CohortIndicator newlyOnPrePGP() {
		return cohortIndicator("GP Individuals newly on PrEP",
		    map(moh731BCohorts.newlyOnPrEPGP(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number MSM Newly for PrEP covers indicators HV02
	 * 
	 * @return indicator
	 */
	public CohortIndicator newOnPrEPMSM() {
		return cohortIndicator("MSM Individuals newly on PrEP",
		    map(moh731BCohorts.newlyOnPrEPMSM(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number FSW Newly on PrEP covers indicators HV02
	 * 
	 * @return indicator
	 */
	public CohortIndicator newOnPrEPFSW() {
		return cohortIndicator("FSW Individuals newly on PrEP",
		    map(moh731BCohorts.newlyOnPrEPFSW(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number PWID Newly on PrEP covers indicators HV02
	 * 
	 * @return indicator
	 */
	public CohortIndicator newOnPrEPPWID() {
		return cohortIndicator("PWID Individuals newly on PrEP",
		    map(moh731BCohorts.newlyOnPrEPPWID(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number Discordant newly on PrEP covers indicators HV02
	 * 
	 * @return indicator
	 */
	public CohortIndicator newOnPrEPDiscordant() {
		return cohortIndicator("Discordant Individuals Newly on PrEP",
		    map(moh731BCohorts.newlyOnPrEPDiscordant(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Newly started on PrEP
	 */
	public CohortIndicator newOnPrEP() {
		return cohortIndicator("Individuals Newly on PrEP",
		    map(moh731BCohorts.newOnPrEP(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	// 3.Number Refilling on PrEP
	/**
	 * Number GP Refill on PrEP HV03
	 * 
	 * @return indicator
	 */
	public CohortIndicator refillOnPrEPGP() {
		return cohortIndicator("GP Individuals refill on PrEP",
		    map(moh731BCohorts.refillOnPrEPGP(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number MSM Refill on PrEP covers indicators HV03
	 * 
	 * @return indicator
	 */
	public CohortIndicator refillingPrEPMSM() {
		return cohortIndicator("MSM Individuals refill on PrEP",
		    map(moh731BCohorts.refillOnPrEPMSM(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number FSW Refill on PrEP covers indicators HV03
	 * 
	 * @return indicator
	 */
	public CohortIndicator refillingPrEPFSW() {
		return cohortIndicator("FSW Individuals refill on PrEP",
		    map(moh731BCohorts.refillOnPrEPFSW(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number PWID Refill on PrEP covers indicators HV03
	 * 
	 * @return indicator
	 */
	public CohortIndicator refillingPrEPPWID() {
		return cohortIndicator("PWID Individuals refill on PrEP",
		    map(moh731BCohorts.refillOnPrEPPWID(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number Discordant Refill on PrEP covers indicators HV03
	 * 
	 * @return indicator
	 */
	public CohortIndicator refillingPrEPDiscordant() {
		return cohortIndicator("Discordant Individuals Refill on PrEP",
		    map(moh731BCohorts.refillOnPrEPDiscordant(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Refill PrEP within the reporting period
	 */
	public CohortIndicator refillOnPrEPTotal() {
		return cohortIndicator("Individuals Refilling PrEP",
		    map(moh731BCohorts.refillOnPrEPTotal(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	// 4.Number Restart on PrEP
	/**
	 * Number GP Restart on PrEP HV04
	 * 
	 * @return indicator
	 */
	public CohortIndicator restartingPrEPGP() {
		return cohortIndicator("GP Individuals restart on PrEP",
		    map(moh731BCohorts.restartOnPrEPGP(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	public CohortIndicator newRefillRestartOnPrEP() {
		return cohortIndicator("On PrEP",
		    map(moh731BCohorts.newRefillRestartOnPrEP(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number MSM Restart on PrEP covers indicators HV04
	 * 
	 * @return indicator
	 */
	public CohortIndicator restartingPrEPMSM() {
		return cohortIndicator("MSM Individuals restart on PrEP",
		    map(moh731BCohorts.restartOnPrEPMSM(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number FSW Restart on PrEP covers indicators HV04
	 * 
	 * @return indicator
	 */
	public CohortIndicator restartingPrEPFSW() {
		return cohortIndicator("FSW Individuals restart on PrEP",
		    map(moh731BCohorts.restartOnPrEPFSW(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number PWID Restart on PrEP covers indicators HV04
	 * 
	 * @return indicator
	 */
	public CohortIndicator restartingPrEPPWID() {
		return cohortIndicator("PWID Individuals restart on PrEP",
		    map(moh731BCohorts.restartOnPrEPPWID(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number Discordant Restart on PrEP covers indicators HV04
	 * 
	 * @return indicator
	 */
	public CohortIndicator restartingPrEPDiscordant() {
		return cohortIndicator("Discordant Individuals Restart on PrEP",
		    map(moh731BCohorts.restartOnPreEPDiscordant(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	public CohortIndicator restartingPrEP() {
		return cohortIndicator("Individuals Restart PrEP",
		    map(moh731BCohorts.restartingPrEP(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Total restarting PrEP
	 * 
	 * @return
	 */
	public CohortIndicator restartingPrEPTotal() {
		return cohortIndicator("Individuals Restart PrEP",
		    map(moh731BCohorts.restartingPrEPTotal(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	// 5.Number Current on PrEP
	/**
	 * Number GP Current on PrEP HV05
	 * 
	 * @return indicator
	 */
	public CohortIndicator currentlyOnPrEPGP() {
		return cohortIndicator("GP Individuals current on PrEP",
		    map(moh731BCohorts.currentOnPrEPGP(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number MSM Current on PrEP covers indicators HV05
	 * 
	 * @return indicator
	 */
	public CohortIndicator currentlyOnPrEPMSM() {
		return cohortIndicator("MSM Individuals current on PrEP",
		    map(moh731BCohorts.currentOnPrEPMSM(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number FSW Current on PrEP covers indicators HV05
	 * 
	 * @return indicator
	 */
	public CohortIndicator currentlyOnPrEPFSW() {
		return cohortIndicator("FSW Individuals current on PrEP",
		    map(moh731BCohorts.currentOnPrEPFSW(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number PWID Current on PrEP covers indicators HV05
	 * 
	 * @return indicator
	 */
	public CohortIndicator currentlyOnPrEPPWID() {
		return cohortIndicator("PWID Individuals current on PrEP",
		    map(moh731BCohorts.currentOnPrEPPWID(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number Discordant Current on PrEP covers indicators HV05
	 * 
	 * @return indicator
	 */
	public CohortIndicator currentlyOnPrEPDiscordant() {
		return cohortIndicator("Discordant Individuals Current on PrEP",
		    map(moh731BCohorts.currentOnPrEPDiscordant(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	public CohortIndicator currentOnPrEPTotal() {
		return cohortIndicator("All Individuals Current on PrEP",
		    map(moh731BCohorts.currentOnPrEPTotal(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	// 6.Number Positive while on PrEP
	/**
	 * Number GP Positive while on PrEP HV06
	 * 
	 * @return indicator
	 */
	public CohortIndicator seroconvertedOnPrEPGP() {
		return cohortIndicator("GP Individuals positive on PrEP",
		    map(moh731BCohorts.positiveOnPrEPGP(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number MSM Positive on PrEP covers indicators HV06
	 * 
	 * @return indicator
	 */
	public CohortIndicator seroconvertedOnPrEPMSM() {
		return cohortIndicator("MSM Individuals Positive on PrEP",
		    map(moh731BCohorts.positiveOnPrEPMSM(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number FSW Positive on PrEP covers indicators HV06
	 * 
	 * @return indicator
	 */
	public CohortIndicator seroconvertedOnPrEPFSW() {
		return cohortIndicator("FSW Individuals Positive on PrEP",
		    map(moh731BCohorts.positiveOnPrEPFSW(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number PWID Positive on PrEP covers indicators HV06
	 * 
	 * @return indicator
	 */
	public CohortIndicator seroconvertedOnPrEPPWID() {
		return cohortIndicator("PWID Individuals Positive on PrEP",
		    map(moh731BCohorts.positiveOnPrEPPWID(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number Discordant Positive on PrEP covers indicators HV06
	 * 
	 * @return indicator
	 */
	public CohortIndicator seroconvertedOnPrEPDiscordant() {
		return cohortIndicator("Discordant Individuals Positive on PrEP",
		    map(moh731BCohorts.positiveOnPrEPDiscordant(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number sero-converted while on PrEP
	 * 
	 * @return indicator
	 */
	public CohortIndicator seroConvertedOnPrEP() {
		return cohortIndicator("Individuals Positive on PrEP",
		    map(moh731BCohorts.positiveOnPrEP(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	// 7.Number STI Diagnosed while on PrEP
	/**
	 * Number GP STI Diagnosed while on PrEP HV07
	 * 
	 * @return indicator
	 */
	public CohortIndicator diagnosedWithSTIGP() {
		return cohortIndicator("GP Individuals STI disgnosed on PrEP",
		    map(moh731BCohorts.stiDiagnosedOnPrEPGP(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number MSM STI disgnosed on PrEP covers indicators HV07
	 * 
	 * @return indicator
	 */
	public CohortIndicator diagnosedWithSTIMSM() {
		return cohortIndicator("MSM Individuals STI disgnosed on PrEP",
		    map(moh731BCohorts.stiDiagnosedOnPrEPMSM(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number FSW STI disgnosed on PrEP covers indicators HV07
	 * 
	 * @return indicator
	 */
	public CohortIndicator diagnosedWithSTIFSW() {
		return cohortIndicator("FSW Individuals STI disgnosed on PrEP",
		    map(moh731BCohorts.stiDiagnosedOnPrEPFSW(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number PWID STI disgnosed on PrEP covers indicators HV07
	 * 
	 * @return indicator
	 */
	public CohortIndicator diagnosedWithSTIPWID() {
		return cohortIndicator("PWID Individuals STI disgnosed on PrEP",
		    map(moh731BCohorts.stiDiagnosedOnPrEPPWID(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number Discordant STI disgnosed on PrEP covers indicators HV07
	 * 
	 * @return indicator
	 */
	public CohortIndicator diagnosedWithSTIDiscordant() {
		return cohortIndicator("Discordant Individuals STI disgnosed on PrEP",
		    map(moh731BCohorts.stiDiagnosedOnPrEPDiscordant(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	// 8.Number Discontinued on PrEP
	/**
	 * Number GP Discontinued PrEP HV08
	 * 
	 * @return indicator
	 */
	public CohortIndicator discontinuedPrEPGP() {
		return cohortIndicator("GP Individuals Discontinued PrEP",
		    map(moh731BCohorts.discontinuedPrEPGP(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number MSM STI disgnosed on PrEP covers indicators HV08
	 * 
	 * @return indicator
	 */
	public CohortIndicator discontinuedPrEPMSM() {
		return cohortIndicator("MSM Individuals Discontinued PrEP",
		    map(moh731BCohorts.discontinuedPrEPMSM(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number FSW Discontinued PrEP covers indicators HV08
	 * 
	 * @return indicator
	 */
	public CohortIndicator discontinuedPrEPFSW() {
		return cohortIndicator("FSW Individuals Discontinued PrEP",
		    map(moh731BCohorts.discontinuedPrEPFSW(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number PWID Discontinued PrEP covers indicators HV08
	 * 
	 * @return indicator
	 */
	public CohortIndicator discontinuedPrEPPWID() {
		return cohortIndicator("PWID Individuals Discontinued PrEP",
		    map(moh731BCohorts.discontinuedPrEPPWID(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number Discordant Discontinued PrEP covers indicators HV08
	 * 
	 * @return indicator
	 */
	public CohortIndicator discontinuedPrEPDiscordant() {
		return cohortIndicator("Discordant Individuals Discontinued PrEP",
		    map(moh731BCohorts.discontinuedPrEPDiscordant(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number assessed for HIV risk
	 * 
	 * @return indicator
	 */
	public CohortIndicator assessedForHIVRisk() {
		return cohortIndicator("Number assessed for HIV risk",
		    map(moh731BCohorts.assessedForHIVRisk(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number Initiated on PrEP
	 * 
	 * @return indicator
	 */
	public CohortIndicator initiatedOnPrEP() {
		return cohortIndicator("Number initiated on PrEP",
		    map(moh731BCohorts.initiatedOnPrEP(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number continuing PrEP
	 * 
	 * @return indicator
	 */
	public CohortIndicator continuingOnPrEP() {
		return cohortIndicator("Number continuing PrEP",
		    map(moh731BCohorts.continuingOnPrEP(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number current on PrEP
	 * 
	 * @return indicator
	 */
	public CohortIndicator currentOnPrEP() {
		return cohortIndicator("Number current on PrEP",
		    map(moh731BCohorts.currentOnPrEP(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number retested HIV Positive while on PrEP
	 * 
	 * @return indicator
	 */
	public CohortIndicator retestedPositiveOnPrEP() {
		return cohortIndicator("Number retested HIV+ while on PrEP",
		    map(moh731BCohorts.retestedPositiveOnPrEP(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number diagnosed with STI
	 * 
	 * @return indicator
	 */
	public CohortIndicator diagnosedWithSTI() {
		return cohortIndicator("Diagnosed with STI",
		    map(moh731BCohorts.diagnosedWithSTI(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number Discontinued PrEP
	 * 
	 * @return indicator
	 */
	public CohortIndicator discontinuedPrEP() {
		return cohortIndicator("Discontinued PrEP",
		    map(moh731BCohorts.discontinuedPrEP(), "startDate=${startDate},endDate=${endDate}"));
	}
}
