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

import org.oasisopen.sca.annotation.Remotable;

/**
 * The Interface UserService defines services provide by Nuvem key-value
 * data-store component also act as abreaction layer for cloud platform specific
 * data services. Element is the basic persistent unit and may contains number
 * of Features that associate with the Element. DataService interface facilitate
 * to create, update , read and delete Elements.
 */
@Remotable
public interface UserService {

	/**
	 * Get the current User.
	 * 
	 * @return the User
	 */
    public User getCurrentUser();

    /**
     * Check if the User is Admin.
     * 
     * @return the boolean
     */
    public boolean isUserAdmin();

    /**
     * Check if the User is Loggein.
     * 
     * @return the boolean
     */
    public boolean isUserLoggedIn();

    /**
     * Get the User Context.
     * 
     * @param destinationURL the destinationURL 
     * @param authDomain the authDomain
     * @return the UserContext
     */
    public UserContext getUserContext(String destinationURL, String authDomain);

    /**
     * Create a Login URL.
     * 
     * @param destinationURL the destinationURL
     * @param authDomain the authDomain
     * @return the Create Login URL
     */
    public String createLoginURL(String destinationURL, String authDomain);

    /**
     * Create a Logout URL.
     * 
     * @param destinationURL the destinationURL
     * @param authDomain the authDomain
     * @return the Create Logout URL
     */
    public String createLogoutURL(String destinationURL, String authDomain);

}
