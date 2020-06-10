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

import com.plexoc.mywatchman.Model.User;
import com.plexoc.mywatchman.R;

import java.util.List;

public class UserAddAdapter extends RecyclerView.Adapter<UserAddAdapter.MyviewHolder> {

    List<User> userlist;
    Context context;
    //View.OnClickListener onClickListener;
    SubuserEdit subuserEdit;
    SubuserDelete subuserDelete;
    SubuserResetPassword subuserResetPassword;


    public UserAddAdapter(List<User> userlist, Context context, SubuserEdit subuserEdit, SubuserDelete subuserDelete, SubuserResetPassword subuserResetPassword) {
        this.userlist = userlist;
        this.context = context;
        //this.onClickListener = onClickListener;
        this.subuserEdit = subuserEdit;
        this.subuserDelete = subuserDelete;
        this.subuserResetPassword = subuserResetPassword;

    }

    public interface SubuserEdit {
        void getSubuserData(int Id, String firstName, String lastName, String Email, String mobileNumber, String password);
    }

    public interface SubuserDelete {
        void getSubuserDelete(int Id);
    }

    public interface SubuserResetPassword {
        void getSubuserResetPassword(int Id, String Password);
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_add_layout, parent, false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {

        holder.textview_username.setText(userlist.get(position).FirstName);
        holder.textview_mobilenumber.setText(userlist.get(position).Mobile);
        //holder.textview_password.setText(userlist.get(position).Password);

       /* if (userlist.get(position).isShow) {
            holder.imageview_passwordshowhide.setImageResource(R.drawable.ic_password_show);
            holder.textview_password.setTransformationMethod(null);
        } else {
            holder.imageview_passwordshowhide.setImageResource(R.drawable.ic_password_hide);
            holder.textview_password.setTransformationMethod(new PasswordTransformationMethod());
        }

        holder.imageview_passwordshowhide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userlist.get(position).isShow) {
                    userlist.get(position).isShow = false;
                } else {
                    userlist.get(position).isShow = true;
                }
                notifyDataSetChanged();
            }
        });*/

        holder.imageview_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, holder.imageview_edit);
                popup.getMenuInflater().inflate(R.menu.item_menu_list, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit:
                                subuserEdit.getSubuserData(userlist.get(position).Id, userlist.get(position).FirstName, userlist.get(position).LastName, userlist.get(position).Email, userlist.get(position).Mobile, userlist.get(position).Password);
                                break;
                            case R.id.delete:
                                new AlertDialog.Builder(context).setMessage("Are you sure want to Delete?")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                subuserDelete.getSubuserDelete(userlist.get(position).Id);
                                            }
                                        })
                                        .setNegativeButton("No", null)
                                        .show();
                                break;
                            case R.id.resetpassword:
                                subuserResetPassword.getSubuserResetPassword(userlist.get(position).Id, userlist.get(position).Password);
                                break;
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
        return userlist.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView textview_username, textview_mobilenumber, textview_password;
        public AppCompatImageView imageview_passwordshowhide, imageview_edit;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);

            textview_username = itemView.findViewById(R.id.textview_username);
            textview_mobilenumber = itemView.findViewById(R.id.textview_mobilenumber);
            textview_password = itemView.findViewById(R.id.textview_password);
            imageview_passwordshowhide = itemView.findViewById(R.id.imageview_passwordshowhide);
            imageview_edit = itemView.findViewById(R.id.imageview_edit);
        }
    }
}
