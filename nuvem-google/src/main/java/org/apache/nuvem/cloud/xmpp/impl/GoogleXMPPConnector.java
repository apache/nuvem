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

package org.apache.nuvem.cloud.xmpp.impl;

import java.util.Map;
import java.util.logging.Logger;

import org.apache.nuvem.cloud.xmpp.api.XMPPConnector;

import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;

/**
 * Connects to the google XMPP Server inside the GAE platform.
 * 
 */
public class GoogleXMPPConnector implements XMPPConnector<XMPPService> {

	/**
	 * Logger.
	 */
	private static final Logger log = Logger.getLogger(XMPPConnector.class
			.getName());

	/**
	 * {@inheritDoc}
	 */
	public XMPPService getConnection() {
		return XMPPServiceFactory.getXMPPService();
	}

	/**
	 * {@inheritDoc}
	 */
	public XMPPService getConnection(Map<String, String> connectionProperties) {
		log.warning("Google App engine doesnt support overriding any connection properties, the conneciton properties passed as parameters are ignored!");
		return getConnection();
	}

	/**
	 * {@inheritDoc}}
	 */
	public String getDescription() {
		return "Google Connector";
	}
}
