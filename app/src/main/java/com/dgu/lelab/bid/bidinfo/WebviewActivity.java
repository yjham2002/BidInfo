package com.dgu.lelab.bid.bidinfo;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class WebviewActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String DEF_URL = "http://www.naver.com";

    private TextView _titleView;
    private WebView webView;
    private ImageView bt_close, bt_share;
    private String webURL;

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.bt_share:
                String shareBody = webURL;
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name) + "에서 공유한 정보 [" + _titleView.getText() + "]");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
                break;
            case R.id.bt_close:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        webView = (WebView)findViewById(R.id.webView);
        bt_close = (ImageView) findViewById(R.id.bt_close);
        bt_share = (ImageView) findViewById(R.id.bt_share);
        _titleView = (TextView) findViewById(R.id.textView);
        bt_close.setOnClickListener(this);
        bt_share.setOnClickListener(this);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url){
                String titleName = view.getTitle();
                if(view.getTitle().length() > 20) titleName = titleName.substring(0, 22) + "...";
                webURL = view.getUrl();
                _titleView.setText(titleName);
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);

        Intent cmd = getIntent();
        Bundle cmdMsg = cmd.getExtras();
        if(cmdMsg == null) webURL = DEF_URL;
        else webURL = cmdMsg.getString("URL");

        webView.loadUrl(webURL);
    }
}
