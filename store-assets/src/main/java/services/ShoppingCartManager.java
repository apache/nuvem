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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import org.apache.nuvem.cloud.user.User;
import org.apache.nuvem.cloud.user.UserService;
import org.apache.tuscany.sca.data.collection.Entry;
import org.apache.tuscany.sca.data.collection.NotFoundException;
import org.oasisopen.sca.annotation.Reference;
import org.oasisopen.sca.annotation.Scope;

@Scope("COMPOSITE")
public class ShoppingCartManager implements ShoppingCart {
    private static final Logger log = Logger.getLogger(ShoppingCartManager.class.getName());
    private static String ANONYMOUS = "anonymous";
    
    private Map<String, ShoppingCart> carts = new HashMap<String, ShoppingCart>();
    
    @Reference
    private UserService userService;
        
    public Entry<String, Item>[] getAll() {
        return getUserShoppingCart().getAll();
    }
 
    public Item get(String key) throws NotFoundException {
        return getUserShoppingCart().get(key);
    }

    public String post(String key, Item item) {
        if (key == null || key.length() == 0) {
            key = this.generateItemKey();
        }
        return getUserShoppingCart().post(key, item);
    }

    public void put(String key, Item item) throws NotFoundException {
        getUserShoppingCart().put(key,item);
    }

    public Entry<String, Item>[] query(String queryString) {
        return getUserShoppingCart().query(queryString);
    }
    
    public void delete(String key) throws NotFoundException {
        this.getUserShoppingCart().delete(key);
    }

    public String getTotal() {
        return this.getUserShoppingCart().getTotal();
    }
    
    /**
     * Utility functions
     */


    private ShoppingCart getUserShoppingCart() {
        String key = getCartKey();
        ShoppingCart userCart = carts.get(key);
        if(userCart == null) {
            userCart = new ShoppingCartImpl();
            carts.put(key, userCart);
        }
        return userCart;
    }
    
    private String getUserId() {
        String userId = null;
        if(userService != null) {
            try {
                User user = userService.getCurrentUser();
                userId = user.getUserId();
            } catch(Exception e) {
                //ignore
                e.printStackTrace();
            }
        }
        if(userId == null || userId.length() == 0) {
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
}
