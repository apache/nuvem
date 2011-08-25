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

import org.oasisopen.sca.annotation.Remotable;

/**
 * The Interface DataService defines services provide by Nuvem key-value
 * data-store component also act as abreaction layer for cloud platform specific
 * data services. Element is the basic persistent unit and may contains number
 * of Features that associate with the Element. DataService interface facilitate
 * to create, update , read and delete Elements.
 */
@Remotable
public interface DataService {
	
	/**
	 * Creates the element.
	 *
	 * @param element the element
	 * @return the element
	 * @throws DataServiceException the data service exception
	 */
	public Element createElement(Element element) throws DataServiceException;
	
	/**
	 * Update element.
	 *
	 * @param element the element
	 * @return the element
	 * @throws DataServiceException the data service exception
	 */
	public Element updateElement(Element element) throws DataServiceException;
	
	/**
	 * Gets the element.
	 *
	 * @param kind the kind
	 * @param id the id
	 * @return the element
	 * @throws DataServiceException the data service exception
	 */
	public Element getElement(String kind,String id) throws DataServiceException;
	
	/**
	 * Delete element.
	 *
	 * @param element the element
	 * @return true, if successful
	 * @throws DataServiceException the data service exception
	 */
	public boolean deleteElement(Element element) throws DataServiceException;
	

}
