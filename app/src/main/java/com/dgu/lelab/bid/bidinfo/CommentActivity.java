package com.dgu.lelab.bid.bidinfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import util.Communicator;
import util.URL;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog progressDialog1;

    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;

    private RecyclerView mRecyclerView;
    private CommentAdapter commentAdapter;

    private Button _close;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt_exit:
                finish();
                break;
            default: break;
        }
    }

    public void loadComment(){
        progressDialog1 = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        progressDialog1.setIndeterminate(true);
        progressDialog1.setMessage("댓글 목록을 불러오는 중...");

        commentAdapter = new CommentAdapter(this, R.layout.listview_comment);
        mRecyclerView.setAdapter(commentAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        Communicator.getHttp(util.URL.MAIN + util.URL.REST_BOARD_PCOMMENT + pref.getInt("id", 0), new Handler(){
            @Override
            public void handleMessage(Message msg){
                String jsonString = msg.getData().getString("jsonString");
                try {
                    JSONArray json_arr = new JSONArray(jsonString);
                    /**
                     * {"id":7,"Comment":"테스트 댓글1","Amount":"100","Date":"2016-10-07T12:45:15.000Z","mid":1,"bid":1,"userName":"함의진"}
                     * int id, String userName, String content, String date, String amount, int mid
                     * */
                    for(int i = 0; i < json_arr.length(); i++){
                        JSONObject json_list = json_arr.getJSONObject(i);
                        commentAdapter.addItem(new CommentData(json_list.getInt("id"), json_list.getString("userName"), json_list.getString("Comment"), json_list.getString("Date"), json_list.getString("Amount"), json_list.getInt("mid"), json_list.getInt("bid")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    progressDialog1.dismiss();
                    commentAdapter.dataChange();
                }
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        pref = getSharedPreferences("BIDINFO", MODE_PRIVATE);
        prefEditor = pref.edit();

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        commentAdapter = new CommentAdapter(this, R.layout.listview_comment);
        mRecyclerView.setAdapter(commentAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        _close = (Button)findViewById(R.id.bt_exit);
        _close.setOnClickListener(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        loadComment();
    }

}
