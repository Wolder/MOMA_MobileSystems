package com.example.moma_mobilesystems.Views;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.example.moma_mobilesystems.Models.ApplicationModel;

import java.util.List;

public class CheckForNetworkChangesAsync extends AsyncTask<Void, Void, Boolean> {
    public final String TAG = "NetworkAsync: ";

    private ApplicationModel[] models;

    public CheckForNetworkChangesAsync(ApplicationModel[] models) {
        this.models = models;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected Boolean doInBackground(Void... params) {
        boolean needUpdate = false;
        for (ApplicationModel applicationModel : models) {
            // If any application models has any new updates
            if (applicationModel != null) {
                List<List<Long>> receiveList = applicationModel.getNetworkReceiveList();
                long previousReceiveData = applicationModel.getPreviousReceiveData();
                if (receiveList.get(receiveList.size() - 1).get(1) > previousReceiveData) {
                    applicationModel.setPreviousReceiveData(receiveList.get(receiveList.size() - 1).get(1));
                    needUpdate = true;
                }

                List<List<Long>> transmitList = applicationModel.getNetworkTransmitList();
                long previousTransmitData = applicationModel.getPreviousTransmitData();
                if (transmitList.get(transmitList.size() - 1).get(1) > previousTransmitData) {
                    applicationModel.setPreviousTransmitData(transmitList.get(transmitList.size() - 1).get(1));
                    needUpdate = true;
                }
            }
        }

        return needUpdate;
    }
    @Override
    protected void onPostExecute(Boolean needUpdate) {
        if (needUpdate) {
            Log.d(TAG, "Asynctask Updated!");
        }
    }
}
