package com.example.system3.shop_locator;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

/**
 * Created by System 3 on 5/11/2018.
 */

public class Connect {
    public String Data(String url,List Namvaluepair){

        String serviceURL =url;
        StringBuffer sb = new StringBuffer();
        try {
            URL u = new URL(serviceURL);
            HttpResponse response;
            HttpClient client = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(u.toString());
            httppost.setEntity(new UrlEncodedFormEntity(Namvaluepair));
            response=client.execute(httppost);
            BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = br.readLine();
            sb.append(line);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
