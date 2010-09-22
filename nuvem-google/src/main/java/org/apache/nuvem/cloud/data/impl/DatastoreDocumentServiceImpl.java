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

package org.apache.nuvem.cloud.data.impl;

import java.util.UUID;

import org.apache.nuvem.cloud.data.DocumentService;
import org.apache.tuscany.sca.data.collection.Entry;
import org.apache.tuscany.sca.data.collection.NotFoundException;
import org.oasisopen.sca.annotation.Init;
import org.oasisopen.sca.annotation.Service;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@Service(DocumentService.class)
public class DatastoreDocumentServiceImpl implements DocumentService<Key, Entity> {
    private DatastoreService googleDataStoreService;

    @Init
    public void init() {
        googleDataStoreService = DatastoreServiceFactory.getDatastoreService();
    }

    public Entry<Key, Entity>[] getAll() {
        throw new UnsupportedOperationException();
    }

    public Entity get(Key key) throws NotFoundException {
        Entity entity = null;

        try {
            entity =  googleDataStoreService.get(key);
        } catch(EntityNotFoundException nf) {
            throw new NotFoundException(nf);
        }

        return entity;
    }


    public Key post(Key key, Entity entity) {

        if( key == null ) {
            key = KeyFactory.createKey("key", UUID.randomUUID().toString());
        }

        return googleDataStoreService.put(entity);
    }

    public void put(Key key, Entity entity) throws NotFoundException {
        if( get(key) == null) {
            throw new NotFoundException("Could not find entity with key '" + key.toString() +"'");
        }

        googleDataStoreService.put(entity);
    }

    public void delete(Key key) throws NotFoundException {
        googleDataStoreService.delete(key);

    }

    public void delete(Key... keys) throws NotFoundException {
        googleDataStoreService.delete(keys);

    }

    public Entry<Key, Entity>[] query(String query) {
        throw new UnsupportedOperationException();
    }

}
