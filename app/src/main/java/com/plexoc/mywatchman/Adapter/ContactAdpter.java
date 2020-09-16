package com.plexoc.mywatchman.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.plexoc.mywatchman.Model.EmergencyContact;
import com.plexoc.mywatchman.R;

import java.util.List;

public class ContactAdpter extends RecyclerView.Adapter<ContactAdpter.ViewHolder> {

    private Context context;
    private List<EmergencyContact> emergencyContactList;
    private EmerengencyCallback emerengencyCallback;

    public ContactAdpter(Context context, List<EmergencyContact> emergencyContactList, EmerengencyCallback emerengencyCallback) {
        this.emergencyContactList = emergencyContactList;
        this.context = context;
        this.emerengencyCallback = emerengencyCallback;
    }

    public interface EmerengencyCallback {
        void getId(int Id);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.textview_conatact_name.setText(emergencyContactList.get(position).ContactName);
        holder.textview_conatact_number.setText(emergencyContactList.get(position).ContactPhone);

        if (emergencyContactList.get(position).Relation != null && !emergencyContactList.get(position).Relation.equals(""))
            holder.textview_conatact_relation.setText(emergencyContactList.get(position).Relation);
        else
            holder.textview_conatact_relation.setVisibility(View.GONE);

        if (emergencyContactList.get(position).ContactEmail != null && !emergencyContactList.get(position).ContactEmail.equals(""))
            holder.textview_conatact_email.setText(emergencyContactList.get(position).ContactEmail);
        else
            holder.textview_conatact_email.setVisibility(View.GONE);

        if (!emergencyContactList.get(position).ApprovedStatus){
            holder.textView_contact_status.setVisibility(View.VISIBLE);
        }
        else
            holder.textView_contact_status.setVisibility(View.GONE);

        holder.imageViewDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Remove Contact")
                    .setMessage("Are you sure you want to remove this contact")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            emerengencyCallback.getId(emergencyContactList.get(position).Id);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();

        });
    }

    @Override
    public int getItemCount() {
        return emergencyContactList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView textview_conatact_name, textview_conatact_number,
                textview_conatact_relation, textview_conatact_email, textView_contact_status;
        AppCompatImageView imageViewDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textview_conatact_name = itemView.findViewById(R.id.textview_conatact_name);
            textview_conatact_number = itemView.findViewById(R.id.textview_conatact_number);
            textview_conatact_relation = itemView.findViewById(R.id.textview_conatact_relation);
            textview_conatact_email = itemView.findViewById(R.id.textview_conatact_email);
            textView_contact_status = itemView.findViewById(R.id.textview_conatact_status);
            imageViewDelete = itemView.findViewById(R.id.imageview_delete);
        }
    }
}
