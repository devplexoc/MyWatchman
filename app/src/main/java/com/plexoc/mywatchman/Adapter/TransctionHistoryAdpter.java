package com.plexoc.mywatchman.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.plexoc.mywatchman.Model.TransactionHistory;
import com.plexoc.mywatchman.R;

import java.util.List;

public class TransctionHistoryAdpter extends RecyclerView.Adapter<TransctionHistoryAdpter.ViewHolder> {

    private Context context;
    private List<TransactionHistory> transactionHistoryList;

    public TransctionHistoryAdpter(List<TransactionHistory> transactionHistoryList,Context context) {
        this.transactionHistoryList = transactionHistoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transcation_list_layout, parent, false);
        return new TransctionHistoryAdpter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(transactionHistoryList.get(position).IsCancel){
            holder.view_divider.setBackgroundColor(context.getResources().getColor(R.color.colorRed));
        }else {
            holder.view_divider.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        }

        holder.textview_plan_name.setText(transactionHistoryList.get(position).PlanName);
        holder.textview_plan_amount.setText("$"+" "+String.valueOf(transactionHistoryList.get(position).TransactionAmount));
        holder.textview_plan_date.setText(transactionHistoryList.get(position).TransactionStartDate);
    }

    @Override
    public int getItemCount() {
        return transactionHistoryList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView textview_plan_name,textview_plan_amount,textview_plan_date;
        public View view_divider;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textview_plan_name = itemView.findViewById(R.id.textview_plan_name);
            textview_plan_amount = itemView.findViewById(R.id.textview_plan_amount);
            textview_plan_date = itemView.findViewById(R.id.textview_plan_date);
            view_divider = itemView.findViewById(R.id.view_divider);
        }
    }
}
