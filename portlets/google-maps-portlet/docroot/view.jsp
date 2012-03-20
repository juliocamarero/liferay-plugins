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

<style>
	#map {
		height: 100%;
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
		<input class="lfr-input-text" id="<portlet:namespace />directionsAddress" name="<portlet:namespace />directionsAddress" onKeyPress="if (event.keyCode == 13) { <portlet:namespace />calcRoute(); return false; }" type="text" value="<%= directionsAddress %>" />

		<select id="<portlet:namespace />travellingMode">
			<option value="DRIVING"><liferay-ui:message key="driving" /></option>
			<option value="WALKING"><liferay-ui:message key="walking" /></option>
			<option value="BICYCLING"><liferay-ui:message key="bicycling" /></option>
		</select>

		<input type="button" value="<liferay-ui:message key="get-directions" />" onClick="<portlet:namespace />calcRoute();" />
	</c:if>

	<c:if test="<%= mapInputEnabled || directionsInputEnabled %>">
		<div style="padding-top: 5px;"></div>
	</c:if>

	<div class="maps-content" id="<portlet:namespace />map" style="height: <%= height %>px; width: 100%;"></div>

	<c:if test="<%= directionsInputEnabled %>">
		<div style="padding-top: 5px;">
			<a id="<portlet:namespace />google-maps-view" onClick="<portlet:namespace />openInGoogleMaps();"><liferay-ui:message key="open-in-google-maps" /></a>
		</div>
	</c:if>

	<div id="<portlet:namespace />warnings_panel"></div>
</form>

<aui:script>
	var <portlet:namespace />directionDisplay;
	var <portlet:namespace />directionsService;
	var <portlet:namespace />googleMap;
	var <portlet:namespace />geocoder;
	var <portlet:namespace />markerArray = [];
	var <portlet:namespace />stepDisplay;

	function <portlet:namespace />initMap() {

		<portlet:namespace />geocoder = new google.maps.Geocoder();

		<portlet:namespace />directionsService = new google.maps.DirectionsService();
		
		var myOptions = {
			zoom: 8,
			mapTypeId: google.maps.MapTypeId.ROADMAP
  		}

		/* Create a map and center it on default address */

		<portlet:namespace />googleMap = new google.maps.Map(document.getElementById("<portlet:namespace />map"), myOptions);

		/* Create a renderer for directions and bind it to the map. */

		var rendererOptions = {
			map: <portlet:namespace />googleMap
		}
		<portlet:namespace />directionsDisplay = new google.maps.DirectionsRenderer(rendererOptions);

		/* Instantiate an info window to hold step text. */

		<portlet:namespace />stepDisplay = new google.maps.InfoWindow();
	}

	<portlet:namespace />codeAddress();

	function <portlet:namespace />attachInstructionText(marker, text) {
		google.maps.event.addListener(marker, 'click', function() {
			<portlet:namespace />stepDisplay.setContent(text);
			<portlet:namespace />stepDisplay.open(<portlet:namespace />googleMap, marker);
		});
	}

	function <portlet:namespace />calcRoute() {
		<portlet:namespace />initMap();
		var fromAddress = <portlet:namespace />getMapAddress();
		var toAddress = document.<portlet:namespace />fm.<portlet:namespace />directionsAddress.value;
		var travellingMode = document.getElementById("<portlet:namespace />travellingMode").value;

		var request = {
			origin: fromAddress,
			destination: toAddress,
			travelMode: google.maps.TravelMode[travellingMode]
		};

		<portlet:namespace />directionsService.route(request, function(response, status) {
			if (status == google.maps.DirectionsStatus.OK) {
				var warnings = document.getElementById("<portlet:namespace />warnings_panel");
				warnings.innerHTML = "" + response.routes[0].warnings + "";
				<portlet:namespace />directionsDisplay.setDirections(response);
				<portlet:namespace />showSteps(response);

				<portlet:namespace />saveDirectionsAddress(toAddress);
			}
		});
	}

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

	<c:if test="<%= directionsInputEnabled %>">
		function <portlet:namespace />openInGoogleMaps() {
			var fromAddress = <portlet:namespace />getMapAddress();
			var toAddress = document.<portlet:namespace />fm.<portlet:namespace />directionsAddress.value;
			var travellingMode = document.getElementById("<portlet:namespace />travellingMode").value;

			window.open("http://maps.google.com/maps?f=q&hl=en&q=" + encodeURIComponent(fromAddress) + "+to+" + encodeURIComponent(toAddress));
		}
	</c:if>

	function <portlet:namespace />showSteps(directionResult) {

		/* For each step, place a marker, and add the text to the marker's
		info window. Also attach the marker to an array so we
		can keep track of it and remove it when calculating new
		routes.*/

		var myRoute = directionResult.routes[0].legs[0];

		for (var i = 0; i < myRoute.steps.length; i++) {
			var marker = new google.maps.Marker({
				position: myRoute.steps[i].start_point,
				map: <portlet:namespace />googleMap
			});
			<portlet:namespace />attachInstructionText(marker, myRoute.steps[i].instructions);
			<portlet:namespace />markerArray[i] = marker;
		}
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
