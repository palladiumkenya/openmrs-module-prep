<%
    ui.decorateWith("kenyaui", "panel", [heading: "PREP Services Care"])

    def dataPoints = []

    dataPoints << [label: "Patient", value: patient.patient]
    dataPoints << [label: "Completion Status", value: complete.complete]

%>
<div class="ke-stack-item">
    <% dataPoints.each { print ui.includeFragment("kenyaui", "widget/dataPoint", it) } %>
</div>