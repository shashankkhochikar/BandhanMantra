package com.bandhan.mantra.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bandhan.mantra.R;
import com.bandhan.mantra.model.Datum;

import java.util.List;

/**
 * Created by shashank on 7/26/2018.
 */

public class ContactListAdapter extends BaseAdapter {

    private Context context;
    private List<Datum> dataList;
    private OnButtonActionListener onButtonActionListener;

    public interface OnButtonActionListener {
        public void onCheckBoxPressed(Datum datum, int position, boolean isChecked);

        public void onDeleteButtonPressed(Datum datum, int position);

        public void onViewClicked(Datum datum, int position);
    }

    public ContactListAdapter(Context context, List<Datum> dataList, OnButtonActionListener onButtonActionListener) {
        this.context = context;
        this.dataList = dataList;
        this.onButtonActionListener = onButtonActionListener;
    }

    @Override
    public int getCount() {
        return this.dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.dataList.get(position).getId();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final Datum datum = dataList.get(position);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_recycleviewitem, parent, false);

        LinearLayout linearLayoutContactItem = (LinearLayout) view.findViewById(R.id.linearLayoutContactItem);
        TextView textViewContactName = (TextView) view.findViewById(R.id.textViewName);
        TextView textViewMobileNo = (TextView) view.findViewById(R.id.textViewMob);
        ImageView imageViewDelGroup = (ImageView) view.findViewById(R.id.img_delcnt);
        CheckBox checkBoxContactItem = (CheckBox) view.findViewById(R.id.checkboxContact);

        textViewContactName.setText(datum.getFirstName() + " " + datum.getLastName());
        textViewMobileNo.setText(datum.getMobileNumber());
        linearLayoutContactItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonActionListener.onViewClicked(datum, position);
            }
        });

        /*if (checkBoxContactItem.isChecked()) {
            onButtonActionListener.onCheckBoxPressed(datum, position, true);
        } else if (!checkBoxContactItem.isChecked()) {
            onButtonActionListener.onCheckBoxPressed(datum, position, false);
        }*/
        checkBoxContactItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onButtonActionListener.onCheckBoxPressed(datum, position, isChecked);
            }
        });

        imageViewDelGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonActionListener.onDeleteButtonPressed(datum, position);
            }
        });

        return view;
    }

    public void removeItem(int pos) {
        if (dataList != null && dataList.size() > 0) {
            dataList.remove(pos);
            notifyDataSetChanged();
        }
    }
}
