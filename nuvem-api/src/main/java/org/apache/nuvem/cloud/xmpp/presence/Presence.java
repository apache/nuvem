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

/**
 * Represents the presence XMPP stanza, it could be one of the below type of
 * presence stanza
 * <ul>
 * <li>presence broad cast by a JID which conveys its current presence state
 * <li>initial presence state of a JID when it logs into the XMPP server.
 * </ul>
 * 
 */
public final class Presence extends PresenceAttributes {

    /**
     * Constructor which takes a builder as parameter to maintain immutability.
     * 
     */
    public Presence(PresenceBuilder builder) {
        this.from = builder.from();
        this.status = builder.status();
        this.type = builder.type();
        this.show = builder.show();
        this.to = builder.to();
        this.stanza = builder.stanza();
    }

    /**
     * Represents the substanza of the presence stanza.
     * 
     */
    public enum Show {
        AVAILABLE, CHAT, AWAY, DND, XA, NONE;
    }

    /**
     * Represents the type of the presence stanza.
     * 
     */
    public enum Type {
        AVAILABLE, PROBE, UNAVAILABLE, SUBSCRIBED, SUBSCRIBE, UNSUBSCRIBE, UNSUBSCRIBED, ERROR;
    }
}
