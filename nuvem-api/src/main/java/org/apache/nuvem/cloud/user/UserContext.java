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

package org.apache.nuvem.cloud.user;

public class UserContext extends User {
    protected String loginUrl;
    protected String logoutUrl;
    protected boolean isUserLoggedIn;
    
    public UserContext(String userId, String nickName, String email, boolean isUserLoggedIn, String loginUrl, String logoutUrl) {
        super(userId, nickName, email);
        this.isUserLoggedIn = isUserLoggedIn;
        this.loginUrl = loginUrl;
        this.logoutUrl = logoutUrl;
    }
    
    public UserContext(User user, boolean isUserLoggedIn, String loginUrl, String logoutUrl) {
        super(user.getUserId(), user.getNickname(), user.getEmail());
        this.isUserLoggedIn = isUserLoggedIn;
        this.loginUrl = loginUrl;
        this.logoutUrl = logoutUrl;
    }
    
    public boolean isUserLoggedIn() {
        return this.isUserLoggedIn;
    }
    
    public String getLoginUrl() {
        return this.loginUrl;
    }
    
    public String getLogoutUrl() {
        return this.logoutUrl;
    }
}
