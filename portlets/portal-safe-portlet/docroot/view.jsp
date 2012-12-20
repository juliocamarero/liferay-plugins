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
	String safePasswordEncoded = PropsUtil.get(PropsKeys.PORTAL_SAFE_PASSWORD + StringPool.PERIOD + company.getCompanyId());

	if(Validator.isNull(safePasswordEncoded) || Validator.isNotNull(ParamUtil.getString(renderRequest, "asdf"))){
		 %><jsp:include page="generate_password.jsp" /><%
		return;
	}

	PortletURL portletURL = renderResponse.createRenderURL();
%>

<jsp:include page="toolbar.jsp" />

<aui:form action="<%= portletURL.toString() %>" method="post" name="fm">
	<aui:input name="redirect" type="hidden" value="<%= portletURL.toString() %>" />

	<liferay-ui:search-container
		searchContainer="<%= new ItemSearch(renderRequest, portletURL) %>"
		>
		<liferay-ui:search-form page="/search.jsp" servletContext="<%= application %>" />

		<liferay-ui:search-container-results>
			<%@ include file="search_results.jspf" %>
		</liferay-ui:search-container-results>


		<liferay-ui:search-container-row
			className="java.lang.String"
			modelVar="itemName"
			>
			<portlet:renderURL var="rowURL">
				<portlet:param name="mvcPath" value="/view_item.jsp" />
				<portlet:param name="redirect" value="<%= currentURL %>" />
				<portlet:param name="itemName" value="<%= itemName %>" />
			</portlet:renderURL>

			<liferay-ui:search-container-column-text
				href="<%= rowURL %>"
				name="name"
				>
				<%= HtmlUtil.escape(itemName) %>
			</liferay-ui:search-container-column-text>

			<liferay-ui:search-container-column-jsp
				align="right"
				path="/view_actions.jsp"
				valign="top"
				/>

		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator />
	</liferay-ui:search-container>
</aui:form>