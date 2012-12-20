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
--%><%@include file="/init.jsp" %>

<div class="portlet-msg-alert">
	<liferay-ui:message key="company-portal-safe-password-is-not-configured" />
</div>

<%
	if(!permissionChecker.isCompanyAdmin()){
%>

<div class="portlet-msg-alert">
	<liferay-ui:message key="only-company-admin-is-allowed-to-configure-portal-safe" />
</div>

<%
		return;
	}

	long companyId = company.getCompanyId();
	String randomInput = request.getParameter("randomInput");
	char[] encodedPassword = null;
	char[] plainTextPassword = null;
	if(Validator.isNotNull(randomInput)){
		char[][] result = CryptoService.getInstance().generatePassword(companyId, randomInput.toCharArray());
		plainTextPassword = result[0];
		encodedPassword = result[1];
	}

	byte[] randPassBytes = new byte[16];
	SecureRandom rand = new SecureRandom();
	rand.nextBytes(randPassBytes);
	String randPass = StringUtil.bytesToHexString(randPassBytes);
%>

<c:choose>
	<c:when test="<%= Validator.isNotNull(encodedPassword) %>">
		<aui:fieldset label="portal-configuration">
			Please update your portal-ext.properties with the following definition:<br />
			<textarea
				class="aui-field-input aui-field-input-text portalSafePassword"
				disabled="true"
				multiple="true">
				<%
					out.print(PropsKeys.PORTAL_SAFE_PASSWORD.concat(StringPool.PERIOD).concat(String.valueOf(companyId)));
					out.print(StringPool.EQUAL);
				    out.print(encodedPassword); %>
				</textarea>
		</aui:fieldset>

		<aui:fieldset label="secret-key">
			<liferay-ui:message key="please-print-the-key-and-store-safely" />.
			<div class="portlet-msg-alert">
				<liferay-ui:message key="key" />: <% out.write(plainTextPassword); %><br />
			</div>

			<liferay-ui:message key="if-you-ever-happen-to-lose-company-key-or-the-password-above,this-is-the-only-key-you-can-use-to-recover-your-data" />.<br />
		</aui:fieldset>
	</c:when>
	<c:otherwise>
		<portlet:renderURL var="generatePasswordURL" />

		<aui:form action="${generatePasswordURL}">
			<liferay-ui:message key="please-move-your-mouse-over-the-portlet-to-obtain-random-data.the-longer-you-move-the-better-is-the-key-strength" />
			<aui:input inputCssClass="randomInput" label="" name="randomInput" type="text" value="<%= randPass %>" />
			<aui:button type="submit" />
		</aui:form>

		<aui:script use="aui-event">
			Liferay.provide(
				window,
				'<portlet:namespace />mousePosition',
				function(e) {

					var A = AUI();

					var randomInput = A.one('#<portlet:namespace />randomInput');

					if (randomInput) {
						var text = randomInput.get('value');
						var mousePos = (e.clientX + e.clientY) % 256;
						if (mousePos == lastPos) {
							return true;
						}
						var pos = Math.floor(Math.random()*(text.length / 2)) * 2;
						var number = parseInt(text.substr(pos, 2), 16);
						var newNumber = (number ^ mousePos).toString(16);
						while (newNumber.length < 2) {
							newNumber = '0' + newNumber;
						}
						text = text.substr(0, pos) + newNumber + text.substr(pos+2);
						randomInput.set('value', text);
					}

					return true;
				},
				['aui-base']
			);

			var A = AUI();
			var lastPos = 0;
			A.on('mouseover', <portlet:namespace />mousePosition, '#p_p_id<portlet:namespace /> .portlet-content');
		</aui:script>
	</c:otherwise>
</c:choose>

<%
	CryptoService.getInstance().clearPassword(plainTextPassword);
	CryptoService.getInstance().clearPassword(encodedPassword);
%>