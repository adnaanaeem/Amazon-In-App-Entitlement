package com.adnan.jbsia.dani;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.amazon.device.iap.PurchasingService;
import com.amazon.device.iap.model.FulfillmentResult;
import com.amazon.device.iap.model.Product;
import com.amazon.device.iap.model.Receipt;
import com.amazon.device.iap.model.UserData;

import java.util.Map;
import java.util.Set;

/**
 * This is a sample of how an application may handle InAppPurchasing. The major
 * functions includes
 * <ul>
 * <li>Simple user and order history management</li>
 * <li>Grant "Level 2" purchases to customer</li>
 * <li>Enable/disable purchases from GUI</li>
 * <li>Save persistent order data into SQLite Database</li>
 * </ul>
 *
 *
 */
public class SampleIapManager {
    /**
     * The EntitlementRecord class represents a Entitlement purchase record that
     * used by the Entitlement Sample App
     *
     */
    public static class EntitlementRecord {
        public static final long DATE_NOT_SET = -1;
        private String receiptId;
        private String userId;
        private String sku;
        private long purchaseDate;
        private long cancelDate;

        public long getCancelDate() {
            return cancelDate;
        }

        public void setCancelDate(final long cancelDate) {
            this.cancelDate = cancelDate;
        }

        public String getReceiptId() {
            return receiptId;
        }

        public void setReceiptId(final String receiptId) {
            this.receiptId = receiptId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(final String userId) {
            this.userId = userId;
        }

        public String getSku() {
            return sku;
        }

        public void setSku(final String sku) {
            this.sku = sku;
        }

        public long getPurchaseDate() {
            return purchaseDate;
        }

        public void setPurchaseDate(final long purchaseDate) {
            this.purchaseDate = purchaseDate;
        }

    }

    private static final String TAG = "SampleIAPManager";

    final private Activity mainActivity;
    private boolean level2ProductAvailable;
    private UserIapData userIapData;
    final private EntitlementsDataSource dataSource;

    public SampleIapManager(final Context mainActivity) {
        this.mainActivity = (Activity) mainActivity;
        dataSource = new EntitlementsDataSource(mainActivity.getApplicationContext());
    }

    /**
     * Method to set the app's amazon user id and marketplace from IAP SDK
     * responses.
     *
     * @param newAmazonUserId
     * @param newAmazonMarketplace
     */
    public void setAmazonUserId(final String newAmazonUserId, final String newAmazonMarketplace) {
        // Reload everything if the Amazon user has changed.
        if (newAmazonUserId == null) {
            // A null user id typically means there is no registered Amazon
            // account.
            if (userIapData != null) {
                userIapData = null;
                refreshLevel2Availability();
            }
        } else if (userIapData == null || !newAmazonUserId.equals(userIapData.getAmazonUserId())) {
            // If there was no existing Amazon user then either no customer was
            // previously registered or the application has just started.

            // If the user id does not match then another Amazon user has
            // registered.

            userIapData = new UserIapData(newAmazonUserId, newAmazonMarketplace);
            refreshLevel2Availability();
        }
    }

    /**
     * Allow the customer to buy Level2 product.
     *
     * @param productData
     */
    public void enablePurchaseForSkus(final Map<String, Product> productData) {
        if (productData.containsKey(MySku.LEVEL2.getSku())) {
            level2ProductAvailable = true;
        }
    }

    /**
     * Disallow purchase for Level2 product.
     *
     * @param unavailableSkus
     */
    public void disablePurchaseForSkus(final Set<String> unavailableSkus) {
        if (unavailableSkus.contains(MySku.LEVEL2.toString())) {
            level2ProductAvailable = false;
            // A product can be unavailable for the following reasonses:
            // * Item not available for this country
            // * Item pulled off from Appstore by developer
            // * Item pulled off from Appstore by Amazon
            // mainActivity.showMessage("the given product isn't available now! ");
            Toast.makeText(mainActivity, "the given product isn't available now!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method to handle receipts
     *
     * @param requestId
     * @param receipt
     * @param userData
     */
    public void handleReceipt(final String requestId, final Receipt receipt, final UserData userData) {
        switch (receipt.getProductType()) {
            case CONSUMABLE:
                // check consumable sample for how to handle consumable purchases
                break;
            case ENTITLED:
                handleEntitlementPurchase(receipt, userData);
                break;
            case SUBSCRIPTION:
                // check subscription sample for how to handle consumable purchases
                break;
        }

    }

    /**
     * Show purchase failed message
     * @param sku
     */
    public void purchaseFailed(final String sku) {
        //  mainActivity.showMessage("Purchase failed!");
        Toast.makeText(mainActivity, "Purchase failed!", Toast.LENGTH_SHORT).show();
    }

    public UserIapData getUserIapData() {
        return this.userIapData;
    }

    public boolean isLevel2ProductAvailable() {
        return level2ProductAvailable;
    }

    public void setLevel2ProductAvailable(final boolean level2ProductAvailable) {
        this.level2ProductAvailable = level2ProductAvailable;
    }

    /**
     * Disable all purchases on UI
     */
    public void disableAllPurchases() {
        this.setLevel2ProductAvailable(false);
        refreshLevel2Availability();
    }

    /**
     * Reload the customer's purchase record from database, and check the Level2
     * product's availability based on the customer's purchase records.
     */
    public void refreshLevel2Availability() {
        boolean level2Purchased = false;
        if (userIapData != null) {
            final EntitlementRecord entitlementRecord = dataSource.getLatestEntitlementRecordBySku(userIapData
                    .getAmazonUserId(), MySku.LEVEL2.getSku());
            // Make sure the entitlement purchase record is not expired or
            // canceled
            level2Purchased = (EntitlementRecord.DATE_NOT_SET == entitlementRecord.cancelDate);
            // mainActivity.sendInternalBroadcast();

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mainActivity);
            prefs.edit().putBoolean("pro", true).apply();
            Intent intent = new Intent();
            String packageName = mainActivity.getPackageName().toString();
            intent.setAction(packageName+".simplebroadcastreceiver.setup");
            intent.putExtra("Activity", "sendInternalBroadcast");
            mainActivity.sendBroadcast(intent);

            //  Toast.makeText(mainActivity, "Yes purchaesd", Toast.LENGTH_SHORT).show();
        }

        // mainActivity.setLevel2Availbility(level2ProductAvailable, level2Purchased);
    }

    /**
     * Gracefully close the database when the main activity's onStop and
     * onDestroy
     *
     */
    public void deactivate() {
        dataSource.close();

    }

    /**
     * Connect to the database when main activity's onStart and onResume
     */
    public void activate() {
        dataSource.open();

    }
    //
//    /**
//     * This method contains the business logic to fulfill the customer's
//     * purchase based on the receipt received from InAppPurchase SDK's
//     * {@link PurchasingListener#onPurchaseResponse} or
//     * {@link PurchasingListener#onPurchaseUpdates} method.
//     *
//     *
//     * @param receiptId
//     * @param userData
//     */
    private void grantEntitlementPurchase(final Receipt receipt, final UserData userData) {
        final MySku mySku = MySku.fromSku(receipt.getSku(), userIapData.getAmazonMarketplace());
        // Verify that the SKU is still applicable.
        if (mySku != MySku.LEVEL2) {
            Log.w(TAG, "The SKU [" + receipt.getSku() + "] in the receipt is not valid anymore ");
            // if the sku is not applicable anymore, call
            // PurchasingService.notifyFulfillment with status "UNAVAILABLE"
            PurchasingService.notifyFulfillment(receipt.getReceiptId(), FulfillmentResult.UNAVAILABLE);
            return;
        }
        try {
            // Actual entitlement granting logic: Save the entitlement purchase
            // record to database so it can be loaded by
            // refreshLevel2Availability() method later,
            // then notify Amazon Appstore.
            saveEntitlementPurchase(receipt, userData.getUserId());
            PurchasingService.notifyFulfillment(receipt.getReceiptId(), FulfillmentResult.FULFILLED);
        } catch (final Throwable e) {
            // If for any reason the app is not able to fulfill the purchase,
            // add your own error handling code here.
            Log.e(TAG, "Failed to grant entitlement purchase, with error " + e.getMessage());
        }
    }

    /**
     * We strongly recommend that you verify the receipt server-side The server
     * side verification ideally should include checking with Amazon RVS
     * (Receipt Verification Service) to verify the receipt details.
     *
     * @see <a href=
     *      "https://developer.amazon.com/appsandservices/apis/earn/in-app-purchasing/docs/rvs"
     *      >Appstore's Receipt Verification Service</a>
     *
     * @param receiptId
     * @return
     */
    private boolean verifyReceiptFromYourService(final String receiptId, final UserData userData) {
        // TODO Add your own server side accessing and verification code
        return true;
    }

    /**
     * This sample app includes a simple SQLite implementation for save
     * Entitlement purchases locally.
     *
     * We strongly recommend that you save purchase information on a server.
     *
     * Use Receipt.isCanceled() to determine whether the receipt is in a
     * "CANCELED" state
     *
     * @param receipt
     * @param userId
     */
    private void saveEntitlementPurchase(final Receipt receipt, final String userId) {
        // TODO replace with your own implementation
        final long purchaseDate = receipt.getPurchaseDate() != null ? receipt.getPurchaseDate().getTime()
                : EntitlementRecord.DATE_NOT_SET;

        final long cancelDate = receipt.isCanceled() ? receipt.getCancelDate().getTime()
                : EntitlementRecord.DATE_NOT_SET;
        dataSource.insertOrUpdateEntitlementRecord(receipt.getReceiptId(),
                userId,
                receipt.getSku(),
                purchaseDate,
                cancelDate);

    }

    /**
     * Method to handle Entitlement Purchase
     *
     * @param receipt
     * @param userData
     */
    private void handleEntitlementPurchase(final Receipt receipt, final UserData userData) {
        try {
            if (receipt.isCanceled()) {
                // Check whether this receipt is to revoke a entitlement
                // purchase
                revokeEntitlement(receipt, userData.getUserId());
            } else {
                // We strongly recommend that you verify the receipt
                // server-side.
                if (!verifyReceiptFromYourService(receipt.getReceiptId(), userData)) {
                    // if the purchase cannot be verified,
                    // show relevant error message to the customer.
                    // mainActivity.showMessage("Purchase cannot be verified, please retry later.");
                    Toast.makeText(mainActivity, "Purchase cannot be verified, please retry later.", Toast.LENGTH_SHORT).show();
                    return;
                }
                grantEntitlementPurchase(receipt, userData);
            }
            return;
        } catch (final Throwable e) {
            /// mainActivity.showMessage("Purchase cannot be completed, please retry");
            Toast.makeText(mainActivity, "Purchase cannot be completed, please retry", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Private method to revoke an entitlement purchase from the customer
     *
     * Please implement your application-specific logic to handle the revocation
     * of an entitlement purchase.
     *
     *
     * @param receipt
     * @param userId
     */
    private void revokeEntitlement(final Receipt receipt, final String userId) {
        String receiptId = receipt.getReceiptId();
        final EntitlementRecord record;
        if (receiptId == null) {
            // The revoked receipt's receipt id may be null on older devices.
            record = dataSource.getLatestEntitlementRecordBySku(userId, receipt.getSku());
            receiptId = record.getReceiptId();
        } else {
            record = dataSource.getEntitlementRecordByReceiptId(receiptId);
        }
        if (record == null) {
            // No purchase record for the entitlement before, do nothing.
            return;
        }
        if (record.getCancelDate() == EntitlementRecord.DATE_NOT_SET || record.getCancelDate() > System
                .currentTimeMillis()) {
            final long cancelDate = receipt.getCancelDate() != null ? receipt.getCancelDate().getTime() : System
                    .currentTimeMillis();
            dataSource.cancelEntitlement(receiptId, cancelDate);
        } else {
            // Already canceled, do nothing
            return;
        }

    }

}
