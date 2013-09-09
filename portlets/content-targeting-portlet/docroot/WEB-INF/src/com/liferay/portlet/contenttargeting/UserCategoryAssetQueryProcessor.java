package com.liferay.portlet.contenttargeting;

import javax.portlet.PortletPreferences;

import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.model.User;
import com.liferay.portlet.asset.service.persistence.AssetEntryQuery;
import com.liferay.portlet.assetpublisher.util.AssetQueryProcessor;

public class UserCategoryAssetQueryProcessor implements AssetQueryProcessor {

	@Override
	public void adaptQuery(User user, PortletPreferences preferences,
			AssetEntryQuery assetEntryQuery) 
		throws Exception {
		
	}

}