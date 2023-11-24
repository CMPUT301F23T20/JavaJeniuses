package com.example.inventorymanager;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Displays the items being tracked by the inventory manager to the user.
 * In each list entry, information about the item is displayed, including:
 * <ul>
 *     <li>the item's name,</li>
 *     <li>a brief description of the item,</li>
 *     <li>estimated monetary value of the item,</li>
 *     <li>and the date that the item was purchased.</li>
 * </ul>
 * @author Kareem Assaf
 * @see com.example.inventorymanager.ui.home.HomeFragment
 */
public class ItemAdapter extends ArrayAdapter{
    private final Context context;
    private ArrayList<Item> items;
    private ArrayList<Tag> tags;
    private Tag tag;
    private TextView itemTag;

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

        // display the most relevant fields for each item
        itemName.setText(item.getItemName());
        description.setText(item.getDescription());
        estimateValue.setText(item.getEstimatedValue());
        purchaseDate.setText(item.getPurchaseDate());

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

}
