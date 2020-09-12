package com.plexoc.mywatchman.Fragment;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plexoc.mywatchman.Adapter.StatisticsAdpter;
import com.plexoc.mywatchman.Model.ListResponse;
import com.plexoc.mywatchman.Model.SosType;
import com.plexoc.mywatchman.R;
import com.plexoc.mywatchman.Utils.Constants;
import com.plexoc.mywatchman.Utils.DrawerUtil;
import com.plexoc.mywatchman.Utils.LoadingDialog;

import retrofit2.Call;
import retrofit2.Callback;

public class StatisticsFragment extends BaseFragment {


    public StatisticsFragment() {
        // Required empty public constructor
    }

    private RecyclerView recyclerStatistics;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Statistics");
        toolbar.setNavigationIcon(R.drawable.ic_menu);

        DrawerUtil.getDrawer(getActivity(), toolbar, user);

        recyclerStatistics = view.findViewById(R.id.recyclerview_statistics);
        getAllSOSType();
        return view;
    }


    private void getAllSOSType() {

        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().getSOSCountBuCustomerId(user.Id).enqueue(new Callback<ListResponse<SosType>>() {
            @Override
            public void onResponse(Call<ListResponse<SosType>> call, retrofit2.Response<ListResponse<SosType>> response) {
                if (response.isSuccessful()) {
                    if (!response.body().Values.isEmpty()) {

                        StatisticsAdpter statisticsAdpter = new StatisticsAdpter(response.body().Values);
                        recyclerStatistics.setLayoutManager(new GridLayoutManager(getContext(), 2));
                        recyclerStatistics.setAdapter(statisticsAdpter);

                        //Log.d("Response","Success");
                    } else
                        showMessage("No Record Found");
                } else
                    showMessage(Constants.DefaultErrorMessage);
                LoadingDialog.cancelLoading();
            }

            @Override
            public void onFailure(Call<ListResponse<SosType>> call, Throwable t) {
                showMessage(Constants.DefaultErrorMessage);
                LoadingDialog.cancelLoading();
            }
        });
    }


}