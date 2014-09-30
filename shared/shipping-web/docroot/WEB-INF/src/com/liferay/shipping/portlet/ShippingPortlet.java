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

package com.liferay.shipping.portlet;

import com.liferay.osgi.util.service.ServiceTrackerUtil;
import com.liferay.portal.kernel.io.unsync.UnsyncStringWriter;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.template.TemplateException;
import com.liferay.portal.kernel.template.TemplateManagerUtil;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.template.TemplateResourceLoaderUtil;
import com.liferay.portal.kernel.template.TemplateTaglibSupportProvider;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.UnsyncPrintWriterPool;
import com.liferay.portal.util.PortalUtil;
import com.liferay.shipping.api.model.ShippingCalculator;
import com.liferay.shipping.api.model.ShippingExtension;
import com.liferay.shipping.api.model.ShippingExtensionRegistry;
import com.liferay.util.bridges.freemarker.FreeMarkerPortlet;

import freemarker.ext.servlet.HttpRequestHashModel;

import freemarker.template.ObjectWrapper;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.MimeResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.UnavailableException;

import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponseWrapper;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * @author Julio Camarero
 * @author Eduardo Garcia
 */
public class ShippingPortlet extends FreeMarkerPortlet {

	public void calculateShippingCost(
			ActionRequest request, ActionResponse response)
		throws Exception {

		int itemQuantity = ParamUtil.getInteger(request, "itemQuantity");

		BigDecimal itemPrice = new BigDecimal(
			ParamUtil.getDouble(request, "itemPrice"));

		BigDecimal shippingCost = _shippingCalculator.calculateShippingCost(
			itemQuantity, itemPrice);

		String shippingExtensionKey = ParamUtil.getString(
			request, "shippingExtensionKey");

		ShippingExtension shippingExtension =
			_shippingExtensionRegistry.getShippingExtension(
				shippingExtensionKey);

		if (shippingExtension != null) {
			shippingCost = shippingExtension.calculateShippingCost(
				request, response, shippingCost);
		}

		PortalUtil.copyRequestParameters(request, response);

		response.setRenderParameter(
			"shippingCost", _shippingCalculator.format(shippingCost));

		PortletSession portletSession = request.getPortletSession();

		portletSession.setAttribute("shippingExtension", shippingExtension);

		SessionMessages.add(request, "shippingCostCalculated");
	}

	@Override
	public void init() throws PortletException {
		super.init();

		Bundle bundle = FrameworkUtil.getBundle(getClass());

		if (bundle == null) {
			throw new UnavailableException(
				"Can't find a reference to the OSGi bundle") {

				@Override
				public boolean isPermanent() {
					return true;
				}
			};
		}

		_shippingCalculator = ServiceTrackerUtil.getService(
			ShippingCalculator.class, bundle.getBundleContext());
		_shippingExtensionRegistry = ServiceTrackerUtil.getService(
			ShippingExtensionRegistry.class, bundle.getBundleContext());
	}

	@Override
	public void serveResource(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws IOException, PortletException {

		String shippingExtensionKey = ParamUtil.getString(
			resourceRequest, "shippingExtensionKey");

		ShippingExtension shippingExtension =
			_shippingExtensionRegistry.getShippingExtension(
				shippingExtensionKey);

		if (shippingExtension == null) {
			return;
		}

		PortletContext portletContext = getPortletContext();

		String servletContextName = portletContext.getPortletContextName();

		String resourcePath = servletContextName.concat(
			TemplateConstants.SERVLET_SEPARATOR).concat("html/view.ftl");

		OutputStream outputStream = resourceResponse.getPortletOutputStream();

		try {
			Template template = getTemplateWithTaglibSupport(
				resourceRequest, resourceResponse, servletContextName,
				resourcePath);

			String shippingExtensionHTMLFields =
				shippingExtension.getFieldsHTML(cloneTemplateContext(template));

			resourceResponse.setContentType(ContentTypes.TEXT);

			outputStream.write(
				shippingExtensionHTMLFields.getBytes(StringPool.UTF8));
		}
		catch (Exception e) {
			_log.error("Cannot process shipping extension template");
		}
		finally {
			outputStream.close();
		}
	}

	protected Map<String, Object> cloneTemplateContext(Template template) {
		Map<String, Object> context = new HashMap<String, Object>();

		for (String key : template.getKeys()) {
			context.put(key, template.get(key));
		}

		return context;
	}

	protected Template getTemplateWithTaglibSupport(
			PortletRequest portletRequest, PortletResponse portletResponse,
			String servletContextName, String resourcePath)
		throws Exception {

		TemplateResource templateResource =
			TemplateResourceLoaderUtil.getTemplateResource(
				TemplateConstants.LANG_TYPE_FTL, resourcePath);

		Template template = TemplateManagerUtil.getTemplate(
			TemplateConstants.LANG_TYPE_FTL, templateResource, false);

		TemplateTaglibSupportProvider templateTaglibSupportProvider =
			getTaglibSupportProvider();

		if (templateTaglibSupportProvider != null) {
			templateTaglibSupportProvider.addTaglibSupport(
				template, servletContextName, portletRequest, portletResponse);
		}

		// LPS-43725

		HttpServletRequestWrapper httpServletRequestWrapper =
			new HttpServletRequestWrapper(
				PortalUtil.getHttpServletRequest(portletRequest));

		HttpServletResponseWrapper httpServletResponseWrapper =
			new HttpServletResponseWrapper(
				PortalUtil.getHttpServletResponse(portletResponse));

		HttpRequestHashModel httpRequestHashModel =
			new HttpRequestHashModel(
				httpServletRequestWrapper, httpServletResponseWrapper,
				ObjectWrapper.DEFAULT_WRAPPER);

		template.put("Request", httpRequestHashModel);

		return template;
	}

	@Override
	protected void include(
			String path, PortletRequest portletRequest,
			PortletResponse portletResponse, String lifecycle)
		throws IOException, PortletException {

		PortletContext portletContext = getPortletContext();

		String servletContextName = portletContext.getPortletContextName();

		String resourcePath = servletContextName.concat(
			TemplateConstants.SERVLET_SEPARATOR).concat(path);

		boolean resourceExists = false;

		try {
			resourceExists = TemplateResourceLoaderUtil.hasTemplateResource(
				TemplateConstants.LANG_TYPE_FTL, resourcePath);
		}
		catch (TemplateException te) {
			throw new IOException(te.getMessage());
		}

		if (!resourceExists) {
			_log.error(path + " is not a valid include");
		}
		else {
			try {
				Template template = getTemplateWithTaglibSupport(
					portletRequest, portletResponse, servletContextName,
					resourcePath);

				populateContext(portletRequest, portletResponse, template);

				Writer writer = null;

				if (portletResponse instanceof MimeResponse) {
					MimeResponse mimeResponse = (MimeResponse)portletResponse;

					writer = UnsyncPrintWriterPool.borrow(
						mimeResponse.getWriter());
				}
				else {
					writer = new UnsyncStringWriter();
				}

				template.processTemplate(writer);
			}
			catch (Exception e) {
				throw new PortletException(e);
			}
		}

		if (clearRequestParameters) {
			if (lifecycle.equals(PortletRequest.RENDER_PHASE)) {
				portletResponse.setProperty("clear-request-parameters", "true");
			}
		}
	}

	protected void populateContext(
			PortletRequest portletRequest, PortletResponse portletResponse,
			Template template)
		throws Exception {

		template.put("currentURL", PortalUtil.getCurrentURL(portletRequest));
		template.put(
			"defaultCurrency", _shippingCalculator.getDefaultCurrency());

		List<ShippingExtensionItem> shippingExtensionItems =
			new ArrayList<ShippingExtensionItem>();

		for (ShippingExtension shippingExtension :
				_shippingExtensionRegistry.getShippingExtensions()) {

			shippingExtensionItems.add(
				new ShippingExtensionItem(shippingExtension));
		}

		template.put(
			"shippingCost",
			ParamUtil.getString(portletRequest, "shippingCost", null));
		template.put("shippingExtensionItems", shippingExtensionItems);

		PortletSession portletSession = portletRequest.getPortletSession();

		ShippingExtension shippingExtension =
			(ShippingExtension)portletSession.getAttribute("shippingExtension");

		template.put("shippingExtension", shippingExtension);

		String shippingCurrency = _shippingCalculator.getDefaultCurrency();

		if (shippingExtension != null) {
			shippingCurrency = shippingExtension.getCurrency();
		}

		template.put("shippingCurrency", shippingCurrency);
	}

	private static Log _log = LogFactoryUtil.getLog(ShippingPortlet.class);

	private ShippingCalculator _shippingCalculator;
	private ShippingExtensionRegistry _shippingExtensionRegistry;

}