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

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.safe.model.NoSuchItemException;
import com.liferay.portal.kernel.safe.storage.StorageException;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.Randomizer;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portalsafe.ItemNameException;
import com.liferay.portalsafe.util.PropsKeys;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

/**
 * @author Tomas Polesovsky
 */
public class FSStorageImpl extends BaseEncryptingStorage {

	/**
	 * Returns full file name based on the companyId, groupId and the name
	 *
	 * @param  companyId ID of the company where to store the file
	 * @param  groupId ID of the group where to store the file
	 * @param  name name to be used for the file
	 * @return <code>{@link #getFileNamePrefix(long, long)}name{@link
	 *         #ENCRYPTED_EXT}</code>
	 */
	protected String getFileName(long companyId, long groupId, String name)
		throws ItemNameException {

		if (!Validator.isFileName(name)) {
			throw new ItemNameException("Cannot save unsafe file: " + name);
		}

		StringBundler sb = new StringBundler(3);
		sb.append(getFileNamePrefix(companyId, groupId));
		sb.append(name);
		sb.append(ENCRYPTED_EXT);

		return sb.toString();
	}

	/**
	 * Creates file name prefix based on the companyId and the groupId
	 *
	 * @param  companyId ID of the company where to store the file
	 * @param  groupId ID of the group where to store the file
	 * @return <code>companyId-groupId-</code>
	 */
	protected String getFileNamePrefix(long companyId, long groupId) {
		StringBundler sb = new StringBundler(4);
		sb.append(companyId);
		sb.append(StringPool.DASH);
		sb.append(groupId);
		sb.append(StringPool.DASH);
		return sb.toString();
	}

	/**
	 * Return all files from the {@link #PORTAL_SAFE_DIRECTORY} which start with
	 * {@link #getFileNamePrefix(long, long)}
	 *
	 * @see com.liferay.portal.kernel.safe.storage.Storage#listNames(long, long)
	 */
	@Override
	protected List<String> listNamesEncrypted(long companyId, long groupId) {
		final String fileNamePrefix = getFileNamePrefix(companyId, groupId);
		String[] filesNames = PORTAL_SAFE_DIRECTORY.list(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.startsWith(fileNamePrefix);
			}
		});

		List<String> result = new ArrayList<String>(filesNames.length);
		int startPos = fileNamePrefix.length();
		for (String fileName : filesNames) {
			int endPos = fileName.length() - ENCRYPTED_EXT.length();
			result.add(fileName.substring(startPos, endPos));
		}

		return result;
	}

	/**
	 * Returns FileInputStream of file with name obtained using {@link
	 * #getFileName(long, long, String)}
	 *
	 * @see com.liferay.portal.kernel.safe.storage.Storage#load(long, long,
	 *      String)
	 */
	@Override
	protected InputStream loadEncrypted(
		long companyId, long groupId, String encryptedName)
		throws NoSuchItemException, StorageException {

		try {
			String fileName = getFileName(companyId, groupId, encryptedName);
			File file = new File(PORTAL_SAFE_DIRECTORY, fileName);

			return new FileInputStream(file);
		} catch (FileNotFoundException e) {

			// remove encrypted file name from the message

			throw new NoSuchItemException();
		}
	}

	/**
	 * Removes file from disk with name obtained using {@link #getFileName(long,
	 * long, String)}. Before deleting tries to rewrite the file content with
	 * random bytes.
	 *
	 * @see com.liferay.portal.kernel.safe.storage.Storage#remove(long, long,
	 *      String)
	 */
	@Override
	protected boolean removeEncrypted(
		long companyId, long groupId, String encryptedName)
		throws NoSuchItemException, StorageException {

		String fileName = getFileName(companyId, groupId, encryptedName);
		File file = new File(PORTAL_SAFE_DIRECTORY, fileName);

		if (!file.exists()) {

			// remove encrypted file name from the message

			throw new NoSuchItemException();
		}

		// rewrite file with a random data

		try {
			long fileSize = file.length();
			FileOutputStream fos = new FileOutputStream(file);
			try {
				byte[] buff = new byte[8192];
				for (int i = 0; i < fileSize / buff.length; i++) {
					Randomizer.getInstance().nextBytes(buff);
					fos.write(buff);
				}

				fos.write(buff, 0, (int) (fileSize % buff.length));
			} finally {
				IOUtils.closeQuietly(fos);
			}
		} catch (IOException e) {
			_log.warn("Unable to rewrite file with random data!", e);
		}

		return file.delete();
	}

	/**
	 * Writes the inputStream into file with name obtained using {@link
	 * #getFileName(long, long, String)}
	 *
	 * @see com.liferay.portal.kernel.safe.storage.Storage#save(
	 *      com.liferay.portal.kernel.safe.serializer.SerializedItem, String)
	 */
	@Override
	protected boolean saveEncrypted(
		long companyId, long groupId, String encryptedName,
		InputStream inputStream)
		throws StorageException {

		String fileName = getFileName(companyId, groupId, encryptedName);
		File file = new File(PORTAL_SAFE_DIRECTORY, fileName);

		try {
			FileUtil.write(file, inputStream);

			return true;
		} catch (IOException e) {
			throw new ItemNameException(e);
		}
	}

	/**
	 * Extension for encrypted files
	 */
	private static final String ENCRYPTED_EXT = ".encrypted";

	/**
	 * Directory for portal safe
	 */
	private static final File PORTAL_SAFE_DIRECTORY =
		new File(PropsUtil.get(PropsKeys.LIFERAY_HOME) + "/data/portal_safe");

	private static Log _log = LogFactoryUtil.getLog(FSStorageImpl.class);

}