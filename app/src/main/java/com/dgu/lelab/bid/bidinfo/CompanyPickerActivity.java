package com.dgu.lelab.bid.bidinfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import util.Communicator;
import util.URL;

public class CompanyPickerActivity extends AppCompatActivity implements View.OnClickListener{

    private Button _exit;
    private RecyclerView mRecyclerView;
    private CompanyAdapter companyAdapter;

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt_exit:
                finish();
                break;
            default: break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_picker);

        _exit = (Button)findViewById(R.id.bt_exit);
        _exit.setOnClickListener(this);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        companyAdapter = new CompanyAdapter(this, R.layout.listview_company);
        mRecyclerView.setAdapter(companyAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    public void onResume(){
        super.onResume();
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("회사 목록을 불러오는 중...");
        progressDialog.show();
        Communicator.getHttp(URL.MAIN + URL.REST_COMPANY_ALL, new Handler(){
            @Override
            public void handleMessage(Message msg){
                String jsonString = msg.getData().getString("jsonString");
                try {
                    companyAdapter.mListData.clear();
                    JSONArray json_arr = new JSONArray(jsonString);
                    for(int i = 0; i < json_arr.length(); i++) {
                        JSONObject json_list = json_arr.getJSONObject(i);
                        //Log.e("json", json_list.toString());
                        companyAdapter.addItem(new CompanyData(json_list.getInt("id"), json_list.getInt("symbol"), json_list.getInt("Pnum"), json_list.getString("Name"), json_list.getString("Rnum"),
                                json_list.getString("Rprt"), json_list.getString("Charge"), json_list.getString("Addr"), json_list.getString("Phone"), json_list.getString("Email"), json_list.getString("Divs"),
                                json_list.getString("Divl"), json_list.getString("Expl"), json_list.getString("Date"), json_list.getString("hid")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    progressDialog.dismiss();
                    companyAdapter.dataChange();
                }

            }
        });
    }
}
