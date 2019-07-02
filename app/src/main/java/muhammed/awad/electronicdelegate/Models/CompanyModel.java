package muhammed.awad.electronicdelegate.Models;

import java.util.HashMap;

public class CompanyModel {

    private String fullname, email, mobile, building, street, district, governorate, title;

    public CompanyModel() {
    }

    public CompanyModel(String fullname, String email, String mobile, String building, String street, String district, String governorate, String title) {
        this.fullname = fullname;
        this.email = email;
        this.mobile = mobile;
        this.building = building;
        this.street = street;
        this.district = district;
        this.governorate = governorate;
        this.title = title;
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

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getGovernorate() {
        return governorate;
    }

    public void setGovernorate(String governorate) {
        this.governorate = governorate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
