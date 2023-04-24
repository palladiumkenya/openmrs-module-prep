package org.openmrs.module.prep.calculation.library.prep;

import org.openmrs.*;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResult;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.kenyacore.calculation.AbstractPatientCalculation;
import org.openmrs.module.kenyacore.calculation.BooleanResult;
import org.openmrs.module.kenyacore.calculation.PatientFlagCalculation;
import org.openmrs.module.prep.metadata.PrepMetadata;
import org.openmrs.module.prep.util.EmrUtils;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
/*
 *HIV Rapid Test, Before initiating PrEP as per the National HTS algorithm. At Month 1, Month 3, thereafter every 3months
 * Creatine Test (UECs),Test within 1-3 months of PrEP Initiation. Age > 50years – Screen every 6-12months
 *Hepatitis B Surface Antigen (HBsAg),Test once within 3 months of initiating PrEP. If negative, offer/refer for immunization.
 *Hepatitis C Virus Serology, Test once within 3months of PrEP initiation. Every 12 months for persons at high risk of Hepatitis C infection
 * */
public class LabMonitoringForPrePCalculation extends AbstractPatientCalculation implements PatientFlagCalculation {
	
	private String labMonitoringMessage;
	Long prepInitialValidPeriod = null;
	PatientService patientService = Context.getPatientService();
	Date currentDate = new Date();
	String htsResults = null;
	int age = 50;
	@Override
	public CalculationResultMap evaluate(Collection<Integer> collection, Map<String, Object> map,
	        PatientCalculationContext patientCalculationContext) {
		CalculationResultMap ret = new CalculationResultMap();
		
		for (Integer ptId : collection) {
			Patient patient = patientService.getPatient(ptId);
			int patientAge = patient.getAge();
			
			boolean onPrep = false;
			Encounter lastPrepInitiation = EmrUtils.lastEncounter(patient, Context.getEncounterService()
			        .getEncounterTypeByUuid(PrepMetadata._EncounterType.PREP_ENROLLMENT), Context.getFormService()
			        .getFormByUuid(PrepMetadata._Form.PREP_ENROLLMENT_FORM));
			CalculationResult htsRes = EmrCalculationUtils
			        .evaluateForPatient(LastHtsResultsCalculation.class, null, patient);
			if (htsRes != null && htsRes.getValue() != null) {
				htsResults = ((Obs) htsRes.getValue()).getValueCoded().getName().toString();
			}
			if (lastPrepInitiation != null) {
				
				Date encounterDate = lastPrepInitiation.getEncounterDatetime();
				long difference = currentDate.getTime() - encounterDate.getTime();
				prepInitialValidPeriod = difference / (24 * 60 * 60 * 1000);
				int months = (int) Math.floor(prepInitialValidPeriod / 30.44);
				int years = (int) Math.floor(months / 12);
				if (months % 3 == 1 || (months > 0 && months % 3 == 0)) {
					labMonitoringMessage = "HIV Rapid Test";
					onPrep = true;
				}
				if (patientAge > age && (encounterDate != null && (months >= 1 && months <= 3) || (months % 6 == 0 && months >= 6))) {
					labMonitoringMessage = "Creatine Test (UECs)";
					onPrep = true;
				}
				
				if (months <= 3 && htsResults.equals("NEGATIVE")) {
					labMonitoringMessage = "Hepatitis B Surface Antigen (HBsAg)";
					onPrep = true;
				}
				if (months <= 3 && (encounterDate != null && years >= 1 && months % 12 == 0 )) {
					labMonitoringMessage = "Hepatitis C Virus Serology";
					onPrep = true;
				}
			}
			ret.put(ptId, new BooleanResult(onPrep, this));
		}
		return ret;
	}
	
	@Override
	public String getFlagMessage() {
		return labMonitoringMessage;
	}
	
}
