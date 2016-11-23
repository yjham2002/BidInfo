package com.dgu.lelab.bid.bidinfo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.List;
import util.KEYWORDS;

public class TutorialFragment extends Fragment implements View.OnClickListener{

    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;

    private InputMethodManager imm;

    private int pos;

    View rootView;

    private Button _start;
    private ImageView _iv;

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.submit:
                getActivity().finish();
                prefEditor.putBoolean("tutorial", false);
                prefEditor.commit();
            default: break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = getContext().getSharedPreferences("BIDINFO", getActivity().MODE_PRIVATE);
        prefEditor = pref.edit();

        rootView = inflater.inflate(R.layout.fragment_tutorial, container, false);

        int position = getArguments().getInt("key");
        pos = position;

        _start = (Button)rootView.findViewById(R.id.submit);
        _start.setOnClickListener(this);

        _iv = (ImageView)rootView.findViewById(R.id.tutorialview);
        _iv.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        switch (position){
            case 0:
                _iv.setImageDrawable(getResources().getDrawable(R.drawable.tutorial_01));
                break;
            case 1:
                _iv.setImageDrawable(getResources().getDrawable(R.drawable.tutorial_02));
                break;
            case 2:
                _iv.setImageDrawable(getResources().getDrawable(R.drawable.tutorial_03));
                break;
            case 3:
                _iv.setImageDrawable(getResources().getDrawable(R.drawable.tutorial_04));
                break;
            case 4:
                _iv.setImageDrawable(getResources().getDrawable(R.drawable.tutorial_05));
                break;
            case 5:
                _iv.setImageDrawable(getResources().getDrawable(R.drawable.tutorial_06));
                break;
            case 6:
                _iv.setImageDrawable(getResources().getDrawable(R.drawable.tutorial_07));
                break;
        }
        if(position == 6) _start.setVisibility(View.VISIBLE);
        else _start.setVisibility(View.INVISIBLE);

        return rootView;
    }
}
