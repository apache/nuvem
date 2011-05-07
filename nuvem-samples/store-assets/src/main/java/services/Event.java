/*
 * Licensed to the Apache Software Foundation (ASF) under one

 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */

package services;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Event {

	private Location loacationBeforeEvent = Location.UNKNOWN;

	private Location loacationAfterEvent = Location.UNKNOWN;

	private Type type;

	private Shipment eventFor;

	private static void generateAndTriggerEvent(Shipment shipment,
			Location currentLocation, Type type) {
		Event event = new Event();
		event.loacationBeforeEvent = shipment.getCurrentLocation();
		event.loacationAfterEvent = currentLocation;
		event.type = type;
		event.eventFor = shipment;
		shipment.updateStatus(event);
	}

	public Shipment eventFor() {
		return eventFor;
	}

	public static void newShipment(Shipment shipment) {
		generateAndTriggerEvent(shipment, shipment.getCurrentLocation(), Type.READY);
	}

	public static void updateShipment(Shipment shipment,
			Location currentLocation) {
		generateAndTriggerEvent(shipment, currentLocation, Type.INPROGRESS);
	}

	public static void triggerCancellation(Shipment shipment,
			Location currentLocation) {
		generateAndTriggerEvent(shipment, currentLocation, Type.CANCELED);
	}

	public static void triggerDeliverySuccessful(Shipment shipment,
			Location currentLocation) {
		generateAndTriggerEvent(shipment, currentLocation, Type.DELIVERED);
	}

	public Type type() {
		return type;
	}

	public Location loacationAfterEvent() {
		return loacationAfterEvent;
	}

	public Location loacationBeforeEvent() {
		return loacationBeforeEvent;
	}

	public static enum Type {
		READY, INPROGRESS, ONHOLD, CANCELED, DELIVERED;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		final Event rhs = (Event) o;

		return new EqualsBuilder().append(this.loacationBeforeEvent,
				rhs.loacationBeforeEvent).append(this.loacationAfterEvent,
				rhs.loacationAfterEvent).append(this.type, rhs.type).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(loacationBeforeEvent).append(
				loacationBeforeEvent).append(type).toHashCode();
	}

	@Override
	public String toString() {
		String description = null;
		switch (type) {
		case READY:
			description = "shipment ready for the voyage from : "
					+ this.loacationBeforeEvent;
			break;
		case INPROGRESS:
			description = "shipmet moved from location: "
					+ this.loacationBeforeEvent + " to location: "
					+ this.loacationAfterEvent;
			break;
		case ONHOLD:
			description = "shipment put on hold at location: "
					+ this.loacationBeforeEvent;
			break;
		case CANCELED:
			description = "shipment canceled at: " + this.loacationAfterEvent;
			break;
		case DELIVERED:
			description = "shipment delivered to the recipient";
		}
		return String.format(
				"update for Shipment with tracking code: %s -> %s ",
				this.eventFor.trackingCode(), description);
	}
}
