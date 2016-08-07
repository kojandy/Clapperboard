package com.kojandy.clapperboard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

public class Adapter extends RealmBaseAdapter<Data> implements ListAdapter {

    private static class ViewHolder {
        TextView textView;
    }

    public Adapter(@NonNull Context context, @Nullable OrderedRealmCollection<Data> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Data item = adapterData.get(position);
        viewHolder.textView.setText(item.scene + "-" + item.cut + "-" + item.take + "-" + item.camera + " " + item.isOk + " " + item.note);
        return convertView;
    }
}
