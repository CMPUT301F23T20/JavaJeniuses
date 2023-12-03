package com.example.inventorymanager;

import android.content.Context;
import android.widget.ArrayAdapter;
import java.util.ArrayList;


/**
 * Adapter class for displaying tags in a ListView or similar view components within an Android application.
 * This adapter is responsible for handling the presentation of Tag objects in the UI. In each list entry,
 * information about the tag is displayed, including:
 * <ul>
 *     <li>the text of the tag,</li>
 *     <li>the color of the tag,</li>
 *     <li>and the list of items associated with the tag.</li>
 * </ul>
 * This adapter uses the Android ArrayAdapter as its base to leverage the handling of list-based data.
 * @author Tomasz Ayobahan
 * @see com.example.inventorymanager.ui.home.HomeFragment
 */
public class TagAdapter extends ArrayAdapter {
    private final Context context;
    private final ArrayList<Tag> tags;

    /**
     * Constructs a new TagAdapter for displaying a list of tags.
     * @param context The current context.
     * @param value The resource ID for a layout file containing a TextView to use when instantiating views.
     * @param tags The list of tags to be displayed.
     */
    public TagAdapter(Context context, int value, ArrayList<Tag> tags) {
        super(context, value, tags);
        this.context = context;
        this.tags = tags;
    }
}
