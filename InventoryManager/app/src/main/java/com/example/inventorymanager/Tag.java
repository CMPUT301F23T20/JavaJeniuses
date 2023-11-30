package com.example.inventorymanager;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A representation of a tag within the context of an inventory management system.
 * Stores relevant information about a tag (alongside access functions), including:
 * <ul>
 *     <li>the text of the tag,</li>
 *     <li>the colour of the tag,</li>
 *     <li>and a list of items associated with this tag.</li>
 * </ul>
 * Implements the Parcelable interface to enable passing between fragments in Android applications,
 * particularly useful for managing the associations between items and tags in the UI.
 * This class includes methods for managing the tag properties as well as adding or removing item associations.
 *
 * @author Sumaiya Salsabil, Tomasz Ayobahan
 * @see TagViewModel
 */
public class Tag implements Parcelable{

    private String text;
    private String colour; // Color can be stored in various formats, like HEX codes
    private List<Item> items; // List to store items associated with this tag

    /**
     * Constructs a new Tag with specified text and colour.
     * Initializes an empty list of items.
     * @param text The text label of the tag.
     * @param colour The colour of the tag.
     */
    public Tag(String text, String colour) {
        this.text = text;
        this.colour = colour;
        this.items = new ArrayList<>(); // Initialize the list of items
    }

    /**
     * Constructs a Tag from a Parcel, typically used for Android inter-fragment communication.
     * @param in The Parcel containing the Tag's data.
     */
    protected Tag(Parcel in) {
        text = in.readString();
        colour = in.readString();
    }

    // Getters and setters
    /**
     * Returns the text label of the tag.
     * @return The text label of this tag.
     */
    public String getText() {
        return text;
    }

    /**
     * Sets a new text label for the tag.
     * @param text The new text label for the tag.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Returns the colour name of the tag.
     * @return The colour name of this tag.
     */

    /**
     * Converts the colour name to its corresponding HEX code.
     * @return The HEX code representation of the tag's colour.
     */
    public String getColourName() { return colour; }

    public String getColour() {

        if (colour.equals("blue")) {return "#A8E4EF"; }
        if (colour.equals("green")) {return "#A6ECA8"; }
        if (colour.equals("yellow")) {return "#FCFC99"; }
        else {return "#FF8986"; }

    }

    /**
     * Returns a list of items associated with this tag.
     * @return A list of items linked to this tag.
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * Sets a new list of items to be associated with this tag.
     * @param items The list of items to associate with the tag.
     */
    public void setItems(List<Item> items) {
        this.items = items;
    }

    /**
     * Removes a specific item from the list of items associated with this tag.
     * @param item The item to be removed.
     */
    public void removeItem(Item item) {
        items.remove(item);
    }

    // Parcelable interface methods
    /**
     * Describes the kinds of special objects contained in this Parcelable's marshalled representation.
     * @return A bitmask indicating the set of special object types marshalled by this Parcelable object instance.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object into a Parcel.
     * @param dest The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     */
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeString(colour);
    }


    /**
     * A public static CREATOR field that generates instances of your Parcelable class from a Parcel.
     */
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

    /**
     * Returns a string representation of the Tag object.
     * @return The text label of the tag.
     */
    @NonNull
    @Override
    public String toString() {
        return this.getText();
    }
}
