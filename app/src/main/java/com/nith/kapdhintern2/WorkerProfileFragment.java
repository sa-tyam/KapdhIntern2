package com.nith.kapdhintern2;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View v;
    TextView name,email,dob,aadhar,mobile,pincode,state,district,city;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference ref;
    ProgressDialog pd;

    public WorkerProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorkerNotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WorkerProfileFragment newInstance(String param1, String param2) {
        WorkerProfileFragment fragment = new WorkerProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

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
        district = (TextView) v.findViewById(R.id.wrkrdistrict);
        city = (TextView) v.findViewById(R.id.wrkrkcity);

        pd = new ProgressDialog(getActivity());
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        ref = db.getReference(auth.getCurrentUser().getUid()).child("Service Provider").child("Profile");
        pd.show();
        pd.setMessage("Wait");
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
                pd.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return v;
    }
}