package com.nith.kapdhintern2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.concurrent.TimeUnit;

public class ServiceprvdrRegVerification extends AppCompatActivity {

    private static final int PICK_DOC1 = 1 ;
    private static final int PICK_DOC2 = 2;
    private static final int PICK_DOC3 = 3;
    String email,phone,aadhar,name,dob,pin,state,district,city,nation;
    Button done,cancel;
    TextView t1,doc1,doc2,doc3;
    ImageView iv1,iv2,add1,add2,add3;
    EditText phoneOtp;
    TextView resendOtp;
    Boolean otpvalid = true;
    Boolean isPhoneVerified = false;
    String verificationid;
    Uri uri1,uri2,uri3;
    String fileurl;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase db;
    StorageReference storageReference;
    DatabaseReference ref;
    PhoneAuthProvider.ForceResendingToken token;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serviceprvdr_reg_verification);
        t1 = (TextView) findViewById(R.id.spemailstatus);
        doc1 = (TextView) findViewById(R.id.doc1);
        doc2 = (TextView) findViewById(R.id.doc2);
        doc3 = (TextView) findViewById(R.id.doc3);

        iv1 = (ImageView) findViewById(R.id.sptickcross);
        iv2 = (ImageView) findViewById(R.id.sptickcross2);
        add1 = (ImageView) findViewById(R.id.add1);
        add2 = (ImageView) findViewById(R.id.add2);
        add3 = (ImageView) findViewById(R.id.add3);
        phoneOtp = (EditText) findViewById(R.id.spOtp);
        done = (Button) findViewById(R.id.spregdone);
        cancel = (Button) findViewById(R.id.spRegVercancel);
        resendOtp = (TextView) findViewById(R.id.spresendOtp);

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
        storageReference = FirebaseStorage.getInstance().getReference();
        user = auth.getCurrentUser();

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

        mcallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationid = s;
                token = forceResendingToken;

            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                verifyAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();

            }
        };
        sendOtp(phone);

        add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select doc"),PICK_DOC1);
            }
        });
        add2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select doc"),PICK_DOC2);
            }
        });
        add3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select doc"),PICK_DOC3);
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.reload();
                user = auth.getCurrentUser();
                Boolean emailflag = user.isEmailVerified();
                isAllOk(emailflag,isPhoneVerified);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ServiceprvdrRegVerification.this,MainActivity.class));
            }
        });
    }

    private void isAllOk(Boolean emailflag, Boolean isPhoneVerified) {

        if(emailflag && isPhoneVerified)
        {
            if(doc1.getText().toString().isEmpty())
            {
                Toast.makeText(getApplicationContext(),"Doc required",Toast.LENGTH_SHORT).show();
            }
            else if(doc2.getText().toString().isEmpty())
            {
                Toast.makeText(getApplicationContext(),"Doc required",Toast.LENGTH_SHORT).show();
            }
            else if(doc3.getText().toString().isEmpty())
            {
                Toast.makeText(getApplicationContext(),"Doc required",Toast.LENGTH_SHORT).show();
            }
            else
                {
                    ref = db.getReference(user.getUid()).child("Service Provider").child("Profile");
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
                    Uploadfile(uri1);
                    Uploadfile(uri2);
                    Uploadfile(uri3);
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }

        }
        else if(emailflag && !(isPhoneVerified))
        {
            t1.setVisibility(View.VISIBLE);
            iv1.setVisibility(View.VISIBLE);
            t1.setText("Verified");
            iv1.setImageResource(R.drawable.tick);
            Toast.makeText(getApplicationContext(),"Email Verified",Toast.LENGTH_SHORT).show();
            validateotp(phoneOtp.getText().toString());

            if(doc1.getText().toString().isEmpty())
            {
                Toast.makeText(getApplicationContext(),"Doc required",Toast.LENGTH_SHORT).show();
            }
            else if(doc2.getText().toString().isEmpty())
            {
                Toast.makeText(getApplicationContext(),"Doc required",Toast.LENGTH_SHORT).show();
            }
            else if(doc3.getText().toString().isEmpty())
            {
                Toast.makeText(getApplicationContext(),"Doc required",Toast.LENGTH_SHORT).show();
            }
            else if(otpvalid)
            {
                String otp = phoneOtp.getText().toString().trim();

                PhoneAuthCredential credential =  PhoneAuthProvider.getCredential(verificationid,otp);
                Toast.makeText(getApplicationContext(),verificationid+"and"+otp,Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_DOC1 && resultCode == RESULT_OK && data != null && data.getData()!=null)
        {
            uri1 = data.getData();
            doc1.setText("Doc1.pdf");
        }
        if(requestCode == PICK_DOC2 && resultCode == RESULT_OK && data != null && data.getData()!=null)
        {
            uri2 = data.getData();
            doc2.setText("Doc2.pdf");
        }
        if(requestCode == PICK_DOC3 && resultCode == RESULT_OK && data != null && data.getData()!=null)
        {
            uri3 = data.getData();
            doc3.setText("Doc3.pdf");
        }
    }

    private void Uploadfile(Uri data)
    {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading...");
        pd.show();
        StorageReference sref1 = storageReference.child(user.getUid()).child("Files").child("doc1.pdf");
        StorageReference sref2 = storageReference.child(user.getUid()).child("Files").child("doc2.pdf");
        StorageReference sref3 = storageReference.child(user.getUid()).child("Files").child("doc3.pdf");
        if(data==uri1)
        {
            sref1.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                    while(!uri.isComplete());
                    Uri url = uri.getResult();
                    DatabaseReference reference = db.getReference(user.getUid()).child("Service Provider").child("Profile");
                    reference.child("Doc1").setValue(url.toString());
                    pd.dismiss();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                    pd.setMessage("Uploaded "+(int)progress+"%");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
        if(data==uri2)
        {
            sref2.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                    while(!uri.isComplete());
                    Uri url = uri.getResult();
                    DatabaseReference reference = db.getReference(user.getUid()).child("Service Provider").child("Profile");
                    reference.child("Doc2").setValue(url.toString());
                    pd.dismiss();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                    pd.setMessage("Uploaded "+(int)progress+"%");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
        if(data==uri3)
        {
            sref3.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                    while(!uri.isComplete());
                    Uri url = uri.getResult();
                    DatabaseReference reference = db.getReference(user.getUid()).child("Service Provider").child("Profile");
                    reference.child("Doc3").setValue(url.toString());
                    pd.dismiss();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                    pd.setMessage("Uploaded "+(int)progress+"%");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
//        if(data == null)
//        {
//
//        }

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
                Toast.makeText(getApplicationContext(),"Linked",Toast.LENGTH_SHORT).show();
                iv2.setVisibility(View.VISIBLE);
                iv2.setImageResource(R.drawable.tick);
                isPhoneVerified = true;
                user.reload();
                user = auth.getCurrentUser();
                Boolean emailflag = user.isEmailVerified();
                isAllOk(emailflag,isPhoneVerified);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                iv2.setVisibility(View.VISIBLE);
                iv2.setImageResource(R.drawable.cross);
            }
        });
    }

}