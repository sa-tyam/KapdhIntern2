package com.nith.kapdhintern2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class RegisterAs extends AppCompatActivity {

    Button cust,sp;
    String email,phone,aadhar,name,dob,pin,state,district,city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_as);
        cust = findViewById(R.id.reg_ascustomer);
        sp = findViewById(R.id.reg_asServiceprvdr);
        email = getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("phone");
        aadhar = getIntent().getStringExtra("aadhar");
        name = getIntent().getStringExtra("name");
        dob = getIntent().getStringExtra("dob");
        pin = getIntent().getStringExtra("pincode");
        state = getIntent().getStringExtra("state");
        district = getIntent().getStringExtra("district");
        city = getIntent().getStringExtra("city");

        Toast.makeText(this,email,Toast.LENGTH_SHORT).show();
        Toast.makeText(this,phone,Toast.LENGTH_SHORT).show();

        cust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterAs.this,CustomerRegVerification.class);
                intent.putExtra("email",email);
                intent.putExtra("phone",phone);
                intent.putExtra("aadhar",aadhar);
                intent.putExtra("name",name);
                intent.putExtra("dob",dob);
                intent.putExtra("pincode",pin);
                intent.putExtra("state",state);
                intent.putExtra("district",district);
                intent.putExtra("city",city);
                startActivity(intent);
            }
        });
        sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterAs.this,ServiceprvdrRegVerification.class);
                intent.putExtra("email",email);
                intent.putExtra("phone",phone);
                intent.putExtra("aadhar",aadhar);
                intent.putExtra("name",name);
                intent.putExtra("dob",dob);
                intent.putExtra("pincode",pin);
                intent.putExtra("state",state);
                intent.putExtra("district",district);
                intent.putExtra("city",city);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RegisterAs.this,MainActivity.class));
    }
}