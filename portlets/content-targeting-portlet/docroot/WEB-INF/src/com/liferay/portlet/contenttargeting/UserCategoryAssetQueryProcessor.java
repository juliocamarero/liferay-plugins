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

package com.liferay.portlet.contenttargeting;

import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserRemotePreference;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.model.AssetVocabulary;
import com.liferay.portlet.asset.service.AssetCategoryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetVocabularyLocalServiceUtil;
import com.liferay.portlet.asset.service.persistence.AssetEntryQuery;
import com.liferay.portlet.assetpublisher.util.AssetQueryProcessor;
import com.liferay.portlet.messageboards.NoSuchCategoryException;

import java.util.List;

import javax.portlet.PortletPreferences;
public class UserCategoryAssetQueryProcessor implements AssetQueryProcessor {

	public static final String VOCABULARY_NAME = "UserSegment";

	public UserCategoryAssetQueryProcessor() {
		_classNameId = PortalUtil.getClassNameId(User.class.getName());
	}

	@Override
	public void adaptQuery(
			User user, PortletPreferences preferences,
			AssetEntryQuery assetEntryQuery)
		throws Exception {

		boolean filterByUserSegment = GetterUtil.getBoolean(
			preferences.getValue("filterByUserSegment", "0"));

		if (!filterByUserSegment) {
			return;
		}

		Group companyGroup = GroupLocalServiceUtil.getCompanyGroup(
			user.getCompanyId());

		AssetVocabulary userSegmentVocabulary =
			AssetVocabularyLocalServiceUtil.getGroupVocabulary(
				companyGroup.getGroupId(), VOCABULARY_NAME);

		_userSegmentVocabularyId = userSegmentVocabulary.getVocabularyId();

		AssetCategory userSegmentCategory = null;

		if (!user.isDefaultUser()) {
			List<AssetCategory> categories =
				AssetCategoryLocalServiceUtil.getCategories(
					_classNameId, user.getUserId());

			for (AssetCategory category : categories) {
				if (category.getVocabularyId() ==
						_userSegmentVocabularyId) {

					userSegmentCategory = category;

					break;
				}
			}
		}
		else {
			UserRemotePreference userSegmentPreference =
				user.getRemotePreference("UserSegment");

			if ((userSegmentPreference != null) ||
				Validator.isNotNull(userSegmentPreference.getValue())) {

				long userSegmentCategoryId = GetterUtil.getLong(
					userSegmentPreference.getValue());

				try {
					userSegmentCategory =
						AssetCategoryLocalServiceUtil.getCategory(
							userSegmentCategoryId);
				}
				catch (NoSuchCategoryException nsce) {
				}
			}
		}

		if (userSegmentCategory != null) {
			long[] allCategoryIds = assetEntryQuery.getAllCategoryIds();

			allCategoryIds = ArrayUtil.append(
				allCategoryIds, userSegmentCategory.getCategoryId());

			assetEntryQuery.setAllCategoryIds(allCategoryIds);
		}
	}

	private final long _classNameId;
	private long _userSegmentVocabularyId;

}