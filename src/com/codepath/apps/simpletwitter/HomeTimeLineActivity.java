package com.codepath.apps.simpletwitter;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.codepath.apps.simpletwitter.fragements.HomeTimeLineFragment;
import com.codepath.apps.simpletwitter.fragements.MentionsTimelineFragement;
import com.codepath.apps.simpletwitter.fragements.TimeLineFragment;
import com.codepath.apps.simpletwitter.fragements.TweetComposeDialog;
import com.codepath.apps.simpletwitter.fragements.TweetComposeDialog.TweetComposeDialogListener;
import com.codepath.apps.simpletwitter.listener.TimeLineTabListener;
import com.codepath.apps.simpletwitter.models.Tweet;

public class HomeTimeLineActivity extends FragmentActivity implements TweetComposeDialogListener{
	
	ActionBar actionBar;
	TimeLineFragment tlf;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_time_line);
		
		setupTabs();
	}
	
	public void setupTabs(){
		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		Tab tab1 = actionBar
			.newTab()
			.setText("Home")
			.setIcon(R.drawable.ic_home)
			.setTag("Home")
			.setTabListener(
				new TimeLineTabListener<HomeTimeLineFragment>(R.id.fragmentContainer, this, "Home",
						HomeTimeLineFragment.class));
		
		

		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);

		Tab tab2 = actionBar
			.newTab()
			.setText("Mentions")
			.setIcon(R.drawable.ic_mentions)
			.setTag("Mentions")
			.setTabListener(
			    new TimeLineTabListener<MentionsTimelineFragement>(R.id.fragmentContainer, this, "Mentions",
			    		MentionsTimelineFragement.class));

		actionBar.addTab(tab2);
	}

	@Override
	public void finishToActivity() {
		Fragment currentFragment = getSupportFragmentManager().findFragmentByTag("Home");
		if(currentFragment!=null && currentFragment instanceof HomeTimeLineFragment){
			((HomeTimeLineFragment)currentFragment).populateHomeTimeLineSinceLatest();
		}else{
			Log.d("twit", "something");
		}
	}
	
	public void refresh(){
		String tabTag = (String)actionBar.getTabAt(actionBar.getSelectedNavigationIndex()).getTag();
		Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(tabTag);
		if(currentFragment!=null && currentFragment instanceof HomeTimeLineFragment){
			((HomeTimeLineFragment)currentFragment).populateTimeLine();
		}else if(currentFragment!=null && currentFragment instanceof MentionsTimelineFragement) {
			((MentionsTimelineFragement)currentFragment).populateTimeLine();
			
		}else{
			Log.d("twit", "something");
		}
	}
	
	public void onCompose() {
		FragmentManager fm = getSupportFragmentManager();		
		TweetComposeDialog compose = new TweetComposeDialog();
		compose.show(fm, "");
	}
	
	public void showUserProfile(){
		Intent i = new Intent(this, ProfileActivity.class);
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_time_line, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			onCompose();
			return true;
		}else if(id == R.id.refresh_settings){
			refresh();
			return true;
		}else if(id == R.id.user_settings){
			showUserProfile();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public class TweetViewClickListener implements OnItemClickListener{
		public void onItemClick(AdapterView<?> arg0, View parent, int position, long arg3) {
			Log.d("twit", "tv onclick");
			Intent i = new Intent(HomeTimeLineActivity.this, DetailedTweetActivity.class);
			Tweet result = tlf.getAdapter().getItem(position);
			i.putExtra("tweet", result);
			i.putExtra("pos", String.valueOf(position));
			startActivityForResult(i,20);
		}
	}

}
