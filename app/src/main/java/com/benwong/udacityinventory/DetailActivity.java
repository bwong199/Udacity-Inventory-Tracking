package com.benwong.udacityinventory;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private TextView productTV;
    private TextView quantityTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        productTV = (TextView)findViewById(R.id.productTV);
        quantityTV = (TextView)findViewById(R.id.currentQuantTV);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }

        String productName = extras.getString("Product");
        int productQuantity = extras.getInt("Quantity");

        productTV.setText(productName);
        quantityTV.setText("Current Quantity: " + String.valueOf(productQuantity));


    }
}
