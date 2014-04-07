/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package com.liferay.wsrp.model.impl;

import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.lar.StagedModelType;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.impl.BaseModelImpl;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.PortalUtil;

import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.expando.util.ExpandoBridgeFactoryUtil;

import com.liferay.wsrp.model.WSRPConsumer;
import com.liferay.wsrp.model.WSRPConsumerModel;

import java.io.Serializable;

import java.sql.Types;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * The base model implementation for the WSRPConsumer service. Represents a row in the &quot;WSRP_WSRPConsumer&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface {@link com.liferay.wsrp.model.WSRPConsumerModel} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link WSRPConsumerImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see WSRPConsumerImpl
 * @see com.liferay.wsrp.model.WSRPConsumer
 * @see com.liferay.wsrp.model.WSRPConsumerModel
 * @generated
 */
public class WSRPConsumerModelImpl extends BaseModelImpl<WSRPConsumer>
	implements WSRPConsumerModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a w s r p consumer model instance should use the {@link com.liferay.wsrp.model.WSRPConsumer} interface instead.
	 */
	public static final String TABLE_NAME = "WSRP_WSRPConsumer";
	public static final Object[][] TABLE_COLUMNS = {
			{ "uuid_", Types.VARCHAR },
			{ "wsrpConsumerId", Types.BIGINT },
			{ "companyId", Types.BIGINT },
			{ "createDate", Types.TIMESTAMP },
			{ "modifiedDate", Types.TIMESTAMP },
			{ "name", Types.VARCHAR },
			{ "url", Types.VARCHAR },
			{ "wsdl", Types.CLOB },
			{ "registrationContextString", Types.CLOB },
			{ "registrationPropertiesString", Types.VARCHAR },
			{ "forwardCookies", Types.VARCHAR },
			{ "forwardHeaders", Types.VARCHAR },
			{ "markupCharacterSets", Types.VARCHAR }
		};
	public static final String TABLE_SQL_CREATE = "create table WSRP_WSRPConsumer (uuid_ VARCHAR(75) null,wsrpConsumerId LONG not null primary key,companyId LONG,createDate DATE null,modifiedDate DATE null,name VARCHAR(75) null,url STRING null,wsdl TEXT null,registrationContextString TEXT null,registrationPropertiesString STRING null,forwardCookies VARCHAR(255) null,forwardHeaders VARCHAR(255) null,markupCharacterSets VARCHAR(255) null)";
	public static final String TABLE_SQL_DROP = "drop table WSRP_WSRPConsumer";
	public static final String ORDER_BY_JPQL = " ORDER BY wsrpConsumer.name ASC";
	public static final String ORDER_BY_SQL = " ORDER BY WSRP_WSRPConsumer.name ASC";
	public static final String DATA_SOURCE = "liferayDataSource";
	public static final String SESSION_FACTORY = "liferaySessionFactory";
	public static final String TX_MANAGER = "liferayTransactionManager";
	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.util.service.ServiceProps.get(
				"value.object.entity.cache.enabled.com.liferay.wsrp.model.WSRPConsumer"),
			true);
	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.util.service.ServiceProps.get(
				"value.object.finder.cache.enabled.com.liferay.wsrp.model.WSRPConsumer"),
			true);
	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(com.liferay.util.service.ServiceProps.get(
				"value.object.column.bitmask.enabled.com.liferay.wsrp.model.WSRPConsumer"),
			true);
	public static long COMPANYID_COLUMN_BITMASK = 1L;
	public static long UUID_COLUMN_BITMASK = 2L;
	public static long NAME_COLUMN_BITMASK = 4L;
	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.util.service.ServiceProps.get(
				"lock.expiration.time.com.liferay.wsrp.model.WSRPConsumer"));

	public WSRPConsumerModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _wsrpConsumerId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setWsrpConsumerId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _wsrpConsumerId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return WSRPConsumer.class;
	}

	@Override
	public String getModelClassName() {
		return WSRPConsumer.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("uuid", getUuid());
		attributes.put("wsrpConsumerId", getWsrpConsumerId());
		attributes.put("companyId", getCompanyId());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("name", getName());
		attributes.put("url", getUrl());
		attributes.put("wsdl", getWsdl());
		attributes.put("registrationContextString",
			getRegistrationContextString());
		attributes.put("registrationPropertiesString",
			getRegistrationPropertiesString());
		attributes.put("forwardCookies", getForwardCookies());
		attributes.put("forwardHeaders", getForwardHeaders());
		attributes.put("markupCharacterSets", getMarkupCharacterSets());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		String uuid = (String)attributes.get("uuid");

		if (uuid != null) {
			setUuid(uuid);
		}

		Long wsrpConsumerId = (Long)attributes.get("wsrpConsumerId");

		if (wsrpConsumerId != null) {
			setWsrpConsumerId(wsrpConsumerId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Date modifiedDate = (Date)attributes.get("modifiedDate");

		if (modifiedDate != null) {
			setModifiedDate(modifiedDate);
		}

		String name = (String)attributes.get("name");

		if (name != null) {
			setName(name);
		}

		String url = (String)attributes.get("url");

		if (url != null) {
			setUrl(url);
		}

		String wsdl = (String)attributes.get("wsdl");

		if (wsdl != null) {
			setWsdl(wsdl);
		}

		String registrationContextString = (String)attributes.get(
				"registrationContextString");

		if (registrationContextString != null) {
			setRegistrationContextString(registrationContextString);
		}

		String registrationPropertiesString = (String)attributes.get(
				"registrationPropertiesString");

		if (registrationPropertiesString != null) {
			setRegistrationPropertiesString(registrationPropertiesString);
		}

		String forwardCookies = (String)attributes.get("forwardCookies");

		if (forwardCookies != null) {
			setForwardCookies(forwardCookies);
		}

		String forwardHeaders = (String)attributes.get("forwardHeaders");

		if (forwardHeaders != null) {
			setForwardHeaders(forwardHeaders);
		}

		String markupCharacterSets = (String)attributes.get(
				"markupCharacterSets");

		if (markupCharacterSets != null) {
			setMarkupCharacterSets(markupCharacterSets);
		}
	}

	@Override
	public String getUuid() {
		if (_uuid == null) {
			return StringPool.BLANK;
		}
		else {
			return _uuid;
		}
	}

	@Override
	public void setUuid(String uuid) {
		if (_originalUuid == null) {
			_originalUuid = _uuid;
		}

		_uuid = uuid;
	}

	public String getOriginalUuid() {
		return GetterUtil.getString(_originalUuid);
	}

	@Override
	public long getWsrpConsumerId() {
		return _wsrpConsumerId;
	}

	@Override
	public void setWsrpConsumerId(long wsrpConsumerId) {
		_wsrpConsumerId = wsrpConsumerId;
	}

	@Override
	public long getCompanyId() {
		return _companyId;
	}

	@Override
	public void setCompanyId(long companyId) {
		_columnBitmask |= COMPANYID_COLUMN_BITMASK;

		if (!_setOriginalCompanyId) {
			_setOriginalCompanyId = true;

			_originalCompanyId = _companyId;
		}

		_companyId = companyId;
	}

	public long getOriginalCompanyId() {
		return _originalCompanyId;
	}

	@Override
	public Date getCreateDate() {
		return _createDate;
	}

	@Override
	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	@Override
	public Date getModifiedDate() {
		return _modifiedDate;
	}

	@Override
	public void setModifiedDate(Date modifiedDate) {
		_modifiedDate = modifiedDate;
	}

	@Override
	public String getName() {
		if (_name == null) {
			return StringPool.BLANK;
		}
		else {
			return _name;
		}
	}

	@Override
	public void setName(String name) {
		_columnBitmask = -1L;

		_name = name;
	}

	@Override
	public String getUrl() {
		if (_url == null) {
			return StringPool.BLANK;
		}
		else {
			return _url;
		}
	}

	@Override
	public void setUrl(String url) {
		_url = url;
	}

	@Override
	public String getWsdl() {
		if (_wsdl == null) {
			return StringPool.BLANK;
		}
		else {
			return _wsdl;
		}
	}

	@Override
	public void setWsdl(String wsdl) {
		_wsdl = wsdl;
	}

	@Override
	public String getRegistrationContextString() {
		if (_registrationContextString == null) {
			return StringPool.BLANK;
		}
		else {
			return _registrationContextString;
		}
	}

	@Override
	public void setRegistrationContextString(String registrationContextString) {
		_registrationContextString = registrationContextString;
	}

	@Override
	public String getRegistrationPropertiesString() {
		if (_registrationPropertiesString == null) {
			return StringPool.BLANK;
		}
		else {
			return _registrationPropertiesString;
		}
	}

	@Override
	public void setRegistrationPropertiesString(
		String registrationPropertiesString) {
		_registrationPropertiesString = registrationPropertiesString;
	}

	@Override
	public String getForwardCookies() {
		if (_forwardCookies == null) {
			return StringPool.BLANK;
		}
		else {
			return _forwardCookies;
		}
	}

	@Override
	public void setForwardCookies(String forwardCookies) {
		_forwardCookies = forwardCookies;
	}

	@Override
	public String getForwardHeaders() {
		if (_forwardHeaders == null) {
			return StringPool.BLANK;
		}
		else {
			return _forwardHeaders;
		}
	}

	@Override
	public void setForwardHeaders(String forwardHeaders) {
		_forwardHeaders = forwardHeaders;
	}

	@Override
	public String getMarkupCharacterSets() {
		if (_markupCharacterSets == null) {
			return StringPool.BLANK;
		}
		else {
			return _markupCharacterSets;
		}
	}

	@Override
	public void setMarkupCharacterSets(String markupCharacterSets) {
		_markupCharacterSets = markupCharacterSets;
	}

	@Override
	public StagedModelType getStagedModelType() {
		return new StagedModelType(PortalUtil.getClassNameId(
				WSRPConsumer.class.getName()));
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(getCompanyId(),
			WSRPConsumer.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public WSRPConsumer toEscapedModel() {
		if (_escapedModel == null) {
			_escapedModel = (WSRPConsumer)ProxyUtil.newProxyInstance(_classLoader,
					_escapedModelInterfaces, new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		WSRPConsumerImpl wsrpConsumerImpl = new WSRPConsumerImpl();

		wsrpConsumerImpl.setUuid(getUuid());
		wsrpConsumerImpl.setWsrpConsumerId(getWsrpConsumerId());
		wsrpConsumerImpl.setCompanyId(getCompanyId());
		wsrpConsumerImpl.setCreateDate(getCreateDate());
		wsrpConsumerImpl.setModifiedDate(getModifiedDate());
		wsrpConsumerImpl.setName(getName());
		wsrpConsumerImpl.setUrl(getUrl());
		wsrpConsumerImpl.setWsdl(getWsdl());
		wsrpConsumerImpl.setRegistrationContextString(getRegistrationContextString());
		wsrpConsumerImpl.setRegistrationPropertiesString(getRegistrationPropertiesString());
		wsrpConsumerImpl.setForwardCookies(getForwardCookies());
		wsrpConsumerImpl.setForwardHeaders(getForwardHeaders());
		wsrpConsumerImpl.setMarkupCharacterSets(getMarkupCharacterSets());

		wsrpConsumerImpl.resetOriginalValues();

		return wsrpConsumerImpl;
	}

	@Override
	public int compareTo(WSRPConsumer wsrpConsumer) {
		int value = 0;

		value = getName().compareTo(wsrpConsumer.getName());

		if (value != 0) {
			return value;
		}

		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof WSRPConsumer)) {
			return false;
		}

		WSRPConsumer wsrpConsumer = (WSRPConsumer)obj;

		long primaryKey = wsrpConsumer.getPrimaryKey();

		if (getPrimaryKey() == primaryKey) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return (int)getPrimaryKey();
	}

	@Override
	public void resetOriginalValues() {
		WSRPConsumerModelImpl wsrpConsumerModelImpl = this;

		wsrpConsumerModelImpl._originalUuid = wsrpConsumerModelImpl._uuid;

		wsrpConsumerModelImpl._originalCompanyId = wsrpConsumerModelImpl._companyId;

		wsrpConsumerModelImpl._setOriginalCompanyId = false;

		wsrpConsumerModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<WSRPConsumer> toCacheModel() {
		WSRPConsumerCacheModel wsrpConsumerCacheModel = new WSRPConsumerCacheModel();

		wsrpConsumerCacheModel.uuid = getUuid();

		String uuid = wsrpConsumerCacheModel.uuid;

		if ((uuid != null) && (uuid.length() == 0)) {
			wsrpConsumerCacheModel.uuid = null;
		}

		wsrpConsumerCacheModel.wsrpConsumerId = getWsrpConsumerId();

		wsrpConsumerCacheModel.companyId = getCompanyId();

		Date createDate = getCreateDate();

		if (createDate != null) {
			wsrpConsumerCacheModel.createDate = createDate.getTime();
		}
		else {
			wsrpConsumerCacheModel.createDate = Long.MIN_VALUE;
		}

		Date modifiedDate = getModifiedDate();

		if (modifiedDate != null) {
			wsrpConsumerCacheModel.modifiedDate = modifiedDate.getTime();
		}
		else {
			wsrpConsumerCacheModel.modifiedDate = Long.MIN_VALUE;
		}

		wsrpConsumerCacheModel.name = getName();

		String name = wsrpConsumerCacheModel.name;

		if ((name != null) && (name.length() == 0)) {
			wsrpConsumerCacheModel.name = null;
		}

		wsrpConsumerCacheModel.url = getUrl();

		String url = wsrpConsumerCacheModel.url;

		if ((url != null) && (url.length() == 0)) {
			wsrpConsumerCacheModel.url = null;
		}

		wsrpConsumerCacheModel.wsdl = getWsdl();

		String wsdl = wsrpConsumerCacheModel.wsdl;

		if ((wsdl != null) && (wsdl.length() == 0)) {
			wsrpConsumerCacheModel.wsdl = null;
		}

		wsrpConsumerCacheModel.registrationContextString = getRegistrationContextString();

		String registrationContextString = wsrpConsumerCacheModel.registrationContextString;

		if ((registrationContextString != null) &&
				(registrationContextString.length() == 0)) {
			wsrpConsumerCacheModel.registrationContextString = null;
		}

		wsrpConsumerCacheModel.registrationPropertiesString = getRegistrationPropertiesString();

		String registrationPropertiesString = wsrpConsumerCacheModel.registrationPropertiesString;

		if ((registrationPropertiesString != null) &&
				(registrationPropertiesString.length() == 0)) {
			wsrpConsumerCacheModel.registrationPropertiesString = null;
		}

		wsrpConsumerCacheModel.forwardCookies = getForwardCookies();

		String forwardCookies = wsrpConsumerCacheModel.forwardCookies;

		if ((forwardCookies != null) && (forwardCookies.length() == 0)) {
			wsrpConsumerCacheModel.forwardCookies = null;
		}

		wsrpConsumerCacheModel.forwardHeaders = getForwardHeaders();

		String forwardHeaders = wsrpConsumerCacheModel.forwardHeaders;

		if ((forwardHeaders != null) && (forwardHeaders.length() == 0)) {
			wsrpConsumerCacheModel.forwardHeaders = null;
		}

		wsrpConsumerCacheModel.markupCharacterSets = getMarkupCharacterSets();

		String markupCharacterSets = wsrpConsumerCacheModel.markupCharacterSets;

		if ((markupCharacterSets != null) &&
				(markupCharacterSets.length() == 0)) {
			wsrpConsumerCacheModel.markupCharacterSets = null;
		}

		return wsrpConsumerCacheModel;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(27);

		sb.append("{uuid=");
		sb.append(getUuid());
		sb.append(", wsrpConsumerId=");
		sb.append(getWsrpConsumerId());
		sb.append(", companyId=");
		sb.append(getCompanyId());
		sb.append(", createDate=");
		sb.append(getCreateDate());
		sb.append(", modifiedDate=");
		sb.append(getModifiedDate());
		sb.append(", name=");
		sb.append(getName());
		sb.append(", url=");
		sb.append(getUrl());
		sb.append(", wsdl=");
		sb.append(getWsdl());
		sb.append(", registrationContextString=");
		sb.append(getRegistrationContextString());
		sb.append(", registrationPropertiesString=");
		sb.append(getRegistrationPropertiesString());
		sb.append(", forwardCookies=");
		sb.append(getForwardCookies());
		sb.append(", forwardHeaders=");
		sb.append(getForwardHeaders());
		sb.append(", markupCharacterSets=");
		sb.append(getMarkupCharacterSets());
		sb.append("}");

		return sb.toString();
	}

	@Override
	public String toXmlString() {
		StringBundler sb = new StringBundler(43);

		sb.append("<model><model-name>");
		sb.append("com.liferay.wsrp.model.WSRPConsumer");
		sb.append("</model-name>");

		sb.append(
			"<column><column-name>uuid</column-name><column-value><![CDATA[");
		sb.append(getUuid());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>wsrpConsumerId</column-name><column-value><![CDATA[");
		sb.append(getWsrpConsumerId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>companyId</column-name><column-value><![CDATA[");
		sb.append(getCompanyId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>createDate</column-name><column-value><![CDATA[");
		sb.append(getCreateDate());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>modifiedDate</column-name><column-value><![CDATA[");
		sb.append(getModifiedDate());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>name</column-name><column-value><![CDATA[");
		sb.append(getName());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>url</column-name><column-value><![CDATA[");
		sb.append(getUrl());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>wsdl</column-name><column-value><![CDATA[");
		sb.append(getWsdl());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>registrationContextString</column-name><column-value><![CDATA[");
		sb.append(getRegistrationContextString());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>registrationPropertiesString</column-name><column-value><![CDATA[");
		sb.append(getRegistrationPropertiesString());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>forwardCookies</column-name><column-value><![CDATA[");
		sb.append(getForwardCookies());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>forwardHeaders</column-name><column-value><![CDATA[");
		sb.append(getForwardHeaders());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>markupCharacterSets</column-name><column-value><![CDATA[");
		sb.append(getMarkupCharacterSets());
		sb.append("]]></column-value></column>");

		sb.append("</model>");

		return sb.toString();
	}

	private static ClassLoader _classLoader = WSRPConsumer.class.getClassLoader();
	private static Class<?>[] _escapedModelInterfaces = new Class[] {
			WSRPConsumer.class
		};
	private String _uuid;
	private String _originalUuid;
	private long _wsrpConsumerId;
	private long _companyId;
	private long _originalCompanyId;
	private boolean _setOriginalCompanyId;
	private Date _createDate;
	private Date _modifiedDate;
	private String _name;
	private String _url;
	private String _wsdl;
	private String _registrationContextString;
	private String _registrationPropertiesString;
	private String _forwardCookies;
	private String _forwardHeaders;
	private String _markupCharacterSets;
	private long _columnBitmask;
	private WSRPConsumer _escapedModel;
}