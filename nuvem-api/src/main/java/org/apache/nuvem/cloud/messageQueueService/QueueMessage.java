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

/**
 * The QueueMessage class
 */
public class QueueMessage {
	
	/** The id */
	private String id;	
	
	/** The message body */
	private String messageBody;
	
	/**
	 * Instantiates a new QueueMessage
	 */
	public QueueMessage (){
		
	}
	
	/**
	 * Instantiates a new QueueMessage
	 * 
	 * @param id the id
	 * @param messageBody the message body
	 */
	public QueueMessage (String id, String messageBody){
		this.messageBody = messageBody;
		this.id = id;		
	}
	
	/**
	 * Instantiates a new QueueMessage
	 * 
	 * @param messageBody the message body
	 */
	public QueueMessage (String messageBody) {
		this.messageBody = messageBody;
	}
	
	/**
	 * Gets the Id
	 * 
	 * @return the id
	 */
	public String getId(){
		return id;
	}
	
	/**
	 * Sets the Id
	 * 
	 * @param id the id
	 */
	public void setId(String id){
		this.id = id;
	}
	
	/**
	 * Gets the message body
	 * 
	 * @return the messages body
	 */
	public String getMessageBody(){
		return messageBody;		
	}
	
	/**
	 * Sets the message body
	 * 
	 * @param messageBody the message body
	 */
	public void setMessageBody(String messageBody){
		this.messageBody = messageBody;
	}
	
}
