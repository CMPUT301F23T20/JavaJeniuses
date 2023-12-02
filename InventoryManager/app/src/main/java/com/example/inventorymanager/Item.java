package com.example.inventorymanager;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.text.NumberFormat;


/**
 * A representation of an item within the context of an inventory management system.
 * Stores relevant information about an item (alongside access functions), including:
 * <ul>
 *     <li>the item's name,</li>
 *     <li>a brief description of the item,</li>
 *     <li>the date the item was purchased,</li>
 *     <li>the model of the item,</li>
 *     <li>the make of the item,</li>
 *     <li>the serial number of the item,</li>
 *     <li>the estimated monetary value of the item,</li>
 *     <li>and a comment about the item.</li>
 * </ul>
 * Implements the Parcelable interface to be able to be passed between fragments, as required for filtering items.
 * @author Kareem Assaf, Isaac Joffe, David Onchuru, Sumaiya Salsabil, Tomasz Ayobahan
 * @see ItemViewModel
 */
public class Item implements Parcelable {
    private String itemName;
    private String purchaseDate;
    private String description;
    private String model;
    private String make;
    private String serialNumber;
    private double estimatedValue;
    private String comment;
    private ArrayList<Tag> tags, itemTags;
    private static final String TAG = "PrivateAddTag";
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<String> imageUrls;

    /**
     * Creates an Item() object with the fields passed in.
     * All inputs are assumed to be valid, as ensured by ItemUtility().
     * Inputs are validated inside the fragment before this constructor is called.
     * @param itemName The name of the item to be created.
     * @param purchaseDate The date that the item to be created was purchased.
     * @param description A brief description of the item to be created.
     * @param model The model of the item to be created.
     * @param make The make of the item to be created.
     * @param serialNumber The serial number of the item to be created.
     * @param estimatedValue The estimated monetary value of the item to be created.
     * @param comment A brief comment about the item to be created.
     * @param imageUrls URLs to pictures of items stored in Firebase Cloud Storage
     */
    public Item(String itemName, String purchaseDate, String description, String model, String make, String serialNumber, String estimatedValue, String comment, String tags, ArrayList<String> imageUrls) {
        this.setItemName(itemName);
        this.setPurchaseDate(purchaseDate);
        this.setDescription(description);
        this.setModel(model);
        this.setMake(make);
        this.setSerialNumber(serialNumber);
        this.setEstimatedValue(estimatedValue);
        this.setComment(comment);
        this.setTags(tags);
        // store empty array if item doesn't have urls
        if (imageUrls != null){ this.setImageUrls(imageUrls); }
        else{ this.setImageUrls(new ArrayList<>()); }
    }

    /**
     * Creates an Item() object from a database-style mapping.
     * @param mapping The HashMap to create the item from.
     */
    public Item(HashMap<String, Object> mapping) {
        this.setItemName((String) mapping.get("name"));
        this.setPurchaseDate((String) mapping.get("date"));
        this.setDescription((String) mapping.get("description"));
        this.setModel((String) mapping.get("model"));
        this.setMake((String) mapping.get("make"));
        this.setSerialNumber((String) mapping.get("number"));
        this.setEstimatedValue((String) mapping.get("value"));
        this.setComment((String) mapping.get("comment"));
        this.setTags((String) mapping.get("tags"));
    }

    /**
     * Creates an item from a parcel.
     * Required to pass full items between fragments safely.
     * @param source The parcel from which to read the data.
     */
    public Item(Parcel source) {
        // assign each field from the parcel
        itemName = source.readString();
        purchaseDate = source.readString();
        description = source.readString();
        model = source.readString();
        make = source.readString();
        serialNumber = source.readString();
        estimatedValue = source.readDouble();
        comment = source.readString();
        tags = new ArrayList<>();
        source.readTypedList(tags, Tag.CREATOR);
        imageUrls = source.createStringArrayList();
    }

    /**
     * Retrieves the name of the item.
     * @return The name of the item.
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * Changes the name of the item.
     * @param itemName The new name of the item.
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * Retrieves the date that the item was purchased.
     * @return The date the item was purchased.
     */
    public String getPurchaseDate() {
        return purchaseDate;
    }

    /**
     * Changes the date that the item was purchased.
     * @param purchaseDate The new date that the item was purchased.
     */
    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    /**
     * Retrieves a brief description of the item.
     * @return A brief description of the item.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Changes the description of the item.
     * @param description The new description of the item.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retrieves the model of the item.
     * @return The model of the item.
     */
    public String getModel() {
        return model;
    }

    /**
     * Changes the model of the item.
     * @param model The new model of the item.
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Retrieves the make of the item.
     * @return The make of the item.
     */
    public String getMake() {
        return make;
    }

    /**
     * Changes the make of the item.
     * @param make The new make of the item.
     */
    public void setMake(String make) {
        this.make = make;
    }

    /**
     * Retrieves the serial number of the item.
     * @return The serial number of the item.
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * Changes the serial number of the item.
     * @param serialNumber The new serial number of the item.
     */
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     * Retrieves the estimated monetary value of the item.
     * This method automatically converts the numerical value stored to a standard String representation.
     * @return The estimated monetary value of the item.
     */
    public String getEstimatedValue() {
        // format string into two decimal places to represent money
        return NumberFormat.getCurrencyInstance().format(this.estimatedValue);
    }

    /**
     * Changes the estimated monetary value of the item.
     * This method automatically converts the String supplied to a numerical value to be stored within.
     * @param estimatedValue The new estimated monetary value of the item.
     */
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

    /**
     * Retrieves the comment about the item.
     * @return The comment about the item.
     */
    public String getComment() {
        return comment;
    }

    /**
     * Changes the comment about the item.
     * @param comment The new comment about the item.
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Retrieves the item's tags in a non-String format (DIFFERENT FROM THE OTHERS).
     * @return The item's tags.
     */
    public ArrayList<Tag> getTags() {
        return this.tags;
    }

    /**
     * Changes the tags associated with an item.
     * @param tags The database String representing the item's tags.
     */
    public void setTags(String tags) {
        this.tags = new ArrayList<>();
        // parse String into tag objects if it exists
        if (tags != null) {
            // split by the delimiter token without any blanks at the end
            String[] individualTags = tags.split(";", 0);
            // create tag object based on th content of the individual tag string
            for (int i = 0; i < individualTags.length; i++) {
                String[] individualTag = individualTags[i].split(",");
                this.tags.add(new Tag(individualTag[0], individualTag[1]));
            }
        }
    }

    /**
     * Retrieves the images representing the item.
     * @return The images representing the item.
     */
    public ArrayList<String> getImageUrls(){
        return this.imageUrls;
    }

    /**
     * Changes the images representing the item.
     * @param imageUrls The new images representing the item.
     */
    public void setImageUrls(ArrayList<String> imageUrls){
        this.imageUrls = imageUrls;
    }

    /**
     * returns whether or not the item has any associated tags.
     * @return TRUE if it has a tag; FALSE otherwise.
     */
    public boolean hasTag(){
        return !getTags().isEmpty();
    }

    /**
     * Retrieves the first tag from the list of tags.
     * @return The first tag, or null if no tags are present.
     */
    public Tag getFirstTag() {
        if (hasTag()) {
            return getTags().get(0);
        }
        return null;
    }

    /**
     * TODO: Update tests because hashmap mapping has changed from <String, String> to <String, Object>. That's what's causing the related problems warning
     * Retrieves a dictionary representation of the original item.
     * The mapping returned is in the proper format for storage in the database.
     * Each field is represented as a String key associated with a String value.
     * @return A representation of the item that can be stored in the database.
     */
    public HashMap<String, Object> getDocument() {
        // data should be key-value mapping of String to String
        HashMap<String, Object> doc = new HashMap<>();
        // add fields one by one
        doc.put("name", this.getItemName());
        doc.put("date", this.getPurchaseDate());
        doc.put("description", this.getDescription());
        doc.put("model", this.getModel());
        doc.put("make", this.getMake());
        doc.put("number", this.getSerialNumber());
        doc.put("value", this.getEstimatedValue());
        doc.put("comment", this.getComment());
        doc.put("imageUrls", this.getImageUrls());
        // add the tags separately by parsing the internal object into a tag String
        String tagsString = "";
        for (int i = 0; i < tags.size(); i++) {
            tagsString += tags.get(i).getText() + "," + tags.get(i).getColour() + ";";
        }
        doc.put("tags", tagsString);
        return doc;
    }

    /**
     * Required to pass full items between fragments safely.
     * @return Numerical value of 0 in all cases.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Converts this item to a parcel object.
     * Required to pass full items between fragments safely.
     * @param parcel The parcel to write the data in this item to.
     * @param i Not used in this application.
     */
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
        parcel.writeList(tags);
        parcel.writeStringList(imageUrls);
    }

    /**
     * Required to pass full items between fragments safely.
     */
    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
