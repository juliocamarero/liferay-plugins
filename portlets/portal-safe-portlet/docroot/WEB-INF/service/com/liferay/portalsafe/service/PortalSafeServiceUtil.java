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

package com.liferay.portalsafe.service;

import com.liferay.portal.kernel.bean.PortletBeanLocatorUtil;
import com.liferay.portal.kernel.util.ReferenceRegistry;
import com.liferay.portal.service.InvokableService;

/**
 * The utility for the portal safe remote service. This utility wraps {@link com.liferay.portalsafe.service.impl.PortalSafeServiceImpl} and is the primary access point for service operations in application layer code running on a remote server.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have security checks based on the propagated JAAS credentials because this service can be accessed remotely.
 * </p>
 *
 * @author Tomáš Polešovský
 * @see PortalSafeService
 * @see com.liferay.portalsafe.service.base.PortalSafeServiceBaseImpl
 * @see com.liferay.portalsafe.service.impl.PortalSafeServiceImpl
 * @generated
 */
public class PortalSafeServiceUtil {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to {@link com.liferay.portalsafe.service.impl.PortalSafeServiceImpl} and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	public static java.lang.String getBeanIdentifier() {
		return getService().getBeanIdentifier();
	}

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	public static void setBeanIdentifier(java.lang.String beanIdentifier) {
		getService().setBeanIdentifier(beanIdentifier);
	}

	public static java.lang.Object invokeMethod(java.lang.String name,
		java.lang.String[] parameterTypes, java.lang.Object[] arguments)
		throws java.lang.Throwable {
		return getService().invokeMethod(name, parameterTypes, arguments);
	}

	/**
	* NOTE FOR DEVELOPERS:
	*
	* Never reference this interface directly. Always use {@link
	* com.liferay.portalsafe.service.PortalSafeServiceUtil} to access the
	* portal safe remote service.
	*/
	public static java.util.List<java.lang.String> listItemsNames(
		long companyId, long groupId)
		throws com.liferay.portal.kernel.safe.PortalSafeException {
		return getService().listItemsNames(companyId, groupId);
	}

	public static com.liferay.portal.kernel.safe.model.Item loadItem(
		long companyId, long groupId, java.lang.String name)
		throws com.liferay.portal.kernel.safe.PortalSafeException {
		return getService().loadItem(companyId, groupId, name);
	}

	public static void removeItem(long companyId, long groupId,
		java.lang.String name)
		throws com.liferay.portal.kernel.safe.PortalSafeException {
		getService().removeItem(companyId, groupId, name);
	}

	public static void saveItem(long companyId, long groupId,
		com.liferay.portal.kernel.safe.model.Item item)
		throws com.liferay.portal.kernel.safe.PortalSafeException {
		getService().saveItem(companyId, groupId, item);
	}

	public static void clearService() {
		_service = null;
	}

	public static PortalSafeService getService() {
		if (_service == null) {
			InvokableService invokableService = (InvokableService)PortletBeanLocatorUtil.locate(ClpSerializer.getServletContextName(),
					PortalSafeService.class.getName());

			if (invokableService instanceof PortalSafeService) {
				_service = (PortalSafeService)invokableService;
			}
			else {
				_service = new PortalSafeServiceClp(invokableService);
			}

			ReferenceRegistry.registerReference(PortalSafeServiceUtil.class,
				"_service");
		}

		return _service;
	}

	/**
	 * @deprecated
	 */
	public void setService(PortalSafeService service) {
	}

	private static PortalSafeService _service;
}