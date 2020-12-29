package com.nith.kapdhintern2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class CustomerRegVerification extends AppCompatActivity {

    String email,phone,aadhar,name,dob,pin,state,district,city,nation;
    Button done,cancel;
    TextView t1;
    ImageView iv1,iv2;
    EditText phoneOtp;
    TextView resendOtp;
    Boolean otpvalid = true;
    Boolean isPhoneVerifed = false;
    Boolean emailflag = false;
    String verificationid;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase db;
    DatabaseReference ref;
    PhoneAuthProvider.ForceResendingToken token;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_reg_verification);
        t1 = (TextView) findViewById(R.id.emailstatus);
        iv1 = (ImageView) findViewById(R.id.tickcross);
        iv2 = (ImageView) findViewById(R.id.tickcross2);
        phoneOtp = (EditText) findViewById(R.id.phoneOtp);
        done = (Button) findViewById(R.id.done);
        cancel = (Button) findViewById(R.id.customerVerCancel);
        resendOtp = (TextView) findViewById(R.id.resendOtp);

        email = getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("phone");
        aadhar = getIntent().getStringExtra("aadhar");
        name = getIntent().getStringExtra("name");
        dob = getIntent().getStringExtra("dob");
        pin = getIntent().getStringExtra("pincode");
        state = getIntent().getStringExtra("state");
        district = getIntent().getStringExtra("district");
        city = getIntent().getStringExtra("city");
        nation = getIntent().getStringExtra("nationality");

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        user = auth.getCurrentUser();

        mcallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationid = s;
                token = forceResendingToken;
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                verifyAuthCredential(phoneAuthCredential);
                Toast.makeText(getApplicationContext(),"called",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        };

        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Sent", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        sendOtp(phone);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.reload();
                user = auth.getCurrentUser();
                emailflag = user.isEmailVerified();
                isAllOk(emailflag,isPhoneVerifed);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerRegVerification.this,MainActivity.class));
            }
        });
    }

    private void isAllOk(Boolean emailflag, Boolean isPhoneVerifed)
    {
        if(emailflag && isPhoneVerifed)
        {
            t1.setVisibility(View.VISIBLE);
            iv1.setVisibility(View.VISIBLE);
            t1.setText("Verified");
            iv1.setImageResource(R.drawable.tick);
            Toast.makeText(getApplicationContext(),"Email Verified",Toast.LENGTH_SHORT).show();
            ref = db.getReference(user.getUid()).child("Customer").child("Profile");
            ref.child("Email").setValue(email);
            ref.child("Phone").setValue(phone);
            ref.child("Aadhar").setValue(aadhar);
            ref.child("Name").setValue(name);
            ref.child("PinCode").setValue(pin);
            ref.child("DOB").setValue(dob);
            ref.child("State").setValue(state);
            ref.child("District").setValue(district);
            ref.child("City").setValue(city);
            ref.child("Nationality").setValue(nation);
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
        else if(emailflag && !(isPhoneVerifed))
        {
            t1.setVisibility(View.VISIBLE);
            iv1.setVisibility(View.VISIBLE);
            t1.setText("Verified");
            iv1.setImageResource(R.drawable.tick);
            Toast.makeText(getApplicationContext(),"Email Verified",Toast.LENGTH_SHORT).show();

            validateotp(phoneOtp.getText().toString());

            if(otpvalid)
            {
                String otp = phoneOtp.getText().toString().trim();

                PhoneAuthCredential credential =  PhoneAuthProvider.getCredential(verificationid,otp);
                verifyAuthCredential(credential);
            }
        }
        else
        {
            t1.setVisibility(View.VISIBLE);
            iv1.setVisibility(View.VISIBLE);
            t1.setText("Not Verified");
            iv1.setImageResource(R.drawable.cross);
            Toast.makeText(getApplicationContext(),"Email Not Verified",Toast.LENGTH_SHORT).show();
        }

    }


    private void sendOtp(String phonenumber) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth).setPhoneNumber(phonenumber).setTimeout(60L, TimeUnit.SECONDS).setActivity(this).setCallbacks(mcallbacks).build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private void resendOtp(String phonenumber) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth).setPhoneNumber(phonenumber).setTimeout(60L, TimeUnit.SECONDS).setActivity(this).setCallbacks(mcallbacks).setForceResendingToken(token).build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void validateotp(String string)
    {
        if(string.isEmpty())
        {
            otpvalid = false;
            phoneOtp.setError("Require field");
        }
        else
            {
                otpvalid = true;
            }

    }
    private void verifyAuthCredential(PhoneAuthCredential credential)
    {
        auth.getCurrentUser().linkWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(getApplicationContext(),"Verified and Linked",Toast.LENGTH_SHORT).show();
                iv2.setVisibility(View.VISIBLE);
                iv2.setImageResource(R.drawable.tick);
                isPhoneVerifed = true;
                user.reload();
                user = auth.getCurrentUser();
                emailflag = user.isEmailVerified();
                isAllOk(emailflag,isPhoneVerifed);
//                if(user.isEmailVerified())
//                {
//                    ref = db.getReference(user.getUid()).child("Customer").child("Info");
//                    ref.child("Email").setValue(email);
//                    ref.child("Phone").setValue(phone);
//                    ref.child("Aadhar").setValue(aadhar);
//                    ref.child("Name").setValue(name);
//                    ref.child("PinCode").setValue(pin);
//                    ref.child("DOB").setValue(dob);
//                    ref.child("State").setValue(state);
//                    ref.child("District").setValue(district);
//                    ref.child("City").setValue(city);
//                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
//                }
//                else
//                    {
//                        t1.setVisibility(View.VISIBLE);
//                        iv1.setVisibility(View.VISIBLE);
//                        t1.setText("Not Verified");
//                        iv1.setImageResource(R.drawable.cross);
//                        Toast.makeText(getApplicationContext(),"Email Not Verified",Toast.LENGTH_SHORT).show();
//                    }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                iv2.setVisibility(View.VISIBLE);
                iv2.setImageResource(R.drawable.cross);
                phoneOtp.setError(e.getMessage());
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CustomerRegVerification.this,MainActivity.class));
    }
}