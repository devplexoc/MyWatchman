package com.plexoc.mywatchman.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.plexoc.mywatchman.R;

public class DashboardAddressAdpter extends RecyclerView.Adapter<DashboardAddressAdpter.ViewHolder> {

    private View.OnClickListener onClickListener;

    public DashboardAddressAdpter(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dashboard_address_list_layout, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_two_location, parent, false);
        return new DashboardAddressAdpter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        /*if (position == 1)
            holder.textViewAddressName.setText("OFFICE");
        if (position == 2)
            holder.textViewAddressName.setText("My Location");*/

        holder.cardViewFirstLocation.setOnClickListener(v -> {
            onClickListener.onClick(v);
        });
        holder.cardViewSecondLocation.setOnClickListener(v -> {
            onClickListener.onClick(v);
        });
        holder.cardViewThirdLocation.setOnClickListener(v -> {
            onClickListener.onClick(v);
        });
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView textViewAddressName;
        ConstraintLayout cardViewRaiseSOS,cardViewFirstLocation,cardViewSecondLocation,cardViewThirdLocation;
        AppCompatImageView imageviewSos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAddressName = itemView.findViewById(R.id.textview_address_name);
            imageviewSos = itemView.findViewById(R.id.imageview_sos);
            //cardViewRaiseSOS = itemView.findViewById(R.id.cardview_raise_sos);

            cardViewFirstLocation = itemView.findViewById(R.id.cardview_one);
            cardViewSecondLocation = itemView.findViewById(R.id.cardview_two);
            cardViewThirdLocation = itemView.findViewById(R.id.cardview_three);
        }
    }
}
