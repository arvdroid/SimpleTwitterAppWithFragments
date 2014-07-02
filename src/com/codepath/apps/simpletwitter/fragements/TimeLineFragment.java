package com.codepath.apps.simpletwitter.fragements;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.codepath.apps.simpletwitter.DetailedTweetActivity;
import com.codepath.apps.simpletwitter.EndlessScrollListener;
import com.codepath.apps.simpletwitter.R;
import com.codepath.apps.simpletwitter.TweetArrayAdapter;
import com.codepath.apps.simpletwitter.TwitterClient;
import com.codepath.apps.simpletwitter.TwitterClientApp;
import com.codepath.apps.simpletwitter.models.Tweet;
import com.codepath.apps.simpletwitter.models.User;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public abstract class TimeLineFragment extends Fragment {
	private ArrayList<Tweet> tweets;
	protected ArrayAdapter<Tweet> lvAdapter;
	protected PullToRefreshListView lvTweets;
	private ProgressBar pb;
	protected TwitterClient client;
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d("twit", "time line fragment on create");
		super.onCreate(savedInstanceState);
		client = TwitterClientApp.getRestClient();
		tweets = new ArrayList<Tweet>();
		lvAdapter = new TweetArrayAdapter(getActivity(), tweets);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("twit", "time line fragement on create view");
		View v = inflater.inflate(R.layout.fragment_tweet_timeline, container, false);
		
		pb = (ProgressBar)v.findViewById(R.id.pbLoading);
		showProgressBar();
		lvTweets = (PullToRefreshListView) v.findViewById(R.id.lvTweels);
		lvTweets.setAdapter(lvAdapter);
		
		lvTweets.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Populate since latest tweet
            	populateTimeLineSinceLatest(getLatestTweetId());
            }
        });
		
		lvTweets.setOnScrollListener(new EndlessScrollListener(){
			@Override
			public void onLoadMore(String maxId) {
				loadMore(maxId);
			}			
		});
		
		lvTweets.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View parent, int position, long arg3) {
				Intent i = new Intent(getActivity(), DetailedTweetActivity.class);
				Tweet result = tweets.get(position);
				i.putExtra("tweet", result);
				i.putExtra("pos", String.valueOf(position));
				startActivityForResult(i,20);
			}			
		});
		
		populateTimeLine();
		return v;
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 20){
			if(data!=null){
				String pos = data.getStringExtra("pos");
				tweets.remove(Integer.parseInt(pos));
				lvAdapter.notifyDataSetChanged();
			}
		}
	}
	
	public PullToRefreshListView getTweetsView(){
		return lvTweets;
	}
	
	public ArrayAdapter<Tweet> getAdapter(){
		return lvAdapter;
	}
	
	public void addAll(ArrayList<Tweet> tweets){
		lvAdapter.addAll(tweets);
	}
	
	public void addLatestTweets(ArrayList<Tweet> tweets){		
		this.tweets.addAll(0, tweets);
		lvAdapter.notifyDataSetChanged();
	}
	
	public void clearAdapter(){
		lvAdapter.clear();
	}
	
	public void clearDB() {
		Tweet.deleteAllTweets();
		User.deleteAllUsers();
	}
	
	public String getLatestTweetId(){
		return String.valueOf(lvAdapter.getItem(0).getUid()+10);
	}
	
	public void showShortToast(String msg){
		Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
	}
	
	public void populateHomeLineFromDB(){
		List<Tweet> tweets = Tweet.getAll();
		for(Tweet t : tweets){
			long userId = t.getUserId();
			User u = User.getUser(userId).get(0);
			t.setUser(u);
		}
		lvAdapter.addAll(tweets);
	}
	
	public void showProgressBar(){
		pb.setVisibility(ProgressBar.VISIBLE);
	}
	
	public void clearProgressBar(){
		pb.setVisibility(ProgressBar.INVISIBLE);
	}
	
	public abstract void populateTimeLine();
	
	public abstract void populateTimeLineSinceLatest(String uid);
	
	public abstract void loadMore(String maxId);

}
