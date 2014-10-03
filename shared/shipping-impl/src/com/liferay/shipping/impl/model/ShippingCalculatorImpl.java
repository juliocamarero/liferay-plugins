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

import com.liferay.shipping.api.model.ShippingCalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.text.DecimalFormat;

import org.osgi.service.component.annotations.Component;

/**
 * @author Julio Camarero
 * @author Eduardo Garcia
 */
@Component(immediate = true, service = ShippingCalculator.class)
public class ShippingCalculatorImpl implements ShippingCalculator {

	@Override
	public BigDecimal calculateShippingCost(
		int itemQuantity, BigDecimal itemPrice) {

		return itemPrice.multiply(new BigDecimal(itemQuantity));
	}

	@Override
	public String format(BigDecimal bigDecimal) {
		bigDecimal.setScale(getDecimalScale(), getDecimalRoundingMode());

		return getDecimalFormat().format(bigDecimal);
	}

	@Override
	public DecimalFormat getDecimalFormat() {
		return _DECIMAL_FORMAT;
	}

	@Override
	public RoundingMode getDecimalRoundingMode() {
		return RoundingMode.HALF_UP;
	}

	@Override
	public int getDecimalScale() {
		return _DECIMAL_SCALE;
	}

	@Override
	public String getDefaultCurrency() {
		return "USD";
	}

	@Override
	public BigDecimal percentage(BigDecimal base, BigDecimal percentage) {
		return base.multiply(percentage).divide(_ONE_HUNDRED);
	}

	private final static DecimalFormat _DECIMAL_FORMAT = new DecimalFormat();

	static {
		_DECIMAL_FORMAT.setMaximumFractionDigits(2);

		_DECIMAL_FORMAT.setMinimumFractionDigits(0);

		_DECIMAL_FORMAT.setGroupingUsed(false);
	}

	private final static int _DECIMAL_SCALE = 2;

	private final static BigDecimal _ONE_HUNDRED = new BigDecimal(100);

}