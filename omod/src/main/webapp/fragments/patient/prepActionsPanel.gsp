<%
	def baseLink = ui.pageLink("htmlformentryui", "htmlform/flowsheet", [patientId: currentPatient.patientId, headerForm: "prep:prepEnrollmentHeaderForm.xml"])
	def clinicalEncounterFlowsheets = "flowsheets=prep:prepSTIScreening.xml&flowsheets=prep:prepCircumcisionStatus.xml&flowsheets=prep:prepFertilityIntentions.xml&flowsheets=prep:prepChronicIllnesses.xml&\n" +
			                          "flowsheets=prep:prepKnownAllergies.xml&flowsheets=prep:prepDrugReactions.xml"

	def clinicalEncounterFlowsheeturl = baseLink + clinicalEncounterFlowsheets
%>
<div class="action-container column">
	<div class="action-section">

		<ul>
			<h3>Flowsheets</h3>

			<li class="float-left" style="margin-top: 7px">
				<a href="${ clinicalEncounterFlowsheeturl }" class="float-left">
					<i class="fa fa-files-o"></i>
					Clinical Encounter
				</a>
			</li>
		</ul>
	</div>
</div>