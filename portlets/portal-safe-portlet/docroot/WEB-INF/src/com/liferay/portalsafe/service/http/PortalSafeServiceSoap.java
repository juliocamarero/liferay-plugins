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

package com.liferay.portalsafe.service.http;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import com.liferay.portalsafe.service.PortalSafeServiceUtil;

import java.rmi.RemoteException;

/**
 * <p>
 * This class provides a SOAP utility for the
 * {@link com.liferay.portalsafe.service.PortalSafeServiceUtil} service utility. The
 * static methods of this class calls the same methods of the service utility.
 * However, the signatures are different because it is difficult for SOAP to
 * support certain types.
 * </p>
 *
 * <p>
 * The benefits of using the SOAP utility is that it is cross platform
 * compatible. SOAP allows different languages like Java, .NET, C++, PHP, and
 * even Perl, to call the generated services. One drawback of SOAP is that it is
 * slow because it needs to serialize all calls into a text format (XML).
 * </p>
 *
 * <p>
 * You can see a list of services at http://localhost:8080/api/axis. Set the
 * property <b>axis.servlet.hosts.allowed</b> in portal.properties to configure
 * security.
 * </p>
 *
 * <p>
 * The SOAP utility is only generated for remote services.
 * </p>
 *
 * @author    Tomáš Polešovský
 * @see       PortalSafeServiceHttp
 * @see       com.liferay.portalsafe.service.PortalSafeServiceUtil
 * @generated
 */
public class PortalSafeServiceSoap {
	/**
	* NOTE FOR DEVELOPERS:
	*
	* Never reference this interface directly. Always use {@link
	* com.liferay.portalsafe.service.PortalSafeServiceUtil} to access the
	* portal safe remote service.
	*/
	public static java.lang.String[] listItemsNames(long companyId, long groupId)
		throws RemoteException {
		try {
			java.util.List<java.lang.String> returnValue = PortalSafeServiceUtil.listItemsNames(companyId,
					groupId);

			return returnValue.toArray(new java.lang.String[returnValue.size()]);
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static com.liferay.portal.kernel.safe.model.Item loadItem(
		long companyId, long groupId, java.lang.String name)
		throws RemoteException {
		try {
			com.liferay.portal.kernel.safe.model.Item returnValue = PortalSafeServiceUtil.loadItem(companyId,
					groupId, name);

			return returnValue;
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static void removeItem(long companyId, long groupId,
		java.lang.String name) throws RemoteException {
		try {
			PortalSafeServiceUtil.removeItem(companyId, groupId, name);
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static void saveItem(long companyId, long groupId,
		com.liferay.portal.kernel.safe.model.Item item)
		throws RemoteException {
		try {
			PortalSafeServiceUtil.saveItem(companyId, groupId, item);
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	private static Log _log = LogFactoryUtil.getLog(PortalSafeServiceSoap.class);
}