package com.codepath.apps.simpletwitter.fragements;

import java.util.ArrayList;

import org.json.JSONArray;

import android.os.Bundle;

import com.codepath.apps.simpletwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class HomeTimeLineFragment extends TimeLineFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
	}
	
	public void populateTimeLine(){		
		
		client.getHomeTimeLine(new JsonHttpResponseHandler(){
			
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
		});
	}
	
	public void populateTimeLineSinceLatest(String uid){
		client.getHomeTimeLineSincelatest(new JsonHttpResponseHandler(){
			
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
	
	public void populateHomeTimeLineSinceLatest(){
		client.getHomeTimeLineSincelatest(new JsonHttpResponseHandler(){
			
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
		}, getLatestTweetId());
	}
	
	public void loadMore(final String maxId){
		client.geMoretHomeTimeLine(new JsonHttpResponseHandler(){

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
}
