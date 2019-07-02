package muhammed.awad.electronicdelegate.CompanyApp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import muhammed.awad.electronicdelegate.Models.PharmacyModel;
import muhammed.awad.electronicdelegate.Presenters.Adapters.PharmacyAdapter;
import muhammed.awad.electronicdelegate.R;

public class RequestsFragment extends Fragment {

    View view;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    // FirebaseRecyclerAdapter<PharmacyOrderModel, OrderViewHolder> firebaseRecyclerAdapter;
    List<PharmacyModel> pharmacyModels;

    private PharmacyAdapter pharmacyAdapter;
    private Context context;

    // RotateLoading rotateLoading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.requests_fragment, container, false);
        context = getContext();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = view.findViewById(R.id.doctors_recyclerview);
        // rotateLoading = view.findViewById(R.id.rotateloading);

        // rotateLoading.start();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.keepSynced(true);

        pharmacyModels = new ArrayList<>();
        pharmacyAdapter = new PharmacyAdapter(pharmacyModels, context);

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(pharmacyAdapter);

        // DisplayAllMedicines();
        DisplayAllPharmacies();
    }

    private void DisplayAllPharmacies() {
//        Query query = FirebaseDatabase.getInstance()
//                .getReference()
//                .child("requests")
//                .child(getUID());

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("requests").child(getUID());
        databaseReference.keepSynced(true);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    final String id = data.getKey();

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference("AllUsers/Pharmacies/" + id);
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            PharmacyModel pharmacyModel = dataSnapshot.getValue(PharmacyModel.class);
                            pharmacyModel.setId(id);
                            pharmacyModels.add(pharmacyModel);

                            pharmacyAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            System.out.println("The read failed: " + databaseError.getCode());
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                    final String id = data.getKey();
//
//                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
//                    DatabaseReference ref = database.getReference("AllUsers/Pharmacies/" + id);
//                    ref.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            PharmacyModel pharmacyModel = dataSnapshot.getValue(PharmacyModel.class);
//                            pharmacyModel.setId(id);
//                            pharmacyModels.add(pharmacyModel);
//
//                            pharmacyAdapter.notifyDataSetChanged();
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                            System.out.println("The read failed: " + databaseError.getCode());
//                        }
//                    });
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

//        FirebaseRecyclerOptions<PharmacyModel> options =
//                new FirebaseRecyclerOptions.Builder<PharmacyModel>()
//                        .setQuery(query, PharmacyModel.class)
//                        .build();
//
//        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PharmacyOrderModel, OrderViewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull final PharmacyOrderModel model) {
//                rotateLoading.stop();
//
//                String key = getRef(position).getKey();
//
//                layout.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(getContext(), "Accept Clicked", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                holder.decline.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(getContext(), "Decline Clicked", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                holder.BindPlaces(model);
//            }
//
//            @NonNull
//            @Override
//            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(getContext()).inflate(R.layout.order_item, parent, false);
//                return new OrderViewHolder(view);
//            }
//        };
//
//        recyclerView.setAdapter(firebaseRecyclerAdapter);
//        rotateLoading.stop();
    }

//    private void DisplayAllMedicines() {
//        Query query = FirebaseDatabase.getInstance()
//                .getReference()
//                .child("requests")
//                .child(getUID())
//                .limitToLast(50);
//
//        FirebaseRecyclerOptions<PharmacyOrderModel> options =
//                new FirebaseRecyclerOptions.Builder<PharmacyOrderModel>()
//                        .setQuery(query, PharmacyOrderModel.class)
//                        .build();
//
//        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PharmacyOrderModel, OrderViewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull final PharmacyOrderModel model) {
//                rotateLoading.stop();
//
//                String key = getRef(position).getKey();
//
//                holder.accept.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(getContext(), "Accept Clicked", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                holder.decline.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(getContext(), "Decline Clicked", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                holder.BindPlaces(model);
//            }
//
//            @NonNull
//            @Override
//            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(getContext()).inflate(R.layout.order_item, parent, false);
//                return new OrderViewHolder(view);
//            }
//        };
//
//        recyclerView.setAdapter(firebaseRecyclerAdapter);
//        rotateLoading.stop();
//    }

//    public static class OrderViewHolder extends RecyclerView.ViewHolder {
//        CircleImageView order_image;
//        TextView order_name, pharmacy_name, order_location, order_price, order_q;
//        Button accept, decline;
//
//        OrderViewHolder(View itemView) {
//            super(itemView);
//
//            order_image = itemView.findViewById(R.id.order_image);
//            order_name = itemView.findViewById(R.id.order_name);
//            pharmacy_name = itemView.findViewById(R.id.order_pharmacy);
//            order_location = itemView.findViewById(R.id.order_location);
//            order_price = itemView.findViewById(R.id.order_price);
//            order_q = itemView.findViewById(R.id.order_q);
//            accept = itemView.findViewById(R.id.accept_btn);
//            decline = itemView.findViewById(R.id.decline_btn);
//        }
//
//        void BindPlaces(final PharmacyOrderModel orderModel) {
//            pharmacy_name.setText("From: " + orderModel.getPharmacy_name());
//            order_name.setText(orderModel.getOrder_name());
//            order_location.setText(orderModel.getOrder_location());
//            order_price.setText("Total: " + orderModel.getOrder_price() + " L.E");
//            order_q.setText(orderModel.getOrder_quantity() + " Items");
//
//            Picasso.get()
//                    .load(orderModel.getOrder_image())
//                    .placeholder(R.drawable.addphoto)
//                    .error(R.drawable.addphoto)
//                    .into(order_image);
//
//        }
//    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        if (firebaseRecyclerAdapter != null) {
//            firebaseRecyclerAdapter.startListening();
//        }
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (firebaseRecyclerAdapter != null) {
//            firebaseRecyclerAdapter.stopListening();
//        }
//    }

    public String getUID() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        return userId;
    }
}
