package com.dgu.lelab.bid.bidinfo;

public class BidInfo {
    /**
     * <입찰종류 컨벤션>
     - 입찰정보(물품0/공사1/용역2/미분류3)
     - 계약정보(물품4/공사5/용역6/미분류7)
     - 민간정보(물품8/공사9/용역10/미분류11)

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

    /**
    * {"id":1,"Type":3,"Url":"www.naver.com","Title":"테스팅 입찰 정보 개체 댓글테스터",
     * "Refer":"네이버","BidNo":"19850-192","Bstart":"2016-11-02","Bexpire":"2016-11-30",
     * "PDate":"2016-10-20","Dept":"조달청","Charge":"홍길동","Date":"2016-10-07T12:43:52.000Z","likecount":1,"commentcount":7}]
    * */

    public BidInfo clone(){
        return new BidInfo(this.like, this.comment, this.id, this.Type, this.Url, this.Title, this.Refer, this.BidNo, this.Bstart, this.Bexpire, this.PDate, this.Dept, this.Charge, this.Date);
    }

    public BidInfo(int like, int comment, int id, int type, String url, String title, String refer, String bidNo, String bstart, String bexpire, String PDate, String dept, String charge, String date) {
        this.like = like;
        this.comment = comment;
        this.id = id;
        this.Type = type;
        this.Url = url;
        this.Title = title;
        this.Refer = refer;
        this.BidNo = bidNo;
        this.Bstart = bstart;
        this.Bexpire = bexpire;
        this.PDate = PDate;
        this.Dept = dept;
        this.Charge = charge;
        this.Date = date;
    }

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
