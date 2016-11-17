package com.dgu.lelab.bid.bidinfo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import util.Communicator;
import util.TimeCaculator;
import util.URL;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {

        private SharedPreferences pref;

        public Context mContext = null;
        public List<NoticeData> mListData = new ArrayList<>();
        public int item_layout;

        public NoticeAdapter(Context mContext, int item_layout) {
            super();
            this.mContext = mContext;
            this.item_layout = item_layout;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_notice, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            pref = mContext.getSharedPreferences("BIDINFO", mContext.MODE_PRIVATE);

            final NoticeData mData = mListData.get(position);
            holder._title.setText(mData.Title);
            holder._date.setText(TimeCaculator.formatTimeString(mData.Date));
            holder._content.setText(mData.Content);
            holder.cardview.setOnClickListener(new CardView.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //final NoticeData mData = mListData.get(position);
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AppTheme_Dark_Dialog);
                    builder.setMessage("공지사항을 삭제하시겠습니까?");
                    builder.setCancelable(true);
                    builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })
                            .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    new Communicator().postHttp(util.URL.MAIN + util.URL.REST_REMOVE_NOTICE + mData.id, new HashMap<String, String>(), new Handler(){
                                        @Override
                                        public void handleMessage(Message msg){
                                            Toast.makeText(mContext, "삭제되었습니다.", Toast.LENGTH_LONG).show();
                                            if(mContext instanceof NoticeActivity) ((NoticeActivity) mContext).loadList();
                                        }
                                    });
                                }
                            });
                    AlertDialog alert = builder.create();
                    if (pref.getString("Uid", "#").equals("admin@lelab.com")) alert.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mListData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView _title;
            public TextView _date;
            public TextView _content;
            public CardView cardview;

            public ViewHolder(View itemView) {
                super(itemView);
                _title = (TextView)itemView.findViewById(R.id.title);
                _date = (TextView)itemView.findViewById(R.id.date);
                _content = (TextView)itemView.findViewById(R.id.content);
                cardview = (CardView)itemView.findViewById(R.id.cardview);
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

    public void addItem(NoticeData addInfo){
        NoticeData newAssign = new NoticeData(addInfo.id, addInfo.Title, addInfo.Content, addInfo.Date);
        mListData.add(newAssign);
    }

    public void dataChange(){
        this.notifyDataSetChanged();
    }

}
