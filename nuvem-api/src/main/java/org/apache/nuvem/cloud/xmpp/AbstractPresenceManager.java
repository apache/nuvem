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

package org.apache.nuvem.cloud.xmpp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.nuvem.cloud.xmpp.presence.PresenceListener;
import org.apache.nuvem.cloud.xmpp.presence.PresenceManager;

/**
 * Holds common code between various cloud platform to avoid duplication.
 *
 */
public abstract class AbstractPresenceManager implements PresenceManager {

	protected List<PresenceListener> listeners = new ArrayList<PresenceListener>();

	public void clearListeners() {
		listeners.clear();
	}

	public List<PresenceListener> listeners() {
		return Collections.unmodifiableList(listeners);
	}

}
