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

import util.Communicator;
import util.URL;

public class CompanyActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1234;
    private String PHONE = "";
    private TextView _title, _regNum, _repCharge, _addr, _phone, _email, _div, _expl, _keyword;
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
        Intent cmd = getIntent();
        Bundle cmdMsg = cmd.getExtras();
        if (cmdMsg == null) {
            Toast.makeText(getApplicationContext(), "사용자 정보를 불러올 수 없습니다.", Toast.LENGTH_LONG).show();
            finish();
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("회사 정보를 불러오는 중...");
            progressDialog.show();
            Communicator.getHttp(URL.MAIN + URL.REST_COMPANY_ONE + cmdMsg.getInt("cid"), new Handler() {
                @Override
                public void handleMessage(Message msg) {
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);

        _exit = (Button)findViewById(R.id.bt_exit);
        _exit.setOnClickListener(this);
        _call = (Button)findViewById(R.id.redirect);
        _call.setOnClickListener(this);

        _title = (TextView)findViewById(R.id.titleText);
        _regNum = (TextView)findViewById(R.id.regNum);
        _phone = (TextView)findViewById(R.id.Phone);
        _email = (TextView)findViewById(R.id.email);
        _expl = (TextView)findViewById(R.id.expl);
        _repCharge = (TextView)findViewById(R.id.repCharge); // 대표자 : 대표자 / 담당자 : 담당자
        _addr = (TextView)findViewById(R.id.addr);
        _div = (TextView)findViewById(R.id.div); // 업종/공종
        _keyword = (TextView)findViewById(R.id.hid);

        refresh();

    }
}
