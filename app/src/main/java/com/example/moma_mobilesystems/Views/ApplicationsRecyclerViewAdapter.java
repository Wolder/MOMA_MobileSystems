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

import org.w3c.dom.Text;

public class ApplicationsRecyclerViewAdapter extends RecyclerView.Adapter<ApplicationsRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "ApplicationsRecyclerViewAdapter: ";

    private ApplicationModel[] mData;
    private LayoutInflater mInflater;

    public ApplicationsRecyclerViewAdapter(Context context, ApplicationModel[] data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public ApplicationsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View applicationItem = mInflater.inflate(R.layout.application_item, parent, false);
        ApplicationsRecyclerViewAdapter.ViewHolder viewHolder = new ApplicationsRecyclerViewAdapter.ViewHolder(applicationItem);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ApplicationsRecyclerViewAdapter.ViewHolder holder, int position) {
        final ApplicationModel applicationModel = mData[position];
        if (applicationModel != null) {

            holder.appLogo.setImageDrawable(applicationModel.getIcon());
            holder.appName.setText("Name: " + applicationModel.getName());
            holder.appUID.setText("UID: " + applicationModel.getUID());
            holder.appPermissions.setText("Total Permissions: " + applicationModel.getPermissions().size());

            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopup(holder.itemView.getRootView(), applicationModel);
                }
            });
        }

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
        LinearLayout item;

        ViewHolder(View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            appLogo = itemView.findViewById(R.id.application_logo);
            appName = itemView.findViewById(R.id.application_name);
            appUID = itemView.findViewById(R.id.application_uid);
            appPermissions = itemView.findViewById(R.id.application_permissions);
        }
    }

    public void showPopup(View view, ApplicationModel applicationModel) {
        View popupView = mInflater.inflate(R.layout.popup_window_applications, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        ImageView appImage = popupView.findViewById(R.id.popupImage);
        TextView appName = popupView.findViewById(R.id.popupName);
        TextView appUID = popupView.findViewById(R.id.popupUID);

        appImage.setImageDrawable(applicationModel.getIcon());
        appName.setText(applicationModel.getName());
        appUID.setText("UID: " + applicationModel.getUID());

        ScrollView permissionView = popupView.findViewById(R.id.popupPermissionScrollView);

        for (int i = 0; i < applicationModel.getPermissions().size(); i++) {
            TextView permission = new TextView(view.getContext());
            permission.setText(applicationModel.getPermissions().get(i));
            permission.setTextSize(10);
            ((LinearLayout)permissionView.getChildAt(0)).addView(permission);
        }


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