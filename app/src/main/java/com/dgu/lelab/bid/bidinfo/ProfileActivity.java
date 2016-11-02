package com.dgu.lelab.bid.bidinfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import util.Communicator;
import util.URL;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1234;

    private String PHONE = "010-0000-0000";

    private TextView _phone, _name, _content;
    private ImageView _close;

    public void setView(){
        _content = (TextView)findViewById(R.id.content);
        _phone = (TextView)findViewById(R.id.phone);
        _phone.setOnClickListener(this);
        _name = (TextView)findViewById(R.id.name);
        _close = (ImageView)findViewById(R.id.closeButton);
        _close.setOnClickListener(this);

        Intent cmd = getIntent();
        Bundle cmdMsg = cmd.getExtras();
        if(cmdMsg == null) {
            Toast.makeText(getApplicationContext(), "사용자 정보를 불러올 수 없습니다.", Toast.LENGTH_LONG).show();
            finish();
        }
        else {
            final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("회사 목록을 불러오는 중...");
            progressDialog.show();
            Communicator.getHttp(URL.MAIN + URL.REST_USER_ONE + cmdMsg.getInt("mid"), new Handler(){
                @Override
                public void handleMessage(Message msg){
                    String jsonString = msg.getData().getString("jsonString");
                    try{
                        JSONArray json_arr = new JSONArray(jsonString);
                        JSONObject json_list = json_arr.getJSONObject(0);
                        _name.setText(json_list.getString("Name"));
                        _phone.setText(json_list.getString("Phone"));
                        PHONE = json_list.getString("Phone");
                        String hash = "#" + json_list.getString("hid").trim().replaceAll("\\|", " #");
                        _content.setText(hash);
                    }catch (JSONException e){
                        Toast.makeText(getApplicationContext(), "사용자 정보를 불러올 수 없습니다.", Toast.LENGTH_LONG).show();
                        finish();
                    }finally {
                        progressDialog.dismiss();
                    }
                }
            });

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
