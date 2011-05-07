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

import java.util.List;
import java.util.logging.Logger;

import org.apache.nuvem.cloud.xmpp.JID;
import org.oasisopen.sca.annotation.Reference;
import org.oasisopen.sca.annotation.Scope;

@Scope("COMPOSITE")
public class ShipmentServiceImpl implements ShipmentService {
	
	private static final Logger log = Logger
	.getLogger(ShipmentServiceImpl.class.getName());
	
	private static final Location STARTING_LOCATION = new Location("starting1",
			new LocationCode("START1"));
	
	@Reference
	private ShipmentObserver observer;

	public String shipItemsAndRegisterForUpdates(Address to, JID jid,
			List<Item> items) throws ShipmentException {
		Shipment shipment = new Shipment(items, to, jid);
		shipment.startFrom(STARTING_LOCATION);
		shipment.addObserver(observer);
		log.info("preparing shipment...");
		return ShipmentRegistry.getInstance().addNewShipment(shipment);
	}

}
