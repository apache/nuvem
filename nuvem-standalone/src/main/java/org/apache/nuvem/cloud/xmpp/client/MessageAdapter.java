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

import org.apache.nuvem.cloud.xmpp.message.MessageBuilder;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

/**
 * Adapter to transform nuvem message into smack message.
 * 
 * 
 */
public class MessageAdapter {

	/**
	 * Converts nuvem specific message object to the one smack API accepts.
	 * 
	 */
	public static Message toSmackMessage(
			org.apache.nuvem.cloud.xmpp.message.Message nuvemMessage,
			String sender) {
		Message smackMessage = new Message();
		smackMessage.setFrom(sender);
		smackMessage.setBody(nuvemMessage.payLoad().content());
		smackMessage.setTo(nuvemMessage.recipient().asString());
		return smackMessage;
	}


	/**
	 * Converts smack message to Nuvem Message.
	 * 
	 * @param message
	 *            the smack message.
	 * @return nuvem message.
	 */
	public static org.apache.nuvem.cloud.xmpp.message.Message toNuvemMessage(
			Message message) {
		return new MessageBuilder().from(message.getFrom()).toRecipient(
				message.getTo()).containing(message.getBody()).build();
	}

}
