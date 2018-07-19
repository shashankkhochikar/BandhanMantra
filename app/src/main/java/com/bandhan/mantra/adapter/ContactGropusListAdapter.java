package com.bandhan.mantra.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bandhan.mantra.R;
import com.bandhan.mantra.model.ContactGroupItemData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shashank on 7/19/2018.
 */

public class ContactGropusListAdapter extends BaseAdapter {

    private Context context;
    private List<ContactGroupItemData> groupList;

    public ContactGropusListAdapter(Context context, List<ContactGroupItemData> groupList) {
        this.context = context;
        this.groupList = groupList;
    }

    @Override
    public int getCount() {
        return this.groupList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.groupList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.groupList.get(position).getId();
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        ContactGroupItemData contactGroupItemData = groupList.get(position);
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_gropus_list_recycleitem, parent, false);
        TextView textViewGroupName = (TextView) view.findViewById(R.id.textViewName);
        TextView textViewGroupCount = (TextView) view.findViewById(R.id.textViewCount);

        textViewGroupName.setText(contactGroupItemData.getName());
        textViewGroupCount.setText(contactGroupItemData.getContactCount());
        return view;
    }

}
