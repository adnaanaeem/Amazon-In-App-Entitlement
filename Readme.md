# Amazon-In-App-Entitlement
Amazon In App Entitlement for inapp purchaseds
Simply add this module in your project and then start service of amazon on create method

  setupLibraryReceiver();  //

// now add these methods in activity or fragment
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

//-----------------------------------Add these also for fargment ---------------

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
