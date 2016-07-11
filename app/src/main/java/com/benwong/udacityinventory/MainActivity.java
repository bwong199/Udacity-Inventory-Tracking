package com.benwong.udacityinventory;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.benwong.udacityinventory.db.InventoryDbHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<Inventory> inventoryList = new ArrayList<Inventory>();
    public static ArrayList<String> inventoryListName = new ArrayList<String>();
    private EditText productNameTF;
    private EditText productPriceTF;
    private EditText productQuantTF;

    private Button submitBtn;
    private SQLiteDatabase myDatabase;
    private ListView inventoryListView;
    private ArrayAdapter<String> arrayAdapter;

    public static InventoryRowAdapter mInventoryRowAdapter;
    public static InventoryDbHelper mHelper;
    private Button deleteTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInventoryRowAdapter = new InventoryRowAdapter(this, inventoryList);

        inventoryListView = (ListView) findViewById(R.id.inventoryListView);


        productNameTF = (EditText) findViewById(R.id.productNameTF);
        productPriceTF = (EditText) findViewById(R.id.productPriceTF);
        productQuantTF = (EditText) findViewById(R.id.productQuantTF);

        submitBtn = (Button) findViewById(R.id.submitBtn);
        deleteTable = (Button) findViewById(R.id.deleteTable);

        mHelper = new InventoryDbHelper(this);

//        myDatabase = this.openOrCreateDatabase("Habits", MODE_PRIVATE, null);

//        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, inventoryList);


        getFromDB();

        for(Inventory x: inventoryList){
            System.out.println("Loop " + x.getProduct());
        }

        inventoryListView.setAdapter(mInventoryRowAdapter);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName = productNameTF.getText().toString();
                int productPrice =  Integer.parseInt(productPriceTF.getText().toString());
                int productQuantity =  Integer.parseInt(productQuantTF.getText().toString());


                System.out.println(productName);
                System.out.println(productPrice);
                System.out.println(productQuantity);

                mHelper.insert(productName, productPrice, productQuantity);
                getFromDB();
                mInventoryRowAdapter.notifyDataSetChanged();

                productNameTF.setText("");
                productPriceTF.setText("");
                productQuantTF.setText("");
            }
        });

        deleteTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inventoryList.clear();
                inventoryListName.clear();
                mHelper.deleteInventoriesDB();
                getFromDB();
                mInventoryRowAdapter.notifyDataSetChanged();

            }
        });


        inventoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent i = new Intent(getApplicationContext(), DetailActivity.class);
                i.putExtra("Product", inventoryList.get(position).getProduct());
                i.putExtra("Quantity",inventoryList.get(position).getQuantity());
                startActivity(i);

                System.out.println(position);

                System.out.println(inventoryList.get(position).getProduct());
                System.out.println(inventoryList.get(position).getPrice());
                System.out.println(inventoryList.get(position).getQuantity());
//                mHelper.update(position);
//
//                getFromDB();

            }
        });



    }

    public static void getFromDB() {
        inventoryList.clear();
        inventoryListName.clear();


                mHelper.read();




        System.out.println("finished reading from getFromDB");


            mInventoryRowAdapter.notifyDataSetChanged();


    }
}
