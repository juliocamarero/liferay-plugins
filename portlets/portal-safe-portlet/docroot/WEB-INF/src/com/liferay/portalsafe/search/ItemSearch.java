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

package com.liferay.portalsafe.search;

import com.liferay.portal.kernel.dao.search.SearchContainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

/**
 * Search container implementation for portal safe.
 *
 * @author Tomas Polesovsky
 */
public class ItemSearch extends SearchContainer<String> {

	static List<java.lang.String> headerNames =
		new ArrayList<java.lang.String>();

	static Map<java.lang.String, java.lang.String> orderableHeaders =
		new HashMap<java.lang.String, java.lang.String>();

	static {
		headerNames.add("name");
	}

	public static final java.lang.String EMPTY_RESULTS_MESSAGE =
		"there-are-no-items";

	public ItemSearch(PortletRequest portletRequest, PortletURL iteratorURL) {
		super(
			portletRequest, new ItemDisplayTerms(portletRequest),
			new ItemSearchTerms(portletRequest), DEFAULT_CUR_PARAM,
			DEFAULT_DELTA, iteratorURL, headerNames, EMPTY_RESULTS_MESSAGE);

		ItemDisplayTerms displayTerms = (ItemDisplayTerms)getDisplayTerms();

		iteratorURL.setParameter(ItemDisplayTerms.NAME, displayTerms.getName());
	}

}