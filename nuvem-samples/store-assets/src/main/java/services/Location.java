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

package services;

public class Location {

	public static final Location UNKNOWN = new Location("unknown",
			LocationCode.DUMMY);

	private LocationCode code;

	private String name;

	/**
	 * To support tuscany.
	 */
	public Location() {

	}

	public Location(String name, LocationCode code) {
		if (code == null)
			throw new IllegalArgumentException("location code cannot be null");
		this.code = code;
		this.name = name;
	}

	public LocationCode getCode() {
		return code;
	}

	public void setCode(LocationCode code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		final Location other = (Location) o;

		return other.code != null && other.code.equals(this.code);
	}

	@Override
	public int hashCode() {
		return code.hashCode();
	}

	@Override
	public String toString() {
		return this.code.code();
	}
}
