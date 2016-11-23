package com.dgu.lelab.bid.bidinfo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.tsengvn.typekit.TypekitContextWrapper;

public class ExpireActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText _name;
    private Button _submit;

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.expr_submit:
                break;
            default: break;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expire);

        _name = (EditText)findViewById(R.id.expire_draw);
        _submit = (Button)findViewById(R.id.expr_submit);
        _submit.setOnClickListener(this);

    }
}
