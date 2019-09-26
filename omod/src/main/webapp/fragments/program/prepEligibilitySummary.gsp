<%
	 ui.decorateWith("kenyaui", "panel", [ heading: "Eligibility Summary", frameOnly: true ])
	 def status = "NEGATIVE"
	 def willingnessToStartPrep = "Yes"
%>
<style>

.label {
	color: white;
	padding-left:50px;
}
.success {background-color: #4CAF50;}
.danger {background-color: #f44336;}
</style>
<div class="ke-page-content">

	<table cellpadding="0" cellspacing="0" border="0" width="100%">
		<th></th>
		<th>PrEP requirement</th>
		<th>Client details</th>
		<th>Eligibility</th>
		<th>Actions</th>

		<tr>
			<td width="30%" valign="top">
				<b>HIV status</b>
			<td width="5%" valign="top" style="padding-left: 5px">
				Negative
			</td>
			<td width="15%" valign="top" style="padding-left: 5px">
				<% if (!htsResult) { %>
				<div>HIV status not recorded</div>
				<% } else { %>
				${htsResult}
				<%}%>
			</td>
			<td width="5%" valign="top" style="padding-left: 5px">
				<% if (htsResult == status && htsInitialValidPeriod <= prepHtsInitialCriteria) { %>
				<div><span class="label success"></span></div>
				<% } else { %>
				<div><span class="label danger"></span></div>
				<%}%>
			</td>
			<td width="15%" valign="top" style="padding-left: 5px">
			</td>
		</tr>
		<tr>
			<td width="30%" valign="top">
				<b>Age</b>
			<td width="15%" valign="top" style="padding-left: 5px">
			 ${prepAgeCriteria} years and above
			</td>
			<td width="15%" valign="top" style="padding-left: 5px">
				<% if (!age) { %>
				<div>Age less than one year</div>
				<% } else { %>
				${age} years
				<%}%>
			</td>
			<td width="5%" valign="top" style="padding-left: 5px">
				<% if (age >=prepAgeCriteria) { %>
				<div><span class="label success"></span></div>
				<% } else { %>
				<div><span class="label danger"></span></div>
				<%}%>

			</td>
			<td width="15%" valign="top" style="padding-left: 5px">
			</td>
		</tr>
		<tr>
			<td width="30%" valign="top">
				<b>Weight</b>
			<td width="15%" valign="top" style="padding-left: 5px">
				${prepWeightCriteria} Kgs and above
			</td>
			<td width="15%" valign="top" style="padding-left: 5px">
				<% if (!weight) { %>
				<div>No weight recorded</div>
				<% } else { %>
				${weight} Kgs
				<%}%>
			</td>
			<td width="5%" valign="top" style="padding-left: 5px">
				<% if (weight >= prepWeightCriteria) { %>
				<div ><span class="label success"></span></div>
				<% } else { %>
				<div ><span class="label danger"></span></div>
				<%}%>
			</td>
			<td width="15%" valign="top" style="padding-left: 5px">

			</td>

		</tr>

		<tr>
			<td width="30%" valign="top">
				<b>Willingness to start PrEP</b>
			<td width="15%" valign="top" style="padding-left: 5px">
				Yes
			</td>
			<td width="15%" valign="top" style="padding-left: 5px">
				<% if (!willingnessToTakePrep) { %>
				<div>Client willingness to start PrEP not recorded</div>
				<% } else { %>
				${willingnessToTakePrep}
				<%}%>
			</td>
			<td width="5%" valign="top" style="padding-left: 5px">
				<% if (willingnessToTakePrep == willingnessToStartPrep) { %>
				<div><span class="label success"></span></div>
				<% } else { %>
				<div><span class="label danger"></span></div>
				<%}%>
			</td>
			<td width="15%" valign="top" style="padding-left: 5px">

			</td>

		</tr>
		<tr>
			<td width="30%" valign="top">
				<b>Creatinine</b>
			<td width="15%" valign="top" style="padding-left: 5px">
				Less than 50mls/Min
			</td>
			<td width="15%" valign="top" style="padding-left: 5px">
				<% if (!creatinine) { %>
				<div>No creatinine result</div>
				<% } else { %>
				${creatinine} umol/L
				<%}%>
			</td>
			<td width="5%" valign="top" style="padding-left: 5px">
				<% if (creatinine <= prepCreatinineCriteria) { %>
				<div><span class="label success"></span></div>
				<% } else { %>
				<div><span class="label danger"></span></div>
				<%}%>
			</td>
			<td width="15%" valign="top" style="padding-left: 5px">

			</td>
		</tr>
	</table>
	<div style="padding-top: 8px">
	     <b>KEY:</b><span>Pass = <span class="label success"></span></span> &nbsp;&nbsp;&nbsp;
	      <span>Fail = <span class="label danger"></span></span>

	</div>

</div>