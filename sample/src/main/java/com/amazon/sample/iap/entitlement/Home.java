package com.amazon.sample.iap.entitlement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.adnan.jbsia.dani.AmazonInApp;

public class Home extends AppCompatActivity {

    Button purchase_btn;
    TextView purchase_txt;
    Context _context;
    private BroadcastReceiver br;
    AmazonInApp amazonInApp = new AmazonInApp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        purchase_btn = (Button) findViewById(R.id.purchase_btn);
        purchase_txt = (TextView) findViewById(R.id.purchase_txt);
        _context = this;

        setupLibraryReceiver();  //


        if (amazonInApp.getPurchase(_context)) {
            disableButoon();
        }
    }

    public void amazon_purchase_click(View view){
        amazonInApp.purchase();
    }



    private void setupLibraryReceiver() {
        amazonInApp.setProductID(getResources().getString(R.string.product_ID));
        amazonInApp.onCreateAmazonInApp(_context);

        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent i) {
                if (amazonInApp.getPurchase(_context)) {
                    disableButoon();
                }
            }
        };
        _context.registerReceiver(br, new IntentFilter(amazonInApp.getBroadCastReceiver(_context)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        amazonInApp.onResumeAmazonInApp();
    }

    @Override
    protected void onStart() {
        super.onStart();
        amazonInApp.onStartAmazonInApp();
    }
    @Override
    protected void onPause() {
        super.onPause();
        amazonInApp.onPauseAmazonInApp();
    }


    public void disableButoon(){
            purchase_txt.setText("Product Purchased");
            purchase_btn.setEnabled(false);
            purchase_btn.setText("Purchase Button Disabled");
            purchase_txt.setBackgroundColor(Color.RED);
            purchase_txt.setTextColor(Color.WHITE);

    }

//  Used for Fragment broadcast
//    @Override
//    public void onAttach(Context context)
//    {
//        super.onAttach(context);
//        _context = context;
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        _context.unregisterReceiver(br);
//    }

}
