package muhammed.awad.electronicdelegate.PharmacyApp.Fragments;

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
import muhammed.awad.electronicdelegate.Models.PharmacyOrderModel;
import muhammed.awad.electronicdelegate.R;
import muhammed.awad.electronicdelegate.dialogs.ConfirmFawryDialog;


public class PharmacyOrderStatusFragment extends Fragment {

    View view;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<PharmacyOrderModel, OrderViewHolder> firebaseRecyclerAdapter;

    RotateLoading rotateLoading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pharmacy_order_status, container, false);
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
                .child("PharmacyOrders")
                .child(getUID())
                .limitToLast(50);

        FirebaseRecyclerOptions<PharmacyOrderModel> options =
                new FirebaseRecyclerOptions.Builder<PharmacyOrderModel>()
                        .setQuery(query, PharmacyOrderModel.class)
                        .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PharmacyOrderModel, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull final PharmacyOrderModel model) {
                rotateLoading.stop();

                final String key = getRef(position).getKey();
                holder.detailsBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (model.getState().equals("Deliverd")) {
                           /* if ( model.getPay().equals("Fawry")){

                                ConfirmFawryDialog.showDialog(getActivity(), key.substring(1, 11) + "", new ConfirmFawryDialog.DialogListener() {
                                    @Override
                                    public void onFawry() {
                                        Toast.makeText(getActivity(), "Payment Successful.", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }else{*/
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
        LinearLayout layout;
        MaterialRippleLayout detailsBtn;
        TextView order_name, pharmacy_name, order_location, order_price, order_q, order_state, pay;

        OrderViewHolder(View itemView) {
            super(itemView);

            detailsBtn = itemView.findViewById(R.id.details_btn);
            order_image = itemView.findViewById(R.id.order_image);
            order_name = itemView.findViewById(R.id.order_name);
            pharmacy_name = itemView.findViewById(R.id.order_pharmacy);
            order_location = itemView.findViewById(R.id.order_location);
            order_price = itemView.findViewById(R.id.order_price);
            order_q = itemView.findViewById(R.id.order_q);
            pay = itemView.findViewById(R.id.paym);
            layout = itemView.findViewById(R.id.layout);
            order_state = itemView.findViewById(R.id.order_status);
            layout.setBackgroundColor(Color.parseColor("#33BC99"));
        }

        void BindPlaces(final PharmacyOrderModel pharmacyOrderModel) {
            pharmacy_name.setText("From: " + pharmacyOrderModel.getPharmacy_name());
            order_name.setText(pharmacyOrderModel.getOrder_name());
            order_location.setText(pharmacyOrderModel.getOrder_location());
            order_price.setText("Total: " + pharmacyOrderModel.getOrder_price() + " L.E");
            order_q.setText(pharmacyOrderModel.getOrder_quantity() + " Items");
            pay.setText(pharmacyOrderModel.getPay());
            order_state.setText("Status: " + pharmacyOrderModel.getState());

            Picasso.get()
                    .load(pharmacyOrderModel.getOrder_image())
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
