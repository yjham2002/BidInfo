package com.dgu.lelab.bid.bidinfo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class HowActivity extends AppCompatActivity implements View.OnClickListener{

    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;

    private Button never, close;
    private ImageView iv;

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.never:
                if(getIntent().getExtras().getInt("what", 0) == 0){
                    prefEditor.putBoolean("privatehow", false);
                }else{
                    prefEditor.putBoolean("detailhow", false);
                }
                prefEditor.commit();
                finish();
                break;
            case R.id.close:
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how);

        pref = getSharedPreferences("BIDINFO", MODE_PRIVATE);
        prefEditor = pref.edit();

        never = (Button)findViewById(R.id.never);
        never.setOnClickListener(this);
        close = (Button)findViewById(R.id.close);
        close.setOnClickListener(this);
        iv = (ImageView)findViewById(R.id.how);

        if(getIntent().getExtras().getInt("what", 0) == 0){
            iv.setImageDrawable(getResources().getDrawable(R.drawable.tutorial_private));
        }else{
            iv.setImageDrawable(getResources().getDrawable(R.drawable.tutorial_detail));
        }

    }
}
