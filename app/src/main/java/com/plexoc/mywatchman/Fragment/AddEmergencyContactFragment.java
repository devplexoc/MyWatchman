package com.plexoc.mywatchman.Fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.plexoc.mywatchman.Model.EmergencyContact;
import com.plexoc.mywatchman.Model.Response;
import com.plexoc.mywatchman.R;
import com.plexoc.mywatchman.Utils.Constants;
import com.plexoc.mywatchman.Utils.LoadingDialog;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddEmergencyContactFragment extends BaseFragment {


    public AddEmergencyContactFragment() {
        // Required empty public constructor
    }

    public static final int RequestPermissionCode = 1;

    private EmergencyContact emergencyContact;
    private Toolbar toolbar;

    private TextInputLayout textinput_addcontact_contactname, textinput_addcontact_contactphone, textinput_addcontact_contectemail,
            textinput_addcontact_contectrelation;
    private TextInputEditText edittext_addcontact_contactname, edittext_addcontact_contactphone, edittext_addcontact_contectemail,
            edittext_addcontact_contectrelation;
    private MaterialButton button_addcontact_add, button_addcontact_contactlist;
    private AppCompatImageView imageview_contact;
    private AppCompatTextView textview_countrycode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_emergency_contact, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Add Emergency Contact");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());

        emergencyContact = new EmergencyContact();

        textview_countrycode = view.findViewById(R.id.textview_countrycode);

        textinput_addcontact_contactname = view.findViewById(R.id.textinput_addcontact_contactname);
        textinput_addcontact_contactphone = view.findViewById(R.id.textinput_addcontact_contactphone);
        textinput_addcontact_contectemail = view.findViewById(R.id.textinput_addcontact_contectemail);
        textinput_addcontact_contectrelation = view.findViewById(R.id.textinput_addcontact_contectrelation);

        edittext_addcontact_contactname = view.findViewById(R.id.edittext_addcontact_contactname);
        edittext_addcontact_contactphone = view.findViewById(R.id.edittext_addcontact_contactphone);
        edittext_addcontact_contectemail = view.findViewById(R.id.edittext_addcontact_contectemail);
        edittext_addcontact_contectrelation = view.findViewById(R.id.edittext_addcontact_contectrelation);

        button_addcontact_add = view.findViewById(R.id.button_addcontact_add);
        button_addcontact_contactlist = view.findViewById(R.id.button_addcontact_contactlist);
        imageview_contact = view.findViewById(R.id.imageview_contact);

        button_addcontact_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doValidate()) {

                    emergencyContact.CustomerId = user.Id;
                    emergencyContact.ContactName = edittext_addcontact_contactname.getText().toString().trim();
                    emergencyContact.ContactPhone = textview_countrycode.getText().toString().trim() + edittext_addcontact_contactphone.getText().toString().trim();
                    emergencyContact.ContactEmail = edittext_addcontact_contectemail.getText().toString().trim();
                    emergencyContact.Relation = edittext_addcontact_contectrelation.getText().toString().trim();

                    if (emergencyContact.ContactPhone.equals(user.Mobile)) {
                        showMessage("You can not add your number as emergency contact.");
                        return;
                    }

                    AddEmergencyContact();
                }
            }
        });

        button_addcontact_contactlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ShowContactListFragment(), null);
            }
        });

        imageview_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnableRuntimePermission();
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 7);
            }
        });

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

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getActivity(), "Permission Granted, Now your application can access CONTACTS.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(getActivity(), "Permission Canceled, Now your application cannot access CONTACTS.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

    @Override
    public void onActivityResult(int RequestCode, int ResultCode, Intent ResultIntent) {

        super.onActivityResult(RequestCode, ResultCode, ResultIntent);

        switch (RequestCode) {

            case (7):
                if (ResultCode == Activity.RESULT_OK) {

                    Uri uri;
                    Cursor cursor1, cursor2;
                    String TempNameHolder, TempNumberHolder, TempContactID, IDresult = "";
                    int IDresultHolder;


                    String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '"
                            + ("1") + "'";
                    String sortOrder = ContactsContract.Contacts.DISPLAY_NAME
                            + " COLLATE LOCALIZED ASC";
                   /* cur = context.getContentResolver().query(
                            ContactsContract.Contacts.CONTENT_URI, projection, selection
                                    + " AND " + ContactsContract.Contacts.HAS_PHONE_NUMBER
                                    + "=1", null, sortOrder);// this query only return contacts which had phone number and not duplicated*/


                    uri = ResultIntent.getData();

                    cursor1 = getActivity().getContentResolver().query(uri, null, selection
                            + " AND " + ContactsContract.Contacts.HAS_PHONE_NUMBER
                            + "=1", null, sortOrder);

                    if (cursor1.moveToFirst()) {

                        TempNameHolder = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                        TempContactID = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID));

                        IDresult = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        IDresultHolder = Integer.valueOf(IDresult);

                        if (IDresultHolder == 1) {

                            cursor2 = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + TempContactID, null, null);

                            while (cursor2.moveToNext()) {

                                TempNumberHolder = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                TempNumberHolder = TempNumberHolder.replaceAll("[^a-zA-Z0-9]", "");
                                if (TempNumberHolder.startsWith("231")){
                                    TempNumberHolder = TempNumberHolder.replaceFirst("231","");
                                }
                                //name.setText(TempNameHolder);
                                //number.setText(TempNumberHolder);

                                //Toast.makeText(getContext(), "Name : " + TempNameHolder +"/n"+ "Number : " + TempNumberHolder, Toast.LENGTH_SHORT).show();
                                edittext_addcontact_contactname.setText(TempNameHolder);
                                edittext_addcontact_contactphone.setText(TempNumberHolder);
                            }
                        }

                    }
                }
                break;
        }
    }

    private void AddEmergencyContact() {
        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().EmergencycontactAdd(emergencyContact).enqueue(new Callback<Response<EmergencyContact>>() {
            @Override
            public void onResponse(Call<Response<EmergencyContact>> call, retrofit2.Response<Response<EmergencyContact>> response) {
                if (response.code() == Constants.SuccessCode) {
                    if (response.body().Item != null) {
                        Toast.makeText(getContext(), "EmergencyContcat Add Successfully", Toast.LENGTH_SHORT).show();
                        replaceFragment(new EmergencyContactFragment(), EmergencyContactFragment.class.getName());
                    } else {
                        showMessage(response.body().Message);
                    }
                } else if (response.code() == Constants.InternalServerError) {
                    showMessage(Constants.DefaultErrorMessage);
                } else {
                    showMessage(Constants.DefaultErrorMessage);
                }
                LoadingDialog.cancelLoading();
            }

            @Override
            public void onFailure(Call<Response<EmergencyContact>> call, Throwable t) {
                LoadingDialog.cancelLoading();
                Log.e("ContactNotAdded : ", t.getLocalizedMessage());
                showMessage(Constants.DefaultErrorMessage);
            }
        });
    }


    private boolean doValidate() {

        if (edittext_addcontact_contactname.getText().toString().trim().isEmpty()) {
            textinput_addcontact_contactname.setError("Please enter name");
            edittext_addcontact_contactname.requestFocus();
            return false;
        } else {
            edittext_addcontact_contactname.clearFocus();
            textinput_addcontact_contactname.setErrorEnabled(false);
        }

        if (edittext_addcontact_contactphone.getText().toString().trim().isEmpty()) {
            textinput_addcontact_contactphone.setError("Please enter mobile number");
            edittext_addcontact_contactphone.requestFocus();
            return false;
        } else {
            edittext_addcontact_contactphone.clearFocus();
            textinput_addcontact_contactphone.setErrorEnabled(false);
        }

        return true;
    }
}
