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

package com.liferay.shipping.extension.europe;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.shipping.api.model.ShippingCalculator;
import com.liferay.shipping.api.model.ShippingExtension;
import com.liferay.shipping.api.util.TemplateParser;

import java.math.BigDecimal;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Julio Camarero
 * @author Eduardo Garcia
 */
@Component(immediate = true, service = ShippingExtension.class)
public class ShippingExtensionEurope implements ShippingExtension {

	@Activate
	public void activate() {
		if (_log.isDebugEnabled()) {
			_log.debug("Rule activate: " + getClass().getSimpleName());
		}
	}

	@Override
	public BigDecimal calculateShippingCost(
		PortletRequest request, PortletResponse response,
		BigDecimal baseShippingCost) {

		BigDecimal shippingCost = baseShippingCost.multiply(_EUR_USD_RATE);

		String destination = ParamUtil.getString(request, "destination");
		String discountCode = ParamUtil.getString(request, "discountCode");

		shippingCost = shippingCost.add(_DESTINATION_COST_MAP.get(destination));

		if (Validator.isNotNull(discountCode)) {
			BigDecimal shippingDiscount = _shippingCalculator.percentage(
				shippingCost, _DISCOUNT);

			if (shippingCost.compareTo(shippingDiscount) > 0) {
				shippingCost = shippingCost.subtract(shippingDiscount);
			}
		}

		BigDecimal shippingTaxes = _shippingCalculator.percentage(
			shippingCost, _TAXES);

		return shippingCost.add(shippingTaxes);
	}

	@Deactivate
	public void deActivate() {
		if (_log.isDebugEnabled()) {
			_log.debug("Rule deactivate: " + getClass().getSimpleName());
		}
	}

	@Override
	public String getCurrency() {
		return "EUR";
	}

	@Override
	public String getFieldsHTML(Map<String, Object> context) {
		String content = StringPool.BLANK;

		try {
			context.put(
				"conversionRate", _shippingCalculator.format(_EUR_USD_RATE));
			context.put(
				"destinations", ListUtil.fromMapKeys(_DESTINATION_COST_MAP));
			context.put("taxesPercent", _shippingCalculator.format(_TAXES));

			content = _templateParser.parseTemplate(
				getClass(), "templates/fields.ftl", context);
		}
		catch (Exception e) {
			_log.error("Error while processing template ", e);
		}

		return content;
	}

	@Override
	public String getName(Locale locale) {
		return LanguageUtil.get(locale, "europe");
	}

	@Override
	public String getShippingExtensionKey() {
		return "europe";
	}

	@Reference
	public void seShippingCalculator(ShippingCalculator shippingCalculator) {
		_shippingCalculator = shippingCalculator;
	}

	@Reference
	public void setTemplateParser(TemplateParser templateParser) {
		_templateParser = templateParser;
	}

	private static final Map<String, BigDecimal> _DESTINATION_COST_MAP =
		new HashMap<String, BigDecimal>(3);

	static {
		_DESTINATION_COST_MAP.put("spain", new BigDecimal(5));
		_DESTINATION_COST_MAP.put("germany", new BigDecimal(10));
		_DESTINATION_COST_MAP.put("other", new BigDecimal(15));
	}

	private static final BigDecimal _DISCOUNT = new BigDecimal(5);

	private static final BigDecimal _EUR_USD_RATE = new BigDecimal(0.8);

	private static final BigDecimal _TAXES = new BigDecimal(30);

	private static Log _log = LogFactoryUtil.getLog(
		ShippingExtensionEurope.class);

	private ShippingCalculator _shippingCalculator;
	private TemplateParser _templateParser;

}