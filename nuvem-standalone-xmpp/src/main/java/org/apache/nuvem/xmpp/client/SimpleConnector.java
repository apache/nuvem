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

import org.apache.nuvem.cloud.xmpp.api.XMPPConnector;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.oasisopen.sca.annotation.Service;

/**
 * Helps establish a connection to the XMPP Server and returns the same
 * connection instance once connected. This specific implementation of
 * {@link XMPPConnector} will try to connect to the XMPPServer in an unsecured
 * way called. For more secure way of connecting to the XMPP server, please use
 * {@link SSLConnector}
 * 
 */
@Service(XMPPConnector.class)
public class SimpleConnector extends AbstractConnector {

	/**
	 * {@inheritDoc}
	 */
	public String getDescription() {
		return "Single Connection Default connector";
	}

	@Override
	protected void checkConfigurations() {
		if (host == null || port == 0 || clientJID == null
				|| clientPassword == null) {
			throw new IllegalStateException(
					"the configuration parameters are not initlaized properly!");
		}
	}

	/**
	 * Establishes a simple connection with the XMPP Server.
	 */
	protected void establishConnection() throws XMPPException {
		ConnectionConfiguration connectionConfiguration = new ConnectionConfiguration(
				host, port, serviceName);
		SASLAuthentication.supportSASLMechanism(authenticationMechanism, 0);
		this.connection = new XMPPConnection(connectionConfiguration);
		connection.connect();
	}

	/**
	 * Authenticates with the XMPPServer using client JID and password.
	 */
	protected void authenticateWithServer() throws XMPPException {
		this.connection.login(this.clientJID, this.clientPassword);

	}

}
