package com.benwong.udacityinventory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.benwong.udacityinventory.db.InventoryDbHelper;

public class DetailActivity extends AppCompatActivity {

    public static TextView productTV;
    public static TextView quantityTV;
    private Button editQuantityBtn;
    private Button orderBtn;
    private Button deleteProductBtn;

    private EditText quantityChangeText;
    private InventoryDbHelper mHelper;
    private String productName;
    private int productQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        productTV = (TextView) findViewById(R.id.productTV);
        quantityTV = (TextView) findViewById(R.id.currentQuantTV);

        quantityChangeText = (EditText) findViewById(R.id.editQuantityText);
        editQuantityBtn = (Button) findViewById(R.id.editQuantBtn);
        deleteProductBtn = (Button) findViewById(R.id.deleteProductBtn);

        orderBtn = (Button) findViewById(R.id.orderBtn);

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

                if(quantityChange.equals("")){
                    Toast.makeText(getApplicationContext(),"Please fill in the quantity change value ", Toast.LENGTH_SHORT).show();
                } else {
                    System.out.println(quantityChange);
                    quantityChangeText.setText("");

                    mHelper = new InventoryDbHelper(getApplicationContext());

                    mHelper.updateSale(productName, Integer.parseInt(quantityChange));

                    try {
                        mHelper.readSingleProduct(productName);

                        MainActivity.getFromDB();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Ordering...");

                String subject = "Ordering new supply " + productName;
                String message = "Ordering supply for " + productName;
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{
                        "mail--id"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
                emailIntent.setType("text/plain");
                startActivity(emailIntent);
            }
        });

        deleteProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Deleting product");
                Intent newIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(newIntent);
                mHelper = new InventoryDbHelper(getApplicationContext());
                mHelper.deleteProduct(productName);
                MainActivity.getFromDB();
            }
        });

    }


}
