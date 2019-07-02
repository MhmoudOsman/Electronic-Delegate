package muhammed.awad.electronicdelegate.PharmacyApp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import muhammed.awad.electronicdelegate.Models.PatientOrderModel;
import muhammed.awad.electronicdelegate.Notifications.MySingleton;
import muhammed.awad.electronicdelegate.R;

public class PharmacyRequestsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<PatientOrderModel, OrderViewHolder> firebaseRecyclerAdapter;

    RotateLoading rotateLoading;

    String patientId;

    private final static String FCM_API = "https://fcm.googleapis.com/fcm/send";
    private final static String serverKey = "key=" + "AAAAj5yZH5U:APA91bE7L5wKxIsMbW7d3gJ-DAifkVQa3-oyfjW83e_dgI3ky57Zk9PpQLhgCiYTCxPAyIbY1eKy2lUsRvpn-OTn_f36-PG9KuFzgkKeQDJPB3DP5Xvl5dyH2ZGnailtXBnA-XE5UfWh";
    private final static String contentType = "application/json";

    public static final String ID = "patientId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_requests);

        recyclerView = findViewById(R.id.doctors_recyclerview);
        rotateLoading = findViewById(R.id.rotateloading);

        patientId = getIntent().getStringExtra(ID);
        if (patientId == null) finish();

        rotateLoading.start();

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        DisplayAllMedicines();
    }

    private void DisplayAllMedicines() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("requestsFromPatient")
                .child(getUID())
                .child(patientId)
                .limitToLast(50);

        FirebaseRecyclerOptions<PatientOrderModel> options =
                new FirebaseRecyclerOptions.Builder<PatientOrderModel>()
                        .setQuery(query, PatientOrderModel.class)
                        .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PatientOrderModel, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull final PatientOrderModel model) {
                rotateLoading.stop();

                String key = getRef(position).getKey();
                model.setId(key);

                holder.accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Toast.makeText(CompanyRequestsActivity.this, "Accept Clicked", Toast.LENGTH_SHORT).show();
                        AddToDb(model, "Deliverd");
                        sendNotification();
                    }
                });


                holder.BindPlaces(model);
            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(PharmacyRequestsActivity.this).inflate(R.layout.order_item, parent, false);
                return new OrderViewHolder(view);
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
        rotateLoading.stop();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        CircleImageView order_image;
        TextView order_name, pharmacy_name, order_location, order_price, order_q, pay;
        Button accept;
        LinearLayout c;

        OrderViewHolder(View itemView) {
            super(itemView);

            order_image = itemView.findViewById(R.id.order_image);
            order_name = itemView.findViewById(R.id.order_name);
            pharmacy_name = itemView.findViewById(R.id.order_pharmacy);
            order_location = itemView.findViewById(R.id.order_location);
            order_price = itemView.findViewById(R.id.order_price);
            order_q = itemView.findViewById(R.id.order_q);
            accept = itemView.findViewById(R.id.accept_btn);
            pay = itemView.findViewById(R.id.Paymn);
            c = itemView.findViewById(R.id.order_c);
            c.setBackgroundColor(Color.parseColor("#33BC99"));
        }

        void BindPlaces(final PatientOrderModel patientOrderModel) {
            pharmacy_name.setText("From: " + patientOrderModel.getPatient_name());
            order_name.setText(patientOrderModel.getOrder_name());
            order_location.setText(patientOrderModel.getOrder_location());
            order_price.setText("Total: " + patientOrderModel.getOrder_price() + " L.E");
            order_q.setText(patientOrderModel.getOrder_quantity() + " Items");
            pay.setText("Pay By : " + patientOrderModel.getPayment());


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

    public void AddToDb(PatientOrderModel patientOrderModel, String state) {
        patientOrderModel.setState(state);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        String key = databaseReference.child("PatientOrders").child(patientId).push().getKey();

        databaseReference.child("PatientOrders").child(patientId).child(key).setValue(patientOrderModel);

        // Delete
        databaseReference = firebaseDatabase.getReference().child("requestsFromPatient")
                .child(getUID())
                .child(patientId)
                .child(patientOrderModel.getId());

        databaseReference.removeValue();

        Toast.makeText(this, "Order " + state + ".", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(this, PharmacyRequestsActivity.class);
        i.putExtra(PharmacyRequestsActivity.ID, patientId);
        startActivity(i);
        finish();
    }

    private void sendNotification() {
        String TOPIC = "/topics/App";
        String NOTIFICATION_TITLE = "Order Status";
        String NOTIFICATION_MESSAGE = "New order status has been updated, check it now!";
        JSONObject notification = new JSONObject();
        JSONObject notificationBody = new JSONObject();
        try {
            notificationBody.put("title", NOTIFICATION_TITLE);
            notificationBody.put("message", NOTIFICATION_MESSAGE);

            notification.put("to", TOPIC);
            notification.put("data", notificationBody);
        } catch (JSONException e) {
            Log.e("SEND_NOTIFICATION", "onCreate: " + e.getMessage());
        }
        sendNotification(notification);
    }

    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("SEND_NOTIFICATION", "onResponse: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PharmacyRequestsActivity.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i("SEND_NOTIFICATION", "onErrorResponse: Didn't work");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
}
