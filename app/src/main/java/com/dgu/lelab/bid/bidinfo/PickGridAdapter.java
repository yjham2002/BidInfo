package com.dgu.lelab.bid.bidinfo;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

class PickGridAdapter extends BaseAdapter {
    Context context;
    int layout;
    List<KeywordItem> data;
    LayoutInflater inf;

    public PickGridAdapter(Context context, int layout, List<KeywordItem> mData) {
        this.context = context;
        this.layout = layout;
        this.data = mData;
        inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) convertView = inf.inflate(layout, null);
        TextView content = (TextView) convertView.findViewById(R.id.grid);
        if(data.get(position).keyword.length() == 0 || data.get(position).isTitle) {
            content.setText(data.get(position).keyword);
        }else{
            content.setText(data.get(position).keyword);
        }

        /*if(data.get(position).isSelected) {
            content.setTextColor(context.getResources().getColor(R.color.white));
            content.setBackgroundColor(context.getResources().getColor(R.color.transparent_gray));
        }
        else {
            content.setTextColor(context.getResources().getColor(R.color.monsoon));
            content.setBackgroundColor(context.getResources().getColor(R.color.transparent));
        }*/

        if(data.get(position).isTitle){

            content.setTextColor(context.getResources().getColor(R.color.coral));

        }

        return convertView;
    }
}