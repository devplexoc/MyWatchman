package com.plexoc.mywatchman.Fragment;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.plexoc.mywatchman.Adapter.RaisedSOSAdpter;
import com.plexoc.mywatchman.Model.ListResponse;
import com.plexoc.mywatchman.Model.RaisedSOSUser;
import com.plexoc.mywatchman.R;
import com.plexoc.mywatchman.Utils.Constants;
import com.plexoc.mywatchman.Utils.LoadingDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SOSHistoryFragment extends BaseFragment {

    private Toolbar toolbar;
    private AppCompatTextView textview_norecordfound;
    private RecyclerView recyclerViewRaisedSOS;
    private RaisedSOSAdpter raisedSOSAdpter;

    private SwipeRefreshLayout swipeRefreshLayout;

    private int raisedSOSID;

    public SOSHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_s_o_s_history, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("SOS History");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());

        textview_norecordfound = view.findViewById(R.id.textview_norecordfound);
        recyclerViewRaisedSOS = view.findViewById(R.id.recyclerview_raised_sos);

        swipeRefreshLayout = view.findViewById(R.id.swipetorefresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            getSOSHistory();
            swipeRefreshLayout.setRefreshing(false);
        });

        getSOSHistory();

        return view;
    }

    private void getSOSHistory() {
        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().getSOSHistory(user.Id,0, 100000).enqueue(new Callback<ListResponse<RaisedSOSUser>>() {
            @Override
            public void onResponse(Call<ListResponse<RaisedSOSUser>> call, Response<ListResponse<RaisedSOSUser>> response) {
                if (response.code() == 200) {
                    if (!response.body().Values.isEmpty()) {

                        raisedSOSAdpter = new RaisedSOSAdpter(response.body().Values, new RaisedSOSAdpter.RaisedSOSCallback() {
                            @Override
                            public void getRaisedSOSId(RaisedSOSUser raisedSOSUser) {
                                raisedSOSID = raisedSOSUser.Id;
                                replaceFragment(new RaisedSOSDetailsFragment(raisedSOSID),null);
                            }
                        });
                        recyclerViewRaisedSOS.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerViewRaisedSOS.setItemAnimator(new DefaultItemAnimator());
                        recyclerViewRaisedSOS.setAdapter(raisedSOSAdpter);

                        textview_norecordfound.setVisibility(View.GONE);
                    } else {
                        textview_norecordfound.setVisibility(View.VISIBLE);
                    }
                } else if (response.code() == Constants.InternalServerError) {
                    showMessage(Constants.DefaultErrorMessage);
                } else {
                    showMessage(Constants.DefaultErrorMessage);
                }
                LoadingDialog.cancelLoading();
            }

            @Override
            public void onFailure(Call<ListResponse<RaisedSOSUser>> call, Throwable t) {
                LoadingDialog.cancelLoading();
                Log.e("RaisedSOS Erro : ", t.getLocalizedMessage());
            }
        });
    }
}
