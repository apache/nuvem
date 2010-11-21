package org.apache.nuvem.cloud.xmpp.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.nuvem.cloud.xmpp.api.Error;
import org.apache.nuvem.cloud.xmpp.api.ErrorCode;
import org.apache.nuvem.cloud.xmpp.api.Status;

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
