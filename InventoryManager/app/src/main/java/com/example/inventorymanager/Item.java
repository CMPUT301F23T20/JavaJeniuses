package com.example.inventorymanager;

import java.util.HashMap;

public class Item {
    private String itemName;
    private String purchaseDate;
    private String description;
    private String model;
    private String make;
    private double serialNumber;
    private double estimateValue;
    private String comment;


    public Item(String itemName, String purchaseDate, String description, String model, String make, double serialNumber, double estimateValue, String comment) {
        this.itemName = itemName;
        this.purchaseDate = purchaseDate;
        this.description = description;
        this.model = model;
        this.make = make;
        this.serialNumber = serialNumber;
        this.estimateValue = estimateValue;
        this.comment = comment;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public double getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(double serialNumber) {
        this.serialNumber = serialNumber;
    }

    public double getEstimateValue() {
        return estimateValue;
    }

    public void setEstimateValue(double estimateValue) {
        this.estimateValue = estimateValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    // converts the original item into the proper format for the database
    public HashMap<String, String> getDocument() {
        // data should be key-value mapping of String to String
        HashMap<String, String> doc = new HashMap<>();
        // add fields one by one
        doc.put("name", this.getItemName());
        doc.put("date", this.getPurchaseDate());
        doc.put("description", this.getDescription());
        doc.put("model", this.getModel());
        doc.put("make", this.getMake());
        doc.put("number", Double.toString(this.getSerialNumber()));
        doc.put("value", Double.toString(this.getEstimateValue()));
        doc.put("comment", this.getComment());
        return doc;
    }
}
