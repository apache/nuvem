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

package org.apache.nuvem.cloud.user.impl;

import org.apache.nuvem.cloud.user.User;
import org.apache.nuvem.cloud.user.UserContext;
import org.apache.nuvem.cloud.user.UserService;
import org.oasisopen.sca.annotation.Init;
import org.oasisopen.sca.annotation.Scope;
import org.oasisopen.sca.annotation.Service;

import com.google.appengine.api.users.UserServiceFactory;

@Service(UserService.class)
@Scope("COMPOSITE")
public class GoogleUserService implements UserService {
    private com.google.appengine.api.users.UserService googleUerService;

    @Init
    public void init() {
        googleUerService = UserServiceFactory.getUserService();
    }
    

    public User getCurrentUser() {
        return fromGoogleUser(googleUerService.getCurrentUser());
    }

    public boolean isUserAdmin() {
        //FIXME handle via roles from tuscany user api
        throw new UnsupportedOperationException("Not supported yet");
    }

    public boolean isUserLoggedIn() {
        return googleUerService.isUserLoggedIn();
    }
    
    public UserContext getUserContext(String destinationURL, String authDomain) {
        return new UserContext (getCurrentUser(),
                                isUserLoggedIn(),
                                createLoginURL(destinationURL, authDomain),
                                createLogoutURL(destinationURL, authDomain));
    }
    
    public String createLoginURL(String destinationURL, String authDomain) {
        if(authDomain != null && authDomain.length() > 0) {
            return googleUerService.createLoginURL(destinationURL, authDomain);
        } else {
            return googleUerService.createLoginURL(destinationURL);
        }
    }

    public String createLogoutURL(String destinationURL, String authDomain) {
        if(authDomain != null && authDomain.length() > 0) {
            return googleUerService.createLogoutURL(destinationURL, authDomain);
        } else {
            return googleUerService.createLogoutURL(destinationURL);
        }
    }

    private static User fromGoogleUser(com.google.appengine.api.users.User googleUser) {
        if(googleUser != null) {
            return new User(googleUser.getUserId(), googleUser.getNickname(), googleUser.getEmail());
        } 
        
        return User.DUMMY_USER;
    }

    
}
