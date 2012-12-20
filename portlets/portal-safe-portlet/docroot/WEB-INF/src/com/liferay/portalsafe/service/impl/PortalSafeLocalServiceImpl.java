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

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletClassLoaderUtil;
import com.liferay.portal.kernel.safe.PortalSafe;
import com.liferay.portal.kernel.safe.PortalSafeException;
import com.liferay.portal.kernel.safe.model.Item;
import com.liferay.portal.kernel.safe.serializer.ItemSerializerRegistryUtil;
import com.liferay.portal.kernel.safe.serializer.SerializedItem;
import com.liferay.portal.kernel.safe.storage.Storage;
import com.liferay.portal.kernel.util.InstanceFactory;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.CompanyConstants;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextThreadLocal;
import com.liferay.portalsafe.ItemNameException;
import com.liferay.portalsafe.PortalSafeConfigurationException;
import com.liferay.portalsafe.service.base.PortalSafeLocalServiceBaseImpl;
import com.liferay.portalsafe.storage.FSStorageImpl;
import com.liferay.portalsafe.util.PortletPropsValues;

import java.util.List;

import org.apache.commons.io.IOUtils;

/**
 * The implementation of the portal safe local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the {@link
 * com.liferay.portalsafe.service.PortalSafeLocalService} interface. <p> This is
 * a local service. Methods of this service will not have security checks based
 * on the propagated JAAS credentials because this service can only be accessed
 * from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see    com.liferay.portalsafe.service.base.PortalSafeLocalServiceBaseImpl
 * @see    com.liferay.portalsafe.service.PortalSafeLocalServiceUtil
 */
public class PortalSafeLocalServiceImpl extends PortalSafeLocalServiceBaseImpl
	implements PortalSafe {

	public PortalSafeLocalServiceImpl() {
		_initStorage();
	}

	/**
	 * Returns registered storage implementation or {@link FSStorageImpl} if
	 * there is no storage registered.
	 *
	 * @return underlying storage
	 */
	public Storage getStorage() {
		if (_storage == null) {
			return new FSStorageImpl();
		}

		return _storage;
	}

	/**
	 * Use registered storage to list the items names.
	 *
	 * @param  companyId ID of company to load items from
	 * @param  groupId ID of group inside the company to load items from
	 * @return not <code>null</code> list of accessible items names. These can
	 *         be later used for loading or removing items from the safe.
	 * @throws PortalSafeException if an other error occurs
	 */
	public List<String> listItemsNames(long companyId, long groupId)
		throws PortalSafeException {

		Storage storage = getStorage();
		if (storage == null) {
			throw new PortalSafeConfigurationException(
				"Portal Safe doesn't have assigned any safe storage!");
		}

		return storage.listNames(companyId, groupId);
	}

	/**
	 * Uses {@link Storage#load(long, long, String)} to load SerializedItem and
	 * then {@link ItemSerializerRegistryUtil#read(SerializedItem)} to obtain
	 * the result.
	 *
	 * @param  companyId ID of company to load items from
	 * @param  groupId ID of group inside the company to load items from
	 * @param  name name of the item to be loaded
	 * @return descendant of {@link Item} interface
	 * @throws PortalSafeException if an other error occurs
	 */
	public Item loadItem(long companyId, long groupId, String name)
		throws PortalSafeException {

		if (Validator.isNull(name)) {
			throw new ItemNameException("Parameter item must be specified!");
		}

		Storage storage = getStorage();
		if (storage == null) {
			throw new IllegalStateException(
				"Portal Safe doesn't have assigned any safe storage!");
		}

		SerializedItem serializedItem = storage.load(companyId, groupId, name);

		if (serializedItem == null) {
			throw new PortalSafeException(
				"Item hasn't been loaded by the underlying Storage!");
		}

		Item result = ItemSerializerRegistryUtil.read(serializedItem);

		if (result == null) {
			throw new PortalSafeException(
				"Item hasn't been deserialized by ItemSerializerRegistryUtil!");
		}

		result.setName(name);

		return result;
	}

	/**
	 * Use {@link Storage#remove(long, long, String)} to remove the item and
	 * then removes Liferay permission resources.
	 *
	 * @param  companyId ID of company to remove the item from
	 * @param  groupId ID of group inside the company to remove the item from
	 * @param  name name of the item to be removed
	 * @throws PortalSafeException if some error occurs during deleting
	 */
	public void removeItem(long companyId, long groupId, String name)
		throws PortalSafeException {

		Storage storage = getStorage();
		if (storage == null) {
			throw new IllegalStateException(
				"Portal Safe doesn't have assigned any safe storage!");
		}

		storage.remove(companyId, groupId, name);

		try {
			resourceLocalService.deleteResource(
				companyId, Item.class.getName(),
				ResourceConstants.SCOPE_INDIVIDUAL, name);

		} catch (Exception e) {
			throw new PortalSafeException(
				"Unable to remove resource for item " + name);
		}
	}

	/**
	 * Use {@link ItemSerializerRegistryUtil#write(Item)} to serialize the item
	 * into serializedItem and then saves the serializedItem using {@link
	 * Storage#save(SerializedItem, String)}. In the end creates resources in
	 * the portal permission system.
	 *
	 * @param  companyId ID of company to save the item to
	 * @param  groupId ID of group inside the company to save the item to
	 * @param  item the item to be saved
	 * @throws PortalSafeException if an other error occurs
	 */
	public void saveItem(long companyId, long groupId, Item item)
		throws PortalSafeException {

		if (Validator.isNull(item)) {
			throw new IllegalArgumentException(
				"Parameter item must be specified!");
		}

		String name = item.getName();

		if (Validator.isNull(name)) {
			throw new ItemNameException("Parameter name must be specified!");
		}

		item.setCompanyId(companyId);
		item.setGroupId(groupId);

		Storage storage = getStorage();
		if (storage == null) {
			throw new PortalSafeException(
				"Portal Safe doesn't have assigned any safe storage!");
		}

		SerializedItem serializedItem = ItemSerializerRegistryUtil.write(item);
		if (serializedItem == null) {
			throw new PortalSafeException(
				"Item hasn't been serialized by ItemSerializerRegistryUtil!");
		}

		serializedItem.setCompanyId(companyId);
		serializedItem.setGroupId(groupId);

		try {
			storage.save(serializedItem, name);
		} finally {
			IOUtils.closeQuietly(serializedItem.getItemStream());
		}

		// Resources

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		long userId = CompanyConstants.SYSTEM;
		if (serviceContext != null) {
			userId = serviceContext.getUserId();
		}
		else {
			PermissionChecker permissionChecker =
				PermissionThreadLocal.getPermissionChecker();

			if (permissionChecker != null) {
				userId = permissionChecker.getUserId();
			}
		}

		try {
			resourceLocalService.addResources(
				companyId, groupId, userId, Item.class.getName(), name, false,
				false, false);

		} catch (Exception e) {
			throw new PortalSafeException(
				"Unable to add resource for item " + name);
		}

	}

	/**
	 * Registers storage to be used
	 */
	public void setStorage(Storage storage) {
		_storage = storage;
	}

	/**
	 * Inits storage from {@link PortletPropsValues#PORTAL_SAFE_STORAGE_CLASS}
	 */
	protected void _initStorage() {
		String storageClass = PortletPropsValues.PORTAL_SAFE_STORAGE_CLASS;

		try {
			_storage =
				(Storage) InstanceFactory.newInstance(
					PortletClassLoaderUtil.getClassLoader(), storageClass);

		} catch (Exception e) {
			_log.warn(
				"Unable to create Portal Safe storage " + storageClass, e);
		}
	}

	private static Log _log = LogFactoryUtil.getLog(
		PortalSafeLocalServiceImpl.class);

	/**
	 * The registered storage
	 */
	private Storage _storage;

}