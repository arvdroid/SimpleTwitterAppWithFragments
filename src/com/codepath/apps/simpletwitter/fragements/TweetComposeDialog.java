package com.codepath.apps.simpletwitter.fragements;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.simpletwitter.HomeTimeLineActivity;
import com.codepath.apps.simpletwitter.R;
import com.codepath.apps.simpletwitter.TwitterClient;
import com.codepath.apps.simpletwitter.TwitterClientApp;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class TweetComposeDialog extends DialogFragment {
	
	public TweetComposeDialog() {}
	
	TwitterClient client;
	EditText msg;
	Button composeB;
	TextView tweetCharCount;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		Dialog dialog = getDialog();
		Window window = dialog.getWindow();
		View view = inflater.inflate(R.layout.activity_compose, container);
		window.requestFeature(Window.FEATURE_NO_TITLE);
		/*window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);*/
		
		
		client = TwitterClientApp.getRestClient();
		msg = (EditText) view.findViewById(R.id.cvTweet);
		tweetCharCount = (TextView) view.findViewById(R.id.tvTweetCharCount);
		
		msg.requestFocus();
		msg.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, 
					int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, 
					int before, int count) {
				String remaining = String.valueOf(140-msg.getText().toString().length());
				tweetCharCount.setText(remaining);
			}
		});
	        
		composeB =  (Button) view.findViewById(R.id.cvTweetSubmit);
		composeB.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onClickSubmitTweet(v);
			}
		});
		
		return view;
	}
	
	public void onClickSubmitTweet(View v){
		String m = msg.getText().toString();
		client.postTweet(new AsyncHttpResponseHandler(){
			
			@Override
			public void onSuccess(String arg0) {
				Toast.makeText(getActivity(), "Tweet posted", Toast.LENGTH_SHORT).show();
				HomeTimeLineActivity act = (HomeTimeLineActivity)getActivity();
				act.finish();
				dismiss();
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				Toast.makeText(getActivity(), "Failed to post tweet", Toast.LENGTH_SHORT).show();
			}
		}, m);		
	}
	
	public interface TweetComposeDialogListener {
    	void finishToActivity();
    }

}
