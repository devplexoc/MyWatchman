package com.plexoc.myapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.plexoc.myapplication.Model.Plan;
import com.plexoc.myapplication.Model.RaisedSOSUser;
import com.plexoc.myapplication.R;

import java.util.List;

public class RaisedSOSAdpter extends RecyclerView.Adapter<RaisedSOSAdpter.ViewHolder> {

    List<RaisedSOSUser> raisedSOSUserList;
    RaisedSOSCallback raisedSOSCallback;

    public RaisedSOSAdpter(List<RaisedSOSUser> raisedSOSUserList, RaisedSOSCallback raisedSOSCallback) {
        this.raisedSOSUserList = raisedSOSUserList;
        this.raisedSOSCallback = raisedSOSCallback;
    }

    public interface RaisedSOSCallback {
        void getRaisedSOSId(RaisedSOSUser raisedSOSUser);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sos_raised_list_layout, parent, false);
        return new RaisedSOSAdpter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            holder.textview_raised_sos_date.setText(raisedSOSUserList.get(position).StartDate);
            holder.textview_raised_sos_time.setText(raisedSOSUserList.get(position).StartTime);
            if (raisedSOSUserList.get(position).CompletedSOSInMin != null)
                holder.textview_help_reached.setText(raisedSOSUserList.get(position).CompletedSOSInMin + " Min.");
            else holder.textview_help_reached.setText(" - ");

            holder.textview_roaming_staff_name.setText(raisedSOSUserList.get(position).ResponserName);
            holder.cardview_raisedsos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    raisedSOSCallback.getRaisedSOSId(raisedSOSUserList.get(position));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return raisedSOSUserList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView textview_raised_sos_date, textview_raised_sos_time, textview_help_reached, textview_roaming_staff_name;
        public MaterialCardView cardview_raisedsos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textview_raised_sos_date = itemView.findViewById(R.id.textview_raised_sos_date);
            textview_raised_sos_time = itemView.findViewById(R.id.textview_raised_sos_time);
            textview_help_reached = itemView.findViewById(R.id.textview_help_reached);
            textview_roaming_staff_name = itemView.findViewById(R.id.textview_roaming_staff_name);
            cardview_raisedsos = itemView.findViewById(R.id.cardview_raisedsos);
        }
    }
}
