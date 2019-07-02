package muhammed.awad.electronicdelegate.PatientApp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

import muhammed.awad.electronicdelegate.Caching.CartMedicineManager;
import muhammed.awad.electronicdelegate.Models.MedicineCartModel;
import muhammed.awad.electronicdelegate.Models.MedicineModel;
import muhammed.awad.electronicdelegate.Models.PatientModel;
import muhammed.awad.electronicdelegate.Models.PatientOrderModel;
import muhammed.awad.electronicdelegate.PatientApp.PatientMainActivity;
import muhammed.awad.electronicdelegate.PharmacyApp.PharmacyMainActivity;
import muhammed.awad.electronicdelegate.Presenters.Adapters.MedicineCartAdapter;
import muhammed.awad.electronicdelegate.R;
import muhammed.awad.electronicdelegate.dialogs.CheckOutDialog;
import muhammed.awad.electronicdelegate.dialogs.FawryDialog;

public class PatientCartFragment extends Fragment {

    private RecyclerView recyclerView;
    private MedicineCartAdapter medicineCartAdapter;
    private List<MedicineCartModel> medicineCartModels;
    private Context context;

    private Button orderButton;
    private TextView totalStaticTextView;
    private TextView totalTextView;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    int random;

    String patientId;
    String patientName;
    String location;
    String fawry = "Fawry";
    String cash = "Cash";

    public PatientCartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patient_cart, container, false);
        context = getContext();
        recyclerView = view.findViewById(R.id.recyclerView);

        orderButton = view.findViewById(R.id.order_now_button);
        totalTextView = view.findViewById(R.id.total_number_textView);
        totalStaticTextView = view.findViewById(R.id.total_textView);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.keepSynced(true);

        random = new Random().nextInt(999999) + 111111;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        medicineCartModels = CartMedicineManager.getInstance().getMedicineCartModels();

//        if (medicineCartModels == null || medicineCartModels.isEmpty())
//            Toast.makeText(context, "No medicines at cart.", Toast.LENGTH_SHORT).show();

        medicineCartAdapter = new MedicineCartAdapter(medicineCartModels, context);
        recyclerView.setAdapter(medicineCartAdapter);

        setTotalPrice(medicineCartModels);

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (medicineCartModels != null && !medicineCartModels.isEmpty()) {
                    order(medicineCartModels);
                }
            }
        });

        getUID();

        return view;
    }

    private void setTotalPrice(List<MedicineCartModel> medicineCartModels) {
        double price = 0;
        if (medicineCartModels == null || medicineCartModels.isEmpty()) {
            totalStaticTextView.setVisibility(View.GONE);
            orderButton.setVisibility(View.GONE);
        } else {
            for (MedicineCartModel medicineCartModel : medicineCartModels) {
                price += Double.valueOf(medicineCartModel.getOrder_price());
            }
            totalTextView.setText(String.valueOf(price));
        }
    }

    private void order(final List<MedicineCartModel> medicineCartModels) {
        CheckOutDialog.showDialog(getActivity(), new CheckOutDialog.DialogListener() {
            @Override
            public void onOkCheckOut() {
                for (MedicineCartModel medicineCartModel : medicineCartModels) {
                    medicineCartModel.setPayment(cash);
                    AddToDb(medicineCartModel.getCompany_uid(), medicineCartModel.getOrder_image(),
                            medicineCartModel.getOrder_name(),
                            medicineCartModel.getOrder_price(),
                            medicineCartModel.getOrder_quantity(),
                            medicineCartModel.getPayment());
                }
            }

            @Override
            public void onFawry() {
                FawryDialog.showDialog(getActivity(), random,new FawryDialog.DialogListener() {
                    @Override
                    public void onFawry() {
                        for (MedicineCartModel medicineCartModel : medicineCartModels) {
                            medicineCartModel.setPayment(fawry);
                            AddToDb(medicineCartModel.getCompany_uid(), medicineCartModel.getOrder_image(),
                                    medicineCartModel.getOrder_name(),
                                    medicineCartModel.getOrder_price(),
                                    medicineCartModel.getOrder_quantity(),
                                    medicineCartModel.getPayment());

                        }
                    }
                });
            }
        });
    }

    public void AddToDb(String company_uid, String image, String name, String price, String q, String pay) {
        PatientOrderModel patientOrderModel = new PatientOrderModel(image, name, patientName, price, location, q, pay);

        String key = databaseReference.child("requestsFromPatient").push().getKey();

        databaseReference.child("requestsFromPatient").child(company_uid).child(patientId).child(key).setValue(patientOrderModel);
        Toast.makeText(getActivity(), "Ordered Successfully.", Toast.LENGTH_SHORT).show();

        // Delete Items From Cart.
        CartMedicineManager.getInstance().delete();

        Intent intent = new Intent(getActivity(), PatientMainActivity.class);
        startActivity(intent);
    }

    public void getUID() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        patientId = user.getUid();
        returnData(patientId);
    }

    public void returnData(String key) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);

        mDatabase.child("AllUsers").child("Patients").child(key).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        PatientModel patientModel = dataSnapshot.getValue(PatientModel.class);
                        patientName = patientModel.getFullname();
                        location = patientModel.getGov() + ", " + patientModel.getDis() + ", "
                                + patientModel.getSt() + ", " + patientModel.getBu();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getContext(), "can\'t fetch data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
