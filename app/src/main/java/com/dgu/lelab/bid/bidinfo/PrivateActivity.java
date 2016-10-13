package com.dgu.lelab.bid.bidinfo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.HashMap;

import util.Communicator;
import util.URL;

public class PrivateActivity extends AppCompatActivity implements View.OnClickListener{

    private Button _exit, _submit;
    private EditText _title, _content, _keyword;
    private RadioGroup radioGroup;

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.private_submit:
                Toast.makeText(getApplicationContext(), Integer.toString(radioGroup.getCheckedRadioButtonId()), Toast.LENGTH_LONG).show();
                postArticle(_title.getText().toString(), _content.getText().toString(), _keyword.getText().toString());
                break;
            case R.id.private_cancel:
                finish();
                break;
            default: break;
        }
    }

    private void postArticle(String title, String content, String keyword){
        if(_title.getText().length() < 5) {
            _title.setError("5자 이상 입력하세요");
            return;
        }
        else _title.setError(null);
        if(_content.getText().length() < 10) {
            _content.setError("10자 이상 입력하세요");
            return;
        }
        else _content.setError(null);
        if(_keyword.getText().length() < 10) {
            _keyword.setError("10자 이상 입력하세요");
            return;
        }
        else _keyword.setError(null);
        HashMap<String, String> data = new HashMap<>();
        data.put("Title", title);
        data.put("Refer", content);
        data.put("hid", toRegexp(keyword));
        new Communicator().postHttp(URL.MAIN, data, new Handler(){});
    }

    private String toRegexp(String msg){
        if(msg == null) return null;
        String res = msg.trim().replaceAll(",", "|");
        return msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private);
        _exit = (Button)findViewById(R.id.private_cancel);
        _submit = (Button)findViewById(R.id.private_submit);
        _exit.setOnClickListener(this);
        _submit.setOnClickListener(this);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        _title = (EditText)findViewById(R.id.private_title);
        _content = (EditText)findViewById(R.id.private_content);
        _keyword = (EditText)findViewById(R.id.private_key);
    }
}
