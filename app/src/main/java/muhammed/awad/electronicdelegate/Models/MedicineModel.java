package muhammed.awad.electronicdelegate.Models;

public class MedicineModel {

    String imageurl, info, name, price, company_name, company_uid, customer_price, category, type;;
    int quantity;

    public MedicineModel() {
    }

    public MedicineModel(String imageurl, String info, String name, String price, String company_name, String company_uid, String customer_price, int quantity, String category, String type) {
        this.imageurl = imageurl;
        this.info = info;
        this.name = name;
        this.price = price;
        this.company_name = company_name;
        this.company_uid = company_uid;
        this.customer_price = customer_price;
        this.quantity = quantity;
        this.category = category;
        this.type = type;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_uid() {
        return company_uid;
    }

    public void setCompany_uid(String company_uid) {
        this.company_uid = company_uid;
    }

    public String getCustomer_price() {
        return customer_price;
    }

    public void setCustomer_price(String customer_price) {
        this.customer_price = customer_price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
