package com.example.inventorymanager;

import android.widget.Button;
import android.widget.EditText;

public class ItemUtility {
    public static boolean validateExpenseFields(EditText itemNameInput, EditText purchaseDateInput, EditText descriptionInput,
                                                EditText makeInput, EditText modelInput,EditText serialNumberInput, EditText estimatedValueInput, EditText commentInput) {

        String itemName = itemNameInput.getText().toString();
        String purchaseDate = purchaseDateInput.getText().toString();
        String description = descriptionInput.getText().toString();
        String make = makeInput.getText().toString();
        String model = modelInput.getText().toString();
        String serialNumber = serialNumberInput.getText().toString();
        String estimatedValue = estimatedValueInput.getText().toString();
        String comment = commentInput.getText().toString();

        boolean isAllFieldsChecked = true;
        // Mandatory Fields: Name, Date, Make, Model, Value
        // Optional Fields: Description, Serial number, comment

        // ITEM NAME CHECKS
        if (itemName.isEmpty()) {
            itemNameInput.setError("This field is required");
            isAllFieldsChecked = false;

        } else if (itemName.length() >= 16) {
            itemNameInput.setError("Up to 15 characters");
            isAllFieldsChecked = false;
        }

        // PURCHASE DATE CHECKS
        if (purchaseDate.isEmpty()) {
            purchaseDateInput.setError("This field is required");
            isAllFieldsChecked = false;
        }

        // DESCRIPTION CHECKS
        if (description.isEmpty()) {
            descriptionInput.setError("This field is required");
            isAllFieldsChecked = false;
        }

        if (comment.length() >= 21) {
            commentInput.setError("Up to 20 characters");
            isAllFieldsChecked = false;
        }

        return isAllFieldsChecked;
    }

    public static void clearTextFields(EditText expenseInput,EditText dateInput,EditText chargeInput,EditText commentInput ){
        expenseInput.getText().clear();
        dateInput.getText().clear();
        chargeInput.getText().clear();
        commentInput.getText().clear();
    }

}
