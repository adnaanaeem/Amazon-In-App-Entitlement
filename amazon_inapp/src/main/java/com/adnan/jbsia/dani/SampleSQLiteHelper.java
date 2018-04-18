package com.adnan.jbsia.dani;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Sample SQLiteHelper for the purchase record table
 * 
 */
public class SampleSQLiteHelper extends SQLiteOpenHelper {
    //talble name
    public static final String TABLE_ENTITLEMENTS = "entitlements";
    //receipt id
    public static final String COLUMN_RECEIPT_ID = "receipt_id";
    //amazon user id
    public static final String COLUMN_USER_ID = "user_id";
    //product sku
    public static final String COLUMN_SKU = "sku";
    //purchase date
    public static final String COLUMN_PURCHASE_DATE = "purchase_date";
    //cancel date
    public static final String COLUMN_CANCEL_DATE = "cancel_date";
    //database name
    private static final String DATABASE_NAME = "entitlements.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table " + TABLE_ENTITLEMENTS
                                                  + "("
                                                  + COLUMN_RECEIPT_ID
                                                  + " text primary key not null, "
                                                  + COLUMN_USER_ID
                                                  + " text not null, "
                                                  + COLUMN_PURCHASE_DATE
                                                  + " integer, "
                                                  + COLUMN_CANCEL_DATE
                                                  + " integer, "
                                                  + COLUMN_SKU
                                                  + " text not null "
                                                  + ");";

    public SampleSQLiteHelper(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        Log
            .w(SampleSQLiteHelper.class.getName(), "Upgrading database from version " + oldVersion
                                                   + " to "
                                                   + newVersion);
        // do nothing in the sample
    }

}