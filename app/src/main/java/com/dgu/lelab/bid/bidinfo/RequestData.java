package com.dgu.lelab.bid.bidinfo;

public class RequestData {
    public int id;
    public int mid;
    public String Draw;
    public String Date;
    public String Phone;

    public RequestData(int id, int mid, String draw, String date, String phone) {
        this.id = id;
        this.mid = mid;
        Draw = draw;
        Date = date;
        Phone = phone;
    }
}
