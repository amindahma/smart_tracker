package com.example.aminda.smart_tracker.FetchData;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.aminda.smart_tracker.Fragments.BusRouteFragment;
import com.example.aminda.smart_tracker.MainActivity;
import com.example.aminda.smart_tracker.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by uwin5 on 05/20/18.
 */

public class BusRoutes extends AsyncTask<String,Void,String []> {

    private MainActivity main;
    private ProgressDialog dialog;

    public BusRoutes(MainActivity main){
        this.main =main;
    }


    @Override
    protected String[] doInBackground(String... params) {

        try {
            URL url = new URL(main.getApplicationContext().getResources().getString(R.string.IP_ADDRESS )+ main.getApplicationContext().getResources().getString(R.string.BUS_ROUTES));
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data= URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode("","UTF-8");
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String result = "";
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                result += line;

            }

            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();

            return new String[]{result};
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return new String[]{main.getApplicationContext().getResources().getString(R.string.NO_INTERNET)};
        }

        return null;
    }


    @Override
    protected void onPreExecute() {
//        dialog = new ProgressDialog(main.getApplicationContext());
//        this.dialog.setMessage("Please Wait!!");
//        this.dialog.show();

    }

    @Override
    protected void onPostExecute(String [] result) {
        super.onPostExecute(result);
//        if (dialog.isShowing()) {
//            dialog.dismiss();
//        }
        Log.d("fcad",result[0]);
        try {
            JSONArray obj = new JSONArray(result[0]);
            main.setBusRoutesArray(obj);
            for (int i = 0; i< obj.length(); i++) {
                JSONObject jo = (JSONObject) obj.get(i);
                String routeNo  = (String) jo.get("routeNo");
                String name = (String) jo.get("name");
                int id = (int) jo.get("id");
                Log.d("aaaaaaaaaa",routeNo+name+id);
            }

            if(obj.length() > 0){
                BusRouteFragment busRouteFragment = new BusRouteFragment();
                busRouteFragment.setMain(main);
                main.showFragment(busRouteFragment);
            }else{
                Toast.makeText(main, "Network Error!", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}


