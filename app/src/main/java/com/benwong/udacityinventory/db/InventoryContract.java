package com.benwong.udacityinventory.db;

import android.provider.BaseColumns;

/**
 * Created by benwong on 2016-07-11.
 */
public class InventoryContract {
    public static final String DB_NAME = "com.benwong.udacityinventory.db";
    public static final int DB_VERSION = 1;

    public class InventoryEntry implements BaseColumns {
        public static final String TABLE = "inventories";
        public static final String COL_TASK_PRODUCT_NAME = "product";
        public static final String COL_TASK_PRODUCT_PRICE = "price";
        public static final String COL_TASK_PRODUCT_QUANTITY = "quantity";
    }
}
