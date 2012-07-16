
package org.apache.nuvem.cloud.messageQueueService.impl;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.net.URL;

import org.apache.nuvem.cloud.messageQueueService.MessageQueueService;
import org.apache.nuvem.cloud.messageQueueService.QueueMessage;
import org.apache.nuvem.cloud.messageQueueService.QueueMessageHandle;
import org.apache.nuvem.cloud.messageQueueService.MessageQueueServiceException;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.amazonaws.services.sqs.model.GetQueueUrlRequest;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.QueueDoesNotExistException;
import com.amazonaws.services.sqs.model.InvalidMessageContentsException;
import com.amazonaws.services.sqs.model.OverLimitException;
import com.amazonaws.services.sqs.model.ReceiptHandleIsInvalidException;
import com.amazonaws.services.sqs.model.InvalidIdFormatException;

public class AmazonMessageQueueServiceImplTestCase {
		
		private MessageQueueService messageQueueService; 
		
		@Before
		public void setUp() throws Exception {
			AmazonSQSClient sqsClient =  getSQSClient();
			messageQueueService = new  AmazonMessageQueueServiceImpl(sqsClient);
		}

		private AmazonSQSClient getSQSClient() {
			try {
				Properties awsCredentialsProperties = new Properties();
				URL url = ClassLoader.getSystemResource("test.properties");
				awsCredentialsProperties.load(url.openStream());
				final String accessKey = awsCredentialsProperties.getProperty("accessKey");
				final String secretKey = awsCredentialsProperties.getProperty("secretKey");
				
				return (AmazonSQSClient) AmazonMessageQueueServiceClient.getAmazonSQSClient(accessKey,secretKey);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@After
		public void tearDown() throws Exception {
			messageQueueService = null;
		}
		
		@Test
		public void testSendMessage() throws MessageQueueServiceException {
			QueueMessage qMessage = new QueueMessage();
			qMessage.setId("1");
			qMessage.setMessageBody("Message");
			
			assertNotNull(qMessage);
			
			QueueMessageHandle qMessageHandle = new QueueMessageHandle();
			qMessageHandle = messageQueueService.sendMessage(qMessage);
			
			assertNotNull(qMessageHandle);
			assertEquals("Message", qMessageHandle.getMessageBody());
		}
		
		@Test
		public void testReceiveMessage() throws MessageQueueServiceException {
			
			AmazonSQSClient amazonSQSClient = getSQSClient();
			GetQueueUrlRequest getQueueUrlRequest = new GetQueueUrlRequest("MyQueue");
			GetQueueUrlResult getQueueUrlResult= amazonSQSClient.getQueueUrl(getQueueUrlRequest);
			String queueURL = getQueueUrlResult.getQueueUrl();
			
			assertNotNull(getQueueUrlRequest);
			assertNotNull(getQueueUrlResult);
			assertNotNull(queueURL);
			
			SendMessageRequest sendMessageRequest1 = new SendMessageRequest(
																	queueURL, "Message 01");
			SendMessageResult sendMessageResult1 = amazonSQSClient.sendMessage(sendMessageRequest1);
			SendMessageRequest sendMessageRequest2 = new SendMessageRequest(
																	queueURL, "Message 02");
			SendMessageResult sendMessageResult2 = amazonSQSClient.sendMessage(sendMessageRequest2);
			
			assertNotNull(sendMessageRequest1);
			assertNotNull(sendMessageResult1);
			assertNotNull(sendMessageRequest2);
			assertNotNull(sendMessageResult2);
			
			int numberOfMessages = 3;
			
			List<QueueMessage> qMessages = messageQueueService.receiveMessage(numberOfMessages);
			
			System.out.println(numberOfMessages);
			assertNotNull(qMessages);
			assertEquals(3, qMessages.size());
			assertNotNull(qMessages.get(qMessages.size()-1));
		}
		
		@Test
		public void testDeleteMessage() throws MessageQueueServiceException {
			
			AmazonSQSClient amazonSQSClient = getSQSClient();
			GetQueueUrlRequest getQueueUrlRequest = new GetQueueUrlRequest("MyQueue");
			GetQueueUrlResult getQueueUrlResult= amazonSQSClient.getQueueUrl(getQueueUrlRequest);
			String queueURL = getQueueUrlResult.getQueueUrl();
			
			assertNotNull(getQueueUrlRequest);
			assertNotNull(getQueueUrlResult);
			assertNotNull(queueURL);
			
			ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueURL);
	        List<Message> messages = amazonSQSClient.receiveMessage(receiveMessageRequest).getMessages();
			
	        assertNotNull(receiveMessageRequest);
			assertNotNull(messages);
			
			Message message = messages.get(0);
			
			assertNotNull(message);
			
			QueueMessage qMessage = new QueueMessage(message.getReceiptHandle(), message.getBody());
			
			assertNotNull(qMessage);
			
			boolean deleteResult = messageQueueService.deleteMessage(qMessage);
	        
			assertNotNull(deleteResult);
			
			assertEquals(true, deleteResult);
			
		}
		
}
