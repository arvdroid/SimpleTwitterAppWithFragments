package com.codepath.apps.simpletwitter;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.codepath.apps.simpletwitter.fragements.UserProfileFragment;
import com.codepath.apps.simpletwitter.fragements.UserTimeLineFragment;
import com.codepath.apps.simpletwitter.models.User;

public class ProfileActivity extends FragmentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_activity);
		User result = (User) getIntent().getSerializableExtra("user");
		UserProfileFragment usrProFrag = (UserProfileFragment) 
	            getSupportFragmentManager().findFragmentById(R.id.fragment11);
		
		UserTimeLineFragment usrTimeLineFrag = (UserTimeLineFragment) 
	            getSupportFragmentManager().findFragmentById(R.id.fragment1);
		
		if(result!=null){			
			usrProFrag.updateUserProfileView(result.getScreenName());
			usrTimeLineFrag.updateUserProfileView(result.getScreenName());
		}
		else{
			usrProFrag.updateUserProfileView(null);
			usrTimeLineFrag.updateUserProfileView(null);
		}
	}
}
