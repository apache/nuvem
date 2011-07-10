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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.nuvem.cloud.xmpp.XMPPEndPoint;
import org.apache.nuvem.cloud.xmpp.message.MessageListener;
import org.oasisopen.sca.ComponentContext;
import org.oasisopen.sca.annotation.Reference;

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
 * {@link MessageListener#listen(org.apache.nuvem.cloud.xmpp.message.Message)}
 * will be called for the <code>JID</code> the <code>MessageListener</code> is
 * registered for.
 * </p>
 */
public class GoogleRecieverServlet extends HttpServlet {

	/**
	 * Logger.
	 */
	private static final Logger log = Logger.getLogger(XMPPEndPoint.class
			.getName());

	/**
	 * serial id.
	 */
	private static final long serialVersionUID = -6839442887435183490L;

	@Reference(required = false)
	private XMPPEndPoint endPoint;

	/**
	 * If the servlet container doesnt support SCA then the endpoint will be
	 * identified explicitly.
	 */
	@Override
	public void init(ServletConfig config) {
		if (endPoint == null) {
			log.info("endpoint not wired, trying to fetch one from the component context using the name: XMPPComponent/XMPPEndPoint");
			ServletContext servletContext = config.getServletContext();
			ComponentContext context = (ComponentContext) servletContext
					.getAttribute("org.oasisopen.sca.ComponentContext");
			
			endPoint = context.getService(XMPPEndPoint.class, "endPoint");
			log.info("endpoint: " + endPoint);
		}
	}

	/**
	 * Adapts the HTTP Post request into a call to the
	 * {@link MessageListener#listen(org.apache.nuvem.cloud.xmpp.message.Message)}
	 * .
	 * 
	 * @see org.apache.nuvem.cloud.xmpp.message.MessageListener
	 * @see org.apache.nuvem.cloud.xmpp.XMPPEndPoint
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		XMPPService xmpp = XMPPServiceFactory.getXMPPService();
		Message message = xmpp.parseMessage(req);
		org.apache.nuvem.cloud.xmpp.message.Message nuvemMessage = GoogleXMPPMessageAdapter
				.toNuvemMessage(message);
		endPoint.broadCastToListeners(nuvemMessage);		
	}
}
