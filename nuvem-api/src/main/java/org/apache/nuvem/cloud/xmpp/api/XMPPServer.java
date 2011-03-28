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

/**
 * Entry point for interacting with the XMPP Server implementation. Each cloud
 * platform will have its own implementation of the XMPPServer if the platform
 * doesn't support a built in XMPP Server.
 * 
 * 
 */
public interface XMPPServer {

	/**
	 * Starts the XMPPServer for use if it was not already started.
	 * 
	 * @throws XMPPException
	 *             if any problem starting the server or if the XMPPServer is
	 *             already running.
	 */
	public void start() throws XMPPException;

	/**
	 * Stops the XMPP Server if it is started before.
	 * 
	 * @throws XMPPException
	 *             if any internal problem during stopping the server.
	 */
	public void stop() throws XMPPException;

}
