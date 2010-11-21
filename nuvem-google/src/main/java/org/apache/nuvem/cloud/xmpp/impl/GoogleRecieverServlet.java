package org.apache.nuvem.cloud.xmpp.impl;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;

/**
 * A servlet to recieve XMPP messages from google cloud platform.
 */
public class GoogleRecieverServlet extends HttpServlet {
    /**
     * serial id.
     */
    private static final long serialVersionUID = -6839442887435183490L;

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        XMPPService xmpp = XMPPServiceFactory.getXMPPService();
        Message message = xmpp.parseMessage(req);
        org.apache.nuvem.cloud.xmpp.api.Message nuvemMessage = GoogleXMPPMessageAdapter.toNuvemMessage(message);
        JID from = message.getFromJid();

        // for identifying the listeners, we exclude the resource.
        String jidExcludingResource = StringUtils.substringBefore(from.getId(), "/");
        GoogleXMPPEndPoint.getListenerFor(new org.apache.nuvem.cloud.xmpp.api.JID(jidExcludingResource))
            .listen(nuvemMessage);
    }
}
