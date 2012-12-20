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
String itemName = ParamUtil.getString(request, "itemName");

KeyStoreItem item = (KeyStoreItem) request.getAttribute("item");

KeyStore keyStore = item.getKeyStore();

String storePass = PwdGenerator.getPassword(12);
String storePassEnc = Base64.encode(CryptoService.getInstance().encrypt(company.getCompanyId(), storePass.getBytes(StringPool.UTF8)));
%>

<aui:fieldset label="keystore-details">
	<p>
		<strong><liferay-ui:message key="type" /></strong>: <%= keyStore.getType() %><br />
		<strong><liferay-ui:message key="provider" /></strong>: <%= keyStore.getProvider() %><br />
		<strong><liferay-ui:message key="keystore-items" /></strong>:
		<ul>
			<c:forEach items="<%= keyStore.aliases() %>" var="alias">
				<li>

				<%
					String alias = (String) pageContext.getAttribute("alias");
				%>

				<c:if test="<%= keyStore.isKeyEntry(alias) %>">
					<liferay-ui:message key="key" />
				</c:if>
				<liferay-ui:message key="alias" />: <%= HtmlUtil.escape(alias) %>,
				<liferay-ui:message key="created" />: <%= DateFormat.getDateTimeInstance().format(keyStore.getCreationDate(alias)) %>
				<c:if test="<%= keyStore.isCertificateEntry(alias) %>">
					, <liferay-ui:message key="certificate" />: <%= HtmlUtil.escape(keyStore.getCertificate(alias).toString()) %>
				</c:if>
				</li>
			</c:forEach>
		</ul>
	</p>
</aui:fieldset>

<div class="separator"><!-- --></div>

<liferay-ui:icon-list>
	<portlet:resourceURL id="<%= itemName %>" var="downloadURL">
		<portlet:param name="storePass" value="<%= storePassEnc %>" />
	</portlet:resourceURL>

	<liferay-ui:icon
		image="download"
		method="get"
		url='<%= "javascript:" + renderResponse.getNamespace() + "downloadFile();" %>'
		/>

	<portlet:renderURL var="editURL">
		<portlet:param name="itemName" value="<%= itemName %>" />
		<portlet:param name="mvcPath" value="/edit_item.jsp" />
		<portlet:param name="redirect" value="<%= currentURL %>" />
	</portlet:renderURL>

	<liferay-ui:icon
		image="edit"
		method="get"
		url="<%= editURL %>"
		/>

	<portlet:actionURL name="deleteItem" var="deleteURL">
		<portlet:param name="itemName" value="<%= itemName %>" />
		<portlet:param name="redirect" value="<%= renderResponse.createRenderURL().toString() %>" />
	</portlet:actionURL>

	<liferay-ui:icon-delete url="<%= deleteURL %>" />
</liferay-ui:icon-list>

<div class="aui-helper-hidden portlet-msg-alert" id="<portlet:namespace/>passwordAlert">
	<liferay-ui:message key="key-store-password-is" />: <span></span>
</div>

<aui:script>
	Liferay.provide(
		window,
		'<portlet:namespace />downloadFile',
		function() {

			var A = AUI();

			var passwordAlertDiv = A.one('#<portlet:namespace />passwordAlert');

			if (passwordAlertDiv) {
				var passwordContent = passwordAlertDiv.one('span');
				passwordContent.set('text', '<%= HtmlUtil.escapeJS(storePass) %>');
				passwordAlertDiv.show();
			}

			location.href = '<%= HtmlUtil.escapeJS(pageContext.getAttribute("downloadURL").toString()) %>';

			return true;
		},
		['aui-base']
	);
</aui:script>