package com.adnan.jbsia.dani;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.adnan.jbsia.dani.SampleIapManager.EntitlementRecord;

/**
 * DAO class for sample purchase data
 * 
 * 
 */
public class EntitlementsDataSource {

    private static final String TAG = "SampleIAPManager";

    private SQLiteDatabase database;
    private final SampleSQLiteHelper dbHelper;

    private final String[] allColumns = { SampleSQLiteHelper.COLUMN_RECEIPT_ID, SampleSQLiteHelper.COLUMN_USER_ID,
            SampleSQLiteHelper.COLUMN_SKU, SampleSQLiteHelper.COLUMN_PURCHASE_DATE,
            SampleSQLiteHelper.COLUMN_CANCEL_DATE };

    public EntitlementsDataSource(final Context context) {
        dbHelper = new SampleSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    private EntitlementRecord cursorToEntitlementRecord(final Cursor cursor) {
        final EntitlementRecord entitlementRecord = new EntitlementRecord();
        entitlementRecord.setReceiptId(cursor.getString(cursor.getColumnIndex(SampleSQLiteHelper.COLUMN_RECEIPT_ID)));
        entitlementRecord.setUserId(cursor.getString(cursor.getColumnIndex(SampleSQLiteHelper.COLUMN_USER_ID)));
        entitlementRecord.setSku(cursor.getString(cursor.getColumnIndex(SampleSQLiteHelper.COLUMN_SKU)));
        entitlementRecord
            .setPurchaseDate(cursor.getLong(cursor.getColumnIndex(SampleSQLiteHelper.COLUMN_PURCHASE_DATE)));
        entitlementRecord.setCancelDate(cursor.getLong(cursor.getColumnIndex(SampleSQLiteHelper.COLUMN_CANCEL_DATE)));
        return entitlementRecord;
    }

    /**
     * Return the entitlement for given user and sku
     * 
     * @param userId
     * @param sku
     * @return
     */
    public final EntitlementRecord getLatestEntitlementRecordBySku(final String userId, final String sku) {
        Log.d(TAG, "getEntitlementRecordBySku: userId (" + userId + "), sku (" + sku + ")");

        final String where = SampleSQLiteHelper.COLUMN_USER_ID + " = ? and " + SampleSQLiteHelper.COLUMN_SKU + " = ?";
        final Cursor cursor = database.query(SampleSQLiteHelper.TABLE_ENTITLEMENTS, allColumns, where, new String[] {
                userId, sku }, null, null, SampleSQLiteHelper.COLUMN_PURCHASE_DATE + " desc ");
        final EntitlementRecord result;
        cursor.moveToFirst();
        if (cursor.isAfterLast()) {
            result = null;
            Log.d(TAG, "getEntitlementRecordBySku: no record found ");
        } else {
            result = cursorToEntitlementRecord(cursor);
            Log.d(TAG, "getEntitlementRecordBySku: found ");
        }
        cursor.close();
        return result;

    }
    /**
     * Insert or update the entitlement records table with specified receipt id as primary key.
     *  
     * @param receiptId
     * @param userId
     * @param sku
     * @param purchaseDate
     * @param cancelDate
     */
    public void insertOrUpdateEntitlementRecord(final String receiptId,
            final String userId,
            final String sku,
            final long purchaseDate,
            final long cancelDate) {

        Log.d(TAG, "insertOrUpdateEntitlementRecord: receiptId (" + receiptId + "),userId (" + userId + ")");
        final String where = SampleSQLiteHelper.COLUMN_RECEIPT_ID + " = ? and "
                             + SampleSQLiteHelper.COLUMN_CANCEL_DATE
                             + " > 0";

        final Cursor cursor = database.query(SampleSQLiteHelper.TABLE_ENTITLEMENTS,
                                             allColumns,
                                             where,
                                             new String[] { receiptId },
                                             null,
                                             null,
                                             null);
        final int count = cursor.getCount();
        cursor.close();
        if (count > 0) {
            // There are record with given receipt id and cancel_date>0 in the
            // table, this record should be final and cannot be overwritten
            // anymore.
            Log.w(TAG, "Record already in final state");
        } else {
            // Insert the record into database with CONFLICT_REPLACE flag.
            final ContentValues values = new ContentValues();
            values.put(SampleSQLiteHelper.COLUMN_RECEIPT_ID, receiptId);
            values.put(SampleSQLiteHelper.COLUMN_USER_ID, userId);
            values.put(SampleSQLiteHelper.COLUMN_SKU, sku);
            values.put(SampleSQLiteHelper.COLUMN_PURCHASE_DATE, purchaseDate);
            values.put(SampleSQLiteHelper.COLUMN_CANCEL_DATE, cancelDate);
            database.insertWithOnConflict(SampleSQLiteHelper.TABLE_ENTITLEMENTS,
                                          null,
                                          values,
                                          SQLiteDatabase.CONFLICT_REPLACE);
        }
    }

    /**
     * Find entitlement record by specified receipt ID
     * @param userId
     * @param receiptId
     * @return
     */
    public EntitlementRecord getEntitlementRecordByReceiptId(final String receiptId) {
        Log.d(TAG, "getEntitlementRecordByReceiptId: receiptId (" + receiptId + ")");

        final String where = SampleSQLiteHelper.COLUMN_RECEIPT_ID + "= ?";
        final Cursor cursor = database.query(SampleSQLiteHelper.TABLE_ENTITLEMENTS,
                                             allColumns,
                                             where,
                                             new String[] { receiptId },
                                             null,
                                             null,
                                             null);
        final EntitlementRecord result;
        cursor.moveToFirst();
        if (cursor.isAfterLast()) {
            result = null;
            Log.d(TAG, "getEntitlementRecordByReceiptId: no record found ");
        } else {
            result = cursorToEntitlementRecord(cursor);
            Log.d(TAG, "getEntitlementRecordByReceiptId: found ");
        }
        cursor.close();
        return result;
    }

    /**
     * Cancel the specified Entitlement record by set the cancel date
     * @param receiptId
     * @param cancelDate
     * @return
     */
    public boolean cancelEntitlement(final String receiptId, final long cancelDate) {
        Log.d(TAG, "cancelEntitlement: receiptId (" + receiptId + "), cancelDate:(" + cancelDate + ")");

        final String where = SampleSQLiteHelper.COLUMN_RECEIPT_ID + " = ?";
        final ContentValues values = new ContentValues();
        values.put(SampleSQLiteHelper.COLUMN_CANCEL_DATE, cancelDate);
        final int updated = database.update(SampleSQLiteHelper.TABLE_ENTITLEMENTS,
                                            values,
                                            where,
                                            new String[] { receiptId });
        Log.d(TAG, "cancelEntitlement: updated " + updated);
        return updated > 0;

    }

}
