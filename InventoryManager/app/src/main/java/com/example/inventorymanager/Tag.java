package com.example.inventorymanager;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Tag implements Parcelable{

    private String text;
    private String colour; // Color can be stored in various formats, like HEX codes
    private List<Item> items; // List to store items associated with this tag

    // Constructor
    public Tag(String text, String colour) {
        this.text = text;
        this.colour = colour;
        this.items = new ArrayList<>(); // Initialize the list of items
    }

    protected Tag(Parcel in) {
        text = in.readString();
        colour = in.readString();
    }

    // Getters and setters
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getColourName() { return colour; }

    public String getColour() {

        if (colour.equals("blue")) {return "#A8E4EF"; }
        if (colour.equals("green")) {return "#A6ECA8"; }
        if (colour.equals("yellow")) {return "#FCFC99"; }
        else {return "#FF8986"; }

    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    // Method to remove an item from the tag
    public void removeItem(Item item) {
        items.remove(item);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeString(colour);
    }

    public static final Parcelable.Creator<Tag> CREATOR = new Parcelable.Creator<Tag>() {
        @Override
        public Tag createFromParcel(Parcel in) {
            return new Tag(in);
        }

        @Override
        public Tag[] newArray(int size) {
            return new Tag[size];
        }
    };

    @NonNull
    @Override
    public String toString() {
        return this.getText();
    }
}
