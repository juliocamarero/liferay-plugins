<#--
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
-->

<#include "init.ftl" />

<@portlet["actionURL"] name="calculateShippingCost" var="calculateShippingCostURL" />

<@aui["form"] action="${calculateShippingCostURL}" method="post" name="fm">
	<@aui["input"] name="redirect" type="hidden" value="${currentURL}" />

	<@aui["input"] label="item-quantity" name="itemQuantity">
		<@aui["validator"] name="digits" />
		<@aui["validator"] name="min">1</@>
		<@aui["validator"] name="required" />
	</@>

	<@aui["input"] label="price-per-item" name="itemPrice" suffix=defaultCurrency>
		<@aui["validator"] name="number" />
		<@aui["validator"] name="required" />
	</@>

	<#if themeDisplay.isSignedIn() && shippingExtensionItems?has_content>
		<@aui["select"] id="selectShippingExtension" inlineField=true label="shipping-to" name="shippingExtensionKey">
			<#list shippingExtensionItems as curShippingExtensionItem>
				<@aui["option"] data=curShippingExtensionItem.getShippingExtensionItemData(liferayPortletResponse) label="${curShippingExtensionItem.getName(locale)}" selected=(shippingExtension?? && (shippingExtension.getShippingExtensionKey() == curShippingExtensionItem.getShippingExtensionKey())) value="${curShippingExtensionItem.getShippingExtensionKey()}" />
			</#list>
		</@>

		<div id="<@portlet["namespace"] />shippingExtensionOptions" class="options-panel"></div>
	</#if>

	<#if shippingCost??>
		<@aui["input"] disabled=true name="total" suffix=shippingCurrency value=shippingCost />
	</#if>

	<@aui["button-row"]>
		<@aui["button"] value="calculate" type="submit" />
		<@aui["button"] value="clear" type="reset" />
	</@>
</@>

<#if themeDisplay.isSignedIn() && shippingExtensionItems?has_content>
	<@aui["script"] use="aui-base,aui-parse-content">
		var showShippingExtensionOptions = function(selectNode) {
			var selectedIndex = selectNode.get('selectedIndex');

			if (selectedIndex >= 0) {
				var selectedOption = selectNode.get('options').item(selectedIndex);

				var resourceURL = selectedOption.attr('data-resourceurl');

				var shippingExtensionOptions = A.one('#<@portlet["namespace"] />shippingExtensionOptions');

				A.io.request(
					resourceURL,
					{
						on: {
							success: function(event, id, obj) {
								shippingExtensionOptions.empty();

								shippingExtensionOptions.plug(A.Plugin.ParseContent);

								shippingExtensionOptions.setContent(this.get('responseData'));
							}
						}
					}
				);
			}
		}

		var selectShippingExtension = A.one('#<@portlet["namespace"] />selectShippingExtension');

		if (selectShippingExtension) {
			selectShippingExtension.on(
				'change',
				function(event) {
					showShippingExtensionOptions(event.currentTarget);
				}
			);

			showShippingExtensionOptions(selectShippingExtension);
		}
	</@>
</#if>