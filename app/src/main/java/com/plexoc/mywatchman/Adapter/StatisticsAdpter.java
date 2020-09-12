package com.plexoc.mywatchman.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.plexoc.mywatchman.Model.SosType;
import com.plexoc.mywatchman.R;

import java.util.List;

public class StatisticsAdpter extends RecyclerView.Adapter<StatisticsAdpter.ViewHolder> {

    private List<SosType> sosTypeList;

    public StatisticsAdpter(List<SosType> sosTypeList) {
        this.sosTypeList = sosTypeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_statistics_list_layout, parent, false);
        return new StatisticsAdpter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (sosTypeList.get(position).Type != null)
            holder.textViewName.setText(sosTypeList.get(position).Type);
        holder.textViewCount.setText(String.valueOf(sosTypeList.get(position).SOSCount));
    }

    @Override
    public int getItemCount() {
        return sosTypeList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView textViewCount;
        AppCompatTextView textViewName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCount = itemView.findViewById(R.id.textview_count);
            textViewName = itemView.findViewById(R.id.textview_name);
        }
    }
}
