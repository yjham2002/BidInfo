package com.dgu.lelab.bid.bidinfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.Communicator;
import util.URL;

public class ApplyActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener{

    private EditText _mail, _pass, _name, _phone;

    public static String cname = "회사명 선택";
    public static int cid = -1;
    public boolean dateset = false;
    public static String cName = "";
    private Button _birth, _submit, _company, _picker, _exit, _inst;
    private DatePickerDialog datePicker;

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.reg_instruction:
                startActivity(new Intent(this, AgreeActivity.class));
                break;
            case R.id.app_birth:
                datePicker.show();
                break;
            case R.id.app_submit:
                onSubmit();
                break;
            case R.id.app_company:
                startActivity(new Intent(this, CompanyPickerActivity.class));
                break;
            case R.id.app_keyword:
                Intent intent = new Intent(this, PickerActivity.class);
                PickerFragment.mode = false;
                startActivity(intent);
                break;
            case R.id.cancel:
                finish();
                break;
            default: break;
        }
    }

    public boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public void onSubmit(){
        if(!isEmailValid(_mail.getText().toString())){
            Toast.makeText(getApplicationContext(), "올바른 이메일 주소를 입력하세요", Toast.LENGTH_LONG).show();
            return;
        }
        if(_pass.getText().length() < 6){
            Toast.makeText(getApplicationContext(), "패스워드는 6자리 이상으로 설정하세요", Toast.LENGTH_LONG).show();
            return;
        }
        if(_name.getText().length() < 2){
            Toast.makeText(getApplicationContext(), "올바른 성명을 입력하세요", Toast.LENGTH_LONG).show();
            return;
        }
        if(_phone.getText().length() < 10){
            Toast.makeText(getApplicationContext(), "올바른 전화번호를 입력하세요", Toast.LENGTH_LONG).show();
            return;
        }
        if(PickerActivity.mList1.size() == 0){
            Toast.makeText(getApplicationContext(), "키워드를 선택하세요", Toast.LENGTH_LONG).show();
            return;
        }
        if(cid == -1){
            Toast.makeText(getApplicationContext(), "회사를 선택하세요", Toast.LENGTH_LONG).show();
            return;
        }
        if(!dateset){
            Toast.makeText(getApplicationContext(), "개업일자를 선택하세요", Toast.LENGTH_LONG).show();
            return;
        }
        HashMap<String, String> data = new HashMap<>();
        data.put("Uid", _mail.getText().toString());
        new Communicator().postHttp(URL.MAIN + URL.REST_USER_CHECK, data, new Handler(){
            @Override
            public void handleMessage(Message msg){
                String jsonString = msg.getData().getString("jsonString");
                try {
                    JSONArray json_arr = new JSONArray(jsonString);
                    if(json_arr.length() == 0){
                        String finalTag = "";
                        for(String s : PickerActivity.mList1){
                            finalTag = finalTag + "|" + s;
                        }
                        finalTag = finalTag.substring(1, finalTag.length()).trim();

                        HashMap<String, String> data = new HashMap<>();
                        data.put("Uid", _mail.getText().toString());
                        data.put("Pwd", _pass.getText().toString());
                        data.put("Name", _name.getText().toString());
                        data.put("Phone", _phone.getText().toString());
                        data.put("Symbol", Integer.toString(cid));
                        data.put("Bdate", _birth.getText().toString());
                        data.put("hid", finalTag);
                        new Communicator().postHttp(URL.MAIN + URL.REST_USER_NEW, data, new Handler(){
                            @Override
                            public void handleMessage(Message msg){
                                Toast.makeText(getApplicationContext(), "가입이 완료되었습니다", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });
                    }else{
                        Toast.makeText(getApplicationContext(), "이미 존재하는 아이디입니다", Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e){
                    Toast.makeText(getApplicationContext(), "요청에 실패하였습니다", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        _company.setText(cname);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        dateset = true;
        _birth.setText(year + "-" + (monthOfYear +1) + "-" + dayOfMonth);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);

        dateset = false;

        PickerActivity.mList1.clear();

        cid = -1;
        cname = "회사명 선택";

        _mail = (EditText)findViewById(R.id.app_mail);
        _pass = (EditText)findViewById(R.id.app_pass);
        _name = (EditText)findViewById(R.id.app_name);
        _phone = (EditText)findViewById(R.id.app_num);
        _inst = (Button)findViewById(R.id.reg_instruction);
        _company = (Button)findViewById(R.id.app_company);
        _picker = (Button)findViewById(R.id.app_keyword);
        _exit = (Button)findViewById(R.id.cancel);
        _birth = (Button)findViewById(R.id.app_birth);
        _submit = (Button)findViewById(R.id.app_submit);
        _inst.setOnClickListener(this);
        _birth.setOnClickListener(this);
        _submit.setOnClickListener(this);
        _company.setOnClickListener(this);
        _picker.setOnClickListener(this);
        _exit.setOnClickListener(this);

        datePicker = new DatePickerDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK, this, 1990, 1, 1);

    }

}
