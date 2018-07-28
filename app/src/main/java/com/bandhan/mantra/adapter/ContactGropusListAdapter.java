package com.bandhan.mantra.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
    private OnButtonActionListener onButtonActionListener;

    public interface OnButtonActionListener {
        public void onEditButtonPressed(ContactGroupItemData contactGroupItemData, int position);
        public void onDeleteButtonPressed(ContactGroupItemData contactGroupItemData, int position);
        public void onViewClicked(ContactGroupItemData contactGroupItemData, int position);
    }

    public ContactGropusListAdapter(Context context, List<ContactGroupItemData> groupList,OnButtonActionListener onButtonActionListener) {
        this.context = context;
        this.groupList = groupList;
        this.onButtonActionListener = onButtonActionListener;
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

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ContactGroupItemData contactGroupItemData = groupList.get(position);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_gropus_list_recycleitem, parent, false);
        TextView textViewGroupName = (TextView) view.findViewById(R.id.textViewName);
        TextView textViewGroupCount = (TextView) view.findViewById(R.id.textViewCount);
        ImageView imageViewDelGroup = (ImageView)view.findViewById(R.id.img_delgrp);
        ImageView imageViewEditGroup = (ImageView)view.findViewById(R.id.img_edtgrp);

        textViewGroupName.setText(contactGroupItemData.getName());
        textViewGroupCount.setText(""+contactGroupItemData.getContactCount());

        imageViewDelGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonActionListener.onDeleteButtonPressed(contactGroupItemData,position);
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonActionListener.onViewClicked(contactGroupItemData,position);
            }
        });
        imageViewEditGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonActionListener.onEditButtonPressed(contactGroupItemData,position);
            }
        });

        return view;
    }

    public void removeItem(int pos) {
        if (groupList != null && groupList.size() > 0) {
            groupList.remove(pos);
            notifyDataSetChanged();
        }
    }
}
