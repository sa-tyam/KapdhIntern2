package com.nith.kapdhintern2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WorkerProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    View v;
    TextView name,email,dob,aadhar,mobile,pincode,state,district,city,nation;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference ref;
    ProgressDialog pd;
    Button edit;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_worker_profile, container, false);

        name = (TextView) v.findViewById(R.id.wrkrname);
        email = (TextView) v.findViewById(R.id.wrkrkemail);
        mobile = (TextView) v.findViewById(R.id.wrkrmobile);
        aadhar = (TextView) v.findViewById(R.id.wrkraadhar);
        dob = (TextView) v.findViewById(R.id.wrkrdob);
        pincode = (TextView) v.findViewById(R.id.wrkrpincode);
        state= (TextView) v.findViewById(R.id.wrkrstate);
        nation = (TextView) v.findViewById(R.id.wrkrkNationality);
        district = (TextView) v.findViewById(R.id.wrkrdistrict);
        city = (TextView) v.findViewById(R.id.wrkrkcity);
        edit = (Button) v.findViewById(R.id.editprofile);

        pd = new ProgressDialog(getActivity());
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();

        pd.show();
        pd.setMessage("Wait");
        String uid = auth.getUid();
        ref = db.getReference(uid).child("Service Provider").child("Profile");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.setText(snapshot.child("Name").getValue().toString());
                email.setText(snapshot.child("Email").getValue().toString());
                mobile.setText(snapshot.child("Phone").getValue().toString());
                aadhar.setText(snapshot.child("Aadhar").getValue().toString());
                pincode.setText(snapshot.child("PinCode").getValue().toString());
                dob.setText(snapshot.child("DOB").getValue().toString());
                state.setText(snapshot.child("State").getValue().toString());
                district.setText(snapshot.child("District").getValue().toString());
                city.setText(snapshot.child("City").getValue().toString());
                nation.setText(snapshot.child("Nationality").getValue().toString());
                pd.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),EditProfile.class));
            }
        });

        return v;
    }
}