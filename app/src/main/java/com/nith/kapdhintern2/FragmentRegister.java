package com.nith.kapdhintern2;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;


public class FragmentRegister extends Fragment {

    Button go;
    EditText name,email,pass,dob,state,nation,mobile,aadhar,pin,city,distrct,repass;
    FirebaseAuth auth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

         View v = inflater.inflate(R.layout.fragment_register, container, false);
         go = (Button) v.findViewById(R.id.go_reg);
         name = (EditText) v.findViewById(R.id.reg_name);
         email = (EditText) v.findViewById(R.id.reg_email);
         pass = (EditText) v.findViewById(R.id.reg_pass);
         repass = (EditText) v.findViewById(R.id.reg_retypepass);
         dob = (EditText) v.findViewById(R.id.reg_dob);
         state = (EditText) v.findViewById(R.id.reg_state);
         mobile = (EditText) v.findViewById(R.id.reg_mobile);
         aadhar = (EditText) v.findViewById(R.id.reg_aadhar);
         nation = (EditText) v.findViewById(R.id.reg_indian);
         pin = (EditText) v.findViewById(R.id.reg_pincode);
         city = (EditText) v.findViewById(R.id.reg_city);
         distrct = (EditText) v.findViewById(R.id.reg_district);

         auth = FirebaseAuth.getInstance();

         go.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 String memail = email.getText().toString().trim();
                 String mpass = pass.getText().toString().trim();
                 String mphone = "+91"+mobile.getText().toString().trim();
                 String maadhar = aadhar.getText().toString().trim();
                 String mrepass = repass.getText().toString().trim();
                 String mname = name.getText().toString().trim();
                 String mdob = dob.getText().toString().trim();
                 String mstate = state.getText().toString().trim();
                 String mpin = pin.getText().toString().trim();
                 String mcity = city.getText().toString().trim();
                 String mdistrict = distrct.getText().toString().trim();

                 if(TextUtils.isEmpty(mname))
                 {
                     name.setError("Required Field");
                     return;
                 }
                 if(TextUtils.isEmpty(memail))
                 {
                     email.setError("Required Field");
                     return;
                 }

                 if(TextUtils.isEmpty(mphone))
                 {
                     mobile.setError("Required Field");
                     return;
                 }
                 if(TextUtils.isEmpty(maadhar))
                 {
                     aadhar.setError("Required Field");
                     return;
                 }
                 if(TextUtils.isEmpty(mdob))
                 {
                     dob.setError("Required Field");
                     return;
                 }
                 if(TextUtils.isEmpty(mpin))
                 {
                     pin.setError("Required Field");
                     return;
                 }
                 if(TextUtils.isEmpty(mstate))
                 {
                     state.setError("Required Field");
                     return;
                 }
                 if(TextUtils.isEmpty(mdistrict))
                 {
                     distrct.setError("Required Field");
                     return;
                 }
                 if(TextUtils.isEmpty(mcity))
                 {
                     city.setError("Required Field");
                     return;
                 }
                 if(TextUtils.isEmpty(mpass))
                 {
                     pass.setError("Required Field");
                     return;
                 }
                 if(TextUtils.isEmpty(mrepass))
                 {
                     repass.setError("Required Field");
                     return;
                 }
                 if(mpass.length()<6)
                 {
                     pass.setError("Password length must be greater than equals to 6");
                     return;
                 }
                 if(!(mpass.equals(mrepass)))
                 {
                     repass.setError("Password does not match");
                     return;
                 }

                 auth.createUserWithEmailAndPassword(memail,mpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         if(task.isSuccessful())
                         {
                             Intent intent = new Intent(getActivity(),RegisterAs.class);
                             intent.putExtra("email",memail);
                             intent.putExtra("phone",mphone);
                             intent.putExtra("aadhar",maadhar);
                             intent.putExtra("name",mname);
                             intent.putExtra("dob",mdob);
                             intent.putExtra("pincode",mpin);
                             intent.putExtra("state",mstate);
                             intent.putExtra("district",mdistrict);
                             intent.putExtra("city",mcity);
                             startActivity(intent);
                         }
                         else
                             {
                                 try
                                 {
                                     throw task.getException();
                                 }
                                 catch (FirebaseAuthUserCollisionException existEmail)
                                 {
                                     FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                     Toast.makeText(getActivity(),existEmail.getMessage()+user.getUid(),Toast.LENGTH_SHORT).show();
                                     AuthCredential credential = EmailAuthProvider.getCredential(memail,mpass);
                                     user.reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
                                         @Override
                                         public void onSuccess(Void aVoid) {
                                             user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                 @Override
                                                 public void onSuccess(Void aVoid) {
                                                     Toast.makeText(getActivity(),"Done",Toast.LENGTH_SHORT).show();
                                                 }
                                             }).addOnFailureListener(new OnFailureListener() {
                                                 @Override
                                                 public void onFailure(@NonNull Exception e) {
                                                     Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                                 }
                                             });
                                         }
                                     }).addOnFailureListener(new OnFailureListener() {
                                         @Override
                                         public void onFailure(@NonNull Exception e) {
                                             Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                         }
                                     });
                                 }
                                 catch (Exception e) {
                                    Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                 }
                             }
                     }
                 });
             }
         });

         return v;
    }
}