package org.apache.nuvem.cloud.xmpp.api;

/**
 * Thrown when unknown exception occurs at the layer beneath nuvem.
 *
 */
public class XMPPException extends RuntimeException {

    /**
     * serial id.
     */
    private static final long serialVersionUID = 5047965549033367275L;

    public XMPPException(Exception e) {
        super(e);
    }

    public XMPPException(String msg, Exception e) {
        super(msg, e);
    }

    public XMPPException(String msg) {
        super(msg);
    }
}
