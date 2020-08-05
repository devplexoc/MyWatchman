package com.plexoc.mywatchman.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.plexoc.mywatchman.Model.Address;
import com.plexoc.mywatchman.R;
import com.plexoc.mywatchman.Utils.Constants;

import java.util.List;

public class AddressAdpter extends RecyclerView.Adapter<AddressAdpter.ViewHolder> {

    private Context context;
    private List<Address> addressList;
    private AddressCallBack addressCallBack;
    private EditAddress editAddress;

    public AddressAdpter(Context context, List<Address> addressList, AddressCallBack addressCallBack, EditAddress editAddress) {
        this.addressList = addressList;
        this.addressCallBack = addressCallBack;
        this.context = context;
        this.editAddress = editAddress;
    }

    public interface AddressCallBack {
        void getAddress(String name, String Address, int AddressId);
    }

    public interface EditAddress {
        void getAddress(Address address);
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

        holder.imageview_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, holder.imageview_edit);
                popup.getMenuInflater().inflate(R.menu.item_menu_list, popup.getMenu());
                popup.getMenu().getItem(1).setVisible(false);
                popup.getMenu().getItem(2).setVisible(false);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.edit) {
                            editAddress.getAddress(addressList.get(position));
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView cardView_address;
        public AppCompatImageView imageview_edit;
        AppCompatTextView textViewAddressName, textViewAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView_address = itemView.findViewById(R.id.cardview_address);
            textViewAddress = itemView.findViewById(R.id.textview_address);
            textViewAddressName = itemView.findViewById(R.id.textview_name);
            imageview_edit = itemView.findViewById(R.id.imageview_edit);
        }
    }
}
