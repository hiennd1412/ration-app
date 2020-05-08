package com.hiennd1412.ration.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hiennd1412.ration.Entity.AllocationModel;
import com.hiennd1412.ration.R;
import com.hiennd1412.ration.Utils.Utils;
import com.hiennd1412.ration.WebserviceGeneralManage.WebserviceInfors;

import java.util.List;

/**
 * Created by hiennguyen on 2/12/17.
 */

public class ListAdapter_AllocationList extends BaseAdapter {

    public static final String TAG = ListAdapter_AllocationList.class.getSimpleName();

    private Context mContext;
    private List<AllocationModel> allocationList;
    private LayoutInflater mInflater;

    public ListAdapter_AllocationList(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<AllocationModel> allocationList) {
        this.allocationList = allocationList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        return allocationList != null ? allocationList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return allocationList.get(position);
    }

     @Override
    public long getItemId(int position) {
        return position;
    }

     @Override
    public View getView(int position, View convertView, ViewGroup parent) {

         ViewHolder holder;
         AllocationModel anAllocationModel = allocationList.get(position);

         // check if the view already exists if so, no need to inflate and findViewById again!
         if (convertView == null) {
             convertView = mInflater.inflate(R.layout.list_view_item_allocation_info, parent, false);

             // create a new "Holder" with subviews
             holder = new ViewHolder();
             holder.tvAllocationPointName = (TextView) convertView.findViewById(R.id.allocation_point_name);
             holder.tvAllocationLocation = (TextView) convertView.findViewById(R.id.allocation_location);
             holder.tvAllocationTime = (TextView) convertView.findViewById(R.id.allocation_time);
             holder.tvAllocationPackageCount = (TextView) convertView.findViewById(R.id.allocation_package_count);
             holder.imgAllocationImage = (ImageView) convertView.findViewById(R.id.allocation_image);
             convertView.setTag(holder);
         }
         else {
             // skip all the expensive inflation/findViewById and just get the holder you already made
             holder = (ViewHolder) convertView.getTag();
         }

         if(anAllocationModel.deliverPoint != null) {
             holder.tvAllocationPointName.setText(anAllocationModel.deliverPoint.deliverPointName);
             holder.tvAllocationLocation.setText(anAllocationModel.deliverPoint.location);
         }
         holder.tvAllocationTime.setText(Utils.formatDate(anAllocationModel.allowcationTime));
         holder.tvAllocationPackageCount.setText(anAllocationModel.packageCount + " phần - ");
         final String imageLink = WebserviceInfors.base_host_service + WebserviceInfors.loadAllocationImage + anAllocationModel.image;
         Glide.with(this.mContext)
                 .load(imageLink)
                 .placeholder(R.drawable.give_gift)
                 .into(holder.imgAllocationImage);
         return convertView;
    }

    private static class ViewHolder {
        public TextView tvAllocationPointName;
        public TextView tvAllocationLocation;
        public TextView tvAllocationTime;
        public TextView tvAllocationPackageCount;
        public ImageView imgAllocationImage;
    }

}
