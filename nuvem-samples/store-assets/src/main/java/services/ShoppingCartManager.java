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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.apache.nuvem.cloud.data.DocumentService;
import org.apache.nuvem.cloud.data.Entry;
import org.apache.nuvem.cloud.data.NotFoundException;
import org.apache.nuvem.cloud.user.User;
import org.apache.nuvem.cloud.user.UserService;
import org.apache.nuvem.cloud.xmpp.JID;
import org.oasisopen.sca.annotation.Reference;
import org.oasisopen.sca.annotation.Scope;

@Scope("COMPOSITE")
public class ShoppingCartManager implements ShoppingCart {
	private static final Logger log = Logger
			.getLogger(ShoppingCartManager.class.getName());
	private static String ANONYMOUS = "anonymous";

	@Reference
	private DocumentService documentService;

	@Reference
	private UserService userService;

	@Reference
	private ShipmentService shipmentService;

	public Entry<String, Item>[] getAll() {
		Map<String, Item> cart = getUserShoppingCart();

		Entry<String, Item>[] entries = new Entry[cart.size()];
		int i = 0;
		for (Map.Entry<String, Item> e : cart.entrySet()) {
			entries[i++] = new Entry<String, Item>(e.getKey(), e.getValue());
		}
		return entries;
	}

	public Item get(String key) throws NotFoundException {
		Map<String, Item> cart = getUserShoppingCart();

		Item item = cart.get(key);
		if (item == null) {
			throw new NotFoundException(key);
		} else {
			return item;
		}
	}

	public String post(String key, Item item) {
		Map<String, Item> cart = getUserShoppingCart();

		if (key == null || key.isEmpty()) {
			key = this.generateItemKey();
		}

		// add to the cart map
		cart.put(key, item);
		// add back to the store
		documentService.post(getCartKey(), cart);
		return key;
	}

	public void put(String key, Item item) throws NotFoundException {
		Map<String, Item> cart = getUserShoppingCart();

		if (!cart.containsKey(key)) {
			throw new NotFoundException(key);
		}
		// add to the cart map
		cart.put(key, item);
		// add back to the store
		documentService.put(getCartKey(), cart);
	}

	public void delete(String key) throws NotFoundException {
		if (key == null || key.isEmpty()) {
			documentService.delete(getCartKey());
		} else {
			Map<String, Item> cart = getUserShoppingCart();

			Item item = cart.remove(key);
			if (item == null) {
				throw new NotFoundException(key);
			}
			documentService.put(getCartKey(), cart);
		}
	}

	public Entry<String, Item>[] query(String queryString) {
		throw new UnsupportedOperationException("Operation not supported !");
	}

	public String getTotal() {
		double total = 0;
		String currencySymbol = "";

		Map<String, Item> cart = getUserShoppingCart();
		if (!cart.isEmpty()) {
			Item item = cart.values().iterator().next();
			currencySymbol = item.getCurrencySymbol();
		}
		for (Item item : cart.values()) {
			total += item.getPrice();
		}

		return currencySymbol + String.valueOf(total);
	}

	/**
	 * Utility functions
	 */

	private Map<String, Item> getUserShoppingCart() {
		String userCartKey = getCartKey();
		HashMap<String, Item> cart;

		try {
			cart = (HashMap<String, Item>) documentService.get(userCartKey);
		} catch (NotFoundException e) {
			cart = new HashMap<String, Item>();
			documentService.post(userCartKey, cart);
		}

		return cart;
	}

	private String getUserId() {
		String userId = null;
		if (userService != null) {
			try {
				User user = userService.getCurrentUser();
				userId = user.getUserId();
			} catch (Exception e) {
				// ignore
				e.printStackTrace();
			}
		}
		if (userId == null || userId.length() == 0) {
			userId = ANONYMOUS;
		}
		return userId;
	}

	private String getCartKey() {
		String cartKey = "cart-" + this.getUserId();
		return cartKey;
	}

	private String generateItemKey() {
		String itemKey = getCartKey() + "-item-" + UUID.randomUUID().toString();
		return itemKey;
	}

	@Override
	public String shipItems(String jid) {
		if (getUserShoppingCart() == null || getUserShoppingCart().isEmpty())
			return StringUtils.EMPTY;
		if (jid == null) {
			log
					.warning("using current user's email address for shipment updates");
			jid = userService.getCurrentUser().getEmail();
		}
		List<Item> items = new ArrayList<Item>();
		items.addAll(getUserShoppingCart().values());
		try {
			return shipmentService.shipItemsAndRegisterForUpdates(
					Address.DUMMY_ADDRESS, new JID(jid), items);
		} catch (ShipmentException e) {
			log.severe("error occured during shipment");
			return "shipment error";
		}

	}
}
