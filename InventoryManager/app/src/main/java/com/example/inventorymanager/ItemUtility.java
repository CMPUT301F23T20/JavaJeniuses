package com.example.inventorymanager;

import android.widget.EditText;

public class ItemUtility {
    public static boolean validateItemFields(EditText itemNameInput, EditText purchaseDateInput, EditText descriptionInput,
                                             EditText makeInput, EditText modelInput, EditText serialNumberInput, EditText estimatedValueInput, EditText commentInput) {

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
        }

        // Model Checks
        if (model.isEmpty()) {
            modelInput.setError("This field is required");
            isAllFieldsChecked = false;
        }

        // Estimated Value Checks
        if (estimatedValue.isEmpty()) {
            estimatedValueInput.setError("This field is required");
            isAllFieldsChecked = false;
        } else {
            try {
                // Implemented with the help of: https://www.geeksforgeeks.org/double-parsedouble-method-in-java-with-examples/
                // Article by: gopaldave, GeeksForGeeks. Last updated: 26 Oct, 2018
                double estimatedValueNumeric = Double.parseDouble(estimatedValue);
                if (estimatedValueNumeric < 0) {
                    estimatedValueInput.setError("Value must be non-negative");
                    isAllFieldsChecked = false;
                }
            } catch (NumberFormatException e) {
                estimatedValueInput.setError("Invalid number format");
                isAllFieldsChecked = false;
            }
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
