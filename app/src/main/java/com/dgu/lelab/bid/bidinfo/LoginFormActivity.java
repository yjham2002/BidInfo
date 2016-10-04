package com.dgu.lelab.bid.bidinfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginFormActivity extends AppCompatActivity implements View.OnClickListener {

    private Button _login, _notyet;
    private EditText _account, _password;

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.bt_sign_f:
                Intent mainCall = new Intent(this, MainActivity.class);
                mainCall.putExtra("user_account", _account.getText().toString());
                startActivity(mainCall);
                finish();
                break;
            case R.id.bt_signup_f:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            default: break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);

        _login = (Button)findViewById(R.id.bt_sign_f);
        _login.setOnClickListener(this);
        _notyet = (Button)findViewById(R.id.bt_signup_f);
        _notyet.setOnClickListener(this);
        _account = (EditText)findViewById(R.id.log_mail);
        _password = (EditText)findViewById(R.id.log_pass);
    }
}
