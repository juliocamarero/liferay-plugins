<%--
/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
--%>

<%@ include file="init.jsp" %> 

<liferay-ui:success key="success" message="the-data-was-created-successfully" />
<liferay-ui:error key="error" message="an-error-has-ocurred-while-generating-the-data.please-check-the-logs" />

<liferay-ui:tabs
				names="users,content"
				refresh="<%= false %>"
			/>

<form action="<portlet:actionURL />" class="uni-form" method="post">
<input name="<portlet:namespace/>redirect" type="hidden" value="<%= currentURL %>" />
	<input name="<portlet:namespace/>userId" type="hidden" value="<%= user.getUserId() %>" />
	<input name="<portlet:namespace/>companyId" type="hidden" value="<%= company.getCompanyId() %>" />

<fieldset class="block-labels">
	<legend>User Generator</legend>

	<p>
		Specify the numbers of users to create. You can also create organizations in
		which case the users will be distributed among them.
	</p>

	<ul>
		<li>Users: <%= UserLocalServiceUtil.searchCount(user.getCompanyId(), StringPool.BLANK, StringPool.BLANK, StringPool.BLANK, StringPool.BLANK, StringPool.BLANK, 1, null, true) %></li>
		<li>Organizations: <%= OrganizationLocalServiceUtil.searchCount(company.getCompanyId(), OrganizationConstants.DEFAULT_PARENT_ORGANIZATION_ID, null, null, null, null, null) %></li>
	</ul>

	<div class="ctrl-holder">
		<label class="icon user" for="<portlet:namespace />numberOfUsers"><liferay-ui:message key="number-of-users" /></label>

		<input class="small-number" name="<portlet:namespace />numberOfUsers" type="text" value="0" />
	</div>

	<div class="ctrl-holder">
		<label class="icon organization" for="<portlet:namespace />numberOfOrganizations"><liferay-ui:message key="number-of-organizations" /></label>

		<input class="small-number" name="<portlet:namespace />numberOfOrganizations" type="text" value="0" />
	</div>

	<div class="ctrl-holder">
		<label class="icon organization" for="<portlet:namespace />parentOrganizationId"><liferay-ui:message key="parent-organization-id" /></label>

		<input class="small-number" name="<portlet:namespace />parentOrganizationId" type="text" value="" />
	</div>

	<%
	String randomPrefix = PwdGenerator.getPassword(PwdGenerator.KEY3, 4);
	%>

	<div class="ctrl-holder">
		<label class="icon organization" for="<portlet:namespace />prefix"><liferay-ui:message key="prefix" /></label>

		<input class="small-number" name="<portlet:namespace />prefix" type="text" value="<%= randomPrefix %>" />
	</div>
</fieldset>

	<input type="submit" value="Generate Data"/>

</form>