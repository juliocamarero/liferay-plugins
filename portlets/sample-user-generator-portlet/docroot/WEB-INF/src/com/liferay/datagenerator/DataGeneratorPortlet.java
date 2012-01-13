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
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

/**
 * <a href="DataGeneratorPortlet.java.html"><b><i>View Source</i></b></a>
 *
 * @author Julio Camarero
 * @author JorgeFerrer
 * @author Manuel de la Peña
 *
 */
public class DataGeneratorPortlet extends MVCPortlet {

	public void processAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException, PortletException {

		try {
			_usersGenerator.generate(actionRequest);

			String redirect = ParamUtil.getString(actionRequest, "redirect");

			actionResponse.sendRedirect(redirect);
		}
		catch (Exception e) {
			SessionErrors.add(actionRequest, "error", e);
			_log.error(e);
		}
	}

	private UsersGenerator _usersGenerator = new UsersGenerator();

	private static Log _log = LogFactoryUtil.getLog(DataGeneratorPortlet.class);

}