package com.plexoc.myapplication.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.plexoc.myapplication.Model.Plan;
import com.plexoc.myapplication.Model.User;
import com.plexoc.myapplication.R;

import java.util.List;

public class PlansAdpter extends RecyclerView.Adapter<PlansAdpter.ViewHolder> {

    private int selectedPosition = 1;
    private Context context;
    private List<Plan> planList;
    private PlanCallBack planCallBack;
    private boolean plan;
    private int user;

    public PlansAdpter(Context context, List<Plan> planList, PlanCallBack planCallBack, boolean plan, int user) {
        this.context = context;
        this.planList = planList;
        this.planCallBack = planCallBack;
        this.plan = plan;
        this.user = user;
    }

    public interface PlanCallBack {
        void getPlanId(Plan plan);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (plan) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan_list_layout, parent, false);
            return new PlansAdpter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan_horizontal_list_layout, parent, false);
            return new PlansAdpter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.textview_plan_price.setText("$" + " " + String.valueOf(planList.get(position).Price));
        holder.textview_plan_categoty.setText(planList.get(position).PlanCategory);
        holder.textview_plan_duration.setText(planList.get(position).PlanDuration + " " + "Month Security");

        /*if (position == 1) {
            holder.textViewPlanName.setText("1 year Security");
            holder.textViewPlanPrice.setText("$200");
        }*/


      /*  if (selectedPosition == position) {
            holder.cardView_plan.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.textview_plan_categoty.setTextColor(context.getResources().getColor(R.color.colorWhite));
        } else {
            holder.cardView_plan.setCardBackgroundColor(Color.WHITE);
            holder.textview_plan_categoty.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        }*/


        if (user == planList.get(position).Id) {
            holder.cardView_plan.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.textview_plan_categoty.setTextColor(context.getResources().getColor(R.color.colorWhite));
        } else {
            holder.cardView_plan.setCardBackgroundColor(Color.WHITE);
            holder.textview_plan_categoty.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        }

        holder.cardView_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planCallBack.getPlanId(planList.get(position));

                /*if (selectedPosition >= 0)
                    notifyItemChanged(selectedPosition);*/

                user = planList.get(position).Id;
                /*selectedPosition = holder.getAdapterPosition();
                notifyItemChanged(selectedPosition);*/
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView textview_plan_categoty, textview_plan_price, textview_plan_duration;
        MaterialCardView cardView_plan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textview_plan_categoty = itemView.findViewById(R.id.textview_plan_categoty);
            textview_plan_price = itemView.findViewById(R.id.textview_plan_price);
            textview_plan_duration = itemView.findViewById(R.id.textview_plan_duration);

            cardView_plan = itemView.findViewById(R.id.cardview_plan);
        }
    }
}
