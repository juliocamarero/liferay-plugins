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
import com.liferay.portal.service.InvokableLocalService;

/**
 * The utility for the portal safe local service. This utility wraps {@link com.liferay.portalsafe.service.impl.PortalSafeLocalServiceImpl} and is the primary access point for service operations in application layer code running on the local server.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Tomáš Polešovský
 * @see PortalSafeLocalService
 * @see com.liferay.portalsafe.service.base.PortalSafeLocalServiceBaseImpl
 * @see com.liferay.portalsafe.service.impl.PortalSafeLocalServiceImpl
 * @generated
 */
public class PortalSafeLocalServiceUtil {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to {@link com.liferay.portalsafe.service.impl.PortalSafeLocalServiceImpl} and rerun ServiceBuilder to regenerate this class.
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
	* Returns registered storage implementation or {@link FSStorageImpl} if
	* there is no storage registered.
	*
	* @return underlying storage
	*/
	public static com.liferay.portal.kernel.safe.storage.Storage getStorage() {
		return getService().getStorage();
	}

	/**
	* Use registered storage to list the items names.
	*
	* @param companyId id of company to load items from
	* @param groupId id of group inside the company to load items from
	* @return not null list of accessible items names. These can be later used
	for loading or removing items from the safe.
	* @throws com.liferay.portal.kernel.safe.storage.StorageException if there
	is a problem with loading the item from the underlying storage
	* @throws PortalSafeException if an other error occurs
	*/
	public static java.util.List<java.lang.String> listItemsNames(
		long companyId, long groupId)
		throws com.liferay.portal.kernel.safe.PortalSafeException {
		return getService().listItemsNames(companyId, groupId);
	}

	/**
	* Uses {@link Storage#load(long, long, String)} to load SerializedItem and
	* then {@link ItemSerializerRegistryUtil#read(SerializedItem)} to obtain
	* the result.
	*
	* @param companyId id of company to load items from
	* @param groupId id of group inside the company to load items from
	* @param name name of the item to be loaded
	* @return descendant of {@link Item} interface
	* @throws com.liferay.portal.kernel.safe.model.NoSuchItemException if
	there isn't any item found with the name
	* @throws com.liferay.portal.kernel.safe.serializer.NoSuchItemSerializerException
	if there is no serializer registered that is able to read the
	item
	* @throws com.liferay.portal.kernel.safe.storage.StorageException if there
	is a problem with loading the item from the underlying storage
	* @throws PortalSafeException if an other error occurs
	*/
	public static com.liferay.portal.kernel.safe.model.Item loadItem(
		long companyId, long groupId, java.lang.String name)
		throws com.liferay.portal.kernel.safe.PortalSafeException {
		return getService().loadItem(companyId, groupId, name);
	}

	/**
	* Use {@link Storage#remove(long, long, String)} to remove the item and
	* then removes Liferay permission resources.
	*
	* @param companyId id of company to remove the item from
	* @param groupId id of group inside the company to remove the item from
	* @param name name of the item to be removed
	* @throws com.liferay.portal.kernel.safe.model.NoSuchItemException if
	there isn't any item found with the name
	* @throws com.liferay.portal.kernel.safe.storage.StorageException if there
	is a problem with removing the item from the underlying storage
	* @throws PortalSafeException if some error occurs during deleting
	*/
	public static void removeItem(long companyId, long groupId,
		java.lang.String name)
		throws com.liferay.portal.kernel.safe.PortalSafeException {
		getService().removeItem(companyId, groupId, name);
	}

	/**
	* Use {@link ItemSerializerRegistryUtil#write(Item)} to serialize the item
	* into serializedItem and then saves the serializedItem using
	* {@link Storage#save(SerializedItem, String)}. In the end creates
	* resources in the portal permission system.
	*
	* @param companyId id of company to save the item to
	* @param groupId id of group inside the company to save the item to
	* @param item the item to be saved
	* @throws com.liferay.portal.kernel.safe.serializer.NoSuchItemSerializerException
	if there is no serializer registered that is able to convert the
	item into its binary form
	* @throws com.liferay.portal.kernel.safe.storage.StorageException if there
	is a problem with saving the item into the underlying storage
	* @throws PortalSafeException if an other error occurs
	*/
	public static void saveItem(long companyId, long groupId,
		com.liferay.portal.kernel.safe.model.Item item)
		throws com.liferay.portal.kernel.safe.PortalSafeException {
		getService().saveItem(companyId, groupId, item);
	}

	/**
	* Registers storage to be used
	*
	* @param storage
	*/
	public static void setStorage(
		com.liferay.portal.kernel.safe.storage.Storage storage) {
		getService().setStorage(storage);
	}

	public static void clearService() {
		_service = null;
	}

	public static PortalSafeLocalService getService() {
		if (_service == null) {
			InvokableLocalService invokableLocalService = (InvokableLocalService)PortletBeanLocatorUtil.locate(ClpSerializer.getServletContextName(),
					PortalSafeLocalService.class.getName());

			if (invokableLocalService instanceof PortalSafeLocalService) {
				_service = (PortalSafeLocalService)invokableLocalService;
			}
			else {
				_service = new PortalSafeLocalServiceClp(invokableLocalService);
			}

			ReferenceRegistry.registerReference(PortalSafeLocalServiceUtil.class,
				"_service");
		}

		return _service;
	}

	/**
	 * @deprecated
	 */
	public void setService(PortalSafeLocalService service) {
	}

	private static PortalSafeLocalService _service;
}