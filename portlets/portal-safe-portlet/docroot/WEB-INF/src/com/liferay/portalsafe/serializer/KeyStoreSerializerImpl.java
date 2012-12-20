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

package com.liferay.portalsafe.serializer;

import com.liferay.portal.kernel.safe.model.KeyStoreItem;
import com.liferay.portal.kernel.safe.serializer.ItemSerializer;
import com.liferay.portal.kernel.safe.serializer.ItemSerializerException;
import com.liferay.portal.kernel.safe.serializer.SerializedItem;
import com.liferay.portalsafe.util.CryptoService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import java.security.KeyStore;

/**
 * Implements serialization of {@link KeyStoreItem} into {@link ItemSerializer}.
 *
 * @author Tomas Polesovsky
 */
public class KeyStoreSerializerImpl implements ItemSerializer<KeyStoreItem> {

	/**
	 * Returns constant {@link #SERIALIZER_UID}.
	 *
	 * @return value of {@link #SERIALIZER_UID}
	 */
	public long getSerializerUID() {
		return SERIALIZER_UID;
	}

	/**
	 * Returns {@link KeyStoreItem}.
	 *
	 * @return class representation of {@link KeyStoreItem}
	 */
	public Class getSupportedClass() {
		return KeyStoreItem.class;
	}

	/**
	 * Reads content of the serializedItem and tries to create a new {@link
	 * KeyStoreItem} with loaded {@link KeyStore}.
	 *
	 * <p>
	 * Uses {@link SerializedItem#getHeader()} to obtain KeyStore type and
	 * creates new KeyStore using {@link KeyStore#load(java.io.InputStream,
	 * char[])}.
	 * </p>
	 *
	 * @param  serializedItem the serialized item to be converted
	 * @return deserialized item with KeyStore
	 * @throws ItemSerializerException if the serializedItem is
	 *         <code>null</code>, itemStream is <code>null</code> or if fails
	 *         KeyStore instantiation or loading from the input stream.
	 */
	public KeyStoreItem read(SerializedItem serializedItem)
		throws ItemSerializerException {

		if (serializedItem == null) {
			throw new IllegalArgumentException(
				"Parameter serializedItem cannot be null!");
		}

		if (serializedItem.getItemStream() == null) {
			throw new IllegalArgumentException("Item stream cannot be null!");
		}

		String keyStoreType = new String(serializedItem.getHeader());
		InputStream is = serializedItem.getItemStream();
		long companyId = serializedItem.getCompanyId();

		KeyStore keyStore = null;
		char[] password = null;
		try {
			password = CryptoService.getInstance().getPassword(companyId);
			keyStore = KeyStore.getInstance(keyStoreType);
			keyStore.load(is, password);
		} catch (Exception e) {
			throw new ItemSerializerException(
				"Unable to initialize keystore", e);
		} finally {
			CryptoService.getInstance().clearPassword(password);
		}

		KeyStoreItem result = new KeyStoreItem();
		result.setCompanyId(companyId);
		result.setGroupId(serializedItem.getGroupId());
		result.setKeyStore(keyStore);

		return result;
	}

	/**
	 * Serializes the item into InputStream using {@link
	 * KeyStore#store(java.io.OutputStream stream, char[] password)} method.
	 *
	 * <p>
	 * {@link com.liferay.portalsafe.util.CryptoService#getPassword(long)} is
	 * used to store internal representation of the KeyStore into stream.
	 * </p>
	 *
	 * <p>
	 * KeyStore type is written into SerializedItem header to be used for
	 * deserialization.
	 * </p>
	 *
	 * @param  item the item with KeyStore to be serialized
	 * @return the serialized item with KeyStore saved as InputStream
	 * @throws ItemSerializerException if the item is <code>null</code>, if
	 *         {@link KeyStoreItem#getKeyStore()} is <code>null</code> or if
	 *         serialization of the KeyStore fails
	 */
	public SerializedItem write(KeyStoreItem item)
		throws ItemSerializerException {

		if (item == null) {
			throw new IllegalArgumentException(
				"Parameter item cannot be null!");
		}

		if (item.getKeyStore() == null) {
			throw new IllegalArgumentException(
				"KeyStore attribute cannot be null!");
		}

		KeyStore keyStore = item.getKeyStore();
		String keyStoreType = keyStore.getType();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		char[] password = null;
		try {
			password =
				CryptoService.getInstance().getPassword(item.getCompanyId());

			keyStore.store(baos, password);
		} catch (Exception e) {
			throw new ItemSerializerException(e);
		} finally {
			CryptoService.getInstance().clearPassword(password);
		}

		InputStream is = new ByteArrayInputStream(baos.toByteArray());

		SerializedItem result = new SerializedItem();
		result.setCompanyId(item.getCompanyId());
		result.setGroupId(item.getGroupId());
		result.setHeader(keyStoreType.getBytes());
		result.setItemStream(is);

		return result;
	}

	private static final long SERIALIZER_UID = 1;

}