package com.dgu.lelab.bid.bidinfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import util.Communicator;
import util.URL;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener{

    private Button _exit;
    private RecyclerView mRecyclerView;
    private RequestAdapter noticeAdapter;

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
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        _exit = (Button)findViewById(R.id.bt_exit);
        _exit.setOnClickListener(this);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        noticeAdapter = new RequestAdapter(this, R.layout.listview_req);
        mRecyclerView.setAdapter(noticeAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        loadList();
    }

    public void loadList(){
        Communicator.getHttp(URL.MAIN + URL.REST_USER_REQS, new Handler(){
            @Override
            public void handleMessage(Message msg){
                String jsonString = msg.getData().getString("jsonString");
                try {
                    noticeAdapter.mListData.clear();
                    JSONArray json_arr = new JSONArray(jsonString);
                    for(int i = 0; i < json_arr.length(); i++) {
                        JSONObject json_list = json_arr.getJSONObject(i);
                        noticeAdapter.addItem(new RequestData(json_list.getInt("id"), json_list.getInt("mid"), json_list.getString("Draw"), json_list.getString("Date"), json_list.getString("Phone")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    noticeAdapter.dataChange();
                }

            }
        });
    }
}
