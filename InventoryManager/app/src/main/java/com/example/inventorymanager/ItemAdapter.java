package com.example.inventorymanager;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import org.w3c.dom.Text;

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
 *     <li>and an indicator of whether this item is selected.</li>
 * </ul>
 * @author Kareem Assaf, Isaac Joffe
 * @see com.example.inventorymanager.ui.home.HomeFragment
 */
public class ItemAdapter extends ArrayAdapter{
    private final Context context;
    private ArrayList<Item> items;
    private ArrayList<Tag> tags;
    private Tag tag;
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
        TextView itemTag = view.findViewById(R.id.itemTag);
        CheckBox checkBox = view.findViewById(R.id.checkBox);

        // display the most relevant fields for each item
        itemName.setText(item.getItemName());
        description.setText(item.getDescription());
        estimateValue.setText(item.getEstimatedValue());
        purchaseDate.setText(item.getPurchaseDate());
        checkBox.setChecked(isChecked.get(item.getItemName()));

        // add effect of clicking on checkbox (toggling whether the item is selected)
        checkBox.setOnClickListener(v -> {
            // toggle the state of the check box
            // use XOR with logical "1" to do this -- if "1" then now "0", and if "0" then now "1"
            isChecked.put(item.getItemName(), Boolean.logicalXor(Boolean.TRUE, isChecked.get(item.getItemName())));
        });

        tags = item.getTags();
        if (tags != null && !tags.isEmpty()) {
            tag = tags.get(0);
            String text = tag.getText();
            String colour = tag.getColour();
            itemTag.setText(text);
            int colourInt = Color.parseColor(colour);
            itemTag.setBackgroundColor(colourInt);
            itemTag.setVisibility(View.VISIBLE);
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
