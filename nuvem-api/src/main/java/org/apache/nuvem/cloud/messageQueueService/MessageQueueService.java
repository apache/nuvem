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

package org.apache.nuvem.cloud.messageQueueService;

import java.util.List;
import org.oasisopen.sca.annotation.Remotable;

/**
 * The Interface MessageQueueService defines services provide by Nuvem Message Queue
 * Component and also acts as the abraction layer for cloud platform specific
 * message queueing services. QueueMessage holds attribute values of a message sent to a message
 * queue. 
 */

@Remotable
public interface MessageQueueService {

	/**
	 * Send message
	 * 
	 * @param queueMessage the message
	 * @return QueueMessageHandle 
	 * @throws MessageQueueServiceException the message queue service exception
	 */
	public QueueMessageHandle sendMessage(QueueMessage queueMessage) throws MessageQueueServiceException;
	
	/**
	 * Receives messages
	 * 
	 * @param numMessages number of messages to receive
	 * @return list of messages
	 * @throws MessageQueueServiceException the message queue service exception
	 */
	public List<QueueMessage> receiveMessage(int numMessages) throws MessageQueueServiceException; 
	
	/**
	 * Deletes the message
	 *  
	 *  @param queueMessage the message
	 *  @return true, if message deleted sucessfully
	 *  @throws MessageQueueServiceException the message queue service exception
	 */
	public boolean deleteMessage(QueueMessage queueMessage) throws MessageQueueServiceException;
	
}
