<%@ page import="com.liferay.portal.kernel.language.LanguageUtil" %>
<%@ page import="com.liferay.portal.kernel.util.GetterUtil" %>
<%@ page import="com.liferay.portal.model.Group" %>
<%@ page import="com.liferay.portal.service.GroupLocalServiceUtil" %>
<%@ page import="com.liferay.portlet.asset.model.AssetVocabulary" %>
<%@ page import="com.liferay.portlet.asset.service.AssetVocabularyLocalServiceUtil" %>
<%@ page import="com.liferay.portlet.contenttargeting.UserCategoryAssetQueryProcessor" %>

<%--
/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<portlet:defineObjects />
<liferay-theme:defineObjects />

<%
Group companyGroup = GroupLocalServiceUtil.getCompanyGroup(user.getCompanyId());
AssetVocabulary userSegmentVocabulary =AssetVocabularyLocalServiceUtil.getGroupVocabulary(companyGroup.getGroupId(),UserCategoryAssetQueryProcessor.VOCABULARY_NAME);

boolean filterByUserSegment = GetterUtil.getBoolean(portletPreferences.getValue("filterByUserSegment", "0"));
%>

<aui:input label='<%= LanguageUtil.format(pageContext, "filter-assets-by-", userSegmentVocabulary.getTitle(locale)) %>"' name="preferences--filterByUserSegment--" type="checkbox" value="<%= filterByUserSegment %>" />