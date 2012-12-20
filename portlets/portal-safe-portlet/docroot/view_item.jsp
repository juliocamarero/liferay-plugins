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

String itemType = StringPool.BLANK;
Item item = null;
try {
	item = PortalSafeServiceUtil.loadItem(company.getCompanyId(), safeGroupId, itemName);
	itemType = item.getClass().getSimpleName();
} catch (Throwable e) {
	_log.warn(e);
	while(e.getCause() != null){
		e = e.getCause();
	}
	SessionErrors.add(renderRequest, e.getClass());
}

	request.setAttribute("item", item);
%>

<liferay-ui:header
	backURL="<%= redirect %>"
	title="<%= itemName %>"
/>

<liferay-ui:error exception="<%= EncryptedContentCorruptedException.class %>" message="content-was-tampered-with-or-password-is-incorrect" />
<liferay-ui:error exception="<%= ItemNameException.class %>" message="invalid-name-was-specified" />
<liferay-ui:error exception="<%= NoSuchItemException.class %>" message="portal-safe-was-unable-find-the-item" />
<liferay-ui:error exception="<%= PortalSafeConfigurationException.class %>" message="portal-safe-is-not-correctly-configured" />
<liferay-ui:error exception="<%= PortalSafeException.class %>" message="portal-safe-was-unable-to-load-the-content" />

<p>
	<liferay-ui:message key="name" />: <%= HtmlUtil.escape(itemName) %><br />
	<liferay-ui:message key="type" />: <%= itemType %>
</p>

<c:if test="<%= item instanceof KeyStoreItem %>">
	<liferay-util:include page="/view_item_keystore.jsp" servletContext="<%= application %>" />
</c:if>

<c:if test="<%= item instanceof BinaryItem %>">
	<liferay-util:include page="/view_item_binary.jsp" servletContext="<%= application %>" />
</c:if>

<%!
	private static final Log _log = LogFactoryUtil.getLog("view_item.jsp");
%>