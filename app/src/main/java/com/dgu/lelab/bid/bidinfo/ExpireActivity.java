package com.dgu.lelab.bid.bidinfo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.HashMap;

import util.Communicator;
import util.URL;

public class ExpireActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;

    private EditText _name;
    private Button _submit, _close;

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.expr_submit:
                onSubmit(_name.getText().toString());
                break;
            case R.id.bt_exit:
                finish();
                break;
            default: break;
        }
    }

    private void onSubmit(String name){
        if(name.length() < 2 || name.length() > 10){
            Toast.makeText(getApplicationContext(), "정상적인 이름을 입력하세요", Toast.LENGTH_LONG).show();
            return;
        }else{
            HashMap<String, String> dataSet = new HashMap<>();
            dataSet.put("Draw", name);
            dataSet.put("mid", Integer.toString(pref.getInt("id", 0)));
            new Communicator().postHttp(URL.MAIN + URL.REST_USER_REQ, dataSet, new Handler(){
                @Override
                public void handleMessage(Message msg){
                    Toast.makeText(getApplicationContext(), "요청이 완료되었습니다.\n관리자의 확인 이후 유료회원으로 전환됩니다.", Toast.LENGTH_LONG).show();
                    finish();
                }
            });
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expire);

        pref = getSharedPreferences("BIDINFO", MODE_PRIVATE);
        prefEditor = pref.edit();

        _name = (EditText)findViewById(R.id.expire_draw);
        _submit = (Button)findViewById(R.id.expr_submit);
        _submit.setOnClickListener(this);
        _close = (Button)findViewById(R.id.bt_exit);
        _close.setOnClickListener(this);

    }
}
