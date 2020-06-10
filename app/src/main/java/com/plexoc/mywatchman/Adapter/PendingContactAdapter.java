package com.plexoc.mywatchman.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.plexoc.mywatchman.Model.EmergencyContact;
import com.plexoc.mywatchman.R;

import java.util.List;

public class PendingContactAdapter extends RecyclerView.Adapter<PendingContactAdapter.MyviewHolder> {

    List<EmergencyContact> pendingContacts;

    public PendingContactAdapter(List<EmergencyContact> pendingContacts) {
        this.pendingContacts = pendingContacts;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact_list_layout, parent, false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {

        holder.textview_conatact_name.setText(pendingContacts.get(position).ContactName);
        holder.textview_conatact_number.setText(pendingContacts.get(position).ContactPhone);
        holder.textview_conatact_relation.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return pendingContacts.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder{

        public AppCompatTextView textview_conatact_name,textview_conatact_number,textview_conatact_relation;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);

            textview_conatact_name = itemView.findViewById(R.id.textview_conatact_name);
            textview_conatact_number = itemView.findViewById(R.id.textview_conatact_number);
            textview_conatact_relation = itemView.findViewById(R.id.textview_conatact_relation);
        }
    }
}
