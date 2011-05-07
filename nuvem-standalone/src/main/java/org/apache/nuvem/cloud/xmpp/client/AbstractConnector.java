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

package org.apache.nuvem.cloud.xmpp.client;

import java.util.Map;
import java.util.logging.Logger;

import org.apache.nuvem.cloud.xmpp.XMPPConnectException;
import org.apache.nuvem.cloud.xmpp.XMPPConnector;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.oasisopen.sca.annotation.Property;

/**
 * Common Template to establish conneciton to the XMPPServer, which is extended
 * by a more specific mechanisms.
 * 
 * 
 */
public abstract class AbstractConnector implements
		XMPPConnector<XMPPConnection> {
	/**
	 * Logger.
	 */
	protected static final Logger log = Logger.getLogger(XMPPConnector.class
			.getName());

	@Property(required = false)
	protected String host = "localhost";
	@Property(required = false)
	protected int port = 5222;
	@Property(required = false)
	protected String serviceName;
	@Property
	protected String clientJID = "nuvem@localhost";
	@Property
	protected String clientPassword = "password";
	@Property(required = false)
	protected String authenticationMechanism = "PLAIN";

	/**
	 * The XMPP Connection.
	 */
	protected XMPPConnection connection;

	/**
	 * Connects to the XMPP Server and returns the XMPPConnection.
	 */
	public XMPPConnection getConnection() {
		if (connection != null)
			return connection;
		log.info("Initializing a new connection...");
		checkConfigurations();
		try {
			log.info("Connecting to XMPP Server....");
			establishConnection();
			log.info("Authenticating with the XMPP Server....");
			authenticateWithServer();
			log.info("EndPoint connected to XMPP Server successfuly!");
		} catch (XMPPException e) {
			log.severe("Error while connecting to the XMPP Server..."
					+ e.getMessage());
			throw new XMPPConnectException(
					"error while connecting to the xmpp server", e);
		}
		return connection;
	}

	/**
	 * {@inheritDoc}
	 */
	public XMPPConnection getConnection(Map<String, String> connectionProperties) {
		throw new UnsupportedOperationException("still not supported");
	}

	protected abstract void checkConfigurations();

	protected abstract void establishConnection() throws XMPPException;

	protected abstract void authenticateWithServer() throws XMPPException;

}
