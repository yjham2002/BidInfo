package com.dgu.lelab.bid.bidinfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private CommentAdapter commentAdapter;
    private String URL = "";
    private Button _redirect, _exit;

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.redirect:
                if(URL.length() <= 7) {
                    Toast.makeText(getApplicationContext(), "주소 정보가 없습니다.", Toast.LENGTH_LONG).show();
                    break;
                }
                Intent i = new Intent(this, WebviewActivity.class);
                i.putExtra("URL", URL);
                startActivity(i);
                break;
            case R.id.bt_exit:
                finish();
                break;
            default: break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        _redirect = (Button)findViewById(R.id.redirect);
        _redirect.setOnClickListener(this);
        _exit = (Button)findViewById(R.id.bt_exit);
        _exit.setOnClickListener(this);

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        commentAdapter = new CommentAdapter(this, R.layout.listview_comment);

        mRecyclerView.setAdapter(commentAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        Intent cmd = getIntent();
        Bundle cmdMsg = cmd.getExtras();
        if(cmdMsg == null) URL = "";
        else URL = cmdMsg.getString("URL");

        commentAdapter.addItem(new CommentData(0, "사용자", "테스트 댓글입니다.", "2014-11-20", "10,000"));
        commentAdapter.addItem(new CommentData(0, "사용자", "테스트 댓글입니다.", "2014-11-20", "10,000"));
        commentAdapter.addItem(new CommentData(0, "사용자", "테스트 댓글입니다.", "2014-11-20", "10,000"));
        commentAdapter.addItem(new CommentData(0, "사용자", "테스트 댓글입니다.", "2014-11-20", "10,000"));
        commentAdapter.addItem(new CommentData(0, "사용자", "테스트 댓글입니다.", "2014-11-20", "10,000"));
        commentAdapter.addItem(new CommentData(0, "사용자", "테스트 댓글입니다.", "2014-11-20", "10,000"));
        commentAdapter.addItem(new CommentData(0, "사용자", "테스트 댓글입니다.", "2014-11-20", "10,000"));
        commentAdapter.addItem(new CommentData(0, "사용자", "테스트 댓글입니다.", "2014-11-20", "10,000"));
        commentAdapter.addItem(new CommentData(0, "사용자", "테스트 댓글입니다.", "2014-11-20", "10,000"));
        commentAdapter.dataChange();

    }
}
