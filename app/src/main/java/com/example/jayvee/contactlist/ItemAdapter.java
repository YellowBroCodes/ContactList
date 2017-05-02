package com.example.jayvee.contactlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemAdapter extends BaseAdapter {

    Context context;
    ArrayList<Contact> list;
    LayoutInflater inflater;

    public ItemAdapter(Context context, ArrayList<Contact> list) {
        super();
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View arg1, ViewGroup parent) {
        ItemHandler handler = null;

        if(arg1 == null) {
            arg1 = inflater.inflate(R.layout.itemlayout, null);
            handler = new ItemHandler();
            handler.iv = (ImageView) arg1.findViewById(R.id.imageView);
            handler.lblName = (TextView) arg1.findViewById(R.id.textView1);
            handler.lblTel = (TextView) arg1.findViewById(R.id.textView2);

            arg1.setTag(handler);
        } else {
            handler = (ItemHandler) arg1.getTag();
        }

        handler.iv.setImageURI(list.get(position).getImageUri());
        handler.lblName.setText(list.get(position).getName());
        handler.lblTel.setText(list.get(position).getTel());

        return arg1;
    }

    static class ItemHandler {
        ImageView iv;
        TextView lblName, lblTel;
    }
}
