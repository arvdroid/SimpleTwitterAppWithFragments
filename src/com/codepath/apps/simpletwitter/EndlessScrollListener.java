package com.codepath.apps.simpletwitter;

import android.util.Log;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import com.codepath.apps.simpletwitter.models.Tweet;

public abstract class EndlessScrollListener implements OnScrollListener{
	
	private int visibleThreshold = 5;
	// The current offset index of data you have loaded
	private int currentPage = 0;
	// The total number of items in the dataset after the last load
	private int previousTotalItemCount = 0;
	// True if we are still waiting for the last set of data to load.
	private boolean loading = true;
	// Sets the starting page index
	private int startingPageIndex = 0;
	
	public EndlessScrollListener() {
	}

	public EndlessScrollListener(int visibleThreshold) {
		this.visibleThreshold = visibleThreshold;
	}

	public EndlessScrollListener(int visibleThreshold, int startPage) {
		this.visibleThreshold = visibleThreshold;
		this.startingPageIndex = startPage;
		this.currentPage = startPage;
	}
	
	public abstract void onLoadMore(String maxid);
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		//Log.d("twit", "beginscrolling");
		//Log.d("twit", String.valueOf(firstVisibleItem)+ " " + String.valueOf(visibleItemCount) + " " + String.valueOf(totalItemCount));
		
		if (totalItemCount < previousTotalItemCount) {
			this.currentPage = this.startingPageIndex;
			this.previousTotalItemCount = totalItemCount;
			if (totalItemCount == 0) { this.loading = true; } 
		}
		
		/*if(!loading && totalItemCount == previousTotalItemCount){
			loading = true;
			//previousTotalItemCount = totalItemCount;
			Log.d("twit", "loading false"+ String.valueOf(loading)+ String.valueOf(previousTotalItemCount));
		}*/
		
		// If it�s still loading, we check to see if the dataset count has
		// changed, if so we conclude it has finished loading and update the current page
		// number and total item count.
		if (loading && (totalItemCount > previousTotalItemCount)) {
			loading = false;
			previousTotalItemCount = totalItemCount;
			currentPage++;
		}
		
		//Log.d("twit", String.valueOf(loading)+ String.valueOf(previousTotalItemCount));		
		
		
		// If it isn�t currently loading, we check to see if we have breached
		// the visibleThreshold and need to reload more data.
		// If we do need to reload some more data, we execute onLoadMore to fetch the data.
		if (!loading && (totalItemCount - visibleItemCount)<=(firstVisibleItem + visibleThreshold)) {
			previousTotalItemCount = totalItemCount;
			//Log.d("twit", "onLoadMore in onscroll");
			Tweet earliestTweet = (Tweet) view.getAdapter().getItem(totalItemCount-1);
			//Log.d("twit", "onLoadMore in onscroll "+ String.valueOf(earliestTweet.getUid()));
			loading = true;
			if(earliestTweet!=null)
				onLoadMore(String.valueOf(earliestTweet.getUid()));
		    
		}
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {}

}