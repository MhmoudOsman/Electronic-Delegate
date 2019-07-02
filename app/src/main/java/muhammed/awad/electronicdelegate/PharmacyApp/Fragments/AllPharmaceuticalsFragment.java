package muhammed.awad.electronicdelegate.PharmacyApp.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;
import com.victor.loading.rotate.RotateLoading;

import de.hdodenhof.circleimageview.CircleImageView;
import muhammed.awad.electronicdelegate.Models.MedicineModel;
import muhammed.awad.electronicdelegate.PharmacyApp.PharmacyAddToCartActivity;
import muhammed.awad.electronicdelegate.R;

public class AllPharmaceuticalsFragment extends Fragment {

    View view;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<MedicineModel, AllpharmaceuticalViewholder> firebaseRecyclerAdapter;

    RotateLoading rotateLoading;

    public static final String EXTRA_PHARMACEUTICAL_ADD_TO_CART_KEY = "add_to_cart_key";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.all_pharmaceuticals_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = view.findViewById(R.id.doctors_recyclerview);
        rotateLoading = view.findViewById(R.id.rotateloading);

        rotateLoading.start();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.keepSynced(true);

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        DisplayallMedicines();
    }

    private void DisplayallMedicines() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Allpharmaceutical")
                .limitToLast(50);

        FirebaseRecyclerOptions<MedicineModel> options =
                new FirebaseRecyclerOptions.Builder<MedicineModel>()
                        .setQuery(query, MedicineModel.class)
                        .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<MedicineModel, AllpharmaceuticalViewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AllpharmaceuticalViewholder holder, int position, @NonNull final MedicineModel model) {
                rotateLoading.stop();

                final String key = getRef(position).getKey();

                holder.details.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), PharmacyAddToCartActivity.class);
                        intent.putExtra(EXTRA_PHARMACEUTICAL_ADD_TO_CART_KEY, key);
                        startActivity(intent);
                    }
                });

                holder.BindPlaces(model);
            }

            @NonNull
            @Override
            public AllpharmaceuticalViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.pharmacy_pharmaceutical_item, parent, false);
                return new AllpharmaceuticalViewholder(view);
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
        rotateLoading.stop();
    }

    public static class AllpharmaceuticalViewholder extends RecyclerView.ViewHolder {
        CircleImageView medicine_image;
        TextView medicine_name, medicine_price, medicine_company ,quntaty;
        MaterialRippleLayout details;
        LinearLayout lColor;

        AllpharmaceuticalViewholder(View itemView) {
            super(itemView);

            medicine_image = itemView.findViewById(R.id.medicine_image);
            medicine_name = itemView.findViewById(R.id.medicine_name);
            medicine_price = itemView.findViewById(R.id.medicine_price);
            medicine_company = itemView.findViewById(R.id.medicine_company);
            quntaty = itemView.findViewById(R.id.item_num);
            details = itemView.findViewById(R.id.details_btn);
            lColor = itemView.findViewById(R.id.lyout_item);
        }

        void BindPlaces(final MedicineModel medicineModel) {
            medicine_name.setText(medicineModel.getName());
            medicine_price.setText("Price : " + medicineModel.getPrice());
            medicine_company.setText("From : " + medicineModel.getCompany_name());
            quntaty.setText("Quantity : " + medicineModel.getQuantity() + " Item");
            lColor.setBackgroundColor(Color.parseColor("#33BC99"));

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
