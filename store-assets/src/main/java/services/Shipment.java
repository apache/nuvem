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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.nuvem.cloud.xmpp.api.JID;

import services.Item;
import services.ShipmentObserver;

public class Shipment {

	private TrackingCode code;

	private List<Item> items;

	private Address deliveryAddress;

	private JID jidToUpdate;

	private List<Event> history = new ArrayList<Event>();

	private Location currentLocation;

	private List<ShipmentObserver> observers = new ArrayList<ShipmentObserver>();

	public Shipment(List<Item> items, Address address, JID jid) {
		this.items = new ArrayList<Item>();
		this.items.addAll(items);
		this.deliveryAddress = address;
		this.jidToUpdate = jid;
		this.currentLocation = new Location("starting location",
				new LocationCode("STR"));
	}

	public void startFrom(Location location) {
		this.currentLocation = location;
	}

	public void setTrackingCode(TrackingCode code) {
		this.code = code;
	}

	public TrackingCode trackingCode() {
		return code;
	}

	public List<Item> getItems() {
		return Collections.unmodifiableList(items);
	}

	public Address getDeliveryAddress() {
		return deliveryAddress;
	}

	public JID getJidToUpdate() {
		return jidToUpdate;
	}

	public void updateStatus(Event event) {
		for (ShipmentObserver observer : observers) {
			observer.onStatusUpdate(this, event);
		}
		this.currentLocation = event.loacationAfterEvent();
		this.history.add(event);
	}

	public void addObserver(ShipmentObserver observer) {
		observers.add(observer);
	}

	public List<Event> history() {
		return history;
	}

	public Location getCurrentLocation() {
		return currentLocation;
	}

}
