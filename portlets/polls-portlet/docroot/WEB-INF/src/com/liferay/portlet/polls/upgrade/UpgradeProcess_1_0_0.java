/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
 *
 * This file is part of Liferay Social Office. Liferay Social Office is free
 * software: you can redistribute it and/or modify it under the terms of the GNU
 * Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * Liferay Social Office is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Liferay Social Office. If not, see http://www.gnu.org/licenses/agpl-3.0.html.
 */

package com.liferay.portlet.polls.upgrade;

import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portlet.polls.upgrade.v1_0_0.UpgradePolls;
import com.liferay.portlet.polls.upgrade.v1_0_0.UpgradePortletId;

/**
 * @author Juan Fernández
 */
public class UpgradeProcess_1_0_0 extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		upgrade(UpgradePortletId.class);
		upgrade(UpgradePolls.class);
	}

}