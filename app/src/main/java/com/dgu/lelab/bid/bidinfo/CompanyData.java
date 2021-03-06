package com.dgu.lelab.bid.bidinfo;

public class CompanyData {
    public int id = 0, symbol = 0, Pnum = 0;
    public String Name = "", Rnum = "", Rprt = "", Charge = "";
    public String Addr = "", Phone = "", Email = "", Divs = "", Divl = "";
    public String Expl = "", Date = "", hid = "";

    public CompanyData(int id, int pnum, String name, String rnum, String rprt, String charge, String addr, String phone, String email, String expl, String date, String hid) {
        this.id = id;
        this.Pnum = pnum;
        this.Name = name;
        this.Rnum = rnum;
        this.Rprt = rprt;
        this.Charge = charge;
        this.Addr = addr;
        this.Phone = phone;
        this.Email = email;
        this.Expl = expl;
        this.Date = date;
        this.hid = hid;
    }

    public CompanyData(int id, String name, String phone, String addr, String hid) {
        this.id = id;
        this.Name = name;
        this.Addr = addr;
        this.Phone = phone;
        this.hid = hid;
    }

    public CompanyData clone(){
        return new CompanyData(this.id, this.Pnum,  this.Name, this.Rnum, this.Rprt,  this.Charge,  this.Addr, this.Phone, this.Email, this.Expl, this.Date, this.hid);
    }

}
