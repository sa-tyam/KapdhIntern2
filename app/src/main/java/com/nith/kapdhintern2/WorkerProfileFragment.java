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
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
    ImageButton workerProfileHeaderToggleImageButton;
    LinearLayout workerProfileDetails;

    boolean profileDetailFlag = false;


    public void workerProfileDetailToggle () {
        if (profileDetailFlag == false) {
            profileDetailFlag = true;
            workerProfileHeaderToggleImageButton.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
            workerProfileDetails.setVisibility(View.VISIBLE);

            pd = new ProgressDialog(getActivity());
            pd.show();
            pd.setMessage("Wait");

            auth = FirebaseAuth.getInstance();
            db = FirebaseDatabase.getInstance();



            String uid = auth.getUid();
            ref = db.getReference(uid).child("Service Provider").child("Profile");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    pd.cancel();
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
        } else {
            profileDetailFlag = false;
            workerProfileHeaderToggleImageButton.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
            workerProfileDetails.setVisibility(View.GONE);
        }

    }

    private void initViews (View v) {
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
        workerProfileHeaderToggleImageButton = v.findViewById(R.id.workerProfileHeaderToggleImageButton);
        workerProfileDetails = v.findViewById(R.id.workerProfileDetails);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_worker_profile, container, false);
        initViews(v);



        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),EditProfile.class));
            }
        });
        workerProfileHeaderToggleImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                workerProfileDetailToggle();
            }
        });

        return v;
    }
}