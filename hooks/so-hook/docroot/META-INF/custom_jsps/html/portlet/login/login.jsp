<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This file is part of Liferay Social Office. Liferay Social Office is free
 * software: you can redistribute it and/or modify it under the terms of the GNU
 * Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * Liferay Social Office is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Liferay Social Office. If not, see http://www.gnu.org/licenses/agpl-3.0.html.
 */
--%>

<%@ include file="/html/portlet/login/init.jsp" %>

<c:choose>
	<c:when test="<%= themeDisplay.isSignedIn() %>">

		<%
		Group mySite = user.getGroup();

		PortletURL portletURL = new PortletURLImpl(request, PortletKeys.SITE_REDIRECTOR, plid, PortletRequest.ACTION_PHASE);

		portletURL.setWindowState(WindowState.NORMAL);
		portletURL.setPortletMode(PortletMode.VIEW);

		portletURL.setParameter("struts_action", "/my_sites/view");
		portletURL.setParameter("groupId", String.valueOf(mySite.getGroupId()));
		%>

		<div class="portlet-login signed-in-as">
			<div class="profile-image">
				<a href="<%= portletURL %>"><img alt="<%= HtmlUtil.escapeAttribute(user.getFullName()) %>" src="<%= user.getPortraitURL(themeDisplay) %>"></a>
			</div>

			<a class="aui-buttonitem-content aui-state-default user-name" href="<%= portletURL %>"><span class="aui-button-label"><liferay-ui:message key="my-dashboard" /></span></a>
		</div>
	</c:when>
	<c:otherwise>
		<liferay-util:include page="/html/portlet/login/login.jsp" useCustomPage="<%= false %>" />
	</c:otherwise>
</c:choose>