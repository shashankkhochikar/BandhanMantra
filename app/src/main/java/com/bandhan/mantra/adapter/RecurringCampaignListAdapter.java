package com.bandhan.mantra.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bandhan.mantra.R;
import com.bandhan.mantra.model.Item;

import java.util.List;

public class RecurringCampaignListAdapter  extends BaseAdapter {
    private Context context;
    private List<Item> campaignList;
    private OnButtonActionListener onButtonActionListener;

    public interface OnButtonActionListener {
        public void onViewClicked(Item item, int position);
    }
    public RecurringCampaignListAdapter(Context context, List<Item> campaignList,OnButtonActionListener onButtonActionListener) {
        this.context = context;
        this.campaignList = campaignList;
        this.onButtonActionListener = onButtonActionListener;
    }


    @Override
    public int getCount() {
        return this.campaignList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.campaignList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.campaignList.get(position).getId();
    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        final Item item = campaignList.get(position);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recurring_campaigns_list_recycleitem, parent, false);

        LinearLayout container = (LinearLayout)view.findViewById(R.id.container);
        TextView campaigns_name = (TextView) view.findViewById(R.id.campaigns_name);
        TextView campaigns_schedule_date = (TextView) view.findViewById(R.id.campaigns_schedule_date);
        TextView campaigns_recipients = (TextView) view.findViewById(R.id.campaigns_recipients);
        campaigns_name.setText(item.getName());
        campaigns_schedule_date.setText(item.getStartDate());
        campaigns_recipients.setVisibility(View.INVISIBLE);

        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonActionListener.onViewClicked(item,position);
            }
        });
        return view;
    }

}
