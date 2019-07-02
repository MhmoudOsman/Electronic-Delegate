package muhammed.awad.electronicdelegate.Models;

public class PatientOrderModel {

    private String id;
    private String order_image;
    private String order_name;
    private String patient_name;
    private String order_price;
    private String order_location;
    private String order_quantity;
    private String payment;
    private String state;



    public PatientOrderModel(String order_image, String order_name, String patient_name, String order_price, String order_location, String order_quantity, String payment) {
        this.order_image = order_image;
        this.order_name = order_name;
        this.patient_name = patient_name;
        this.order_price = order_price;
        this.order_location = order_location;
        this.order_quantity = order_quantity;
        this.payment = payment;
    }

    public PatientOrderModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
