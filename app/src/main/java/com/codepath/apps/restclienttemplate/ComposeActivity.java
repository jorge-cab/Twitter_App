package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    public static final int MAX_TWEET_LENGTH = 280;
    public static final String TAG = "ComposeActivity";

    EditText etCompose;
    Button btnTweet;
    TwitterClient client;
    TextInputLayout tilTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);


        client = TwitterApp.getRestClient(this);
        etCompose = findViewById(R.id.etCompose);
        btnTweet = findViewById(R.id.btnTweetF);
        tilTweet = findViewById(R.id.tilTweet);
        tilTweet.setCounterMaxLength(MAX_TWEET_LENGTH);

        if (getIntent().hasExtra("screen_name")) {
            etCompose.setText("@" + getIntent().getStringExtra("screen_name") + " ");
            Long tweetId = getIntent().getLongExtra("id", 0);
            // Set click listener on button
            btnTweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String tweetContent = etCompose.getText().toString();
                    if (tweetContent.isEmpty()) {
                        Toast.makeText(ComposeActivity.this, "Sorry, your tweet cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                    if (tweetContent.length() > MAX_TWEET_LENGTH) {
                        Toast.makeText(ComposeActivity.this, "Sorry, your tweet is too long", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(ComposeActivity.this, tweetContent, Toast.LENGTH_LONG).show();
                    // Make and API call to Twitter to publish the tweet
                    client.replyTweet(tweetId ,tweetContent, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i(TAG, "onSuccess to publish tweet");
                            try {
                                Tweet tweet = Tweet.fromJson(json.jsonObject);
                                Log.i(TAG, "Published tweet says: " + tweet.body);
                                Intent intent = new Intent(ComposeActivity.this, TimelineActivity.class);
                                intent.putExtra("tweet", Parcels.wrap(tweet));
                                setResult(RESULT_OK, intent);
                                // Closes activity, pass data to parent
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e(TAG, "onFailure to publish tweet", throwable);
                        }
                    });

                }
            });
        } else {
            // Set click listener on button
            btnTweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String tweetContent = etCompose.getText().toString();
                    if (tweetContent.isEmpty()) {
                        Toast.makeText(ComposeActivity.this, "Sorry, your tweet cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                    if (tweetContent.length() > MAX_TWEET_LENGTH) {
                        Toast.makeText(ComposeActivity.this, "Sorry, your tweet is too long", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(ComposeActivity.this, tweetContent, Toast.LENGTH_LONG).show();
                    // Make and API call to Twitter to publish the tweet
                    client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i(TAG, "onSuccess to publish tweet");
                            try {
                                Tweet tweet = Tweet.fromJson(json.jsonObject);
                                Log.i(TAG, "Published tweet says: " + tweet.body);
                                Intent intent = new Intent(ComposeActivity.this, TimelineActivity.class);
                                intent.putExtra("tweet", Parcels.wrap(tweet));
                                setResult(RESULT_OK, intent);
                                // Closes activity, pass data to parent
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e(TAG, "onFailure to publish tweet", throwable);
                        }
                    });

                }
            });
        }


    }
}