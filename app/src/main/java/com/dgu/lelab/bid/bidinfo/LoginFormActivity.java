package com.dgu.lelab.bid.bidinfo;

import android.app.Activity;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import util.Communicator;
import util.URL;

public class LoginFormActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;
    private Button _login, _notyet;
    private EditText _account, _password;

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.bt_sign_f:
                verifyLogin(_account.getText().toString(), _password.getText().toString());
                break;
            case R.id.bt_signup_f:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            default: break;
        }
    }

    public void verifyLogin(String email, String password){
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("로그인하는 중...");
        progressDialog.show();

        _login.setEnabled(false);
        if(email.equals("")) {
            _account.setError("이메일을 입력하세요");
            _login.setEnabled(true);
            progressDialog.dismiss();
            return;
        }
        else {
            _account.setError(null);
            _login.setEnabled(true);
            progressDialog.dismiss();
        }
        if(password.equals("")) {
            _password.setError("비밀번호를 입력하세요");
            _login.setEnabled(true);
            progressDialog.dismiss();
            return;
        }
        else {
            _password.setError(null);
            _login.setEnabled(true);
            progressDialog.dismiss();
        }
        final String tempMail = email;
        HashMap<String, String> data = new HashMap<>();
        data.put("Uid", email);
        data.put("Pwd", password);
        new Communicator().postHttp(URL.MAIN + URL.REST_USER_LOGIN, data, new Handler(){
            @Override
            public void handleMessage(Message msg){
                String jsonString = msg.getData().getString("jsonString");
                try {
                    JSONArray json_arr = new JSONArray(jsonString);
                    JSONObject json_list = json_arr.getJSONObject(0);

                    Intent mainCall = new Intent(LoginFormActivity.this, MainActivity.class);
                    mainCall.putExtra("user_account", json_list.getString("Uid"));
                    mainCall.putExtra("user_name", json_list.getString("Name"));

                    prefEditor.putString("Uid", json_list.getString("Uid"));
                    prefEditor.putString("Name", json_list.getString("Name"));
                    prefEditor.putString("hid", json_list.getString("hid"));
                    prefEditor.putInt("id", json_list.getInt("id"));
                    prefEditor.commit();
                    Log.e("hid", pref.getString("hid", "#"));
                    progressDialog.dismiss();
                    _login.setEnabled(true);
                    startActivity(mainCall);
                    finish();
                }catch (JSONException e){
                    Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    _login.setEnabled(true);
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);

        pref = getSharedPreferences("BIDINFO", MODE_PRIVATE);
        prefEditor = pref.edit();

        _login = (Button)findViewById(R.id.bt_sign_f);
        _login.setOnClickListener(this);
        _notyet = (Button)findViewById(R.id.bt_signup_f);
        _notyet.setOnClickListener(this);
        _account = (EditText)findViewById(R.id.log_mail);
        _password = (EditText)findViewById(R.id.log_pass);

        _account.setText("yjham2002@gmail.com");
        _password.setText("gpswpf12!");
    }
}
