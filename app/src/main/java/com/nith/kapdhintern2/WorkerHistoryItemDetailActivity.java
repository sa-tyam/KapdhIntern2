package com.nith.kapdhintern2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WorkerHistoryItemDetailActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Long orderId;
    String uid;
    ProgressDialog pd;

    // views used
    TextView workerHistoryItemDetailOrderIdTextView;
    TextView workerHistoryItemDetailTimeTextView;
    TextView workerHistoryItemDetailTotalDaysTextView;
    TextView workerHistoryItemDetailTotalCostTextView;
    TextView workerHistoryItemDetailCustomerNameTextView;
    TextView workerHistoryItemDetailCustomerPhoneTextView;
    TextView workerHistoryItemDetailCustomerAddressTextView;
    TextView workerHistoryItemDetailCustomerPinCodeTextView;
    TextView workerHistoryItemDetailCustomerRatingTextView;
    TextView workerHistoryItemDetailCustomerReviewTextView;

    // variables used
    long orderNumber = -1;
    int price = -1;
    int daysCount = -1;
    String orderTime = "";
    String customerId = "";
    int customerRating = -1;
    String customerReview = "";

    String customerName;
    String customerMobile;
    String customerAddress;
    String customerPincode;

    public void backButtonPressed (View v) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_history_item_detail);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        initViews();
        orderId = getIntent().getLongExtra("orderNumber", -1);
        uid = FirebaseAuth.getInstance().getUid();
        pd = new ProgressDialog(getApplicationContext());
        pd.show();
        pd.setMessage("Wait");
        getDataFromDatabase();
    }

    private void getDataFromDatabase() {
        databaseReference.child(String.valueOf(uid)).child("Service Provider").child("History").child(String.valueOf(orderId)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getDataForViews(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getDataForViews(DataSnapshot snapshot) {

        for (DataSnapshot keyNode : snapshot.getChildren()) {

            if (keyNode.getKey().equals("orderId")) {
                orderNumber = keyNode.getValue(Long.class);
                Log.d("detail order number", String.valueOf(orderNumber));
            }
            if (keyNode.getKey().equals("price")) {
                price = keyNode.getValue(Integer.class);
            }
            if (keyNode.getKey().equals("daysCount")) {
                daysCount = (int)keyNode.getValue(Integer.class);
            }
            if (keyNode.getKey().equals("orderTime")) {
                orderTime = keyNode.getValue(String.class);
            }
            if (keyNode.getKey().equals("customerId")) {
                customerId = keyNode.getValue(String.class);
                getCustomerDetails(customerId);
            }
            if (keyNode.getKey().equals("customerRating")) {
                customerRating = keyNode.getValue(Integer.class);
            }
            if (keyNode.getKey().equals("customerReview")) {
                customerReview = keyNode.getValue(String.class);
            }
        }
        setDataInViews();
    }

    public void setDataInViews () {
        if(pd.isShowing()) {
            pd.cancel();
        }
        workerHistoryItemDetailOrderIdTextView.setText("#" + String.valueOf(orderId));
        workerHistoryItemDetailTimeTextView.setText(orderTime);
        workerHistoryItemDetailTotalDaysTextView.setText(String.valueOf(daysCount) + " days");
        workerHistoryItemDetailTotalCostTextView.setText("\u20B9" + String.valueOf(price));
        workerHistoryItemDetailCustomerRatingTextView.setText(String.valueOf(customerRating)+"\5");
        workerHistoryItemDetailCustomerReviewTextView.setText(customerReview);
    }

    public void getCustomerDetails(String customerId) {
        databaseReference.child(customerId).child("Service Provider").child("Profile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot keyNode : snapshot.getChildren()) {
                    if (keyNode.getKey().equals("Name")) {
                        customerName = keyNode.getValue(String.class);
                    }
                    if (keyNode.getKey().equals("Phone")) {
                        customerMobile = keyNode.getValue(String.class);
                    }
                    if (keyNode.getKey().equals("City")) {
                        customerAddress = keyNode.getValue(String.class);
                    }
                    if (keyNode.getKey().equals("Name")) {
                        customerPincode = keyNode.getValue(String.class);
                    }
                }
                setDataForCustomerInViews();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setDataForCustomerInViews() {
        workerHistoryItemDetailCustomerNameTextView.setText(customerName);
        workerHistoryItemDetailCustomerPhoneTextView.setText(customerMobile);
        workerHistoryItemDetailCustomerAddressTextView.setText(customerAddress);
        workerHistoryItemDetailCustomerPinCodeTextView.setText(customerPincode);
    }

    public void initViews () {
        workerHistoryItemDetailOrderIdTextView = findViewById(R.id.workerHistoryItemDetailOrderIdTextView);
        workerHistoryItemDetailTimeTextView = findViewById(R.id.workerHistoryItemDetailOrderTimeTextView);
        workerHistoryItemDetailTotalDaysTextView = findViewById(R.id.workerHistoryItemDetailTotalDaysTextView);
        workerHistoryItemDetailTotalCostTextView = findViewById(R.id.workerHistoryItemDetailTotalCostTextView);
        workerHistoryItemDetailCustomerNameTextView = findViewById(R.id.workerHistoryItemDetailCustomerNameTextView);
        workerHistoryItemDetailCustomerPhoneTextView = findViewById(R.id.workerHistoryItemDetailCustomerMobileTextView);
        workerHistoryItemDetailCustomerAddressTextView = findViewById(R.id.workerHistoryItemDetailCustomerAddressTextView);
        workerHistoryItemDetailCustomerPinCodeTextView = findViewById(R.id.workerHistoryItemDetailCustomerPinCodeTextView);
        workerHistoryItemDetailCustomerRatingTextView = findViewById(R.id.workerHistoryItemDetailCustomerRatingTextView);
        workerHistoryItemDetailCustomerReviewTextView = findViewById(R.id.workerHistoryItemDetailCustomerReviewTextView);
    }
}