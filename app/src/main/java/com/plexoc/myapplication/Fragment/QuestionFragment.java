package com.plexoc.myapplication.Fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.plexoc.myapplication.Adapter.AddressAdpter;
import com.plexoc.myapplication.Adapter.OptionsAdpter;
import com.plexoc.myapplication.Model.Address;
import com.plexoc.myapplication.Model.OptionsModel;
import com.plexoc.myapplication.Model.QuestionAnswer;
import com.plexoc.myapplication.Model.QuestionAnswerBody;
import com.plexoc.myapplication.Model.QuestionModel;
import com.plexoc.myapplication.Model.Rating;
import com.plexoc.myapplication.Model.Response;
import com.plexoc.myapplication.R;
import com.plexoc.myapplication.Utils.Constants;
import com.plexoc.myapplication.Utils.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionFragment extends BaseFragment {


    public QuestionFragment(List<QuestionModel> questionModelList, int SOSID) {
        this.questionModelList = questionModelList;
        this.SOSID = SOSID;
    }

    private int count = 0;
    private int SOSID;
    private Toolbar toolbar;
    private AppCompatTextView textview_question_number, textview_question;
    private RecyclerView recyclerView_options;

    private List<QuestionModel> questionModelList;
    private MaterialButton btnNext, btn_attachfile;
    private AppCompatTextView textview_filepath;
    private AppCompatTextView textview_rating;
    private AppCompatRatingBar ratingBar;
    private TextInputLayout inputLayoutNote;
    private EditText editTextNote;
    private OptionsModel optionsModel;
    private boolean selected = false;
    private QuestionAnswerBody questionAnswerBody;

    private ArrayList<QuestionAnswer> questionAnswerList = new ArrayList<>();

    private AlertDialog.Builder builder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question, container, false);

        textview_question_number = view.findViewById(R.id.textview_question_number);
        textview_question = view.findViewById(R.id.textview_question);

        recyclerView_options = view.findViewById(R.id.recyclerView_options);

        textview_rating = view.findViewById(R.id.textview_your_rating);
        ratingBar = view.findViewById(R.id.roaming_staff_rating);

        inputLayoutNote = view.findViewById(R.id.inputlayout_note);
        editTextNote = view.findViewById(R.id.edittext_note);

        btn_attachfile = view.findViewById(R.id.btn_attachfile);
        textview_filepath = view.findViewById(R.id.textview_filepath);

        builder = new AlertDialog.Builder(getContext());


        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Questions");
        toolbar.setNavigationIcon(R.drawable.ic_backarrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        setData();

        btnNext = view.findViewById(R.id.btn_next);
        btnNext.setOnClickListener(v -> {
            if (selected) {
                if (!questionModelList.get(count).hasNextQuestion) {
                    if (ratingBar.getRating() != 0) {

                        builder.setMessage("By submitting, I confirm that I have answered all questions truthfully and completely. " +
                                "Both caller and responder(s) are no longer in danger or in need of immediate assistance at the time of " +
                                "this documentation.")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        questionAnswerBody = new QuestionAnswerBody(questionAnswerList);
                                        AddQuestionAnswer();
                                        insertRating();
                                        //replaceFragment(new SuccessfullySOSCompleteFragment(SOSID), null);
                                        //getActivity().finish();
                                    }
                                });

                        AlertDialog alert = builder.create();
                        //Setting the title manually
                        alert.setTitle("Message");
                        alert.show();
                    } else {
                        showMessage("Please give rating");
                    }

                } else {
                    QuestionAnswer questionAnswer = new QuestionAnswer(questionModelList.get(count).question, optionsModel.optionText);
                    questionAnswerList.add(questionAnswer);
                    count++;
                    setData();
                }
            } else {
                Toast.makeText(getContext(), "Please Select Any Answer", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void AddQuestionAnswer() {
        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().AddQuestionAnswer(user.Id, SOSID, editTextNote.getText().toString().trim(), questionAnswerList).enqueue(new Callback<Response<QuestionAnswer>>() {
            @Override
            public void onResponse(Call<Response<QuestionAnswer>> call, retrofit2.Response<Response<QuestionAnswer>> response) {
                if (response.code() == Constants.SuccessCode) {
                    assert response.body() != null;
                    if (response.body().Item != null) {
                        replaceFragment(new SuccessfullySOSCompleteFragment(SOSID), null);
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
            public void onFailure(Call<Response<QuestionAnswer>> call, Throwable t) {
                LoadingDialog.cancelLoading();
                Log.e("QuestionAnswernotAdd : ", t.getLocalizedMessage());
                showMessage(Constants.DefaultErrorMessage);
            }
        });
    }

    private void setData() {

        textview_question.setText(questionModelList.get(count).question);

        if (!questionModelList.get(count).hasNextQuestion) {
            btnNext.setText("Submit");
        }
        if (questionModelList.get(count).isOptional) {
            selected = true;
        } else {
            selected = false;
        }
        if (questionModelList.get(count).isNoteRequired) {
            inputLayoutNote.setVisibility(View.VISIBLE);
        } else {
            inputLayoutNote.setVisibility(View.GONE);
        }

        if (!questionModelList.get(count).hasOptions) {
            inputLayoutNote.setVisibility(View.VISIBLE);
            ratingBar.setVisibility(View.VISIBLE);
            textview_rating.setVisibility(View.VISIBLE);
            btn_attachfile.setVisibility(View.GONE);
            textview_filepath.setVisibility(View.GONE);
            recyclerView_options.setVisibility(View.GONE);
            textview_question_number.setText("");

        } else {

            textview_question_number.setText("Q:" + (count + 1));
            OptionsAdpter optionsAdpter = new OptionsAdpter(getContext(), questionModelList.get(count).optionsModelList, optionsModel -> {
                selected = true;
                this.optionsModel = optionsModel;
                if (!questionModelList.get(count).isNoteRequired) {
                    if (optionsModel.isNoteRequired) {
                        if (optionsModel.noteType == Constants.TEXTNOTE) {
                            inputLayoutNote.setVisibility(View.VISIBLE);
                            btn_attachfile.setVisibility(View.GONE);
                            textview_filepath.setVisibility(View.GONE);
                        } else if (optionsModel.noteType == Constants.IMAGEORVIDEONOTE) {
                            inputLayoutNote.setVisibility(View.GONE);
                            btn_attachfile.setVisibility(View.VISIBLE);
                            textview_filepath.setVisibility(View.VISIBLE);
                        } else {
                            inputLayoutNote.setVisibility(View.VISIBLE);
                            btn_attachfile.setVisibility(View.GONE);
                            textview_filepath.setVisibility(View.GONE);
                        }
                    } else {
                        inputLayoutNote.setVisibility(View.GONE);
                        btn_attachfile.setVisibility(View.GONE);
                        textview_filepath.setVisibility(View.GONE);
                    }
                }

            });
            recyclerView_options.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView_options.setAdapter(optionsAdpter);
        }
    }

    private void insertRating() {

        Rating rating = new Rating();
        rating.CustomerId = user.Id;
        rating.SOSId = SOSID;
        rating.Rating = String.valueOf(ratingBar.getRating());

        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().insertRating(rating).enqueue(new Callback<Response<Rating>>() {
            @Override
            public void onResponse(Call<Response<Rating>> call, retrofit2.Response<Response<Rating>> response) {
                if (response.isSuccessful()) {

                }
                LoadingDialog.cancelLoading();
            }

            @Override
            public void onFailure(Call<Response<Rating>> call, Throwable t) {
                LoadingDialog.cancelLoading();
            }
        });
    }

}
