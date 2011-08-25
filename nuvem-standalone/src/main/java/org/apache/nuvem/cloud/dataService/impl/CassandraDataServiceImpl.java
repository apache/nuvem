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

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.SliceQuery;

import org.apache.nuvem.cloud.dataService.DataService;
import org.apache.nuvem.cloud.dataService.DataServiceException;
import org.apache.nuvem.cloud.dataService.Element;
import org.apache.nuvem.cloud.dataService.Feature;

public class CassandraDataServiceImpl implements DataService {

	private final static String DEFAULT_CLUSTER_NAME = "NuvemCluster";
	private final static String DEFAULT_KEYSPACE_NAME = "NuvemData";
	private final static String DEFAULT_HOST = "localhost";
	private final static String DEFAULT_PORT = "9160";

	private Cluster cluster;
	private Keyspace keyspace;
	private String keyspaceName;
	private StringSerializer stringSerializer;

	public CassandraDataServiceImpl() {
		this(DEFAULT_CLUSTER_NAME, DEFAULT_KEYSPACE_NAME, DEFAULT_HOST,
				DEFAULT_PORT);
	}

	public CassandraDataServiceImpl(String host, String port) {
		this(DEFAULT_CLUSTER_NAME, DEFAULT_KEYSPACE_NAME, host, port);
	}

	public CassandraDataServiceImpl(String clusterName, String keyspaceName,
			String host, String port) {
		cluster = HFactory.createCluster(clusterName,
				new CassandraHostConfigurator(host + ":" + port));
		// The key space where all the data shall be stored
		keyspace = HFactory.createKeyspace(keyspaceName, cluster);
		this.keyspaceName = keyspaceName;
		stringSerializer = StringSerializer.get();
	}

	public CassandraDataServiceImpl(Cluster cluster, Keyspace keyspace,
			String keyspaceName) {
		this.cluster = cluster;
		this.keyspace = keyspace;
		this.keyspaceName = keyspaceName;
		stringSerializer = StringSerializer.get();
	}

	@Override
	public Element createElement(Element element) throws DataServiceException {
		saveElement(element);
		return element;
	}

	@Override
	public Element updateElement(Element element) throws DataServiceException {
		String type = element.getType();
		String id = element.getKey();
		Mutator<String> mutator = getMutator();
		mutator.addDeletion(id, type);
		mutator.execute();
		saveElement(element);
		return element;
	}

	@Override
	public Element getElement(String kind, String id)
			throws DataServiceException {
		SliceQuery<String, String, String> q = HFactory.createSliceQuery(
				keyspace, stringSerializer, stringSerializer, stringSerializer);
		q.setKey(id).setColumnFamily(kind)
				.setRange(null, null, false, Integer.MAX_VALUE);
		QueryResult<ColumnSlice<String, String>> r = q.execute();
		ColumnSlice<String, String> cs = r.get();
		Element element = new Element(kind);
		element.setKey(id);
		element.setPersist();
		for (HColumn hc : cs.getColumns()) {
			element.addFeature(String.valueOf(hc.getName()), hc.getValue());
		}
		return element;
	}

	@Override
	public boolean deleteElement(Element element) throws DataServiceException {
		String type = element.getType();
		String id = element.getKey();
		Mutator<String> mutator = getMutator();
		mutator.addDeletion(id, type);
		mutator.execute();
		return true;
	}

	private Mutator getMutator() {
		Mutator<String> mutator = HFactory.createMutator(keyspace,
				stringSerializer);
		return mutator;

	}

	private void saveElement(Element element) throws DataServiceException {
		String type = element.getType();
		String id = element.getKey();
		createColumnFamilyifnotExsists(type);

		Mutator<String> mutator = getMutator();
		for (Feature feature : element.getFeatures()) {
			mutator.addInsertion(
					id,
					type,
					HFactory.createStringColumn(feature.getName(),
							String.valueOf(feature.getValue())));
		}
		mutator.execute();
	}

	private void createColumnFamilyifnotExsists(String ColumnFamilyName) {
		KeyspaceDefinition kd = cluster.describeKeyspace(keyspaceName);
		boolean create = true;
		for (ColumnFamilyDefinition c : kd.getCfDefs()) {
			if (ColumnFamilyName != null
					&& c.getName().equals(ColumnFamilyName)) {
				create = false;
			}
		}

		if (!create) {
			// CF already exist;
			return;
		}

		// create new CF
		ColumnFamilyDefinition cfDef = HFactory.createColumnFamilyDefinition(
				keyspaceName, ColumnFamilyName);
		cluster.addColumnFamily(cfDef, false);
	}

}
