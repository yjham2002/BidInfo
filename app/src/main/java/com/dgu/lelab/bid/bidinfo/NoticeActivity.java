package com.dgu.lelab.bid.bidinfo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;

public class NoticeActivity extends AppCompatActivity implements View.OnClickListener{

    private Button _exit;
    private RecyclerView mRecyclerView;
    private NoticeAdapter noticeAdapter;

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
        setContentView(R.layout.activity_notice);

        _exit = (Button)findViewById(R.id.bt_exit);
        _exit.setOnClickListener(this);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        noticeAdapter = new NoticeAdapter(this, R.layout.listview_notice);
        mRecyclerView.setAdapter(noticeAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        noticeAdapter.addItem(new NoticeData(0, "테스팅 제목", "테스트1 내용입니다!", "2014-11-20"));
        noticeAdapter.addItem(new NoticeData(0, "테스팅 제목", "테스트2 내용입니다!", "2014-11-20"));
        noticeAdapter.addItem(new NoticeData(0, "테스팅 제목", "테스트3 내용입니다!", "2014-11-20"));
        noticeAdapter.addItem(new NoticeData(0, "테스팅 제목", "테스트4 내용입니다!", "2014-11-20"));
        noticeAdapter.addItem(new NoticeData(0, "테스팅 제목", "테스트5 내용입니다!", "2014-11-20"));
        noticeAdapter.addItem(new NoticeData(0, "테스팅 제목", "테스트6 내용입니다!", "2014-11-20"));
        noticeAdapter.addItem(new NoticeData(0, "테스팅 제목", "테스트7 내용입니다!", "2014-11-20"));
        noticeAdapter.addItem(new NoticeData(0, "테스팅 제목", "테스트8 내용입니다!", "2014-11-20"));
        noticeAdapter.addItem(new NoticeData(0, "테스팅 제목", "테스트9 내용입니다!", "2014-11-20"));
        noticeAdapter.addItem(new NoticeData(0, "테스팅 제목", "테스트 22내용입니다!", "2014-11-20"));
        noticeAdapter.addItem(new NoticeData(0, "테스팅 제목", "테스트2222 내용입니다!", "2014-11-20"));
        noticeAdapter.addItem(new NoticeData(0, "테스팅 제목", "테스트3123 내용입니다!", "2014-11-20"));
        noticeAdapter.addItem(new NoticeData(0, "테스팅 제목", "테스트 123내용입니다!", "2014-11-20"));
        noticeAdapter.addItem(new NoticeData(0, "테스2팅 제목", "테스트 내용입니다!", "2014-11-20"));
        noticeAdapter.addItem(new NoticeData(0, "테스2팅 제목", "테스트 내용입니다!", "2014-11-20"));
        noticeAdapter.addItem(new NoticeData(0, "테스팅 2제목", "테스트 내용입니다!", "2014-11-20"));
        noticeAdapter.addItem(new NoticeData(0, "테스팅 2제목", "테스트 내용입니다!", "2014-11-20"));
        noticeAdapter.addItem(new NoticeData(0, "테스2팅2 제목", "테스트 내용입니다!", "2014-11-20"));
        noticeAdapter.addItem(new NoticeData(0, "테스2팅 제목", "테스트 내용입니다!", "2014-11-20"));
        noticeAdapter.addItem(new NoticeData(0, "테스팅 제목", "테스트 내용입니다!", "2014-11-20"));
        noticeAdapter.addItem(new NoticeData(0, "테스2팅 제목", "테스트 내용입니다!", "2014-11-20"));
        noticeAdapter.addItem(new NoticeData(0, "테스2팅 제목", "테스트 내용입니다!", "2014-11-20"));
        noticeAdapter.addItem(new NoticeData(0, "테12스팅 제목", "테스트 내용입니다!", "2014-11-20"));
        noticeAdapter.dataChange();

    }
}
