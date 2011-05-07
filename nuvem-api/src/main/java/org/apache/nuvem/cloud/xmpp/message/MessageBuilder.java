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

package org.apache.nuvem.cloud.xmpp.message;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.nuvem.cloud.xmpp.JID;

/**
 * Used to build the message.
 */
public class MessageBuilder {

    /**
     * The sender of a message.
     */
    private JID from;

    /**
     * The recipient to whom a message is being built.
     */
    private JID recipient;

    /**
     * The content to deliver to the recipient.
     */
    private String content;

    /**
     * The locale.
     */
    private Locale locale;

    /**
     * Headers if any. <b>Note:This is not used internally at the moment. </b>
     */
    private Map<String, String> headers = new HashMap<String, String>();

    /**
     * Initializes the JID for recipient.
     * 
     * @param jid the JID for the recipient.
     * @return the message builder.
     */
    public MessageBuilder toRecipient(String jid) {
        this.recipient = new JID(jid);
        return this;
    }

    /**
     * The sender for a message.
     * 
     * @param jid the jid of the sender.
     * @return the message builder.
     */
    public MessageBuilder from(String jid) {
        this.from = new JID(jid);
        return this;
    }

    public MessageBuilder inLanguage(Locale locale) {
        this.locale = locale;
        return this;
    }

    /**
     * Initialises the content for sending to the recipient.
     * 
     * @param content the actual content.
     * @return the message builder.
     */
    public MessageBuilder containing(String content) {
        this.content = content;
        return this;
    }

    /**
     * Initializes a header.
     * 
     * @param key the key of the header.
     * @param value the value for the header.
     * @return the message builder.
     */
    public MessageBuilder withHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    /**
     * Initialises the headeres.
     * 
     * @param headers a map of headers.
     * @return the message builder.
     */
    public MessageBuilder withHeaders(Map<String, String> headers) {
        if (headers != null && headers.size() > 0)
            this.headers.putAll(headers);
        return this;
    }

    /**
     * Builds the message by using all the previously passed attributes through
     * various methods like.
     * <ul>
     * <li/>{@link #containing(Object)}
     * <li/>{@link #toRecipient(String)}
     * <li/>{@link #withHeader(String, String)}
     * <li/>{@link #withHeaders(Map)}
     * 
     * @return
     */
    public Message build() {
        PayLoad payLoad = new PayLoad(content, locale);
        Message message = new Message(payLoad, recipient, from);
        message.addHeaders(headers);
        return message;
    }
}
