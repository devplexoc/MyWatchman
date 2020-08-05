package com.plexoc.mywatchman.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;


import com.plexoc.mywatchman.Model.Notifiaction;
import com.plexoc.mywatchman.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private Context context;
    private List<Notifiaction> notifiactionList;

    public NotificationAdapter(Context context, List<Notifiaction> notifiactionList) {
        this.context = context;
        this.notifiactionList = notifiactionList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_list_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (notifiactionList.get(position).Description != null)
            holder.textview_notification.setText(notifiactionList.get(position).Description);
        else
            holder.textview_notification.setText(" - ");
    }

    @Override
    public int getItemCount() {
        return notifiactionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView textview_notification;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textview_notification = itemView.findViewById(R.id.textview_notification);

        }
    }
}
