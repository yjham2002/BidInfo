package com.dgu.lelab.bid.bidinfo;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import util.Communicator;
import util.TimeCaculator;
import util.URL;

/**
 * Created by HP on 2016-11-24.
 */
public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> implements DatePickerDialog.OnDateSetListener{

    private static int mmid = 0;
    private static int iid = 0;

    private DatePickerDialog datePicker;
    private SharedPreferences pref;

    public Context mContext = null;
    public List<RequestData> mListData = new ArrayList<>();
    public int item_layout;

    public RequestAdapter(Context mContext, int item_layout) {
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
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        HashMap<String, String> dataSet = new HashMap<>();
        dataSet.put("ExpDate", year + "-" + (monthOfYear +1) + "-" + dayOfMonth);
        dataSet.put("mid", Integer.toString(mmid));
        new Communicator().postHttp(URL.MAIN + URL.REST_USER_PAID + iid, dataSet, new Handler(){
            @Override
            public void handleMessage(Message msg){
                Toast.makeText(mContext, "적용되었습니다.", Toast.LENGTH_LONG).show();

                if(mContext instanceof AdminActivity) ((AdminActivity) mContext).loadList();
            }
        });
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        pref = mContext.getSharedPreferences("BIDINFO", mContext.MODE_PRIVATE);

        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        datePicker = new DatePickerDialog(mContext, android.app.AlertDialog.THEME_DEVICE_DEFAULT_DARK, this, mYear, mMonth, mDay);

        final RequestData mData = mListData.get(position);
        holder._title.setText(mData.Draw);
        holder._date.setText(TimeCaculator.formatTimeString(mData.Date));
        holder._content.setText(mData.Phone);
        holder.cardview.setOnClickListener(new CardView.OnClickListener() {
            @Override
            public void onClick(View v) {
                //final NoticeData mData = mListData.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AppTheme_Dark_Dialog);
                builder.setMessage("결제를 확인하시겠습니까?");
                builder.setCancelable(true);
                builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                        .setNegativeButton("확인하며 날짜 선택", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mmid = mData.mid;
                                iid = mData.id;
                                datePicker.show();
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

    public void addItem(RequestData addInfo){
        mListData.add(addInfo);
    }

    public void dataChange(){
        this.notifyDataSetChanged();
    }

}
