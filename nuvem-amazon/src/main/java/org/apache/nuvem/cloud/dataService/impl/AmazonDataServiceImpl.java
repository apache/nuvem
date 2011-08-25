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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.nuvem.cloud.dataService.DataService;
import org.apache.nuvem.cloud.dataService.DataServiceException;
import org.apache.nuvem.cloud.dataService.DataServiceUtil;
import org.apache.nuvem.cloud.dataService.Element;
import org.apache.nuvem.cloud.dataService.Feature;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.BatchPutAttributesRequest;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.DeleteAttributesRequest;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.ListDomainsResult;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.ReplaceableItem;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;
import com.amazonaws.services.simpledb.model.Attribute;

/**
 * The Class AmazonDataServiceImpl is implementation of Nuvem DataService
 * interface based on AmazonsimpleDB service. Element and Feature objects are
 * mapped into GAE Entry and Properties.
 */
public class AmazonDataServiceImpl implements DataService {

	private AmazonSimpleDB simpleDBClient;
	private boolean isDefaultDomainExist;
	private String domainName;

	public AmazonDataServiceImpl() {
		try {
			isDefaultDomainExist = false;
			domainName = AMAZON_DEFAULT_DOMAIN_NAME;
			simpleDBClient = new AmazonSimpleDBClient(
					new PropertiesCredentials(AmazonDataServiceImpl.class
							.getResourceAsStream(AMAZON_CONFIG_FILE_NAME)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public AmazonDataServiceImpl(final String accessKey, final String secretKey) {
		AWSCredentials credentials = new AWSCredentials() {
			@Override
			public String getAWSSecretKey() {
				return secretKey;
			}

			@Override
			public String getAWSAccessKeyId() {
				return accessKey;
			}
		};

		simpleDBClient = new AmazonSimpleDBClient(credentials);
		domainName = AMAZON_DEFAULT_DOMAIN_NAME;
	}

	public AmazonDataServiceImpl(final Map config) {
		AWSCredentials credentials = new AWSCredentials() {
			@Override
			public String getAWSSecretKey() {
				return (String) config.get(AMAZON_SECRET_KEY);
			}

			@Override
			public String getAWSAccessKeyId() {
				return (String) config.get(AMAZON_ACCESS_KEY);
			}
		};
		simpleDBClient = new AmazonSimpleDBClient(credentials);
		domainName = AMAZON_DEFAULT_DOMAIN_NAME;

	}

	public AmazonDataServiceImpl(AmazonSimpleDBClient client) {
		simpleDBClient = client;
		domainName = AMAZON_DEFAULT_DOMAIN_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.nuvem.cloud.dataService.DataService#createElement(org.apache
	 * .nuvem.cloud.dataService.Element)
	 */
	@Override
	public Element createElement(Element element) throws DataServiceException {
		try {
			createDomainIfNotExsist();
			BatchPutAttributesRequest batchPutAttributesRequest = new BatchPutAttributesRequest();
			batchPutAttributesRequest.setItems(getElementData(element));
			batchPutAttributesRequest.setDomainName(domainName);
			simpleDBClient.batchPutAttributes(batchPutAttributesRequest);
			return element;
		} catch (Exception e) {
			throw new DataServiceException(e);
		}
	}

	private List<ReplaceableItem> getElementData(Element element) {
		List<ReplaceableItem> items = new ArrayList<ReplaceableItem>();
		List<ReplaceableAttribute> attributes = new ArrayList<ReplaceableAttribute>();
		for (Feature feature : element.getFeatures()) {
			ReplaceableAttribute attribute = new ReplaceableAttribute(
					feature.getName(), feature.getValue().toString(), true);
			attributes.add(attribute);
		}
		ReplaceableItem elementItem = new ReplaceableItem(element.getKey())
				.withAttributes(attributes);
		items.add(elementItem);
		return items;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.nuvem.cloud.dataService.DataService#updateElement(org.apache
	 * .nuvem.cloud.dataService.Element)
	 */
	@Override
	public Element updateElement(Element element) throws DataServiceException {
		try {
			createDomainIfNotExsist();
			List<ReplaceableAttribute> attributes = new ArrayList<ReplaceableAttribute>();
			for (Feature feature : element.getFeatures()) {
				ReplaceableAttribute attribute = new ReplaceableAttribute(
						feature.getName(), feature.getValue().toString(), true);
				attributes.add(attribute);
			}
			PutAttributesRequest putAttributesRequest = new PutAttributesRequest(
					domainName, element.getKey(), attributes);
			simpleDBClient.putAttributes((putAttributesRequest));
			return element;
		} catch (Exception e) {
			throw new DataServiceException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.nuvem.cloud.dataService.DataService#getElement(java.lang.String
	 * , java.lang.String)
	 */
	@Override
	public Element getElement(String kind, String id)
			throws DataServiceException {
		try {
			String itemName = DataServiceUtil.createKey(kind, id);
			 String selectExpression = "select * from "+ domainName +
			 " where itemName() in('"+ itemName+ "')";			
			SelectRequest selectRequest = new SelectRequest();
			selectRequest.setSelectExpression(selectExpression);
			SelectResult selectResult = simpleDBClient.select(selectRequest);
			List items = selectResult.getItems();
			// Consider only first Item.
			if (selectResult.getItems().size() == 0) {
				return null;
			}
			Item item = selectResult.getItems().get(0);
			if (item != null) {
				String eleKind = DataServiceUtil.getKind(item.getName());
				String eleId = DataServiceUtil.getId(item.getName());
				Element element = new Element(eleKind);
				element.setId(eleId);
				element.setKey(item.getName());
				element.setPersist();
				Set<Feature> features = new HashSet<Feature>();
				if (item.getAttributes() != null
						&& item.getAttributes().size() > 0) {
					for (Attribute att : item.getAttributes()) {
						features.add(new Feature(att.getName(), att.getValue()));
					}
				}
				element.setFeatures(features);
				return element;
			}
			return null;
		} catch (Exception e) {
			throw new DataServiceException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.nuvem.cloud.dataService.DataService#deleteElement(org.apache
	 * .nuvem.cloud.dataService.Element)
	 */
	@Override
	public boolean deleteElement(Element element) throws DataServiceException {
		try {
			List<Attribute> attributes = new ArrayList<Attribute>();
			for (Feature feature : element.getFeatures()) {
				Attribute attribute = new Attribute(feature.getName(), feature
						.getValue().toString());
				attributes.add(attribute);
			}
			DeleteAttributesRequest deleteAttributesRequest = new DeleteAttributesRequest(
					domainName, element.getKey(), attributes);
			simpleDBClient.deleteAttributes(deleteAttributesRequest);
			return true;

		} catch (Exception e) {
			throw new DataServiceException(e);
		}

	}

	private void createDomainIfNotExsist() {
		// Check default domain exists or not
		if (isDefaultDomainExist) {
			return;
		}
		ListDomainsResult listDomainsResult = simpleDBClient.listDomains();
		for (String domainName : listDomainsResult.getDomainNames()) {
			if (AMAZON_DEFAULT_DOMAIN_NAME.equals(domainName)) {
				isDefaultDomainExist = true;
				break;
			}
		}
		if (!isDefaultDomainExist) {
			CreateDomainRequest createDomainRequest = new CreateDomainRequest();
			createDomainRequest.setDomainName(AMAZON_DEFAULT_DOMAIN_NAME);
			simpleDBClient.createDomain(createDomainRequest);
		}
	}

	public static final String AMAZON_CONFIG_FILE_NAME = "AwsCredentials.properties";
	public static final String AMAZON_ACCESS_KEY = "accessKey";
	public static final String AMAZON_SECRET_KEY = "secretKey";
	public static final String AMAZON_DEFAULT_DOMAIN_NAME = "default";

}
