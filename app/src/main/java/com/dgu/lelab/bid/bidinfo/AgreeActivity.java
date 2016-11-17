package com.dgu.lelab.bid.bidinfo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.tsengvn.typekit.TypekitContextWrapper;

import util.URL;

public class AgreeActivity extends AppCompatActivity implements View.OnClickListener{

    private WebView _agreeview;
    private Button _close;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt_exit:
                finish();
                break;
            default: break;
        }
    }

    public void setView(){
        _agreeview = (WebView)findViewById(R.id.agreeweb);
        _close = (Button)findViewById(R.id.bt_exit);
        _close.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agree);

        setView();

        _agreeview.loadUrl(URL.MAIN + URL.REST_AGREEMENT_VIEW);
    }
}
