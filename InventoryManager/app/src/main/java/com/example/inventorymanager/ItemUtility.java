package com.example.inventorymanager;

import android.widget.EditText;


/**
 * This is a utility class that contains helpful methods for handling item-related operations and validations.
 * Various conditions are enforced on each field of an item, including:<ul>
 *     <li>The name is mandatory, must be no longer than 15 characters, and must be unique.</li>
 *     <li>The date purchased is mandatory, must follow YYYY-MM-DD format, and must be in the past.</li>
 *     <li>The description is optional and must be no longer than 20 characters.</li>
 *     <li>The make is mandatory.</li>
 *     <li>The make ismandatory.</li>
 *     <li>The serial number is optional and must be no longer than 20 characters.</li>
 *     <li>The estimated value is mandatory and must follow a monetary format (i.e., "$NNN,NNN.NN")</li>
 *     <li>The comment is optional and must be no longer than 20 characters.</li>
 * </ul>
 * @author Kareem Assaf, Isaac Joffe
 * @see Item
 * @see com.example.inventorymanager.ui.addItem.addItemFragment
 * @see com.example.inventorymanager.ui.editItem.EditItemFragment
 */
public class ItemUtility {
    /**
     * Validates the input fields for an item, ensuring that mandatory and optional fields meet specific criteria.
     *
     * @param itemNameInput       The EditText for the item name.
     * @param purchaseDateInput   The EditText for the purchase date.
     * @param descriptionInput    The EditText for the item description.
     * @param makeInput           The EditText for the item's make.
     * @param modelInput          The EditText for the item's model.
     * @param serialNumberInput   The EditText for the item's serial number.
     * @param estimatedValueInput The EditText for the item's estimated value.
     * @param commentInput        The EditText for additional comments.
     * @return True if all fields are valid; false otherwise.
     */
    public static boolean validateItemFields(EditText itemNameInput, EditText purchaseDateInput, EditText descriptionInput,
                                             EditText makeInput, EditText modelInput, EditText serialNumberInput,
                                             EditText estimatedValueInput, EditText commentInput, String oldName, ItemViewModel itemViewModel) {

        String itemName = itemNameInput.getText().toString();
        String purchaseDate = purchaseDateInput.getText().toString();
        String description = descriptionInput.getText().toString();
        String make = makeInput.getText().toString();
        String model = modelInput.getText().toString();
        String serialNumber = serialNumberInput.getText().toString();
        String estimatedValue = estimatedValueInput.getText().toString();
        String comment = commentInput.getText().toString();

        boolean isAllFieldsChecked = true;

        // ----------- MANDATORY FIELDS ------------
        // Item Name Checks
        if (itemName.isEmpty()) {
            itemNameInput.setError("This field is required");
            isAllFieldsChecked = false;
        } else if (itemName.length() >= 16) {
            itemNameInput.setError("Up to 15 characters");
            isAllFieldsChecked = false;
        } else if (itemViewModel.isIllegalNameChange(oldName, itemName)) {
            itemNameInput.setError("Item name must be unique");
            isAllFieldsChecked = false;
        }

        // Purchase Date Checks
        if (purchaseDate.isEmpty()) {
            purchaseDateInput.setError("This field is required");
            isAllFieldsChecked = false;
        }

        // Make Checks
        if (make.isEmpty()) {
            makeInput.setError("This field is required");
            isAllFieldsChecked = false;
        }else if (make.length() >= 16) {
            makeInput.setError("Up to 15 characters");
            isAllFieldsChecked = false;
        }

        // Model Checks
        if (model.isEmpty()) {
            modelInput.setError("This field is required");
            isAllFieldsChecked = false;
        }else if (model.length() >= 16) {
            modelInput.setError("Up to 15 characters");
            isAllFieldsChecked = false;
        }

        // Estimated Value Checks
        if (estimatedValue.isEmpty()) {
            estimatedValueInput.setError("This field is required");
            isAllFieldsChecked = false;
        }

        // ----------- OPTIONAL FIELDS ------------
        // Description Checks
        if (description.length() >= 21) {
            descriptionInput.setError("Up to 20 characters");
            isAllFieldsChecked = false;
        }

        // Serial Number Checks
        if (serialNumber.length() >= 21) {
            serialNumberInput.setError("Up to 20 characters");
            isAllFieldsChecked = false;
        }

        // Comment Checks
        if (comment.length() >= 21) {
            commentInput.setError("Up to 20 characters");
            isAllFieldsChecked = false;
        }

        return isAllFieldsChecked;
    }

    /**
     * Clears the text fields for an item, making them empty.
     *
     * @param itemNameInput       The EditText for the item name.
     * @param purchaseDateInput   The EditText for the purchase date.
     * @param descriptionInput    The EditText for the item description.
     * @param makeInput           The EditText for the item's make.
     * @param modelInput          The EditText for the item's model.
     * @param serialNumberInput   The EditText for the item's serial number.
     * @param estimatedValueInput The EditText for the item's estimated value.
     * @param commentInput        The EditText for additional comments.
     */
    public static void clearTextFields(EditText itemNameInput, EditText purchaseDateInput, EditText descriptionInput,
    EditText makeInput, EditText modelInput, EditText serialNumberInput, EditText estimatedValueInput, EditText commentInput){
        itemNameInput.getText().clear();
        purchaseDateInput.getText().clear();
        descriptionInput.getText().clear();
        makeInput.getText().clear();
        modelInput.getText().clear();
        serialNumberInput.getText().clear();
        estimatedValueInput.getText().clear();
        commentInput.getText().clear();
    }
}
