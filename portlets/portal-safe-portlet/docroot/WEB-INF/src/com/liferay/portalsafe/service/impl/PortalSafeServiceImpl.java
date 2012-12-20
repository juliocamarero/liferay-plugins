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

package com.liferay.portalsafe.service.impl;

import com.liferay.portal.kernel.safe.PortalSafe;
import com.liferay.portal.kernel.safe.PortalSafeException;
import com.liferay.portal.kernel.safe.model.Item;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portalsafe.service.base.PortalSafeServiceBaseImpl;
import com.liferay.portalsafe.service.permission.PortalSafeItemPermission;
import com.liferay.portalsafe.service.permission.PortalSafePermission;

import java.util.Iterator;
import java.util.List;

/**
 * The implementation of the portal safe remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the {@link
 * com.liferay.portalsafe.service.PortalSafeService} interface. <p> This is a
 * remote service. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be accessed
 * remotely.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see    com.liferay.portalsafe.service.base.PortalSafeServiceBaseImpl
 * @see    com.liferay.portalsafe.service.PortalSafeServiceUtil
 */
public class PortalSafeServiceImpl extends PortalSafeServiceBaseImpl
	implements PortalSafe {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link com.liferay.portalsafe.service.PortalSafeServiceUtil} to access the portal safe remote service.
	 */

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link
	 * com.liferay.portalsafe.service.PortalSafeServiceUtil} to access the
	 * portal safe remote service.
	 */
	public List<String> listItemsNames(long companyId, long groupId)
		throws PortalSafeException {

		List<String> names = portalSafeLocalService.listItemsNames(
			companyId, groupId);

		for (Iterator<String> it = names.iterator(); it.hasNext();) {
			String name = it.next();

			boolean hasViewPermission = false;
			try {
				hasViewPermission = PortalSafeItemPermission.contains(
					getPermissionChecker(), groupId, name, ActionKeys.VIEW);
			} catch (PrincipalException e) {
				throw new PortalSafeException(e);
			}

			if (!hasViewPermission) {
				it.remove();
			}
		}

		return names;
	}

	public Item loadItem(long companyId, long groupId, String name)
		throws PortalSafeException {

		try {
			PortalSafeItemPermission.check(
				getPermissionChecker(), groupId, name, ActionKeys.VIEW);

		} catch (Exception e) {
			throw new PortalSafeException(e);
		}

		return portalSafeLocalService.loadItem(companyId, groupId, name);
	}

	public void removeItem(long companyId, long groupId, String name)
		throws PortalSafeException {

		try {
			PortalSafeItemPermission.check(
				getPermissionChecker(), groupId, name, ActionKeys.DELETE);

		} catch (Exception e) {
			throw new PortalSafeException(e);
		}

		portalSafeLocalService.removeItem(companyId, groupId, name);
	}

	public void saveItem(long companyId, long groupId, Item item)
		throws PortalSafeException {

		String name = item.getName();

		List<String> names = portalSafeLocalService.listItemsNames(
			companyId, groupId);

		try {
			if (names.contains(name)) {
				PortalSafeItemPermission.check(
					getPermissionChecker(), groupId, name, ActionKeys.UPDATE);
			}
			else {
				PortalSafePermission.check(
					getPermissionChecker(), groupId, ActionKeys.ADD_ITEM);
			}
		} catch (Exception e) {
			throw new PortalSafeException(e);
		}

		portalSafeLocalService.saveItem(companyId, groupId, item);
	}

}