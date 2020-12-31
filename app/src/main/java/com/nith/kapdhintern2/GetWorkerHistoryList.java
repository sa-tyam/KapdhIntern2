// crated by Satyam
// on 27/12/2020

package com.nith.kapdhintern2;

import android.app.ProgressDialog;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GetWorkerHistoryList {

    // static array list
    static ArrayList<WorkerHistoryItem> workerHistoryItemArrayList = new ArrayList<>();
    static ArrayList<String> dataKeys = new ArrayList<>();

    static FirebaseDatabase firebaseDatabase;
    static DatabaseReference databaseReference;

    public interface DataStatus {
        void DataIsLoaded(ArrayList<WorkerHistoryItem> orderItemArrayList, ArrayList<String> dataKeys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public static void getDataFromDatabase (final DataStatus dataStatus) {
        workerHistoryItemArrayList.clear();
        firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        databaseReference = firebaseDatabase.getReference(uid);

        databaseReference.child("Service Provider").child("History").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount()>0) {
                    for (DataSnapshot keyNode : snapshot.getChildren()) {
                        if (keyNode.getKey() != null) {
                            long orderNumber = Long.valueOf(keyNode.getKey());
                            Log.i("ordernumber", String.valueOf(orderNumber));
                            getFromAll(dataStatus, orderNumber);
                        }
                    }
                } else {
                    if (workerHistoryItemArrayList.isEmpty()) {
                        dataStatus.DataIsLoaded(workerHistoryItemArrayList, dataKeys);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    static public void getFromAll (final DataStatus dataStatus, final Long filter) {

        databaseReference.child("Service Provider").child("History").child(String.valueOf(filter)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                addToList(dataSnapshot);
                dataStatus.DataIsLoaded(workerHistoryItemArrayList, dataKeys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static ArrayList<WorkerHistoryItem> addToList (DataSnapshot dataSnapshot) {

        long orderNumber = -1;
        int price = -1;
        int daysCount = -1;
        String orderTime = "";
        String customerId = "";
        int customerRating = -1;

        for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
            dataKeys.add(keyNode.getKey());

            if (keyNode.getKey().equals("orderId")) {
                orderNumber = keyNode.getValue(Long.class);
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
            }
            if (keyNode.getKey().equals("customerRating")) {
                customerRating = keyNode.getValue(Integer.class);
            }
        }
        if (orderNumber != -1) {
            WorkerHistoryItem item = new WorkerHistoryItem(orderNumber, price, daysCount, orderTime, customerId);
            if (customerRating != -1) {
                item.setCustomerRating(customerRating);
            }
            workerHistoryItemArrayList.add(item);
        }

        return workerHistoryItemArrayList;
    }
}
