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

package com.liferay.portalsafe.service;

import com.liferay.portal.service.InvokableLocalService;

/**
 * @author Tomáš Polešovský
 */
public class PortalSafeLocalServiceClp implements PortalSafeLocalService {
	public PortalSafeLocalServiceClp(
		InvokableLocalService invokableLocalService) {
		_invokableLocalService = invokableLocalService;

		_methodName0 = "getBeanIdentifier";

		_methodParameterTypes0 = new String[] {  };

		_methodName1 = "setBeanIdentifier";

		_methodParameterTypes1 = new String[] { "java.lang.String" };

		_methodName3 = "getStorage";

		_methodParameterTypes3 = new String[] {  };

		_methodName4 = "listItemsNames";

		_methodParameterTypes4 = new String[] { "long", "long" };

		_methodName5 = "loadItem";

		_methodParameterTypes5 = new String[] { "long", "long", "java.lang.String" };

		_methodName6 = "removeItem";

		_methodParameterTypes6 = new String[] { "long", "long", "java.lang.String" };

		_methodName7 = "saveItem";

		_methodParameterTypes7 = new String[] {
				"long", "long", "com.liferay.portal.kernel.safe.model.Item"
			};

		_methodName8 = "setStorage";

		_methodParameterTypes8 = new String[] {
				"com.liferay.portal.kernel.safe.storage.Storage"
			};
	}

	public java.lang.String getBeanIdentifier() {
		Object returnObj = null;

		try {
			returnObj = _invokableLocalService.invokeMethod(_methodName0,
					_methodParameterTypes0, new Object[] {  });
		}
		catch (Throwable t) {
			t = ClpSerializer.translateThrowable(t);

			if (t instanceof RuntimeException) {
				throw (RuntimeException)t;
			}
			else {
				throw new RuntimeException(t.getClass().getName() +
					" is not a valid exception");
			}
		}

		return (java.lang.String)ClpSerializer.translateOutput(returnObj);
	}

	public void setBeanIdentifier(java.lang.String beanIdentifier) {
		try {
			_invokableLocalService.invokeMethod(_methodName1,
				_methodParameterTypes1,
				new Object[] { ClpSerializer.translateInput(beanIdentifier) });
		}
		catch (Throwable t) {
			t = ClpSerializer.translateThrowable(t);

			if (t instanceof RuntimeException) {
				throw (RuntimeException)t;
			}
			else {
				throw new RuntimeException(t.getClass().getName() +
					" is not a valid exception");
			}
		}
	}

	public java.lang.Object invokeMethod(java.lang.String name,
		java.lang.String[] parameterTypes, java.lang.Object[] arguments)
		throws java.lang.Throwable {
		throw new UnsupportedOperationException();
	}

	public com.liferay.portal.kernel.safe.storage.Storage getStorage() {
		Object returnObj = null;

		try {
			returnObj = _invokableLocalService.invokeMethod(_methodName3,
					_methodParameterTypes3, new Object[] {  });
		}
		catch (Throwable t) {
			t = ClpSerializer.translateThrowable(t);

			if (t instanceof RuntimeException) {
				throw (RuntimeException)t;
			}
			else {
				throw new RuntimeException(t.getClass().getName() +
					" is not a valid exception");
			}
		}

		return (com.liferay.portal.kernel.safe.storage.Storage)ClpSerializer.translateOutput(returnObj);
	}

	public java.util.List<java.lang.String> listItemsNames(long companyId,
		long groupId) throws com.liferay.portal.kernel.safe.PortalSafeException {
		Object returnObj = null;

		try {
			returnObj = _invokableLocalService.invokeMethod(_methodName4,
					_methodParameterTypes4, new Object[] { companyId, groupId });
		}
		catch (Throwable t) {
			t = ClpSerializer.translateThrowable(t);

			if (t instanceof com.liferay.portal.kernel.safe.PortalSafeException) {
				throw (com.liferay.portal.kernel.safe.PortalSafeException)t;
			}

			if (t instanceof RuntimeException) {
				throw (RuntimeException)t;
			}
			else {
				throw new RuntimeException(t.getClass().getName() +
					" is not a valid exception");
			}
		}

		return (java.util.List<java.lang.String>)ClpSerializer.translateOutput(returnObj);
	}

	public com.liferay.portal.kernel.safe.model.Item loadItem(long companyId,
		long groupId, java.lang.String name)
		throws com.liferay.portal.kernel.safe.PortalSafeException {
		Object returnObj = null;

		try {
			returnObj = _invokableLocalService.invokeMethod(_methodName5,
					_methodParameterTypes5,
					new Object[] {
						companyId,
						
					groupId,
						
					ClpSerializer.translateInput(name)
					});
		}
		catch (Throwable t) {
			t = ClpSerializer.translateThrowable(t);

			if (t instanceof com.liferay.portal.kernel.safe.PortalSafeException) {
				throw (com.liferay.portal.kernel.safe.PortalSafeException)t;
			}

			if (t instanceof RuntimeException) {
				throw (RuntimeException)t;
			}
			else {
				throw new RuntimeException(t.getClass().getName() +
					" is not a valid exception");
			}
		}

		return (com.liferay.portal.kernel.safe.model.Item)ClpSerializer.translateOutput(returnObj);
	}

	public void removeItem(long companyId, long groupId, java.lang.String name)
		throws com.liferay.portal.kernel.safe.PortalSafeException {
		try {
			_invokableLocalService.invokeMethod(_methodName6,
				_methodParameterTypes6,
				new Object[] {
					companyId,
					
				groupId,
					
				ClpSerializer.translateInput(name)
				});
		}
		catch (Throwable t) {
			t = ClpSerializer.translateThrowable(t);

			if (t instanceof com.liferay.portal.kernel.safe.PortalSafeException) {
				throw (com.liferay.portal.kernel.safe.PortalSafeException)t;
			}

			if (t instanceof RuntimeException) {
				throw (RuntimeException)t;
			}
			else {
				throw new RuntimeException(t.getClass().getName() +
					" is not a valid exception");
			}
		}
	}

	public void saveItem(long companyId, long groupId,
		com.liferay.portal.kernel.safe.model.Item item)
		throws com.liferay.portal.kernel.safe.PortalSafeException {
		try {
			_invokableLocalService.invokeMethod(_methodName7,
				_methodParameterTypes7,
				new Object[] {
					companyId,
					
				groupId,
					
				ClpSerializer.translateInput(item)
				});
		}
		catch (Throwable t) {
			t = ClpSerializer.translateThrowable(t);

			if (t instanceof com.liferay.portal.kernel.safe.PortalSafeException) {
				throw (com.liferay.portal.kernel.safe.PortalSafeException)t;
			}

			if (t instanceof RuntimeException) {
				throw (RuntimeException)t;
			}
			else {
				throw new RuntimeException(t.getClass().getName() +
					" is not a valid exception");
			}
		}
	}

	public void setStorage(
		com.liferay.portal.kernel.safe.storage.Storage storage) {
		try {
			_invokableLocalService.invokeMethod(_methodName8,
				_methodParameterTypes8,
				new Object[] { ClpSerializer.translateInput(storage) });
		}
		catch (Throwable t) {
			t = ClpSerializer.translateThrowable(t);

			if (t instanceof RuntimeException) {
				throw (RuntimeException)t;
			}
			else {
				throw new RuntimeException(t.getClass().getName() +
					" is not a valid exception");
			}
		}
	}

	private InvokableLocalService _invokableLocalService;
	private String _methodName0;
	private String[] _methodParameterTypes0;
	private String _methodName1;
	private String[] _methodParameterTypes1;
	private String _methodName3;
	private String[] _methodParameterTypes3;
	private String _methodName4;
	private String[] _methodParameterTypes4;
	private String _methodName5;
	private String[] _methodParameterTypes5;
	private String _methodName6;
	private String[] _methodParameterTypes6;
	private String _methodName7;
	private String[] _methodParameterTypes7;
	private String _methodName8;
	private String[] _methodParameterTypes8;
}