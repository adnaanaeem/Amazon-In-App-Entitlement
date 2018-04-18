package com.adnan.jbsia.dani;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.amazon.device.iap.PurchasingService;
import com.amazon.device.iap.model.RequestId;

import java.util.HashSet;
import java.util.Set;

public class AmazonInApp {

    private SampleIapManager sampleIapManager;

    public AmazonInApp() {
        super();
    }

    public static String product_id;
    public static String boradCastID = ".simplebroadcastreceiver.setup";


    public void onResumeAmazonInApp(){

        sampleIapManager.activate();

        //  Log.d(TAG, "onResume: call getUserData");
        PurchasingService.getUserData();
        //  Log.d(TAG, "onResume: getPurchaseUpdates");
        PurchasingService.getPurchaseUpdates(false);
    }

    public void onStartAmazonInApp(){
        final Set<String> productSkus = new HashSet<String>();
        for (final MySku mySku : MySku.values()) {
            productSkus.add(mySku.getSku());
        }
        PurchasingService.getProductData(productSkus);
    }

    public void purchase(){
        RequestId requestId = PurchasingService.purchase(MySku.LEVEL2.getSku());
    }

    public void setProductID(String id){
       this.product_id = id;
    }

    public void onCreateAmazonInApp(Context _context){
            sampleIapManager = new SampleIapManager(_context);
            final SamplePurchasingListener purchasingListener = new SamplePurchasingListener(sampleIapManager);
            // Log.d(TAG, "onCreate: registering PurchasingListener");
            PurchasingService.registerListener(_context, purchasingListener);

    }

    public void onPauseAmazonInApp(){
        sampleIapManager.deactivate();
    }

    public boolean getPurchase(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean PRO = prefs.getBoolean("pro", false);
        return PRO;
    }

//    public void setPurchase(Context context){
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        Boolean PRO = prefs.getBoolean("pro", false);
//    }

    public String getBroadCastReceiver(Context context){
       String broadCastID = context.getPackageName()+boradCastID;
       return broadCastID;
    }

}
