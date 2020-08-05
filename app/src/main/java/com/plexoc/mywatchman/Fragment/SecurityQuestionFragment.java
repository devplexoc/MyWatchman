package com.plexoc.mywatchman.Fragment;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.plexoc.mywatchman.Model.Error;
import com.plexoc.mywatchman.Model.ListResponse;
import com.plexoc.mywatchman.Model.SecurityQuestion;
import com.plexoc.mywatchman.Model.User;
import com.plexoc.mywatchman.R;
import com.plexoc.mywatchman.Utils.Constants;
import com.plexoc.mywatchman.Utils.LoadingDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SecurityQuestionFragment extends BaseFragment {

    private User user;

    public SecurityQuestionFragment(User user) {
        this.user = user;
    }

    private AppCompatSpinner spinnerQuestions;
    private List<SecurityQuestion> securityQuestionList;
    private List<String> questions = new ArrayList<>();
    private TextInputLayout inputLayoutAnswer;
    private TextInputEditText editTextAnswer;
    private int QuestionId;
    private MaterialButton buttonSubmit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_security_question, container, false);

        spinnerQuestions = view.findViewById(R.id.spinner_question);
        inputLayoutAnswer = view.findViewById(R.id.textinput_answer);
        editTextAnswer = view.findViewById(R.id.edittext_answer);
        buttonSubmit = view.findViewById(R.id.button_submit);
        getAllQuestions();


        buttonSubmit.setOnClickListener(view1 -> {

            if (editTextAnswer.getText().toString().trim().isEmpty()) {
                inputLayoutAnswer.setError("Please enter answer");
                editTextAnswer.requestFocus();
            } else {
                closeKeybord();
                editTextAnswer.clearFocus();
                inputLayoutAnswer.setEnabled(false);

                //updateAnswer(editTextAnswer.getText().toString().trim());
                user.SecurityQuestionId = QuestionId;
                user.Answer = editTextAnswer.getText().toString().trim();
                CallSignupApi();
            }

        });


        return view;
    }

    private void updateAnswer(String Answer) {

        SecurityQuestion securityQuestion = new SecurityQuestion();
        securityQuestion.CustomerId = user.Id;
        securityQuestion.SecurityQuestionId = QuestionId;
        securityQuestion.Answer = Answer;

        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());

    }

    private void getAllQuestions() {

        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().getAllSecurityQuestion(0, 10000).enqueue(new Callback<ListResponse<SecurityQuestion>>() {
            @Override
            public void onResponse(Call<ListResponse<SecurityQuestion>> call, Response<ListResponse<SecurityQuestion>> response) {
                if (response.code() == Constants.SuccessCode) {
                    if (!response.body().Values.isEmpty()) {

                        securityQuestionList = response.body().Values;

                        for (int i = 0; i < securityQuestionList.size(); i++) {
                            questions.add(securityQuestionList.get(i).Questions);
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, questions);
                        spinnerQuestions.setAdapter(arrayAdapter);


                        spinnerQuestions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                QuestionId = securityQuestionList.get(i).Id;
                                Log.e("QuestionId", QuestionId + "");
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

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
            public void onFailure(Call<ListResponse<SecurityQuestion>> call, Throwable t) {
                LoadingDialog.cancelLoading();
            }
        });
    }

    private void CallSignupApi() {
        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().SignUp(user).enqueue(new Callback<com.plexoc.mywatchman.Model.Response<User>>() {
            @Override
            public void onResponse(Call<com.plexoc.mywatchman.Model.Response<User>> call, retrofit2.Response<com.plexoc.mywatchman.Model.Response<User>> response) {
                if (response.code() == Constants.SuccessCode) {
                    if (response.body().Item != null) {

                        closeKeybord();

                        replaceFragment(new PlansFragment(response.body().Item), null);

                        //replaceFragment(new OTPConfirmFragment(response.body().Item),null);

                        /*Intent intent = new Intent(getActivity(), HomeActivity.class);
                        startActivity(intent);*/
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
            public void onFailure(Call<com.plexoc.mywatchman.Model.Response<User>> call, Throwable t) {
                LoadingDialog.cancelLoading();
                Log.e("Signup Fail", t.getLocalizedMessage());
                showMessage(Constants.DefaultErrorMessage);
            }
        });
    }
}