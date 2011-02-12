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

package org.apache.tuscany.sca.host.xmpp;

import java.io.File;
import java.io.IOException;

import org.apache.vysper.mina.TCPEndpoint;
import org.apache.vysper.stanzasession.StanzaSessionFactory;
import org.apache.vysper.storage.StorageProviderRegistry;
import org.apache.vysper.storage.inmemory.MemoryStorageProviderRegistry;
import org.apache.vysper.xmpp.authorization.AccountManagement;
import org.apache.vysper.xmpp.modules.extension.xep0049_privatedata.PrivateDataModule;
import org.apache.vysper.xmpp.modules.extension.xep0054_vcardtemp.VcardTempModule;
import org.apache.vysper.xmpp.modules.extension.xep0092_software_version.SoftwareVersionModule;
import org.apache.vysper.xmpp.modules.extension.xep0119_xmppping.XmppPingModule;
import org.apache.vysper.xmpp.modules.extension.xep0202_entity_time.EntityTimeModule;

/**
 * Default XMPP Server
 */
public class DefaultXMPPServer implements XMPPServer {
    private static String DEFAULT_HOST = "localhost";
    
    org.apache.vysper.xmpp.server.XMPPServer server;

    /**
     * Default constructor
     */
    public DefaultXMPPServer() {
     
    }
    
    /**
     * Start a Embedded XMPP Server
     */
    public void start() {
        try {
            StorageProviderRegistry providerRegistry = new MemoryStorageProviderRegistry();

            final AccountManagement accountManagement = (AccountManagement)providerRegistry.retrieve(AccountManagement.class);

            accountManagement.addUser("test@localhost", "password");
            accountManagement.addUser("test2@localhost", "password");

            server = new org.apache.vysper.xmpp.server.XMPPServer(DEFAULT_HOST);
            server.addEndpoint(new TCPEndpoint());
            server.addEndpoint(new StanzaSessionFactory());
            server.setStorageProviderRegistry(providerRegistry);

            server.setTLSCertificateInfo(new File("src/main/resources/config/bogus_mina_tls.cert"), "boguspw");

            try {
                server.start();
                System.out.println("vysper server is running...");
            } catch (Exception e) {
                e.printStackTrace();
            }

            server.addModule(new SoftwareVersionModule());
            server.addModule(new EntityTimeModule());
            server.addModule(new VcardTempModule());
            server.addModule(new XmppPingModule());
            server.addModule(new PrivateDataModule());
            
            //server.addModule(new MUCModule());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * Stop the XMPP Server and perform any cleanup necessary
     */
    public void stop() {
        server.stop();

    }
    
    /**
     * For debug/test purposes only
     * @param args
     * @throws IOException
     */
    public static void main(String args[]) throws IOException {
        DefaultXMPPServer server = new DefaultXMPPServer();
        server.start();
        System.out.println("XMPP server started !!!");
        System.in.read();
        System.out.println("Stopping ...");
        server.stop();
        System.out.println();
    }

}
