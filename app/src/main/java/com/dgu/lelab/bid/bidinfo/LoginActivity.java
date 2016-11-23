package com.dgu.lelab.bid.bidinfo;


import android.app.ProgressDialog;
import android.content.Context;
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

import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import util.Communicator;
import util.URL;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;

    private Button _login, _signup, _seeing;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.bt_see:
                fakeLogin();
                break;
            case R.id.bt_signin:
                Intent i = new Intent(this, LoginFormActivity.class);
                startActivity(i);
                finish(); // Should be deleted
                //signIn();
                break;
            case R.id.bt_signup_ent:
                startActivity(new Intent(this, ApplyActivity.class));
                break;
            default: break;
        }
    }

    public void fakeLogin(){
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("둘러보기 데이터를 로드하는 중...");
        progressDialog.show();

        _login.setEnabled(false);

        HashMap<String, String> data = new HashMap<>();
        data.put("Uid", "testmode@test.com");
        data.put("Pwd", "testmode");
        new Communicator().postHttp(URL.MAIN + URL.REST_USER_LOGIN, data, new Handler(){
            @Override
            public void handleMessage(Message msg){
                String jsonString = msg.getData().getString("jsonString");
                try {
                    JSONArray json_arr = new JSONArray(jsonString);
                    JSONObject json_list = json_arr.getJSONObject(0);

                    Intent mainCall = new Intent(LoginActivity.this, MainActivity.class);
                    mainCall.putExtra("user_account", getResources().getString(R.string.app_name) + " 둘러보기 모드");
                    mainCall.putExtra("user_name", json_list.getString("Name"));

                    MainActivity.userId = json_list.getInt("id");

                    prefEditor.putString("Uid", json_list.getString("Uid"));
                    prefEditor.putString("Name", json_list.getString("Name"));
                    prefEditor.putString("Pwd", json_list.getString("Pwd"));
                    prefEditor.putString("hid", json_list.getString("hid"));
                    prefEditor.putInt("cid", json_list.getInt("symbol"));
                    prefEditor.putInt("id", json_list.getInt("id"));
                    prefEditor.putBoolean("auto", false);
                    prefEditor.commit();
                    Log.e("hid", pref.getString("hid", "#"));
                    progressDialog.dismiss();
                    _login.setEnabled(true);
                    startActivity(mainCall);
                    finish();
                }catch (JSONException e){
                    Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다.", Toast.LENGTH_LONG).show();
                    prefEditor.putBoolean("auto", false);
                    prefEditor.commit();
                    progressDialog.dismiss();
                    _login.setEnabled(true);
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pref = getSharedPreferences("BIDINFO", MODE_PRIVATE);
        prefEditor = pref.edit();

        _seeing = (Button)findViewById(R.id.bt_see);
        _login = (Button)findViewById(R.id.bt_signin);
        _signup = (Button)findViewById(R.id.bt_signup_ent);
        _login.setOnClickListener(this);
        _seeing.setOnClickListener(this);
        _signup.setOnClickListener(this);

        if(pref.getBoolean("tutorial", true)) startActivity(new Intent(this, TutorialActivity.class));

    }

}
