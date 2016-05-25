package com.tranetech.openspace.listviewwithbuttondemo;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Arpit Patel on 08-Apr-16.
 */

public class MainActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener{
    ListView userList;
    UserCustomAdapter userAdapter=null;
    ArrayList<User> userArray = new ArrayList<User>();
    Button bt_view;
    TextView tv_empty;
    private SwipeRefreshLayout swipeRefreshLayout;
    static List<String> all_pdf;
    // URL to get contacts JSON
    private static String url = "http://api.androidhive.info/songs/albums.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        bt_view = (Button) findViewById(R.id.bt_view);
        userList = (ListView) findViewById(R.id.listView);
        tv_empty= (TextView)findViewById(R.id.tv_empty);

        //         set item into adapter
        userAdapter = new UserCustomAdapter(MainActivity.this, R.layout.row_item,
                userArray);
        userList.setAdapter(userAdapter);

        swipeRefreshLayout.setOnRefreshListener(this);
        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        Log.d("Runnable method ", "");
                                        new GetPapers().execute();
                                    }
                                }
        );

        /**
         * get on item click listener
         */
        userList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    final int position, long id) {

                TextView tv_paper_name = (TextView) v.findViewById(R.id.tv_paper_name);
                String PaperName = tv_paper_name.getText().toString();
                File extStore = Environment.getExternalStorageDirectory();
                File myFile = new File(extStore.getAbsolutePath() + "/Exam Papers/" + PaperName + ".pdf");

                if (myFile.exists()) {

                    File pdfFile = new File(Environment.getExternalStorageDirectory() + "/Exam Papers/" + PaperName + ".pdf");  // -> filename
                    Uri path = Uri.fromFile(pdfFile);
                    Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                    pdfIntent.setDataAndType(path, "application/pdf");
                    pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    try {
                        startActivity(pdfIntent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(MainActivity.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Download file first", Toast.LENGTH_SHORT).show();
                }

            }
        });

        bt_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String folder_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Exam Papers/";
                    all_pdf = new ArrayList<String>();
                    all_pdf = textFiles(folder_path);
                    Log.d("All files ", " " + all_pdf);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(MainActivity.this, ActivityTwo.class);
                startActivity(intent);
            }
        });
    }

    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        Log.d("onRefresh()", "");
        new GetPapers().execute();
    }


    //For fetch the all pdf file in one Directory
    List<String> textFiles(String directory) {
        List<String> textFiles = new ArrayList<String>();
        File dir = new File(directory);
        for (File file : dir.listFiles()) {
            if (file.getName().endsWith((".pdf"))) {
                textFiles.add(file.getName());
            }
        }
        return textFiles;
    }

    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetPapers extends AsyncTask<Void, Void, Void> {

        String Response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // showing refresh animation before making http call
            swipeRefreshLayout.setRefreshing(true);

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            JSONParser sh = new JSONParser();

            // Making a request to url and getting response
            Response = sh.makeServiceCall(url);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (Response != null) {
                userArray.clear();
                try {
                    JSONArray jsonArray = new JSONArray(Response);

                    for (int i = 0; i < jsonArray.length(); i++){

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String PaperName = jsonObject.getString("name");
                        String Conts = jsonObject.getString("songs_count");
                        String id = jsonObject.getString("id");

                        /**
                         * add item in arraylist
                         */
                        userArray.add(new User(PaperName, Conts, id));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                // stopping swipe refresh
                swipeRefreshLayout.setRefreshing(false);
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

//            userAdapter.notifyDataSetChanged();


            // stopping swipe refresh
            userAdapter = new UserCustomAdapter(MainActivity.this, R.layout.row_item,
                    userArray);
            userList.setAdapter(userAdapter);
            // stopping swipe refresh
            swipeRefreshLayout.setRefreshing(false);


        }

    }

}


