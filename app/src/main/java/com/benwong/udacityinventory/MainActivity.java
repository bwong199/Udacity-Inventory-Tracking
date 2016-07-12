package com.benwong.udacityinventory;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.benwong.udacityinventory.db.InventoryDbHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<Inventory> inventoryList = new ArrayList<Inventory>();
    public static ArrayList<String> inventoryListName = new ArrayList<String>();
    private EditText productNameTF;
    private EditText productPriceTF;
    private EditText productQuantTF;
    public static TextView instructionsTV;
    public static final int REQUEST_PICK_IMAGE = 1;

    private Button submitBtn;
    private Button attachImgBtn;

    private SQLiteDatabase myDatabase;
    private ListView inventoryListView;
    private ArrayAdapter<String> arrayAdapter;

    public static InventoryRowAdapter mInventoryRowAdapter;
    public static InventoryDbHelper mHelper;
    private Button deleteTable;

    private String productImage;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        System.out.println("Fragment onActivityResult" + " " + requestCode + " " + resultCode + " " + data);
        System.out.println("constant request code " + REQUEST_PICK_IMAGE);

        if (resultCode == -1 && data != null) {
            Toast.makeText(getApplicationContext(), "Image successfully uploaded", Toast.LENGTH_SHORT).show();
            Uri selectedImage = data.getData();

            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), selectedImage);

                bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                byte[] byteArray = stream.toByteArray();

                productImage = Base64.encodeToString(byteArray, Base64.DEFAULT);


            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(getApplicationContext(), "Error Uploading Image", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInventoryRowAdapter = new InventoryRowAdapter(this, inventoryList);

        inventoryListView = (ListView) findViewById(R.id.inventoryListView);
        productNameTF = (EditText) findViewById(R.id.productNameTF);
        productPriceTF = (EditText) findViewById(R.id.productPriceTF);
        productQuantTF = (EditText) findViewById(R.id.productQuantTF);

        instructionsTV = (TextView) findViewById(R.id.instructionsTV);

        submitBtn = (Button) findViewById(R.id.submitBtn);
        deleteTable = (Button) findViewById(R.id.deleteTable);
        attachImgBtn = (Button)findViewById(R.id.attachImgBtn);

        mHelper = new InventoryDbHelper(this);

        inventoryList.clear();
        inventoryListName.clear();
        getFromDB();

        for (Inventory x : inventoryList) {
            System.out.println("Loop " + x.getProduct());
        }

        inventoryListView.setAdapter(mInventoryRowAdapter);

        attachImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(i, REQUEST_PICK_IMAGE);
                Toast.makeText(getApplicationContext(), "Successfully uploaded product image", Toast.LENGTH_SHORT).show();
            }
        });




        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(productNameTF.getText().toString().equals("") || productPriceTF.getText().toString().equals("") || productQuantTF.getText().toString().equals("") || productImage == null){
                    Toast.makeText(getApplicationContext(), "Please fill in all the fields and make sure to upload a picture", Toast.LENGTH_SHORT).show();

                }else {

//                    System.out.println("Product Image String before submit " + productImage);
                    String productName = productNameTF.getText().toString();
                    int productPrice = Integer.parseInt(productPriceTF.getText().toString());
                    int productQuantity = Integer.parseInt(productQuantTF.getText().toString());
                    mHelper.insert(productName, productPrice, productQuantity, productImage);
                    getFromDB();
                    mInventoryRowAdapter.notifyDataSetChanged();

                    productNameTF.setText("");
                    productPriceTF.setText("");
                    productQuantTF.setText("");
                }
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

                try {
                    Intent i = new Intent(getApplicationContext(), DetailActivity.class);
                    i.putExtra("Product", inventoryList.get(position).getProduct());
                    i.putExtra("Quantity", inventoryList.get(position).getQuantity());
//                    i.putExtra("Image", inventoryList.get(position).getImage());
                    startActivity(i);
                }catch(Exception e){
                    e.printStackTrace();
                }


                System.out.println(position);
                System.out.println(inventoryList.get(position).getProduct());
                System.out.println(inventoryList.get(position).getPrice());
                System.out.println(inventoryList.get(position).getQuantity());

            }
        });
    }

    public static void getFromDB() {
        inventoryList.clear();
        inventoryListName.clear();

        mHelper.read();
        System.out.println("finished reading from getFromDB");
        mInventoryRowAdapter.notifyDataSetChanged();

        if (inventoryList.isEmpty()) {
            instructionsTV.setVisibility(View.VISIBLE);
        } else {
            instructionsTV.setVisibility(View.INVISIBLE);
        }
    }
}
