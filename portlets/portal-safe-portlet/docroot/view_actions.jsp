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

<%@ include file="/init.jsp" %>

<%
ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);
String itemName = (String)row.getObject();
%>

<liferay-ui:icon-menu>
	<portlet:renderURL var="viewURL">
		<portlet:param name="mvcPath" value="/view_item.jsp" />
		<portlet:param name="redirect" value="<%= currentURL %>" />
		<portlet:param name="itemName" value="<%= itemName %>" />
	</portlet:renderURL>

	<liferay-ui:icon
		image="view"
		method="get"
		url="${viewURL}"
		/>

	<portlet:renderURL var="editURL">
		<portlet:param name="mvcPath" value="/edit_item.jsp" />
		<portlet:param name="redirect" value="<%= currentURL %>" />
		<portlet:param name="itemName" value="<%= itemName %>" />
	</portlet:renderURL>

	<liferay-ui:icon
		image="edit"
		method="get"
		url="${editURL}"
		/>

	<liferay-security:permissionsURL
		modelResource="<%= Item.class.getName() %>"
		modelResourceDescription="<%= itemName %>"
		resourcePrimKey="<%= itemName %>"
		var="permissionsURL"
		/>

	<liferay-ui:icon
		image="permissions"
		url="${permissionsURL}"
		/>

	<portlet:actionURL name="deleteItem" var="deleteURL">
		<portlet:param name="redirect" value="<%= currentURL %>" />
		<portlet:param name="itemName" value="<%= itemName %>" />
	</portlet:actionURL>

	<liferay-ui:icon-delete url="${deleteURL}" />
</liferay-ui:icon-menu>