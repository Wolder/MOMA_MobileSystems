package com.example.moma_mobilesystems.Models;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.moma_mobilesystems.Utility.NetworkHelper;

import java.util.ArrayList;
import java.util.List;

public class ApplicationModel {

    private String name;
    private int UID;
    private Drawable icon;
    private List<String> permissions;
    private String packageName;
    private String versionName;
    private long networkReceiveTotal;
    private long networkTransmitTotal;
    private long networkReceiveStartAmount;
    private long networkTransmitStartAmount;
    private long previousReceiveData = 0;
    private long previousTransmitData = 0;

    private List<List<Long>> networkReceiveList = new ArrayList<>();
    private List<List<Long>> networkTransmitList = new ArrayList<>();
    private boolean visable = false;


    public ApplicationModel(String name, int UID, Drawable icon, List<String> permissions, String packageName, String versionName) {
        this.name = name;
        this.UID = UID;
        this.icon = icon;
        this.permissions = permissions;
        this.packageName = packageName;
        this.versionName = versionName;
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setCurrentNetworkTraffic(Context context, NetworkHelper networkHelper) {
        long totalWifiDataRx = networkHelper.getPackageRxBytesWifi(0, UID);
        long totalMobileDataRx = networkHelper.getPackageRxBytesMobile(context, 0, UID);
        networkReceiveTotal = totalMobileDataRx + totalWifiDataRx;

        long totalWifiDataTx = networkHelper.getPackageTxBytesWifi(0, UID);
        long totalMobileDataTx = networkHelper.getPackageTxBytesMobile(context, 0, UID);
        networkTransmitTotal = totalMobileDataTx + totalWifiDataTx;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setTotalNetworkTraffic(Context context, NetworkHelper networkHelper) {
        long currentWifiDataRx = networkHelper.getPackageRxBytesWifi(0, UID);
        long currentMobileDataRx = networkHelper.getPackageRxBytesMobile(context,0, UID);

        List<Long> networkEntryRx = new ArrayList<>();
        networkEntryRx.add(System.currentTimeMillis());
        networkEntryRx.add(currentMobileDataRx + currentWifiDataRx - networkReceiveStartAmount);

        networkReceiveList.add(networkEntryRx);

        long currentWifiDataTx = networkHelper.getPackageTxBytesWifi(0, UID);
        long currentMobileDataTx = networkHelper.getPackageTxBytesMobile(context, 0, UID);

        List<Long> networkEntryTx = new ArrayList<>();
        networkEntryTx.add(System.currentTimeMillis());
        networkEntryTx.add(currentMobileDataTx + currentWifiDataTx - networkTransmitStartAmount);

        networkTransmitList.add(networkEntryTx);
    }

    public List<List<Long>> getNetworkReceiveList() {
        return networkReceiveList;
    }

    public List<List<Long>> getNetworkTransmitList() {
        return networkTransmitList;
    }

    public long getPreviousReceiveData() {
        return previousReceiveData;
    }

    public void setPreviousReceiveData(long previousReceiveData) {
        this.previousReceiveData = previousReceiveData;
    }

    public long getPreviousTransmitData() {
        return previousTransmitData;
    }

    public void setPreviousTransmitData(long previousTransmitData) {
        this.previousTransmitData = previousTransmitData;
    }

    public long getNetworkReceiveTotal() {
        return networkReceiveTotal;
    }
    public long getNetworkTransmitTotal() {
        return networkTransmitTotal;
    }

    public boolean isCorrectData() {
        if ((name == null) || (UID == 0) || (icon == null) || (packageName == null) || (versionName == null)) {
            return false;
        } else {
            return true;
        }
    }

    public long getNetworkReceiveStartAmount() {
        return networkReceiveStartAmount;
    }

    public void setNetworkReceiveStartAmount(long networkReceiveStartAmount) {
        this.networkReceiveStartAmount = networkReceiveStartAmount;
    }

    public long getNetworkTransmitStartAmount() {
        return networkTransmitStartAmount;
    }

    public void setNetworkTransmitStartAmount(long networkTransmitStartAmount) {
        this.networkTransmitStartAmount = networkTransmitStartAmount;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUID() {
        return UID;
    }

    public void setUID(int UID) {
        this.UID = UID;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(ArrayList<String> permissions) {
        this.permissions = permissions;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public void setVisable(boolean visable) {
        this.visable = visable;
    }

    public boolean isVisable() {
        return visable;
    }


}
