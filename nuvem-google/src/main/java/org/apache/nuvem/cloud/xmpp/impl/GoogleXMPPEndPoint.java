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

import java.util.logging.Logger;

import org.apache.nuvem.cloud.xmpp.api.Error;
import org.apache.nuvem.cloud.xmpp.api.ErrorCode;
import org.apache.nuvem.cloud.xmpp.api.Status;
import org.apache.nuvem.cloud.xmpp.api.XMPPEndPoint;
import org.apache.nuvem.cloud.xmpp.common.AbstractXMPPEndPoint;
import org.oasisopen.sca.annotation.Init;
import org.oasisopen.sca.annotation.Scope;
import org.oasisopen.sca.annotation.Service;

import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.SendResponse;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;

@Service(XMPPEndPoint.class)
@Scope("COMPOSITE")
public class GoogleXMPPEndPoint extends AbstractXMPPEndPoint implements XMPPEndPoint {

    private static final Logger log = Logger.getLogger(XMPPEndPoint.class.getName());

    /**
     * Google's XMPP service.
     */
    private XMPPService xmpp;

    @Init
    public void init() {
        xmpp = XMPPServiceFactory.getXMPPService();
    }

    /**
     * {@inheritDoc}
     */
    public Status sendMessage(org.apache.nuvem.cloud.xmpp.api.Message message) {
        if (message == null || message.recipient() == null) {
            throw new IllegalArgumentException("invalid input");
        }

        Status deliveryStatus = new Status();
        JID jid = new JID(message.recipient().asString());
        Message msg = GoogleXMPPMessageAdapter.toGoogleMessage(message);

        SendResponse status = null;
        if (xmpp.getPresence(jid).isAvailable()) {
            log.info("user online...");
            try {
                status = xmpp.sendMessage(msg);
            } catch (RuntimeException e) {
                deliveryStatus.addError(new Error(ErrorCode.UNKNOWN_ERROR,
                                                  "unknown runtime error while sending message over XMPP"));
                return deliveryStatus;
            }
            log.info(status.toString());
        } else {
            log.info("user offline...");
            deliveryStatus.addError(new org.apache.nuvem.cloud.xmpp.api.Error(ErrorCode.USER_OFFLINE));
            return deliveryStatus;
        }
        return GoogleStatusAdapter.toStatus(status);
    }

    @Override
    public Status invite(org.apache.nuvem.cloud.xmpp.api.JID jid) {
        if (jid == null)
            throw new IllegalArgumentException("jid cannot be null");
        Status deliveryStatus = new Status();
        JID googleJID = new JID(jid.asString());
        try {
            xmpp.sendInvitation(googleJID);
        } catch (RuntimeException e) {
            deliveryStatus.addError(new Error(ErrorCode.UNKNOWN_ERROR,
                                              "unknown runtime error while sending invitiation over XMPP"));
            return deliveryStatus;
        }
        return deliveryStatus;
    }

    /**
     * {@inheritDoc} validation of the input will be done within
     * {@link org.apache.nuvem.cloud.xmpp.api.JID}
     */
    @Override
    public Status invite(String jid) {
        return invite(new org.apache.nuvem.cloud.xmpp.api.JID(jid));
    }

    /**
     * {@inheritDoc}
     */
    public boolean isPresent(org.apache.nuvem.cloud.xmpp.api.JID jid) {
        return xmpp.getPresence(new JID(jid.asString())).isAvailable();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isPresent(String id) {
        return isPresent(new org.apache.nuvem.cloud.xmpp.api.JID(id));
    }

}