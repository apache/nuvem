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

import org.jivesoftware.smack.packet.Message;

/**
 * Adapter to transform nuvem message into smack message.
 * 
 * 
 */
public class SmackMessageAdapter {

	/**
	 * Converts nuvem specific message object to the one smack API accepts.
	 * 
	 */
	public static Message toSmackMessage(
			org.apache.nuvem.cloud.xmpp.api.Message nuvemMessage, String sender) {
		Message smackMessage = new Message();
		smackMessage.setFrom(sender);
		smackMessage.setBody(nuvemMessage.payLoad().content());
		smackMessage.setTo(nuvemMessage.recipient().asString());
		return smackMessage;
	}

}
