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

import java.util.ArrayList;
import java.util.List;

import org.oasisopen.sca.annotation.Property;
import org.oasisopen.sca.annotation.Reference;
import org.oasisopen.sca.annotation.Scope;
import org.oasisopen.sca.annotation.Service;

@Service(Catalog.class)
@Scope("COMPOSITE")
public class CatalogAggregatorImpl implements Catalog {

    @Property
    public String currencyCode = "USD";

    @Reference
    public CurrencyConverter currencyConverter;

    @Reference(required = false)
    public Catalog fruitsCatalog;

    @Reference(required = false)
    public Catalog vegetablesCatalog;

    public Item[] items() {
        String currencySymbol = currencyConverter.getCurrencySymbol(currencyCode);

        List<Item> catalog = new ArrayList<Item>();
        if (fruitsCatalog != null) {
            Item[] fruits = fruitsCatalog.items();

            for (Item item : fruits) {
                double price = item.getPrice();
                price = currencyConverter.getConversion("USD", currencyCode, price);
                catalog.add(new Item(item.getName(), currencyCode, currencySymbol, price));
            }
        }

        if (vegetablesCatalog != null) {
            Item[] vegetables = vegetablesCatalog.items();

            for (Item item : vegetables) {
                double price = item.getPrice();
                price = currencyConverter.getConversion("USD", currencyCode, price);
                catalog.add(new Item(item.getName(), currencyCode, currencySymbol, price));
            }
        }

        Item[] catalogArray = new Item[catalog.size()];
        catalog.toArray(catalogArray);

        return catalogArray;
    }

}
