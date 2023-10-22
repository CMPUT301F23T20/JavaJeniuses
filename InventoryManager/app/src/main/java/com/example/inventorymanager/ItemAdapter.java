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

    public ItemAdapter(Context context, ArrayList<Item> cities) {
            super(context, 0, cities);
            this.items = cities;
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = convertView;

            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.content, parent, false);
            }

            Item item = items.get(position);
            TextView itemName = view.findViewById(R.id.itemNameTextView);
            TextView description = view.findViewById(R.id.descriptionTextView);
            TextView estimateValue = view.findViewById(R.id.estimateValueTextView);
            TextView purchaseDate = view.findViewById(R.id.purchaseDateTextView);

            String formattedValue = String.format(Locale.US, "%.2f", item.getEstimateValue());
            itemName.setText(item.getItemName());
            description.setText(item.getDescription());
            estimateValue.setText("$" + formattedValue);
            purchaseDate.setText(item.getPurchaseDate());
            return view;
        }



}



