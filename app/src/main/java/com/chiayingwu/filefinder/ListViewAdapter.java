package com.chiayingwu.filefinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.listallfiles.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by chiaying.wu on 2017/7/19.
 */

public class ListViewAdapter extends ArrayAdapter<File> {
    public ListViewAdapter(Context context, int resource, ArrayList<File> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (null == v) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item, null);
        }
        File matchedFile = getItem(position);
        ImageView img = (ImageView) v.findViewById(R.id.imageView);
        TextView txtName = (TextView) v.findViewById(R.id.txtName);
        TextView txtSize = (TextView) v.findViewById(R.id.txtSize);
        TextView txtCreationDate = (TextView) v.findViewById(R.id.txtCreationDate);

        img.setImageResource(R.drawable.icon_file);
        txtName.setText(matchedFile.getName());
        txtSize.setText(DataConverter.humanReadableByteCount(matchedFile.length(), true));
        txtCreationDate.setText(String.valueOf(DataConverter.convertTime(matchedFile.lastModified())));

        return v;
    }
}
