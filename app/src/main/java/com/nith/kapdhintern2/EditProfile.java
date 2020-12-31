package com.nith.kapdhintern2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditProfile extends AppCompatActivity {

    EditText name,email,dob,state,nation,mobile,aadhar,pin,city,distrct;
    ImageView iv;
    FirebaseAuth auth;
    FirebaseDatabase db;
    Button go;
    final Calendar myCalendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        name = (EditText) findViewById(R.id.edit_name);
        email = (EditText) findViewById(R.id.edit_email);
        dob = (EditText) findViewById(R.id.edit_dob);
        state = (EditText) findViewById(R.id.edit_state);
        mobile = (EditText) findViewById(R.id.edit_mobile);
        aadhar = (EditText) findViewById(R.id.edit_aadhar);
        nation = (EditText) findViewById(R.id.edit_nationality);
        pin = (EditText) findViewById(R.id.edit_pincode);
        city = (EditText) findViewById(R.id.edit_city);
        distrct = (EditText) findViewById(R.id.edit_district);
        go = (Button) findViewById(R.id.go_edit);
        iv = (ImageView) findViewById(R.id.calendr2);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();

        String uid = auth.getUid();
        DatabaseReference ref = db.getReference(uid).child("Service Provider").child("Profile");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.setText(snapshot.child("Name").getValue().toString());
                email.setText(snapshot.child("Email").getValue().toString());
                email.setEnabled(false);
                mobile.setText(snapshot.child("Phone").getValue().toString());
                mobile.setEnabled(false);
                aadhar.setText(snapshot.child("Aadhar").getValue().toString());
                pin.setText(snapshot.child("PinCode").getValue().toString());
                dob.setText(snapshot.child("DOB").getValue().toString());
                state.setText(snapshot.child("State").getValue().toString());
                distrct.setText(snapshot.child("District").getValue().toString());
                city.setText(snapshot.child("City").getValue().toString());
                nation.setText(snapshot.child("Nationality").getValue().toString());
                nation.setEnabled(false);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR,year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                UpdateLabel();
            }
        };
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mname = name.getText().toString();
//              String memail = email.getText().toString();
//              String mMobile = mobile.getText().toString();
                String maadhar = aadhar.getText().toString();
                String mdob = dob.getText().toString();
                String mpin = pin.getText().toString();
                String mstate = state.getText().toString();
                String mdistrict = distrct.getText().toString();
                String mcity = city.getText().toString();

                ref.child("Name").setValue(mname);
//              ref.child("Email").setValue(memail);
//              ref.child("Phone").setValue(mMobile);
                ref.child("Aadhar").setValue(maadhar);
                ref.child("DOB").setValue(mdob);
                ref.child("District").setValue(mdistrict);
                ref.child("PinCode").setValue(mpin);
                ref.child("City").setValue(mcity);
                ref.child("State").setValue(mstate);
                startActivity(new Intent(EditProfile.this,WorkerPage.class));
            }
        });
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditProfile.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void UpdateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dob.setText(sdf.format(myCalendar.getTime()));
    }
}