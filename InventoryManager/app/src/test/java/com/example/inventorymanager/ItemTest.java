package com.example.inventorymanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.HashMap;

/**
 * Tests the functionality of the Item() class.
 * Ensures that all getters and setters work properly and that the item can be properly transformed into a database document.
 * @author Isaac Joffe
 * @see Item
 */
public class ItemTest {
    // defines realistic test data to be used for two different items
    private final String itemName1 = "Computer";
    private final String itemName2 = "Laptop";
    private final String purchaseDate1 = "2023-01-01";
    private final String purchaseDate2 = "2023-08-31";
    private final String description1 = "PC used for gaming.";
    private final String description2 = "Laptop used for school.";
    private final String model1 = "iMac";
    private final String model2 = "Surface Pro";
    private final String make1 = "Apple";
    private final String make2 = "Microsoft";
    private final String serialNumber1 = "1234567890ABCD";
    private final String serialNumber2 = "DCBA0987654321";
    private final String estimatedValue1 = "$2,500.00";
    private final String estimatedValue2 = "$1,399.99";
    private final String comment1 = "Uses high-speed M1 chip.";
    private final String comment2 = "Uses medium-speed Intel chip.";

    /**
     * Creates an item with default fields to be used by the other tests.
     * @return The main item to be used by other tests.
     */
    private Item defaultItem() {
        return new Item(itemName1, purchaseDate1, description1, model1, make1, serialNumber1, estimatedValue1, comment1);
    }

    /**
     * Tests that the itemName field of Item has proper getters and setters.
     */
    @Test
    public void testItemName() {
        // ensure that getter works properly on original data
        Item item = defaultItem();
        assertEquals(itemName1, item.getItemName());
        // ensure that setter works on new value based on prior confirmed assumption that getter works
        item.setItemName(itemName2);
        assertEquals(itemName2, item.getItemName());
    }

    /**
     * Tests that the purchaseDate field of Item has proper getters and setters.
     */
    @Test
    public void testPurchaseDate() {
        // ensure that getter works properly on original data
        Item item = defaultItem();
        assertEquals(purchaseDate1, item.getPurchaseDate());
        // ensure that setter works on new value based on prior confirmed assumption that getter works
        item.setPurchaseDate(purchaseDate2);
        assertEquals(purchaseDate2, item.getPurchaseDate());
    }

    /**
     * Tests that the description field of Item has proper getters and setters.
     */
    @Test
    public void testDescription() {
        // ensure that getter works properly on original data
        Item item = defaultItem();
        assertEquals(description1, item.getDescription());
        // ensure that setter works on new value based on prior confirmed assumption that getter works
        item.setDescription(description2);
        assertEquals(description2, item.getDescription());
    }

    /**
     * Tests that the model field of Item has proper getters and setters.
     */
    @Test
    public void testModel() {
        // ensure that getter works properly on original data
        Item item = defaultItem();
        assertEquals(model1, item.getModel());
        // ensure that setter works on new value based on prior confirmed assumption that getter works
        item.setModel(model2);
        assertEquals(model2, item.getModel());
    }

    /**
     * Tests that the make field of Item has proper getters and setters.
     */
    @Test
    public void testMake() {
        // ensure that getter works properly on original data
        Item item = defaultItem();
        assertEquals(make1, item.getMake());
        // ensure that setter works on new value based on prior confirmed assumption that getter works
        item.setMake(make2);
        assertEquals(make2, item.getMake());
    }

    /**
     * Tests that the serialNumber field of Item has proper getters and setters.
     */
    @Test
    public void testSerialNumber() {
        // ensure that getter works properly on original data
        Item item = defaultItem();
        assertEquals(serialNumber1, item.getSerialNumber());
        // ensure that setter works on new value based on prior confirmed assumption that getter works
        item.setSerialNumber(serialNumber2);
        assertEquals(serialNumber2, item.getSerialNumber());
    }

    /**
     * Tests that the estimatedValue field of Item has proper getters and setters.
     * This is the most crucial test since the estimated value formatting can be difficult.
     */
    @Test
    public void testEstimatedValue() {
        // ensure that getter works properly on original data
        Item item = defaultItem();
        assertEquals(estimatedValue1, item.getEstimatedValue());
        // ensure that setter works on new value based on prior confirmed assumption that getter works
        item.setEstimatedValue(estimatedValue2);
        assertEquals(estimatedValue2, item.getEstimatedValue());

        // test additional extraneous cases of monetary input
        // these cases should never occur based on ItemUtility(), but worth testing just in case
        item.setEstimatedValue("0");
        assertEquals("$0.00", item.getEstimatedValue());
        item.setEstimatedValue("100");
        assertEquals("$100.00", item.getEstimatedValue());
        item.setEstimatedValue("10000");
        assertEquals("$10,000.00", item.getEstimatedValue());
        item.setEstimatedValue("1000000");
        assertEquals("$1,000,000.00", item.getEstimatedValue());
        item.setEstimatedValue("0.1");
        assertEquals("$0.10", item.getEstimatedValue());
        item.setEstimatedValue("099.990");
        assertEquals("$99.99", item.getEstimatedValue());
        item.setEstimatedValue("0.99999");
        assertEquals("$1.00", item.getEstimatedValue());
    }

    /**
     * Tests that the comment field of Item has proper getters and setters.
     */
    @Test
    public void testComment() {
        // ensure that getter works properly on original data
        Item item = defaultItem();
        assertEquals(comment1, item.getComment());
        // ensure that setter works on new value based on prior confirmed assumption that getter works
        item.setComment(comment2);
        assertEquals(comment2, item.getComment());
    }

    /**
     * Tests that the document translation feature of an Item works properly.
     * This is crucial to ensuring that the database can be used safely.
     */
    @Test
    public void testGetDocument() {
        // ensure that the document is accurate to original data
        Item item = defaultItem();
        HashMap<String, String> document = item.getDocument();
        assertEquals(item.getItemName(), document.get("name"));
        assertEquals(item.getPurchaseDate(), document.get("date"));
        assertEquals(item.getDescription(), document.get("description"));
        assertEquals(item.getModel(), document.get("model"));
        assertEquals(item.getMake(), document.get("make"));
        assertEquals(item.getSerialNumber(), document.get("number"));
        assertEquals(item.getEstimatedValue(), document.get("value"));
        assertEquals(item.getComment(), document.get("comment"));

        // ensure that the document is accurate to data changes
        item.setItemName(itemName2);
        item.setPurchaseDate(purchaseDate2);
        item.setDescription(description2);
        item.setModel(model2);
        item.setMake(make2);
        item.setSerialNumber(serialNumber2);
        item.setEstimatedValue(estimatedValue2);
        item.setComment(comment2);
        document = item.getDocument();
        assertEquals(item.getItemName(), document.get("name"));
        assertEquals(item.getPurchaseDate(), document.get("date"));
        assertEquals(item.getDescription(), document.get("description"));
        assertEquals(item.getModel(), document.get("model"));
        assertEquals(item.getMake(), document.get("make"));
        assertEquals(item.getSerialNumber(), document.get("number"));
        assertEquals(item.getEstimatedValue(), document.get("value"));
        assertEquals(item.getComment(), document.get("comment"));
    }
}
