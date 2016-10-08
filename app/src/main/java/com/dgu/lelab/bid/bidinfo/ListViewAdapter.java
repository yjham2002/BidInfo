package com.dgu.lelab.bid.bidinfo;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        switch (mData.Type){
            case 0:case 1:case 2:case 3:
                holder._favicon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.div_01));
                break;
            case 4:case 5:case 6:case 7:
                holder._favicon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.div_03));
                break;
            default:
                holder._favicon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.div_02));
                break;
        }
        holder._subject.setText(mData.Title);
        holder._detail.setText(mData.Url);
        holder._date.setText(mData.PDate);
        holder._like.setText(Integer.toString(mData.like));
        holder._comment.setText(Integer.toString(mData.comment));
        holder.cardview.setOnClickListener(new CardView.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BidInfo mData = mListData.get(position);
                Intent i = new Intent(mContext, DetailActivity.class);
                i.putExtra("URL", mData.Url);
                i.putExtra("id", mData.id);
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView _favicon;
        public TextView _subject;
        public TextView _detail;
        public TextView _like;
        public TextView _comment;
        public TextView _date;
        public CardView cardview;

        public ViewHolder(View itemView) {
            super(itemView);
            _date = (TextView)itemView.findViewById(R.id.rawdate);
            _favicon = (ImageView)itemView.findViewById(R.id.favicon);
            _subject = (TextView)itemView.findViewById(R.id.subject);
            _detail = (TextView)itemView.findViewById(R.id.detail);
            _like = (TextView)itemView.findViewById(R.id.indicate_like);
            _comment = (TextView)itemView.findViewById(R.id.indicate_comment);
            cardview = (CardView)itemView.findViewById(R.id.cardview);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(BidInfo addInfo){
        mListData.add(addInfo.clone());
    }

    public void dataChange(){
        this.notifyDataSetChanged();
    }

}