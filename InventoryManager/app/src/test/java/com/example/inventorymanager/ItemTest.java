package com.example.inventorymanager;

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
        assertTrue(item.getItemName().equals(itemName1));
        // ensure that setter works on new value based on prior confirmed assumption that getter works
        item.setItemName(itemName2);
        assertTrue(item.getItemName().equals(itemName2));
    }

    /**
     * Tests that the purchaseDate field of Item has proper getters and setters.
     */
    @Test
    public void testPurchaseDate() {
        // ensure that getter works properly on original data
        Item item = defaultItem();
        assertTrue(item.getPurchaseDate().equals(purchaseDate1));
        // ensure that setter works on new value based on prior confirmed assumption that getter works
        item.setPurchaseDate(purchaseDate2);
        assertTrue(item.getPurchaseDate().equals(purchaseDate2));
    }

    /**
     * Tests that the description field of Item has proper getters and setters.
     */
    @Test
    public void testDescription() {
        // ensure that getter works properly on original data
        Item item = defaultItem();
        assertTrue(item.getDescription().equals(description1));
        // ensure that setter works on new value based on prior confirmed assumption that getter works
        item.setDescription(description2);
        assertTrue(item.getDescription().equals(description2));
    }

    /**
     * Tests that the model field of Item has proper getters and setters.
     */
    @Test
    public void testModel() {
        // ensure that getter works properly on original data
        Item item = defaultItem();
        assertTrue(item.getModel().equals(model1));
        // ensure that setter works on new value based on prior confirmed assumption that getter works
        item.setModel(model2);
        assertTrue(item.getModel().equals(model2));
    }

    /**
     * Tests that the make field of Item has proper getters and setters.
     */
    @Test
    public void testMake() {
        // ensure that getter works properly on original data
        Item item = defaultItem();
        assertTrue(item.getMake().equals(make1));
        // ensure that setter works on new value based on prior confirmed assumption that getter works
        item.setMake(make2);
        assertTrue(item.getMake().equals(make2));
    }

    /**
     * Tests that the serialNumber field of Item has proper getters and setters.
     */
    @Test
    public void testSerialNumber() {
        // ensure that getter works properly on original data
        Item item = defaultItem();
        assertTrue(item.getSerialNumber().equals(serialNumber1));
        // ensure that setter works on new value based on prior confirmed assumption that getter works
        item.setSerialNumber(serialNumber2);
        assertTrue(item.getSerialNumber().equals(serialNumber2));
    }

    /**
     * Tests that the estimatedValue field of Item has proper getters and setters.
     * This is the most crucial test since the estimated value formatting can be difficult.
     */
    @Test
    public void testEstimatedValue() {
        // ensure that getter works properly on original data
        Item item = defaultItem();
        assertTrue(item.getEstimatedValue().equals(estimatedValue1));
        // ensure that setter works on new value based on prior confirmed assumption that getter works
        item.setEstimatedValue(estimatedValue2);
        assertTrue(item.getEstimatedValue().equals(estimatedValue2));

        // test additional extraneous cases of monetary input
        // these cases should never occur based on ItemUtility(), but worth testing just in case
        item.setEstimatedValue("0");
        assertTrue(item.getEstimatedValue().equals("$0.00"));
        item.setEstimatedValue("100");
        assertTrue(item.getEstimatedValue().equals("$100.00"));
        item.setEstimatedValue("10000");
        assertTrue(item.getEstimatedValue().equals("$10,000.00"));
        item.setEstimatedValue("1000000");
        assertTrue(item.getEstimatedValue().equals("$1,000,000.00"));
        item.setEstimatedValue("0.1");
        assertTrue(item.getEstimatedValue().equals("$0.10"));
        item.setEstimatedValue("099.990");
        assertTrue(item.getEstimatedValue().equals("$99.99"));
        item.setEstimatedValue("0.99999");
        assertTrue(item.getEstimatedValue().equals("$1.00"));
    }

    /**
     * Tests that the comment field of Item has proper getters and setters.
     */
    @Test
    public void testComment() {
        // ensure that getter works properly on original data
        Item item = defaultItem();
        assertTrue(item.getComment().equals(comment1));
        // ensure that setter works on new value based on prior confirmed assumption that getter works
        item.setComment(comment2);
        assertTrue(item.getComment().equals(comment2));
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
        assertTrue(item.getItemName().equals(document.get("name")));
        assertTrue(item.getPurchaseDate().equals(document.get("date")));
        assertTrue(item.getDescription().equals(document.get("description")));
        assertTrue(item.getModel().equals(document.get("model")));
        assertTrue(item.getMake().equals(document.get("make")));
        assertTrue(item.getSerialNumber().equals(document.get("number")));
        assertTrue(item.getEstimatedValue().equals(document.get("value")));
        assertTrue(item.getComment().equals(document.get("comment")));

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
        assertTrue(item.getItemName().equals(document.get("name")));
        assertTrue(item.getPurchaseDate().equals(document.get("date")));
        assertTrue(item.getDescription().equals(document.get("description")));
        assertTrue(item.getModel().equals(document.get("model")));
        assertTrue(item.getMake().equals(document.get("make")));
        assertTrue(item.getSerialNumber().equals(document.get("number")));
        assertTrue(item.getEstimatedValue().equals(document.get("value")));
        assertTrue(item.getComment().equals(document.get("comment")));
    }
}
