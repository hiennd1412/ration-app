package com.hiennd1412.ration.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hiennd1412.ration.Entity.DeliverPointModel;
import com.hiennd1412.ration.R;
import com.hiennd1412.ration.Utils.Utils;
import com.hiennd1412.ration.WebserviceGeneralManage.WebserviceInfors;
import com.hiennd1412.ration.activity.ChooseLocationActivity;

import java.util.List;

/**
 * Created by hiennguyen on 2/12/17.
 */

public class ListAdapter_DeliverPointList extends BaseAdapter {

    public static final String TAG = ListAdapter_DeliverPointList.class.getSimpleName();

    private Context mContext;
    private List<DeliverPointModel> listDeliverPointModel;
    private LayoutInflater mInflater;

    public ListAdapter_DeliverPointList(Context context) {
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
                 Gson gson = new Gson();
                 String chooseDeliverPoint = gson.toJson(aDeliverPointModel);
                 SharedPreferences.Editor sharePreferenceEditor = Utils.getCommonSharepreference(mContext).edit();
                 sharePreferenceEditor.putString(mContext.getResources().getString(R.string.choosen_location),chooseDeliverPoint);
                 sharePreferenceEditor.commit();
                 ChooseLocationActivity activity = (ChooseLocationActivity)mContext;
                 activity.finish();
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
