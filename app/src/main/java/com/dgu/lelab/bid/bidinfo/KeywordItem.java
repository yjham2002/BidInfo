package com.dgu.lelab.bid.bidinfo;

public class KeywordItem {
    public String keyword = "#";
    public boolean isSelected = false;
    public KeywordItem(String s){
        keyword = s;
        isSelected = false;
    }
    public KeywordItem(String s, boolean b){
        keyword = s;
        isSelected = b;
    }
}
