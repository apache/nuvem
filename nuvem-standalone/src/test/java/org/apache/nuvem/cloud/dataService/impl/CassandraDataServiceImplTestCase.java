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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.SliceQuery;

import org.apache.commons.collections.map.HashedMap;
import org.apache.nuvem.cloud.dataService.DataService;
import org.apache.nuvem.cloud.dataService.DataServiceException;
import org.apache.nuvem.cloud.dataService.Element;
import org.apache.nuvem.cloud.dataService.Feature;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CassandraDataServiceImplTestCase {

	private DataService dataService;

	@Before
	public void setUp() throws Exception {
		dataService = new CassandraDataServiceImpl("LogsCluster", "NuvemData",
				"localhost", "9160");
	}

	@After
	public void tearDown() throws Exception {
		System.out.println();
		getCluster().dropColumnFamily("NuvemData", "student");
		dataService = null;
	}

	@Test
	public void testCreateElement() throws DataServiceException {
		Element e1 = new Element("student");
		e1.setId("1");
		e1.addFeature("name", "sampleName1");
		Feature f1 = new Feature("age", 12);
		e1.addFeature(f1);
		dataService.createElement(e1);

		QueryResult<ColumnSlice<String, String>> r = getSliceQuery()
				.setKey("student(1)").setColumnFamily("student")
				.setRange(null, null, false, Integer.MAX_VALUE).execute();
		ColumnSlice<String, String> cs = r.get();
		Map<String, Object> results = new HashMap<String, Object>();
		for (HColumn hc : cs.getColumns()) {
			results.put(String.valueOf(hc.getName()), hc.getValue());
		}
		assertEquals(2, results.size());
		assertNotNull(results.get("name"));
		assertEquals("sampleName1", results.get("name"));
		assertNotNull(results.get("age"));
		assertEquals(12, Integer.parseInt((String) results.get("age")));

	}

	@Test
	public void testUpdateCreateElement() throws DataServiceException {
		Element e1 = new Element("student");
		e1.setId("2");
		e1.addFeature("name", "sampleName1");
		Feature f1 = new Feature("age", 12);
		e1.addFeature(f1);
		dataService.createElement(e1);

		QueryResult<ColumnSlice<String, String>> r = getSliceQuery()
				.setKey("student(2)").setColumnFamily("student")
				.setRange(null, null, false, Integer.MAX_VALUE).execute();
		ColumnSlice<String, String> cs = r.get();
		Map<String, Object> results = new HashMap<String, Object>();
		for (HColumn hc : cs.getColumns()) {
			results.put(String.valueOf(hc.getName()), hc.getValue());
		}
		assertEquals(2, results.size());
		assertNotNull(results.get("name"));
		assertEquals("sampleName1", results.get("name"));
		assertNotNull(results.get("age"));
		assertEquals(12, Integer.parseInt((String) results.get("age")));
		assertNull(results.get("country"));

		e1.addFeature("country", "LK");
		dataService.updateElement(e1);

		r = getSliceQuery().setKey("student(2)").setColumnFamily("student")
				.setRange(null, null, false, Integer.MAX_VALUE).execute();
		cs = r.get();
		results = new HashMap<String, Object>();
		for (HColumn hc : cs.getColumns()) {
			results.put(String.valueOf(hc.getName()), hc.getValue());
		}

		assertEquals(3, results.size());
		assertNotNull(results.get("name"));
		assertEquals("sampleName1", results.get("name"));
		assertNotNull(results.get("age"));
		assertEquals(12, Integer.parseInt((String) results.get("age")));
		assertNotNull(results.get("country"));
		assertEquals("LK", results.get("country"));

	}

	@Test
	public void testGetElement() throws DataServiceException {

		ColumnFamilyDefinition cfDef = HFactory.createColumnFamilyDefinition(
				"NuvemData", "student");
		getCluster().addColumnFamily(cfDef, false);
		Mutator<String> mutator = HFactory.createMutator(getKeyspace(),
				StringSerializer.get());
		mutator.addInsertion("student(3)", "student",
				HFactory.createStringColumn("name", "sampleName1"));
		mutator.addInsertion("student(3)", "student",
				HFactory.createStringColumn("age", "12"));
		mutator.execute();

		Element ele = dataService.getElement("student", "student(3)");

		assertNotNull(ele);
		assertEquals("student(3)", ele.getKey());
		Map<String, Object> results = new HashMap<String, Object>();
		for (Feature f : ele.getFeatures()) {
			results.put(f.getName(), f.getValue());
		}
		assertEquals(2, results.size());
		assertNotNull(results.get("name"));
		assertEquals("sampleName1", results.get("name"));
		assertNotNull(results.get("age"));
		assertEquals(12, Integer.parseInt((String) results.get("age")));

	}

	@Test
	public void testDeleteElement() throws DataServiceException {

		ColumnFamilyDefinition cfDef = HFactory.createColumnFamilyDefinition(
				"NuvemData", "student");
		getCluster().addColumnFamily(cfDef, false);
		Mutator<String> mutator = HFactory.createMutator(getKeyspace(),
				StringSerializer.get());
		mutator.addInsertion("student(4)", "student",
				HFactory.createStringColumn("name", "sampleName1"));
		mutator.addInsertion("student(4)", "student",
				HFactory.createStringColumn("age", "12"));
		mutator.execute();

		Element e1 = new Element("student");
		e1.setId("4");
		boolean re = dataService.deleteElement(e1);

		assertTrue(re);

		QueryResult<ColumnSlice<String, String>> r = getSliceQuery()
				.setKey("student(4)").setColumnFamily("student")
				.setRange(null, null, false, Integer.MAX_VALUE).execute();
		ColumnSlice<String, String> cs = r.get();
		Map<String, Object> results = new HashMap<String, Object>();
		for (HColumn hc : cs.getColumns()) {
			results.put(String.valueOf(hc.getName()), hc.getValue());
		}
		assertEquals(0, results.size());

	}

	private SliceQuery getSliceQuery() {
		// The key space where all the data shall be stored
		SliceQuery<String, String, String> q = HFactory.createSliceQuery(
				getKeyspace(), StringSerializer.get(), StringSerializer.get(),
				StringSerializer.get());
		return q;
	}

	private Keyspace getKeyspace() {
		return HFactory.createKeyspace("NuvemData", getCluster());
	}

	private Cluster getCluster() {
		Cluster cluster = HFactory.createCluster("LogsCluster",
				new CassandraHostConfigurator("localhost:9160"));
		return cluster;

	}

}
