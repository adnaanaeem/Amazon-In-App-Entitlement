# Amazon-In-App-Entitlement
[![](https://jitpack.io/v/Adnan865/Amazon-In-App-Entitlement.svg)](https://jitpack.io/#Adnan865/Amazon-In-App-Entitlement)

Amazon In App Entitlement for inapp purchases


<p>

<div class="row">
   <div class="column">
<a target="_blank" rel="noopener noreferrer" href="https://github.com/Adnan865/Amazon-In-App-Entitlement/blob/master/extras/Screenshots/1.png">
<img src="https://github.com/Adnan865/Amazon-In-App-Entitlement/blob/master/extras/Screenshots/1.png" height="auto" width="200"></a> 
	  </dive>

 <div class="column">
<a target="_blank" rel="noopener noreferrer" href="https://github.com/Adnan865/Amazon-In-App-Entitlement/blob/master/extras/Screenshots/2.png">
<img src="https://github.com/Adnan865/Amazon-In-App-Entitlement/blob/master/extras/Screenshots/2.png" height="auto" width="200"></a>
	  </div>

 <div class="column">
<a target="_blank" rel="noopener noreferrer" href="https://github.com/Adnan865/Amazon-In-App-Entitlement/blob/master/extras/Screenshots/3.png">
<img src="https://github.com/Adnan865/Amazon-In-App-Entitlement/blob/master/extras/Screenshots/3.png" height="auto" width="200"></a>
	</div>

 <div class="column">
<a target="_blank" rel="noopener noreferrer" href="https://github.com/Adnan865/Amazon-In-App-Entitlement/blob/master/extras/Screenshots/4.png">
<img src="https://github.com/Adnan865/Amazon-In-App-Entitlement/blob/master/extras/Screenshots/4.png" height="auto" width="200"></a>
</div>
</div>
</p>



<h4>Add it in your root build.gradle at the end of repositories:</h4>


```
allprojects {
		 repositories {
			 ...
			 maven { url 'https://jitpack.io' }
		 }
}
```

<h4>Step #2. Add the dependency See Latest Release</h4> 

[![](https://jitpack.io/v/Adnan865/Amazon-In-App-Entitlement.svg)](https://jitpack.io/#Adnan865/Amazon-In-App-Entitlement)

```
dependencies {
	        implementation 'com.github.Adnan865:Amazon-In-App-Entitlement:1.0.0'
	}
```
 
<h4>Declare varibales of InApp Library and Broadcast Receiver</h4>

```
private BroadcastReceiver br;
AmazonInApp amazonInApp = new AmazonInApp();
```

<h4>Initilize Amazon Inapp veraibales</h4>

```
 amazonInApp.setProductID(getResources().getString(R.string.product_ID));
 amazonInApp.onCreateAmazonInApp(_context);
```


<h4>Now add this methods in activity or fragment</h4>

```
 private void setupLibraryReceiver() {
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
```

<h4>Call this module in on create method.</h4>

```
 setupLibraryReceiver();
 ```

<h4>Implemnet overirde methods</h4>

```
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
```

<h4>Add these also if you are working with fargments</h4>

```
 // Used for Fragment broadcast
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        _context = context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        _context.unregisterReceiver(br);
    }
```

<h4>Now just Call this method to purchase</h4>

```
 amazonInApp.purchase();
 ```
 

<h3>For SandBox Test Purchase Enviroment do below tasks</h3>


- [ ] Downlaod Amzaon app store app from [here](https://github.com/Adnan865/Amazon-In-App-Entitlement/blob/master/extras/amazonappstore.apk) and instal it.
- [ ] Downlaod Amazon App Tester app [here](https://www.amazon.com/Amazon-App-Tester/dp/B00BN3YZM2/) or from <b>amzaon app store</b> and instal it also.
- [ ] Get json file for test product key from app [here](https://github.com/Adnan865/Amazon-In-App-Entitlement/blob/master/extras/amazon.sdktester.json) and copy to phone memory.
- [ ] All set now download the peoject and run the demo app for purchase demo.

