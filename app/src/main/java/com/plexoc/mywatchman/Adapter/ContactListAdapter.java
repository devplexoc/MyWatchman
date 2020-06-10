package com.plexoc.mywatchman.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.plexoc.mywatchman.Model.ContactList;
import com.plexoc.mywatchman.R;

import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.MyviewHolder> {

    List<ContactList> contctLists;

    public ContactListAdapter(List<ContactList> contctLists) {
        this.contctLists = contctLists;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_showallcontact_list_layout, parent, false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
        holder.textview_contactperson_name.setText(contctLists.get(position).getName());
        holder.textview_contactperson_contact.setText(contctLists.get(position).getContactNumber());
    }

    @Override
    public int getItemCount() {
        return contctLists.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder{

        AppCompatTextView textview_contactperson_name,textview_contactperson_contact;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);

            textview_contactperson_name = itemView.findViewById(R.id.textview_contactperson_name);
            textview_contactperson_contact = itemView.findViewById(R.id.textview_contactperson_contact);
        }
    }
}
