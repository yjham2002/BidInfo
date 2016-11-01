package com.dgu.lelab.bid.bidinfo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1234;

    private String PHONE = "010-0000-0000";

    private TextView _phone, _name;
    private ImageView _close;

    public void setView(){
        _phone = (TextView)findViewById(R.id.phone);
        _phone.setOnClickListener(this);
        _name = (TextView)findViewById(R.id.name);
        _close = (ImageView)findViewById(R.id.closeButton);
        _close.setOnClickListener(this);

        Intent cmd = getIntent();
        Bundle cmdMsg = cmd.getExtras();
        if(cmdMsg == null) finish();
        else {
            _name.setText(cmdMsg.getString("name"));
        }
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.phone:
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                }else {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + PHONE));
                    startActivity(intent);
                }
                break;
            case R.id.closeButton:
                finish();
                break;
            default: break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setView();
    }
}
