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

import com.liferay.portal.kernel.safe.model.BinaryItem;
import com.liferay.portal.kernel.safe.serializer.ItemSerializer;
import com.liferay.portal.kernel.safe.serializer.SerializedItem;

import java.io.InputStream;

/**
 * Implements serialization of {@link BinaryItem} into {@link ItemSerializer}.
 *
 * @author Tomas Polesovsky
 */
public class BinaryItemSerializerImpl implements ItemSerializer<BinaryItem> {

	/**
	 * Returns constant {@link #SERIALIZER_UID}.
	 *
	 * @return value of {@link #SERIALIZER_UID}
	 */
	public long getSerializerUID() {
		return SERIALIZER_UID;
	}

	/**
	 * Returns {@link BinaryItem}.
	 *
	 * @return class representation of {@link BinaryItem}
	 */
	public Class getSupportedClass() {
		return BinaryItem.class;
	}

	/**
	 * Converts the serializedItem into the result by copying values.
	 *
	 * @param  serializedItem the item to be deserialized into the BinaryItem
	 * @return new instance of BinaryItem with values copied from the
	 *         serializedItem
	 */
	public BinaryItem read(SerializedItem serializedItem) {
		if (serializedItem == null) {
			throw new IllegalArgumentException(
				"Parameter serializedItem cannot be null!");
		}

		BinaryItem result = new BinaryItem();
		result.setCompanyId(serializedItem.getCompanyId());
		result.setGroupId(serializedItem.getGroupId());
		result.setInputStream(serializedItem.getItemStream());

		return result;
	}

	/**
	 * Serialize provided item into new SerializedItem by copying values.
	 *
	 * @param  item the item to be converted
	 * @return new instance of SerializedItem with existing values from the item
	 */
	public SerializedItem write(BinaryItem item) {
		if (item == null) {
			throw new IllegalArgumentException(
				"Parameter item cannot be null!");
		}

		InputStream inputStream = item.getInputStream();

		SerializedItem result = new SerializedItem();
		result.setCompanyId(item.getCompanyId());
		result.setGroupId(item.getGroupId());
		result.setItemStream(inputStream);

		return result;
	}

	private static final long SERIALIZER_UID = 0;

}