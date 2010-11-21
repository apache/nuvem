package org.apache.nuvem.cloud.xmpp.common;

import java.util.logging.Logger;

import org.apache.nuvem.cloud.xmpp.api.Message;
import org.apache.nuvem.cloud.xmpp.api.MessageListener;
import org.apache.nuvem.cloud.xmpp.api.XMPPEndPoint;

/**
 * Default message listener which will log the message information.
 *
 */
public class LoggingMessageListener implements MessageListener {

    private static final Logger log = Logger.getLogger(XMPPEndPoint.class.getName());

    @Override
    public void listen(Message message) {
        log.warning("no listeners found to listen to a specific message:" + message);
    }

}
