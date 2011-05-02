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

package org.apache.nuvem.cloud.xmpp.api.presence;

import org.apache.nuvem.cloud.xmpp.api.JID;

/**
 * Serves as a super class to hold all the attributes related to the presence
 * stanza to avoid duplicate attributes and methods in both the {@link Presence}
 * and {@link PresenceBuilder} classes.
 * 
 */
public class PresenceAttributes {

	/**
	 * The sender of a presence.
	 */
	protected JID from;

	/**
	 * The recipient of the presence.
	 */
	protected JID to;

	/**
	 * The stanza of the presence message.
	 */
	protected String stanza;

	/**
	 * The additional status string sent by an entity.
	 */
	protected String status;

	/**
	 * Additional availability status along with the {@link #type}} value
	 */
	protected Presence.Show show;

	/**
	 * Represents the type of the presence stanza which could be
	 * <ul>
	 * <li>unavailable -- Signals that the entity is no longer available for communication.
	 * <li>subscribe -- The sender wishes to subscribe to the recipient's presence.
	 * <li>subscribed -- The sender has allowed the recipient to receive their presence.
	 * <li>unsubscribe -- The sender is unsubscribing from another entity's presence.
     * <li>unsubscribed -- The subscription request has been denied or a previously-granted subscription has been cancelled.
	 * <li>probe -- A request for an entity's current presence; SHOULD be generated only by a server on behalf of a user.
	 * <li>error -- An error has occurred regarding processing or delivery of a previously-sent presence stanza.
	 * </ul>
	 */
	protected Presence.Type type;

	/**
	 * The priority of the presence message.
	 */
	protected int priority;

	public final Presence.Type type() {
		return type;
	}

	public final Presence.Show show() {
		return show;
	}

	public final JID from() {
		return from;
	}

	public final JID to() {
		return to;
	}

	public final String stanza() {
		return stanza;
	}

	public final String status() {
		return status;
	}

}
