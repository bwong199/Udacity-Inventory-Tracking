package com.benwong.udacityinventory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.benwong.udacityinventory.db.InventoryDbHelper;

import java.util.ArrayList;

/**
 * Created by benwong on 2016-07-11.
 */
public class InventoryRowAdapter extends ArrayAdapter<Inventory> {

    private InventoryDbHelper mHelper;

    public InventoryRowAdapter(Context context, ArrayList<Inventory> inventories) {
        super(context, 0, inventories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Inventory inventoryItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_item, parent, false);
        }

        if (inventoryItem != null) {
            // Lookup view for data population
            TextView tvProduct = (TextView) convertView.findViewById(R.id.tvProduct);
            TextView tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
            TextView tvQuantity = (TextView) convertView.findViewById(R.id.tvQuantity);
            ImageView productImageView = (ImageView)convertView.findViewById(R.id.productImage);
            final Button saleButton = (Button) convertView.findViewById(R.id.saleBtn);

            saleButton.setFocusable(false);

            saleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(inventoryItem.getProduct());
                    mHelper = new InventoryDbHelper(getContext());

                    mHelper.updateSale(inventoryItem.getProduct(), -1);

                    try {
                        MainActivity.getFromDB();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            tvProduct.setText(inventoryItem.getProduct());
            tvPrice.setText("$" + String.valueOf(inventoryItem.getPrice()));
            tvQuantity.setText(String.valueOf(inventoryItem.getQuantity()));

            byte[] imageAsBytes = Base64.decode(inventoryItem.getImage(), Base64.DEFAULT);
            Bitmap bmp = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);

            productImageView.setImageBitmap(bmp);
        }

        return convertView;
    }
}