package com.dgu.lelab.bid.bidinfo;

public class CompanyData {
    public int id = 0, symbol = 0, Pnum = 0;
    public String Name = "", Rnum = "", Rprt = "", Charge = "";
    public String Addr = "", Phone = "", Email = "", Divs = "", Divl = "";
    public String Expl = "", Date = "", hid = "";

    public CompanyData(int id, int symbol, int pnum, String name, String rnum, String rprt, String charge, String addr, String phone, String email, String divs, String divl, String expl, String date, String hid) {
        this.id = id;
        this.symbol = symbol;
        this.Pnum = pnum;
        this.Name = name;
        this.Rnum = rnum;
        this.Rprt = rprt;
        this.Charge = charge;
        this.Addr = addr;
        this.Phone = phone;
        this.Email = email;
        this.Divs = divs;
        this.Divl = divl;
        this.Expl = expl;
        this.Date = date;
        this.hid = hid;
    }

    public CompanyData clone(){
        return new CompanyData(this.id, this.symbol, this.Pnum,  this.Name, this.Rnum, this.Rprt,  this.Charge,  this.Addr, this.Phone, this.Email, this.Divs, this.Divl, this.Expl, this.Date, this.hid);
    }

}
