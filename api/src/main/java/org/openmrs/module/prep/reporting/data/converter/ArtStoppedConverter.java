/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.prep.reporting.data.converter;

import org.openmrs.api.context.Context;
import org.openmrs.calculation.result.CalculationResult;
import org.openmrs.module.kenyaui.KenyaUiUtils;
import org.openmrs.module.reporting.data.converter.DataConverter;

import java.util.Calendar;
import java.util.Date;

/**
 * Converts the status and date when patients stopped ART.
 */
public class ArtStoppedConverter implements DataConverter {
	
	@Override
	public Object convert(Object obj) {
		KenyaUiUtils kenyaui = Context.getRegisteredComponents(KenyaUiUtils.class).get(0);
		
		if (obj == null) {
			return "";
		}
		
		Object value = ((CalculationResult) obj).getValue();
		if (value != null) {
			return kenyaui.formatDate(stringToDate(value.toString()));
		}
		return null;
	}
	
	@Override
	public Class<?> getInputDataType() {
		return CalculationResult.class;
	}
	
	@Override
	public Class<?> getDataType() {
		return String.class;
	}
	
	private Date stringToDate(String date) {
		String datePart = date.split(" ")[0];
		String[] dateParts = datePart.split("-");
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Integer.parseInt(dateParts[0]), (Integer.parseInt(dateParts[1]) - 1), Integer.parseInt(dateParts[2]));
		Date dateRequired = calendar.getTime();
		return dateRequired;
	}
}