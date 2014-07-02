package com.codepath.apps.simpletwitter.fragements;

import java.util.ArrayList;

import org.json.JSONArray;

import android.os.Bundle;

import com.codepath.apps.simpletwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class UserTimeLineFragment extends TimeLineFragment {
	
	String userName;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	public void updateUserProfileView(String user){
		userName = user;
		if(userName!=null)
			populateTimeLine(userName);
		else
			populateTimeLine("");
	}
	
	public void populateTimeLine(String username) {
		showProgressBar();
		client.userTimeline(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray jsonArray) {				
				clearAdapter();
				clearDB();				
				addAll(Tweet.getFromJsonArray(jsonArray,""));
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				showShortToast("No Connection so loading from db");
				populateHomeLineFromDB();
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
				clearProgressBar();
			}
		},username);		
	}

	@Override
	public void populateTimeLineSinceLatest(String uid) {
		client.userTimeLineSinceLatest(new JsonHttpResponseHandler(){
			
			@Override
			public void onSuccess(JSONArray jsonArray) {
				ArrayList<Tweet> latestT = Tweet.getFromJsonArray(jsonArray,"");
				addLatestTweets(latestT);
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				showShortToast("Failed to load tweets");
			}
			
			@Override
			public void onFinish() {
				lvTweets.onRefreshComplete();
				super.onFinish();
			}
		}, uid);

	}

	@Override
	public void loadMore(final String maxId) {
		client.geMoretUserTimeLine(new JsonHttpResponseHandler(){

			@Override
			public void onSuccess(JSONArray jsonArray) {
				addAll(Tweet.getFromJsonArray(jsonArray, maxId));						
			}

			@Override
			public void onFailure(Throwable e, String s) {
				showShortToast("Failed to load tweets");
			}
		}, maxId);
	}

	@Override
	public void populateTimeLine() {
		// TODO Auto-generated method stub
	}

}
