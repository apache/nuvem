package services;

import java.util.ArrayList;
import java.util.List;

import org.oasisopen.sca.annotation.Init;
import org.oasisopen.sca.annotation.Property;
import org.oasisopen.sca.annotation.Reference;
import org.oasisopen.sca.annotation.Scope;
import org.oasisopen.sca.annotation.Service;

@Service(Catalog.class)
@Scope("COMPOSITE")
public class FruitsCatalogImpl implements Catalog {
    @Property
    public String currencyCode = "USD";
    
    @Reference
    public CurrencyConverter currencyConverter;

    private List<Item> catalog = new ArrayList<Item>();

    @Init
    public void init() {
        String currencySymbol = currencyConverter.getCurrencySymbol(currencyCode);
        catalog.add(new Item("Acai", currencyCode, currencySymbol, currencyConverter.getConversion("USD", currencyCode, 2.99)));
        catalog.add(new Item("Carambola", currencyCode, currencySymbol, currencyConverter.getConversion("USD", currencyCode, 3.55)));
        catalog.add(new Item("Cashew", currencyCode, currencySymbol, currencyConverter.getConversion("USD", currencyCode, 1.55)));
    }

    public Item[] items() {
        Item[] catalogArray = new Item[catalog.size()];
        catalog.toArray(catalogArray);
        return catalogArray;
    }
}
