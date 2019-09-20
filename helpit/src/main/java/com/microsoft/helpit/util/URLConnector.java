package com.microsoft.helpit.util;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class URLConnector {

    public static String doGet(String url_str,String bearerToken, boolean gZip){
        String out = null;
        URL url = null;
        HttpsURLConnection connection = null;

        try {
            url = new URL(url_str);
            connection = (HttpsURLConnection) url.openConnection();
            if(bearerToken!=null){
                connection.setRequestProperty("Authorization", "Bearer " + bearerToken);
            }
            connection.setRequestProperty("Content-Type", "text/json");
            connection.setRequestProperty("Charset", "utf-8");
            connection.setRequestMethod("GET");
            connection.connect();

            InputStreamReader inputStreamReader = null;
            if(connection.getResponseCode()==200){
                if(gZip){
                    inputStreamReader = new InputStreamReader(new GZIPInputStream(connection.getInputStream()));
                }else
                    inputStreamReader = new InputStreamReader(connection.getInputStream());

                BufferedReader reader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                String str = null;
                while ((str=reader.readLine())!=null){
                    stringBuilder.append(str);
                }
                inputStreamReader.close();
                reader.close();
                out = stringBuilder.toString();
            }
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

    public static String doPost(String url_str, Map<String,String> property, byte[] params){
        String out = null;
        URL url = null;
        HttpsURLConnection connection = null;

        try {
            url = new URL(url_str);
            connection = (HttpsURLConnection) url.openConnection();
            for(String key :property.keySet()){
                connection.setRequestProperty(key,property.get(key));
            }
            connection.setRequestProperty("Content-Type", "text/json;charset=utf-8");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.connect();

            DataOutputStream dataout = new DataOutputStream(connection.getOutputStream());
            dataout.write(params,0, params.length);
            dataout.flush();
            dataout.close();

            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String str = null;
            while ((str=reader.readLine())!=null){
                stringBuilder.append(str);
            }
            inputStreamReader.close();
            reader.close();
            out = stringBuilder.toString();
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }
}
