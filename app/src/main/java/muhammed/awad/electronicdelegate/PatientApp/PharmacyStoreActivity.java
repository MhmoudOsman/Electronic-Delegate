package muhammed.awad.electronicdelegate.PatientApp;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import muhammed.awad.electronicdelegate.Models.MedicineModel;
import muhammed.awad.electronicdelegate.PatientApp.Fragments.PharmaciesFragment;
import muhammed.awad.electronicdelegate.R;

import static muhammed.awad.electronicdelegate.PharmacyApp.Fragments.AllPharmaceuticalsFragment.EXTRA_PHARMACEUTICAL_ADD_TO_CART_KEY;

public class PharmacyStoreActivity extends AppCompatActivity {

    String KEY;
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<MedicineModel, pharmaceuticalViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_store);

        KEY = getIntent().getStringExtra(PharmaciesFragment.PH_KEY);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        recyclerView = findViewById(R.id.recyclerview);

        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        DisplayAllMedicines(KEY);
    }

    private void DisplayAllMedicines(String key) {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("PharmaciesStores")
                .child(key)
                .limitToLast(50);

        FirebaseRecyclerOptions<MedicineModel> options =
                new FirebaseRecyclerOptions.Builder<MedicineModel>()
                        .setQuery(query, MedicineModel.class)
                        .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<MedicineModel, pharmaceuticalViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull pharmaceuticalViewHolder holder, int position, @NonNull final MedicineModel model) {
                final String key = getRef(position).getKey();

                holder.details.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(getApplicationContext(), CheckActivity.class);
//                        startActivity(intent);

                        Intent intent = new Intent(PharmacyStoreActivity.this, PatientAddToCartActivity.class);
                        intent.putExtra(EXTRA_PHARMACEUTICAL_ADD_TO_CART_KEY, KEY);
                        intent.putExtra("ORDER_KEY", key);
                        startActivity(intent);
                    }
                });

                holder.BindPlaces(model);
            }

            @NonNull
            @Override
            public pharmaceuticalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.pharmaceutical_item, parent, false);
                return new pharmaceuticalViewHolder(view);

            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class pharmaceuticalViewHolder extends RecyclerView.ViewHolder {
        CircleImageView medicine_image;
        ImageView more_btn;
        LinearLayout color;
        TextView medicine_name, medicine_price, phar_name, q;
        MaterialRippleLayout details;

        pharmaceuticalViewHolder(View itemView) {
            super(itemView);

            medicine_image = itemView.findViewById(R.id.medicine_image);
            medicine_name = itemView.findViewById(R.id.medicine_name);
            medicine_price = itemView.findViewById(R.id.medicine_price);
            phar_name = itemView.findViewById(R.id.phar_name);
            details = itemView.findViewById(R.id.details_btn);
            color = itemView.findViewById(R.id.pharmacitical_card);
            more_btn= itemView.findViewById(R.id.more_btn);
            q = itemView.findViewById(R.id.item_num);

            more_btn.setVisibility(View.GONE);

            color.setBackgroundColor(Color.parseColor("#70E3FF"));
        }

        void BindPlaces(final MedicineModel medicineModel) {
            medicine_name.setText(medicineModel.getName());
            medicine_price.setText("Price : " + medicineModel.getCustomer_price());
            phar_name.setText("From : " + medicineModel.getCompany_name());
            q.setText("Quantity : " + medicineModel.getQuantity());

            Picasso.get()
                    .load(medicineModel.getImageurl())
                    .placeholder(R.drawable.addphoto)
                    .error(R.drawable.addphoto)
                    .into(medicine_image);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.stopListening();
        }
    }

}
