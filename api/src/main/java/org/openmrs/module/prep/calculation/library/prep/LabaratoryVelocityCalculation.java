/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.prep.calculation.library.prep;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.Order;
import org.openmrs.OrderType;
import org.openmrs.api.OrderService;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.kenyacore.calculation.Calculations;
import org.openmrs.module.kenyaemr.calculation.BaseEmrCalculation;

import java.text.SimpleDateFormat;
import java.util.*;

public class LabaratoryVelocityCalculation extends BaseEmrCalculation {
	
	public static final Locale LOCALE = Locale.ENGLISH;
	
	protected static final Log log = LogFactory.getLog(LabaratoryVelocityCalculation.class);
	
	static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy");
	
	@Override
	public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> parameterValues,
	        PatientCalculationContext context) {
		PatientService patientService = Context.getPatientService();
		OrderService orderService = Context.getOrderService();
		
		Concept creatinineResult = Context.getConceptService().getConcept(790);
		Concept hepatitisBResult = Context.getConceptService().getConcept(159430);
		Concept hepatitisCResult = Context.getConceptService().getConcept(1325);
		CalculationResultMap ret = new CalculationResultMap();
		CalculationResultMap currentCreatinineResult = Calculations.lastObs(creatinineResult, cohort, context);
		CalculationResultMap currentHepatitisBResult = Calculations.lastObs(hepatitisBResult, cohort, context);
		CalculationResultMap curhepatitisCResult = Calculations.lastObs(hepatitisCResult, cohort, context);
		
		StringBuilder sb = new StringBuilder();
		for (Integer ptId : cohort) {
			Patient patient = patientService.getPatient(ptId);
			Obs creatinineResultObs = EmrCalculationUtils.obsResultForPatient(currentCreatinineResult, ptId);
			Obs hepatitsiBResultObs = EmrCalculationUtils.obsResultForPatient(currentHepatitisBResult, ptId);
			Obs hepatitisCResultObs = EmrCalculationUtils.obsResultForPatient(curhepatitisCResult, ptId);
			
			String creatinineResultDate = null;
			String creatinineOrdetDate = null;
			Double creatinineRes = null;
			String hepBResultDate = null;
			String hepBOrderDate = null;
			String hepBRes = null;
			String hepCResultDate = null;
			String hepCOrderDate = null;
			String hepCRes = null;
			
			if (creatinineResultObs != null) {
				creatinineResultDate = DATE_FORMAT.format(creatinineResultObs.getObsDatetime());
				creatinineRes = creatinineResultObs.getValueNumeric();
				creatinineOrdetDate = DATE_FORMAT.format(creatinineResultObs.getOrder().getDateActivated());
				
			}
			
			if (hepatitsiBResultObs != null) {
				hepBResultDate = DATE_FORMAT.format(hepatitsiBResultObs.getObsDatetime());
				hepBRes = hepatitsiBResultObs.getValueCoded().getName(LOCALE).getName();
				hepBOrderDate = DATE_FORMAT.format(hepatitsiBResultObs.getOrder().getDateActivated());
				
			}
			if (hepatitisCResultObs != null) {
				hepCResultDate = DATE_FORMAT.format(hepatitisCResultObs.getObsDatetime());
				hepCRes = hepatitisCResultObs.getValueCoded().getName(LOCALE).getName();
				hepCOrderDate = DATE_FORMAT.format(hepatitisCResultObs.getOrder().getDateActivated());
				
			}
			OrderType labType = orderService.getOrderTypeByUuid(OrderType.TEST_ORDER_TYPE_UUID);
			List<Order> activeOrders = orderService.getActiveOrders(patient, labType, null, null);
			
			for (Order order : activeOrders) {
				if (order.getConcept().getConceptId() == 159430) {
					hepBOrderDate = DATE_FORMAT.format(order.getDateActivated());
				}
				
				if (order.getConcept().getConceptId() == 1325) {
					hepCOrderDate = DATE_FORMAT.format(order.getDateActivated());
				}
				if (order.getConcept().getConceptId() == 790) {
					creatinineOrdetDate = DATE_FORMAT.format(order.getDateActivated());
				}
			}
			
			sb.append("creatinineResultObs:").append(creatinineRes).append(",");
			sb.append("creatinineResultDate:").append(creatinineResultDate).append(",");
			sb.append("creatinineOrderDate:").append(creatinineOrdetDate).append(",");
			
			sb.append("hepatitisBorderDate:").append(hepBOrderDate).append(",");
			sb.append("hepatitisBresultDate:").append(hepBResultDate).append(",");
			sb.append("hepatitisBresult:").append(hepBRes).append(",");
			
			sb.append("hepatitisCorderDate:").append(hepCOrderDate).append(",");
			sb.append("hepatitisCresultDate:").append(hepCResultDate).append(",");
			sb.append("hepatitisCresult:").append(hepCRes).append(",");
			
			ret.put(ptId, new SimpleResult(sb.toString(), this, context));
		}
		return ret;
	}
	
}
