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

package org.apache.nuvem.cloud.xmpp.presence;

import org.apache.nuvem.cloud.xmpp.JID;

/**
 * Builds the presence object.
 * 
 */
public final class PresenceBuilder extends PresenceAttributes {

    /**
     * Initializes the JID for recipient.
     * 
     * @param jid
     *            the JID for the recipient.
     * @return the message builder.
     */
    public PresenceBuilder toRecipient(String jid) {
        this.to = new JID(jid);
        return this;
    }

    /**
     * The sender for a message.
     * 
     * @param jid
     *            the jid of the sender.
     * @return the message builder.
     */
    public PresenceBuilder from(String jid) {
        this.from = new JID(jid);
        return this;
    }

    /**
     * Initialises the content for sending to the recipient.
     * 
     * @param content
     *            the actual content.
     * @return the message builder.
     */
    public PresenceBuilder withStanza(String stanza) {
        this.stanza = stanza;
        return this;
    }

    public PresenceBuilder withStatus(String status) {
        this.status = status;
        return this;
    }

    public PresenceBuilder withShow(Presence.Show show) {
        this.show = show;
        return this;
    }

    public PresenceBuilder withType(Presence.Type type) {
        this.type = type;
        return this;
    }

    public PresenceBuilder from(JID jid) {
        this.from = jid;
        return this;
    }

    public PresenceBuilder to(JID jid) {
        this.to = jid;
        return this;
    }

    /**
     * Builds the Presence object by using all the previously passed attributes
     * through various methods like.
     * <ul>
     * <li/>{@link #containing(Object)}
     * <li/>{@link #toRecipient(String)}
     * <li/>{@link #withShow(String)}
     * <li/>{@link #withStanza(String)}
     * <li/>{@link #withStatus(String)}
     * <li/>{@link #withType(String)}
     * 
     * @return
     */
    public Presence build() {
        return new Presence(this);
    }
}
