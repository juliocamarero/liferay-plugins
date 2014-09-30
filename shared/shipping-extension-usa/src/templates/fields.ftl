<#assign aui = PortletJspTagLibs["/META-INF/aui.tld"] />

<@aui["select"] label="destination" name="destination">
	<#list destinations as curDestination>
		<@aui["option"] label="${curDestination}" value=curDestination />
	</#list>
</@>

<@aui["input"] label="express-delivery" name="expressDelivery" type="checkbox" />

<@aui["input"] disabled=true name="taxes" suffix="%" value=taxesPercent />