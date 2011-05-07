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

import java.util.logging.Logger;

import org.apache.commons.lang.Validate;
import org.apache.nuvem.cloud.xmpp.AbstractPresenceManager;
import org.apache.nuvem.cloud.xmpp.XMPPConnector;
import org.apache.nuvem.cloud.xmpp.presence.PresenceListener;
import org.apache.nuvem.cloud.xmpp.presence.PresenceManager;
import org.oasisopen.sca.annotation.Init;
import org.oasisopen.sca.annotation.Reference;
import org.oasisopen.sca.annotation.Service;

import com.google.appengine.api.xmpp.XMPPService;

@Service(PresenceManager.class)
public class GooglePresenceManager extends AbstractPresenceManager {

	/**
	 * Logger.
	 */
	private static final Logger log = Logger.getLogger(PresenceManager.class
			.getName());

	@Reference(required = false)
	protected XMPPConnector<XMPPService> connector;

	@Init
	public void init() {
		if (connector == null) {
			log.info("Google Presence manager is getting initialized with a default connector...");
			connector = new GoogleXMPPConnector();
		}
	}

	@Override
	public void registerListener(PresenceListener listener) {
		Validate.notNull(listener);
		listeners.add(listener);
	}

}
