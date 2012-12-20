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

package com.liferay.portalsafe.util;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.util.portlet.PortletProps;

/**
 * @author Tomas Polesovsky
 */
public class PortletPropsValues {

	public static final String PORTAL_SAFE_ALGORITHM =
		GetterUtil.getString(
			PortletProps.get(PortletPropsKeys.PORTAL_SAFE_ALGORITHM), "AES");

	public static final int PORTAL_SAFE_ALGORITHM_KEY_SIZE =
		GetterUtil.getInteger(
			PortletProps.get(PortletPropsKeys.PORTAL_SAFE_ALGORITHM), 256);

	public static final int PORTAL_SAFE_BCRYPT_LOG_ROUNDS =
		GetterUtil.getInteger(
			PortletProps.get(PortletPropsKeys.PORTAL_SAFE_BCRYPT_LOG_ROUNDS),
			12);

	public static final String[] PORTAL_SAFE_SERIALIZER_CLASSES =
		PortletProps.getArray(PortletPropsKeys.PORTAL_SAFE_SERIALIZER_CLASSES);

	public static final String PORTAL_SAFE_STORAGE_CLASS = PortletProps.get(
		PortletPropsKeys.PORTAL_SAFE_STORAGE_CLASS);

}