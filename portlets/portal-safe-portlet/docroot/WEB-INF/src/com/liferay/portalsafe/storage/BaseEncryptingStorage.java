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

import com.liferay.portal.kernel.io.ByteHeaderInputStream;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.safe.model.NoSuchItemException;
import com.liferay.portal.kernel.safe.serializer.SerializedItem;
import com.liferay.portal.kernel.safe.storage.Storage;
import com.liferay.portal.kernel.safe.storage.StorageException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portalsafe.EncryptedContentCorruptedException;
import com.liferay.portalsafe.util.CryptoService;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base32;

/**
 * Base storage implementation that encrypts stream and file name. Implementing
 * class should override only <code>*Encrypted</code> methods.
 *
 * @author Tomas Polesovsky
 */
public abstract class BaseEncryptingStorage implements Storage {

	/**
	 * List encoded names using {@link #listNamesEncrypted(long, long)} and
	 * decrypt each name using {@link #decryptFileName(long, String)}.
	 */
	public List<String> listNames(long companyId, long groupId)
		throws StorageException {

		List<String> encodedNames = listNamesEncrypted(companyId, groupId);
		List<String> result = new ArrayList<String>(encodedNames.size());

		for (String encodedName : encodedNames) {
			try {
				result.add(decryptFileName(companyId, encodedName));
			} catch (Exception e) {
				_log.warn("Unable to decrypt file name " + encodedName, e);
			}
		}

		Collections.sort(result);

		return result;
	}

	/**
	 * Loads item's stream using encrypted name and verify checksum against the
	 * companyId.
	 */
	public SerializedItem load(long companyId, long groupId, String name)
		throws NoSuchItemException, StorageException {

		try {
			String encryptedName = encryptFileName(companyId, name);

			InputStream encryptedStream = loadEncrypted(
				companyId, groupId, encryptedName);

			InputStream decryptingStream =
				CryptoService.getInstance().getCipherStream(
					Cipher.DECRYPT_MODE, companyId, encryptedStream);

			DataInputStream dataInputStream =
				new DataInputStream(decryptingStream);

			long companyIdChecksum = dataInputStream.readLong();

			if (companyIdChecksum != companyId) {
				throw new EncryptedContentCorruptedException(
					"Corrupted content of " + name);
			}

			SerializedItem serializedItem = new SerializedItem();
			serializedItem.setCompanyId(companyId);
			serializedItem.setGroupId(groupId);
			serializedItem.setItemStream(dataInputStream);

			return serializedItem;
		} catch (Exception e) {
			throw new StorageException(e);
		}
	}

	/**
	 * Encrypts the name and calls {@link #removeEncrypted(long, long, String)}
	 */
	public void remove(long companyId, long groupId, String name)
		throws NoSuchItemException, StorageException {

		try {
			String encryptedName = encryptFileName(companyId, name);

			removeEncrypted(companyId, groupId, encryptedName);

		} catch (Exception e) {
			throw new StorageException(e);
		}
	}

	/**
	 * Prepends companyId as a checksum value, creates encrypting
	 * CihperInputStream and saves the serializedItem stream using encrypted
	 * name.
	 */
	public void save(SerializedItem serializedItem, String name)
		throws StorageException {

		long companyId = serializedItem.getCompanyId();
		long groupId = serializedItem.getGroupId();
		InputStream inputStream = serializedItem.getItemStream();

		try {
			String encryptedName = encryptFileName(companyId, name);

			// prepend companyId as a checksum

			ByteArrayOutputStream baos = new ByteArrayOutputStream(8);
			DataOutputStream daos = new DataOutputStream(baos);
			daos.writeLong(companyId);
			daos.close();
			byte[] companyIdBytes = baos.toByteArray();

			ByteHeaderInputStream inputStreamWithChecksum =
				new ByteHeaderInputStream(inputStream, companyIdBytes);

			InputStream encryptingStream =
				CryptoService.getInstance().getCipherStream(
					Cipher.ENCRYPT_MODE, companyId, inputStreamWithChecksum);

			saveEncrypted(companyId, groupId, encryptedName, encryptingStream);

		} catch (Exception e) {
			throw new StorageException(e);
		}
	}

	/**
	 * Decode the fileName using {@link Base32} encoding and decrypt bytes using
	 * {@link CryptoService#decrypt(long, byte[])}.
	 *
	 * @param  companyId ID of company to be used to get and decrypt secret key
	 * @param  fileName the name to be processed
	 * @return plain text file name
	 * @throws Exception if CryptoService throws the exception
	 */
	protected String decryptFileName(long companyId, String fileName)
		throws Exception {

		byte[] encryptedNameBytes = new Base32().decode(fileName);

		byte[] decryptedNameBytes =
			CryptoService.getInstance().decrypt(companyId, encryptedNameBytes);

		return new String(decryptedNameBytes, StringPool.UTF8);
	}

	/**
	 * Encrypts the fileName into bytes using {@link CryptoService#encrypt(long,
	 * byte[])} and encode bytes into {@link Base32} encoding, which is safe for
	 * file names.
	 *
	 * @param  companyId ID of company to be used to get and decrypt secret key
	 * @param  fileName the name to be processed
	 * @return encrypted and base32 encoded file name
	 * @throws Exception if CryptoService throws the exception
	 */
	protected String encryptFileName(long companyId, String fileName)
		throws Exception {

		byte[] encryptedNameBytes =
			CryptoService.getInstance().encrypt(
				companyId, fileName.getBytes(StringPool.UTF8));

		// use file name safe Base32 encoding

		String encryptedName = new Base32().encodeAsString(encryptedNameBytes);

		return encryptedName;
	}

	/**
	 * To be implemented by extending class.
	 *
	 * @see Storage#listNames(long, long)
	 */
	protected abstract List<String> listNamesEncrypted(
		long companyId, long groupId)
		throws StorageException;

	/**
	 * To be implemented by extending class.
	 *
	 * @see Storage#load(long, long, String)
	 */
	protected abstract InputStream loadEncrypted(
		long companyId, long groupId, String encryptedName)
		throws NoSuchItemException, StorageException;

	/**
	 * To be implemented by extending class.
	 *
	 * @see Storage#remove(long, long, String)
	 */
	protected abstract boolean removeEncrypted(
		long companyId, long groupId, String encryptedName)
		throws NoSuchItemException, StorageException;

	/**
	 * To be implemented by extending class.
	 *
	 * @see Storage#save(SerializedItem, String)
	 */
	protected abstract boolean saveEncrypted(
		long companyId, long groupId, String encryptedName,
		InputStream inputStream)
		throws StorageException;

	private static Log _log = LogFactoryUtil.getLog(
		BaseEncryptingStorage.class);

}