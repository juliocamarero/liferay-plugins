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

package com.liferay.socialcoding.model.impl;

import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.impl.BaseModelImpl;
import com.liferay.portal.service.ServiceContext;

import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.expando.util.ExpandoBridgeFactoryUtil;

import com.liferay.socialcoding.model.JIRAAction;
import com.liferay.socialcoding.model.JIRAActionModel;

import java.io.Serializable;

import java.sql.Types;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * The base model implementation for the JIRAAction service. Represents a row in the &quot;jiraaction&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface {@link com.liferay.socialcoding.model.JIRAActionModel} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link JIRAActionImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see JIRAActionImpl
 * @see com.liferay.socialcoding.model.JIRAAction
 * @see com.liferay.socialcoding.model.JIRAActionModel
 * @generated
 */
public class JIRAActionModelImpl extends BaseModelImpl<JIRAAction>
	implements JIRAActionModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a j i r a action model instance should use the {@link com.liferay.socialcoding.model.JIRAAction} interface instead.
	 */
	public static final String TABLE_NAME = "jiraaction";
	public static final Object[][] TABLE_COLUMNS = {
			{ "id", Types.BIGINT },
			{ "author", Types.VARCHAR },
			{ "created", Types.TIMESTAMP },
			{ "updated", Types.TIMESTAMP },
			{ "issueid", Types.BIGINT },
			{ "actiontype", Types.VARCHAR },
			{ "actionbody", Types.VARCHAR },
			{ "actionlevel", Types.VARCHAR }
		};
	public static final String TABLE_SQL_CREATE = "create table jiraaction (id LONG not null primary key,author VARCHAR(75) null,created DATE null,updated DATE null,issueid LONG,actiontype VARCHAR(75) null,actionbody VARCHAR(75) null,actionlevel VARCHAR(75) null)";
	public static final String TABLE_SQL_DROP = "drop table jiraaction";
	public static final String ORDER_BY_JPQL = " ORDER BY jiraAction.modifiedDate DESC";
	public static final String ORDER_BY_SQL = " ORDER BY jiraaction.updated DESC";
	public static final String DATA_SOURCE = "jiraDataSource";
	public static final String SESSION_FACTORY = "jiraSessionFactory";
	public static final String TX_MANAGER = "jiraTransactionManager";
	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.util.service.ServiceProps.get(
				"value.object.entity.cache.enabled.com.liferay.socialcoding.model.JIRAAction"),
			true);
	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.util.service.ServiceProps.get(
				"value.object.finder.cache.enabled.com.liferay.socialcoding.model.JIRAAction"),
			true);
	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(com.liferay.util.service.ServiceProps.get(
				"value.object.column.bitmask.enabled.com.liferay.socialcoding.model.JIRAAction"),
			true);
	public static long JIRAISSUEID_COLUMN_BITMASK = 1L;
	public static long JIRAUSERID_COLUMN_BITMASK = 2L;
	public static long TYPE_COLUMN_BITMASK = 4L;
	public static long MODIFIEDDATE_COLUMN_BITMASK = 8L;
	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.util.service.ServiceProps.get(
				"lock.expiration.time.com.liferay.socialcoding.model.JIRAAction"));

	public JIRAActionModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _jiraActionId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setJiraActionId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _jiraActionId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return JIRAAction.class;
	}

	@Override
	public String getModelClassName() {
		return JIRAAction.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("jiraActionId", getJiraActionId());
		attributes.put("jiraUserId", getJiraUserId());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("jiraIssueId", getJiraIssueId());
		attributes.put("type", getType());
		attributes.put("body", getBody());
		attributes.put("jiraGroupName", getJiraGroupName());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long jiraActionId = (Long)attributes.get("jiraActionId");

		if (jiraActionId != null) {
			setJiraActionId(jiraActionId);
		}

		String jiraUserId = (String)attributes.get("jiraUserId");

		if (jiraUserId != null) {
			setJiraUserId(jiraUserId);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Date modifiedDate = (Date)attributes.get("modifiedDate");

		if (modifiedDate != null) {
			setModifiedDate(modifiedDate);
		}

		Long jiraIssueId = (Long)attributes.get("jiraIssueId");

		if (jiraIssueId != null) {
			setJiraIssueId(jiraIssueId);
		}

		String type = (String)attributes.get("type");

		if (type != null) {
			setType(type);
		}

		String body = (String)attributes.get("body");

		if (body != null) {
			setBody(body);
		}

		String jiraGroupName = (String)attributes.get("jiraGroupName");

		if (jiraGroupName != null) {
			setJiraGroupName(jiraGroupName);
		}
	}

	@Override
	public long getJiraActionId() {
		return _jiraActionId;
	}

	@Override
	public void setJiraActionId(long jiraActionId) {
		_jiraActionId = jiraActionId;
	}

	@Override
	public String getJiraUserId() {
		if (_jiraUserId == null) {
			return StringPool.BLANK;
		}
		else {
			return _jiraUserId;
		}
	}

	@Override
	public void setJiraUserId(String jiraUserId) {
		_columnBitmask |= JIRAUSERID_COLUMN_BITMASK;

		if (_originalJiraUserId == null) {
			_originalJiraUserId = _jiraUserId;
		}

		_jiraUserId = jiraUserId;
	}

	public String getOriginalJiraUserId() {
		return GetterUtil.getString(_originalJiraUserId);
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
		_columnBitmask = -1L;

		_modifiedDate = modifiedDate;
	}

	@Override
	public long getJiraIssueId() {
		return _jiraIssueId;
	}

	@Override
	public void setJiraIssueId(long jiraIssueId) {
		_columnBitmask |= JIRAISSUEID_COLUMN_BITMASK;

		if (!_setOriginalJiraIssueId) {
			_setOriginalJiraIssueId = true;

			_originalJiraIssueId = _jiraIssueId;
		}

		_jiraIssueId = jiraIssueId;
	}

	public long getOriginalJiraIssueId() {
		return _originalJiraIssueId;
	}

	@Override
	public String getType() {
		if (_type == null) {
			return StringPool.BLANK;
		}
		else {
			return _type;
		}
	}

	@Override
	public void setType(String type) {
		_columnBitmask |= TYPE_COLUMN_BITMASK;

		if (_originalType == null) {
			_originalType = _type;
		}

		_type = type;
	}

	public String getOriginalType() {
		return GetterUtil.getString(_originalType);
	}

	@Override
	public String getBody() {
		if (_body == null) {
			return StringPool.BLANK;
		}
		else {
			return _body;
		}
	}

	@Override
	public void setBody(String body) {
		_body = body;
	}

	@Override
	public String getJiraGroupName() {
		if (_jiraGroupName == null) {
			return StringPool.BLANK;
		}
		else {
			return _jiraGroupName;
		}
	}

	@Override
	public void setJiraGroupName(String jiraGroupName) {
		_jiraGroupName = jiraGroupName;
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(0,
			JIRAAction.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public JIRAAction toEscapedModel() {
		if (_escapedModel == null) {
			_escapedModel = (JIRAAction)ProxyUtil.newProxyInstance(_classLoader,
					_escapedModelInterfaces, new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		JIRAActionImpl jiraActionImpl = new JIRAActionImpl();

		jiraActionImpl.setJiraActionId(getJiraActionId());
		jiraActionImpl.setJiraUserId(getJiraUserId());
		jiraActionImpl.setCreateDate(getCreateDate());
		jiraActionImpl.setModifiedDate(getModifiedDate());
		jiraActionImpl.setJiraIssueId(getJiraIssueId());
		jiraActionImpl.setType(getType());
		jiraActionImpl.setBody(getBody());
		jiraActionImpl.setJiraGroupName(getJiraGroupName());

		jiraActionImpl.resetOriginalValues();

		return jiraActionImpl;
	}

	@Override
	public int compareTo(JIRAAction jiraAction) {
		int value = 0;

		value = DateUtil.compareTo(getModifiedDate(),
				jiraAction.getModifiedDate());

		value = value * -1;

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

		if (!(obj instanceof JIRAAction)) {
			return false;
		}

		JIRAAction jiraAction = (JIRAAction)obj;

		long primaryKey = jiraAction.getPrimaryKey();

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
		JIRAActionModelImpl jiraActionModelImpl = this;

		jiraActionModelImpl._originalJiraUserId = jiraActionModelImpl._jiraUserId;

		jiraActionModelImpl._originalJiraIssueId = jiraActionModelImpl._jiraIssueId;

		jiraActionModelImpl._setOriginalJiraIssueId = false;

		jiraActionModelImpl._originalType = jiraActionModelImpl._type;

		jiraActionModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<JIRAAction> toCacheModel() {
		JIRAActionCacheModel jiraActionCacheModel = new JIRAActionCacheModel();

		jiraActionCacheModel.jiraActionId = getJiraActionId();

		jiraActionCacheModel.jiraUserId = getJiraUserId();

		String jiraUserId = jiraActionCacheModel.jiraUserId;

		if ((jiraUserId != null) && (jiraUserId.length() == 0)) {
			jiraActionCacheModel.jiraUserId = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			jiraActionCacheModel.createDate = createDate.getTime();
		}
		else {
			jiraActionCacheModel.createDate = Long.MIN_VALUE;
		}

		Date modifiedDate = getModifiedDate();

		if (modifiedDate != null) {
			jiraActionCacheModel.modifiedDate = modifiedDate.getTime();
		}
		else {
			jiraActionCacheModel.modifiedDate = Long.MIN_VALUE;
		}

		jiraActionCacheModel.jiraIssueId = getJiraIssueId();

		jiraActionCacheModel.type = getType();

		String type = jiraActionCacheModel.type;

		if ((type != null) && (type.length() == 0)) {
			jiraActionCacheModel.type = null;
		}

		jiraActionCacheModel.body = getBody();

		String body = jiraActionCacheModel.body;

		if ((body != null) && (body.length() == 0)) {
			jiraActionCacheModel.body = null;
		}

		jiraActionCacheModel.jiraGroupName = getJiraGroupName();

		String jiraGroupName = jiraActionCacheModel.jiraGroupName;

		if ((jiraGroupName != null) && (jiraGroupName.length() == 0)) {
			jiraActionCacheModel.jiraGroupName = null;
		}

		return jiraActionCacheModel;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(17);

		sb.append("{jiraActionId=");
		sb.append(getJiraActionId());
		sb.append(", jiraUserId=");
		sb.append(getJiraUserId());
		sb.append(", createDate=");
		sb.append(getCreateDate());
		sb.append(", modifiedDate=");
		sb.append(getModifiedDate());
		sb.append(", jiraIssueId=");
		sb.append(getJiraIssueId());
		sb.append(", type=");
		sb.append(getType());
		sb.append(", body=");
		sb.append(getBody());
		sb.append(", jiraGroupName=");
		sb.append(getJiraGroupName());
		sb.append("}");

		return sb.toString();
	}

	@Override
	public String toXmlString() {
		StringBundler sb = new StringBundler(28);

		sb.append("<model><model-name>");
		sb.append("com.liferay.socialcoding.model.JIRAAction");
		sb.append("</model-name>");

		sb.append(
			"<column><column-name>jiraActionId</column-name><column-value><![CDATA[");
		sb.append(getJiraActionId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>jiraUserId</column-name><column-value><![CDATA[");
		sb.append(getJiraUserId());
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
			"<column><column-name>jiraIssueId</column-name><column-value><![CDATA[");
		sb.append(getJiraIssueId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>type</column-name><column-value><![CDATA[");
		sb.append(getType());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>body</column-name><column-value><![CDATA[");
		sb.append(getBody());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>jiraGroupName</column-name><column-value><![CDATA[");
		sb.append(getJiraGroupName());
		sb.append("]]></column-value></column>");

		sb.append("</model>");

		return sb.toString();
	}

	private static ClassLoader _classLoader = JIRAAction.class.getClassLoader();
	private static Class<?>[] _escapedModelInterfaces = new Class[] {
			JIRAAction.class
		};
	private long _jiraActionId;
	private String _jiraUserId;
	private String _originalJiraUserId;
	private Date _createDate;
	private Date _modifiedDate;
	private long _jiraIssueId;
	private long _originalJiraIssueId;
	private boolean _setOriginalJiraIssueId;
	private String _type;
	private String _originalType;
	private String _body;
	private String _jiraGroupName;
	private long _columnBitmask;
	private JIRAAction _escapedModel;
}