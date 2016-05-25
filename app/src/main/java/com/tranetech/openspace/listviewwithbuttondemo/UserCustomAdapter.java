package com.tranetech.openspace.listviewwithbuttondemo;

/**
 * Created by Arpit Patel on 08-Apr-16.
 */

import java.io.File;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UserCustomAdapter extends ArrayAdapter<User> {
    Context context;
    int layoutResourceId;
    ArrayList<User> data = new ArrayList<User>();
    static View row;
    static DownloadTask downloadTask;

    public UserCustomAdapter(Context context, int layoutResourceId,
                             ArrayList<User> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        row = convertView;
        UserHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new UserHolder();
            holder.tv_paper_name = (TextView) row.findViewById(R.id.tv_paper_name);
//            holder.tv_paper_desc = (TextView) row.findViewById(R.id.tv_paper_desc);
            holder.bt_download = (Button) row.findViewById(R.id.bt_download);
            row.setTag(holder);
        } else {
            holder = (UserHolder) row.getTag();
        }
        User user = data.get(position);
        holder.tv_paper_name.setText(user.getName());
//        holder.tv_paper_desc.setText(user.getAddress());
//        holder.textLocation.setText(user.getLocation());
        final UserHolder finalHolder = holder;

        holder.bt_download.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.i("Download Button Clicked", "**********");
//                Toast.makeText(context, "Download  "+ finalHolder.tv_paper_name.getText().toString()+"  " + position,
//                        Toast.LENGTH_LONG).show();
                File extStore = Environment.getExternalStorageDirectory();
                File myFile = new File(extStore.getAbsolutePath() + "/Exam Papers/"+finalHolder.tv_paper_name.getText().toString()+".pdf");

                if (!myFile.exists()) {

                    // execute this when the downloader must be fired
                    downloadTask = new DownloadTask(context);
                  /*  downloadTask.execute("http://ia.tranetech.ae:82/upload/uploads/five-point-someone-chetan-bhagat_ebook.pdf",""+finalHolder.tv_paper_name.getText().toString()+".pdf");*/
                    downloadTask.execute("http://ia.tranetech.ae:82/web/uploads/five-point-someone-chetan-bhagat_ebook.pdf",""+finalHolder.tv_paper_name.getText().toString()+".pdf");


                } else {

                    Toast.makeText(context, "File already Exists in "+myFile, Toast.LENGTH_SHORT).show();
                }

            }
        });

        return row;

    }

    static class UserHolder {
        TextView tv_paper_name;
//        TextView tv_paper_desc;
        Button bt_download;
    }
}

