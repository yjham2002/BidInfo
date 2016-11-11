package com.dgu.lelab.bid.bidinfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import util.Communicator;
import util.URL;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    private View line;
    private TextView unit;

    private InputMethodManager imm;
    private ProgressDialog progressDialog;

    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;

    private Bundle cmdMsg;

    private String mTitle = "";
    private int mToken = 0;

    private Button _like, _remove;
    private boolean like = false;
    private TextView _Title, _PDate, _ReferAndBNum, _Bstart, _Bexpire, _Dept, _Charge, _bidno;
    private ImageView _Type;

    private GridAdapter adapter1;
    private ExpandableHeightGridView _hid;
    private List<String> mList1;

    private EditText _comment, _amount;
    private RecyclerView mRecyclerView;
    private CommentAdapter commentAdapter;
    private String URL = "";
    private Button _redirect, _exit, _submit;

    public void sendPush(String title, String message){
        if(mToken == 0) return;
        HashMap<String, String> data = new HashMap<>();
        data.put("title", title);
        data.put("message", message);
        new Communicator().postHttp(util.URL.MAIN + util.URL.REST_FCM_ONE + mToken, data, new Handler());
    }

    public void toggleLike(){
        String URL = util.URL.MAIN;
        if(like) { // unlike
            URL = URL + util.URL.REST_UNLIKE;
            _like.setText("관심정보 지정");
            like = false;
        }
        else{ // like
            URL = URL + util.URL.REST_LIKE;
            like = true;
            _like.setText("관심정보 취소");
            sendPush("새로운 관심글 지정 알림", mTitle);
        }
        HashMap<String, String> data = new HashMap<>();
        data.put("mid", Integer.toString(pref.getInt("id", 0)));
        data.put("bid", Integer.toString(cmdMsg.getInt("id")));
        new Communicator().postHttp(URL, data, new Handler(){});

    }

    public void uploadComment(String msg){
        if(msg.length() < 5) {
            Toast.makeText(getApplicationContext(), "5글자 이상 입력하세요", Toast.LENGTH_LONG).show();
            return;
        }
        HashMap<String, String> data = new HashMap<>();
        String amount;
        if(_amount.getText().toString().length() == 0) amount = null;
        else amount = _amount.getText().toString();
        data.put("Amount", amount);
        data.put("Comment", msg);
        data.put("mid", Integer.toString(pref.getInt("id", 0)));
        data.put("bid", Integer.toString(cmdMsg.getInt("id")));
        new Communicator().postHttp(util.URL.MAIN + util.URL.REST_COMMENT_NEW, data, new Handler(){
            @Override
            public void handleMessage(Message msg){
                _amount.setText("");
                _comment.setText("");
                imm.hideSoftInputFromWindow(_comment.getWindowToken(), 0);
                loadComment();
            }
        });
        String titleTemp = mTitle;
        String msgTemp = msg;
        if(titleTemp.length() > 7) titleTemp = titleTemp.substring(0, 8) + "... ";
        if(msgTemp.length() > 25) msgTemp = msgTemp.substring(0, 24) + "...";
        sendPush(titleTemp + "에 새로운 댓글 등록됨", msgTemp);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt_remove:
                new Communicator().postHttp(util.URL.MAIN + util.URL.REST_REMOVE_BOARD + cmdMsg.getInt("id"), new HashMap<String, String>(), new Handler(){
                    @Override
                    public void handleMessage(Message msg){
                        Toast.makeText(getApplicationContext(), "삭제되었습니다.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
                break;
            case R.id.detail_submit:
                uploadComment(_comment.getText().toString());
                break;
            case R.id.like:
                toggleLike();
                break;
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
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        pref = getSharedPreferences("BIDINFO", MODE_PRIVATE);
        prefEditor = pref.edit();

        like = false;

        unit = (TextView)findViewById(R.id.unit);
        line = (View)findViewById(R.id.line);
        _amount = (EditText)findViewById(R.id.detail_amount);
        _bidno = (TextView)findViewById(R.id.bidno);
        _comment = (EditText)findViewById(R.id.detail_comment);
        _submit = (Button)findViewById(R.id.detail_submit);
        _submit.setOnClickListener(this);
        _redirect = (Button)findViewById(R.id.redirect);
        _redirect.setOnClickListener(this);
        _exit = (Button)findViewById(R.id.bt_exit);
        _exit.setOnClickListener(this);

        _like = (Button)findViewById(R.id.like);
        _like.setOnClickListener(this);

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        commentAdapter = new CommentAdapter(this, R.layout.listview_comment);

        _Type = (ImageView)findViewById(R.id.favicon);
        _Title = (TextView)findViewById(R.id.Title);
        _PDate = (TextView)findViewById(R.id.PDate);
        _ReferAndBNum = (TextView)findViewById(R.id.Refer);

        _Bstart = (TextView)findViewById(R.id.Bstart);
        _Bexpire = (TextView)findViewById(R.id.Bexpire);
        _Dept = (TextView)findViewById(R.id.Dept);
        _Charge = (TextView)findViewById(R.id.Charge);
        _remove = (Button)findViewById(R.id.bt_remove);
        _remove.setOnClickListener(this);
        _Type = (ImageView)findViewById(R.id.favicon);

        mRecyclerView.setAdapter(commentAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mList1 = new ArrayList<>();

        adapter1 = new GridAdapter(this, R.layout.grid_item_detail, mList1);
        _hid = (ExpandableHeightGridView)findViewById(R.id.hid);
        _hid.setExpanded(true);
        _hid.setAdapter(adapter1);

        _amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                if(arg0.length() > 6) {
                    _amount.setText(_amount.getText().toString().substring(0, 6));
                    Toast.makeText(getApplicationContext(), "금액은 6자 이내로 입력하세요", Toast.LENGTH_LONG).show();
                }
            }
        });

        _comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                if(arg0.length() > 100) {
                    _amount.setText(_amount.getText().toString().substring(0, 100));
                    Toast.makeText(getApplicationContext(), "100자 이내로 입력하세요", Toast.LENGTH_LONG).show();
                }
            }
        });

        _hid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        Intent cmd = getIntent();
        cmdMsg = cmd.getExtras();

        _remove.setVisibility(View.INVISIBLE);

    }

    public void hideAmount(){
        _amount.setVisibility(View.GONE);
        line.setVisibility(View.GONE);
        unit.setVisibility(View.GONE);
    }

    @Override
    public void onResume(){
        super.onResume();
        progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("상세정보를 불러오는 중...");
        progressDialog.show();

        HashMap<String, String> data = new HashMap<>();
        data.put("mid", Integer.toString(pref.getInt("id", 0)));
        data.put("bid", Integer.toString(cmdMsg.getInt("id")));
        new Communicator().postHttp(util.URL.MAIN + util.URL.REST_CHECK, data, new Handler(){
            @Override
            public void handleMessage(Message msg){
                String jsonString = msg.getData().getString("jsonString");
                try{
                    JSONArray json_arr = new JSONArray(jsonString);
                    JSONObject json_list = json_arr.getJSONObject(0);
                    if(json_list.getInt("check")==0) {
                        like = false;
                        _like.setText("관심정보 지정");
                    }
                    else {
                        like = true;
                        _like.setText("관심정보 취소");
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
            });

        Communicator.getHttp(util.URL.MAIN + util.URL.REST_BOARD_ONE + cmdMsg.getInt("id"), new Handler(){
            @Override
            public void handleMessage(Message msg){
                String jsonString = msg.getData().getString("jsonString");
                commentAdapter.mListData.clear();
                try {
                    JSONArray json_arr = new JSONArray(jsonString);
                    JSONObject json_list = json_arr.getJSONObject(0);
                    Log.e("JSON", msg.getData().getString("jsonString"));
                    switch (json_list.getInt("Type")){
                        case 0:case 1:case 2:case 3:
                            hideAmount();
                            _Type.setImageDrawable(getResources().getDrawable(R.drawable.div_01));
                            break;
                        case 4:case 5:case 6:case 7:
                            hideAmount();
                            _Type.setImageDrawable(getResources().getDrawable(R.drawable.div_03));
                            break;
                        default:
                            _Type.setImageDrawable(getResources().getDrawable(R.drawable.div_02));
                            break;
                    }

                    try{
                        mToken = json_list.getInt("mid");
                        mTitle = json_list.getString("Title");
                    }catch (Exception e){
                        mToken = 0;
                    }

                    String none = "정보없음";

                    if(json_list.getString("Url").equals("null") || json_list.getString("Url").equals("about:blank")) {
                        _redirect.setVisibility(View.GONE);
                    }
                    if(json_list.getString("Title").equals("null")) _Title.setText(none);
                    else _Title.setText(json_list.getString("Title"));
                    if(json_list.getString("Date").equals("null") || json_list.getString("Date").equals("NULL")) _PDate.setText(none);
                    else _PDate.setText(json_list.getString("Date"));
                    if(json_list.getString("Refer").equals("null")) _ReferAndBNum.setText(none);
                    else _ReferAndBNum.setText(json_list.getString("Refer"));
                    if(json_list.getString("BidNo").equals("null")) _bidno.setText(none);
                    else _bidno.setText(json_list.getString("BidNo"));
                    if(json_list.getString("Bstart").equals("null")) _Bstart.setText(none);
                    else _Bstart.setText(json_list.getString("Bstart"));
                    if(json_list.getString("Bexpire").equals("null")) _Bexpire.setVisibility(View.GONE);
                    else _Bexpire.setText(" / " + json_list.getString("Bexpire"));
                    if(json_list.getString("Dept").equals("null")) _Dept.setText(none);
                    else _Dept.setText(json_list.getString("Dept"));
                    if(json_list.getString("Charge").equals("null")) _Charge.setText(none);
                    else _Charge.setText(json_list.getString("Charge"));

                    Log.e("REMOVE Button", cmdMsg.getInt("id") + " / " + json_list.getInt("mid"));

                    if(pref.getInt("id", 0) != json_list.getInt("mid")){
                        _remove.setVisibility(View.GONE);
                    }else{
                        _remove.setVisibility(View.VISIBLE);
                    }

                    if(!json_list.getString("hid").equals("null")) {
                        List<String> list = new ArrayList<String>(Arrays.asList(json_list.getString("hid").split("\\|")));
                        mList1.clear();
                        mList1.addAll(list);
                        adapter1.notifyDataSetChanged();
                    }

                    URL = json_list.getString("Url");
                    Communicator.getHttp(util.URL.MAIN + util.URL.REST_HIT + cmdMsg.getInt("id"), new Handler());
                    loadComment();

                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "정보 불러오는 중 오류가 발생하였습니다.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void loadComment(){
        commentAdapter = new CommentAdapter(this, R.layout.listview_comment);
        mRecyclerView.setAdapter(commentAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        Communicator.getHttp(util.URL.MAIN + util.URL.REST_BOARD_COMMENT + cmdMsg.getInt("id"), new Handler(){
            @Override
            public void handleMessage(Message msg){
                String jsonString = msg.getData().getString("jsonString");

                try {
                    JSONArray json_arr = new JSONArray(jsonString);
                    /**
                     * {"id":7,"Comment":"테스트 댓글1","Amount":"100","Date":"2016-10-07T12:45:15.000Z","mid":1,"bid":1,"userName":"함의진"}
                     * int id, String userName, String content, String date, String amount, int mid
                     * */
                    for(int i = 0; i < json_arr.length(); i++){
                        JSONObject json_list = json_arr.getJSONObject(i);
                        commentAdapter.addItem(new CommentData(json_list.getInt("id"), json_list.getString("userName"), json_list.getString("Comment"), json_list.getString("Date"), json_list.getString("Amount"), json_list.getInt("mid")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    progressDialog.dismiss();
                    commentAdapter.dataChange();
                }
            }
        });

    }


}
