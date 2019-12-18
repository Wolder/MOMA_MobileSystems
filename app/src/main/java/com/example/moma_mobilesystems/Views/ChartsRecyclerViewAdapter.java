package com.example.moma_mobilesystems.Views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.moma_mobilesystems.Models.ApplicationModel;
import com.example.moma_mobilesystems.R;

public class ChartsRecyclerViewAdapter extends RecyclerView.Adapter<ChartsRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "ChartsRecyclerViewAdapter: ";

    private ApplicationModel[] mData;
    private LayoutInflater mInflater;

    public ChartsRecyclerViewAdapter(Context context, ApplicationModel[] data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View applicationItem = mInflater.inflate(R.layout.charts_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(applicationItem);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ApplicationModel applicationModel = mData[position];
        if (applicationModel != null) {
            holder.appLogo.setImageDrawable(applicationModel.getIcon());
            holder.appName.setText("Name: " + applicationModel.getName());
            holder.appUID.setText("UID: " + applicationModel.getUID());
            holder.appPermissions.setText("Total Permissions: " + applicationModel.getPermissions().size());
            holder.appNetworkReceive.setText("Total Network Receive: " + applicationModel.getNetworkReceiveList().get(applicationModel.getNetworkReceiveList().size() - 1).get(1) + " B");
            holder.appNetworkTransmit.setText("Total Network Transmit: " + applicationModel.getNetworkTransmitList().get(applicationModel.getNetworkTransmitList().size() - 1).get(1) + " B");
        }

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(holder.itemView.getRootView(), applicationModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.length;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView appLogo;
        TextView appName;
        TextView appUID;
        TextView appPermissions;
        TextView appNetworkReceive;
        TextView appNetworkTransmit;
        LinearLayout item;

        ViewHolder(View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            appLogo = itemView.findViewById(R.id.application_logo);
            appName = itemView.findViewById(R.id.application_name);
            appUID = itemView.findViewById(R.id.application_uid);
            appPermissions = itemView.findViewById(R.id.application_permissions);
            appNetworkReceive = itemView.findViewById(R.id.application_network_receive);
            appNetworkTransmit = itemView.findViewById(R.id.application_network_transmit);
        }
    }

    ApplicationModel getItem(int id) {
        return mData[id];
    }

    public void showPopup(View view, ApplicationModel applicationModel) {
        View popupView = mInflater.inflate(R.layout.popup_window_charts, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        ImageView appImage = popupView.findViewById(R.id.popupImage);
        TextView appName = popupView.findViewById(R.id.popupName);
        TextView appUID = popupView.findViewById(R.id.popupUID);
        TextView appNetworkReceive = popupView.findViewById(R.id.popupReceive);
        TextView appNetworkTransmit = popupView.findViewById(R.id.popupTransmit);

        appImage.setImageDrawable(applicationModel.getIcon());
        appName.setText(applicationModel.getName());
        appUID.setText("UID: " + applicationModel.getUID());

        long trantmitData = applicationModel.getNetworkTransmitList().get(applicationModel.getNetworkTransmitList().size() - 1).get(1);
        long recieveData = applicationModel.getNetworkReceiveList().get(applicationModel.getNetworkReceiveList().size() - 1).get(1);

        trantmitData = trantmitData/1000;
        recieveData = recieveData/1000;

        appNetworkReceive.setText("Reveived: " + recieveData + " KB");
        appNetworkTransmit.setText("Transmitted: " + trantmitData + " KB");



        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }
}