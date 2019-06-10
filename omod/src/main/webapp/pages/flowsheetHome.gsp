<%
	ui.decorateWith("kenyaemr", "standardPage")

	def visit = currentVisit
%>

<div class="ke-page-content">
	${ ui.includeFragment("prep", "patient/prepActionsPanel", [ patient: currentPatient ]) }
</div>