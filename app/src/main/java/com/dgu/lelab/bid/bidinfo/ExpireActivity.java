package com.dgu.lelab.bid.bidinfo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class ExpireActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int UNIT = 30000;
    private Button _submit;
    private Spinner _spinner;
    private TextView _amount;

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.expr_submit:
                break;
            default: break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expire);

        _submit = (Button)findViewById(R.id.expr_submit);
        _spinner = (Spinner)findViewById(R.id.expr_spinner);
        _amount = (TextView)findViewById(R.id.amount);
        _submit.setOnClickListener(this);
        _spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _amount.setText("입금할 금액 : " + (Integer.parseInt((String)parent.getItemAtPosition(position)) * UNIT) + "원");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

    }
}
