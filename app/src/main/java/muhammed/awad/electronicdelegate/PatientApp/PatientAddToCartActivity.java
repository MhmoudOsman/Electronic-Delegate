package muhammed.awad.electronicdelegate.PatientApp;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.victor.loading.rotate.RotateLoading;

import de.hdodenhof.circleimageview.CircleImageView;
import muhammed.awad.electronicdelegate.Caching.CartMedicineManager;
import muhammed.awad.electronicdelegate.MainActivity;
import muhammed.awad.electronicdelegate.Models.CompanyModel;
import muhammed.awad.electronicdelegate.Models.MedicineCartModel;
import muhammed.awad.electronicdelegate.Models.MedicineModel;
import muhammed.awad.electronicdelegate.PharmacyApp.Fragments.AllPharmaceuticalsFragment;
import muhammed.awad.electronicdelegate.R;

public class PatientAddToCartActivity extends AppCompatActivity {

    String KEY, ORDER_KEY;
    CircleImageView pharmaceutical_image;
    int quantity;
    TextView pharmaceutical_name, pharmaceutical_price, pharmaceutical_info, quantity_txt, pharmaceutical_company_name_field, customer_price_field, cat, typ;
    Button add_pharmaceutical_btn;
    String pharma_image, pharma_name, pharma_location, company_uid, order_name, pharma_price, c, t, info, price;
    FloatingActionButton minus_btn, add_btn;
    String comp;

    int i = 0;
    double o = 0;

    RotateLoading rotateLoading;

    Uri photoPath;
    String selected_image_url;


    FirebaseStorage firebaseStorage;
    StorageReference storageReference;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_add_to_cart);

        KEY = getIntent().getStringExtra(AllPharmaceuticalsFragment.EXTRA_PHARMACEUTICAL_ADD_TO_CART_KEY);
        ORDER_KEY = getIntent().getStringExtra("ORDER_KEY");

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.keepSynced(true);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("Images");


        pharmaceutical_image = findViewById(R.id.pharmaceutical_image);
        pharmaceutical_name = findViewById(R.id.pharmaceutical_name_field);
        pharmaceutical_price = findViewById(R.id.pharmaceutical_price_field);
        customer_price_field = findViewById(R.id.customer_price_field);
        pharmaceutical_info = findViewById(R.id.pharmaceutical_info_field);
        pharmaceutical_company_name_field = findViewById(R.id.pharmaceutical_company_name_field);
        cat = findViewById(R.id.typ);
        typ = findViewById(R.id.cate);
        quantity_txt = findViewById(R.id.quantity_txt);
        pharmaceutical_price.setVisibility(View.GONE);

        add_pharmaceutical_btn = findViewById(R.id.addtocart_btn);

        minus_btn = findViewById(R.id.minus_btn);
        add_btn = findViewById(R.id.add_btn);

        rotateLoading = findViewById(R.id.rotateloading);

        rotateLoading.start();

        returnData();
        returnData(KEY);

        quantity_txt.setText("" + i);

        minus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i ==0 ) {
                    Toast.makeText(getApplicationContext(), "can't order less than 0 item", Toast.LENGTH_SHORT).show();
                } else {
                    i = i - 1;
                    quantity_txt.setText("" + i);
                }
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == quantity) {
                    Toast.makeText(getApplicationContext(), "The quantity is more than that in the store", Toast.LENGTH_SHORT).show();
                } else {
                    i = i + 1;
                    quantity_txt.setText("" + i);
                }
            }
        });

        add_pharmaceutical_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 0) {
                    Toast.makeText(getApplicationContext(), "you can't order 0 item", Toast.LENGTH_SHORT).show();
                } else {
                    double all = i * o;
                    String allprice = String.valueOf(all);

                    String pay = null;

                    updatePharmaceuticaltoDB(pharma_image,info, order_name, price,comp, company_uid, pharma_price, quantity - i, c, t);
                    AddToDb(pharma_image, order_name, pharma_name, allprice, pharma_location, "" + i, pay);

                }
            }
        });
    }

    private void updatePharmaceuticaltoDB(String imageurl, String info, String name, String price, String company, String uid, String cust_price, int quntaty,String category, String type) {
        MedicineModel medicineModel = new MedicineModel(imageurl, info, name, price, company, uid, cust_price, quntaty,category, type);

        databaseReference.child("PharmaciesStores").child(getUID()).child(KEY).setValue(medicineModel);
        databaseReference.child("AllPharmaciesMedicine").child(KEY).setValue(medicineModel);

    }


    public void AddToDb(String image, String name, String pharmacy, String price, String location, String q, String pay) {
        MedicineCartModel medicineCartModel = new MedicineCartModel(company_uid, image, name, pharmacy, price, location, q, pay);
        CartMedicineManager.getInstance().addMedicineCartModels(medicineCartModel);

        Toast.makeText(getApplicationContext(), "Add to cart.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), PatientMainActivity.class);
        startActivity(intent);
    }

    public void returnData(String key) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);

        mDatabase.child("PharmaciesStores").child(key).child(ORDER_KEY).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        rotateLoading.stop();
                        // Get user value
                        MedicineModel medicineModel = dataSnapshot.getValue(MedicineModel.class);

                        pharma_image = medicineModel.getImageurl();
                        order_name = medicineModel.getName();
                        pharma_price = medicineModel.getCustomer_price();
                        quantity = medicineModel.getQuantity();
                        c = medicineModel.getCategory();
                        t = medicineModel.getType();
                        info = medicineModel.getInfo();
                        price = medicineModel.getPrice();
                        comp = medicineModel.getCompany_name();


                        String string = pharma_price;

                        String[] parts = string.split(" ");
                        String pr = parts[0];

                        o = Double.parseDouble(pr);

                        pharmaceutical_name.setText("Name : " + medicineModel.getName());
                        customer_price_field.setText("Price : " + medicineModel.getCustomer_price());
                        pharmaceutical_info.setText(medicineModel.getInfo());
                        pharmaceutical_company_name_field.setText("From : " + medicineModel.getCompany_name());
                        cat.setText(medicineModel.getCategory());
                        typ.setText(medicineModel.getType());
                        company_uid = medicineModel.getCompany_uid();

                        Picasso.get()
                                .load(medicineModel.getImageurl())
                                .placeholder(R.drawable.addphoto)
                                .error(R.drawable.addphoto)
                                .into(pharmaceutical_image);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "can\'t fetch data", Toast.LENGTH_SHORT).show();
                        rotateLoading.stop();
                    }
                });
    }

    public void returnData() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);

        mDatabase.child("AllUsers").child("Patients").child(getUID()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        CompanyModel companyModel = dataSnapshot.getValue(CompanyModel.class);

                        pharma_location = companyModel.getGovernorate() + ", " + companyModel.getDistrict() + ", " + companyModel.getStreet() + ", " + companyModel.getBuilding();
                        pharma_name = companyModel.getTitle();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "can\'t fetch data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public String getUID() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        return userId;
    }
}
