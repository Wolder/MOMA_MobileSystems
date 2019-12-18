package com.example.moma_mobilesystems.Views.Fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moma_mobilesystems.Models.ApplicationModel;
import com.example.moma_mobilesystems.R;
import com.example.moma_mobilesystems.ViewModels.ApplicationViewModel;
import com.example.moma_mobilesystems.Views.ChartsRecyclerViewAdapter;
import com.example.moma_mobilesystems.Views.CheckForNetworkChangesAsync;

import java.util.ArrayList;
import java.util.List;

public class ChartFragment extends Fragment {

    private ChartsRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private final String TAG = "ChartFragment: ";
    private ApplicationModel[] am;


    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_chart, container, false);

        recyclerView = root.findViewById(R.id.chartList);

        final ApplicationViewModel model = ViewModelProviders.of(this).get(ApplicationViewModel.class);
        model.getApplicationList(root.getContext()).observe(this, new Observer<ApplicationModel[]>() {
            @Override
            public void onChanged(ApplicationModel[] applicationModels) {
                // update UI based on new list
                new CheckForNetworkChangesAsync(applicationModels).execute();
                //updateUI(applicationModels);
                new GetNetworkAsync().execute();
                initRecyclerView(applicationModels, root.getContext());
                am = applicationModels;
            }

            // AsyncTask for updating
            class GetNetworkAsync extends AsyncTask<Void, Void, Boolean> {
                @Override
                protected Boolean doInBackground(Void... voids) {

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                protected void onPostExecute(Boolean aBoolean) {
                    super.onPostExecute(aBoolean);
                    model.getApplicationList(root.getContext()).getValue();
                }
            }

        });
        return root;
    }

    public void initRecyclerView(ApplicationModel[] applicationModels, Context context) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        List<ApplicationModel> newList = new ArrayList<>();
        for (ApplicationModel applicationModel : applicationModels) {
            if (applicationModel != null) {
                if (applicationModel.isVisable()) {
                    newList.add(applicationModel);
                }
            }
        }

        // Convert ArrayList to ApplicationModel[]
        ApplicationModel[] newModelsList = new ApplicationModel[newList.size()];
        for (int i = 0; i < newList.size(); i++) {
            newModelsList[i] = newList.get(i);
        }

        adapter = new ChartsRecyclerViewAdapter(context, newModelsList);
        recyclerView.setAdapter(adapter);
    }

    public void updateRecyclerView(ApplicationModel[] applicationModels, Context context) {
        ChartsRecyclerViewAdapter newAdapter = new ChartsRecyclerViewAdapter(context, applicationModels);
        recyclerView.swapAdapter(newAdapter, false);
    }

}
