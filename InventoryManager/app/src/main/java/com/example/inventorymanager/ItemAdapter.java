package com.example.inventorymanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Locale;

public class ItemAdapter extends ArrayAdapter{


    private final Context context;
    private ArrayList<Item> items;

    public ItemAdapter(Context context, int value,ArrayList<Item> items) {
        super(context, value, items);
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_cell, parent, false);
        }

        Item item = items.get(position);
        TextView itemName = view.findViewById(R.id.itemNameTextView);
        TextView description = view.findViewById(R.id.descriptionTextView);
        TextView estimateValue = view.findViewById(R.id.estimateValueTextView);
        TextView purchaseDate = view.findViewById(R.id.purchaseDateTextView);

        itemName.setText(item.getItemName());
        description.setText(item.getDescription());
        estimateValue.setText(item.getEstimatedValue());
        purchaseDate.setText(item.getPurchaseDate());
        return view;
    }



}

