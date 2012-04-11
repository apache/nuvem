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

import java.io.Serializable;

/**
 * The Class User.
 */
public class User implements Serializable {
    private static final long serialVersionUID = -503746790472903416L;

    protected String userId;
    protected String nickName;
    protected String email;

    public static enum ROLES {
        UNDEFINED, USER, ADMIN
    };

    /** Represents a dummy user for usage instead of nulls **/
    public static final User DUMMY_USER = new User("dummyid", "dummy name", "dummy@email.com");

    /**
     * Constructs a new User.
     */
    public User() {

    }

    /**
     * Constructs a new User.
     * 
     * @param userId the userId
     * @param nickeName the nickeName 
     * @param email the email
     */
    public User(String userId, String nickName, String email) {
        this.userId = userId;
        this.nickName = nickName;
        this.email = email;
    }

    /**
     * Returns the user id of the User.
     * 
     * @return the user id
     */
    public String getUserId() {
        return this.userId;
    }

    /**
     * Returns the nick name of the User.
     * 
     * @return the nick name.
     */
    public String getNickname() {
        return this.nickName;
    }

    /**
     * Returns the email of the User.
     * 
     * @return the email.
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Checks only for the user ID as it is supposed to be a unique identifier
     * for the user.
     * 
     * @param o the o
     * @return true, if successful
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof User))
            return false;

        final User user = (User)o;

        return userId.equals(user.userId);
    }

    /**
     * Considers only the hashcode of the userid as userid is the unique id of
     * the user.
     * 
     * @return the hashcode of the user id.
     */
    @Override
    public int hashCode() {
        return userId.hashCode();
    }
}
