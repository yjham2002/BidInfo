package com.dgu.lelab.bid.bidinfo;

public class BidInfo {
    /**
     * <입찰종류 컨벤션>
     - 입찰정보(물품0/공사1/용역2/미분류3)
     - 계약정보(물품4/공사5/용역6/미분류7)
     * Bidinfo_bidlist 입찰종류에 들어갈 분류 번호이니 크롤러 코딩 시 필수참조바랍니다.
     *
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
    public int like = 3;
    public int comment = 11;
    public int id = -1;
    public int Type = -1;
    public String Url = "http://www.naver.com/";
    public String Title = "SW) 모바일 애플리케이션 개발 공고";
    public String Refer = "";
    public String BidNo = "";
    public String Bstart = "";
    public String Bexpire = "";
    public String PDate = "";
    public String Dept = "";
    public String Charge = "";
    public String Date = "";

}
