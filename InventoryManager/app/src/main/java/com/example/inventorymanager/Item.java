package com.example.inventorymanager;

import android.util.Log;

import java.util.HashMap;
import java.util.Locale;

public class Item {
    private String itemName;
    private String purchaseDate;
    private String description;
    private String model;
    private String make;
    private String serialNumber;
    private double estimatedValue;
    private String comment;


    public Item(String itemName, String purchaseDate, String description, String model, String make, String serialNumber, String estimatedValue, String comment) {
        this.setItemName(itemName);
        this.setPurchaseDate(purchaseDate);
        this.setDescription(description);
        this.setModel(model);
        this.setMake(make);
        this.setSerialNumber(serialNumber);
        this.setEstimatedValue(estimatedValue);
        this.setComment(comment);
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

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getEstimatedValue() {
        return String.format(Locale.US, "$%.2f", this.estimatedValue);
    }

    public void setEstimatedValue(String estimatedValue) {
        Log.d("test", estimatedValue);
        Log.d("test", Double.toString(Double.valueOf(estimatedValue.substring(1))));
        if (estimatedValue.charAt(0) == '$') {
            estimatedValue = estimatedValue.substring(1);
        }
        this.estimatedValue = Double.valueOf(estimatedValue);
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
        doc.put("number", this.getSerialNumber());
        doc.put("value", this.getEstimatedValue());
        doc.put("comment", this.getComment());
        return doc;
    }
}
