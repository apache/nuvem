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

package org.apache.nuvem.xmpp.client;

import java.util.Map;
import java.util.logging.Logger;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

import org.apache.nuvem.cloud.xmpp.api.XMPPConnectException;
import org.apache.nuvem.cloud.xmpp.api.XMPPConnector;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.oasisopen.sca.annotation.Property;
import org.oasisopen.sca.annotation.Reference;
import org.oasisopen.sca.annotation.Scope;
import org.oasisopen.sca.annotation.Service;

/**
 * Helps establish a connection to the XMPP Server and returns the same
 * connection instance once connected. This specific implementation of
 * {@link XMPPConnector} will just maintain on connection instance to the
 * XMPPServer and return the same each time the {@link #connect()} method is
 * called.
 * 
 */
@Service(XMPPConnector.class)
@Scope("COMPOSITE")
public class SingleConnectionConnector implements XMPPConnector<XMPPConnection> {

	/**
	 * Logger.
	 */
	private static final Logger log = Logger.getLogger(XMPPConnector.class
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
	protected boolean sslMode = false;
	@Property(required = false)
	protected String trustStorePath;
	@Property(required = false)
	protected String trustStorePassword;

	/** Authentication mechanism. */
	private String authenticationMechanism = "PLAIN";

	@Reference(required = false)
	protected SocketFactory socketFactory = SSLSocketFactory.getDefault();

	// TODO: connection and connected classes like the chat are assumed to be
	// thread safe.. if required, a connection pool needs to be implemented.
	/**
	 * The XMPP Connection.
	 */
	private XMPPConnection connection;

	/**
	 * {@inheritDoc}
	 */
	public XMPPConnection connect() {
		if (connection != null)
			return connection;
		log
				.info(String
						.format(
								"initializing XMPP Connection for server %s and with client jid %s",
								host, clientJID));
		if (host == null || port == 0 || clientJID == null
				|| clientPassword == null) {
			throw new IllegalStateException(
					"the configuration parameters are not initlaized properly!");
		}
		connection = new XMPPConnection(buildConnectionConfiguration());
		SASLAuthentication.supportSASLMechanism(authenticationMechanism, 0);
		log.info("Connecting to XMPP Server....");
		try {
			connection.connect();
			connection.login(clientJID, clientPassword);
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
	public XMPPConnection connect(Map<String, String> connectionProperties) {
		throw new UnsupportedOperationException("still not supported");
	}

	private ConnectionConfiguration buildConnectionConfiguration() {
		ConnectionConfiguration connectionConfiguration = new ConnectionConfiguration(
				host, port, serviceName);
		if (sslMode && !(trustStorePassword == null || trustStorePath == null)) {
			log.info("XMPP is configured to use SSL mode.");
			connectionConfiguration
					.setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);
			connectionConfiguration.setSocketFactory(socketFactory);
			connectionConfiguration.setTruststorePath(trustStorePath);
			connectionConfiguration.setTruststorePassword(trustStorePassword);
		}
		return connectionConfiguration;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getDescription() {
		return "Single Connection Default connector";
	}

}
