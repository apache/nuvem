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

/**
 * The Class UserContex.
 */
public class UserContext extends User {
    private static final long serialVersionUID = 1339319191804052423L;

    protected String loginUrl;
    protected String logoutUrl;
    protected boolean isUserLoggedIn;

    /**
     * Constructs a new User Context.
     */
    public UserContext() {
        super();
    }

    /**
     * Constructs a new User Context.
     * 
     * @param userId the userId 
     * @param nickName the nickName
     * @param email the email
     * @param isUserLoggedIn the isUserLoggedIn
     * @param loginUrl the loginUrl
     * @param logoutUrl the logoutUrl
     */
    public UserContext(String userId,
                       String nickName,
                       String email,
                       boolean isUserLoggedIn,
                       String loginUrl,
                       String logoutUrl) {
        super(userId, nickName, email);
        this.isUserLoggedIn = isUserLoggedIn;
        this.loginUrl = loginUrl;
        this.logoutUrl = logoutUrl;
    }

    /**
     * Constructs a new User Context.
     * 
     * @param user the user
     * @param isUserLoggedIn the isUserLoggedIn 
     * @param loginUrl the loginUrl
     * @param logoutUrl the logoutUrl
     */
    public UserContext(User user, boolean isUserLoggedIn, String loginUrl, String logoutUrl) {
        super(user.getUserId(), user.getNickname(), user.getEmail());
        this.isUserLoggedIn = isUserLoggedIn;
        this.loginUrl = loginUrl;
        this.logoutUrl = logoutUrl;
    }

    /**
     * Return the isUserLoggedIn.
     * 
     * @return the isUserLoggedIn
     */
    public boolean isUserLoggedIn() {
        return this.isUserLoggedIn;
    }

    /**
     * Return the getLoginUrl.
     * 
     * @return the loginUrl
     */
    public String getLoginUrl() {
        return this.loginUrl;
    }

    /**
     * Return the logoutUrl.
     * 
     * @return the logoutUrl
     */
    public String getLogoutUrl() {
        return this.logoutUrl;
    }
}
