package com.tranetech.openspace.listviewwithbuttondemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Arpit Patel on 11-Apr-16.
 */
public class DownloadTask extends AsyncTask<String, Integer, String> {

    Context context;
    private PowerManager.WakeLock mWakeLock;
    ProgressDialog mProgressDialog;
    private static final int MEGABYTE = 1024 * 1024;
    DownloadTask downloadTask;
    String Name;

    public DownloadTask(Context context) {
        this.context = context;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // take CPU lock to prevent CPU from going off if the user
        // presses the power button during download
        // instantiate it within the onCreate method
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("Downloading....");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire();
        mProgressDialog.show();

        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                String sdcard_path = Environment.getExternalStorageDirectory().getPath();
                File file = new File(sdcard_path + "/Exam Papers/"+Name+".pdf");
                file.delete();

                Toast.makeText(context, "Download In Background", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    protected String doInBackground(String... str) {

        String URL = str[0];
        Name = str[1];
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();

            // download the file
            input = connection.getInputStream();

            String sdcard_path = Environment.getExternalStorageDirectory().getPath();
            Log.d("Path ------ ", " " + sdcard_path);
            // create a File object for the parent directory
            File PapersDiractory = new File(sdcard_path + "/Exam Papers/");
            // have the object build the directory structure, if needed.
            PapersDiractory.mkdirs();
            // create a File object for the output file
            File outputFile = new File(PapersDiractory, ""+Name);
            // now attach the OutputStream to the file object, instead of a String representation
            output = new FileOutputStream(outputFile);
//                 output = new FileOutputStream(Environment.getExternalStorageDirectory().getPath() + "/five-point-someone-chetan-bhagat_ebook.pdf");

            byte data[] = new byte[MEGABYTE];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                // allow canceling with back button
                if (isCancelled()) {
                    input.close();
                    return null;
                }
                total += count;
                // publishing the progress....
                if (fileLength > 0) // only if total length is known
                    publishProgress((int) (total * 100 / fileLength));
                int progress= (int) (total * 100 / fileLength);
                Log.d("Progress = ", "" + (int) (total * 100 / fileLength));
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            return e.toString();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        // if we get here, length is known, now set indeterminate to false
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMax(100);
        mProgressDialog.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        mWakeLock.release();
        mProgressDialog.dismiss();
        if (result != null)
            Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
        else
            Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show();
    }

}