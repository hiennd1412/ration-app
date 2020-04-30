package com.hiennd1412.ration.adapter;

import android.content.Context;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hiennd1412.ration.Entity.CallLogModel;
import com.hiennd1412.ration.R;
import com.hiennd1412.ration.Utils.Utils;

import java.util.List;

/**
 * Created by hiennguyen on 2/12/17.
 */

public class ListAdapter_ReceivedCallList extends BaseAdapter {

    public static final String TAG = ListAdapter_ReceivedCallList.class.getSimpleName();

    private Context mContext;
    private List<CallLogModel> callLogList;
    private LayoutInflater mInflater;

    public ListAdapter_ReceivedCallList(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<CallLogModel> callLogList) {
        this.callLogList = callLogList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        return callLogList != null ? callLogList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return callLogList.get(position);
    }

     @Override
    public long getItemId(int position) {
        return position;
    }

     @Override
    public View getView(int position, View convertView, ViewGroup parent) {

         ViewHolder holder;
         CallLogModel aCallLog = callLogList.get(position);

         // check if the view already exists if so, no need to inflate and findViewById again!
         if (convertView == null) {
             convertView = mInflater.inflate(R.layout.list_view_item_receive_call, parent, false);

             // create a new "Holder" with subviews
             holder = new ViewHolder();
             holder.tvCallNumber = (TextView) convertView.findViewById(R.id.textview_phone_number);
             holder.tvCallTime = (TextView) convertView.findViewById(R.id.textview_call_time);
             convertView.setTag(holder);
         }
         else {
             // skip all the expensive inflation/findViewById and just get the holder you already made
             holder = (ViewHolder) convertView.getTag();
         }

         holder.tvCallNumber.setText(aCallLog.callNumber);
         holder.tvCallTime.setText(Utils.formatDate(aCallLog.callTime));
         return convertView;
    }

    private static class ViewHolder {
        public TextView tvCallNumber;
        public TextView tvCallTime;
    }

}
