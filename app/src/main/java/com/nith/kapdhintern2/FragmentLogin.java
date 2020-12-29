package com.nith.kapdhintern2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class FragmentLogin extends Fragment {


    public Context mcontext;
    FirebaseDatabase db;
    FirebaseAuth auth;
    View v;
    EditText email,password;
    Button login;
    ProgressDialog pd;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mcontext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_login, container, false);
        email = (EditText) v.findViewById(R.id.login_email);
        password = (EditText) v.findViewById(R.id.login_password);
        login = (Button) v.findViewById(R.id.login_go);

        pd = new ProgressDialog(getActivity());
        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(email.getText().toString().isEmpty())
               {
                   email.setError("Required field");
               }
               else if(password.getText().toString().isEmpty())
               {
                   password.setError("Required field");
               }
               else
                   {
                       pd.show();
                       pd.setMessage("Wait!");
                       auth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                           @Override
                           public void onComplete(@NonNull Task<AuthResult> task) {
                               if(task.isSuccessful())
                               {
                                   DatabaseReference ref = db.getReference(auth.getCurrentUser().getUid());

                                   ref.addValueEventListener(new ValueEventListener() {
                                       @Override
                                       public void onDataChange(@NonNull DataSnapshot snapshot) {

                                           if(snapshot.exists() & auth.getCurrentUser().isEmailVerified())
                                           {
                                               pd.dismiss();
                                               if(snapshot.hasChild("Customer"))
                                               {
                                                   startActivity(new Intent(mcontext,CustomerPage.class));
                                                   getActivity().finish();
                                               }
                                               if(snapshot.hasChild("Service Provider"))
                                               {
                                                   startActivity(new Intent(mcontext,LoginAs.class));
                                                   getActivity().finish();
                                               }


                                           }
                                           else
                                           {
                                               pd.dismiss();
                                               FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                               user.delete();
                                               Toast.makeText(getActivity(),"Complete your verification first",Toast.LENGTH_SHORT).show();
                                           }
                                       }

                                       @Override
                                       public void onCancelled(@NonNull DatabaseError error) {

                                       }
                                   });
                               }
                               else
                               {
                                   pd.dismiss();
                                   Toast.makeText(getActivity(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                               }
                           }
                       });
                   }

            }
        });

        return v;
    }

}