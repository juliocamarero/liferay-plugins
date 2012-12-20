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

import com.liferay.portal.kernel.io.ByteHeaderInputStream;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletClassLoaderUtil;
import com.liferay.portal.kernel.safe.model.Item;
import com.liferay.portal.kernel.safe.serializer.DuplicateItemSerializerException;
import com.liferay.portal.kernel.safe.serializer.ItemSerializer;
import com.liferay.portal.kernel.safe.serializer.ItemSerializerException;
import com.liferay.portal.kernel.safe.serializer.ItemSerializerRegistry;
import com.liferay.portal.kernel.safe.serializer.NoSuchItemSerializerException;
import com.liferay.portal.kernel.safe.serializer.SerializedItem;
import com.liferay.portal.kernel.util.InstanceFactory;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portalsafe.util.PortletPropsValues;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Implementation of {@link ItemSerializerRegistry} which provides
 * (de)serialization capabilities of portal safe.
 *
 * @author Tomas Polesovsky
 */
public class ItemSerializerRegistryImpl implements ItemSerializerRegistry {

	public ItemSerializerRegistryImpl() {
		_initSerializers();
	}

	/**
	 * Removes serializer from the {@link #_serializers}.
	 *
	 * @param  serializer the serializer to be removed.
	 * @return <code>true</code> if serializer was found in the registry and
	 *         successfully removed, <code>false</code> otherwise
	 */
	public boolean deregisterSerializer(ItemSerializer serializer) {
		return _serializers.remove(serializer);
	}

	/**
	 * Tries to read header from the input stream and then deserialize the rest
	 * of the input stream using one of serializers.
	 *
	 * @param  serializedItem serialized form of item to be read
	 * @return deserialized instance of the serializedItem
	 * @throws ItemSerializerException if the deserialization process fails
	 * @throws NoSuchItemSerializerException if no suitable serializer is found
	 *         to parse the serializedItem
	 */
	public Item read(SerializedItem serializedItem)
		throws ItemSerializerException, NoSuchItemSerializerException {

		if (Validator.isNull(serializedItem)) {
			throw new IllegalArgumentException(
				"Parameter serializedItem must be specified!");
		}

		if (Validator.isNull(serializedItem.getItemStream())) {
			throw new IllegalArgumentException(
				"ItemStream of serializedItem is required!");
		}

		try {
			readMetadata(serializedItem);
		} catch (IOException e) {
			throw new ItemSerializerException(
				"Unable to read serialized item metadata!", e);
		}

		ItemSerializer serializer = getSerializerById(
			serializedItem.getSerializerUID());

		if (serializer == null) {
			long serializerUID = serializedItem.getSerializerUID();
			throw new NoSuchItemSerializerException(
				"Unsupported serializer for: " + serializerUID);
		}

		Item result = serializer.read(serializedItem);

		return result;
	}

	/**
	 * Registers serializer into {@link #_serializers}.
	 *
	 * @param serializer the serializer to be registered
	 */
	public void registerSerializer(ItemSerializer serializer)
		throws DuplicateItemSerializerException {

		if (Validator.isNull(serializer)) {
			throw new IllegalArgumentException(
				"Parameter serializer must be specified!");
		}

		long serializerUID = serializer.getSerializerUID();
		Class serializerClass = serializer.getSupportedClass();

		for (ItemSerializer itemSerializer : _serializers) {
			if ((itemSerializer.getSerializerUID() == serializerUID) ||
				itemSerializer.getSupportedClass().equals(serializerClass)) {

				throw new DuplicateItemSerializerException();
			}
		}

		_serializers.add(serializer);
	}

	/**
	 * Serializes the item into binary form using one of registered serializers.
	 *
	 * @param item the item to be converted into binary form.
	 */
	public SerializedItem write(Item item)
		throws ItemSerializerException, NoSuchItemSerializerException {

		if (Validator.isNull(item)) {
			throw new IllegalArgumentException(
				"Parameter item must be specified!");
		}

		ItemSerializer serializer = getSerializerByClass(item.getClass());

		if (serializer == null) {
			throw new NoSuchItemSerializerException(
				"Unsupported serializer for: " + item.getClass().getName());
		}

		SerializedItem serializedItem = serializer.write(item);
		if (serializedItem == null) {
			throw new ItemSerializerException(
				"Unable to serialize item " + item.getName());
		}

		long serializerUID = serializer.getSerializerUID();
		serializedItem.setSerializerUID(serializerUID);

		writeMetadata(serializedItem);

		return serializedItem;
	}

	/**
	 * Registers serializers specified in {@link
	 * PortletPropsValues#PORTAL_SAFE_SERIALIZER_CLASSES}.
	 */
	protected void _initSerializers() {
		String[] serializersClasses =
			PortletPropsValues.PORTAL_SAFE_SERIALIZER_CLASSES;

		for (String serializerClass : serializersClasses) {
				try {
					ItemSerializer serializer =
					(ItemSerializer) InstanceFactory.newInstance(
						PortletClassLoaderUtil.getClassLoader(),
						serializerClass);

				registerSerializer(serializer);
			} catch (Exception e) {
				_log.warn(
					"Unable to create Portal Safe Item Serializer " +
						serializerClass, e);
			}
		}
	}

	/**
	 * Returns serializer from the registry based on the itemClass
	 *
	 * @param  itemClass key to be used to fetch the serializer using {@link
	 *         ItemSerializer#getSupportedClass()}
	 * @return serializer to be used for serializing the itemClass
	 */
	protected ItemSerializer getSerializerByClass(Class itemClass) {
		for (ItemSerializer itemSerializer : _serializers) {
			if (itemClass.equals(itemSerializer.getSupportedClass())) {
				return itemSerializer;
			}
		}

		return null;
	}

	/**
	 * Returns serializer from the registry based on the serializerUID
	 *
	 * @param  serializerUID key to be used to fetch serializers using {@link
	 *         ItemSerializer#getSerializerUID()}
	 * @return serializer to be used for deserializing
	 */
	protected ItemSerializer getSerializerById(long serializerUID) {
		for (ItemSerializer itemSerializer : _serializers) {
			if (itemSerializer.getSerializerUID() == serializerUID) {
				return itemSerializer;
			}
		}

		return null;
	}

	/**
	 * Loads and sets {@link SerializedItem}'s header from the serializedItem's
	 * stream.
	 *
	 * @param  serializedItem the SerializedItem to be filled
	 * @throws IOException if the stream throws the exception while reading
	 */
	protected void readMetadata(SerializedItem serializedItem)
		throws IOException {

		InputStream rawInputStream = serializedItem.getItemStream();
		DataInputStream dis = new DataInputStream(rawInputStream);
		serializedItem.setItemStream(dis);

		try {
			long serializerUID = dis.readLong();
			serializedItem.setSerializerUID(serializerUID);

			int serializedItemHeaderLen = dis.readInt();
			byte[] serializerItemHeader = new byte[serializedItemHeaderLen];
			dis.readFully(serializerItemHeader);
			serializedItem.setHeader(serializerItemHeader);
		} catch (IOException e) {
			throw new IOException("Unable to read item headers!", e);
		}
	}

	/**
	 * Writes metadata ({@link com.liferay.portal.kernel.safe.serializer
	 * .SerializedItem#getHeader()} into the serializedItem's stream.
	 *
	 * @param serializedItem the serializedItem to be filled
	 */
	protected void writeMetadata(SerializedItem serializedItem) {
		byte[] fullHeader = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);

			dos.writeLong(serializedItem.getSerializerUID());

			dos.writeInt(serializedItem.getHeader().length);
			dos.write(serializedItem.getHeader());

			dos.flush();

			fullHeader = baos.toByteArray();
		} catch (IOException e) {

			// BAOS never throws IOExceptions

		}

		InputStream itemStream =
			new ByteHeaderInputStream(
				serializedItem.getItemStream(), fullHeader);

		serializedItem.setItemStream(itemStream);
	}

	private static Log _log = LogFactoryUtil.getLog(
		ItemSerializerRegistryImpl.class);

	/**
	 * Registry with serializers
	 */
	private Set<ItemSerializer> _serializers =
		new CopyOnWriteArraySet<ItemSerializer>();

}