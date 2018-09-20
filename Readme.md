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

<h4>Now add these methods in activity or fragment</h4>

```
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
```

<h4>cal this module in your project and then start service of amazon on create method</h4>

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

<h3>Add these also if you are working with fargments</h3>



