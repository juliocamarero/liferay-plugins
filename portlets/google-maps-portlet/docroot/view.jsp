<%--
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
--%>

<%@ include file="/init.jsp" %>

<%
mapAddress = GetterUtil.getString((String)portletSession.getAttribute("mapAddress"), mapAddress);
directionsAddress = GetterUtil.getString((String)portletSession.getAttribute("directionsAddress"), directionsAddress);
%>

<c:choose>
	<c:when test="<%= Validator.isNotNull(mapAddress) %>">
		<style>
			#<portlet:namespace />map {
				height: 100%;
			}

			#<portlet:namespace />google-maps-view{
				cursor: pointer;
			}

			.ie6 .maps-content img {
					behavior: expression(this.pngSet=true);
			}
		</style>

		<c:choose>
			<c:when test="<%= PortalUtil.isSecure(request) %>">
				<script src="https://maps-api-ssl.google.com/maps/api/js?v=3&sensor=true" type="text/javascript"></script>
			</c:when>
			<c:otherwise>
				<script type="text/javascript" src="https://maps.google.com/maps/api/js?sensor=true"></script>
			</c:otherwise>
		</c:choose>

		<form name="<portlet:namespace />fm">
		
			<c:if test="<%= mapInputEnabled %>">
				<input class="lfr-input-text" id="<portlet:namespace />mapAddress" name="<portlet:namespace />mapAddress" onKeyPress="if (event.keyCode == 13) { <portlet:namespace />getMap(); return false; }" type="text" value="<%= mapAddress %>" />
		
				<input type="button" value="<liferay-ui:message key="get-map" />" onClick="<portlet:namespace />getMap();" />
			</c:if>
		
			<c:if test="<%= directionsInputEnabled %>">
				<input class="lfr-input-text" id="<portlet:namespace />directionsAddress" name="<portlet:namespace />directionsAddress" onKeyPress="<portlet:namespace />openInGoogleMaps();" type="text" value="<%= directionsAddress %>" />
		
				<input type="button" value="<liferay-ui:message key="get-directions" />" onClick="<portlet:namespace />openInGoogleMaps();" />
			</c:if>
		
			<div class="maps-content" id="<portlet:namespace />map" style="height: <%= height %>px; width: 100%;"></div>

		</form>
		
		<aui:script>
			var <portlet:namespace />googleMap;
			var <portlet:namespace />geocoder;

			function <portlet:namespace />initMap() {

				<portlet:namespace />geocoder = new google.maps.Geocoder();

				var myOptions = {
					zoom: 8,
					mapTypeId: google.maps.MapTypeId.ROADMAP
		  		}

				/* Create a map and center it on default address */

				<portlet:namespace />googleMap = new google.maps.Map(document.getElementById("<portlet:namespace />map"), myOptions);
			}

			<portlet:namespace />codeAddress();

			function <portlet:namespace />codeAddress() {
				<portlet:namespace />initMap();
				var address = <portlet:namespace />getMapAddress();
				<portlet:namespace />geocoder.geocode( {'address': address}, function(results, status) {
					if (status == google.maps.GeocoderStatus.OK) {
						<portlet:namespace />googleMap.setCenter(results[0].geometry.location);
						var marker = new google.maps.Marker({
							map: <portlet:namespace />googleMap,
							position: results[0].geometry.location
						});
					} else {
						alert("Geocode was not successful for the following reason: " + status);
					}
				});
			}
		
			function <portlet:namespace />getMap() {
				var mapAddress = <portlet:namespace />getMapAddress();

				<portlet:namespace />codeAddress(mapAddress);
				<portlet:namespace />saveMapAddress(mapAddress);
		
				return mapAddress;
			}
		
			function <portlet:namespace />getMapAddress() {
				var mapAddress = "<%= mapAddress %>";
		
				<c:if test="<%= mapInputEnabled %>">
					mapAddress = document.<portlet:namespace />fm.<portlet:namespace />mapAddress.value;
				</c:if>
		
				return mapAddress;
			}
		
			Liferay.provide(
				window,
				'<portlet:namespace />saveDirectionsAddress',
				function(address) {
					var A = AUI();
		
					A.io.request(
						'<portlet:actionURL><portlet:param name="<%= Constants.CMD %>" value="saveDirectionsAddress" /></portlet:actionURL>',
						{
							data: {
								directionsAddress: address
							}
						}
					);
				},
				['aui-io-request']
			);
		
			Liferay.provide(
				window,
				'<portlet:namespace />saveMapAddress',
				function(address) {
					var A = AUI();
		
					A.io.request(
						'<portlet:actionURL><portlet:param name="<%= Constants.CMD %>" value="saveMapAddress" /></portlet:actionURL>',
						{
							data: {
								mapAddress: address
							}
						}
					);
				},
				['aui-io-request']
			);
		</aui:script>
	</c:when>
	<c:otherwise>
		<liferay-util:include page="/html/portal/portlet_not_setup.jsp" />
	</c:otherwise>
</c:choose>