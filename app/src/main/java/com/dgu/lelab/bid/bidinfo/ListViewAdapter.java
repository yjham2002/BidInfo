package com.dgu.lelab.bid.bidinfo;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> {

    public static final int HEADER = 3, DEFAULT = 0;

    public Context mContext = null;
    public List<BidInfo> mListData = new ArrayList<>();
    public int item_layout;
    public int addition = 0;

    public ListViewAdapter(Context mContext, int item_layout) {
        super();
        this.mContext = mContext;
        this.item_layout = item_layout;
    }

    public ListViewAdapter(Context mContext, int item_layout, int addition) {
        this.mContext = mContext;
        this.item_layout = item_layout;
        this.addition = addition;
    }

    @Override
    public int getItemViewType(int position){
        if(position == 0) return HEADER;
        else return DEFAULT;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch(viewType){
            case DEFAULT:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_bid, parent, false);
                break;
//            case HEADER:
  //              v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_header, parent, false);
    //            break;
            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_bid, parent, false);
                break;
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        BidInfo mData = mListData.get(position);
        holder._subject.setText(mData.text);
        holder._detail.setText(mData.Url);
        holder.cardview.setOnClickListener(new CardView.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BidInfo mData = mListData.get(position);
                Intent i = new Intent(mContext, WebviewActivity.class);
                i.putExtra("URL", mData.Url);
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView _subject;
        public TextView _detail;
        public CardView cardview;

        public ViewHolder(View itemView) {
            super(itemView);
            _subject = (TextView)itemView.findViewById(R.id.subject);
            _detail = (TextView)itemView.findViewById(R.id.detail);
            cardview = (CardView)itemView.findViewById(R.id.cardview);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(BidInfo addInfo){
        BidInfo newAssign = new BidInfo();
        newAssign.text = addInfo.text;
        newAssign.Url = addInfo.Url;
        mListData.add(newAssign);
    }

    public void dataChange(){
        this.notifyDataSetChanged();
    }

}