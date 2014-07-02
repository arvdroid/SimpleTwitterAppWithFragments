package com.codepath.apps.simpletwitter.fragements;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.simpletwitter.R;
import com.codepath.apps.simpletwitter.TwitterClient;
import com.codepath.apps.simpletwitter.TwitterClientApp;
import com.codepath.apps.simpletwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UserProfileFragment extends Fragment {
	
	TextView pUserName;
	TextView pUserTag;
	TextView lblTweets;
	TextView cntTweets;
	TextView lblFollowing;
	TextView cntFollowing;
	TextView lblFollowers;
	TextView cntFollowers;
	ImageView imgView;
	
	String userName;
	
	TwitterClient client;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		client = TwitterClientApp.getRestClient();
		View v = inflater.inflate(R.layout.user_profile_fragment, container, false);
		
		pUserName = (TextView) v.findViewById(R.id.tvPUserName);
		pUserTag = (TextView) v.findViewById(R.id.tvPUserTag);
		cntTweets = (TextView) v.findViewById(R.id.tvTweetsCnt);
		lblTweets = (TextView) v.findViewById(R.id.tvTweetsLbl);
		cntTweets = (TextView) v.findViewById(R.id.tvTweetsCnt);
		lblFollowing = (TextView) v.findViewById(R.id.tvFollowingLbl);
		cntFollowing = (TextView) v.findViewById(R.id.tvFollowingCnt);
		lblFollowers = (TextView) v.findViewById(R.id.tvFollowersLbl);
		cntFollowers = (TextView) v.findViewById(R.id.tvFollowersCnt);
		
		imgView = (ImageView) v.findViewById(R.id.pUserImageView);
			
		return v;
	}
	
	public void updateUserProfileView(String user){
		userName = user;
		if(userName!=null)
			setTweetUserProfile(userName);
		else
			setCurrentUserProfile();
	}
	
	private void setCurrentUserProfile(){
		client.currentUserProfile(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int arg0, JSONObject arg1) {
				User u = User.getFromJson(arg1);
				updateUserProfile(u);
			}			
			
			@Override
			public void onFailure(Throwable e, String s) {
				Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private void setTweetUserProfile(String userName){
		client.userProfile(new JsonHttpResponseHandler(){
			
			@Override
			public void onSuccess(int arg0, JSONObject arg1) {
				User u = User.getFromJson(arg1);
				updateUserProfile(u);
			}			
			
			@Override
			public void onFailure(Throwable e, String s) {
				Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
			}
		}, userName);
	}
	
	private void updateUserProfile(User u){
		imgView.setImageResource(android.R.color.transparent);
		ImageLoader loader = ImageLoader.getInstance();		
		loader.displayImage(u.getProfileImageUrl(), imgView);
		
		pUserName.setText(u.getName());
		pUserTag.setText("@"+u.getScreenName());
		cntTweets.setText(String.valueOf(u.getTweetsCount()));
		cntFollowing.setText(String.valueOf(u.getFollowingCount()));
		cntFollowers.setText(String.valueOf(u.getFollowersCount()));
	}
}
