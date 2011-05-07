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

import java.util.HashMap;
import java.util.Map;

import org.apache.nuvem.cloud.xmpp.Error;
import org.apache.nuvem.cloud.xmpp.ErrorCode;
import org.apache.nuvem.cloud.xmpp.Status;

import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.SendResponse;

/**
 * Acts as an adapter to transform the status recieved by the the send API to
 * the form compatible with nuvem.
 */
public class GoogleStatusAdapter {

    /**
     * converstion map.
     */
    private static Map<SendResponse.Status, ErrorCode> converstionMap;

    static {
        converstionMap = new HashMap<SendResponse.Status, ErrorCode>();
        converstionMap.put(SendResponse.Status.INVALID_ID, ErrorCode.INVALID_ID);
        converstionMap.put(SendResponse.Status.OTHER_ERROR, ErrorCode.UNKNOWN_ERROR);
    }

    public static Status toStatus(SendResponse responseStatus) {
        Status status = new Status();
        ErrorCode code = null;
        if (responseStatus != null && responseStatus.getStatusMap() != null && !responseStatus.getStatusMap().isEmpty()) {
            Map<JID, SendResponse.Status> statuses = responseStatus.getStatusMap();
            for (JID jid : statuses.keySet()) {
                if (converstionMap.containsKey(statuses.get(jid)))
                    status.addError(new Error(code, "For :" + jid.getId()));
            }
        }
        return status;

    }
}
