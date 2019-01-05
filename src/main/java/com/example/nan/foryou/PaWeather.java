package com.example.nan.foryou;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.CharArrayBuffer;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Nan on 2017/10/27.
 */

public class PaWeather extends Thread{
    Context mycontext;
    SharedPreferences sharedPreferences;
    PaWeather(SharedPreferences preferences,Context context){
        sharedPreferences=preferences;
        mycontext=context;
    }
    @Override
    public void run() {
        String allcitys=sharedPreferences.getString("city",null);
        String[] citys=allcitys.split(" ");
        for(int j=0;j<citys.length;j++){
            StringBuffer stringBuffer=new StringBuffer();
            char[] c=citys[j].toCharArray();
            for (int k=0;k<c.length-1;k++){
            stringBuffer.append(c[k]);
            }
            citys[j]=stringBuffer.toString();
        }
        SharedPreferences.Editor editor=sharedPreferences.edit();
        for (int i=0;i<citys.length;i++){

            editor.putString(citys[i],streampost(queryid(citys[i])));
        }
        editor.commit();
        Log.i("待加载",allcitys);

        String string=streampost("190101");
        Log.i("data",string);
    }
    public String streampost(String citycode) {

        URL infoUrl = null;

//        String remote_addr="http://mobile.weather.com.cn/data/forecast/"+citycode+".html?_=1381891660081";
        String remote_addr="http://www.weather.com.cn/data/cityinfo/101"+citycode+".html";


        try {
            infoUrl = new URL(remote_addr);
            URLConnection connection = infoUrl.openConnection();
            HttpURLConnection httpconnection = (HttpURLConnection) connection;
            httpconnection.setRequestMethod("GET");
            httpconnection.setReadTimeout(5000);
            InputStream inStream = httpconnection.getInputStream();
            ByteArrayOutputStream data = new ByteArrayOutputStream();

            byte[] buffer = new byte[1000];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                data.write(buffer, 0, len);
            }
            inStream.close();
            String test=new String(data.toByteArray(), "utf-8");
            Log.i("test",test);
            String wea=new String(data.toByteArray(), "utf-8");
            StringBuffer weath=new StringBuffer();
            try {
                JSONObject json=new JSONObject(wea);
                JSONObject jsonObject=json.getJSONObject("weatherinfo");
                weath.append(jsonObject.getString("weather")+"  ");
                weath.append(jsonObject.getString("temp1")+"~");
                weath.append(jsonObject.getString("temp2"));
            }catch (Exception e){
e.printStackTrace();
            }
            return weath.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
//            Toast.makeText(this,"网络异常", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
//            Toast.makeText(this,"输出异常",Toast.LENGTH_SHORT).show();
        }

        return "异常";
    }
    public String queryid(String city){
//        AssetsDatabaseManager manager=new AssetsDatabaseManager("citycode.db");
//        SQLiteDatabase db=manager.openDatabase(mycontext);
//        if(db!=null){
//            Log.i("打开了","ID数据库");
//        }
//
//        String[] columns = new String[]{"CityCode", "CityName"};
//        Cursor cursor = db.query("T_CityCode", columns, null, null,null,null,null);
//        int i=0;
//        while (cursor.moveToNext()){
//            int n=cursor.getColumnIndex("CityName");
//            if (city.equals(cursor.getColumnName(n))){
//                return cursor.getString(cursor.getColumnIndex("CityCode"));
//            }
//
//        }
//        db.close();
        java.util.Random r=new java.util.Random();

        return "19010"+r.nextInt(10);
    }
}

