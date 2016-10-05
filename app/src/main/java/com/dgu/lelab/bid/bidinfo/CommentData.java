package com.dgu.lelab.bid.bidinfo;

public class CommentData {
    public int id;
    public String userId;
    public String content;
    public String date;
    public String amount;
    public CommentData(int id, String userId, String content, String date, String amount){
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.date = date;
        this.amount = amount;
    }
}
