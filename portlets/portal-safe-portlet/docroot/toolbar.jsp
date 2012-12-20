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
String toolbarItem = ParamUtil.getString(request, "toolbarItem", "view-all");
%>

<div class="lfr-portlet-toolbar">

	<span class="lfr-toolbar-button view-button <%= toolbarItem.equals("view-all") ? "current" : StringPool.BLANK %>">
		<a href="<portlet:renderURL var="viewURL" />"><liferay-ui:message key="view-all" /></a>
	</span>

	<c:if test="<%= PortalSafePermission.contains(permissionChecker, safeGroupId, ActionKeys.ADD_ITEM) %>">
		<liferay-ui:icon-menu align="left" cssClass='<%= "lfr-toolbar-button add-button " + (toolbarItem.equals("add") ? "current" : StringPool.BLANK) %>' direction="down" extended="<%= false %>" icon='<%= themeDisplay.getPathThemeImages() + "/common/add.png" %>' message="add" showWhenSingleIcon="<%= true %>">

			<portlet:renderURL var="addBinaryItemURL">
				<portlet:param name="mvcPath" value="/edit_item.jsp" />
				<portlet:param name="type" value="binary-item" />
				<portlet:param name="redirect" value="<%= currentURL %>" />
			</portlet:renderURL>

			<liferay-ui:icon
				image="add"
				message="binary-item"
				method="get"
				url="${addBinaryItemURL}"
				/>

			<portlet:renderURL var="addKeyStoreItemURL">
				<portlet:param name="mvcPath" value="/edit_item.jsp" />
				<portlet:param name="type" value="keystore-item" />
				<portlet:param name="redirect" value="<%= currentURL %>" />
			</portlet:renderURL>

			<liferay-ui:icon
				image="add"
				message="key-store-item"
				method="get"
				url="${addKeyStoreItemURL}"
				/>

		</liferay-ui:icon-menu>
	</c:if>
</div>