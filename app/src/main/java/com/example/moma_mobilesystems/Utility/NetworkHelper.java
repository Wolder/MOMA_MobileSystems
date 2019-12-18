package com.example.moma_mobilesystems.Utility;

import android.annotation.SuppressLint;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.RemoteException;
import android.telephony.TelephonyManager;

import androidx.annotation.RequiresApi;

public class NetworkHelper {

    private NetworkStatsManager networkStatsManager;

    public NetworkHelper(NetworkStatsManager networkStatsManager) {
        this.networkStatsManager = networkStatsManager;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public long getAllRxBytesMobile(Context context, long startTime) {
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE,
                    getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),
                    startTime,
                    System.currentTimeMillis());
        } catch (RemoteException e) {
            return -1;
        }
        return bucket.getRxBytes();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public long getAllTxBytesMobile(Context context, long startTime) {
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE,
                    getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),
                    startTime,
                    System.currentTimeMillis());
        } catch (RemoteException e) {
            return -1;
        }
        return bucket.getTxBytes();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public long getAllRxBytesWifi(long startTime) {
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_WIFI,
                    "",
                    startTime,
                    System.currentTimeMillis());
        } catch (RemoteException e) {
            return -1;
        }
        return bucket.getRxBytes();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public long getAllTxBytesWifi(long startTime) {
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_WIFI,
                    "",
                    startTime,
                    System.currentTimeMillis());
        } catch (RemoteException e) {
            return -1;
        }
        return bucket.getTxBytes();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public long getPackageRxBytesMobile(Context context, long startTime, int packageUID) {
        NetworkStats networkStats = null;
        networkStats = networkStatsManager.queryDetailsForUid(
                ConnectivityManager.TYPE_MOBILE,
                getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),
                startTime,
                System.currentTimeMillis(),
                packageUID);

        long rxBytes = 0L;
        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
        while (networkStats.hasNextBucket()) {
            networkStats.getNextBucket(bucket);
            rxBytes += bucket.getRxBytes();
        }
        networkStats.close();
        return rxBytes;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public long getPackageTxBytesMobile(Context context, long startTime, int packageUID) {
        NetworkStats networkStats = null;
        networkStats = networkStatsManager.queryDetailsForUid(
                ConnectivityManager.TYPE_MOBILE,
                getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),
                startTime,
                System.currentTimeMillis(),
                packageUID);

        long txBytes = 0L;
        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
        while (networkStats.hasNextBucket()) {
            networkStats.getNextBucket(bucket);
            txBytes += bucket.getTxBytes();
        }
        networkStats.close();
        return txBytes;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public long getPackageRxBytesWifi(long startTime, int packageUID) {
        NetworkStats networkStats = null;
        networkStats = networkStatsManager.queryDetailsForUid(
                ConnectivityManager.TYPE_WIFI,
                "",
                startTime,
                System.currentTimeMillis(),
                packageUID);

        long rxBytes = 0L;
        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
        while (networkStats.hasNextBucket()) {
            networkStats.getNextBucket(bucket);
            rxBytes += bucket.getRxBytes();
        }
        networkStats.close();
        return rxBytes;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public long getPackageTxBytesWifi(long startTime, int packageUID) {
        NetworkStats networkStats = null;
        networkStats = networkStatsManager.queryDetailsForUid(
                ConnectivityManager.TYPE_WIFI,
                "",
                startTime,
                System.currentTimeMillis(),
                packageUID);

        long txBytes = 0L;
        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
        while (networkStats.hasNextBucket()) {
            networkStats.getNextBucket(bucket);
            txBytes += bucket.getTxBytes();
        }
        networkStats.close();
        return txBytes;
    }

    @SuppressLint("MissingPermission")
    private String getSubscriberId(Context context, int networkType) {
        if (ConnectivityManager.TYPE_MOBILE == networkType) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getSubscriberId();
        }
        return "";
    }
}

