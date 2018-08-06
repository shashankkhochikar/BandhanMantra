package com.bandhan.mantra.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bandhan.mantra.R;
import com.bandhan.mantra.model.TemplateItem;

import java.util.List;

public class TemplateListAdapter extends BaseAdapter {

    private Context context;
    private List<TemplateItem> templeteList;
    private OnButtonActionListener onButtonActionListener;

    public interface OnButtonActionListener {
        public void onViewClicked(TemplateItem templateItem, int position);
    }
    public TemplateListAdapter(Context context, List<TemplateItem> templeteList,OnButtonActionListener onButtonActionListener) {
        this.context = context;
        this.templeteList = templeteList;
        this.onButtonActionListener = onButtonActionListener;
    }
    @Override
    public int getCount() {
        return this.templeteList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.templeteList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.templeteList.get(position).getId();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final TemplateItem templateItem = templeteList.get(position);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.templates_list_recycleitem, parent, false);
        LinearLayout container = (LinearLayout)view.findViewById(R.id.container);
        TextView templates_title = (TextView) view.findViewById(R.id.templates_title);
        TextView template_msg = (TextView) view.findViewById(R.id.template_msg);

        template_msg.setText(templateItem.getMessage());
        templates_title.setText(templateItem.getTitle());
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonActionListener.onViewClicked(templateItem,position);
            }
        });

        return view;
    }

}
