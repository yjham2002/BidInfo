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
import android.widget.Button;
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

public class CompanyActivity extends AppCompatActivity implements View.OnClickListener{

    private GridAdapter adapter1;
    private ExpandableHeightGridView _hid;
    private List<String> mList1 = new ArrayList<>();

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1234;
    private String PHONE = "";
    private TextView _name, _rnum, _rprt, _charge, _addr, _email, _url;
    private Button _exit, _call;

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.bt_exit:
                finish();
                break;
            case R.id.redirect:
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                }else {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + PHONE));
                    startActivity(intent);
                }
                break;
            default: break;
        }
    }

    public void refresh() {
        mList1.clear();
        Intent cmd = getIntent();
        Bundle cmdMsg = cmd.getExtras();
        if (cmdMsg == null) {
            Toast.makeText(getApplicationContext(), "회사 정보를 불러올 수 없습니다.", Toast.LENGTH_LONG).show();
            finish();
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("회사 정보를 불러오는 중...");
            progressDialog.show();
            Communicator.getHttp(URL.MAIN + URL.REST_COMPANY_ONE + cmdMsg.getInt("cid"), new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    String jsonString = msg.getData().getString("jsonString");
                    try{
                        JSONArray json_arr = new JSONArray(jsonString);
                        JSONObject json_list = json_arr.getJSONObject(0);

                        _name.setText(json_list.getString("Name"));
                        _rnum.setText(json_list.getString("Rnum"));
                        _rprt.setText(json_list.getString("Rprt"));
                        _charge.setText(json_list.getString("Charge"));
                        _addr.setText(json_list.getString("Addr"));
                        _email.setText(json_list.getString("Email"));
                        _url.setText(json_list.getString("Expl"));
                        PHONE = json_list.getString("Phone");

                        if(!json_list.getString("hid").equals("null")) {
                            List<String> list = new ArrayList<String>(Arrays.asList(json_list.getString("hid").split("\\|")));
                            mList1.clear();
                            mList1.addAll(list);
                            adapter1.notifyDataSetChanged();
                        }

                    }catch (JSONException e){
                        Toast.makeText(getApplicationContext(), "회사 정보를 불러올 수 없습니다.", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }finally {
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }

    public void initView(){
        _exit = (Button)findViewById(R.id.bt_exit);
        _exit.setOnClickListener(this);
        _call = (Button)findViewById(R.id.redirect);
        _call.setOnClickListener(this);

        _name = (TextView)findViewById(R.id.company_name);
        _rnum = (TextView)findViewById(R.id.company_rnum);
        _rprt = (TextView)findViewById(R.id.company_rprt);
        _charge = (TextView)findViewById(R.id.company_charge);
        _addr = (TextView)findViewById(R.id.company_addr);
        _email = (TextView)findViewById(R.id.company_email);
        _url = (TextView)findViewById(R.id.company_url);

        adapter1 = new GridAdapter(this, R.layout.grid_item_detail, mList1);
        _hid = (ExpandableHeightGridView)findViewById(R.id.hid);
        _hid.setExpanded(true);
        _hid.setAdapter(adapter1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);

        initView();

        refresh();

    }
}
