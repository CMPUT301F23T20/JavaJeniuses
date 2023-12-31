package com.example.inventorymanager;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Displays the items being tracked by the inventory manager to the user.
 * In each list entry, information about the item is displayed, including:
 * <ul>
 *     <li>the item's name,</li>
 *     <li>a brief description of the item,</li>
 *     <li>estimated monetary value of the item,</li>
 *     <li>the date that the item was purchased,</li>
 *     <li>an indicator of whether this item is selected, and</li>
 *     <li>a scrollable list of the tags associated with an item.</li>
 * </ul>
 * @author Kareem Assaf, Isaac Joffe, Sumaiya Salsabil
 * @see com.example.inventorymanager.ui.home.HomeFragment
 */
public class ItemAdapter extends ArrayAdapter{
    private final Context context;
    private final ArrayList<Item> items;
    private HashMap<String, Boolean> isChecked;

    /**
     * Creates an adapter to display the list of items.
     * @param context The context in which to display the items.
     * @param value The UI element to bind the adapter to.
     * @param items The items to be displayed.
     */
    public ItemAdapter(Context context, int value, ArrayList<Item> items) {
        super(context, value, items);
        this.context = context;
        this.items = items;
        this.createEmptyIsChecked();
    }

    /**
     * Produces a view showing information about each item to be displayed.
     * @param position The index of the item to be displayed within the array of items.
     * @param convertView The view associated with this adapter.
     * @param parent The view group associated with this adapter.
     * @return A view representing basic information about the item.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // create View to display the item
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_cell, parent, false);
        }

        // get the item to be displayed
        Item item = items.get(position);

        // bind UI elements to variables
        TextView itemName = view.findViewById(R.id.itemNameTextView);
        TextView description = view.findViewById(R.id.descriptionTextView);
        TextView estimateValue = view.findViewById(R.id.estimateValueTextView);
        TextView purchaseDate = view.findViewById(R.id.purchaseDateTextView);
        LinearLayout tagList = view.findViewById(R.id.tagList);
        CheckBox checkBox = view.findViewById(R.id.checkBox);

        // apply algorithm to ensure item description is not too long
        String descriptionText = item.getDescription();
        if (descriptionText.length() > 25) {
            descriptionText = descriptionText.substring(0, 20).strip() + "...";
        }

        // display the most relevant fields for each item
        itemName.setText(item.getItemName());
        description.setText(descriptionText);
        estimateValue.setText(item.getEstimatedValue());
        purchaseDate.setText(item.getPurchaseDate());
        checkBox.setChecked(isChecked.get(item.getItemName()));

        // add effect of clicking on checkbox (toggling whether the item is selected)
        checkBox.setOnClickListener(v -> {
            // toggle the state of the check box
            // use XOR with logical "1" to do this -- if "1" then now "0", and if "0" then now "1"
            isChecked.put(item.getItemName(), Boolean.logicalXor(Boolean.TRUE, isChecked.get(item.getItemName())));
        });

        // programmatically generate the tags to be displayed
        ArrayList<Tag> tags = item.getTagsArray();
        if (tags != null) {
            // delete the previous tags shown
            tagList.removeAllViews();
            for (int i = 0; i < tags.size(); i++) {
                // generate a new text view for each tag
                TextView tagTextView = new TextView(getContext());
                tagTextView.setTextSize(15);
                // set this text view to display the text with spaces for padding, and the proper colour
                SpannableString tagName = new SpannableString(" " + tags.get(i).getText() + " ");
                tagName.setSpan(new BackgroundColorSpan(Color.parseColor(tags.get(i).getColourCode())), 0, tagName.length(), 0);
                tagTextView.setText(tagName);
                tagTextView.setPadding(0, 10, 0, 10);
                // add text view to the scrollable interface
                tagList.addView(tagTextView);
            }
        }

        return view;
    }

    /**
     * Slightly alters the behavior of the built-in data updating call.
     * Still properly updates the data displayed in the list, but now resets check boxes too.
     */
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        createEmptyIsChecked();
    }

    /**
     * Retrieves whether or not the checkbox of a particular item is selected.
     * @param key The name of the item to check.
     * @return TRUE if the item is selected; FALSE otherwise.
     */
    public Boolean getIsChecked(String key) {
        return this.isChecked.get(key);
    }

    /**
     * Refreshes the checkbox tracker so that no items are actively selected.
     */
    private void createEmptyIsChecked() {
        // reset to a new, empty mapping because certain items may no longer exist
        this.isChecked = new HashMap<>();
        // uncheck each checkbox for all the items that do exist now
        for (int i = 0; i < this.items.size(); i++) {
            this.isChecked.put(items.get(i).getItemName(), Boolean.FALSE);
        }
    }
}
