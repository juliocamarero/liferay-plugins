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
import java.math.RoundingMode;

import java.text.DecimalFormat;

/**
 * @author Julio Camarero
 * @author Eduardo Garcia
 */
public interface ShippingCalculator {

	public BigDecimal calculateShippingCost(
		int itemQuantity, BigDecimal itemPrice);

	public String format(BigDecimal bigDecimal);

	public DecimalFormat getDecimalFormat();

	public RoundingMode getDecimalRoundingMode();

	public int getDecimalScale();

	public String getDefaultCurrency();

	public BigDecimal percentage(BigDecimal base, BigDecimal percentage);

}