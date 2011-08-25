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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.nuvem.cloud.dataService.DataService;
import org.apache.nuvem.cloud.dataService.DataServiceException;
import org.apache.nuvem.cloud.dataService.DataServiceUtil;
import org.apache.nuvem.cloud.dataService.Element;
import org.apache.nuvem.cloud.dataService.Feature;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.BatchPutAttributesRequest;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.DeleteDomainRequest;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.ReplaceableItem;
import com.amazonaws.services.simpledb.model.SelectRequest;

/**
 * 
 * It's Required to have valid Amazon accessKey/secretKey pair in the
 * test.properties file to run this test.
 * 
 */
public class AmazonDataServiceImplTestCase {

	private DataService dataService;

	@Before
	public void setUp() throws Exception {
		dataService = new AmazonDataServiceImpl(getSDBClient());
		clearData();
	}

	@After
	public void tearDown() throws Exception {
		dataService = null;

	}

	@Test
	public void testCreateElement() throws DataServiceException {
		Element e1 = new Element("student");
		// For SDB it required set Id explicitly.
		e1.setId("1");
		e1.addFeature("name", "saman");
		Feature f1 = new Feature("age", 12);
		e1.addFeature(f1);
		dataService.createElement(e1);

		String selectExpression = "select * from "
				+ AmazonDataServiceImpl.AMAZON_DEFAULT_DOMAIN_NAME
				+ " where itemName() in('student(1)')";
		AmazonSimpleDBClient client = getSDBClient();
		List<Item> items = client.select(
				new SelectRequest().withSelectExpression(selectExpression))
				.getItems();
		assertNotNull(items);
		assertNotNull(items.get(0));
		assertEquals("student(1)", items.get(0).getName());
		assertEquals(2, items.get(0).getAttributes().size());
	}

	@Test
	public void testUpdateElement() throws DataServiceException,
			InterruptedException {

		// create sample data
		AmazonSimpleDBClient client = getSDBClient();
		client.createDomain(new CreateDomainRequest()
				.withDomainName(AmazonDataServiceImpl.AMAZON_DEFAULT_DOMAIN_NAME));
		List<ReplaceableItem> items = new ArrayList<ReplaceableItem>();
		List<ReplaceableAttribute> attributes = new ArrayList<ReplaceableAttribute>();
		String key = DataServiceUtil.createKey("student", "1");
		attributes.add(new ReplaceableAttribute("name", "saman", true));
		attributes.add(new ReplaceableAttribute("age", "12", true));
		PutAttributesRequest putAttributesRequest = new PutAttributesRequest(
				AmazonDataServiceImpl.AMAZON_DEFAULT_DOMAIN_NAME, key,
				attributes);
		client.putAttributes(putAttributesRequest);

		Thread.sleep(2000);

		Element element = dataService.getElement("student", "1");
		assertNotNull(element);
		assertEquals(key, element.getKey());
		assertNotNull(element.getFeatures());
		assertNotNull(element.getFeatures().size());
		assertEquals(2, element.getFeatures().size());

		Set<Feature> features = element.getFeatures();
		element.getFeatures().removeAll(features);

		element.addFeature("name", "kamal");
		element.addFeature("age", "24");
		element.addFeature("country", "LK");
		dataService.updateElement(element);

		Thread.sleep(5000);

		Element element2 = dataService.getElement("student", "1");
		for (Feature f : element2.getFeatures()) {
			System.out.println(f.getValue());
		}
		assertNotNull(element2);
		assertEquals("student(1)", element2.getKey());
		assertNotNull(element2.getFeatures());
		assertNotNull(element2.getFeatures().size());

	}

	@Test
	public void testGetElement() throws DataServiceException {

		// create sample data
		AmazonSimpleDBClient client = getSDBClient();
		client.createDomain(new CreateDomainRequest()
				.withDomainName(AmazonDataServiceImpl.AMAZON_DEFAULT_DOMAIN_NAME));
		BatchPutAttributesRequest batchPutAttributesRequest = new BatchPutAttributesRequest();
		batchPutAttributesRequest
				.setDomainName(AmazonDataServiceImpl.AMAZON_DEFAULT_DOMAIN_NAME);
		List<ReplaceableItem> items = new ArrayList<ReplaceableItem>();
		List<ReplaceableAttribute> attributes = new ArrayList<ReplaceableAttribute>();
		String key = DataServiceUtil.createKey("student", "1");
		attributes.add(new ReplaceableAttribute("name", "saman", true));
		attributes.add(new ReplaceableAttribute("age", "12", true));
		ReplaceableItem item = new ReplaceableItem(key, attributes);
		items.add(item);
		batchPutAttributesRequest.setItems(items);
		client.batchPutAttributes(batchPutAttributesRequest);

		Element element = dataService.getElement("student", "1");
		assertNotNull(element);
		assertEquals("student(1)", element.getKey());
		assertNotNull(element.getFeatures());
		assertNotNull(element.getFeatures().size());

	}

	@Test
	public void testDeleteElement() throws DataServiceException,
			InterruptedException {
		// create sample data
		AmazonSimpleDBClient client = getSDBClient();
		client.createDomain(new CreateDomainRequest()
				.withDomainName(AmazonDataServiceImpl.AMAZON_DEFAULT_DOMAIN_NAME));
		List<ReplaceableItem> items = new ArrayList<ReplaceableItem>();
		List<ReplaceableAttribute> attributes = new ArrayList<ReplaceableAttribute>();
		String key = DataServiceUtil.createKey("student", "1");
		attributes.add(new ReplaceableAttribute("name", "saman", true));
		attributes.add(new ReplaceableAttribute("age", "12", true));
		PutAttributesRequest putAttributesRequest = new PutAttributesRequest(
				AmazonDataServiceImpl.AMAZON_DEFAULT_DOMAIN_NAME, key,
				attributes);
		client.putAttributes(putAttributesRequest);

		Element element = dataService.getElement("student", "1");
		assertNotNull(element);
		assertEquals(key, element.getKey());
		assertNotNull(element.getFeatures());
		assertNotNull(element.getFeatures().size());
		assertEquals(2, element.getFeatures().size());
		Thread.sleep(5000);
		boolean result = dataService.deleteElement(element);
		assertTrue(result);
		Thread.sleep(5000);
		Element element2 = dataService.getElement("student", "1");
		assertNull(element2);

	}

	private void clearData() {
		DeleteDomainRequest deleteDomainRequest = new DeleteDomainRequest();
		deleteDomainRequest
				.setDomainName(AmazonDataServiceImpl.AMAZON_DEFAULT_DOMAIN_NAME);
		getSDBClient().deleteDomain(deleteDomainRequest);
	}

	private AmazonSimpleDBClient getSDBClient() {
		Properties props = new Properties();
		URL url = ClassLoader.getSystemResource("test.properties");
		try {
			props.load(url.openStream());
			final String accessKey = props.getProperty("accessKey");
			final String secretKey = props.getProperty("secretKey");
			return (AmazonSimpleDBClient) AmazonDataServiceClientFactory
					.getAmazonWebServiceClient(accessKey, secretKey);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

}
