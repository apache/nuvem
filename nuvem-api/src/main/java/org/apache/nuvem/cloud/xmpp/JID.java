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

package org.apache.nuvem.cloud.xmpp;

import java.io.Serializable;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * Represents the XMPP ID.
 */
public final class JID implements Serializable{

	/**
	 * serial id.
	 */
	private static final long serialVersionUID = -5954050237038799970L;

	/**
	 * The validation pattern for an JID id.
	 */
	private static final Pattern VALID_PATTERN = Pattern
			.compile("^(?:([^@/<>'\"]+)@)?([^@/<>'\"]+)(?:/([^<>'\"]*))?$");

	/**
	 * To be used instead of null.
	 */
	public static final JID UNKNOWN = new JID("unknown@unknown.com");

	/**
	 * The string form of the JID.
	 */
	private String id;

	/**
	 * No argument constructor for supporting frameworks like tuscany.
	 */
	public JID() {
		
	}

	/**
	 * A setter method for usage if default constructor is prefered.
	 * @param id the jid string.
	 */
	public void setId(String id) {
		if (id == null || !VALID_PATTERN.matcher(id).matches())
			throw new IllegalArgumentException(
					"invalid JID! please check the format.. ");
		this.id = id;
	}
	
	/**
	 * Constructor. Validates the id passed as paramter befor initialising.
	 * 
	 * @param id
	 */
	public JID(String id) {
		if (id == null || !VALID_PATTERN.matcher(id).matches())
			throw new IllegalArgumentException(
					"invalid JID! please check the format.. ");
		this.id = id;
	}

	/**
	 * Returns the string representation of the JID.
	 * 
	 * @return
	 */
	public String asString() {
		return id;
	}

	/**
	 * Returns only the domain identifier of the JID.
	 * 
	 * @return
	 */
	public String domainIdentifier() {
		return StringUtils.substringBefore(this.id, "@");
	}

	/**
	 * Returns only the node identifier of this JID.
	 * 
	 * @return the node identifier.
	 */
	public String nodeIdentifier() {
		return StringUtils.substringBetween(id, "@", "/");
	}

	/**
	 * Returns only the resource identifier of the JID.
	 * 
	 * @return the resource identifier.
	 */
	public String resourceIdentifier() {
		return StringUtils.substringAfter(id, "/");
	}

	/**
	 * Just to make the equality check more descriptive and meaningful.
	 * 
	 * @param jid
	 *            the jid to which this jid is compared with.
	 * @return true if both the jid are same, false otherwise.
	 */
	public boolean isSameAs(JID jid) {
		return this.equals(jid);
	}

	/**
	 * returns true if the passed JID is same as this JID.
	 */
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof JID))
			return false;

		final JID jid = (JID) o;

		return id.equals(jid.id);
	}

	/**
	 */
	public int hashCode() {
		return id.hashCode();
	}

}
