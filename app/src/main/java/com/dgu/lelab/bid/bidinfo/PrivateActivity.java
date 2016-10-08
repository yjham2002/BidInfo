package com.dgu.lelab.bid.bidinfo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class PrivateActivity extends AppCompatActivity implements View.OnClickListener{

    private Button _exit, _submit;

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.private_submit:
                break;
            case R.id.private_cancel:
                finish();
                break;
            default: break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private);
        _exit = (Button)findViewById(R.id.private_cancel);
        _submit = (Button)findViewById(R.id.private_submit);
        _exit.setOnClickListener(this);
        _submit.setOnClickListener(this);
    }
}
