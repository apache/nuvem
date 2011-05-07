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

import org.apache.commons.lang.Validate;
import org.apache.nuvem.cloud.xmpp.XMPPConnector;
import org.apache.nuvem.cloud.xmpp.presence.PresenceListener;
import org.apache.nuvem.cloud.xmpp.presence.PresenceManager;
import org.apache.nuvem.cloud.xmpp.AbstractPresenceManager;
import org.jivesoftware.smack.XMPPConnection;
import org.oasisopen.sca.annotation.Reference;
import org.oasisopen.sca.annotation.Service;

/**
 * Utility Service to provide all presence related operations.
 * 
 */
@Service(PresenceManager.class)
public class DefaultPresenceManager extends AbstractPresenceManager {

	/**
	 * Connecter to fetch XMPP Connection.
	 */
	@Reference
	protected XMPPConnector<XMPPConnection> connector;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerListener(PresenceListener listener) {
		Validate.notNull(listener);
		connector.getConnection().getRoster().addRosterListener(
				new RosterListenerAdapter(listener));
		//Keeps a reference to the listener for later use.
		listeners.add(listener);
	}

}
