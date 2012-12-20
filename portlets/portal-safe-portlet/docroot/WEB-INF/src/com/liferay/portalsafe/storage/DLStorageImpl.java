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

package com.liferay.portalsafe.storage;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.safe.model.NoSuchItemException;
import com.liferay.portal.kernel.safe.storage.StorageException;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.CompanyConstants;
import com.liferay.portalsafe.ItemNameException;
import com.liferay.portlet.documentlibrary.NoSuchDirectoryException;
import com.liferay.portlet.documentlibrary.NoSuchFileException;
import com.liferay.portlet.documentlibrary.store.DLStoreUtil;

import java.io.InputStream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Saves and loads files using Document Library
 *
 * @author Tomas Polesovsky
 */
public class DLStorageImpl extends BaseEncryptingStorage {

	/**
	 * Returns directory in which are files stored.
	 *
	 * @param  companyId omitted
	 * @param  groupId used for
	 * @return directory <code>{@link #PORTAL_SAFE}/groupId</code>
	 */
	protected String getDirectoryName(long companyId, long groupId) {
		StringBundler sb = new StringBundler(3);
		sb.append(PORTAL_SAFE);
		sb.append(StringPool.SLASH);
		sb.append(groupId);

		return sb.toString();
	}

	/**
	 * Returns file name with full path for Document Library.
	 *
	 * @param  companyId ID of the company where to store the file
	 * @param  groupId ID of the group where to store the file
	 * @param  name name of the file
	 * @return <code>{@link #getDirectoryName(long, long)}/name.{@link
	 *         #ENCRYPTED_EXT}</code>
	 */
	protected String getFileName(long companyId, long groupId, String name)
		throws PortalException {
		if (Validator.isNull(name)) {
			throw new ItemNameException(
				"Parameter itemName must not be empty!");
		}

		StringBundler sb = new StringBundler(4);
		sb.append(getDirectoryName(companyId, groupId));
		sb.append(StringPool.SLASH);
		sb.append(name);
		sb.append(ENCRYPTED_EXT);

		return sb.toString();
	}

	/**
	 * List names in {@link #getDirectoryName(long, long)} directory using
	 * {@link DLStoreUtil}.
	 *
	 * @see com.liferay.portal.kernel.safe.storage.Storage#listNames(long, long)
	 */
	@Override
	protected List<String> listNamesEncrypted(long companyId, long groupId)
		throws StorageException {

		String directory = getDirectoryName(companyId, groupId);

		String[] names;
		try {
			names = DLStoreUtil.getFileNames(companyId, STORAGE_ID, directory);
		} catch (NoSuchDirectoryException e) {
			return Collections.emptyList();
		} catch (PortalException e) {
			throw new StorageException(e);
		} catch (SystemException e) {
			throw new StorageException(e);
		}

		ArrayList<String> result = new ArrayList<String>(names.length);
		for (String name : names) {
			int startPos = name.lastIndexOf(CharPool.SLASH) + 1;
			int endPos = name.length() - ENCRYPTED_EXT.length();
			String truncatedName = name.substring(startPos, endPos);
			result.add(truncatedName);
		}

		return result;

	}

	/**
	 * Constructs correct file name using {@link #getFileName(long, long,
	 * String)} and loads file using {@link DLStoreUtil}.
	 *
	 * @see com.liferay.portal.kernel.safe.storage.Storage#load(long, long,
	 *      String)
	 */
	@Override
	protected InputStream loadEncrypted(
		long companyId, long groupId, String name)
		throws NoSuchItemException, StorageException {

		try {
			String fileName = getFileName(companyId, groupId, name);

			return DLStoreUtil.getFileAsStream(companyId, STORAGE_ID, fileName);

		} catch(NoSuchFileException e) {

			// remove encrypted file name from the message

			throw new NoSuchItemException();
		} catch (PortalException e) {
			throw new StorageException(e);
		} catch (SystemException e) {
			throw new StorageException(e);
		}

	}

	/**
	 * Remove file from {@link DLStoreUtil} using {@link #getFileName(long,
	 * long, String)}
	 *
	 * @see com.liferay.portal.kernel.safe.storage.Storage#remove(long, long,
	 *      String)
	 */
	@Override
	protected boolean removeEncrypted(long companyId, long groupId, String name)
		throws NoSuchItemException, StorageException {

		try {
			String fileName = getFileName(companyId, groupId, name);

			DLStoreUtil.deleteFile(companyId, STORAGE_ID, fileName);
			return true;
		} catch(NoSuchFileException e) {

			// remove encrypted file name from the message

			throw new NoSuchItemException();
		} catch (PortalException e) {
			throw new StorageException(e);
		} catch (SystemException e) {
			throw new StorageException(e);
		}
	}

	/**
	 * Constructs correct file name using {@link #getFileName(long, long,
	 * String)} and saves file using {@link DLStoreUtil}. If Document Library
	 * already has the file, then removes previous file and creates a new one.
	 *
	 * @see com.liferay.portal.kernel.safe.storage.Storage#save(
	 *      com.liferay.portal.kernel.safe.serializer.SerializedItem, String)
	 */
	@Override
	protected boolean saveEncrypted(
		long companyId, long groupId, String name, InputStream inputStream)
		throws StorageException {

		try {
			String fileName = getFileName(companyId, groupId, name);

			if (DLStoreUtil.hasFile(companyId, STORAGE_ID, fileName)) {
				DLStoreUtil.deleteFile(companyId, STORAGE_ID, fileName);
			}

			DLStoreUtil.addFile(
				companyId, STORAGE_ID, fileName, false, inputStream);

			return true;
		} catch (Exception e) {
			throw new StorageException(e);
		}
	}

	/**
	 * Extension for encrypted files
	 */
	private static final String ENCRYPTED_EXT = ".encrypted";

	/**
	 * Portal Safe directory in Document Library
	 */
	private static final String PORTAL_SAFE = "portal_safe";

	/**
	 * Document Library storage - uses {@link CompanyConstants#SYSTEM}
	 */
	private static final long STORAGE_ID = CompanyConstants.SYSTEM;

}