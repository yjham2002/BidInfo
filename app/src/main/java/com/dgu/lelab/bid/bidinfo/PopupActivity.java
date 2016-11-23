package com.dgu.lelab.bid.bidinfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.tsengvn.typekit.TypekitContextWrapper;

public class PopupActivity extends AppCompatActivity implements View.OnClickListener{

    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;

    private Button _never, _close;
    private WebView webView;

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.never:
                prefEditor.putBoolean("popup", false);
                prefEditor.commit();
                finish();
                break;
            case R.id.close:
                finish();
                break;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        pref = getSharedPreferences("BIDINFO", MODE_PRIVATE);
        prefEditor = pref.edit();

        _never = (Button)findViewById(R.id.never);
        webView = (WebView)findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);

        webView.getSettings().setLoadWithOverviewMode(true);
        //webView.getSettings().setUseWideViewPort(true);
        webView.clearCache(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        webView.getSettings().setAllowFileAccessFromFileURLs(true);

        _close = (Button)findViewById(R.id.close);

        _never.setOnClickListener(this);
        _close.setOnClickListener(this);

        webView.loadUrl(pref.getString("popurl", "#"));

        if(pref.getString("popurl", "#").equals("#")) finish();
    }

    @Override
    public void onResume(){
        super.onResume();
    }
}
