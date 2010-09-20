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

import org.apache.tuscany.sca.data.collection.NotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class MemcacheDocumentServiceImplTestCase extends AbsGAELocalTestSetup{
	
	private MemcacheDocumentServiceImpl documentService = new MemcacheDocumentServiceImpl();

	@Before
	public void setUp() {
		super.setUp();
		documentService.init();
	}

	@Test
	public void get(){

		documentService.post(customer.getId(), customer);

		try {
			Customer retrievedCustomer = (Customer) documentService.get(customer.getId());
			Assert.assertNotNull(retrievedCustomer);
			Assert.assertEquals(customer.getId(), retrievedCustomer.getId());
			Assert.assertEquals(customer.getName(), retrievedCustomer.getName());
			Assert.assertEquals(customer.getCreditCard(), retrievedCustomer.getCreditCard());
		} catch (NotFoundException e) {
			e.printStackTrace();
			Assert.fail("get Method failed");
		}
	}

	@Test
	public void delete() 
	{
		documentService.post(customer.getId(), customer);
		
		try {
			documentService.delete(customer.getId());
		} catch (NotFoundException e) {
			e.printStackTrace();
			Assert.fail("delete Method failed");
		}
		
		try {
			// This call should throw NotFoundException
			documentService.get(customer.getId());
			Assert.fail();
		} catch (NotFoundException e) {
		}
	}
}
