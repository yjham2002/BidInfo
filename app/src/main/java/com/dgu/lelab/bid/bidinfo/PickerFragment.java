package com.dgu.lelab.bid.bidinfo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import util.Communicator;
import util.KEYWORDS;
import util.URL;

public class PickerFragment extends Fragment implements View.OnClickListener{

    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;

    private InputMethodManager imm;

    private int pos;

    View rootView;

    private Button _add, _end;
    private EditText _keyword;
    private TextView _title;

    private String[][] dataSet;
    private String titleData;

    private PickGridAdapter adapter1;

    private ExpandableHeightGridView gv1;
    private List<KeywordItem> mList1;

    private String finalTag;

    public static boolean mode = false;

    public void updateTags(List<String> l){
        final ProgressDialog pdial = new ProgressDialog(getActivity(), R.style.AppTheme_Dark_Dialog);
        pdial.setMessage("업데이트 요청중...");
        pdial.setCancelable(false);
        pdial.show();
        HashMap<String, String> dataSet = new HashMap<>();
        finalTag = "";
        for(String s : PickerActivity.mList1){
            finalTag = finalTag + "|" + s;
        }
        finalTag = finalTag.substring(1, finalTag.length()).trim();
        dataSet.put("hid", finalTag);
        dataSet.put("id", Integer.toString(pref.getInt("id", 0)));
        Log.e("HID", finalTag);

        new Communicator().postHttp(URL.MAIN + URL.REST_USER_UPDATE, dataSet, new Handler(){
            @Override
            public void handleMessage(Message msg){
                prefEditor.putString("hid", finalTag);
                prefEditor.commit();
                pdial.dismiss();
                getActivity().finish();
            }
        });

    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.key_end:
                if(mode) updateTags(PickerActivity.mList1);
                else getActivity().finish();
                break;
            case R.id.key_submit:
                if(_keyword.getText().length() < 2){
                    Toast.makeText(getActivity(), "2글자 이상 입력하세요", Toast.LENGTH_LONG).show();
                    break;
                }
                addToBottom(_keyword.getText().toString());
                _keyword.setText("");
                imm.hideSoftInputFromWindow(_keyword.getWindowToken(), 0);
                break;
            default: break;
        }
    }

    public void init(View v, int ii){

        imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(ii == 3){
            _end = (Button)v.findViewById(R.id.key_end);
            _end.setOnClickListener(this);
            _title = (TextView)v.findViewById(R.id.pick_title);
            _title.setText(titleData);
            _keyword = (EditText)v.findViewById(R.id.view);
            _add = (Button)v.findViewById(R.id.key_submit);
            _add.setOnClickListener(this);
        }else{
            mList1 = new ArrayList<>();

            boolean flag = false;

            for(String[] dimen : dataSet){
                for(int i = 0; i < dimen.length; i++){
                    KeywordItem keywordItem = new KeywordItem(dimen[i]);
                    mList1.add(keywordItem);
                    if(i == 0){
                        keywordItem.isTitle = true;
                        mList1.add(new KeywordItem(""));
                        mList1.add(new KeywordItem(""));
                    }
                    else if(i == dimen.length - 1){
                        keywordItem.isTitle = false;
                        for(int k = 0; k < 2 - (((dimen.length + 2)-1) % 3); k++){
                            mList1.add(new KeywordItem(""));
                        }
                    }
                }
            }

            _title = (TextView)v.findViewById(R.id.pick_title);
            _title.setText(titleData);
            adapter1 = new PickGridAdapter(getActivity(), R.layout.grid_item, mList1);
            gv1 = (ExpandableHeightGridView) v.findViewById(R.id.gridView1);
            gv1.setExpanded(true);
            gv1.setAdapter(adapter1);

            gv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mList1.get(position).isSelected = mList1.get(position).isSelected ? false : true;
                    addToBottom(mList1.get(position).keyword);
                    adapter1.notifyDataSetChanged();
                }
            });
        }


    }

    public void addToBottom(String s){
        if(PickerActivity.mList1.size() >= 15){
            Toast.makeText(getActivity(), "15개 이하로 선택하세요", Toast.LENGTH_LONG).show();
            return;
        }
        if(s.length() != 0 && !contains(PickerActivity.mList1, s)) PickerActivity.mList1.add(s);
        PickerActivity.adapter1.notifyDataSetChanged();
    }

    public boolean contains(List<String> set, String element){
        for(String s : set) if(s.equals(element)) return true;
        return false;
    }

    @Override
    public void onResume(){
        super.onResume();
        //final ScrollView scrollview = ((ScrollView) rootView.findViewById(R.id.scrollview));
        //if(pos != 3) scrollview.post(new Runnable() { @Override public void run() { scrollview.fullScroll(ScrollView.FOCUS_UP); } });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = getContext().getSharedPreferences("BIDINFO", getActivity().MODE_PRIVATE);
        prefEditor = pref.edit();

        int position = getArguments().getInt("key");
        pos = position;

        switch (position){
            case 0:
                dataSet = KEYWORDS.KEY_GOODS;
                titleData = "물품";
                break;
            case 1:
                dataSet = KEYWORDS.KEY_CONSTRUCTION;
                titleData = "공사";
                break;
            case 2:
                dataSet = KEYWORDS.KEY_SERVICE;
                titleData = "용역";
                break;
            case 3:
                titleData = "기타";
                break;
            default: dataSet = null; break;
        }

        if(position == 3) rootView = inflater.inflate(R.layout.fragment_etc, container, false);
        else rootView = inflater.inflate(R.layout.fragment_picker, container, false);

        init(rootView, position);

        return rootView;
    }
}
