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

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.transaction.Isolation;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.service.BaseLocalService;
import com.liferay.portal.service.InvokableLocalService;

/**
 * The interface for the portal safe local service.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Tomáš Polešovský
 * @see PortalSafeLocalServiceUtil
 * @see com.liferay.portalsafe.service.base.PortalSafeLocalServiceBaseImpl
 * @see com.liferay.portalsafe.service.impl.PortalSafeLocalServiceImpl
 * @generated
 */
@Transactional(isolation = Isolation.PORTAL, rollbackFor =  {
	PortalException.class, SystemException.class})
public interface PortalSafeLocalService extends BaseLocalService,
	InvokableLocalService {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link PortalSafeLocalServiceUtil} to access the portal safe local service. Add custom service methods to {@link com.liferay.portalsafe.service.impl.PortalSafeLocalServiceImpl} and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	public java.lang.String getBeanIdentifier();

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	public void setBeanIdentifier(java.lang.String beanIdentifier);

	public java.lang.Object invokeMethod(java.lang.String name,
		java.lang.String[] parameterTypes, java.lang.Object[] arguments)
		throws java.lang.Throwable;

	/**
	* Returns registered storage implementation or {@link FSStorageImpl} if
	* there is no storage registered.
	*
	* @return underlying storage
	*/
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public com.liferay.portal.kernel.safe.storage.Storage getStorage();

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
	public java.util.List<java.lang.String> listItemsNames(long companyId,
		long groupId) throws com.liferay.portal.kernel.safe.PortalSafeException;

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
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public com.liferay.portal.kernel.safe.model.Item loadItem(long companyId,
		long groupId, java.lang.String name)
		throws com.liferay.portal.kernel.safe.PortalSafeException;

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
	public void removeItem(long companyId, long groupId, java.lang.String name)
		throws com.liferay.portal.kernel.safe.PortalSafeException;

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
	public void saveItem(long companyId, long groupId,
		com.liferay.portal.kernel.safe.model.Item item)
		throws com.liferay.portal.kernel.safe.PortalSafeException;

	/**
	* Registers storage to be used
	*
	* @param storage
	*/
	public void setStorage(
		com.liferay.portal.kernel.safe.storage.Storage storage);
}