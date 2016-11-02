package com.dgu.lelab.bid.bidinfo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class AgreeActivity extends AppCompatActivity implements View.OnClickListener{

    private Button _close;

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
        _close = (Button)findViewById(R.id.bt_exit);
        _close.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agree);

        setView();
    }
}
