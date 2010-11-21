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

import java.lang.reflect.Field;


import junit.framework.Assert;

import org.apache.nuvem.cloud.user.UserService;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.users.User;

/**
 * Tests the Google user service Wrapper class by mocking the the actual google
 * API.

 *
 */
public class GoogleUserServiceTestCase {

	private com.google.appengine.api.users.UserService mockGoogleUserService;

	private UserService userService = new GoogleUserService();

	@Before
	public void setUp() throws IllegalArgumentException, IllegalAccessException {
		mockGoogleUserService = EasyMock
				.createMock(com.google.appengine.api.users.UserService.class);

		for (Field field : userService.getClass().getDeclaredFields()) {
			if (field.getName().equals("googleUerService")) {
				field.setAccessible(true);
				field.set(userService, mockGoogleUserService);
			}
		}
	}

	@Test
	public void testForValidUser() {
		// set expectations
		EasyMock.expect(mockGoogleUserService.getCurrentUser()).andReturn(
				new User("test@test.com", "domain", "tester"));
		EasyMock.replay(mockGoogleUserService);
		// test the api
		org.apache.nuvem.cloud.user.User user = userService.getCurrentUser();
		Assert.assertNotNull(user);
	}

	@Test
	public void testForNullUser() {
		// set expecatations
		EasyMock.expect(mockGoogleUserService.getCurrentUser()).andReturn(null);
		EasyMock.replay(mockGoogleUserService);

		// test api
		org.apache.nuvem.cloud.user.User user = userService.getCurrentUser();
		Assert.assertEquals(org.apache.nuvem.cloud.user.User.DUMMY_USER, user);
	}
}
