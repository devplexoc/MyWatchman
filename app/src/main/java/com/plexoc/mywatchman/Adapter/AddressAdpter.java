package com.plexoc.mywatchman.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.plexoc.mywatchman.Model.Address;
import com.plexoc.mywatchman.R;

import java.util.List;

public class AddressAdpter extends RecyclerView.Adapter<AddressAdpter.ViewHolder> {

    private List<Address> addressList;
    private AddressCallBack addressCallBack;

    public AddressAdpter(List<Address> addressList, AddressCallBack addressCallBack) {
        this.addressList = addressList;
        this.addressCallBack = addressCallBack;
    }

    public interface AddressCallBack {
        void getAddress(String name, String Address, int AddressId);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address_list_layout, parent, false);
        return new AddressAdpter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.textViewAddressName.setText(addressList.get(position).AddressName);
        holder.textViewAddress.setText(addressList.get(position).Address);

        holder.cardView_address.setOnClickListener(v -> addressCallBack.getAddress(addressList.get(position).AddressName,
                addressList.get(position).Address,
                addressList.get(position).Id));

    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView cardView_address;
        AppCompatTextView textViewAddressName, textViewAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView_address = itemView.findViewById(R.id.cardview_address);
            textViewAddress = itemView.findViewById(R.id.textview_address);
            textViewAddressName = itemView.findViewById(R.id.textview_name);
        }
    }
}
