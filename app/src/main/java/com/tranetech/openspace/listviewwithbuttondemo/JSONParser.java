package com.tranetech.openspace.listviewwithbuttondemo;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSONParser {

	public JSONParser() {

	}

	public String makeServiceCall(String serviceUrl) {
		Boolean registered = false;
		StringBuffer response = new StringBuffer();
		URL url = null;

		HttpURLConnection conn = null;
		try {

			url = new URL(serviceUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(30000);
			conn.setDoOutput(false);
			conn.setUseCaches(false);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			int status = conn.getResponseCode();
			if (status != 200) {
				throw new IOException("Post failed with error code " + status);
			}
			registered = true;

			// Get Response
			InputStream is = conn.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;

			while ((line = rd.readLine()) != null) {
				response.append(line);

			}
			rd.close();

		} catch (Exception e) {
			e.printStackTrace();
			//ErrorLogger.writeLog(claimNo, "Error Msg : "+e.getMessage()+"..."+ErrorLogger.StackTraceToString(e), "sendSyncService Failed");
			//response.append("Error");
		}
		Log.v("Response:", response.toString());
		return response.toString();

	}


}


