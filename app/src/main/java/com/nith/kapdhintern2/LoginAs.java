package com.nith.kapdhintern2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class LoginAs extends AppCompatActivity {

    Button b1,b2;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_as);
        b1 = (Button) findViewById(R.id.login_ascustomer);
        b2 = (Button) findViewById(R.id.login_asServiceprvdr);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginAs.this,CustomerPage.class));
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginAs.this,WorkerPage.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        auth = FirebaseAuth.getInstance();
        auth.signOut();
    }
}