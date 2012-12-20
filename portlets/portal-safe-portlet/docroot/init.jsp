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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %><%@
taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %><%@
taglib uri="http://liferay.com/tld/security" prefix="liferay-security" %><%@
taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %><%@
taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>

<%@ page import="com.liferay.portal.kernel.dao.search.ResultRow" %><%@
page import="com.liferay.portal.kernel.log.Log" %><%@
page import="com.liferay.portal.kernel.log.LogFactoryUtil" %><%@
page import="com.liferay.portal.kernel.log.LogUtil" %><%@
page import="com.liferay.portal.kernel.safe.model.BinaryItem" %><%@
page import="com.liferay.portal.kernel.safe.model.Item" %><%@
page import="com.liferay.portal.kernel.safe.model.KeyStoreItem" %><%@
page import="com.liferay.portal.kernel.safe.model.NoSuchItemException" %><%@
page import="com.liferay.portal.kernel.safe.PortalSafeException" %><%@
page import="com.liferay.portal.kernel.servlet.SessionErrors" %><%@
page import="com.liferay.portal.kernel.util.Base64" %><%@
page import="com.liferay.portal.kernel.util.HtmlUtil" %><%@
page import="com.liferay.portal.kernel.util.ListUtil" %><%@
page import="com.liferay.portal.kernel.util.ParamUtil" %><%@
page import="com.liferay.portal.kernel.util.PropsUtil" %><%@
page import="com.liferay.portal.kernel.util.StringPool" %><%@
page import="com.liferay.portal.kernel.util.StringUtil" %><%@
page import="com.liferay.portal.kernel.util.Validator" %><%@
page import="com.liferay.portal.kernel.util.WebKeys" %><%@
page import="com.liferay.portalsafe.EncryptedContentCorruptedException" %><%@
page import="com.liferay.portalsafe.ItemNameException" %><%@
page import="com.liferay.portalsafe.PortalSafeConfigurationException" %><%@
page import="com.liferay.portalsafe.search.ItemDisplayTerms" %><%@
page import="com.liferay.portalsafe.search.ItemSearch" %><%@
page import="com.liferay.portalsafe.search.ItemSearchTerms" %><%@
page import="com.liferay.portalsafe.service.permission.PortalSafePermission" %><%@
page import="com.liferay.portalsafe.service.PortalSafeServiceUtil" %><%@
page import="com.liferay.portal.kernel.safe.storage.StorageException" %><%@
page import="com.liferay.portalsafe.util.CryptoService" %><%@
page import="com.liferay.portalsafe.util.PortletPropsValues" %><%@
page import="com.liferay.portalsafe.util.PropsKeys" %><%@
page import="com.liferay.portal.security.permission.ActionKeys" %><%@
page import="com.liferay.portal.util.PortalUtil" %><%@
page import="com.liferay.util.PwdGenerator" %><%@
page import="java.security.KeyStore" %><%@
page import="java.security.Provider" %><%@
page import="java.security.SecureRandom" %><%@
page import="java.security.Security" %><%@
page import="java.text.DateFormat" %><%@
page import="java.util.Iterator" %><%@
page import="java.util.List" %><%@
page import="javax.portlet.PortletURL" %>


<portlet:defineObjects />

<liferay-theme:defineObjects />

<%
	String currentURL = PortalUtil.getCurrentURL(request);
	long safeGroupId = scopeGroupId;
	String preferencesGroupId = portletPreferences.getValue("groupId", "null");
	if(Validator.isNotNull(preferencesGroupId)){
		safeGroupId = Long.valueOf(preferencesGroupId);
	}
%>