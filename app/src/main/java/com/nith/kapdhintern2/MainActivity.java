package com.nith.kapdhintern2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class MainActivity extends AppCompatActivity {

    RadioButton login,register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = (RadioButton) findViewById(R.id.radio_login);
        register = (RadioButton) findViewById(R.id.radio_register);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_LoginReg,
                    new FragmentLogin()).commit();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               boolean isSelected = login.isChecked();
               if(isSelected)
               {
                   login.setTextColor(Color.WHITE);
                   register.setTextColor(Color.BLACK);
               }
               ChangeFragment(view);

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isSelected = register.isChecked();
                if(isSelected)
                {
                    login.setTextColor(Color.BLACK);
                    register.setTextColor(Color.WHITE);
                }
                ChangeFragment(view);
            }
        });

    }

    public void ChangeFragment(View v)
    {
        Fragment fragment ;
        if(v == findViewById(R.id.radio_login))
        {
            fragment = new FragmentLogin();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_LoginReg , fragment);
            ft.commit();
        }
        if(v == findViewById(R.id.radio_register))
        {
            fragment = new FragmentRegister();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_LoginReg , fragment);
            ft.commit();
        }

    }
}