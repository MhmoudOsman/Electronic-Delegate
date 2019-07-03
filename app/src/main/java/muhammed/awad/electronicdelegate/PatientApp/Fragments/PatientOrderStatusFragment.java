package muhammed.awad.electronicdelegate.PatientApp.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;
import com.victor.loading.rotate.RotateLoading;

import de.hdodenhof.circleimageview.CircleImageView;
import muhammed.awad.electronicdelegate.Models.PatientOrderModel;
import muhammed.awad.electronicdelegate.R;
import muhammed.awad.electronicdelegate.dialogs.ConfirmFawryDialog;

public class PatientOrderStatusFragment extends Fragment {

    View view;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<PatientOrderModel, OrderViewHolder> firebaseRecyclerAdapter;

    RotateLoading rotateLoading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_patient_order_status, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = view.findViewById(R.id.doctors_recyclerview);
        rotateLoading = view.findViewById(R.id.rotateloading);

        rotateLoading.start();

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        DisplayAllMedicines();
    }

    private void DisplayAllMedicines() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("PatientOrders")
                .child(getUID())
                .limitToLast(50);

        FirebaseRecyclerOptions<PatientOrderModel> options =
                new FirebaseRecyclerOptions.Builder<PatientOrderModel>()
                        .setQuery(query, PatientOrderModel.class)
                        .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PatientOrderModel, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, final int position, @NonNull final PatientOrderModel model) {
                rotateLoading.stop();

                final String key = getRef(position).getKey();
                holder.detailsBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (model.getState().equals("Deliverd")) {
                                Toast.makeText(getActivity(), "This order Deliverd.", Toast.LENGTH_SHORT).show();


                        } else {
                            Toast.makeText(getActivity(), "This order not Deliverd.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                holder.BindPlaces(model);
            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.order_item_state, parent, false);
                return new OrderViewHolder(view);
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
        rotateLoading.stop();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        CircleImageView order_image;
        MaterialRippleLayout detailsBtn;
        TextView order_name, pharmacy_name, order_location, order_price, order_q, payment, order_state;

        OrderViewHolder(View itemView) {
            super(itemView);

            detailsBtn = itemView.findViewById(R.id.details_btn);
            order_image = itemView.findViewById(R.id.order_image);
            order_name = itemView.findViewById(R.id.order_name);
            pharmacy_name = itemView.findViewById(R.id.order_pharmacy);
            order_location = itemView.findViewById(R.id.order_location);
            order_price = itemView.findViewById(R.id.order_price);
            order_q = itemView.findViewById(R.id.order_q);
            payment = itemView.findViewById(R.id.paym);
            order_state = itemView.findViewById(R.id.order_status);
        }

        void BindPlaces(final PatientOrderModel patientOrderModel) {
            pharmacy_name.setText("From: " + patientOrderModel.getPatient_name());
            order_name.setText(patientOrderModel.getOrder_name());
            order_location.setText(patientOrderModel.getOrder_location());
            order_price.setText("Total: " + patientOrderModel.getOrder_price() + " L.E");
            order_q.setText(patientOrderModel.getOrder_quantity() + " Items");
            payment.setText("pay by: " +patientOrderModel.getPayment());
            order_state.setText("Status: " + patientOrderModel.getState());

            Picasso.get()
                    .load(patientOrderModel.getOrder_image())
                    .placeholder(R.drawable.addphoto)
                    .error(R.drawable.addphoto)
                    .into(order_image);
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

    public String getUID() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        return userId;
    }
}
