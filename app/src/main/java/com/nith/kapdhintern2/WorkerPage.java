// created by Satyam
// on 25/12/2020

package com.nith.kapdhintern2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class WorkerPage extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_page);

        bottomNavigationView = findViewById(R.id.worker_bottom_navigation);

        //set home as selected
        bottomNavigationView.setSelectedItemId(R.id.bottombar_home);
        getSupportFragmentManager().beginTransaction().replace(R.id.worker_fragment_container,
                new WorkerHomeFragment()).commit();

        //set on item selected
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.bottombar_home:
                        selectedFragment = new WorkerHomeFragment();
                        break;

                    case R.id.bottombar_orders:
                        selectedFragment = new WorkerOrdersFragment();
                        break;

                    case R.id.bottombar_products:
                        selectedFragment = new WorkerNotificationFragment();
                        break;

                    case R.id.bottombar_categories:
                        selectedFragment = new WorkerCategoryFragment();
                        break;

                    case R.id.bottombar_account:
                        selectedFragment = new WorkerCategoryFragment();
                        break;

                }

                getSupportFragmentManager().beginTransaction().replace(R.id.worker_fragment_container,
                        selectedFragment).commit();

                return true;
            }
        });
    }
}