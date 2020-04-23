package com.plexoc.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.plexoc.myapplication.Model.OptionsModel;
import com.plexoc.myapplication.R;

import java.util.List;

public class OptionsAdpter extends RecyclerView.Adapter<OptionsAdpter.ViewHolder> {

    private Context context;
    private List<OptionsModel> optionsList;
    private int SelectedIndex = -1;
    private OptionsCallback optionsCallback;

    public OptionsAdpter(Context context, List<OptionsModel> optionsList, OptionsCallback optionsCallback) {
        this.context = context;
        this.optionsList = optionsList;
        this.optionsCallback = optionsCallback;
    }

    public interface OptionsCallback {
        void getDetail(OptionsModel optionsModel);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_options, parent, false);
        return new OptionsAdpter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.textViewOptionsOne.setText(optionsList.get(position).optionText);

        holder.viewOptionOne.setOnClickListener(v -> {
            optionsCallback.getDetail(optionsList.get(position));
            SelectedIndex = position;
            notifyDataSetChanged();
        });


        if (SelectedIndex == position) {
            holder.imageViewOptionOne.setSelected(true);
            holder.textViewOptionsOne.setSelected(true);
            holder.viewOptionOne.setSelected(true);

            holder.isSelected = true;

        } else {
            holder.imageViewOptionOne.setSelected(false);
            holder.textViewOptionsOne.setSelected(false);
            holder.viewOptionOne.setSelected(false);

            holder.isSelected = false;
        }
    }

    @Override
    public int getItemCount() {
        return optionsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private View viewOptionOne;
        private ImageView imageViewOptionOne;
        private TextView textViewOptionsOne;
        private boolean isSelected;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            viewOptionOne = itemView.findViewById(R.id.viewOptionOne);
            imageViewOptionOne = itemView.findViewById(R.id.imageViewOptionOne);
            textViewOptionsOne = itemView.findViewById(R.id.textViewOptionsOne);
            isSelected = false;
        }
    }
}
