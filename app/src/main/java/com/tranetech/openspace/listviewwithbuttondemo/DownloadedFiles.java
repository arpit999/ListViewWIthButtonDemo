package com.tranetech.openspace.listviewwithbuttondemo;

import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Android Developer on 14-Apr-16.
 */
public class DownloadedFiles extends Fragment {

    private View rootView;
    ListView file_list;
    TextView tv_empty_list;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_downloaded_files, container,
                false);
        file_list = (ListView) rootView.findViewById(R.id.file_list);
        tv_empty_list = (TextView) rootView.findViewById(R.id.tv_empty_list);

        Log.d("PAss","Fragment");
        if (MainActivity.all_pdf.isEmpty()) {
            tv_empty_list.setVisibility(View.VISIBLE);
        } else {

            // This is the array adapter, it takes the context of the activity as a
            // first parameter, the type of list view as a second parameter and your
            // array as a third parameter.
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    getActivity(),
                    android.R.layout.simple_list_item_1,
                    MainActivity.all_pdf);

            file_list.setAdapter(arrayAdapter);

        }

        file_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView tv_paper_name = (TextView) view.findViewById(android.R.id.text1);
                String PaperName = tv_paper_name.getText().toString();
                File extStore = Environment.getExternalStorageDirectory();
                File myFile = new File(extStore.getAbsolutePath() + "/Exam Papers/" + PaperName );

                if (myFile.exists()) {

                    File pdfFile = new File(Environment.getExternalStorageDirectory() + "/Exam Papers/" + PaperName);  // -> filename
                    Uri path = Uri.fromFile(pdfFile);
                    Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                    pdfIntent.setDataAndType(path, "application/pdf");
                    pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    try {
                        startActivity(pdfIntent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(getActivity(), "No Application available to view PDF", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Download file first", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return rootView;
    }
}
