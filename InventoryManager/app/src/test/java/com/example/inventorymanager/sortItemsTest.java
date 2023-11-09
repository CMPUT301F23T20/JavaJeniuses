package com.example.inventorymanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.example.inventorymanager.ui.sort.SortOptionsFragment;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Tests the functionality of the sorting functions in SortOptionsFragment
 * Ensures the items are properly sorted on basis of the user's input
 * @author Sumaiya Salsabil
 */
public class sortItemsTest {
    // defines realistic test data to be used for two different items
    private final String itemName1 = "Computer";
    private final String itemName2 = "Laptop";

    private final String purchaseDate1 = "2023-01-01";
    private final String purchaseDate2 = "2023-08-20";

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

    private Item item1, item2;
    SortOptionsFragment sortOptionsFragment = new SortOptionsFragment();

    /**
     * Prepopulates a list of items that will be used to test how the filtering functions
     * @return a list of default items
     */
    private ArrayList<Item> defaultList() {
        ArrayList<Item> items = new ArrayList<Item>();

        item1 = new Item(itemName1, purchaseDate1, description1, model1, make1, serialNumber1, estimatedValue1, comment1);
        item2 = new Item(itemName2, purchaseDate2, description2, model2, make2, serialNumber2, estimatedValue2, comment2);

        items.add(item1);
        items.add(item2);

        return items;
    }

    /**
     * Tests that the sort by date feature works as expected
     */
    @Test
    public void testSortByDate() {
        ArrayList<Item> items = defaultList();

        // test if sorted in ascending order
        Comparator<Item> comparator = SortOptionsFragment.createComparator("Date", true);
        items.sort(comparator);
        ArrayList<Item> sortedItems = new ArrayList<Item>();
        sortedItems.add(item1);
        sortedItems.add(item2);

        assertEquals(sortedItems,items);

        // test if sorted in descending order
        comparator = SortOptionsFragment.createComparator("Date", false);
        items.sort(comparator);
        sortedItems.clear();
        sortedItems.add(item2);
        sortedItems.add(item1);

        assertEquals(sortedItems,items);

    }

    /**
     * Tests that the sort by description feature works as expected
     */
    @Test
    public void testSortByDescription() {
        ArrayList<Item> items = defaultList();

        // test if sorted in ascending order
        Comparator<Item> comparator = SortOptionsFragment.createComparator("Description", true);
        items.sort(comparator);
        ArrayList<Item> sortedItems = new ArrayList<Item>();
        sortedItems.add(item2);
        sortedItems.add(item1);

        assertEquals(sortedItems,items);

        // test if sorted in descending order
        comparator = SortOptionsFragment.createComparator("Description", false);
        items.sort(comparator);
        sortedItems.clear();
        sortedItems.add(item1);
        sortedItems.add(item2);

        assertEquals(sortedItems,items);

    }

    /**
     * Tests that the sort by make feature works as expected
     */
    @Test
    public void testSortByMake() {
        ArrayList<Item> items = defaultList();

        // test if sorted in ascending order
        Comparator<Item> comparator = SortOptionsFragment.createComparator("Make", true);
        items.sort(comparator);
        ArrayList<Item> sortedItems = new ArrayList<Item>();
        sortedItems.add(item1);
        sortedItems.add(item2);

        assertEquals(sortedItems,items);

        // test if sorted in descending order
        comparator = SortOptionsFragment.createComparator("Make", false);
        items.sort(comparator);
        sortedItems.clear();
        sortedItems.add(item2);
        sortedItems.add(item1);

        assertEquals(sortedItems,items);

    }

    /**
     * Tests that the sort by value feature works as expected
     */
    @Test
    public void testSortByValue() {
        ArrayList<Item> items = defaultList();

        // test if sorted in ascending order
        Comparator<Item> comparator = SortOptionsFragment.createComparator("Value", true);
        items.sort(comparator);
        ArrayList<Item> sortedItems = new ArrayList<Item>();
        sortedItems.add(item2);
        sortedItems.add(item1);

        assertEquals(sortedItems,items);

        // test if sorted in descending order
        comparator = SortOptionsFragment.createComparator("Value", false);
        items.sort(comparator);
        sortedItems.clear();
        sortedItems.add(item1);
        sortedItems.add(item2);

        assertEquals(sortedItems,items);

    }
}

