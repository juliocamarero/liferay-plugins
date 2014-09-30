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

package com.liferay.shipping.api.model;

import java.math.BigDecimal;

import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

/**
 * @author Julio Camarero
 * @author Eduardo Garcia
 */
public interface ShippingExtension {

	public BigDecimal calculateShippingCost(
		PortletRequest request, PortletResponse response,
		BigDecimal baseShippingCost);

	public String getCurrency();

	public String getFieldsHTML(Map<String, Object> context);

	public String getName(Locale locale);

	public String getShippingExtensionKey();

}