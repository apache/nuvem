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

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.nuvem.cloud.xmpp.XMPPEndPoint;
import org.apache.nuvem.cloud.xmpp.presence.PresenceListener;
import org.apache.nuvem.cloud.xmpp.presence.PresenceManager;
import org.oasisopen.sca.annotation.Reference;

import com.google.appengine.api.xmpp.Presence;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;

/**
 * A servlet to recieve XMPP presence change requests from google cloud
 * platform.
 * <p>
 * This servlet will recieve the presence change requests posted in the HTTP
 * POST request, parse the message using the APIs provided by GAE convert the
 * request into nuvem specific presence object so that the
 * {@link PresenceListener#listen(org.apache.nuvem.cloud.xmpp.presence.Presence)}
 * will for all listeners registered through the
 * {@link PresenceManager#registerListener(PresenceListener)} API.
 * </p>
 */

public class GooglePresenceServlet extends HttpServlet {

	/**
	 * Logger.
	 */
	private static final Logger log = Logger
			.getLogger(GooglePresenceServlet.class.getName());

	/**
	 * serial id.
	 */
	private static final long serialVersionUID = 3109512262386132981L;

	@Reference
	private XMPPEndPoint endPoint;

	/**
	 * Adapts the HTTP Post request into a call to the
	 * {@link PresenceListener#listen(org.apache.nuvem.cloud.xmpp.presence.Presence)}
	 * .
	 * 
	 * @see org.apache.nuvem.cloud.xmpp.XMPPEndPoint
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		XMPPService xmpp = XMPPServiceFactory.getXMPPService();
		Presence presence = xmpp.parsePresence(req);
		PresenceManager presenceManager = endPoint.presenceManager();
		if (presenceManager == null) {
			log.warning("no presence manager available to notify presence updates!!");
			return;
		}
		for (PresenceListener listener : endPoint.presenceManager().listeners()) {
			listener.listen(PresenceAdapter
					.toNuvemPresence(presence));
		}
	}

}
