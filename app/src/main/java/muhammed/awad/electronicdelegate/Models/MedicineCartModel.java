package muhammed.awad.electronicdelegate.Models;

public class MedicineCartModel {

    private String company_uid;
    private String order_image;
    private String order_name;
    private String pharmacy_name;
    private String company_name;
    private String order_price;
    private String order_location;
    private String order_quantity;
    private String payment;

    public MedicineCartModel(String company_uid, String order_image, String order_name, String pharmacy_name, String order_price, String order_location, String order_quantity, String pay) {
        this.company_uid = company_uid;
        this.order_image = order_image;
        this.order_name = order_name;
        this.pharmacy_name = pharmacy_name;
        this.order_price = order_price;
        this.order_location = order_location;
        this.order_quantity = order_quantity;
        this.payment = pay;
    }



  /*  public MedicineCartModel(String company_uid, String order_image, String order_name, String company_name, String order_price, String order_quantity, String pay) {
        this.company_uid = company_uid;
        this.order_image = order_image;
        this.order_name = order_name;
        this.company_name = company_name;
        this.order_price = order_price;
        this.order_quantity = order_quantity;
        this.payment = pay;

    }

    public MedicineCartModel() {
    }*/

    public String getCompany_uid() {
        return company_uid;
    }

    public void setCompany_uid(String company_uid) {
        this.company_uid = company_uid;
    }

    public String getOrder_image() {
        return order_image;
    }

    public void setOrder_image(String order_image) {
        this.order_image = order_image;
    }

    public String getOrder_name() {
        return order_name;
    }

    public void setOrder_name(String order_name) {
        this.order_name = order_name;
    }

    public String getPharmacy_name() {
        return pharmacy_name;
    }

    public void setPharmacy_name(String pharmacy_name) {
        this.pharmacy_name = pharmacy_name;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getOrder_price() {
        return order_price;
    }

    public void setOrder_price(String order_price) {
        this.order_price = order_price;
    }

    public String getOrder_location() {
        return order_location;
    }

    public void setOrder_location(String order_location) {
        this.order_location = order_location;
    }

    public String getOrder_quantity() {
        return order_quantity;
    }

    public void setOrder_quantity(String order_quantity) {
        this.order_quantity = order_quantity;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }
}
