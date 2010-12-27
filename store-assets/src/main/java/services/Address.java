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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Address {

	public static final Address DUMMY_ADDRESS = new Address("245",
			Location.UNKNOWN, Location.UNKNOWN, Location.UNKNOWN);

	private String door;

	private Location street;

	private Location city;

	private Location country;

	/**
	 * To support tuscany.
	 */
	public Address() {
		
	}

	public Address(String door, Location street, Location city, Location country) {
		this.door = door;
		this.street = street;
		this.city = city;
		this.country = country;
	}

	public String getDoor() {
		return door;
	}

	public void setDoor(String door) {
		this.door = door;
	}

	public Location getStreet() {
		return street;
	}

	public void setStreet(Location street) {
		this.street = street;
	}

	public Location getCity() {
		return city;
	}

	public void setCity(Location city) {
		this.city = city;
	}

	public Location getCountry() {
		return country;
	}

	public void setCountry(Location country) {
		this.country = country;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		final Address rhs = (Address) o;

		return new EqualsBuilder().append(this.door, rhs.door).append(
				this.city, rhs.city).append(this.country, rhs.country)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(door).append(city).append(country)
				.toHashCode();
	}
}
