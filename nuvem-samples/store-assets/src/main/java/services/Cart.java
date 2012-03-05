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

import org.apache.nuvem.cloud.data.Entry;
import org.apache.nuvem.cloud.data.NotFoundException;
import org.oasisopen.sca.annotation.Remotable;

@Remotable
/**
 * [rfeng] Tuscany cannot handle subclassing Collection<String, Item> to resolve the type variable. As a workaround, we copy all the methods into this interface directly 
 */
public interface Cart {
    /**
     * Get the whole collection.
     * 
     * @return the whole collection.
     */
    Entry<String, Item>[] getAll();

    /**
     * Returns a collection resulting from a query.
     * 
     * @return the collection.
     */
    Entry<String, Item>[] query(String queryString);

    /**
     * Creates a new item.
     * 
     * @param key
     * @param item
     * @return
     */
    String post(String key, Item item);

    /**
     * Retrieves an item.
     * 
     * @param key
     * @return
     */
    Item get(String key) throws NotFoundException;

    /**
     * Updates an item.
     * 
     * @param key
     * @param item
     * @return
     */
    void put(String key, Item item) throws NotFoundException;

    /**
     * Delete an item.
     * 
     * @param key
     */
    void delete(String key) throws NotFoundException;

	/**
	 * Ships the items selected in the shopping cart. this will be called in a
	 * secure manner after the payment process.
	 */
	String shipItems(String jid);
}
