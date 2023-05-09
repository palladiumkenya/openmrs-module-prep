/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.prep.calculation.library.prep;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.openmrs.Order;
import org.openmrs.Obs;
import org.openmrs.module.kenyaemr.Dictionary;
import org.openmrs.api.OrderService;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResult;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.kenyacore.calculation.*;
import org.openmrs.module.kenyaemr.calculation.library.hiv.PendingSerumCreatinineUECsResultCalculation;
import org.openmrs.module.kenyaemr.calculation.library.PendingHepatitisCResultCalculation;
import org.openmrs.module.kenyaemr.calculation.library.PendingHepatitisBResultCalculation;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.prep.metadata.PrepMetadata;
import org.openmrs.module.prep.util.EmrUtils;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Form;
import org.openmrs.Order;
import org.openmrs.OrderType;
import org.openmrs.Patient;
import org.openmrs.Program;
import org.openmrs.CareSetting;

import java.util.*;

import static org.joda.time.Months.monthsBetween;

/*
 *HIV Rapid Test, Before initiating PrEP as per the National HTS algorithm. At Month 1, Month 3, thereafter every 3months
 * Creatine Test (UECs),Test within 1-3 months of PrEP Initiation. Age > 50years – Screen every 6-12months
 *Hepatitis B Surface Antigen (HBsAg),Test once within 3 months of initiating PrEP. If negative, offer/refer for immunization.
 *Hepatitis C Virus Serology, Test once within 3months of PrEP initiation. Every 12 months for persons at high risk of Hepatitis C infection
 * */
public class LabMonitoringForPrePCalculation extends AbstractPatientCalculation implements PatientFlagCalculation {
	
	StringBuilder labMonitoringMessage = new StringBuilder();
	
	PatientService patientService = Context.getPatientService();
	
	final int MIN_AGE = 50;
	
	@Override
	public String getFlagMessage() {
		return labMonitoringMessage.toString();
	}
	
	@Override
	public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> map,
	        PatientCalculationContext patientCalculationContext) {
		CalculationResultMap ret = new CalculationResultMap();
		
		CalculationResultMap creatinine = Calculations.lastObs(
		    Dictionary.getConcept(Dictionary.SERUM_CREATININE_UMOL_PER_L), cohort, patientCalculationContext);
		
		Set<Integer> alive = Filters.alive(cohort, patientCalculationContext);
		Program prepProgram = MetadataUtils.existing(Program.class, PrepMetadata._Program.PREP);
		Set<Integer> inPrepProgram = Filters.inProgram(prepProgram, alive, patientCalculationContext);
		Set<Integer> pendingSerumCreatinineTestResults = CalculationUtils.patientsThatPass(calculate(
		    new PendingSerumCreatinineUECsResultCalculation(), cohort, patientCalculationContext));
		
		Set<Integer> pendingHepatitisBResult = CalculationUtils.patientsThatPass(calculate(
		    new PendingHepatitisBResultCalculation(), cohort, patientCalculationContext));
		
		Set<Integer> pendingHepatitisCResult = CalculationUtils.patientsThatPass(calculate(
		    new PendingHepatitisCResultCalculation(), cohort, patientCalculationContext));
		
		OrderService orderService = Context.getOrderService();
		
		for (Integer ptId : cohort) {
			Patient patient = patientService.getPatient(ptId);
			boolean needsPrEPMonitoringLab = false;
			OrderType labType = orderService.getOrderTypeByUuid(OrderType.TEST_ORDER_TYPE_UUID);
			
			if (inPrepProgram.contains(ptId)) {
				
				int patientAge = patient.getAge();
				
				Date currentDate = patientCalculationContext.getNow();
				
				Encounter latestPrEPEnrEncounter = EmrUtils.lastEncounter(patient, Context.getEncounterService()
				        .getEncounterTypeByUuid(PrepMetadata._EncounterType.PREP_ENROLLMENT), Context.getFormService()
				        .getFormByUuid(PrepMetadata._Form.PREP_ENROLLMENT_FORM));
				Date latestPrEPEnrDate = latestPrEPEnrEncounter.getEncounterDatetime();
				
				CalculationResult lastSerumCreatinine = creatinine.get(ptId);
				CareSetting careset = orderService.getCareSetting(1);
				List<Order> patientLabOrders = orderService.getOrders(patient, careset, labType, false);
				
				Date latestSerumCreatinineOrderDate = null;
				Date hepBOrderDate = null;
				Date hepCOrderDate = null;
				Date latestHTSOrderDate = null;
				Date latestHIVTestDate = null;
				Date latestHTSObsDate = null;
				List<Order> latestSerumCreatinineLabTestOrders = new ArrayList<Order>();
				List<Order> latestHTSLabTestOrders = new ArrayList<Order>();
				List<Order> latestHepBLabTestOrders = new ArrayList<Order>();
				List<Order> latestHepCLabTestOrders = new ArrayList<Order>();
				
				if (patientLabOrders.size() > 0) {
					//Get active lab orders
					for (Order contextOrder : patientLabOrders) {
						if (contextOrder.getConcept().equals(Dictionary.getConcept(Dictionary.SERUM_CREATININE_UMOL_PER_L))) {
							latestSerumCreatinineLabTestOrders.add(contextOrder);
						} else if (contextOrder.getConcept().equals(
						    Dictionary.getConcept(Dictionary.RAPID_HIV_CONFIRMATORY_TEST))) {
							latestHTSLabTestOrders.add(contextOrder);
						} else if (contextOrder.getConcept().equals(Dictionary.getConcept(Dictionary.HEPATITITS_B))) {
							latestHepBLabTestOrders.add(contextOrder);
						} else if (contextOrder.getConcept().equals(Dictionary.getConcept(Dictionary.HEPATITITS_C))) {
							latestHepCLabTestOrders.add(contextOrder);
						}
					}
					
					if (latestSerumCreatinineLabTestOrders.size() > 0) {
						Order latestSerumCreatinineOrder = latestSerumCreatinineLabTestOrders.get(0);
						latestSerumCreatinineOrderDate = latestSerumCreatinineOrder != null ? latestSerumCreatinineOrder
						        .getDateActivated() : null;
					}
					if (latestHTSLabTestOrders.size() > 0) {
						Order latestHTSOrder = latestHTSLabTestOrders.get(0);
						latestHTSOrderDate = latestHTSOrder != null ? latestHTSOrder.getDateActivated() : null;
					}
					if (latestHepBLabTestOrders.size() > 0) {
						Order latestHBOrder = latestHepBLabTestOrders.get(0);
						hepBOrderDate = latestHBOrder != null ? latestHBOrder.getDateActivated() : null;
					}
					if (latestHepCLabTestOrders.size() > 0) {
						Order latestHCOrder = latestHepCLabTestOrders.get(0);
						hepCOrderDate = latestHCOrder != null ? latestHCOrder.getDateActivated() : null;
					}
				}
				CalculationResultMap latestHTSObsMap = Calculations.lastObs(
				    Dictionary.getConcept(Dictionary.RESULT_OF_HIV_TEST), cohort, patientCalculationContext);
				
				Obs latestHTSObs = EmrCalculationUtils.obsResultForPatient(latestHTSObsMap, ptId);
				if (latestHTSObs != null) {
					
					latestHTSObsDate = latestHTSObs.getObsDatetime();
				}
				
				if (latestHTSObsDate != null && latestHTSOrderDate != null) {
					if (latestHTSObsDate.after(latestHTSOrderDate)) {
						latestHIVTestDate = latestHTSObsDate;
					} else {
						latestHIVTestDate = latestHTSOrderDate;
					}
				} else if (latestHTSObsDate != null) {
					latestHIVTestDate = latestHTSObsDate;
				} else if (latestHTSOrderDate != null) {
					latestHIVTestDate = latestHTSOrderDate;
				}
				
				int monthsSinceLastHIVTest = Months
				        .monthsBetween(new DateTime(latestHIVTestDate), new DateTime(currentDate)).getMonths();
				int monthsSincePrEPInitiation = Months.monthsBetween(new DateTime(latestPrEPEnrDate),
				    new DateTime(currentDate)).getMonths();
				int monthsSinceLastCreatinineLabOrder = Months.monthsBetween(new DateTime(latestSerumCreatinineOrderDate),
				    new DateTime(currentDate)).getMonths();
				int monthsSinceHepCOrderDate = Months.monthsBetween(new DateTime(hepCOrderDate), new DateTime(currentDate))
				        .getMonths();
				
				Obs serumCreatinineObs = lastSerumCreatinine != null ? EmrCalculationUtils.obsResultForPatient(creatinine,
				    ptId) : null;
				Date lastCrAgResultDate = serumCreatinineObs != null ? serumCreatinineObs.getObsDatetime() : null;
				
				if (!pendingSerumCreatinineTestResults.contains(ptId)
				        && (((serumCreatinineObs == null || lastCrAgResultDate.before(latestPrEPEnrDate)) && monthsSincePrEPInitiation > 0) || (patientAge > MIN_AGE && monthsSinceLastCreatinineLabOrder >= 6))) {
					needsPrEPMonitoringLab = true;
					labMonitoringMessage.append("Due for Creatine Test (UECs)");
				}
				
				if (((latestHTSObs == null && latestHTSOrderDate == null)
				        || (monthsSinceLastHIVTest >= 1 && monthsSincePrEPInitiation >= 1 && monthsSincePrEPInitiation < 3)
				        || (monthsSinceLastHIVTest >= 2 && monthsSincePrEPInitiation == 3) || (monthsSinceLastHIVTest >= 3 && monthsSincePrEPInitiation > 3))) {
					needsPrEPMonitoringLab = true;
					if (labMonitoringMessage.length() == 0) {
						labMonitoringMessage.append("Due for HIV Rapid Test");
					} else {
						labMonitoringMessage.append(", ").append("HIV Rapid Test");
					}
				}
				
				if (!pendingHepatitisBResult.contains(ptId) && hepBOrderDate == null && monthsSincePrEPInitiation >= 1) {
					needsPrEPMonitoringLab = true;
					if (labMonitoringMessage.length() == 0) {
						labMonitoringMessage.append("Due for Hepatitis B Surface Antigen (HBsAg)");
					} else {
						labMonitoringMessage.append(", ").append("Hepatitis B Surface Antigen (HBsAg)");
					}
				}
				
				if (!pendingHepatitisCResult.contains(ptId) && (hepCOrderDate == null && monthsSincePrEPInitiation >= 1)
				        || (monthsSincePrEPInitiation > 3 && monthsSinceHepCOrderDate >= 12)) {
					needsPrEPMonitoringLab = true;
					if (labMonitoringMessage.length() == 0) {
						labMonitoringMessage.append("Due for Hepatitis C Virus Serology");
					} else {
						labMonitoringMessage.append(", ").append("Hepatitis C Virus Serology");
					}
				}
			}
			ret.put(ptId, new BooleanResult(needsPrEPMonitoringLab, this));
		}
		return ret;
	}
}
