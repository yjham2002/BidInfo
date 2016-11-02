package com.dgu.lelab.bid.bidinfo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.ViewHolder> {

    public Context mContext = null;
    public List<CompanyData> mListData = new ArrayList<>();
    public int item_layout;

    public CompanyAdapter(Context mContext, int item_layout) {
        super();
        this.mContext = mContext;
        this.item_layout = item_layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_company, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final CompanyData mData = mListData.get(position);
        holder._name.setText(mData.Name);
        holder._addr.setText(mData.Addr);
        holder._charge.setText(mData.Rprt);
        holder.cardview.setOnClickListener(new CardView.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplyActivity.cid = mData.id;
                ApplyActivity.cname = mData.Name;
                ((AppCompatActivity)mContext).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView _name;
        public TextView _addr;
        public TextView _charge;
        public CardView cardview;
        public ViewHolder(View itemView) {
            super(itemView);
            _name = (TextView)itemView.findViewById(R.id.com_name);
            _addr = (TextView)itemView.findViewById(R.id.com_addr);
            _charge = (TextView)itemView.findViewById(R.id.com_rprt);
            cardview = (CardView) itemView.findViewById(R.id.cardview);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(CompanyData addInfo){
        mListData.add(addInfo.clone());
    }

    public void dataChange(){
        this.notifyDataSetChanged();
    }

}
