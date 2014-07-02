package com.codepath.apps.simpletwitter.fragements;

import java.util.ArrayList;

import org.json.JSONArray;

import com.codepath.apps.simpletwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.os.Bundle;
import android.util.Log;

public class MentionsTimelineFragement extends TimeLineFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	public void populateTimeLine() {
		showProgressBar();
		client.getMentionsTimeLine(new JsonHttpResponseHandler(){
			
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

	@Override
	public void populateTimeLineSinceLatest(String uid) {
		Log.d("twit", "add all "+ uid);
		client.getMentionTimelineSinceLatest(new JsonHttpResponseHandler(){

			@Override
			public void onSuccess(JSONArray jsonArray) {
				Log.d("twit", "mention refresh "+jsonArray.length());
				ArrayList<Tweet> latestT = Tweet.getFromJsonArray(jsonArray,"");
				Log.d("twit", "mention refresh "+latestT.size());
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
		}, uid, "");

	}

	@Override
	public void loadMore(final String maxId) {
		client.geMoretMentionTimeLine(new JsonHttpResponseHandler(){

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
