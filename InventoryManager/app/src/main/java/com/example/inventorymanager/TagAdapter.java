package com.example.inventorymanager;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class TagAdapter extends ArrayAdapter {
    private final Context context;
    private ArrayList<Tag> tags;

    /**
     *
     */
    public TagAdapter(Context context, int value, ArrayList<Tag> tags) {
        super(context, value, tags);
        this.context = context;
        this.tags = tags;
    }
}
