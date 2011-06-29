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

package org.apache.nuvem.cloud.dataService.impl;

import static org.junit.Assert.*;

import org.apache.nuvem.cloud.dataService.DataService;
import org.apache.nuvem.cloud.dataService.DataServiceException;
import org.apache.nuvem.cloud.dataService.Element;
import org.apache.nuvem.cloud.dataService.Feature;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;


/**
 * The Class GoogleDataServiceImplTest.
 */
public class GoogleDataServiceImplTest {
	
	/** The data service. */
	private DataService dataService ;
	
	/** The ds. */
	private DatastoreService ds;

	/** The helper. */
	private final LocalServiceTestHelper helper =
	        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());


	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
		helper.setUp();
		ds = DatastoreServiceFactory.getDatastoreService();
		dataService = new GoogleDataServiceImpl(ds);
	}

	/**
	 * Tear down.
	 *
	 * @throws Exception the exception
	 */
	@After
	public void tearDown() throws Exception {
		helper.tearDown();
	}

	/**
	 * Test create element.
	 *
	 * @throws DataServiceException the data service exception
	 */
	@Test
	public void testCreateElement() throws DataServiceException {
		
		Element e1= new Element("student");
		e1.addFeature("name", "saman");
		Feature f1= new Feature("age",12);
		e1.addFeature(f1);
		dataService.createElement(e1);
		assertEquals(1, ds.prepare(new Query("student")).countEntities());
		
		
		
	}

	/**
	 * Test update element.
	 *
	 * @throws DataServiceException the data service exception
	 */
	@Test
	public void testUpdateElement() throws DataServiceException {
		
		Entity entity = new Entity("student");
		entity.setProperty("name", "saman");
		entity.setProperty("age",12);
		Key key = ds.put(entity);
		assertEquals(1, ds.prepare(new Query("student")).countEntities());
		
		Element e1= new Element("student");
		e1.addFeature("name", "saman");
		Feature f1= new Feature("age",12);		
		e1.addFeature(f1);
		e1.addFeature("countrt", "SL");
		e1.setType(key.getKind());
		e1.setId(String.valueOf(key.getId()));			
		dataService.updateElement(e1);
		assertEquals(1, ds.prepare(new Query("student")).countEntities());
		
	}

	/**
	 * Test get element.
	 *
	 * @throws DataServiceException the data service exception
	 */
	@Test
	public void testGetElement() throws DataServiceException {

		Entity entity = new Entity("student");
		entity.setProperty("name", "saman");
		entity.setProperty("age",12);
		Key key = ds.put(entity);
		assertEquals(1, ds.prepare(new Query("student")).countEntities());		
		Element e2 = dataService.getElement(key.getKind(),String.valueOf(key.getId()));
		assertNotNull(e2);
	}

	/**
	 * Test delete element.
	 *
	 * @throws DataServiceException the data service exception
	 */
	@Test
	public void testDeleteElement() throws DataServiceException {

		Entity entity = new Entity("student");
		entity.setProperty("name", "saman");
		entity.setProperty("age",12);
		Key key = ds.put(entity);
		assertEquals(1, ds.prepare(new Query("student")).countEntities());
		
		Element e1= new Element("student");
		e1.addFeature("name", "saman");
		Feature f1= new Feature("age","12");		
		e1.addFeature(f1);
		e1.addFeature("countrt", "SL");
		e1.setType(key.getKind());
		e1.setId(String.valueOf(key.getId()));			
		dataService.updateElement(e1);
		dataService.deleteElement(e1);
		assertEquals(0, ds.prepare(new Query("student")).countEntities());
		
	}

}
