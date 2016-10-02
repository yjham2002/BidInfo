package com.dgu.lelab.bid.bidinfo;

public class BidInfo {
    public String text = "테스팅 내용";

    /**
     * @Info
     * id : The primary key as DB tuple
     * Type : Bid Type
     * Title : The main title of bid
     * Refer : Reference name
     * BidNo : official Bid Number
     * Bstart : Start time of bid
     * Bexpire : Expiration time of bid
     * Pdate : Time when the article has been posted
     * Dept : Department Name
     * Charge : The name of person who is in charge of bid
     * Date : Created Date
     */

    public int id = -1;
    public int Type = -1;
    public String Url = "http://www.pps.go.kr/kor/index.do";
    public String Title = "";
    public String Refer = "";
    public String BidNo = "";
    public String Bstart = "";
    public String Bexpire = "";
    public String PDate = "";
    public String Dept = "";
    public String Charge = "";
    public String Date = "";

}
