package com.dgu.lelab.bid.bidinfo;

public class CommentData {
    public int id;
    public int mid;
    public String content;
    public String date;
    public String amount;
    public String userName;

    public CommentData clone(){
        return new CommentData(this.id, this.userName, this.content, this.date, this.amount, this.mid);
    }

    public CommentData(int id, String userName, String content, String date, String amount, int mid){
        this.id = id;
        this.userName = userName;
        this.content = content;
        this.date = date;
        this.amount = amount;
        this.mid = mid;
    }

}
