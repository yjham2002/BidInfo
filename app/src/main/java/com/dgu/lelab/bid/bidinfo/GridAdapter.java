package com.dgu.lelab.bid.bidinfo;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

class GridAdapter extends BaseAdapter {
    Context context;
    int layout;
    List<String> data;
    LayoutInflater inf;

    public GridAdapter(Context context, int layout, List<String> mData) {
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
        content.setText(data.get(position));
        return convertView;
    }
}