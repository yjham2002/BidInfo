package com.dgu.lelab.bid.bidinfo;

import android.app.Application;

import com.tsengvn.typekit.Typekit;

public class Yman extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "fonts/NanumGothic.ttf"))
                .addBold(Typekit.createFromAsset(this, "fonts/NanumGothicBold.ttf"));
    }
}