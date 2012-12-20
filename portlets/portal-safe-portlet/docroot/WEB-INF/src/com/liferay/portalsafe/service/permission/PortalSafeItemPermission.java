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

package com.liferay.portalsafe.service.permission;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.safe.model.Item;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.PermissionChecker;

/**
 * @author Tomas Polesovsky
 */
public class PortalSafeItemPermission {

	public static void check(
		PermissionChecker permissionChecker, long groupId, String itemName,
		String actionId)
		throws PortalException, SystemException {

		if (!contains(permissionChecker, groupId, itemName, actionId)) {
			throw new PrincipalException();
		}
	}

	public static void check(
		PermissionChecker permissionChecker, Item item, String actionId)
		throws PortalException, SystemException {

		if (!contains(permissionChecker, item, actionId)) {
			throw new PrincipalException();
		}
	}

	public static boolean contains(
		PermissionChecker permissionChecker, long groupId, String itemName,
		String actionId) {

		return permissionChecker.hasPermission(
			groupId, Item.class.getName(), itemName, actionId);
	}

	public static boolean contains(
		PermissionChecker permissionChecker, Item item, String actionId)
		throws PortalException, SystemException {

		return contains(
			permissionChecker, item.getGroupId(), item.getName(), actionId);
	}

}