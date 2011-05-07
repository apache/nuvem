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

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Registry/Container for all shipments.
 * 
 * @author john
 * 
 */
public final class ShipmentRegistry {

	private static final Logger log = Logger.getLogger(ShipmentRegistry.class
			.getName());

	private static final ShipmentRegistry INSTANCE = new ShipmentRegistry();

	private Simulator simulator;

	private Map<TrackingCode, Shipment> shipments = new ConcurrentHashMap<TrackingCode, Shipment>();

	private BlockingQueue<TrackingCode> trackingCodes = new ArrayBlockingQueue<TrackingCode>(
			10);

	private ShipmentRegistry() {
		// simulator = new Simulator(trackingCodes);
		// new Thread(simulator).start();
	}

	public static ShipmentRegistry getInstance() {
		return INSTANCE;
	}

	public void shipmentHandled(TrackingCode code, Location newLocation) {
		Shipment shipment = shipments.get(code);
		Event.updateShipment(shipment, newLocation);
	}

	public void cancelShipment(TrackingCode trackingCode) {
		Shipment shipment = shipments.get(trackingCode);
		Event.triggerCancellation(shipment, shipment.getCurrentLocation());
	}

	public void markShipmentDelivered(TrackingCode trackingCode) {
		Shipment shipment = shipments.get(trackingCode);
		Event.triggerDeliverySuccessful(shipment, shipment.getDeliveryAddress()
				.getStreet());
	}

	public String addNewShipment(Shipment shipment) throws ShipmentException {
		TrackingCode code = TrackingCode.getNewTrackingCode();
		shipment.setTrackingCode(code);
		shipments.put(code, shipment);
		Event.newShipment(shipment);
		// trackingCodes.add(code);

		// shipment simulation done in the same thread as GAE doesnt allow
		// creation of threads.
		shipmentHandled(code, new Location("inter1", new LocationCode("INT1")));
		try {
			Thread.sleep(5000);

			shipmentHandled(code, new Location("inter2", new LocationCode(
					"INT2")));
			Thread.sleep(5000);
			shipmentHandled(code, new Location("inter3", new LocationCode(
					"INT3")));
			Thread.sleep(5000);
			shipmentHandled(code, new Location("inter4", new LocationCode(
					"INT4")));
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO:fix me
			log.severe("error during shipment");
			throw new ShipmentException("shipment error", e);
		}
		markShipmentDelivered(code);
		return code.toString();
	}

	public static class Simulator implements Runnable {

		private BlockingQueue<TrackingCode> codes;

		Simulator(BlockingQueue<TrackingCode> queue) {
			codes = queue;
		}

		public void run() {
			TrackingCode codeToProcess;
			try {
				while (true) {
					codeToProcess = codes.take();
					ShipmentRegistry registry = ShipmentRegistry.getInstance();
					registry.shipmentHandled(codeToProcess, new Location(
							"inter1", new LocationCode("INT1")));
					Thread.sleep(5000);
					registry.shipmentHandled(codeToProcess, new Location(
							"inter2", new LocationCode("INT2")));
					Thread.sleep(5000);
					registry.shipmentHandled(codeToProcess, new Location(
							"inter3", new LocationCode("INT3")));
					Thread.sleep(5000);
					registry.shipmentHandled(codeToProcess, new Location(
							"inter4", new LocationCode("INT4")));
					Thread.sleep(5000);
					registry.markShipmentDelivered(codeToProcess);
				}
			} catch (InterruptedException ex) {
			}
		}
	}
}
