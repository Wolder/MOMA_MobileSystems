package com.example.moma_mobilesystems.Views.Fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moma_mobilesystems.Models.ApplicationModel;
import com.example.moma_mobilesystems.R;
import com.example.moma_mobilesystems.ViewModels.ApplicationViewModel;
import com.example.moma_mobilesystems.Views.ApplicationsRecyclerViewAdapter;

public class AppsFragment extends Fragment {

    private ApplicationsRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private final String TAG = "AppsFragment: ";



    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_apps, container, false);
        final ApplicationViewModel model = ViewModelProviders.of(this).get(ApplicationViewModel.class);
        ApplicationModel[] models = model.getApplicationList(root.getContext()).getValue();

        recyclerView = root.findViewById(R.id.applicationList);
        initRecyclerView(models, root.getContext());

        return root;
    }

    public void initRecyclerView(ApplicationModel[] applicationModels, Context context) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ApplicationsRecyclerViewAdapter(context, applicationModels);
        recyclerView.setAdapter(adapter);
    }

}
