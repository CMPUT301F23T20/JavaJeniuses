package com.example.inventorymanager;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Locale;

public class Item implements Parcelable {
    private String itemName;
    private String purchaseDate;
    private String description;
    private String model;
    private String make;
    private String serialNumber;
    private double estimatedValue;
    private String comment;

    public Item(Parcel source) {
        itemName = source.readString();
        purchaseDate = source.readString();
        description = source.readString();
        model = source.readString();
        make = source.readString();
        serialNumber = source.readString();
        estimatedValue = source.readDouble();
        comment = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {

        parcel.writeString(itemName);
        parcel.writeString(purchaseDate);
        parcel.writeString(description);
        parcel.writeString(model);
        parcel.writeString(make);
        parcel.writeString(serialNumber);
        parcel.writeDouble(estimatedValue);
        parcel.writeString(comment);
    }

    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

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
        // format string into two decimal places to represent money
        return String.format(Locale.US, "$%.2f", this.estimatedValue);
    }

    public void setEstimatedValue(String estimatedValue) {
        // remove leading dollar sign if necessary
        if (estimatedValue.charAt(0) == '$') {
            estimatedValue = estimatedValue.substring(1);
        }
        // remove any commas that are present in the number for visual appeal
        estimatedValue = estimatedValue.replace(",", "");
        // cast back to String from double
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
