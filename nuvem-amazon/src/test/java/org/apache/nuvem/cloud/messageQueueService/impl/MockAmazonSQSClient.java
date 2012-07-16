
package org.apache.nuvem.cloud.messageQueueService.impl;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.AmazonWebServiceClient;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ResponseMetadata;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.amazonaws.services.sqs.model.GetQueueUrlRequest;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.ListQueuesResult;
import com.amazonaws.services.sqs.model.AddPermissionRequest;
import com.amazonaws.services.sqs.model.DeleteMessageBatchResult;
import com.amazonaws.services.sqs.model.DeleteMessageBatchRequest;
import com.amazonaws.services.sqs.model.ListQueuesResult;
import com.amazonaws.services.sqs.model.ListQueuesRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.SetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.ChangeMessageVisibilityBatchResult;
import com.amazonaws.services.sqs.model.ChangeMessageVisibilityBatchRequest;
import com.amazonaws.services.sqs.model.ChangeMessageVisibilityRequest;
import com.amazonaws.services.sqs.model.RemovePermissionRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchResult;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.QueueDoesNotExistException;
import com.amazonaws.services.sqs.model.InvalidMessageContentsException;
import com.amazonaws.services.sqs.model.OverLimitException;
import com.amazonaws.services.sqs.model.ReceiptHandleIsInvalidException;
import com.amazonaws.services.sqs.model.InvalidIdFormatException;

public class MockAmazonSQSClient extends AmazonWebServiceClient implements AmazonSQS {
	
	public MockAmazonSQSClient() {
		super(null);
	}
	
	@Override 
	public GetQueueUrlResult getQueueUrl(GetQueueUrlRequest getQueueUrlRequest)
            throws QueueDoesNotExistException, AmazonServiceException, AmazonClientException {
		return (null);
	}
	
	@Override
	public SendMessageResult sendMessage(SendMessageRequest sendMessageRequest)
            throws InvalidMessageContentsException, AmazonServiceException, AmazonClientException {
		return (null);
	}
	
	@Override
	public ReceiveMessageResult receiveMessage(ReceiveMessageRequest receiveMessageRequest)
            throws OverLimitException, AmazonServiceException, AmazonClientException {
		return (null);
	}
	
	@Override
	public void deleteMessage(DeleteMessageRequest deleteMessageRequest)
            throws ReceiptHandleIsInvalidException, InvalidIdFormatException, AmazonServiceException, AmazonClientException {
	}
	
	@Override
	public ResponseMetadata getCachedResponseMetadata(AmazonWebServiceRequest request) {
		return (null);
	}
	
	@Override
	public ListQueuesResult listQueues() throws AmazonServiceException, AmazonClientException {
		return (null);
	}
	
	@Override
	public void addPermission(AddPermissionRequest addPermissionRequest) 
            					throws AmazonServiceException, AmazonClientException {
	}
	
	@Override
	public CreateQueueResult createQueue(CreateQueueRequest createQueueRequest)
            					throws AmazonServiceException, AmazonClientException {
		return (null); 
	}
	
	@Override
	public DeleteMessageBatchResult deleteMessageBatch(DeleteMessageBatchRequest deleteMessageBatchRequest)
            					throws AmazonServiceException, AmazonClientException {
		return (null); 
	}
	
	@Override
	public ListQueuesResult listQueues(ListQueuesRequest listQueuesRequest) 
								throws AmazonServiceException, AmazonClientException {
		return (null); 
	}
	
	@Override
	public void deleteQueue(DeleteQueueRequest deleteQueueRequest)
            					throws AmazonServiceException, AmazonClientException {
		
	}
	
	@Override
	public void setEndpoint(String endpoint) throws IllegalArgumentException {
		
	}
	
	@Override
	public void setQueueAttributes(SetQueueAttributesRequest setQueueAttributesRequest)
            					throws AmazonServiceException, AmazonClientException {
		
	}
	
	@Override
	public ChangeMessageVisibilityBatchResult changeMessageVisibilityBatch(ChangeMessageVisibilityBatchRequest changeMessageVisibilityBatchRequest)
            					throws AmazonServiceException, AmazonClientException {
		return (null);
	}
	
	@Override
	public void changeMessageVisibility(ChangeMessageVisibilityRequest changeMessageVisibilityRequest)
            					throws AmazonServiceException, AmazonClientException {
		
	}
	
	@Override
	public void removePermission(RemovePermissionRequest removePermissionRequest)
            					throws AmazonServiceException, AmazonClientException {
		
	}
	
	@Override
	public GetQueueAttributesResult getQueueAttributes(GetQueueAttributesRequest getQueueAttributesRequest)
            					throws AmazonServiceException, AmazonClientException {
		return (null);
	}
	
	@Override
	public SendMessageBatchResult sendMessageBatch(SendMessageBatchRequest sendMessageBatchRequest)
            					throws AmazonServiceException, AmazonClientException {
		return (null);
	}
	
	@Override
	public void shutdown() {
		
	}
	
}