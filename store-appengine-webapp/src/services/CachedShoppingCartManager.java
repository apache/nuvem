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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;


import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheManager;

import org.apache.nuvem.cloud.user.User;
import org.apache.nuvem.cloud.user.UserService;
import org.apache.nuvem.cloud.xmpp.api.JID;
import org.apache.tuscany.sca.data.collection.Entry;
import org.apache.tuscany.sca.data.collection.NotFoundException;
import org.oasisopen.sca.annotation.Init;
import org.oasisopen.sca.annotation.Reference;
import org.oasisopen.sca.annotation.Scope;
import org.oasisopen.sca.annotation.Service;

@Scope("COMPOSITE")
@Service(ShoppingCart.class)
public class CachedShoppingCartManager implements ShoppingCart {
    private static final Logger log = Logger.getLogger(CachedShoppingCartManager.class.getName());
    private static String ANONYMOUS = "anonymous";

    private Cache cache;

	@Reference
	private ShipmentService shipmentService;
	
    @Init
    public void init() {
        try {
            cache = CacheManager.getInstance().getCacheFactory().createCache(Collections.emptyMap());
        } catch (Throwable t) {
            log.log(Level.SEVERE, "Error initialize cache + " + t.getMessage());
        }
    }

    @Reference
    private UserService userService;

    public Entry<String, Item>[] getAll() {
        Map<String, Item> cart = getUserItems();

        Entry<String, Item>[] entries = new Entry[cart.size()];
        int i = 0;
        for (Map.Entry<String, Item> e : cart.entrySet()) {
            entries[i++] = new Entry<String, Item>(e.getKey(), e.getValue());
        }
        return entries;
    }

    public Item get(String key) throws NotFoundException {
        Map<String, Item> cart = getUserItems();

        Item item = cart.get(key);
        if (item == null) {
            throw new NotFoundException(key);
        } else {
            return item;
        }
    }

    public String post(String key, Item item) {
        Map<String, Item> cart = getUserItems();

        if (key == null || key.isEmpty()) {
            key = this.generateItemKey();
        }

        cart.put(key, item);
        cache.put(getCartKey(), cart);
        return key;
    }

    public void put(String key, Item item) throws NotFoundException {
        Map<String, Item> cart = getUserItems();

        if (!cart.containsKey(key)) {
            throw new NotFoundException(key);
        }
        cart.put(key, item);
        cache.put(getCartKey(), cart);
    }

    public void delete(String key) throws NotFoundException {
        if (key == null || key.isEmpty()) {
            cache.remove(getCartKey());
        } else {
            Map<String, Item> cart = getUserItems();

            Item item = cart.remove(key);
            if (item == null) {
                throw new NotFoundException(key);
            }
            cache.put(getCartKey(), cart);
        }
    }

    public Entry<String, Item>[] query(String queryString) {
        throw new UnsupportedOperationException("Operation not supported !");
    }

    public String getTotal() {
        double total = 0;
        String currencySymbol = "";
        if (cache.containsKey(getCartKey())) {
            Map<String, Item> cart = getUserItems();
            if (!cart.isEmpty()) {
                Item item = cart.values().iterator().next();
                currencySymbol = item.getCurrencySymbol();
            }
            for (Item item : cart.values()) {
                total += item.getPrice();
            }

        }
        return currencySymbol + String.valueOf(total);
    }

    /**
     * Utility functions
     */
    /*
     * private ShoppingCart getUserShoppingCart() { String key = getCartKey();
     * ShoppingCart userCart = (ShoppingCart) cache.get(key); if(userCart ==
     * null) { userCart = new ShoppingCartImpl(); cache.put(key, userCart); }
     * return userCart; }
     */

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

    private Map<String, Item> getUserItems() {
        String userCartKey = getCartKey();
        HashMap<String, Item> cartMap;

        if (!cache.containsKey(userCartKey)) {
            cartMap = new HashMap<String, Item>();
            cache.put(userCartKey, cartMap);
        } else {
            cartMap = (HashMap<String, Item>)cache.get(userCartKey);
        }

        return cartMap;
    }

	@Override
	public String shipItems(String jid) {
		List<Item> items = new ArrayList<Item>();
		items.addAll(getUserItems().values());
		try {
			return shipmentService.shipItemsAndRegisterForUpdates(
					Address.DUMMY_ADDRESS, new JID(jid), items);
		} catch (ShipmentException e) {
			return "shipment error";
		}
	}
}
