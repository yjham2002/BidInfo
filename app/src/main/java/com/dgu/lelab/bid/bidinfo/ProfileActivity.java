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
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import util.Communicator;
import util.URL;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1234;

    private String PHONE = "010-0000-0000";

    private TextView _phone, _name, _addr;
    private ImageView _close;
    private ExpandableHeightGridView _content;
    private GridAdapter adapter1;
    private List<String> mList1 = new ArrayList<>();

    public void setView(){
        mList1.clear();

        adapter1 = new GridAdapter(this, R.layout.grid_item_detail, mList1);
        _content = (ExpandableHeightGridView) findViewById(R.id.content);
        _content.setExpanded(true);
        _content.setAdapter(adapter1);
        _addr = (TextView) findViewById(R.id.addr);
        _addr.setOnClickListener(this);
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
            progressDialog.setMessage("정보를 불러오는 중...");
            progressDialog.show();
            Communicator.getHttp(URL.MAIN + URL.REST_USER_ONE + cmdMsg.getInt("mid"), new Handler(){
                @Override
                public void handleMessage(Message msg){
                    String jsonString = msg.getData().getString("jsonString");
                    try{
                        JSONArray json_arr = new JSONArray(jsonString);
                        JSONObject json_list = json_arr.getJSONObject(0);
                        _name.setText(json_list.getString("Name"));

                        SpannableString content = new SpannableString(json_list.getString("Uid"));
                        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                        _addr.setText(content);

                        _phone.setText(json_list.getString("Phone"));
                        PHONE = json_list.getString("Phone");

                        mList1.clear();

                        if(!json_list.getString("hid").equals("null")) {
                            List<String> list = new ArrayList<String>(Arrays.asList(json_list.getString("hid").split("\\|")));
                            mList1.clear();
                            mList1.addAll(list);
                            adapter1.notifyDataSetChanged();
                        }
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
            case R.id.addr:
                Uri uri = Uri.parse("mailto:"+_addr.getText().toString());
                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                startActivity(it);
                break;
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
