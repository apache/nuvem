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
import org.apache.nuvem.cloud.data.Entry;
import org.apache.nuvem.cloud.data.NotFoundException;
import org.oasisopen.sca.annotation.Init;
import org.oasisopen.sca.annotation.Property;
import org.oasisopen.sca.annotation.Service;

import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

@Service(DocumentService.class)
public class MemcacheDocumentServiceImpl implements DocumentService<Object, Object> {
    private MemcacheService googleMemecacheService;
    private Expiration expiration;

    @Property(required = false)
    protected int defaultExpiration = 3600;

    @Init
    public void init() {
        expiration = Expiration.byDeltaSeconds(defaultExpiration); // 1hr
        googleMemecacheService = MemcacheServiceFactory.getMemcacheService();
    }

    public Entry<Object, Object>[] getAll() {
        throw new UnsupportedOperationException();
    }

    public Object get(Object key) throws NotFoundException {
        Object entity = null;

        entity = googleMemecacheService.get(key);

        if (entity == null) {
            throw new NotFoundException("Could not find object with key '" + key.toString() + "'");
        }

        return entity;
    }

    public Object post(Object key, Object entity) {
        if (key == null) {
            key = UUID.randomUUID().toString();
        }

        googleMemecacheService.put(key, entity, expiration);

        return entity;
    }

    public void put(Object key, Object entity) throws NotFoundException {
        if (get(key) == null) {
            throw new NotFoundException("Could not find entity with key '" + key.toString() + "'");
        }

        googleMemecacheService.put(key, entity, expiration);
    }

    public void delete(Object key) throws NotFoundException {
        googleMemecacheService.delete(key);
    }

    public void delete(Object... keys) throws NotFoundException {
        googleMemecacheService.delete(keys);
    }

    public Entry<Object, Object>[] query(String query) {
        throw new UnsupportedOperationException();
    }
}
