/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.prep;

/**
 * KenyaEMR specific constants
 */
public class EmrConstants {
	
	/**
	 * Module ID
	 */
	public static final String MODULE_ID = "prep";
	
	/**
	 * Application IDs
	 */
	public static final String APP_PREP = MODULE_ID + ".prep";
	
	/**
	 * Global property names
	 */
	public static final String GP_DEFAULT_LOCATION = MODULE_ID + ".defaultLocation";
	
	public static final String GP_CONTROLLER_WHITELIST = MODULE_ID + ".controllerWhitelist";
	
	public static final String GP_SUPPORT_PHONE_NUMBER = MODULE_ID + ".supportPhoneNumber";
	
	public static final String GP_SUPPORT_EMAIL_ADDRESS = MODULE_ID + ".supportEmailAddress";
	
	public static final String GP_EXTERNAL_HELP_URL = MODULE_ID + ".externalHelpUrl";
	
	public static final String GP_DHIS2_DATASET_MAPPING = MODULE_ID + ".adxDatasetMapping";
	
	/**
	 * Default global property values
	 */
	public static final String DEFAULT_SUPPORT_PHONE_NUMBER = "0800720701";
	
	public static final String DEFAULT_SUPPORT_EMAIL_ADDRESS = "help@kenyaemr.org";
	
	public static final String DEFAULT_EXTERNAL_HELP_URL = "/help";
}
