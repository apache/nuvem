package org.apache.nuvem.cloud.xmpp;

import org.apache.nuvem.cloud.xmpp.api.XMPPEndPoint;

public final class XMPPEndPointFactory {

    private static final XMPPEndPointFactory instance = new XMPPEndPointFactory();

    public static XMPPEndPointFactory getInstance() {
        if (instance == null)
            throw new IllegalStateException("Factory not initialized properly...");
        return instance;
    }

    public XMPPEndPoint getEndPoint() {
        // TODO:find the implementation here.
        // return new GoogleXMPPEndpoint();
        return null;
    }
}
