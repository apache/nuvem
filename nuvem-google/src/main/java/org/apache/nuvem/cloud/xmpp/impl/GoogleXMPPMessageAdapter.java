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

import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.MessageBuilder;

/**
 * Transforms messages of nuvem format to google's format.
 */
public class GoogleXMPPMessageAdapter {

    public static Message toGoogleMessage(org.apache.nuvem.cloud.xmpp.message.Message nuvemMessage) {
        JID jid = new JID(nuvemMessage.recipient().asString());
        Message msg = new MessageBuilder().withRecipientJids(jid).withBody(nuvemMessage.payLoad().content()).build();
        return msg;
    }

    public static org.apache.nuvem.cloud.xmpp.message.Message toNuvemMessage(Message googleMessage) {
        String sender = googleMessage.getFromJid().getId();
        org.apache.nuvem.cloud.xmpp.message.Message nuvemMessage =
            new org.apache.nuvem.cloud.xmpp.message.MessageBuilder().from(sender).containing(googleMessage.getBody())
                .build();
        return nuvemMessage;
    }
}
