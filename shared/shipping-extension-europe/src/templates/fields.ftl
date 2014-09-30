<#assign aui = PortletJspTagLibs["/META-INF/aui.tld"] />

<@aui["input"] disabled=true name="conversion-rate" suffix="EUR-USD" value=conversionRate />

<@aui["select"] label="destination" name="destination">
	<#list destinations as curDestination>
		<@aui["option"] label="${curDestination}" value=curDestination />
	</#list>
</@>

<@aui["input"] helpMessage="discount-code-help" label="discount-code" name="discountCode" />

<@aui["input"] disabled=true name="taxes" suffix="%" value=taxesPercent />