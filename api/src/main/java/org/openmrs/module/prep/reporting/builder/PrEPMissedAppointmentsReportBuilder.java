/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.prep.reporting.builder;

import org.openmrs.EncounterType;
import org.openmrs.module.kenyacore.report.HybridReportDescriptor;
import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.kenyacore.report.builder.AbstractHybridReportBuilder;
import org.openmrs.module.kenyacore.report.builder.Builds;
import org.openmrs.module.kenyacore.report.data.patient.definition.CalculationDataDefinition;
import org.openmrs.module.kenyaemr.calculation.library.TelephoneNumberCalculation;
import org.openmrs.module.kenyaemr.calculation.library.rdqa.PatientProgramEnrollmentCalculation;
import org.openmrs.module.kenyaemr.reporting.calculation.converter.EncounterDatetimeConverter;
import org.openmrs.module.kenyaemr.reporting.calculation.converter.PatientProgramEnrollmentConverter;
import org.openmrs.module.kenyaemr.reporting.data.converter.CalculationResultConverter;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.prep.metadata.PrepMetadata;
import org.openmrs.module.prep.reporting.cohort.definition.PrEPMissedAppointmentsCohortDefinition;
import org.openmrs.module.prep.reporting.data.converter.definition.prep.NextAppointmentDateDataDefinition;
import org.openmrs.module.prep.reporting.data.converter.definition.prep.NumberOfDaysLateDataDefinition;
import org.openmrs.module.prep.reporting.data.converter.definition.prep.PrepPopulatonTypeDataDefinition;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.common.SortCriteria;
import org.openmrs.module.reporting.common.TimeQualifier;
import org.openmrs.module.reporting.data.DataDefinition;
import org.openmrs.module.reporting.data.converter.DataConverter;
import org.openmrs.module.reporting.data.converter.DateConverter;
import org.openmrs.module.reporting.data.converter.ObjectFormatter;
import org.openmrs.module.reporting.data.patient.definition.EncountersForPatientDataDefinition;
import org.openmrs.module.reporting.data.person.definition.*;
import org.openmrs.module.reporting.dataset.definition.PatientDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Builds({ "kenyaemr.prep.prep.report.prepMissedAppointments" })
public class PrEPMissedAppointmentsReportBuilder extends AbstractHybridReportBuilder {
	
	public static final String DATE_FORMAT = "dd/MM/yyyy";
	
	/**
	 * @see org.openmrs.module.kenyacore.report.builder.AbstractCohortReportBuilder#addColumns(org.openmrs.module.kenyacore.report.CohortReportDescriptor,
	 *      PatientDataSetDefinition)
	 */
	@Override
	protected void addColumns(HybridReportDescriptor report, PatientDataSetDefinition dsd) {
		
		DataConverter nameFormatter = new ObjectFormatter("{familyName}, {givenName}");
		DataDefinition nameDef = new ConvertedPersonDataDefinition("name", new PreferredNameDataDefinition(), nameFormatter);
		dsd.addSortCriteria("Number of days late", SortCriteria.SortDirection.ASC);
		dsd.setName("missedAppointments");
		dsd.addColumn("id", new PersonIdDataDefinition(), "");
		dsd.addColumn("Name", nameDef, "");
		dsd.addColumn("Age", new AgeDataDefinition(), "", new DataConverter[0]);
		dsd.addColumn("Sex", new GenderDataDefinition(), "", new DataConverter[0]);
		EncountersForPatientDataDefinition definition = new EncountersForPatientDataDefinition();
		EncounterType followup = MetadataUtils.existing(EncounterType.class, PrepMetadata._EncounterType.PREP_CONSULTATION);
		EncounterType initialFollowup = MetadataUtils.existing(EncounterType.class,
		    PrepMetadata._EncounterType.PREP_INITIAL_FOLLOWUP);
		EncounterType monthlyRefill = MetadataUtils.existing(EncounterType.class,
		    PrepMetadata._EncounterType.PREP_MONTHLY_REFILL);
		
		List<EncounterType> encounterTypes = Arrays.asList(followup, initialFollowup, monthlyRefill);
		
		definition.setWhich(TimeQualifier.LAST);
		definition.setTypes(encounterTypes);
		dsd.addColumn("Last Visit Date", definition, "", new EncounterDatetimeConverter());
		dsd.addColumn("Last PrEP Appointment date", new NextAppointmentDateDataDefinition(), "");
		dsd.addColumn("Population type", new PrepPopulatonTypeDataDefinition(), "");
		dsd.addColumn("Number of days late", new NumberOfDaysLateDataDefinition(), "");
		dsd.addColumn("Program", new CalculationDataDefinition("Program", new PatientProgramEnrollmentCalculation()), "",
		    new PatientProgramEnrollmentConverter());
		dsd.addColumn("Phone number", new CalculationDataDefinition("Phone number", new TelephoneNumberCalculation()), "",
		    new DataConverter[] { new CalculationResultConverter() });
	}
	
	@Override
	protected Mapped<CohortDefinition> buildCohort(HybridReportDescriptor descriptor, PatientDataSetDefinition dsd) {
		CohortDefinition cd = new PrEPMissedAppointmentsCohortDefinition();
		cd.setName("Missed Appointments");
		return ReportUtils.map(cd, "");
	}
}
