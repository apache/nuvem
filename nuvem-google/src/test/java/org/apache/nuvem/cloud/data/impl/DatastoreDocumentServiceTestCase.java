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

import org.apache.nuvem.cloud.data.NotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class DatastoreDocumentServiceTestCase extends AbsGAELocalTestSetup {

    private DatastoreDocumentServiceImpl documentService = new DatastoreDocumentServiceImpl();

    private Entity customerEntity;
    private Key key;

    @Before
    public void setUp() {
        super.setUp();

        key = KeyFactory.createKey(Customer.class.getName(), customer.getId());
        customerEntity = new Entity(key);
        customerEntity.setProperty("id", customer.getId());
        customerEntity.setProperty("name", customer.getName());
        customerEntity.setProperty("creditCard", customer.getCreditCard());
        documentService.init();
    }

    @Test
    public void get() {
        documentService.post(key, customerEntity);
        try {
            Entity entity = documentService.get(key);
            System.out.println("Returned Entity:" + entity);
            Assert.assertNotNull(entity);

            Assert.assertEquals(customer.getId(), entity.getProperty("id"));
            Assert.assertEquals(customer.getName(), entity.getProperty("name"));
            Assert.assertEquals(customer.getCreditCard(), entity.getProperty("creditCard"));

        } catch (NotFoundException e) {
            e.printStackTrace();
            Assert.fail("get Method failed");
        }
    }

    @Test
    public void delete() {
        documentService.post(key, customerEntity);
        try {
            documentService.delete(key);
        } catch (NotFoundException e) {
            e.printStackTrace();
            Assert.fail("delete Method failed");
        }

        try {
            // This call should throw NotFoundException
            documentService.get(key);
            Assert.fail();
        } catch (NotFoundException e) {
        }
    }
}
