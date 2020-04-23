package com.plexoc.myapplication.Fragment;


import android.Manifest;
import android.bluetooth.BluetoothAssignedNumbers;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Contacts;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.plexoc.myapplication.Adapter.ContactAdpter;
import com.plexoc.myapplication.Adapter.ContactListAdapter;
import com.plexoc.myapplication.Model.ContactList;
import com.plexoc.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import static com.plexoc.myapplication.Fragment.AddEmergencyContactFragment.RequestPermissionCode;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowContactListFragment extends BaseFragment {


    public ShowContactListFragment() {
        // Required empty public constructor
    }

    private Toolbar toolbar;
    private ArrayList<ContactList> contactLists = new ArrayList<>();
    private RecyclerView recyclerView_contactlist;
    private ContactListAdapter contactListAdapter;
    Cursor cursor;
    //private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_contact_list, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Contact List");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());

        recyclerView_contactlist = view.findViewById(R.id.recyclerview_contact_list);

        Dexter.withActivity(getActivity()).withPermission(Manifest.permission.READ_CONTACTS)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {


                        String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '"
                                + ("1") + "'";
                        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME
                                + " COLLATE LOCALIZED ASC";

                        cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, selection + " AND " + ContactsContract.Contacts.HAS_PHONE_NUMBER
                                + "=1", null, sortOrder);

                        while (cursor.moveToNext()) {
                            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                            String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                            phoneNumber = phoneNumber.replaceAll("[^a-zA-Z0-9]", "");


                            ContactList contactListmodel = new ContactList(name, phoneNumber);
                            contactListmodel.setName(name);
                            contactListmodel.setContactNumber(phoneNumber);

                            if (!contactLists.contains(contactListmodel))
                                contactLists.add(contactListmodel);

                            Log.d("name>>", name + "  " + phoneNumber);


                        }
                        cursor.close();

                        contactListAdapter = new ContactListAdapter(contactLists);
                        recyclerView_contactlist.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView_contactlist.setAdapter(contactListAdapter);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(getContext(), "Please give Permission", Toast.LENGTH_SHORT).show();
                        //EnableRuntimePermission();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();


        return view;
    }

    public void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.READ_CONTACTS)) {

            Toast.makeText(getActivity(), "CONTACTS permission allows us to Access CONTACTS app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.READ_CONTACTS}, RequestPermissionCode);

        }
    }


   /* private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
            LoadContact loadContact = new LoadContact();
            loadContact.execute();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();
            } else {
                Toast.makeText(getContext(), "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class LoadContact extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Get Contact list from Phone

            if (cursor != null) {
                Log.e("count", "" + cursor.getCount());
                if (cursor.getCount() == 0) {
                }

                while (cursor.moveToNext()) {
                    Bitmap bit_thumb = null;
                    //String id = cursor.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


                    ContactsContract.Contacts selectUser = new ContactsContract.Contacts();
                    selectUser.setName(name);
                    selectUser.setPhone(phoneNumber);
                    selectUsers.add(selectUser);
                }
            } else {
                Log.e("Cursor close 1", "----------------");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // sortContacts();
            int count = selectUsers.size();
            ArrayList<Contacts> removed = new ArrayList<>();
            ArrayList<Contacts> contacts = new ArrayList<>();
            for (int i = 0; i < selectUsers.size(); i++) {
                Contacts inviteFriendsProjo = selectUsers.get(i);

                if (inviteFriendsProjo.getName().matches("\\d+(?:\\.\\d+)?") || inviteFriendsProjo.getName().trim().length() == 0) {
                    removed.add(inviteFriendsProjo);
                    Log.d("Removed Contact", new Gson().toJson(inviteFriendsProjo));
                } else {
                    contacts.add(inviteFriendsProjo);
                }
            }
            contacts.addAll(removed);
            selectUsers = contacts;
            contactAdpter = new ContactAdpter(inflater, selectUsers);
            recyclerView_contactlist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView_contactlist.setAdapter(adapter);

        }
    }*/
}
