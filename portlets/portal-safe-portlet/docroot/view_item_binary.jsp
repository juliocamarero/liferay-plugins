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
String redirect = ParamUtil.getString(request, "redirect");

String itemName = ParamUtil.getString(request, "itemName");

BinaryItem item = (BinaryItem) request.getAttribute("item");
%>

<div class="separator"><!-- --></div>

<liferay-ui:icon-list>
	<portlet:resourceURL id="<%= itemName %>" var="downloadURL" />

	<liferay-ui:icon
		image="download"
		method="get"
		url="${downloadURL}"
		/>

	<portlet:renderURL var="editURL">
		<portlet:param name="itemName" value="<%= itemName %>" />
		<portlet:param name="mvcPath" value="/edit_item.jsp" />
		<portlet:param name="redirect" value="<%= currentURL %>" />
	</portlet:renderURL>

	<liferay-ui:icon
		image="edit"
		method="get"
		url="${editURL}"
		/>

	<portlet:actionURL name="deleteItem" var="deleteURL">
		<portlet:param name="itemName" value="<%= itemName %>" />
		<portlet:param name="redirect" value="<%= renderResponse.createRenderURL().toString() %>" />
	</portlet:actionURL>

	<liferay-ui:icon-delete url="${deleteURL}" />
</liferay-ui:icon-list>