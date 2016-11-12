package com.dgu.lelab.bid.bidinfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import util.Communicator;
import util.URL;

public class PrivateActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener{

    private ExpandableHeightGridView gv1;
    private List<String> mList1;
    GridAdapter adapter1;

    private Date startDate, endDate;

    private DatePickerDialog datePicker;

    private boolean flagStart = false, flagEnd = false, whoPicked = false;

    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;

    private Button _exit, _submit, _add, _pickStart, _pickEnd;
    private EditText _title, _keyword, _bidno, _url, _charge, _dept;
    private RadioGroup radioGroup;

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.bt_add:
                addKeyword(_keyword.getText().toString());
                _keyword.setText("");
                break;
            case R.id.private_start:
                whoPicked = false;
                datePicker.show();
                break;
            case R.id.private_end:
                whoPicked = true;
                datePicker.show();
                break;
            case R.id.private_submit:
                postArticle();
                break;
            case R.id.private_cancel:
                finish();
                break;
            default: break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if(!whoPicked){
            flagStart = true;
            _pickStart.setText(year + "-" + (monthOfYear +1) + "-" + dayOfMonth);
            startDate = new Date(year, monthOfYear, dayOfMonth);
        }else{
            flagEnd = true;
            _pickEnd.setText(year + "-" + (monthOfYear +1) + "-" + dayOfMonth);
            endDate = new Date(year, monthOfYear, dayOfMonth);
        }

    }


    public void addKeyword(String msg){
        if(msg.length() < 2){
            Toast.makeText(getApplicationContext(), "2자 이상 입력하세요", Toast.LENGTH_LONG).show();
            return;
        }
        msg = msg.replaceAll(" ", "_").trim();
        mList1.add(msg);
        adapter1.notifyDataSetChanged();
    }

    private void postArticle(){
        if(_title.getText().length() < 5) {
            _title.setError("5자 이상 입력하세요");
            return;
        }
        else _title.setError(null);
        if(_dept.getText().length() < 2) {
            _dept.setError("두 글자 이상 입력하세요");
            return;
        }
        else _dept.setError(null);
        if(_charge.getText().length() < 2) {
            _charge.setError("두 글자 이상 입력하세요");
            return;
        }
        else _charge.setError(null);

        if(!flagStart || !flagEnd){
            Toast.makeText(getApplicationContext(), "개시 및 마감 일자를 선택하세요", Toast.LENGTH_LONG).show();
            return;
        }

        if(startDate.after(endDate)){
            Toast.makeText(getApplicationContext(), "마감일을 개시일 이후로 설정하세요", Toast.LENGTH_LONG).show();
            return;
        }

        if(mList1.size() < 2) {
            Toast.makeText(getApplicationContext(), "키워드를 두 항목 이상 설정하세요", Toast.LENGTH_LONG).show();
            return;
        }

        int radioButtonID = radioGroup.getCheckedRadioButtonId();
        if(radioButtonID == -1){
            Toast.makeText(getApplicationContext(), "분류를 선택하세요", Toast.LENGTH_LONG).show();
            return;
        }
        View radioButton = radioGroup.findViewById(radioButtonID);
        int idx = radioGroup.indexOfChild(radioButton) + 8;

        String finalHash = "";
        for(String token : mList1) finalHash += token.trim() + "|";
        finalHash = finalHash.substring(0, finalHash.length() - 1);

        HashMap<String, String> data = new HashMap<>();

        String finalURL = _url.getText().toString();
        if(finalURL.length() == 0) finalURL = "about:blank";
        else if(!finalURL.contains("http")) finalURL = "http://" + finalURL;

        data.put("Title", _title.getText().toString());
        data.put("mid", Integer.toString(pref.getInt("id", 0)));
        data.put("Dept", _dept.getText().toString());
        data.put("Url", finalURL);
        data.put("Bstart", _pickStart.getText().toString());
        data.put("Bexpire", _pickEnd.getText().toString());
        data.put("Charge", _charge.getText().toString());
        data.put("BidNo", _bidno.getText().toString());
        data.put("Type", Integer.toString(idx));
        data.put("hid", finalHash);

        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("업로드하는 중...");
        progressDialog.show();

        new Communicator().postHttp(URL.MAIN + URL.REST_BOARD_NEW, data, new Handler(){
            @Override
            public void handleMessage(Message msg){
                progressDialog.dismiss();
                finish();
            }
        });
        sendPush("#keyword", finalHash);
    }

    public void sendPush(String title, String message){
        HashMap<String, String> data = new HashMap<>();
        data.put("title", title);
        data.put("message", message);
        new Communicator().postHttp(util.URL.MAIN + util.URL.REST_FCM_ALL, data, new Handler());
    }

    public void setView(){
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        datePicker = new DatePickerDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK, this, mYear, mMonth, mDay);

        flagStart = false;
        flagEnd = false;
        whoPicked = false;

        mList1 = new ArrayList<>();

        _pickStart = (Button)findViewById(R.id.private_start);
        _pickStart.setOnClickListener(this);
        _pickEnd = (Button)findViewById(R.id.private_end);
        _pickEnd.setOnClickListener(this);

        _dept = (EditText)findViewById(R.id.private_dept);
        _charge = (EditText)findViewById(R.id.private_charge);
        _bidno = (EditText)findViewById(R.id.private_bidno);
        _url = (EditText)findViewById(R.id.private_url);

        _add = (Button)findViewById(R.id.bt_add);
        _add.setOnClickListener(this);
        _exit = (Button)findViewById(R.id.private_cancel);
        _submit = (Button)findViewById(R.id.private_submit);
        _exit.setOnClickListener(this);
        _submit.setOnClickListener(this);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        _title = (EditText)findViewById(R.id.private_title);
        _keyword = (EditText)findViewById(R.id.private_key);
        gv1 = (ExpandableHeightGridView)findViewById(R.id.gridView1);
        adapter1 = new GridAdapter(getApplicationContext(), R.layout.grid_item_small, mList1);
        gv1.setExpanded(true);
        gv1.setAdapter(adapter1);
        gv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mList1.remove(position);
                adapter1.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private);

        pref = getSharedPreferences("BIDINFO", MODE_PRIVATE);
        prefEditor = pref.edit();

        setView();
    }
}
