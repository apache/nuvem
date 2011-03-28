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

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.nuvem.cloud.xmpp.api.MessageListener;

import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;

/**
 * A servlet to recieve XMPP messages from google cloud platform.
 * <p>
 * This servlet will recieve the messages posted in the HTTP POST request, parse
 * the message using the APIs provided by GAE convert the message into nuvem
 * specific message so that the
 * {@link MessageListener#listen(org.apache.nuvem.cloud.xmpp.api.Message)} will
 * be called for the <code>JID</code> the <code>MessageListener</code> is
 * registered for.
 * </p>
 */
public class GoogleRecieverServlet extends HttpServlet {
	/**
	 * serial id.
	 */
	private static final long serialVersionUID = -6839442887435183490L;

	/**
	 * Adapts the HTTP Post request into a call to the {@link MessageListener#listen(org.apache.nuvem.cloud.xmpp.api.Message)}.
	 * @see org.apache.nuvem.cloud.xmpp.api.MessageListener
	 * @see org.apache.nuvem.cloud.xmpp.api.XMPPEndPoint
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		XMPPService xmpp = XMPPServiceFactory.getXMPPService();
		Message message = xmpp.parseMessage(req);
		org.apache.nuvem.cloud.xmpp.api.Message nuvemMessage = GoogleXMPPMessageAdapter
				.toNuvemMessage(message);
		JID from = message.getFromJid();

		// for identifying the listeners, we exclude the resource.
		String jidExcludingResource = StringUtils.substringBefore(from.getId(),
				"/");
		GoogleXMPPEndPoint.getListenerFor(
				new org.apache.nuvem.cloud.xmpp.api.JID(jidExcludingResource))
				.listen(nuvemMessage);
	}
}
