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

package org.apache.nuvem.cloud.dataService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * The Class Element.
 */
public class Element {
	
	/** The features. */
	private Set<Feature> features;
	
	/** The type. */
	private String type;
	
	/** The id. */
	private String id;
	
	/** The key. */
	private String key;
	
	/** The persist. */
	private boolean persist;
	
	/**
	 * Checks if is persist.
	 *
	 * @return true, if is persist
	 */
	public boolean isPersist() {
		return persist;
	}

	/**
	 * Sets the persist.
	 */
	public void setPersist() {
		this.persist = true;
	}

	/**
	 * Instantiates a new element.
	 *
	 * @param type the type
	 */
	public Element(String type){
		this.type = type;
		this.persist = false;
		this.features = new HashSet<Feature>();
	}
	
	/**
	 * Instantiates a new element.
	 *
	 * @param type the type
	 * @param features the features
	 */
	public Element(String type, Set<Feature> features){
		this.type = type;
		this.features=features;
		
	}	
   
	/**
	 * Sets the key.
	 *
	 * @param key the new key
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the key.
	 *
	 * @return the key
	 */
	public String getKey() {
		if(key == null){
			setKey(getType().concat("(").concat(getId()).concat(")"));
		}
		return key;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Instantiates a new element.
	 *
	 * @param featuresList the features list
	 */
	public Element(List<Feature> featuresList){
		this.features = new HashSet<Feature>(featuresList);
		
	}
	
	/**
	 * Adds the feature.
	 *
	 * @param feature the feature
	 */
	public void addFeature(Feature feature){
		getFeatures().add(feature);
	}
	
	/**
	 * Adds the feature.
	 *
	 * @param name the name
	 * @param propValue the prop value
	 */
	public void addFeature(String name,Object propValue){
		getFeatures().add(new Feature(name,propValue));
	}
	
	/**
	 * Removes the feature.
	 *
	 * @param feature the feature
	 */
	public void removeFeature(Feature feature){
		getFeatures().remove(feature);
	}
	

	/**
	 * Gets the features.
	 *
	 * @return the features
	 */
	public Set<Feature> getFeatures() {
		return features;
	}

	/**
	 * Sets the features.
	 *
	 * @param features the new features
	 */
	public void setFeatures(Set<Feature> features) {
		this.features = features;
	}

}
