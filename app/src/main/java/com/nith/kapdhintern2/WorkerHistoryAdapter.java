package com.nith.kapdhintern2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WorkerHistoryAdapter extends RecyclerView.Adapter<WorkerHistoryAdapter.ViewHolder> {

    Context mContext;
    ArrayList<WorkerHistoryItem> workerHistoryItemArrayList;

    public WorkerHistoryAdapter (Context mContext, ArrayList<WorkerHistoryItem> workerHistoryItemArrayList) {
        this.mContext = mContext;
        this.workerHistoryItemArrayList = workerHistoryItemArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.worker_history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final long orderNumber = workerHistoryItemArrayList.get(position).getOrderNumber();
        if (orderNumber != 0) {
            holder.workerHistoryItemOrderIdTextView.setText("Order #" + String.valueOf(orderNumber));
            holder.workerHistoryItemPriceTextView.setText("\u20B9"+String.valueOf(workerHistoryItemArrayList.get(position).getPrice()));
            holder.workerHistoryItemDaysTextView.setText(String.valueOf(workerHistoryItemArrayList.get(position).getDaysCount()+" days"));
            holder.workerHistoryItemDateTextView.setText(String.valueOf(workerHistoryItemArrayList.get(position).getOrderTime()));
            int customerRating = workerHistoryItemArrayList.get(position).getCustomerRating();
            if (customerRating > 0 ) {
                holder.workerHistoryItemRatingTextView.setText(String.valueOf(customerRating)+"/5");
            } else {
                holder.workerHistoryItemRatingTextView.setText("-/-");
            }

            if ( orderNumber != -1) {
                holder.workerHistoryItemDetailButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent myIntent = new Intent(mContext, WorkerHistoryItemDetailActivity.class);
                        myIntent.putExtra("orderNumber", orderNumber);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(myIntent);
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent myIntent = new Intent(mContext, WorkerHistoryItemDetailActivity.class);
                        myIntent.putExtra("orderNumber", orderNumber);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(myIntent);
                    }
                });
            }
        } else {
            holder.workerHistoryItemOrderIdTextView.setText("No order");
            holder.workerHistoryItemPriceTextView.setText("");
            holder.workerHistoryItemDaysTextView.setText("");
            holder.workerHistoryItemDateTextView.setText("");
            holder.workerHistoryItemRatingTextView.setText("");
            holder.workerHistoryItemDetailButton.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return workerHistoryItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView workerHistoryItemOrderIdTextView;
        TextView workerHistoryItemPriceTextView;
        TextView workerHistoryItemDaysTextView;
        TextView workerHistoryItemDateTextView;
        TextView workerHistoryItemRatingTextView;
        Button workerHistoryItemDetailButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            workerHistoryItemOrderIdTextView = itemView.findViewById(R.id.workerHistoryItemOrderIdTextView);
            workerHistoryItemPriceTextView = itemView.findViewById(R.id.workerHistoryItemPriceTextView);
            workerHistoryItemDaysTextView = itemView.findViewById(R.id.workerHistoryItemDaysTextView);
            workerHistoryItemDateTextView = itemView.findViewById(R.id.workerHistoryItemDateTextView);
            workerHistoryItemRatingTextView = itemView.findViewById(R.id.workerHistoryItemRatingTextView);
            workerHistoryItemDetailButton = itemView.findViewById(R.id.workerHistoryItemDetailButton);
        }
    }
}
