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

package com.liferay.bbb.model.impl;

import com.liferay.bbb.model.BBBParticipant;
import com.liferay.bbb.service.BBBParticipantLocalServiceUtil;

import com.liferay.portal.kernel.exception.SystemException;

/**
 * The extended model base implementation for the BBBParticipant service. Represents a row in the &quot;BBBParticipant&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This class exists only as a container for the default extended model level methods generated by ServiceBuilder. Helper methods and all application logic should be put in {@link BBBParticipantImpl}.
 * </p>
 *
 * @author Shinn Lok
 * @see BBBParticipantImpl
 * @see com.liferay.bbb.model.BBBParticipant
 * @generated
 */
public abstract class BBBParticipantBaseImpl extends BBBParticipantModelImpl
	implements BBBParticipant {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a b b b participant model instance should use the {@link BBBParticipant} interface instead.
	 */
	@Override
	public void persist() throws SystemException {
		if (this.isNew()) {
			BBBParticipantLocalServiceUtil.addBBBParticipant(this);
		}
		else {
			BBBParticipantLocalServiceUtil.updateBBBParticipant(this);
		}
	}
}