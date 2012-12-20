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

import com.liferay.portal.service.ServiceWrapper;

/**
 * <p>
 * This class is a wrapper for {@link PortalSafeService}.
 * </p>
 *
 * @author    Tomáš Polešovský
 * @see       PortalSafeService
 * @generated
 */
public class PortalSafeServiceWrapper implements PortalSafeService,
	ServiceWrapper<PortalSafeService> {
	public PortalSafeServiceWrapper(PortalSafeService portalSafeService) {
		_portalSafeService = portalSafeService;
	}

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	public java.lang.String getBeanIdentifier() {
		return _portalSafeService.getBeanIdentifier();
	}

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	public void setBeanIdentifier(java.lang.String beanIdentifier) {
		_portalSafeService.setBeanIdentifier(beanIdentifier);
	}

	public java.lang.Object invokeMethod(java.lang.String name,
		java.lang.String[] parameterTypes, java.lang.Object[] arguments)
		throws java.lang.Throwable {
		return _portalSafeService.invokeMethod(name, parameterTypes, arguments);
	}

	/**
	* NOTE FOR DEVELOPERS:
	*
	* Never reference this interface directly. Always use {@link
	* com.liferay.portalsafe.service.PortalSafeServiceUtil} to access the
	* portal safe remote service.
	*/
	public java.util.List<java.lang.String> listItemsNames(long companyId,
		long groupId) throws com.liferay.portal.kernel.safe.PortalSafeException {
		return _portalSafeService.listItemsNames(companyId, groupId);
	}

	public com.liferay.portal.kernel.safe.model.Item loadItem(long companyId,
		long groupId, java.lang.String name)
		throws com.liferay.portal.kernel.safe.PortalSafeException {
		return _portalSafeService.loadItem(companyId, groupId, name);
	}

	public void removeItem(long companyId, long groupId, java.lang.String name)
		throws com.liferay.portal.kernel.safe.PortalSafeException {
		_portalSafeService.removeItem(companyId, groupId, name);
	}

	public void saveItem(long companyId, long groupId,
		com.liferay.portal.kernel.safe.model.Item item)
		throws com.liferay.portal.kernel.safe.PortalSafeException {
		_portalSafeService.saveItem(companyId, groupId, item);
	}

	/**
	 * @deprecated Renamed to {@link #getWrappedService}
	 */
	public PortalSafeService getWrappedPortalSafeService() {
		return _portalSafeService;
	}

	/**
	 * @deprecated Renamed to {@link #setWrappedService}
	 */
	public void setWrappedPortalSafeService(PortalSafeService portalSafeService) {
		_portalSafeService = portalSafeService;
	}

	public PortalSafeService getWrappedService() {
		return _portalSafeService;
	}

	public void setWrappedService(PortalSafeService portalSafeService) {
		_portalSafeService = portalSafeService;
	}

	private PortalSafeService _portalSafeService;
}