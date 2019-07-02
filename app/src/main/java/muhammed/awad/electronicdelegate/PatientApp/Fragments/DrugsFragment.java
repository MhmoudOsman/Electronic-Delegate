package muhammed.awad.electronicdelegate.PatientApp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import muhammed.awad.electronicdelegate.CompanyApp.Fragments.NewsFragment;
import muhammed.awad.electronicdelegate.CompanyApp.Fragments.PharmaceuticalFragment;
import muhammed.awad.electronicdelegate.Models.MedicineModel;
import muhammed.awad.electronicdelegate.PatientApp.CheckActivity;
import muhammed.awad.electronicdelegate.R;

public class DrugsFragment extends Fragment {
    View view;

    RecyclerView recyclerView;
    EditText search_field;
    ImageView search_btn;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<MedicineModel, pharmaceuticalViewholder> firebaseRecyclerAdapter;
    FirebaseRecyclerAdapter<MedicineModel, pharmaceuticalViewholder> firebaseRecyclerAdapter2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.patient_drugs_fragment, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        recyclerView = view.findViewById(R.id.recyclerview);
        search_field = view.findViewById(R.id.search_field);
        search_btn = view.findViewById(R.id.search_btn);

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        DisplayallMedicines();

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = search_field.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getContext(), "please enter drug name", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (firebaseRecyclerAdapter2 != null) {
                    firebaseRecyclerAdapter2.startListening();
                }

                if (firebaseRecyclerAdapter != null) {
                    firebaseRecyclerAdapter.stopListening();
                }

                DisplayallMedicines2(name);
            }
        });
    }

    private void DisplayallMedicines() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("AllPharmaciesMedicine")
                .limitToLast(50);

        FirebaseRecyclerOptions<MedicineModel> options =
                new FirebaseRecyclerOptions.Builder<MedicineModel>()
                        .setQuery(query, MedicineModel.class)
                        .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<MedicineModel, pharmaceuticalViewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull pharmaceuticalViewholder holder, int position, @NonNull final MedicineModel model) {
                final String key = getRef(position).getKey();

                holder.details.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), CheckActivity.class);
                        startActivity(intent);
                    }
                });

                holder.BindPlaces(model);
            }

            @NonNull
            @Override
            public pharmaceuticalViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.pharmaceutical_item, parent, false);
                return new pharmaceuticalViewholder(view);
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void DisplayallMedicines2(String name) {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("PhDrugsByName")
                .child(name)
                .limitToLast(50);

        FirebaseRecyclerOptions<MedicineModel> options =
                new FirebaseRecyclerOptions.Builder<MedicineModel>()
                        .setQuery(query, MedicineModel.class)
                        .build();

        firebaseRecyclerAdapter2 = new FirebaseRecyclerAdapter<MedicineModel, pharmaceuticalViewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull pharmaceuticalViewholder holder, int position, @NonNull final MedicineModel model) {
                final String key = getRef(position).getKey();

                holder.details.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), CheckActivity.class);
                        startActivity(intent);
                    }
                });

                holder.BindPlaces(model);
            }

            @NonNull
            @Override
            public pharmaceuticalViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.pharmaceutical_item, parent, false);
                return new pharmaceuticalViewholder(view);
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter2);
    }

    public static class pharmaceuticalViewholder extends RecyclerView.ViewHolder {
        CircleImageView medicine_image;
        TextView medicine_name, medicine_price, phar_name;
        MaterialRippleLayout details;
        ImageView imageView;

        pharmaceuticalViewholder(View itemView) {
            super(itemView);

            medicine_image = itemView.findViewById(R.id.medicine_image);
            medicine_name = itemView.findViewById(R.id.medicine_name);
            medicine_price = itemView.findViewById(R.id.medicine_price);
            details = itemView.findViewById(R.id.details_btn);
            imageView = itemView.findViewById(R.id.more_btn);
            phar_name = itemView.findViewById(R.id.phar_name);

            imageView.setVisibility(View.GONE);
        }

        void BindPlaces(final MedicineModel medicineModel) {
            medicine_name.setText(medicineModel.getName());
            medicine_price.setText("Price : " + medicineModel.getPrice());
            phar_name.setText("From Pharmacy : " + medicineModel.getCompany_name());

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
