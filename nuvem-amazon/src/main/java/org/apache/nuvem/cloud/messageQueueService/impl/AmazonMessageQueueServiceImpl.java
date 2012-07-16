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

package org.apache.nuvem.cloud.messageQueueService.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Iterator;
import java.util.ArrayList;

import org.apache.nuvem.cloud.messageQueueService.MessageQueueService;
import org.apache.nuvem.cloud.messageQueueService.QueueMessage;
import org.apache.nuvem.cloud.messageQueueService.QueueMessageHandle;
import org.apache.nuvem.cloud.messageQueueService.MessageQueueServiceException;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.PropertiesCredentials;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.amazonaws.services.sqs.model.GetQueueUrlRequest;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;

/**
 *The AmazonMessageQueueServiceImpl class
 */
public class AmazonMessageQueueServiceImpl implements MessageQueueService {
	
	public static final String AMAZON_CREDENTIALS_CONFIG_FILE_NAME = "AwsCredentials.properties";
	public static final String AMAZON_ACCESS_KEY = "accessKey";
	public static final String AMAZON_SECRET_KEY = "secretKey";
	public static final String AMAZON_QUEUE_CONFIG_FILE_NAME = "queue.properties";
	public static final String AMAZON_QUEUE_NAME = "QueueName";
	public static final String AMAZON_DEFAULT_QUEUE_NAME = "MyQueue";
	
	private AmazonSQS sqsClient;
	private String queueURL;
	private String queueName;
	
	/**
	 * Instantiates a new AmazonMessageQueueServiceImpl
	 */
	public AmazonMessageQueueServiceImpl() {
		try {
			this.sqsClient = new AmazonSQSClient(new PropertiesCredentials(
					AmazonMessageQueueServiceImpl.class.
	                		getResourceAsStream(AMAZON_CREDENTIALS_CONFIG_FILE_NAME)));
			
			this.queueName = AMAZON_DEFAULT_QUEUE_NAME;
			setQueueURl(queueName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Instantiates a new AmazonMessageQueueServiceImpl
	 * 
	 * @param accessKey The Amazon access key 
	 * @param secretKey The Amazon secret key
	 */
	public AmazonMessageQueueServiceImpl(final String accessKey, final String secretKey) {
		try{
			AWSCredentials amazonCredentials = new AWSCredentials() {
				@Override
				public String getAWSAccessKeyId() {
					return accessKey;
				}
				
				@Override
				public String getAWSSecretKey() {
					return secretKey;
				}
			};
			
			sqsClient = new AmazonSQSClient(amazonCredentials);
			this.queueName = AMAZON_DEFAULT_QUEUE_NAME;
			setQueueURl(queueName);
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
	
	/**
	 * Instantiates a new AmazonMessageQueueServiceImpl
	 * 
	 * @param configAmazonCredentials Amazon credential configuration
	 */
	public AmazonMessageQueueServiceImpl(final Map configAmazonCredentials) {
		try{
			AWSCredentials amazonCredentials = new AWSCredentials() {
				@Override
				public String getAWSAccessKeyId() {
					return (String) configAmazonCredentials.get(AMAZON_ACCESS_KEY);
				}
				
				@Override
				public String getAWSSecretKey() {
					return (String) configAmazonCredentials.get(AMAZON_SECRET_KEY);
				}
			};
			
			sqsClient = new AmazonSQSClient(amazonCredentials);
			this.queueName = AMAZON_DEFAULT_QUEUE_NAME;
			setQueueURl(queueName);
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
	
	/**
	 * Instantiates a new AmazonMessageQueueServiceImpl
	 * 
	 * @param sqsClient The AmazonSQS client
	 */
	public AmazonMessageQueueServiceImpl(AmazonSQSClient sqsClient) {
		try{
			this.sqsClient = sqsClient;
			this.queueName = AMAZON_DEFAULT_QUEUE_NAME;
			setQueueURl(queueName);
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
	
	/**
	 * Sets the queueURL using queueName
	 * 
	 * @param queueName the queue name
	 */
	private void setQueueURl(String queueName) throws IOException  {		
		GetQueueUrlRequest getQueueUrlRequest = new GetQueueUrlRequest(queueName);
        GetQueueUrlResult getQueueUrlResult= sqsClient.getQueueUrl(getQueueUrlRequest);
        this.queueURL = getQueueUrlResult.getQueueUrl();
	}
	
	/**
	 * Gets the queue name from property file	 
	 */
	private String getQueueName() throws IOException {
		Properties queueProperties = new Properties();
		queueProperties.load(AmazonMessageQueueServiceImpl.class.
				getResourceAsStream(AMAZON_QUEUE_CONFIG_FILE_NAME));
		return queueProperties.getProperty(AMAZON_QUEUE_NAME);
	}
	
	/**
	 * Sends the message
	 * 
	 * @param queueMessage the queue message
	 * @return the queue message handle
	 * @throws MessageQueueServiceException
	 */
	@Override
	public QueueMessageHandle sendMessage(QueueMessage queueMessage) throws MessageQueueServiceException {
		try{
			SendMessageRequest sendMessageRequest = new SendMessageRequest(queueURL, queueMessage.getMessageBody());
			SendMessageResult sendMessageResult = sqsClient.sendMessage(sendMessageRequest);
			QueueMessageHandle queueMessageHandle = new QueueMessageHandle();
			queueMessageHandle.setId(sendMessageResult.getMessageId());
			queueMessageHandle.setMessageBody(queueMessage.getMessageBody());
			return queueMessageHandle;
		}catch(Exception e){
			throw new MessageQueueServiceException(e);
		}
	}
	
	/**
	 * Receive Messages
	 * 
	 * @param numMessages number of messages to receive
	 * @return list of queue messages
	 * @throws MessageQueueServiceException
	 */
	@Override
	public List<QueueMessage> receiveMessage(int numMessages) throws MessageQueueServiceException{
		try{
			ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(this.queueURL);
			receiveMessageRequest.setMaxNumberOfMessages(numMessages);
	        List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).getMessages();
	        List<QueueMessage> queueMessages = new ArrayList<QueueMessage>(messages.size());
	        Iterator messageIterator = messages.iterator();
	        Message message;
	        while(messageIterator.hasNext()){
	        	message = (Message)messageIterator.next();
	        	queueMessages.add(new QueueMessage(message.getReceiptHandle(), message.getBody()));	        	
	        }
	        return queueMessages;
		}catch(Exception e){			
			throw new MessageQueueServiceException(e);
		}
	}
	
	/**
	 * Deletes the message
	 * 
	 * @param queueMessage the message to delete
	 * @return true, if deleting message is successful
	 * @throws MessageQueueServiceException
	 */
	@Override
	public boolean deleteMessage(QueueMessage queueMessage) throws MessageQueueServiceException{
		try{
			String messageRecieptHandle = queueMessage.getId();
			sqsClient.deleteMessage(new DeleteMessageRequest(this.queueURL, messageRecieptHandle));
			return true;
		}catch(Exception e){
			throw new MessageQueueServiceException(e);
		}
	}
}
