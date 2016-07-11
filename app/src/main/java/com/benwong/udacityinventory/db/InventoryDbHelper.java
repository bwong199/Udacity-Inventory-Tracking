package com.benwong.udacityinventory.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.benwong.udacityinventory.Inventory;
import com.benwong.udacityinventory.MainActivity;

/**
 * Created by benwong on 2016-07-11.
 */
public class InventoryDbHelper extends SQLiteOpenHelper {

    public InventoryDbHelper(Context context) {
        super(context, InventoryContract.DB_NAME, null, InventoryContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        String createTable = "CREATE TABLE IF NOT EXISTS " + InventoryContract.InventoryEntry.TABLE + " (" + InventoryContract
                .InventoryEntry.COL_TASK_PRODUCT_NAME + " VARCHAR, " + InventoryContract.InventoryEntry.COL_TASK_PRODUCT_PRICE + " INT(10), " + InventoryContract.InventoryEntry.COL_TASK_PRODUCT_QUANTITY + " INT(10))";
        System.out.println("Create table " + createTable);
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + InventoryContract.InventoryEntry.TABLE);
        onCreate(db);
    }

    public void deleteDatabase() {

        this.deleteInventoriesDB();
    }


    public void deleteInventoriesDB() {
        String deleteScript = "delete from " + InventoryContract.InventoryEntry.TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(deleteScript);


    }


    public void insert(String habitName, int price, int quantity) {


        SQLiteDatabase db = this.getWritableDatabase();
        System.out.println("Habit name in DB Helper " + habitName);

        ContentValues values = new ContentValues();
        values.put(InventoryContract.InventoryEntry.COL_TASK_PRODUCT_NAME, habitName);
        values.put(InventoryContract.InventoryEntry.COL_TASK_PRODUCT_PRICE, price);
        values.put(InventoryContract.InventoryEntry.COL_TASK_PRODUCT_QUANTITY, quantity);

        db.insertWithOnConflict(InventoryContract.InventoryEntry.TABLE,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);

        db.close(); // Closing database connection

    }

//    public void update(int position){
//
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        System.out.println("Update method habit name " + MainActivity.inventoryListName.get(position).toString());
//        Cursor c = db.rawQuery("SELECT * FROM habits WHERE habit = " + "'" + MainActivity.inventoryListName.get(position).toString() + "'" , null);
//
//        try {
//            int habitIndex = c.getColumnIndex(InventoryContract.HabitEntry.COL_TASK_HABIT_NAME);
//            int frequencyIndex = c.getColumnIndex(InventoryContract.HabitEntry.COL_TASK_HABIT_FREQ);
//
//
//            if(c != null && c.moveToFirst()){
//                do{
//                    System.out.println("Update method " + c.getString(habitIndex));
//                    System.out.println("Update method " + Integer.toString(c.getInt(frequencyIndex)));
//                    int updatedFreq = c.getInt(frequencyIndex) + 1;
//                    System.out.println("Updated freq " +  updatedFreq);
////                    String updateScript = "UPDATE " + HabitContract.HabitEntry.TABLE + " SET " + HabitContract.HabitEntry.COL_TASK_HABIT_FREQ  +" = " + updatedFreq + " WHERE " + HabitContract.HabitEntry.COL_TASK_HABIT_NAME +  " = "  + "'" +  c.getString(habitIndex)  + "'";
////                    System.out.println(updateScript);
////                    db.execSQL(updateScript);
//
//                    ContentValues values = new ContentValues();
//                    values.put(InventoryContract.HabitEntry.COL_TASK_HABIT_NAME, c.getString(habitIndex));
//                    values.put(InventoryContract.HabitEntry.COL_TASK_HABIT_FREQ, updatedFreq);
//
//
//                    db.update(InventoryContract.HabitEntry.TABLE, values, InventoryContract.HabitEntry.COL_TASK_HABIT_NAME + " = ?",
//                            new String[] { String.valueOf(c.getString(habitIndex)) });
//
////                    System.out.println("UPDATE habits SET" + " frequency = " + updatedFreq + " WHERE habit = "  + "'" +  c.getString(habitIndex)  + "'");
//
//                } while(c.moveToNext());
//            }
//
//            c.close();
//        } catch(Exception e){
//            e.printStackTrace();
//        }
//    }

    public void read() {
        try {

            SQLiteDatabase db = this.getWritableDatabase();

            String queryString = "SELECT * FROM " + InventoryContract.InventoryEntry.TABLE;

//            myDatabase.execSQL("INSERT INTO habits (habit, frequency) VALUES ('Do homework', 1)");
//
//            myDatabase.execSQL("INSERT INTO habits (habit, frequency) VALUES ('Clean car', 1)");

            Cursor c = db.rawQuery(queryString, null);

            int productIndex = c.getColumnIndex(InventoryContract.InventoryEntry.COL_TASK_PRODUCT_NAME);
            int priceIndex = c.getColumnIndex(InventoryContract.InventoryEntry.COL_TASK_PRODUCT_PRICE);
            int quantityIndex = c.getColumnIndex(InventoryContract.InventoryEntry.COL_TASK_PRODUCT_QUANTITY);

            if (c != null && c.moveToFirst()) {
                do {
                    System.out.println("READ product " + c.getString(productIndex));
                    System.out.println("READ price " + Integer.toString(c.getInt(priceIndex)));
                    System.out.println("READ quantity " + Integer.toString(c.getInt(quantityIndex)));
                    String singleProduct = c.getString(productIndex) + " : " + Integer.toString(c.getInt(priceIndex)) + " - " + Integer.toString(c.getInt(quantityIndex));

                    Inventory eachProduct = new Inventory();
                    eachProduct.setProduct(c.getString(productIndex));
                    eachProduct.setPrice(c.getInt(priceIndex));
                    eachProduct.setQuantity(c.getInt(quantityIndex));

                    MainActivity.inventoryListName.add(eachProduct.getProduct());
                    MainActivity.inventoryList.add(eachProduct);

                } while (c.moveToNext());
            }

            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}