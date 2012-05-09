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
				<script src="http://maps.google.com/maps/api/js?sensor=true" type="text/javascript"></script>
			</c:otherwise>
		</c:choose>

		<aui:form name="fm">
			<c:choose>
				<c:when test="<%= mapInputEnabled %>">
					<aui:input cssClass="lfr-input-text" inlineField="<%= true %>" name="mapAddress" type="text" value="<%= mapAddress %>" />

					<aui:button name="getMapButton" value="get-map" />

					<c:if test="<%= enableRouteCalculation %>">
						<aui:input cssClass="lfr-input-text" inlineField="<%= true %>"  name="directionsAddress" type="text" value="<%= directionsAddress %>" />

						<c:choose>
							<c:when test="<%= allowChangeTravellingMode %>">
								<aui:select inlineField="<%= true %>" name="travellingMode">
									<aui:option label="<%= GoogleMapsConstants.DRIVING %>" />
									<aui:option label="<%= GoogleMapsConstants.WALKING %>" />
									<aui:option label="<%= GoogleMapsConstants.BICYCLING %>" />
								</aui:select>
							</c:when>
							<c:otherwise>
								<aui:input name="travellingMode" type="hidden" value="<%= GoogleMapsConstants.DRIVING %>" />
							</c:otherwise>
						</c:choose>

						<aui:button name="getDirectionsButton" value="get-directions" />
					</c:if>

					<div style="padding-top: 5px;"></div>

				</c:when>
				<c:otherwise>
					<aui:input name="mapAddress" type="hidden" value="<%= mapAddress %>" />

					<c:if test="<%= enableRouteCalculation %>">
						<aui:input name="directionsAddress" type="hidden" value="<%= directionsAddress %>" />
					</c:if>

					<aui:input name="travellingMode" type="hidden" value="<%= GoogleMapsConstants.DRIVING %>" />
				</c:otherwise>
			</c:choose>

			<div class="maps-content" id="<portlet:namespace />map" style="height: <%= height %>px; width: 100%;"></div>

			<c:if test="<%= enableRouteCalculation %>">
				<div style="padding-top: 5px;">
					<a href="javascript:;" id="<portlet:namespace />openInGoogleMapsLink"><liferay-ui:message key="open-in-google-maps" /></a>
				</div>
			</c:if>

			<div id="<portlet:namespace />warnings_panel"></div>
		</aui:form>

		<aui:script use="aui-base,aui-io-request">
			var <portlet:namespace />directionDisplay;
			var <portlet:namespace />directionsService;
			var <portlet:namespace />geocoder;
			var <portlet:namespace />map;
			var <portlet:namespace />markerArray = [];
			var <portlet:namespace />stepDisplay;

			function <portlet:namespace />initMap() {
				<portlet:namespace />geocoder = new google.maps.Geocoder();

				<portlet:namespace />directionsService = new google.maps.DirectionsService();

				var myOptions = {
					mapTypeId: google.maps.MapTypeId.ROADMAP,
					zoom: 8
				}

				<portlet:namespace />map = new google.maps.Map(document.getElementById("<portlet:namespace />map"), myOptions);

				var rendererOptions = {
					map: <portlet:namespace />map
				}

				<portlet:namespace />directionsDisplay = new google.maps.DirectionsRenderer(rendererOptions);

				<portlet:namespace />stepDisplay = new google.maps.InfoWindow();

				var openInGoogleMapsLink = A.one('#<portlet:namespace />openInGoogleMapsLink');

				if (openInGoogleMapsLink) {
					openInGoogleMapsLink.on(
						'click',
						function(event) {
							var fromAddress = <portlet:namespace />getMapAddress();

							var toAddress = document.<portlet:namespace />fm.<portlet:namespace />directionsAddress.value;

							window.open("http://maps.google.com/maps?f=q&hl=en&q=" + encodeURIComponent(fromAddress) + "+to+" + encodeURIComponent(toAddress));
						}
					);
				}

				var getDirectionsButton = A.one('#<portlet:namespace />getDirectionsButton');

				if (getDirectionsButton) {
					getDirectionsButton.on(
						'click',
						function(event) {
							<portlet:namespace />calculateRoute();
						}
					);
				}

				var travellingMode = A.one('#<portlet:namespace />travellingMode');

				if (travellingMode) {
					travellingMode.on(
						'change',
						function(event) {
							<portlet:namespace />calculateRoute();
						}
					);
				}

				var getMapButton = A.one('#<portlet:namespace />getMapButton');

				if (getMapButton) {
					getMapButton.on(
						'click',
						function(event) {
							<portlet:namespace />getMap();
						}
					);
				}

				var directionsAddress = A.one('#<portlet:namespace />directionsAddress');

				if (directionsAddress) {
					directionsAddress.on(
						'keyPress',
						function(event) {
							if (event.keyCode == 13) {
								<portlet:namespace />calculateRoute();
								return false;
							}
						}
					);
				}

				var mapAddress = A.one('#<portlet:namespace />directionsAddress');

				if (mapAddress) {
					mapAddress.on(
						'keyPress',
						function(event) {
							if (event.keyCode == 13) {
								<portlet:namespace />getMap();
								return false;
							}
						}
					);
				}
			}

			<c:choose>
				<c:when test="<%= enableRouteCalculation %>">
					<portlet:namespace />calculateRoute();
				</c:when>
				<c:otherwise>
					<portlet:namespace />getAddress();
				</c:otherwise>
			</c:choose>

			<c:if test="<%= enableRouteCalculation %>">
				function <portlet:namespace />attachInstructionText(marker, text) {
					google.maps.event.addListener(
						marker,
						'click',
						function() {
							<portlet:namespace />stepDisplay.setContent(text);

							<portlet:namespace />stepDisplay.open(<portlet:namespace />map, marker);
						}
					);
				}

				function <portlet:namespace />calculateRoute() {
					<portlet:namespace />initMap();

					var fromAddress = <portlet:namespace />getMapAddress();

					var toAddress = document.<portlet:namespace />fm.<portlet:namespace />directionsAddress.value;

					var travellingMode = document.getElementById("<portlet:namespace />travellingMode").value;

					var request = {
						destination: toAddress,
						origin: fromAddress,
						travelMode: google.maps.TravelMode[travellingMode]
					};

					<portlet:namespace />directionsService.route(
						request,
						function(response, status) {
							if (status == google.maps.DirectionsStatus.OK) {
								var warnings = document.getElementById("<portlet:namespace />warnings_panel");
								warnings.innerHTML = "" + response.routes[0].warnings + "";

								<portlet:namespace />directionsDisplay.setDirections(response);

								<portlet:namespace />showSteps(response);

								<portlet:namespace />saveDirectionsAddress(toAddress);
							}
							else {
								//alert(status);
							}
						}
					);
				}
			</c:if>

			function <portlet:namespace />getAddress() {
				<portlet:namespace />initMap();

				var address = <portlet:namespace />getMapAddress();

				<portlet:namespace />geocoder.geocode(
					{
						'address': address
					},
					function(results, status) {
						if (status == google.maps.GeocoderStatus.OK) {
							var location = results[0].geometry.location;

							<portlet:namespace />map.setCenter(location);

							var marker = new google.maps.Marker(
								{
									map: <portlet:namespace />map,
									position: location
								}
							);
						} else {
							//alert(status);
						}
					}
				);
			}

			function <portlet:namespace />getMap() {
				var mapAddress = <portlet:namespace />getMapAddress();

				<portlet:namespace />getAddress(mapAddress);
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

			<c:if test="<%= enableRouteCalculation %>">
				function <portlet:namespace />showSteps(directionResult) {

					/* For each step, place a marker, and add the text to the marker's info window.
					Also attach the marker to an array so we can keep track of it and remove it
					when calculating new routes.*/

					var myRoute = directionResult.routes[0].legs[0];

					for (var i = 0; i < myRoute.steps.length; i++) {
						var marker = new google.maps.Marker(
							{
								position: myRoute.steps[i].start_point,
								map: <portlet:namespace />map
							}
						);

						<portlet:namespace />attachInstructionText(marker, myRoute.steps[i].instructions);

						<portlet:namespace />markerArray[i] = marker;
					}
				}
			</c:if>

			function <portlet:namespace />saveDirectionsAddress(address) {
				A.io.request(
					'<portlet:actionURL><portlet:param name="<%= Constants.CMD %>" value="saveDirectionsAddress" /></portlet:actionURL>',
					{
						data: {
							directionsAddress: address
						}
					}
				);
			}

			function <portlet:namespace />saveMapAddress(address) {
				A.io.request(
					'<portlet:actionURL><portlet:param name="<%= Constants.CMD %>" value="saveMapAddress" /></portlet:actionURL>',
					{
						data: {
							mapAddress: address
						}
					}
				);
			}
		</aui:script>
	</c:when>
	<c:otherwise>
		<liferay-util:include page="/html/portal/portlet_not_setup.jsp" />
	</c:otherwise>
</c:choose>