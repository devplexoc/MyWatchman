package com.plexoc.mywatchman.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.plexoc.mywatchman.Model.User;
import com.plexoc.mywatchman.R;

import java.util.List;

public class CommunityRequestAdpter extends RecyclerView.Adapter<CommunityRequestAdpter.ViewHolder> {

    private List<User> communityList;
    private Callback callback;
    private Context context;

    public CommunityRequestAdpter(List<User> communityList, Callback callback, Context context) {
        this.communityList = communityList;
        this.callback = callback;
        this.context = context;
    }

    public interface Callback {
        void getStatus(int Id, boolean status);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_community_request_list_layout, parent, false);
        return new CommunityRequestAdpter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.imageview_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setMessage("Are you sure want to Cancel Request?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                callback.getStatus(communityList.get(position).Id, false);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        holder.imageview_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.getStatus(communityList.get(position).Id, true);
            }
        });

        holder.textView_name.setText(communityList.get(position).FirstName + " " + communityList.get(position).LastName);
    }

    @Override
    public int getItemCount() {
        return communityList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView textView_name;
        public AppCompatImageView imageview_reject, imageview_accept;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView_name = itemView.findViewById(R.id.textview_name);
            imageview_reject = itemView.findViewById(R.id.imageview_reject);
            imageview_accept = itemView.findViewById(R.id.imageview_accept);
        }
    }
}
