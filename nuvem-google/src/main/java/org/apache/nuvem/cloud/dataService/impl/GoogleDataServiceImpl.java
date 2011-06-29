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

import java.util.Iterator;
import java.util.Map;

import org.apache.nuvem.cloud.dataService.DataService;
import org.apache.nuvem.cloud.dataService.DataServiceException;
import org.apache.nuvem.cloud.dataService.Element;
import org.apache.nuvem.cloud.dataService.Feature;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * The Class GoogleDataServiceImpl.
 */
public class GoogleDataServiceImpl implements DataService {

	/** The datastore service. */
	private DatastoreService datastoreService;	

	/**
	 * Instantiates a new google data service impl.
	 *
	 * @param datastoreService the datastore service
	 */
	public GoogleDataServiceImpl(DatastoreService datastoreService) {
		this.datastoreService = datastoreService;
	}

	/**
	 * Gets the datastore service.
	 *
	 * @return the datastore service
	 */
	public DatastoreService getDatastoreService() {
		return datastoreService;
	}

	/**
	 * Sets the datastore service.
	 *
	 * @param datastoreService the new datastore service
	 */
	public void setDatastoreService(DatastoreService datastoreService) {
		this.datastoreService = datastoreService;
	}

	/* (non-Javadoc)
	 * @see org.apache.nuvem.cloud.dataService.DataService#createElement(org.apache.nuvem.cloud.dataService.Element)
	 */
	@Override
	public Element createElement(Element element) throws DataServiceException {

		Entity entity = new Entity(element.getType());
		Iterator<Feature> feIterator = element.getFeatures().iterator();
		while (feIterator.hasNext()) {
			Feature feature = feIterator.next();
			entity.setProperty(feature.getName(), feature.getValue());
		}
		Key key = datastoreService.put(entity);
		element.setId(String.valueOf(key.getId()));
		element.setPersist();
		System.out.println(key);
		return element;
	}

	/* (non-Javadoc)
	 * @see org.apache.nuvem.cloud.dataService.DataService#updateElement(org.apache.nuvem.cloud.dataService.Element)
	 */
	@Override
	public Element updateElement(Element element) throws DataServiceException {
		try {
			Key key = KeyFactory.createKey(element.getType(), Long.parseLong(element.getId()));
			Entity entity = new Entity(key);
			Iterator<Feature> feIterator = element.getFeatures().iterator();
			while (feIterator.hasNext()) {
				Feature feature = feIterator.next();
				entity.setProperty(feature.getName(), feature.getValue());
			}				
			Key keyRet = datastoreService.put(entity);
			element.setId(String.valueOf(keyRet.getId()));
			element.setPersist();
			System.out.println(keyRet);
			return element;

		} catch (Exception e) {
			throw new DataServiceException(e);
		}

	}

	/* (non-Javadoc)
	 * @see org.apache.nuvem.cloud.dataService.DataService#getElement(java.lang.String, java.lang.String)
	 */
	@Override
	public Element getElement(String kind,String id) throws DataServiceException {		
		try {
			Key key=KeyFactory.createKey(kind, Long.parseLong(id));
			Entity entity = datastoreService.get(key);
			Element element= new Element(entity.getKind());
			element.setId(String.valueOf(entity.getKey().getId()));
			Map<String,Object> properties = entity.getProperties();
			Iterator<String> propKeyItr = properties.keySet().iterator();
			while(propKeyItr.hasNext()){
				String propName = propKeyItr.next();
				Object propValue = properties.get(propName);
				element.addFeature(propName, propValue);				
			}
			element.setPersist();
			return element;
		} catch (EntityNotFoundException e) {
			throw new DataServiceException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.nuvem.cloud.dataService.DataService#deleteElement(org.apache.nuvem.cloud.dataService.Element)
	 */
	@Override
	public boolean deleteElement(Element element) throws DataServiceException {
		try {
			Key key=KeyFactory.createKey(element.getType(), Long.parseLong(element.getId()));
			datastoreService.delete(key);
			return true;

		} catch (Exception e) {
			throw new DataServiceException(e);
		}
	}

	

}
