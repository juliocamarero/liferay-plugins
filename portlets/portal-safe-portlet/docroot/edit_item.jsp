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
String redirect = com.liferay.portal.kernel.util.ParamUtil.getString(request, "redirect");

String type = ParamUtil.getString(request, "type");
String newTypeName = "new-binary-item";
String itemType = BinaryItem.class.getSimpleName();
if(Validator.isNotNull(type) && type.equals("keystore-item")){
	newTypeName = "new-key-store-item";
	itemType = KeyStoreItem.class.getSimpleName();
}

String itemName = ParamUtil.getString(request, "itemName");

Item item = null;
if (Validator.isNotNull(itemName)) {
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
}

String keyStoreDefaultType = KeyStore.getDefaultType();
Provider[] providers = Security.getProviders("KeyStore." + keyStoreDefaultType);
String providerDefaultName = StringPool.BLANK;
String providerDefaultInfo = StringPool.BLANK;
if(providers.length > 0){
	providerDefaultName = providers[0].getName();
	providerDefaultInfo= providers[0].getInfo();
}
%>

<liferay-ui:header
	backURL="<%= redirect %>"
	title='<%= (item != null) ? itemName : newTypeName %>'
/>

<portlet:renderURL var="renderURL" />

<portlet:actionURL name="updateItem" var="updateItemURL">
	<portlet:param name="mvcPath" value="/edit_item.jsp" />
</portlet:actionURL>

<aui:form action="<%= updateItemURL %>" enctype="multipart/form-data">
	<aui:input name="itemType" type="hidden" value="<%= itemType %>" />
	<aui:input name="oldItemName" type="hidden" value="<%= itemName %>" />
	<aui:input name="redirect" type="hidden" value="<%= redirect %>" />

	<liferay-ui:error exception="<%= EncryptedContentCorruptedException.class %>" message="content-was-tampered-with-or-password-is-incorrect" />
	<liferay-ui:error exception="<%= ItemNameException.class %>" message="please-enter-a-valid-and-unique-name" />
	<liferay-ui:error exception="<%= NoSuchItemException.class %>" message="portal-safe-was-unable-find-the-item" />
	<liferay-ui:error exception="<%= PortalSafeConfigurationException.class %>" message="portal-safe-is-not-correctly-configured" />
	<liferay-ui:error exception="<%= PortalSafeException.class %>" message="portal-safe-was-unable-to-save-content" />

	<aui:fieldset>
		<aui:input label="name" name="itemName" value="<%= itemName %>">
			<aui:validator name="required" />
		</aui:input>

		<aui:input label="file" name="itemStream" type="file">
		</aui:input>

		<c:if test="<%= itemType.equals(KeyStoreItem.class.getSimpleName()) %>">
			<aui:input label="key-store-password" name="storePass" type="password">
				<aui:validator name="required" />
			</aui:input>

			<aui:button-row>
				<aui:button id='<%= renderResponse.getNamespace() + "advOptionsButton" %>' onClick='<%= renderResponse.getNamespace() + "advancedOptions();" %>' value="advanced" />
			</aui:button-row>

			<aui:fieldset cssClass="aui-helper-hidden" id="advOptionsForm" label="advanced-options">
				<aui:input label="store-type" name="storeType" value="<%= keyStoreDefaultType %>" />
				<aui:input label="store-provider" name="storeProvider" value="<%= providerDefaultName %>" />
				<aui:fieldset label="registered-providers">
					<ul>

						<%
							for(Provider provider : Security.getProviders()){
						%>

							<li><strong><%= provider.getName() %></strong>: <%= provider.getInfo() %></li>

						<%
							}
						%>

					</ul>
				</aui:fieldset>
			</aui:fieldset>
		</c:if>
	</aui:fieldset>

	<aui:button-row>
		<aui:button type="submit" value="save" />
		<aui:button href="<%= redirect %>" type="cancel" />
	</aui:button-row>
</aui:form>

<aui:script>
	Liferay.provide(
		window,
		'<portlet:namespace />advancedOptions',
		function() {
			var A = AUI();

			var advOptionsForm = A.one('#<portlet:namespace />advOptionsForm');

			if (advOptionsForm) {
				advOptionsForm.show();
				var advOptionsButton = A.one('#<portlet:namespace />advOptionsButton');

				if (advOptionsButton) {
					advOptionsButton.hide();
				}
			}
		},
		['aui-base']
	);
</aui:script>

<%!
	private static final Log _log = LogFactoryUtil.getLog("edit_item.jsp");
%>