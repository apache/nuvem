package org.apache.nuvem.cloud.xmpp.api;

import org.apache.nuvem.cloud.xmpp.common.LoggingMessageListener;

/**
 * Listens to XMPP messages of a specific type.
 */
public interface MessageListener {

    /**
     * To be used as default one.
     */
    public static final MessageListener LOGGING_LISTENER = new LoggingMessageListener();

    /**
     * Listens for messages.
     * 
     * @param message
     */
    void listen(Message message);

}
