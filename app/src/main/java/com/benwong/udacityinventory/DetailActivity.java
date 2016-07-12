package com.benwong.udacityinventory;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.benwong.udacityinventory.db.InventoryDbHelper;

public class DetailActivity extends AppCompatActivity {

    public static TextView productTV;
    public static TextView quantityTV;
    private Button editQuantityBtn;
    private EditText quantityChangeText;
    private InventoryDbHelper mHelper;
    private String productName;
    private int productQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        productTV = (TextView)findViewById(R.id.productTV);
        quantityTV = (TextView)findViewById(R.id.currentQuantTV);

        quantityChangeText = (EditText)findViewById(R.id.editQuantityText);
        editQuantityBtn = (Button)findViewById(R.id.editQuantBtn);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }

        productName = extras.getString("Product");
        productQuantity = extras.getInt("Quantity");

        productTV.setText(productName);
        quantityTV.setText("Current Quantity: " + String.valueOf(productQuantity));


        editQuantityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantityChange = quantityChangeText.getText().toString();
                System.out.println(quantityChange);
                quantityChangeText.setText("");

                mHelper = new InventoryDbHelper(getApplicationContext());

                mHelper.updateSale(productName, Integer.parseInt(quantityChange));

                try {
                    mHelper.readSingleProduct(productName);

                    MainActivity.getFromDB();




                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getData(){

    }
}
