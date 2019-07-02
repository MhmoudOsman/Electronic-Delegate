package muhammed.awad.electronicdelegate;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import muhammed.awad.electronicdelegate.Models.CompanyModel;
import muhammed.awad.electronicdelegate.PharmacyApp.PharmacyMainActivity;

public class Register2Activity extends AppCompatActivity {

    Button get_started, sign_in, patient_signin;

    PinView pinView;
    EditText mobile_field;
    String mobile, codeSent;
    Button cancel, signin, verify_btn;

    ProgressDialog progressDialog;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        auth = FirebaseAuth.getInstance();

        get_started = findViewById(R.id.getstarted_btn);
        sign_in = findViewById(R.id.signin_btn);
        patient_signin = findViewById(R.id.patient_signin_btn);

        get_started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnVerificationStateChanged();

                showMobileDialog();
            }
        });

        patient_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PatientSignInActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showMobileDialog() {
        final Dialog dialog = new Dialog(Register2Activity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.sign_in_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes();
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        mobile_field = dialog.findViewById(R.id.mobile_field);

        signin = dialog.findViewById(R.id.sign_in_btn);
        cancel = dialog.findViewById(R.id.cancel_btn);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile = mobile_field.getText().toString();

                if (TextUtils.isEmpty(mobile)) {
                    Toast.makeText(getApplicationContext(), "please enter your mobile number", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog = new ProgressDialog(Register2Activity.this);
                progressDialog.setTitle("Verification Code");
                progressDialog.setMessage("Please Wait Until Sending Code ...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                progressDialog.setCancelable(false);

                startPhoneNumberVerification(mobile);

                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void showVerificationDialog() {
        final Dialog dialog = new Dialog(Register2Activity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.mobile_verification_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes();
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        pinView = dialog.findViewById(R.id.code_field);

        verify_btn = dialog.findViewById(R.id.verify_btn);
        cancel = dialog.findViewById(R.id.cancel_btn);

        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = pinView.getText().toString();

                if (TextUtils.isEmpty(code) || code.equals(codeSent)) {
                    Toast.makeText(getApplicationContext(), "please enter a valid verification code", Toast.LENGTH_SHORT).show();
                    pinView.requestFocus();
                    return;
                }

                progressDialog = new ProgressDialog(Register2Activity.this);
                progressDialog.setTitle("Verification Code");
                progressDialog.setMessage("Please Wait Until Verify Your Number ...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                progressDialog.setCancelable(false);

                signInWithPhoneAuthCredential(SignIn(code));
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void OnVerificationStateChanged() {
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onVerificationFailed(FirebaseException e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "code sent to : " + mobile, Toast.LENGTH_SHORT).show();
                codeSent = s;
                showVerificationDialog();
            }
        };
    }

    public PhoneAuthCredential SignIn(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);

        return credential;
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            category();
                        } else {
                            progressDialog.dismiss();
                            String error_message = task.getException().getMessage();
                            Toast.makeText(getApplicationContext(), error_message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+2" + phoneNumber,                // Phone number to verify
                60,                              // Timeout duration
                TimeUnit.SECONDS,        // Unit of timeout
                this,                // Activity (for callback binding)
                callbacks);                 // OnVerificationStateChangedCallbacks
    }

    public void category() {
        final String id = getUID();
        FirebaseDatabase firebaseDatabase;
        final DatabaseReference databaseReference;

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        databaseReference.child("AllUsers").child("Companies").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(id)) {
                            //Toast.makeText(getContext(), "Company : " + id, Toast.LENGTH_SHORT).show();
                            updateCompanyUI();
                        } else {
                            databaseReference.child("AllUsers").child("Pharmacies").addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.hasChild(id)) {
                                                //Toast.makeText(getContext(), "patient : " + id, Toast.LENGTH_SHORT).show();
                                                updatePharmacyUI();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                            );
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void updateCompanyUI() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

    public void updatePharmacyUI() {
        Intent i = new Intent(getApplicationContext(), PharmacyMainActivity.class);
        startActivity(i);
    }

    private String getUID() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String UserID = user.getUid();

        return UserID;
    }
}
