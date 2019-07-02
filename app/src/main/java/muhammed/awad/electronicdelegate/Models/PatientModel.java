package muhammed.awad.electronicdelegate.Models;

public class PatientModel {

    String id;
    String fullname, email, mobile, gov, dis, bu, st;

    public PatientModel() {
    }

    public PatientModel(String fullname, String email, String mobile, String gov, String dis, String bu, String st) {
        this.fullname = fullname;
        this.email = email;
        this.mobile = mobile;
        this.gov = gov;
        this.dis = dis;
        this.bu = bu;
        this.st = st;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGov() {
        return gov;
    }

    public void setGov(String gov) {
        this.gov = gov;
    }

    public String getDis() {
        return dis;
    }

    public void setDis(String dis) {
        this.dis = dis;
    }

    public String getBu() {
        return bu;
    }

    public void setBu(String bu) {
        this.bu = bu;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }
}
