package com.solodroid.newsappnoads;
 
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

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.PushService;

public class MainActivity extends FragmentActivity implements
ActionBar.TabListener {

	private String[] tabs = { "RECENT NEWS", "ALL NEWS" };
    private TabsPagerAdapter mAdapter;
    private ViewPager viewPager;
    ActionBar.Tab tab;
    private ActionBar bar;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Parse push notification
        Parse.initialize(this, getString(R.string.parse_application_id), getString(R.string.parse_client_key));
		ParseAnalytics.trackAppOpened(getIntent());
		PushService.setDefaultPushCallback(this, MainActivity.class);
	    ParseInstallation.getCurrentInstallation().saveInBackground();	    
        
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
   
	
}
