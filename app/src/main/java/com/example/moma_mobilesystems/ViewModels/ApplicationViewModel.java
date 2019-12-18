package com.example.moma_mobilesystems.ViewModels;

import android.Manifest;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moma_mobilesystems.Models.ApplicationModel;
import com.example.moma_mobilesystems.Utility.NetworkHelper;
import com.example.moma_mobilesystems.Views.CheckForNetworkChangesAsync;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApplicationViewModel extends ViewModel {

    private static final String TAG = "ApplicationViewModel: ";
    private MutableLiveData<ApplicationModel[]> liveApplicationList;
    private ApplicationModel[] applicationArray;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public LiveData<ApplicationModel[]> getApplicationList(Context context) {
        if (liveApplicationList == null) {
            applicationArray = getPackages(context);
            liveApplicationList = new MutableLiveData<>();
            initNetworkTrafficInfo(context);
        }
        // Update network information
        updateApplicationList(context);
        liveApplicationList.setValue(applicationArray);

        return liveApplicationList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initNetworkTrafficInfo(Context context) {
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
        NetworkHelper networkHelper = new NetworkHelper(networkStatsManager);

        Log.d(TAG, "init activity list network traffic");

        for (int i = 0; i < applicationArray.length; i++) {

            if (applicationArray[i] == null) {
                removeElement(applicationArray, i);
            } else {
                // Total network update
                applicationArray[i].setCurrentNetworkTraffic(context, networkHelper);

                // Add current batch to array
               // applicationArray[i].setTotalNetworkTraffic(context, networkHelper, appStartTime);

                // Set initial values for network traffic calcs.
                applicationArray[i].setNetworkReceiveStartAmount(applicationArray[i].getNetworkReceiveTotal());
                applicationArray[i].setNetworkTransmitStartAmount(applicationArray[i].getNetworkTransmitTotal());
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateApplicationList(Context context) {
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
        NetworkHelper networkHelper = new NetworkHelper(networkStatsManager);
        Log.d(TAG, "Updated activity list");

        for (int i = 0; i < applicationArray.length; i++) {
            if (applicationArray[i] == null) {
                removeElement(applicationArray, i);
            } else {
                applicationArray[i].setTotalNetworkTraffic(context, networkHelper);
            }
        }

        for (ApplicationModel applicationModel : applicationArray) {
            // Filter out applications that has not had any previous network activity
            if (applicationModel != null) {
                if ((applicationModel.getNetworkReceiveList().get(applicationModel.getNetworkReceiveList().size() - 1).get(1) > 0) ||
                        (applicationModel.getNetworkTransmitList().get(applicationModel.getNetworkTransmitList().size() - 1).get(1) > 0)) {
                    applicationModel.setVisable(true);
                }
            }
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "Array Cleared.");
    }

    private ApplicationModel[] getPackages(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);

        ApplicationModel[] modelArray = new ApplicationModel[packageInfoList.size()];
        int i = 0;
        for (PackageInfo packageInfo : packageInfoList) {
            // Delete
            if (packageManager.checkPermission(Manifest.permission.INTERNET,
                    packageInfo.packageName) == PackageManager.PERMISSION_DENIED) {
                continue;
            }

            ApplicationModel am = new ApplicationModel(
                    packageManager.getApplicationLabel(packageInfo.applicationInfo).toString(),
                    packageInfo.applicationInfo.uid,
                    packageInfo.applicationInfo.loadIcon(context.getPackageManager()),
                    getPackagePermissions(context, packageInfo),
                    packageInfo.packageName,
                    packageInfo.versionName
            );

            if (am.isCorrectData()) {
                modelArray[i] = am;
            }

            i++;
        }
        return modelArray;
    }

    private List<String> getPackagePermissions(Context context, PackageInfo packageInfo) {
        // Construct Permission ArrayList
        List<String> permissionArray = null;
        try {
            PackageInfo permissionInfo = context.getPackageManager().getPackageInfo(packageInfo.packageName, PackageManager.GET_PERMISSIONS);

            if (permissionInfo.requestedPermissions != null) {
                permissionArray = Arrays.asList(permissionInfo.requestedPermissions);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (permissionArray == null) {
            permissionArray = new ArrayList<>();
        }

        return permissionArray;
    }

    public void removeElement(Object[] arr, int removedIdx) {
        System.arraycopy(arr, removedIdx + 1, arr, removedIdx, arr.length - 1 - removedIdx);
    }
}
