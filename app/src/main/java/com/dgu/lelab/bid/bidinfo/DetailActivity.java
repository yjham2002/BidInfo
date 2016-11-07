package com.dgu.lelab.bid.bidinfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import util.Communicator;
import util.URL;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    private InputMethodManager imm;
    private ProgressDialog progressDialog;

    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;

    private Bundle cmdMsg;

    private String mTitle = "";
    private int mToken = 0;

    private CheckBox _like;
    private TextView _Url, _Title, _PDate, _ReferAndBNum, _hid, _Bstart, _Bexpire, _Dept, _Charge;
    private ImageView _Type;

    private EditText _comment;
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
        if(!_like.isChecked()) // unlike
            URL = URL + util.URL.REST_UNLIKE;
        else{ // like
            URL = URL + util.URL.REST_LIKE;
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
        data.put("Amount", null);
        data.put("Comment", msg);
        data.put("mid", Integer.toString(pref.getInt("id", 0)));
        data.put("bid", Integer.toString(cmdMsg.getInt("id")));
        new Communicator().postHttp(util.URL.MAIN + util.URL.REST_COMMENT_NEW, data, new Handler(){
            @Override
            public void handleMessage(Message msg){
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

        _comment = (EditText)findViewById(R.id.detail_comment);
        _submit = (Button)findViewById(R.id.detail_submit);
        _submit.setOnClickListener(this);
        _redirect = (Button)findViewById(R.id.redirect);
        _redirect.setOnClickListener(this);
        _exit = (Button)findViewById(R.id.bt_exit);
        _exit.setOnClickListener(this);

        _like = (CheckBox)findViewById(R.id.like);
        _like.setOnClickListener(this);

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        commentAdapter = new CommentAdapter(this, R.layout.listview_comment);

        _Type = (ImageView)findViewById(R.id.favicon);
        _Url = (TextView)findViewById(R.id.Url);
        _Title = (TextView)findViewById(R.id.Title);
        _PDate = (TextView)findViewById(R.id.PDate);
        _ReferAndBNum = (TextView)findViewById(R.id.Refer);
        _hid = (TextView)findViewById(R.id.hid);
        _Bstart = (TextView)findViewById(R.id.Bstart);
        _Bexpire = (TextView)findViewById(R.id.Bexpire);
        _Dept = (TextView)findViewById(R.id.Dept);
        _Charge = (TextView)findViewById(R.id.Charge);
        _Type = (ImageView)findViewById(R.id.favicon);

        mRecyclerView.setAdapter(commentAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        Intent cmd = getIntent();
        cmdMsg = cmd.getExtras();

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
                    if(json_list.getInt("check")==0) _like.setChecked(false);
                    else _like.setChecked(true);
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
                            _Type.setImageDrawable(getResources().getDrawable(R.drawable.div_01));
                            break;
                        case 4:case 5:case 6:case 7:
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

                    if(json_list.getString("Url").equals("null") || json_list.getString("Url").equals("about:blank")) {
                        _Url.setVisibility(View.GONE);
                        _redirect.setVisibility(View.GONE);
                    }
                    else _Url.setText(json_list.getString("Url"));
                    if(json_list.getString("Title").equals("null")) _Title.setVisibility(View.GONE);
                    else _Title.setText(json_list.getString("Title"));
                    if(json_list.getString("PDate").equals("null") || json_list.getString("PDate").equals("NULL")) _PDate.setVisibility(View.GONE);
                    else _PDate.setText(json_list.getString("PDate"));
                    if(json_list.getString("Refer").equals("null")) _ReferAndBNum.setVisibility(View.GONE);
                    else{
                        if(json_list.getString("BidNo").equals("null")) _ReferAndBNum.setText(json_list.getString("Refer"));
                        else _ReferAndBNum.setText(json_list.getString("Refer") + " - " + json_list.getString("BidNo"));
                    }
                    if(json_list.getString("Bstart").equals("null")) _Bstart.setVisibility(View.GONE);
                    else _Bstart.setText("입찰시작일자 : " + json_list.getString("Bstart"));
                    if(json_list.getString("Bexpire").equals("null")) _Bexpire.setVisibility(View.GONE);
                    else _Bexpire.setText("입찰종료일자 : " + json_list.getString("Bexpire"));
                    if(json_list.getString("Dept").equals("null")) _Dept.setVisibility(View.GONE);
                    else _Dept.setText(json_list.getString("Dept"));
                    if(json_list.getString("Charge").equals("null")) _Charge.setVisibility(View.GONE);
                    else _Charge.setText(json_list.getString("Charge"));
                    if(json_list.getString("hid").equals("null")) _hid.setVisibility(View.GONE);
                    else {
                        _hid.setText("#" + json_list.getString("hid").replaceAll("\\|", " #"));
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
        Communicator.getHttp(util.URL.MAIN + util.URL.REST_BOARD_COMMENT + cmdMsg.getInt("id"), new Handler(){
            @Override
            public void handleMessage(Message msg){
                String jsonString = msg.getData().getString("jsonString");
                commentAdapter.mListData.clear();
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
