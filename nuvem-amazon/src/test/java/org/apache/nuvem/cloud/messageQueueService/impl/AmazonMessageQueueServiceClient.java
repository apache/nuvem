
package org.apache.nuvem.cloud.messageQueueService.impl;

import com.amazonaws.AmazonWebServiceClient;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSClient;

public class AmazonMessageQueueServiceClient {
	
	public static AmazonWebServiceClient getAmazonSQSClient(final String accessKey, final String secretKey) {	
		
		if(accessKey == null || secretKey == null){
			return new MockAmazonSQSClient();
		}
		
		AWSCredentials awsCredentials = new AWSCredentials() {
			@Override
			public String getAWSAccessKeyId() {
				return accessKey;
			}
			@Override
			public String getAWSSecretKey() {
				return secretKey;
			}
		};
		
		return  new AmazonSQSClient(awsCredentials);
	}
}