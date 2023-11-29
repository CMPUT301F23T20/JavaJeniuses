package com.example.inventorymanager;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
 * @author Kareem Assaf, Isaac Joffe, David Onchuru
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
     */
    public Item(String itemName, String purchaseDate, String description, String model, String make, String serialNumber, String estimatedValue, String comment) {
        this.setItemName(itemName);
        this.setPurchaseDate(purchaseDate);
        this.setDescription(description);
        this.setModel(model);
        this.setMake(make);
        this.setSerialNumber(serialNumber);
        this.setEstimatedValue(estimatedValue);
        this.setComment(comment);
        this.tags = new ArrayList<>();
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

    // Methods for handling tags
    public void addTag(Tag tag) {
//        if (!tags.contains(tag)) {
//            tags.add(tag);
//        }

        // get the user from firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        // set the tags database accessed to be for this one user
        CollectionReference privateTagsRef = db.collection("users")
                .document(user.getEmail().substring(0, user.getEmail().indexOf('@')))
                .collection("items").document(itemName).collection("tags");

        // add colour in a Hashmap to make into a field
        Map<String, Object> addTagColour = new HashMap<>();
        addTagColour.put("name", tag.getText());
        addTagColour.put("colour", tag.getColourName());

        // add tag colour as a field to the database, updates the colour of a tag that already exists
        privateTagsRef.document(tag.getText())
                .set(addTagColour, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
    }

    public ArrayList<Tag> getTags() {

        // get the user from firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        // set the tags database accessed to be for this one user
        CollectionReference privateTagsRef = db.collection("users")
                .document(user.getEmail().substring(0, user.getEmail().indexOf('@')))
                .collection("items").document(itemName).collection("tags");

//        // query database to get all items indiscriminately
        privateTagsRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // fetch the results of the blank query
                            QuerySnapshot rawData = task.getResult();
                            List<DocumentSnapshot> cleanedData = rawData.getDocuments();
                            // iterate through fetched documents and make an item for each
                            ArrayList<Tag> myTags = new ArrayList<>();
                            for (int i = 0; i < cleanedData.size(); i++) {
                                DocumentSnapshot rawTag = cleanedData.get(i);
                                // translate database format to the Item class
                                Tag cleanedTag = new Tag(
                                        rawTag.getString("name"),
                                        rawTag.getString("colour")
                                );
                                myTags.add(cleanedTag);
                            }
                            itemTags = myTags;
                        }
                    }
                });
        return itemTags;

    }

//    public void setTags(ArrayList<Tag> tags) {
//        this.tags = tags;
//    }


    /**
     * Retrieves a dictionary representation of the original item.
     * The mapping returned is in the proper format for storage in the database.
     * Each field is represented as a String key associated with a String value.
     * @return A representation of the item that can be stored in the database.
     */
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
