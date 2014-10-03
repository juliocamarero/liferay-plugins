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

package com.liferay.shipping.impl.model;

import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.shipping.api.model.ShippingExtension;
import com.liferay.shipping.api.model.ShippingExtensionRegistry;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * @author Julio Camarero
 * @author Eduardo Garcia
 */
@Component(immediate = true, service = ShippingExtensionRegistry.class)
public class ShippingExtensionRegistryImpl
	implements ShippingExtensionRegistry {

	@Override
	public ShippingExtension getShippingExtension(String shippingExtensionKey) {
		return _shippingExtensions.get(shippingExtensionKey);
	}

	@Override
	public List<ShippingExtension> getShippingExtensions() {
		return ListUtil.fromMapValues(_shippingExtensions);
	}

	@Reference(
		unbind = "unregisterShippingExtension",
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC)
	public void registerShippingExtension(ShippingExtension shippingExtension) {
		_shippingExtensions.put(
			shippingExtension.getShippingExtensionKey(), shippingExtension);
	}

	public void unregisterShippingExtension(
		ShippingExtension shippingExtension) {

		_shippingExtensions.remove(shippingExtension);
	}

	private Map<String, ShippingExtension> _shippingExtensions =
		new ConcurrentHashMap<String, ShippingExtension>();

}