package se.kd35a.blekingskaSangbok;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class BlekingskaSangbokActivity extends TabActivity {
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;
		
		intent = new Intent().setClass(this, StartActivity.class);
		spec = tabHost.newTabSpec("start").setIndicator("Start")
						.setContent(intent);
		tabHost.addTab(spec);
		
		intent = new Intent().setClass(this, SearchActivity.class);
		spec = tabHost.newTabSpec("search").setIndicator("Sök")
						.setContent(intent);
		tabHost.addTab(spec);
		
		intent = new Intent().setClass(this, ListActivity.class);
		spec = tabHost.newTabSpec("list").setIndicator("Lista")
						.setContent(intent);
		tabHost.addTab(spec);
		
		intent = new Intent().setClass(this, AboutActivity.class);
		spec = tabHost.newTabSpec("about").setIndicator("Om")
						.setContent(intent);
		tabHost.addTab(spec);
		
		tabHost.setCurrentTab(0);
	}

}