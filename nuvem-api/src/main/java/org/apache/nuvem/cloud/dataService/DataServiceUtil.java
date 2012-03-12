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
package org.apache.nuvem.cloud.dataService;

public class DataServiceUtil {

    public static String createKey(String kind, String id) {
        return kind.concat("(").concat(id).concat(")");
    }

    public static String getKind(String key) {
        if (key != null) {
            return key.substring(0, key.indexOf("("));
        }
        return null;
    }

    public static String getId(String key) {
        if (key != null) {
            return key.substring(key.indexOf("(") + 1, key.indexOf(")"));
        }
        return null;
    }

}
