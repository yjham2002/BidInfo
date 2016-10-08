package com.dgu.lelab.bid.bidinfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

public class ApplyActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener{

    public static int cid = -1;
    public static String cName = "";
    private Button _birth, _submit, _company, _picker, _exit;
    private DatePickerDialog datePicker;

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.app_birth:
                datePicker.show();
                break;
            case R.id.app_submit:
                break;
            case R.id.app_company:
                startActivity(new Intent(this, CompanyPickerActivity.class));
                break;
            case R.id.app_keyword:
                break;
            case R.id.cancel:
                finish();
                break;
            default: break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        _birth.setText(year + "년 " + monthOfYear + "월 " + dayOfMonth +"일");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);

        _company = (Button)findViewById(R.id.app_company);
        _picker = (Button)findViewById(R.id.app_keyword);
        _exit = (Button)findViewById(R.id.cancel);
        _birth = (Button)findViewById(R.id.app_birth);
        _submit = (Button)findViewById(R.id.app_submit);
        _birth.setOnClickListener(this);
        _submit.setOnClickListener(this);
        _company.setOnClickListener(this);
        _picker.setOnClickListener(this);
        _exit.setOnClickListener(this);

        datePicker = new DatePickerDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK, this, 1990, 1, 1);


    }

}
