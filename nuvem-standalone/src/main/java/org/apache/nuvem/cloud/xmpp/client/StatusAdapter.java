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

import java.util.HashMap;
import java.util.Map;

import org.apache.nuvem.cloud.xmpp.Error;
import org.apache.nuvem.cloud.xmpp.ErrorCode;
import org.apache.nuvem.cloud.xmpp.Status;
import org.jivesoftware.smack.packet.XMPPError;

/**
 * Acts as an adapter to transform the status recieved by the the send API to
 * the form compatible with nuvem.
 */
public class StatusAdapter {

	/**
	 * converstion map.
	 */
	private static Map<Integer, ErrorCode> converstionMap;

	static {
		converstionMap = new HashMap<Integer, ErrorCode>();
		converstionMap.put(Integer.valueOf(400), ErrorCode.BAD_FORMAT);
		converstionMap.put(Integer.valueOf(401), ErrorCode.NOT_AUTHORIZED);
		converstionMap.put(Integer.valueOf(408), ErrorCode.CONNECTION_TIMEOUT);
	}

	public static Status toStatus(XMPPError xmppError) {
		Status status = new Status();
		if (xmppError != null
				&& converstionMap.containsKey(xmppError.getCode())) {
			status.addError(new Error(converstionMap.get(xmppError.getCode()),
					xmppError.getMessage()));
			return status;
		}
		status.addError(new Error(ErrorCode.UNKNOWN_ERROR, xmppError
				.getMessage()));
		return status;

	}
}
