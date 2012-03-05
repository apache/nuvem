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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.nuvem.cloud.data.DocumentService;
import org.apache.nuvem.cloud.data.Entry;
import org.apache.nuvem.cloud.data.NotFoundException;
import org.oasisopen.sca.annotation.Scope;
import org.oasisopen.sca.annotation.Service;

@Scope("COMPOSITE")
@Service(DocumentService.class)
public class MapDocumentServiceImpl implements DocumentService<Object, Object> {
    private Map<Object, Object> documentMap = new HashMap<Object, Object>();

    @Override
    public Entry<Object, Object>[] getAll() {
        Entry<Object, Object>[] entries = new Entry[documentMap.size()];
        int i = 0;
        for (Map.Entry<Object, Object> e : documentMap.entrySet()) {
            entries[i++] = new Entry<Object, Object>(e.getKey(), e.getValue());
        }
        return entries;
    }

    @Override
    public Object get(Object key) throws NotFoundException {
        Object entry = documentMap.get(key);
        if (entry == null) {
            throw new NotFoundException(key.toString());
        } else {
            return entry;
        }
    }

    @Override
    public Object post(Object key, Object item) {
        if (key == null || key.toString().isEmpty()) {
            key = "document-" + UUID.randomUUID().toString();
        }
        documentMap.put(key, item);
        return key;
    }

    @Override
    public void put(Object key, Object item) throws NotFoundException {
        if (!documentMap.containsKey(key)) {
            throw new NotFoundException(key.toString());
        }
        documentMap.put(key, item);
    }

    @Override
    public void delete(Object key) throws NotFoundException {
        if (key == null || key.toString().isEmpty()) {
            documentMap.clear();
        } else {
            Object entry = documentMap.remove(key);
            if (entry == null)
                throw new NotFoundException(key.toString());
        }

    }

    @Override
    public void delete(Object... keys) throws NotFoundException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Entry<Object, Object>[] query(String queryString) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
