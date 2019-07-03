package muhammed.awad.electronicdelegate;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;
import muhammed.awad.electronicdelegate.CompanyApp.Fragments.PharmaceuticalFragment;
import muhammed.awad.electronicdelegate.Models.CompanyModel;
import muhammed.awad.electronicdelegate.Models.MedicineModel;

public class PharmaceuticalActivity extends AppCompatActivity {

    String KEY;

    CircleImageView pharmaceutical_image;
    TextView company_name_txt;
    EditText pharmaceutical_name, pharmaceutical_price, pharmaceutical_info, customer_price, category_spinner, type_spinner, quantity;
    Button add_pharmaceutical_btn;
    CardView delete_card;
    int q;

    Uri photoPath;
    String selected_image_url, company_name, exist_image;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmaceutical);

        KEY = getIntent().getStringExtra(PharmaceuticalFragment.EXTRA_EDIT_PHARMA);

        company_name_txt = findViewById(R.id.company_name_txt);
        pharmaceutical_image = findViewById(R.id.pharmaceutical_image);
        pharmaceutical_name = findViewById(R.id.pharmaceutical_name_field);
        pharmaceutical_price = findViewById(R.id.pharmaceutical_price_field);
        customer_price = findViewById(R.id.customer_price_field);
        quantity = findViewById(R.id.quantity_field);
        category_spinner = findViewById(R.id.category_spinner);
        type_spinner = findViewById(R.id.type_spinner);
        pharmaceutical_info = findViewById(R.id.pharmaceutical_info_field);
        add_pharmaceutical_btn = findViewById(R.id.add_pharmaceutical_btn);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.keepSynced(true);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("Images");


        if (TextUtils.isEmpty(KEY)) {
            returndata();

            add_pharmaceutical_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = pharmaceutical_name.getText().toString();
                    String price = pharmaceutical_price.getText().toString();
                    String info = pharmaceutical_info.getText().toString();
                    String cust_price = customer_price.getText().toString();
                    String quntity = quantity.getText().toString();
                    String category = category_spinner.getText().toString();
                    String type = type_spinner.getText().toString();

                    if (TextUtils.isEmpty(name)) {
                        Toast.makeText(getApplicationContext(), "please enter pharmaceutical name", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(price)) {
                        Toast.makeText(getApplicationContext(), "please enter purchasing price", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(cust_price)) {
                        Toast.makeText(getApplicationContext(), "please enter selling price", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(quntity)) {
                        Toast.makeText(getApplicationContext(), "please enter quantity", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(category)) {
                        Toast.makeText(getApplicationContext(), "please select category", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(type)) {
                        Toast.makeText(getApplicationContext(), "please select type", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(info)) {
                        Toast.makeText(getApplicationContext(), "please enter pharmaceutical information", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (photoPath == null) {
                        Toast.makeText(getApplicationContext(), "please select image", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    progressDialog = new ProgressDialog(PharmaceuticalActivity.this);
                    progressDialog.setTitle("Add Pharmaceutical");
                    progressDialog.setMessage("Please Wait Until Adding Pharmaceutical ...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.show();
                    progressDialog.setCancelable(false);

                    uploadImage(info, name, price + " L.E", company_name, getUID(), cust_price + " L.E", Integer.parseInt(quntity),category, type);
                }
            });
        } else {
            add_pharmaceutical_btn.setText("edit");

            returndata(KEY);

            add_pharmaceutical_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = pharmaceutical_name.getText().toString();
                    String price = pharmaceutical_price.getText().toString();
                    String info = pharmaceutical_info.getText().toString();
                    String cust_price = customer_price.getText().toString();
                    String quntity = quantity.getText().toString();
                    String category = category_spinner.getText().toString();
                    String type = type_spinner.getText().toString();

                    if (TextUtils.isEmpty(name)) {
                        Toast.makeText(getApplicationContext(), "please enter pharmaceutical name", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(price)) {
                        Toast.makeText(getApplicationContext(), "please enter purchasing price", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(cust_price)) {
                        Toast.makeText(getApplicationContext(), "please enter selling price", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(quntity)) {
                        Toast.makeText(getApplicationContext(), "please enter quantity", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(category)) {
                        Toast.makeText(getApplicationContext(), "please select category", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(type)) {
                        Toast.makeText(getApplicationContext(), "please select type", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(info)) {
                        Toast.makeText(getApplicationContext(), "please enter pharmaceutical information", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (photoPath == null) {
                        progressDialog = new ProgressDialog(PharmaceuticalActivity.this);
                        progressDialog.setTitle("Edit Pharmaceutical");
                        progressDialog.setMessage("Please Wait Until Editing Pharmaceutical ...");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.show();
                        progressDialog.setCancelable(false);

                        updatePharmaceuticaltoDB(exist_image, info, name, price + " L.E", company_name_txt.getText().toString(), getUID(), cust_price + " L.E", Integer.parseInt(quntity), category, type);
                    } else {
                        progressDialog = new ProgressDialog(PharmaceuticalActivity.this);
                        progressDialog.setTitle("Edit Pharmaceutical");
                        progressDialog.setMessage("Please Wait Until Editing Pharmaceutical ...");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.show();
                        progressDialog.setCancelable(false);

                        updateuploadImage(info, name, price + " L.E", company_name_txt.getText().toString(), getUID(), cust_price + " L.E", Integer.parseInt(quntity), category, type);
                    }
                }
            });
        }

        pharmaceutical_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                        .setAspectRatio(1, 1)
                        .start(PharmaceuticalActivity.this);
            }
        });
    }

    public void returndata() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);

        mDatabase.child("AllUsers").child("Companies").child(getUID()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        CompanyModel companyModel = dataSnapshot.getValue(CompanyModel.class);

                        company_name = companyModel.getTitle();
                        company_name_txt.setText(company_name);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "can\'t fetch data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void returndata(String key) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);

        mDatabase.child("pharmaceutical").child(getUID()).child(key).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        MedicineModel medicineModel = dataSnapshot.getValue(MedicineModel.class);

                        pharmaceutical_name.setText(medicineModel.getName());

                        String string = medicineModel.getPrice();

                        String[] parts = string.split(" ");
                        String pr = parts[0];

                        String string2 = medicineModel.getCustomer_price();

                        String[] parts2 = string2.split(" ");
                        String pr2 = parts2[0];

                        customer_price.setText(pr2);
                        pharmaceutical_price.setText(pr);
                        pharmaceutical_info.setText(medicineModel.getInfo());
                        company_name_txt.setText(medicineModel.getCompany_name());
                        exist_image = medicineModel.getImageurl();
                        q = medicineModel.getQuantity();
                        String qu =String.valueOf(q);
                        quantity.setText(qu);

                        category_spinner.setText(medicineModel.getCategory());
                        type_spinner.setText(medicineModel.getType());

                        Picasso.get()
                                .load(medicineModel.getImageurl())
                                .placeholder(R.drawable.addphoto)
                                .error(R.drawable.addphoto)
                                .into(pharmaceutical_image);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "can\'t fetch data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadImage(final String info, final String name, final String price, final String company, final String uid, final String cust_price,final int quntity ,final String category, final String type) {
        UploadTask uploadTask;

        final StorageReference ref = storageReference.child("images/" + photoPath.getLastPathSegment());

        uploadTask = ref.putFile(photoPath);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Uri downloadUri = task.getResult();

                selected_image_url = downloadUri.toString();

                AddPharmaceuticaltoDB(selected_image_url, info, name, price, company, uid, cust_price, quntity,category, type);
                progressDialog.dismiss();

                Toast.makeText(getApplicationContext(), "Added successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(getApplicationContext(), "Can't Upload Photo", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void AddPharmaceuticaltoDB(String imageurl, String info, String name, String price, String company, String uid, String cust_price, int quntity,String category, String type) {
        MedicineModel medicineModel = new MedicineModel(imageurl, info, name, price, company, uid, cust_price, quntity,category, type);

        String key = databaseReference.child("pharmaceutical").push().getKey();

        databaseReference.child("pharmaceutical").child(getUID()).child(key).setValue(medicineModel);
        databaseReference.child("Allpharmaceutical").child(key).setValue(medicineModel);
    }

    private void updateuploadImage(final String info, final String name, final String price, final String company, final String uid, final String cust_price, final int quntity, final String category, final String type) {
        UploadTask uploadTask;

        final StorageReference ref = storageReference.child("images/" + photoPath.getLastPathSegment());

        uploadTask = ref.putFile(photoPath);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Uri downloadUri = task.getResult();

                selected_image_url = downloadUri.toString();

                updatePharmaceuticaltoDB(selected_image_url, info, name, price, company, uid, cust_price, quntity, category, type);
                progressDialog.dismiss();

                Toast.makeText(getApplicationContext(), "Saved ..", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(getApplicationContext(), "Can't Upload Photo", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void updatePharmaceuticaltoDB(String imageurl, String info, String name, String price, String company, String uid, String cust_price, int quntity, String category, String type) {
        MedicineModel medicineModel = new MedicineModel(imageurl, info, name, price, company, uid, cust_price, quntity,category, type);

        databaseReference.child("pharmaceutical").child(getUID()).child(KEY).setValue(medicineModel);
        databaseReference.child("Allpharmaceutical").child(KEY).setValue(medicineModel);

        progressDialog.dismiss();
        Toast.makeText(getApplicationContext(), "Saved ..", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                if (result != null) {
                    photoPath = result.getUri();

                    Picasso.get()
                            .load(photoPath)
                            .placeholder(R.drawable.addphoto)
                            .error(R.drawable.addphoto)
                            .into(pharmaceutical_image);
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void delete() {
        databaseReference.child("pharmaceutical").child(getUID()).child(KEY).removeValue();
        databaseReference.child("Allpharmaceutical").child(KEY).removeValue();

        Toast.makeText(getApplicationContext(), "Deleted ..", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void showDeleteDialog() {
        final Dialog dialog = new Dialog(PharmaceuticalActivity.this);

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

        yes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
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

    public String getUID() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        return userId;
    }
}
