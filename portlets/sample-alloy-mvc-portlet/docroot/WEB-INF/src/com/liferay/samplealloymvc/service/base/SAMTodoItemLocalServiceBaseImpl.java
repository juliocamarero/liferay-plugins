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

package com.liferay.samplealloymvc.service.base;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DefaultActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiService;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.BaseLocalServiceImpl;
import com.liferay.portal.kernel.service.PersistedModelLocalServiceRegistryUtil;
import com.liferay.portal.kernel.service.persistence.ClassNamePersistence;
import com.liferay.portal.kernel.service.persistence.UserPersistence;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PortalUtil;

import com.liferay.samplealloymvc.model.SAMTodoItem;
import com.liferay.samplealloymvc.service.SAMTodoItemLocalService;
import com.liferay.samplealloymvc.service.persistence.SAMTodoItemPersistence;
import com.liferay.samplealloymvc.service.persistence.SAMTodoListPersistence;

import java.io.Serializable;

import java.util.List;

import javax.sql.DataSource;

/**
 * Provides the base implementation for the s a m todo item local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.samplealloymvc.service.impl.SAMTodoItemLocalServiceImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.samplealloymvc.service.impl.SAMTodoItemLocalServiceImpl
 * @see com.liferay.samplealloymvc.service.SAMTodoItemLocalServiceUtil
 * @generated
 */
@ProviderType
public abstract class SAMTodoItemLocalServiceBaseImpl
	extends BaseLocalServiceImpl implements SAMTodoItemLocalService,
		IdentifiableOSGiService {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link com.liferay.samplealloymvc.service.SAMTodoItemLocalServiceUtil} to access the s a m todo item local service.
	 */

	/**
	 * Adds the s a m todo item to the database. Also notifies the appropriate model listeners.
	 *
	 * @param samTodoItem the s a m todo item
	 * @return the s a m todo item that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public SAMTodoItem addSAMTodoItem(SAMTodoItem samTodoItem) {
		samTodoItem.setNew(true);

		return samTodoItemPersistence.update(samTodoItem);
	}

	/**
	 * Creates a new s a m todo item with the primary key. Does not add the s a m todo item to the database.
	 *
	 * @param samTodoItemId the primary key for the new s a m todo item
	 * @return the new s a m todo item
	 */
	@Override
	public SAMTodoItem createSAMTodoItem(long samTodoItemId) {
		return samTodoItemPersistence.create(samTodoItemId);
	}

	/**
	 * Deletes the s a m todo item with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param samTodoItemId the primary key of the s a m todo item
	 * @return the s a m todo item that was removed
	 * @throws PortalException if a s a m todo item with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public SAMTodoItem deleteSAMTodoItem(long samTodoItemId)
		throws PortalException {
		return samTodoItemPersistence.remove(samTodoItemId);
	}

	/**
	 * Deletes the s a m todo item from the database. Also notifies the appropriate model listeners.
	 *
	 * @param samTodoItem the s a m todo item
	 * @return the s a m todo item that was removed
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public SAMTodoItem deleteSAMTodoItem(SAMTodoItem samTodoItem) {
		return samTodoItemPersistence.remove(samTodoItem);
	}

	@Override
	public DynamicQuery dynamicQuery() {
		Class<?> clazz = getClass();

		return DynamicQueryFactoryUtil.forClass(SAMTodoItem.class,
			clazz.getClassLoader());
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return samTodoItemPersistence.findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.samplealloymvc.model.impl.SAMTodoItemModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery, int start,
		int end) {
		return samTodoItemPersistence.findWithDynamicQuery(dynamicQuery, start,
			end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.samplealloymvc.model.impl.SAMTodoItemModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery, int start,
		int end, OrderByComparator<T> orderByComparator) {
		return samTodoItemPersistence.findWithDynamicQuery(dynamicQuery, start,
			end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(DynamicQuery dynamicQuery) {
		return samTodoItemPersistence.countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(DynamicQuery dynamicQuery,
		Projection projection) {
		return samTodoItemPersistence.countWithDynamicQuery(dynamicQuery,
			projection);
	}

	@Override
	public SAMTodoItem fetchSAMTodoItem(long samTodoItemId) {
		return samTodoItemPersistence.fetchByPrimaryKey(samTodoItemId);
	}

	/**
	 * Returns the s a m todo item with the primary key.
	 *
	 * @param samTodoItemId the primary key of the s a m todo item
	 * @return the s a m todo item
	 * @throws PortalException if a s a m todo item with the primary key could not be found
	 */
	@Override
	public SAMTodoItem getSAMTodoItem(long samTodoItemId)
		throws PortalException {
		return samTodoItemPersistence.findByPrimaryKey(samTodoItemId);
	}

	@Override
	public ActionableDynamicQuery getActionableDynamicQuery() {
		ActionableDynamicQuery actionableDynamicQuery = new DefaultActionableDynamicQuery();

		actionableDynamicQuery.setBaseLocalService(samTodoItemLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(SAMTodoItem.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("samTodoItemId");

		return actionableDynamicQuery;
	}

	@Override
	public IndexableActionableDynamicQuery getIndexableActionableDynamicQuery() {
		IndexableActionableDynamicQuery indexableActionableDynamicQuery = new IndexableActionableDynamicQuery();

		indexableActionableDynamicQuery.setBaseLocalService(samTodoItemLocalService);
		indexableActionableDynamicQuery.setClassLoader(getClassLoader());
		indexableActionableDynamicQuery.setModelClass(SAMTodoItem.class);

		indexableActionableDynamicQuery.setPrimaryKeyPropertyName(
			"samTodoItemId");

		return indexableActionableDynamicQuery;
	}

	protected void initActionableDynamicQuery(
		ActionableDynamicQuery actionableDynamicQuery) {
		actionableDynamicQuery.setBaseLocalService(samTodoItemLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(SAMTodoItem.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("samTodoItemId");
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException {
		return samTodoItemLocalService.deleteSAMTodoItem((SAMTodoItem)persistedModel);
	}

	@Override
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {
		return samTodoItemPersistence.findByPrimaryKey(primaryKeyObj);
	}

	/**
	 * Returns a range of all the s a m todo items.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.samplealloymvc.model.impl.SAMTodoItemModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of s a m todo items
	 * @param end the upper bound of the range of s a m todo items (not inclusive)
	 * @return the range of s a m todo items
	 */
	@Override
	public List<SAMTodoItem> getSAMTodoItems(int start, int end) {
		return samTodoItemPersistence.findAll(start, end);
	}

	/**
	 * Returns the number of s a m todo items.
	 *
	 * @return the number of s a m todo items
	 */
	@Override
	public int getSAMTodoItemsCount() {
		return samTodoItemPersistence.countAll();
	}

	/**
	 * Updates the s a m todo item in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param samTodoItem the s a m todo item
	 * @return the s a m todo item that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public SAMTodoItem updateSAMTodoItem(SAMTodoItem samTodoItem) {
		return samTodoItemPersistence.update(samTodoItem);
	}

	/**
	 * Returns the s a m todo item local service.
	 *
	 * @return the s a m todo item local service
	 */
	public SAMTodoItemLocalService getSAMTodoItemLocalService() {
		return samTodoItemLocalService;
	}

	/**
	 * Sets the s a m todo item local service.
	 *
	 * @param samTodoItemLocalService the s a m todo item local service
	 */
	public void setSAMTodoItemLocalService(
		SAMTodoItemLocalService samTodoItemLocalService) {
		this.samTodoItemLocalService = samTodoItemLocalService;
	}

	/**
	 * Returns the s a m todo item persistence.
	 *
	 * @return the s a m todo item persistence
	 */
	public SAMTodoItemPersistence getSAMTodoItemPersistence() {
		return samTodoItemPersistence;
	}

	/**
	 * Sets the s a m todo item persistence.
	 *
	 * @param samTodoItemPersistence the s a m todo item persistence
	 */
	public void setSAMTodoItemPersistence(
		SAMTodoItemPersistence samTodoItemPersistence) {
		this.samTodoItemPersistence = samTodoItemPersistence;
	}

	/**
	 * Returns the s a m todo list local service.
	 *
	 * @return the s a m todo list local service
	 */
	public com.liferay.samplealloymvc.service.SAMTodoListLocalService getSAMTodoListLocalService() {
		return samTodoListLocalService;
	}

	/**
	 * Sets the s a m todo list local service.
	 *
	 * @param samTodoListLocalService the s a m todo list local service
	 */
	public void setSAMTodoListLocalService(
		com.liferay.samplealloymvc.service.SAMTodoListLocalService samTodoListLocalService) {
		this.samTodoListLocalService = samTodoListLocalService;
	}

	/**
	 * Returns the s a m todo list persistence.
	 *
	 * @return the s a m todo list persistence
	 */
	public SAMTodoListPersistence getSAMTodoListPersistence() {
		return samTodoListPersistence;
	}

	/**
	 * Sets the s a m todo list persistence.
	 *
	 * @param samTodoListPersistence the s a m todo list persistence
	 */
	public void setSAMTodoListPersistence(
		SAMTodoListPersistence samTodoListPersistence) {
		this.samTodoListPersistence = samTodoListPersistence;
	}

	/**
	 * Returns the counter local service.
	 *
	 * @return the counter local service
	 */
	public com.liferay.counter.kernel.service.CounterLocalService getCounterLocalService() {
		return counterLocalService;
	}

	/**
	 * Sets the counter local service.
	 *
	 * @param counterLocalService the counter local service
	 */
	public void setCounterLocalService(
		com.liferay.counter.kernel.service.CounterLocalService counterLocalService) {
		this.counterLocalService = counterLocalService;
	}

	/**
	 * Returns the class name local service.
	 *
	 * @return the class name local service
	 */
	public com.liferay.portal.kernel.service.ClassNameLocalService getClassNameLocalService() {
		return classNameLocalService;
	}

	/**
	 * Sets the class name local service.
	 *
	 * @param classNameLocalService the class name local service
	 */
	public void setClassNameLocalService(
		com.liferay.portal.kernel.service.ClassNameLocalService classNameLocalService) {
		this.classNameLocalService = classNameLocalService;
	}

	/**
	 * Returns the class name persistence.
	 *
	 * @return the class name persistence
	 */
	public ClassNamePersistence getClassNamePersistence() {
		return classNamePersistence;
	}

	/**
	 * Sets the class name persistence.
	 *
	 * @param classNamePersistence the class name persistence
	 */
	public void setClassNamePersistence(
		ClassNamePersistence classNamePersistence) {
		this.classNamePersistence = classNamePersistence;
	}

	/**
	 * Returns the resource local service.
	 *
	 * @return the resource local service
	 */
	public com.liferay.portal.kernel.service.ResourceLocalService getResourceLocalService() {
		return resourceLocalService;
	}

	/**
	 * Sets the resource local service.
	 *
	 * @param resourceLocalService the resource local service
	 */
	public void setResourceLocalService(
		com.liferay.portal.kernel.service.ResourceLocalService resourceLocalService) {
		this.resourceLocalService = resourceLocalService;
	}

	/**
	 * Returns the user local service.
	 *
	 * @return the user local service
	 */
	public com.liferay.portal.kernel.service.UserLocalService getUserLocalService() {
		return userLocalService;
	}

	/**
	 * Sets the user local service.
	 *
	 * @param userLocalService the user local service
	 */
	public void setUserLocalService(
		com.liferay.portal.kernel.service.UserLocalService userLocalService) {
		this.userLocalService = userLocalService;
	}

	/**
	 * Returns the user persistence.
	 *
	 * @return the user persistence
	 */
	public UserPersistence getUserPersistence() {
		return userPersistence;
	}

	/**
	 * Sets the user persistence.
	 *
	 * @param userPersistence the user persistence
	 */
	public void setUserPersistence(UserPersistence userPersistence) {
		this.userPersistence = userPersistence;
	}

	public void afterPropertiesSet() {
		Class<?> clazz = getClass();

		_classLoader = clazz.getClassLoader();

		PersistedModelLocalServiceRegistryUtil.register("com.liferay.samplealloymvc.model.SAMTodoItem",
			samTodoItemLocalService);
	}

	public void destroy() {
		PersistedModelLocalServiceRegistryUtil.unregister(
			"com.liferay.samplealloymvc.model.SAMTodoItem");
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return SAMTodoItemLocalService.class.getName();
	}

	@Override
	public Object invokeMethod(String name, String[] parameterTypes,
		Object[] arguments) throws Throwable {
		Thread currentThread = Thread.currentThread();

		ClassLoader contextClassLoader = currentThread.getContextClassLoader();

		if (contextClassLoader != _classLoader) {
			currentThread.setContextClassLoader(_classLoader);
		}

		try {
			return _clpInvoker.invokeMethod(name, parameterTypes, arguments);
		}
		finally {
			if (contextClassLoader != _classLoader) {
				currentThread.setContextClassLoader(contextClassLoader);
			}
		}
	}

	protected Class<?> getModelClass() {
		return SAMTodoItem.class;
	}

	protected String getModelClassName() {
		return SAMTodoItem.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource = samTodoItemPersistence.getDataSource();

			DB db = DBManagerUtil.getDB();

			sql = db.buildSQL(sql);
			sql = PortalUtil.transformSQL(sql);

			SqlUpdate sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(dataSource,
					sql);

			sqlUpdate.update();
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	@BeanReference(type = SAMTodoItemLocalService.class)
	protected SAMTodoItemLocalService samTodoItemLocalService;
	@BeanReference(type = SAMTodoItemPersistence.class)
	protected SAMTodoItemPersistence samTodoItemPersistence;
	@BeanReference(type = com.liferay.samplealloymvc.service.SAMTodoListLocalService.class)
	protected com.liferay.samplealloymvc.service.SAMTodoListLocalService samTodoListLocalService;
	@BeanReference(type = SAMTodoListPersistence.class)
	protected SAMTodoListPersistence samTodoListPersistence;
	@BeanReference(type = com.liferay.counter.kernel.service.CounterLocalService.class)
	protected com.liferay.counter.kernel.service.CounterLocalService counterLocalService;
	@BeanReference(type = com.liferay.portal.kernel.service.ClassNameLocalService.class)
	protected com.liferay.portal.kernel.service.ClassNameLocalService classNameLocalService;
	@BeanReference(type = ClassNamePersistence.class)
	protected ClassNamePersistence classNamePersistence;
	@BeanReference(type = com.liferay.portal.kernel.service.ResourceLocalService.class)
	protected com.liferay.portal.kernel.service.ResourceLocalService resourceLocalService;
	@BeanReference(type = com.liferay.portal.kernel.service.UserLocalService.class)
	protected com.liferay.portal.kernel.service.UserLocalService userLocalService;
	@BeanReference(type = UserPersistence.class)
	protected UserPersistence userPersistence;
	private ClassLoader _classLoader;
	private SAMTodoItemLocalServiceClpInvoker _clpInvoker = new SAMTodoItemLocalServiceClpInvoker();
}