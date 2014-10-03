/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package com.liferay.shipping.portlet;

import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.shipping.api.model.ShippingExtension;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.portlet.ResourceURL;

/**
 * @author Julio Camarero
 * @author Eduardo Garcia
 */
public class ShippingExtensionItem {

	public ShippingExtensionItem(ShippingExtension shippingExtension) {
		_shippingExtension = shippingExtension;
	}

	public String getName(Locale locale) {
		return _shippingExtension.getName(locale);
	}

	public Map<String, Object> getShippingExtensionItemData(
		LiferayPortletResponse liferayPortletResponse) {

		ResourceURL resourceURL = liferayPortletResponse.createResourceURL();

		resourceURL.setParameter(
			"shippingExtensionKey",
			_shippingExtension.getShippingExtensionKey());

		Map<String, Object> data = new HashMap<String, Object>();

		data.put("resourceURL", resourceURL.toString());

		return data;
	}

	public String getShippingExtensionKey() {
		return _shippingExtension.getShippingExtensionKey();
	}

	private ShippingExtension _shippingExtension;

}