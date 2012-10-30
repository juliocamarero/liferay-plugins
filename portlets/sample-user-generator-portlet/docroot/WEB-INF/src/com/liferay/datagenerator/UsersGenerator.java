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

package com.liferay.datagenerator;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.OrganizationConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.service.UserLocalServiceUtil;

import javax.portlet.ActionRequest;

/**
 * <a href="UsersGenerator.java.html"><b><i>View Source</i></b></a>
 *
 * @author Jorge Ferrer
 * @author Manuel de la Peña
 */
public class UsersGenerator implements DataGenerator {

	public void generate(ActionRequest actionRequest) throws Exception {
		long numberOfOrganizations = ParamUtil.getLong(
			actionRequest, "numberOfOrganizations", 0);
		long numberOfUsers = ParamUtil.getLong(
			actionRequest, "numberOfUsers", 0);
		long parentOrganizationId = ParamUtil.getLong(
			actionRequest, "parentOrganizationId",
			OrganizationConstants.DEFAULT_PARENT_ORGANIZATION_ID);
		String prefix = ParamUtil.getString(actionRequest, "prefix");

		long userId = ParamUtil.getLong(actionRequest, "userId", 0);
		long companyId = ParamUtil.getLong(actionRequest, "companyId", 0);

		if (userId == 0 || companyId == 0 || 
			(numberOfUsers == 0 && numberOfOrganizations == 0)) {
			
			return;
		}

		ServiceContext serviceContextUser =
			ServiceContextFactory.getInstance(
				User.class.getName(), actionRequest);
		ServiceContext serviceContextOrg =
			ServiceContextFactory.getInstance(
				Organization.class.getName(), actionRequest);

		if (numberOfOrganizations > 0 && numberOfUsers > 0) {
			long usersPerOrganization =
				numberOfUsers / numberOfOrganizations;
			long restOfUsers = numberOfUsers % numberOfOrganizations;

			for (int i = 0; i < numberOfOrganizations; i++){

				String organizationName =
					prefix + "_organization_" + i;

				Organization organization = createOrganization(
					userId, parentOrganizationId, organizationName,
					serviceContextOrg);

				_log.info(
					"created organization " + i + " : " +
						organization.getName());

				if (i == numberOfOrganizations - 1 ) {
					usersPerOrganization += restOfUsers;
				}

				for (int j = 0; j < usersPerOrganization; j++){
					String userName =
						prefix + "-user-" + i + "-" + j;

					User user = createUser(
						j, userId, companyId, userName,
						new long[] {organization.getOrganizationId()},
						serviceContextUser);

					_log.info("created user " + i + "-" + j + " : "
						+ user.getFirstName());

				}
			}

			SessionMessages.add(actionRequest, "success");
		}
		else if (numberOfUsers > 0) {
			for (int j = 0; j < numberOfUsers; j++){

				String userName = prefix + "-user-" + j;

				long[] organizationIds = null;

				if (parentOrganizationId > 0) {
					organizationIds = new long[]{parentOrganizationId};
				}

				User user = createUser(
					j, userId, companyId, userName, organizationIds,
					serviceContextUser);

				_log.info("created user " + j + " : "
					+ user.getFirstName());

			}

			SessionMessages.add(actionRequest, "success");
		}
		else {
			for (int k = 0; k < numberOfOrganizations; k++) {
				String organizationName =
					prefix + "_organization_" + k;

				Organization organization = createOrganization(
					userId, parentOrganizationId, organizationName,
					serviceContextOrg);

				_log.info(
					"created organization " + k + " : " +
						organization.getName());
			}
		}
	}

	protected static Organization createOrganization(
			long userId, long parentOrganizationId, String organizationName,
			ServiceContext serviceContext)
		throws Exception{

		int statusId = GetterUtil.getInteger(PropsUtil.get(
			"sql.data.com.liferay.portal.model.ListType.organization.status"));

		return OrganizationLocalServiceUtil.addOrganization(
			userId, parentOrganizationId, organizationName, 
			OrganizationConstants.TYPE_REGULAR_ORGANIZATION, true, 0L, 0L,
			statusId, StringPool.BLANK, serviceContext);
	}

	protected static User createUser(
			int index, long userId, long companyId, String userName,
			long[] organizationIds, ServiceContext serviceContext)
		throws Exception {

		boolean male = (index % 2 == 0);
		
		User user = UserLocalServiceUtil.addUser(
			userId, companyId, true, "test", "test", false, userName,
			userName + "@test.com", 0, StringPool.BLANK, 
			LocaleUtil.getDefault(), userName, StringPool.BLANK, 
			StringPool.BLANK, 1, 1, male, 1, 1, 1970, StringPool.BLANK,
			new long[0], organizationIds, new long[0], new long[0], false, 
			serviceContext);

		UserLocalServiceUtil.updateAgreedToTermsOfUse(user.getUserId(), true);
		UserLocalServiceUtil.updateReminderQuery(user.getUserId(), "ok", "ok");

		return user;
	}

	private static Log _log = LogFactoryUtil.getLog(UsersGenerator.class);

}
