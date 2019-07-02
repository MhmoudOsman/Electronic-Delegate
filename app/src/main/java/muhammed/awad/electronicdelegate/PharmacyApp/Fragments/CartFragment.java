package muhammed.awad.electronicdelegate.PharmacyApp.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;
import com.victor.loading.rotate.RotateLoading;

import de.hdodenhof.circleimageview.CircleImageView;
import muhammed.awad.electronicdelegate.Models.MedicineModel;
import muhammed.awad.electronicdelegate.PharmacyApp.PharmacyMedActivity;
import muhammed.awad.electronicdelegate.R;

public class CartFragment extends Fragment {

    View view;

    FloatingActionButton add_new_pharmaceutical;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<MedicineModel, StoreViewholder> firebaseRecyclerAdapter;

    RotateLoading rotateLoading;

    public final static String EXTRA_EDIT_PHARMA = "edit_pharmacy_pharma";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.cart_fragment, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = view.findViewById(R.id.doctors_recyclerview);
        rotateLoading = view.findViewById(R.id.rotateloading);
        add_new_pharmaceutical = view.findViewById(R.id.add_new_pharmaceutical);

        add_new_pharmaceutical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PharmacyMedActivity.class);
                startActivity(intent);
            }
        });

        rotateLoading.start();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.keepSynced(true);

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        DisplayAllMedicines();
    }

    private void DisplayAllMedicines() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("PharmaciesStores")
                .child(getUID())
                .limitToLast(50);

        FirebaseRecyclerOptions<MedicineModel> options =
                new FirebaseRecyclerOptions.Builder<MedicineModel>()
                        .setQuery(query, MedicineModel.class)
                        .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<MedicineModel, StoreViewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final StoreViewholder holder, int position, @NonNull final MedicineModel model) {
                rotateLoading.stop();

                final String key = getRef(position).getKey();


                holder.more_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popup = new PopupMenu(getContext(), holder.more_btn);
                        //Inflating the Popup using xml file
                        popup.getMenuInflater()
                                .inflate(R.menu.more_menu, popup.getMenu());

                        //registering popup with OnMenuItemClickListener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.edit:
                                        Intent intent = new Intent(getContext(), PharmacyMedActivity.class);
                                        intent.putExtra(EXTRA_EDIT_PHARMA, key);
                                        startActivity(intent);
                                        return true;
                                    case R.id.delete:
                                        showDeleteDialog(key);
                                        return true;
                                    default:
                                        return true;
                                }
                            }
                        });

                        popup.show(); //showing popup menu
                    }
                });

                holder.BindPlaces(model);
            }

            @NonNull
            @Override
            public StoreViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.pharmaceutical_item, parent, false);
                return new StoreViewholder(view);
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
        rotateLoading.stop();
    }

    public static class StoreViewholder extends RecyclerView.ViewHolder {
        CircleImageView medicine_image;
        TextView medicine_name,pharma_name, medicine_price,q;
        ImageView more_btn;
        LinearLayout c;

        StoreViewholder(View itemView) {
            super(itemView);

            medicine_image = itemView.findViewById(R.id.medicine_image);
            medicine_name = itemView.findViewById(R.id.medicine_name);
            medicine_price = itemView.findViewById(R.id.medicine_price);
            pharma_name = itemView.findViewById(R.id.phar_name);
            more_btn = itemView.findViewById(R.id.more_btn);
            q = itemView.findViewById(R.id.item_num);
            c = itemView.findViewById(R.id.pharmacitical_card);
            c.setBackgroundColor(Color.parseColor("#33BC99"));
            pharma_name.setVisibility(View.GONE);
        }

        void BindPlaces(final MedicineModel medicineModel) {
            medicine_name.setText(medicineModel.getName());
            medicine_price.setText("Price : " + medicineModel.getCustomer_price());
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

    public String getUID() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        return userId;
    }

    private void showDeleteDialog(final String key) {
        final Dialog dialog = new Dialog(getContext());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.delete_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes();
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        Button yes_btn = dialog.findViewById(R.id.yes_btn);
        Button cancel_btn = dialog.findViewById(R.id.cancel_btn);
        yes_btn.setBackgroundColor(Color.parseColor("#33BC99"));
        cancel_btn.setBackgroundColor(Color.parseColor("#33BC99"));

        yes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(key);
                dialog.dismiss();
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void delete(String key) {

        databaseReference.child("PharmaciesStores").child(getUID()).child(key).removeValue();
        databaseReference.child("AllPharmaciesMedicine").child(key).removeValue();
        Toast.makeText(getContext(), "Deleted ..", Toast.LENGTH_SHORT).show();
        /*Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);*/
    }
}
