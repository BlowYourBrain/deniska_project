package com.example.notebook.denis_project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.net.URL;
import java.util.Formatter;

public class LoginActivity extends AppCompatActivity {

    public static final int RESULT_OK = 100;
    public static final String ACCESS_TOKEN = "token";

    WebView mLoginWebView;
    ProgressBar mProgressBar;

    //Эти строки обычно описываются в strings.xml но и так сойдёт
    public final String client_id = "3372888ec3ab4f678722c665f8d96357";
    public final String client_secret = "5ca7ede9978b4220bc6eec33a053c4dd";
    public final String rediret_uri = "https://hardboy21ru.myjetbrains.com/youtrack/";

    private String template_url = "https://api.instagram.com/oauth/authorize/?client_id=%s&redirect_uri=%s&response_type=token";
    private String login_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Formatter formatter = new Formatter();
        formatter.format(template_url, client_id, rediret_uri);
        login_url = formatter.toString();
        mLoginWebView = (WebView) findViewById(R.id.login_webview);
        mLoginWebView.getSettings().setJavaScriptEnabled(true);
        mLoginWebView.loadUrl(login_url);
        mLoginWebView.setWebViewClient(new AuthClient());
        mProgressBar = (ProgressBar) findViewById(R.id.loading_progress);
    }

    private class AuthClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            returnData(url);
            view.loadUrl(url);
            return true;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            returnData(url);
            view.loadUrl(url);
            return true;
        }

        private void returnData(String url) {
            if (url.startsWith(rediret_uri)) {
                try {
                    Log.d("redirect_url", url);
                    Intent intent = new Intent();
                    String[] parts = url.split("=");
                    intent.putExtra(ACCESS_TOKEN, parts[1]);
                    setResult(RESULT_OK, intent);
                    finish();
                }catch (Exception e){
                    Log.d("error", e.getLocalizedMessage());
                }
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    public static void getToken(Activity activity) {
        activity.startActivityForResult(new Intent(activity, LoginActivity.class), RESULT_OK);
    }

}
