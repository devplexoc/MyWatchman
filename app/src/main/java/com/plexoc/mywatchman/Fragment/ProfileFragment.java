package com.plexoc.mywatchman.Fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.plexoc.mywatchman.Model.Error;
import com.plexoc.mywatchman.Model.Response;
import com.plexoc.mywatchman.Model.SecurityQuestion;
import com.plexoc.mywatchman.Model.User;
import com.plexoc.mywatchman.R;
import com.plexoc.mywatchman.Utils.Constants;
import com.plexoc.mywatchman.Utils.DrawerUtil;
import com.plexoc.mywatchman.Utils.LoadingDialog;
import com.plexoc.mywatchman.Utils.Prefs;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends BaseFragment {


    public ProfileFragment() {
        // Required empty public constructor
    }

    private Toolbar toolbar;
    private TextInputLayout textinput_profile_firstname, textinput_lastname, textinput_email, textinput_mobilenumber,
            textinput_password, textinput_username;
    private TextInputEditText edittext_profile_firstname, edittext_lastname, edittext_email, edittext_mobilenumber,
            edittext_password, edittext_username;
    private MaterialButton button_update;
    private AppCompatTextView textview_username, textview_email;
    private AppCompatImageView imageView_profile_image;
    private MultipartBody.Part part;
    private AlertDialog.Builder builder;
    private View dialogview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        dialogview = inflater.inflate(R.layout.dialog_security_question, container, false);

        textview_username = view.findViewById(R.id.textview_username);
        textview_email = view.findViewById(R.id.textview_email);

        imageView_profile_image = view.findViewById(R.id.imageview_profile);

        textinput_profile_firstname = view.findViewById(R.id.textinput_profile_firstname);
        textinput_lastname = view.findViewById(R.id.textinput_lastname);
        textinput_email = view.findViewById(R.id.textinput_email);
        textinput_mobilenumber = view.findViewById(R.id.textinput_mobilenumber);
        textinput_password = view.findViewById(R.id.textinput_password);
        textinput_username = view.findViewById(R.id.textinput_username);

        edittext_profile_firstname = view.findViewById(R.id.edittext_profile_firstname);
        edittext_lastname = view.findViewById(R.id.edittext_lastname);
        edittext_email = view.findViewById(R.id.edittext_email);
        edittext_mobilenumber = view.findViewById(R.id.edittext_mobilenumber);
        edittext_password = view.findViewById(R.id.edittext_password);
        edittext_username = view.findViewById(R.id.edittext_username);

        textinput_mobilenumber.setBoxStrokeColor(getResources().getColor(R.color.colorPrimaryDark));
        textinput_email.setBoxStrokeColor(getResources().getColor(R.color.colorPrimaryDark));

        button_update = view.findViewById(R.id.button_update);

        getCustomer();

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Profile");
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        DrawerUtil.getDrawer(getActivity(), toolbar, user);


        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doValidate()) {

                    user.FirstName = edittext_profile_firstname.getText().toString().trim();
                    user.LastName = edittext_lastname.getText().toString().trim();
                    user.UserName = edittext_username.getText().toString().trim();
                    if (!edittext_email.getText().toString().trim().isEmpty())
                        user.Email = edittext_email.getText().toString().trim();
                    //user.Mobile = edittext_mobilenumber.getText().toString().trim();
                    if (!user.Mobile.equals(edittext_mobilenumber.getText().toString().trim())) {
                        openDialog();
                    } else {
                        CallUpdateApi();
                    }
                }
            }
        });


        imageView_profile_image.setOnClickListener(view1 -> {
            ImagePicker.create(ProfileFragment.this).returnMode(ReturnMode.ALL)
                    .folderMode(true)
                    .showCamera(true)
                    .includeVideo(true)
                    .toolbarFolderTitle("Folder")
                    .toolbarImageTitle("Tap to select")
                    .toolbarArrowColor(Color.WHITE)
                    .single()
                    .start();
        });
        return view;
    }

    private void openDialog() {
        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().getSecurityQuestionAnswerByUserId(user.Id).enqueue(new Callback<Response<SecurityQuestion>>() {
            @Override
            public void onResponse(Call<Response<SecurityQuestion>> call, retrofit2.Response<Response<SecurityQuestion>> response) {
                if (response.code() == Constants.SuccessCode) {
                    if (response.body().Item != null) {

                        builder = new AlertDialog.Builder(getContext());
                        builder.setView(dialogview);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        alertDialog.setOnDismissListener(dialogInterface -> {
                            ((ViewGroup) dialogview.getParent()).removeView(dialogview);
                        });

                        TextInputLayout inputLayoutAnswer = dialogview.findViewById(R.id.textinput_answer);
                        TextInputEditText editTextAnswer = dialogview.findViewById(R.id.edittext_answer);
                        AppCompatTextView appCompatTextView = dialogview.findViewById(R.id.textview_question_value);
                        MaterialButton buttonSubmit = dialogview.findViewById(R.id.button_submit);

                        appCompatTextView.setText(response.body().Item.Questions);

                        buttonSubmit.setOnClickListener(view -> {
                            if (editTextAnswer.getText().toString().trim().isEmpty()) {
                                inputLayoutAnswer.setError("Please enter answer");
                                editTextAnswer.requestFocus();
                            } else if (!editTextAnswer.getText().toString().trim().equals(response.body().Item.Answer)) {
                                inputLayoutAnswer.setError("Answer is not correct");
                                editTextAnswer.requestFocus();
                            } else {
                                closeKeybord();
                                editTextAnswer.clearFocus();
                                inputLayoutAnswer.setEnabled(false);
                                alertDialog.dismiss();

                                CheckOTP();
                            }
                        });

                    } else
                        showMessage(response.body().Message);
                } else if (response.code() == Constants.InternalServerError) {
                    showMessage(Constants.DefaultErrorMessage);
                } else {
                    try {
                        Error error = new Gson().fromJson(response.errorBody().string(), Error.class);
                        showMessage(error.Message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                LoadingDialog.cancelLoading();
            }

            @Override
            public void onFailure(Call<Response<SecurityQuestion>> call, Throwable t) {
                LoadingDialog.cancelLoading();
            }
        });
    }

    private void CallUpdateApi() {
        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().SignUp(user).enqueue(new Callback<Response<User>>() {
            @Override
            public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
                if (response.code() == Constants.SuccessCode) {
                    if (response.body().Item != null) {

                        Prefs.putString(Prefs.USER, new Gson().toJson(response.body().Item));
                        user = new Gson().fromJson(Prefs.getString(Prefs.USER), User.class);

                        Toast.makeText(getActivity(), "Profile Update Successfully", Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    } else {
                        showMessage(response.body().Message);
                    }
                } else if (response.code() == Constants.InternalServerError) {
                    showMessage(Constants.DefaultErrorMessage);
                } else {
                    try {
                        Error error = new Gson().fromJson(response.errorBody().string(), Error.class);
                        showMessage(error.Message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                LoadingDialog.cancelLoading();
            }

            @Override
            public void onFailure(Call<Response<User>> call, Throwable t) {
                LoadingDialog.cancelLoading();
                Log.e("Update Error", t.getLocalizedMessage());
            }
        });
    }

    private boolean doValidate() {
        if (edittext_profile_firstname.getText().toString().trim().isEmpty()) {
            textinput_profile_firstname.setError("Please enter first name");
            edittext_profile_firstname.requestFocus();
            return false;
        } else {
            edittext_profile_firstname.clearFocus();
            textinput_profile_firstname.setErrorEnabled(false);
        }

        if (edittext_lastname.getText().toString().trim().isEmpty()) {
            textinput_lastname.setError("Please enter last name");
            edittext_lastname.requestFocus();
            return false;
        } else {
            edittext_lastname.clearFocus();
            textinput_lastname.setErrorEnabled(false);
        }

        if (edittext_username.getText().toString().trim().isEmpty()) {
            textinput_username.setError("Please enter Username");
            edittext_username.requestFocus();
            return false;
        } else {
            edittext_username.clearFocus();
            textinput_username.setErrorEnabled(false);
        }

       /* if (edittext_email.getText().toString().trim().isEmpty()) {
            textinput_email.setError("Please enter email");
            edittext_email.requestFocus();
            return false;
        } else {
            edittext_email.clearFocus();
            textinput_email.setErrorEnabled(false);
        }*/

        if (!edittext_email.getText().toString().trim().isEmpty()) {
            if (!isValidEmail(edittext_email.getText().toString().trim())) {
                textinput_email.setError("Please enter valid email address");
                edittext_email.requestFocus();
                return false;
            } else {
                edittext_email.clearFocus();
                textinput_email.setErrorEnabled(false);
            }
        } else {
            edittext_email.clearFocus();
            textinput_email.setErrorEnabled(false);
        }

        if (edittext_mobilenumber.getText().toString().trim().isEmpty()) {
            textinput_mobilenumber.setError("Please enter mobile number");
            edittext_mobilenumber.requestFocus();
            return false;
        } else {
            edittext_mobilenumber.clearFocus();
            textinput_mobilenumber.setErrorEnabled(false);
        }

        return true;
    }


    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            /*Bitmap myBitmap = BitmapFactory.decodeFile(ImagePicker.getFirstImageOrNull(data).getPath());
            imageView_profile_update.setImageBitmap(myBitmap);*/

            File file1 = new File(ImagePicker.getFirstImageOrNull(data).getPath());
            RequestBody requestBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
            part = MultipartBody.Part.createFormData("", file1.getName(), requestBody1);

            Log.e("Image Path : ", ImagePicker.getFirstImageOrNull(data).getPath());
            Glide.with(getContext()).load(ImagePicker.getFirstImageOrNull(data).getPath()).circleCrop().into(imageView_profile_image);
            updateProfileImage();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateProfileImage() {
        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().updateProfilePicture(user.Id, part).enqueue(new Callback<Response<User>>() {
            @Override
            public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
                if (response.code() == Constants.SuccessCode) {
                    if (response.body().Item != null) {

                        Prefs.putString(Prefs.USER, new Gson().toJson(response.body().Item));
                        user = new Gson().fromJson(Prefs.getString(Prefs.USER), User.class);

                        Toast.makeText(getActivity(), "Profile Picture Updated Successfully", Toast.LENGTH_SHORT).show();
                        //getActivity().onBackPressed();
                    } else {
                        showMessage(response.body().Message);
                    }
                } else if (response.code() == Constants.InternalServerError) {
                    showMessage(Constants.DefaultErrorMessage);
                } else {
                    try {
                        Error error = new Gson().fromJson(response.errorBody().string(), Error.class);
                        showMessage(error.Message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                LoadingDialog.cancelLoading();

            }

            @Override
            public void onFailure(Call<Response<User>> call, Throwable t) {
                LoadingDialog.cancelLoading();
                Log.e("ProfilePicUpdate Error", t.getLocalizedMessage());
            }
        });

    }


    private void CheckOTP() {
        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().Checkuser("+231" + edittext_mobilenumber.getText().toString().trim(),
                null,
                null).enqueue(new Callback<Response<User>>() {
            @Override
            public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
                if (response.isSuccessful()) {
                    closeKeybord();
                    if (response.body().Code == 200) {
                        user.Otp = response.body().Item.Otp;
                       // Toast.makeText(getContext(), "Your OTP is : " + user.Otp, Toast.LENGTH_LONG).show();
                        replaceFragment(new MobileChangeOTPVerifyFragment(user, edittext_mobilenumber.getText().toString().trim()), null);
                    } else {
                        showMessage(response.body().Message);
                    }
                } else if (response.code() == Constants.InternalServerError) {
                    showMessage(Constants.DefaultErrorMessage);
                } else {
                    try {
                        Error error = new Gson().fromJson(response.errorBody().string(), Error.class);
                        showMessage(error.Message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                LoadingDialog.cancelLoading();
            }

            @Override
            public void onFailure(Call<Response<User>> call, Throwable t) {

            }
        });
    }


    private void getCustomer() {
        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().getCustomerByid(user.Id).enqueue(new Callback<com.plexoc.mywatchman.Model.Response<User>>() {
            @Override
            public void onResponse(Call<com.plexoc.mywatchman.Model.Response<User>> call, retrofit2.Response<Response<User>> response) {
                if (response.isSuccessful()) {
                    if (response.body().Item != null) {

                        Prefs.putString(Prefs.USER, new Gson().toJson(response.body().Item));
                        user = new Gson().fromJson(Prefs.getString(Prefs.USER), User.class);

                        setData();

                    }
                }
                LoadingDialog.cancelLoading();
            }

            @Override
            public void onFailure(Call<com.plexoc.mywatchman.Model.Response<User>> call, Throwable t) {
                LoadingDialog.cancelLoading();
                Log.e("User Fail", t.getLocalizedMessage());
            }
        });
    }

    private void setData(){

        if (user.FirstName != null) {
            edittext_profile_firstname.setText(user.FirstName);
        }
        if (user.LastName != null) {
            edittext_lastname.setText(user.LastName);
        }
        if (user.Email != null) {
            edittext_email.setText(user.Email);
        }
        if (user.Mobile != null) {
            if (user.Mobile.startsWith("+231")) {
                user.Mobile = user.Mobile.replaceFirst("\\+231", "");
            }
            edittext_mobilenumber.setText(user.Mobile);
        }
        if (user.Password != null) {
            edittext_password.setText(user.Password);
        }
        if (user.UserName != null) {
            edittext_username.setText(user.UserName);
        }

        if (user.ProfilePicture != null) {
            Glide.with(getContext())
                    .load(user.ProfilePicture)
                    .placeholder(R.drawable.ic_user_colorful)
                    .error(R.drawable.ic_user_colorful)
                    .circleCrop()
                    .into(imageView_profile_image);
        }

        if (user.UserType == 2) {
            textinput_username.setVisibility(View.GONE);
            textinput_email.setVisibility(View.GONE);
            edittext_username.setVisibility(View.GONE);
            edittext_email.setVisibility(View.GONE);
            textview_username.setVisibility(View.GONE);
            textview_email.setVisibility(View.GONE);
        }

    }

}
