package com.hiennd1412.ration.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;
import com.hiennd1412.ration.Entity.DeliverPointModel;
import com.hiennd1412.ration.R;
import com.hiennd1412.ration.Utils.Utils;
import com.hiennd1412.ration.WebserviceGeneralManage.WebserviceInfors;
import com.hiennd1412.ration.activity.AskLoginVerificationCodeActivity;
import com.hiennd1412.ration.activity.ChooseLocationActivity;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by hiennguyen on 2/12/17.
 */

public class ListAdapter_DeliverPointList extends BaseAdapter {

    public static final String TAG = ListAdapter_DeliverPointList.class.getSimpleName();

    private ChooseLocationActivity mContext;
    private List<DeliverPointModel> listDeliverPointModel;
    private LayoutInflater mInflater;

    public ListAdapter_DeliverPointList(ChooseLocationActivity context) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<DeliverPointModel> deliverPointModel) {
        this.listDeliverPointModel = deliverPointModel;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        return listDeliverPointModel != null ? listDeliverPointModel.size() : 0;
//        return 2;
    }

    @Override
    public Object getItem(int position) {
        return listDeliverPointModel.get(position);
    }

     @Override
    public long getItemId(int position) {
        return position;
    }

     @Override
    public View getView(int position, View convertView, ViewGroup parent) {

         ViewHolder holder;
         final DeliverPointModel aDeliverPointModel = listDeliverPointModel.get(position);

         // check if the view already exists if so, no need to inflate and findViewById again!
         if (convertView == null) {
             convertView = mInflater.inflate(R.layout.list_view_item_deliver_point, parent, false);

             // create a new "Holder" with subviews
             holder = new ViewHolder();
             holder.tvDeliverPointName = (TextView) convertView.findViewById(R.id.deliver_point_name);
             holder.tvDeliverPointLocation = (TextView) convertView.findViewById(R.id.deliver_point_location);
             holder.btChoose = (Button) convertView.findViewById(R.id.bt_choose_deviver_point);
             holder.deliverPointImage = (ImageView) convertView.findViewById(R.id.deliver_point_image);

             convertView.setTag(holder);
         }
         else {
             // skip all the expensive inflation/findViewById and just get the holder you already made
             holder = (ViewHolder) convertView.getTag();
         }

         holder.btChoose.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 mContext.doVerifyUserByPhoneNumber(aDeliverPointModel);
             }
         });
         final String imageLink = WebserviceInfors.base_host_service + WebserviceInfors.loadDeliverPointImage + aDeliverPointModel.image;
         Glide.with(this.mContext)
                 .load(imageLink)
                 .placeholder(R.drawable.giving_to_charity_blog_size)
                 .into(holder.deliverPointImage);

         holder.tvDeliverPointName.setText(aDeliverPointModel.deliverPointName);
         holder.tvDeliverPointLocation.setText(aDeliverPointModel.location);
         return convertView;
    }

    private static class ViewHolder {
        public TextView tvDeliverPointName;
        public TextView tvDeliverPointLocation;
        public Button btChoose;
        public ImageView deliverPointImage;
    }

}
