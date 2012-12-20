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

package com.liferay.portalsafe.portlet;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletResponseUtil;
import com.liferay.portal.kernel.safe.PortalSafeUtil;
import com.liferay.portal.kernel.safe.model.BinaryItem;
import com.liferay.portal.kernel.safe.model.Item;
import com.liferay.portal.kernel.safe.model.KeyStoreItem;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portalsafe.util.CryptoService;
import com.liferay.util.bridges.mvc.MVCPortlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

/**
 * Manipulates data in underlying {@link PortalSafeUtil}. It's the main and only
 * portlet of this plugin.
 *
 * @author Tomas Polesovsky
 */
public class PortalSafePortlet extends MVCPortlet {

	/**
	 * Removes item from portal safe.
	 *
	 * @param request the portlet request
	 * @param response the portlet request
	 */
	public void deleteItem(ActionRequest request, ActionResponse response) {

		try {
			ThemeDisplay themeDisplay =
				(ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);

			long companyId = themeDisplay.getCompanyId();
			long groupId = themeDisplay.getScopeGroupId();

			String preferencesGroupId =
				request.getPreferences().getValue("groupId", StringPool.NULL);

			if (Validator.isNotNull(preferencesGroupId)) {
				groupId = Long.valueOf(preferencesGroupId);
			}

			String itemName = ParamUtil.getString(request, "itemName");

			remove(companyId, groupId, itemName);
		} catch(Exception e) {
			_log.warn(e);
			SessionErrors.add(request, e.getClass());
		}
	}

	/**
	 * Writes binary content of an item into response.
	 *
	 * @param request the portlet request
	 * @param response the portlet response
	 */
	@Override
	public void serveResource(
		ResourceRequest request, ResourceResponse response) {

		try {
			String itemName = request.getResourceID();

			ThemeDisplay themeDisplay =
				(ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);

			long companyId = themeDisplay.getCompanyId();
			long groupId = themeDisplay.getScopeGroupId();

			String preferencesGroupId =
				request.getPreferences().getValue("groupId", StringPool.NULL);

			if (Validator.isNotNull(preferencesGroupId)) {
				groupId = Long.valueOf(preferencesGroupId);
			}

			Item item = PortalSafeUtil.loadItem(companyId, itemName, groupId);

			if (item instanceof BinaryItem) {
				BinaryItem binaryItem = (BinaryItem) item;
				PortletResponseUtil.sendFile(
					request, response, itemName, binaryItem.getInputStream(),
					APPLICATION_OCTET_STREAM);

				return;
			}

			if (item instanceof KeyStoreItem) {
				String storePass = ParamUtil.getString(request, "storePass");
				byte[] storePassPlainTextBytes =
					CryptoService.getInstance().decrypt(
						companyId, Base64.decode(storePass));

				String storePassPlainText =
					new String(storePassPlainTextBytes, StringPool.UTF8);

				KeyStoreItem keyStoreItem = (KeyStoreItem) item;
				KeyStore keyStore = keyStoreItem.getKeyStore();

				ByteArrayOutputStream storeContent =
					new ByteArrayOutputStream();

				keyStore.store(storeContent, storePassPlainText.toCharArray());

				PortletResponseUtil.sendFile(
					request, response, itemName, storeContent.toByteArray(),
					APPLICATION_OCTET_STREAM);

				return;
			}

		} catch (Exception e) {
			_log.warn(e);
		}
	}

	/**
	 * Creates or updates existing item.
	 *
	 * @param request the portlet request
	 * @param response the portlet response
	 */
	public void updateItem(ActionRequest request, ActionResponse response) {

		try {
			UploadPortletRequest uploadPortletRequest =
				PortalUtil.getUploadPortletRequest(request);

			ThemeDisplay themeDisplay =
				(ThemeDisplay) uploadPortletRequest.getAttribute(
					WebKeys.THEME_DISPLAY);

			long companyId = themeDisplay.getCompanyId();
			long groupId = themeDisplay.getScopeGroupId();

			String preferencesGroupId =
				request.getPreferences().getValue("groupId", StringPool.NULL);

			if (Validator.isNotNull(preferencesGroupId)) {
				groupId = Long.valueOf(preferencesGroupId);
			}

			String itemName = ParamUtil.getString(
				uploadPortletRequest, "itemName");

			String oldItemName = ParamUtil.getString(
				uploadPortletRequest, "oldItemName");

			InputStream itemStream = uploadPortletRequest.getFileAsStream(
				"itemStream");

			Long size = uploadPortletRequest.getSize("itemStream");
			if ((size != null) && (size > 0)) {
				save(
					companyId, groupId, uploadPortletRequest, itemName,
					itemStream);
			}

			if (Validator.isNotNull(oldItemName) &&
				!oldItemName.equals(itemName)) {

				if ((size == null) || (size == 0)) {
					copy(companyId, groupId, oldItemName, itemName);
				}

				remove(companyId, groupId, oldItemName);
			}
		} catch (Exception e) {
			_log.warn(e);
			SessionErrors.add(request, e.getClass());
		}

	}

	/**
	 * Copies binary content of the oldItemName and saves under the new itemName
	 * name.
	 *
	 * @param  companyId ID of the company where the oldItemName is stored
	 * @param  groupId ID of the group where the oldItemName is stored
	 * @param  oldItemName name of existing item
	 * @param  itemName name of the new item
	 * @throws PortalException if underlying PortalSafeUtil throws the exception
	 * @throws SystemException if underlying PortalSafeUtil throws the exception
	 */
	private void copy(
		long companyId, long groupId, String oldItemName, String itemName)
		throws PortalException, SystemException {

		Item item = PortalSafeUtil.loadItem(companyId, oldItemName, groupId);

		item.setName(itemName);

		PortalSafeUtil.saveItem(companyId, groupId, item);
	}

	/**
	 * Removes item with the oldItemName from portal safe.
	 *
	 * @param  companyId ID of the company where the oldItemName is stored
	 * @param  groupId ID of the group where the oldItemName is stored
	 * @param  itemName name of the item to be removed
	 * @throws PortalException if underlying PortalSafeUtil throws the exception
	 * @throws SystemException if underlying PortalSafeUtil throws the exception
	 */
	private void remove(long companyId, long groupId, String itemName)
		throws PortalException, SystemException {

		PortalSafeUtil.removeItem(companyId, itemName, groupId);
	}

	/**
	 * Saves new item or updates existing item.
	 *
	 * @param  companyId ID of the company where the oldItemName is stored
	 * @param  groupId ID of the group where the oldItemName is stored
	 * @param  request the portlet request
	 * @param  itemName name of the item to be saved
	 * @param  itemStream binary content of the item
	 * @throws CertificateException if certificates from KeyStore could not be
	 *         loaded
	 * @throws IOException if the exception occures while loading KeyStore
	 * @throws KeyStoreException if KeyStore doesn't support provided provider
	 * @throws NoSuchAlgorithmException if the exception occures while loading
	 *         the KeyStore
	 * @throws NoSuchProviderException if the exception occures while loading
	 *         the KeyStore
	 * @throws PortalException if underlying PortalSafeUtil throws the exception
	 * @throws SystemException if underlying PortalSafeUtil throws the exception
	 */
	private void save(
			long companyId, long groupId, UploadPortletRequest request,
			String itemName, InputStream itemStream)
		throws CertificateException, IOException, KeyStoreException,
		NoSuchAlgorithmException, NoSuchProviderException, PortalException,
		SystemException {

		String itemType = ParamUtil.getString(request, "itemType");

		Item item = null;

		if (itemType.equals(BinaryItem.class.getSimpleName())) {
			BinaryItem binaryItem = new BinaryItem();
			binaryItem.setCompanyId(companyId);
			binaryItem.setGroupId(groupId);
			binaryItem.setInputStream(itemStream);
			item = binaryItem;
		}

		if (itemType.equals(KeyStoreItem.class.getSimpleName())) {
			String storePass = ParamUtil.getString(request, "storePass");
			String storeType = ParamUtil.getString(request, "storeType");
			String storeProvider = ParamUtil.getString(
				request, "storeProvider");

			KeyStore keyStore = KeyStore.getInstance(storeType, storeProvider);
			keyStore.load(itemStream, storePass.toCharArray());

			KeyStoreItem keyStoreItem = new KeyStoreItem();
			keyStoreItem.setCompanyId(companyId);
			keyStoreItem.setGroupId(groupId);
			keyStoreItem.setKeyStore(keyStore);
			item = keyStoreItem;
		}

		item.setName(itemName);

		PortalSafeUtil.saveItem(companyId, groupId, item);
	}

	private static final String APPLICATION_OCTET_STREAM =
		"application/octet-stream";

	private static Log _log = LogFactoryUtil.getLog(PortalSafePortlet.class);

}