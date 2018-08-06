package com.bandhan.mantra.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bandhan.mantra.R;
import com.bandhan.mantra.model.CouponCampaign;

import java.util.List;

public class CouponCampaignListAdapter extends BaseAdapter {

    private Context context;
    private List<CouponCampaign> couponList;
    private OnButtonActionListener onButtonActionListener;

    public interface OnButtonActionListener {
        public void onViewClicked(CouponCampaign couponCampaign, int position);
    }
    public CouponCampaignListAdapter(Context context, List<CouponCampaign> couponList,OnButtonActionListener onButtonActionListener) {
        this.context = context;
        this.couponList = couponList;
        this.onButtonActionListener = onButtonActionListener;
    }
    @Override
    public int getCount() {
        return this.couponList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.couponList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.couponList.get(position).getId();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final CouponCampaign couponCampaign = couponList.get(position);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coupon_campaigns_list_recycleitem, parent, false);
        TextView coupon_name = (TextView) view.findViewById(R.id.coupon_name);
        TextView coupon_schedule_date = (TextView) view.findViewById(R.id.coupon_schedule_date);
        TextView coupon_recipients = (TextView) view.findViewById(R.id.coupon_recipients);
        LinearLayout container = (LinearLayout)view.findViewById(R.id.container);

        coupon_name.setText(couponCampaign.getTitle());
        coupon_schedule_date.setText(couponCampaign.getSendOn());
        coupon_recipients.setText(""+couponCampaign.getRecipientsCount());
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonActionListener.onViewClicked(couponCampaign,position);
            }
        });
        return view;
    }

}
