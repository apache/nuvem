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

package org.apache.nuvem.cloud.dataService.impl;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.AmazonWebServiceClient;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.model.BatchDeleteAttributesRequest;
import com.amazonaws.services.simpledb.model.BatchPutAttributesRequest;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.DeleteAttributesRequest;
import com.amazonaws.services.simpledb.model.DeleteDomainRequest;
import com.amazonaws.services.simpledb.model.DomainMetadataRequest;
import com.amazonaws.services.simpledb.model.DomainMetadataResult;
import com.amazonaws.services.simpledb.model.GetAttributesRequest;
import com.amazonaws.services.simpledb.model.GetAttributesResult;
import com.amazonaws.services.simpledb.model.ListDomainsRequest;
import com.amazonaws.services.simpledb.model.ListDomainsResult;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;

public class MockAmazonSimpleDBClient extends AmazonWebServiceClient implements AmazonSimpleDB{

	public MockAmazonSimpleDBClient(){
		super(null);
	}
	
	public MockAmazonSimpleDBClient(ClientConfiguration clientConfiguration) {
		super(clientConfiguration);
		// TODO Auto-generated constructor stub
	}

	@Override
	public SelectResult select(SelectRequest selectRequest)
			throws AmazonServiceException, AmazonClientException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putAttributes(PutAttributesRequest putAttributesRequest)
			throws AmazonServiceException, AmazonClientException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void batchDeleteAttributes(
			BatchDeleteAttributesRequest batchDeleteAttributesRequest)
			throws AmazonServiceException, AmazonClientException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteDomain(DeleteDomainRequest deleteDomainRequest)
			throws AmazonServiceException, AmazonClientException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createDomain(CreateDomainRequest createDomainRequest)
			throws AmazonServiceException, AmazonClientException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAttributes(DeleteAttributesRequest deleteAttributesRequest)
			throws AmazonServiceException, AmazonClientException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ListDomainsResult listDomains(ListDomainsRequest listDomainsRequest)
			throws AmazonServiceException, AmazonClientException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GetAttributesResult getAttributes(
			GetAttributesRequest getAttributesRequest)
			throws AmazonServiceException, AmazonClientException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void batchPutAttributes(
			BatchPutAttributesRequest batchPutAttributesRequest)
			throws AmazonServiceException, AmazonClientException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DomainMetadataResult domainMetadata(
			DomainMetadataRequest domainMetadataRequest)
			throws AmazonServiceException, AmazonClientException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListDomainsResult listDomains() throws AmazonServiceException,
			AmazonClientException {
		// TODO Auto-generated method stub
		return null;
	}

}
