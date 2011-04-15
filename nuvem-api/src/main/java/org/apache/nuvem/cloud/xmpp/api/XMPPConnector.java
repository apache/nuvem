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

package org.apache.nuvem.cloud.xmpp.api;

import java.util.Map;

/**
 * Represents a connection strategy. Each implementation of this interface will be a specific
 * strategy to establish connection to the XMPP Server.
 *
 * @param <T> - type of the connection this connector uses, will be platform specific.
 */
public interface XMPPConnector<T> {

	/**
	 * Returns a connection to the XMPPServer by using the connection properties
	 * injected into the implementation class.
	 * 
	 * 
	 * @throws XMPPConnectException
	 *             if any error during connection setup.
	 */
	T getConnection() throws XMPPConnectException;

	/**
	 * Connects to the XMPPServer by making use of the connection configuration
	 * values passed as parameters. It is expected that the various
	 * implementations will have a default connection configuration parameters
	 * injected as properties which will be used for establishing connections
	 * with the XMPP server. however, if you would like to override a specific
	 * set of connection configuration values, then only those properties can be
	 * passed in a map which will be considered during connection establishment.
	 * 
	 * @param connectionProperties
	 */
	T getConnection(Map<String, String> connectionProperties);

	/**
	 * Returns the description of the connector.
	 * @return the description.
	 */
	String getDescription();
}
