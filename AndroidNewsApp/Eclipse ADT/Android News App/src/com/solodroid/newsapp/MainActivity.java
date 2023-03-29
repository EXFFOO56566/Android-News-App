package com.solodroid.newsapp;
 
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.PushService;
import com.startapp.android.publish.StartAppAd;

public class MainActivity extends FragmentActivity implements
ActionBar.TabListener {

	private String[] tabs = { "RECENT NEWS", "ALL NEWS" };
    private TabsPagerAdapter mAdapter;
    private ViewPager viewPager;
    ActionBar.Tab tab;
    private AdView mAdView;
    private StartAppAd startAppAd = new StartAppAd(this); 
    private ActionBar bar;
    private InterstitialAd interstitial;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
       
        super.onCreate(savedInstanceState);
        StartAppAd.init(this, getString(R.string.startapp_dev_id), getString(R.string.startapp_app_id));
        setContentView(R.layout.activity_main);
        
        //Parse push notification
        Parse.initialize(this, getString(R.string.parse_application_id), getString(R.string.parse_client_key));
		ParseAnalytics.trackAppOpened(getIntent());
		PushService.setDefaultPushCallback(this, MainActivity.class);
	    ParseInstallation.getCurrentInstallation().saveInBackground();
        
        StartAppAd.showSlider(this);
        mAdView = (AdView) findViewById(R.id.adView);
	    mAdView.loadAd(new AdRequest.Builder().build());
	    
	    // Prepare the Interstitial Ad
	 	interstitial = new InterstitialAd(MainActivity.this);
	 	// Insert the Ad Unit ID
	 	interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));
	 	AdRequest adRequest = new AdRequest.Builder().build();
	 	// Load ads into Interstitial Ads
	 	interstitial.loadAd(adRequest);
	 	// Prepare an Interstitial Ad Listener
	 	interstitial.setAdListener(new AdListener() {
	 			public void onAdLoaded() {
	 				// Call displayInterstitial() function
	 				displayInterstitial();
	 			}
	 		});
        
        viewPager = (ViewPager) findViewById(R.id.pager);
        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.header)));
        bar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.header)));
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent))); 
        
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        
        //tab added in action bar
         for(String tab_name:tabs)
         {
        	tab = getActionBar().newTab();
            tab.setText(tab_name);
            tab.setTabListener(this);
            getActionBar().addTab(tab);
         }
         
         /**
 		 * on swiping the viewpager make respective tab selected
 		 * */
 		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

 			@Override
 			public void onPageSelected(int position) {
 				// on changing the page
 				// make respected tab selected
 				getActionBar().setSelectedNavigationItem(position);
 			}

 			@Override
 			public void onPageScrolled(int arg0, float arg1, int arg2) {
 			}

 			@Override
 			public void onPageScrollStateChanged(int arg0) {
 			}
 		});
 		
 		
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction transaction) {
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction transaction) {
    	viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction transaction) {
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	
    	  MenuInflater inflater = getMenuInflater();
          inflater.inflate(R.menu.main, menu);
          

          return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	switch (item.getItemId()) 
        {
        case R.id.refresh:
        	finish();
        	startActivity(getIntent());
        	overridePendingTransition(R.anim.open_next, R.anim.close_next);
    		
    		return true;
    		
    	case R.id.menu_about:
    		
    		Intent about=new Intent(getApplicationContext(), About_Us.class);
    		startActivity(about);
    		
    		return true;
    		
    	case R.id.menu_overflow:
    		return true;
    		
    	case R.id.menu_moreapp:
    		
    		startActivity(new Intent(
						Intent.ACTION_VIEW,
						Uri.parse(getString(R.string.play_more_apps))));
			
    		return true;
    	
    	case R.id.menu_rateapp:
    		
    		final String appName = getPackageName();//your application package name i.e play store application url
			try {
				startActivity(new Intent(Intent.ACTION_VIEW,
						Uri.parse("market://details?id="
								+ appName)));
			} catch (android.content.ActivityNotFoundException anfe) {
				startActivity(new Intent(
						Intent.ACTION_VIEW,
						Uri.parse("http://play.google.com/store/apps/details?id="
								+ appName)));
			}
    		return true;
    	
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    protected void onPause() {
        mAdView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdView.resume();
        startAppAd.onResume();
    }

    @Override
    protected void onDestroy() {
        mAdView.destroy();
        super.onDestroy();
    }

    public void displayInterstitial() {
		// If Ads are loaded, show Interstitial else show nothing.
		if (interstitial.isLoaded()) {
			interstitial.show();
		}
	}
	
}
