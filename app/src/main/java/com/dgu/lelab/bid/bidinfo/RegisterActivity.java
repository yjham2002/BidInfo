package com.dgu.lelab.bid.bidinfo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import util.Communicator;
import util.URL;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    public static boolean mode = false;

    private ExpandableHeightGridView gv1;
    private List<String> mList1 = new ArrayList<>();
    GridAdapter adapter1;

    private EditText _name, _rnum, _rprt, _charge, _addr, _phone, _email, _home, _keyword;
    private Button _submit, _cancel, _add;

    public void addKeyword(String msg){
        if(msg.length() < 2){
            Toast.makeText(getApplicationContext(), "2자 이상 입력하세요", Toast.LENGTH_LONG).show();
            return;
        }
        msg = msg.replaceAll(" ", "_").trim();
        mList1.add(msg);
        adapter1.notifyDataSetChanged();
    }

    public void onPost(){
        if(_name.getText().length() < 2 || _rnum.getText().length() == 0 || _rprt.getText().length() < 2 || _charge.getText().length() < 2 || _addr.getText().length() ==0 || _phone.getText().length() < 10 || _email.getText().length() < 2){
            Toast.makeText(getApplicationContext(), "필수입력란을 모두 입력하세요", Toast.LENGTH_LONG).show();
            return;
        }
        if(mList1.size() < 2){
            Toast.makeText(getApplicationContext(), "키워드를 2개 이상 입력하세요", Toast.LENGTH_LONG).show();
            return;
        }
        String finalHash = "";
        for(String token : mList1) finalHash += token.trim() + "|";
        finalHash = finalHash.substring(0, finalHash.length() - 1);
        String finalURL = URL.MAIN;
        if(!mode){ // On Registeration

        }else{ // On Modifying

        }
        HashMap<String, String> data = new HashMap<>();

        new Communicator().postHttp(finalURL, data, new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(!mode){ // On Registeration
                    Toast.makeText(getApplicationContext(), "추가되었습니다", Toast.LENGTH_LONG).show();
                }else{ // On Modifying
                    Toast.makeText(getApplicationContext(), "적용되었습니다", Toast.LENGTH_LONG).show();
                }
                finish();
            }
        });
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.bt_add:
                addKeyword(_keyword.getText().toString());
                _keyword.setText("");
                break;
            case R.id.reg_submit:
                break;
            case R.id.reg_cancel:
                finish();
                break;
            default: break;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    public void initView(){
        mList1.clear();
        _name = (EditText)findViewById(R.id.reg_name);
        _rnum = (EditText)findViewById(R.id.reg_num);
        _rprt = (EditText)findViewById(R.id.reg_rep);
        _charge = (EditText)findViewById(R.id.reg_chr);
        _addr = (EditText)findViewById(R.id.reg_addr);
        _phone = (EditText)findViewById(R.id.reg_phone);
        _email = (EditText)findViewById(R.id.reg_email);
        _home = (EditText)findViewById(R.id.reg_homepage);
        _keyword = (EditText)findViewById(R.id.private_key);
        _submit = (Button)findViewById(R.id.reg_submit);
        _cancel = (Button)findViewById(R.id.reg_cancel);
        _add = (Button)findViewById(R.id.bt_add);
        _add.setOnClickListener(this);
        _submit.setOnClickListener(this);
        _cancel.setOnClickListener(this);

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
        setContentView(R.layout.activity_register);

        initView();
    }
}
