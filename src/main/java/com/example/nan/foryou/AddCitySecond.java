package com.example.nan.foryou;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class AddCitySecond extends AppCompatActivity implements View.OnClickListener{
    SharedPreferences cityinfo;
    SharedPreferences.Editor editor;
    String proid;
    public void onClick(View v) {
        Toast.makeText(getApplicationContext(),v.getTag().toString(),Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(this,AddCityThird.class);
        Bundle bundle=getIntent().getExtras();
        bundle.putString("市",v.getTag().toString());
        intent.putExtras(bundle);
        setResult(3,intent);
//        startActivityForResult(intent,3);
        finish();

    }

    ArrayList<String> town=new ArrayList<String>();
    ArrayList<String> townid=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city__second);
        Bundle bundle=getIntent().getExtras();

        Toast.makeText(getApplicationContext(),bundle.getString("省份"),Toast.LENGTH_SHORT).show();
//      Toast.makeText(getApplicationContext(),bundle.getString("proid"),Toast.LENGTH_SHORT).show();
        cityinfo=getSharedPreferences("cityinfo",MODE_PRIVATE);
        editor=cityinfo.edit();
        proid=bundle.getString("proid");
        //打开数据库输出流  
        AssetsDatabaseManager s=new AssetsDatabaseManager("citydb.db");
        SQLiteDatabase db=s.openDatabase(getApplicationContext());
//        if(db!=null)
//            Toast.makeText(this,"有数据库",Toast.LENGTH_SHORT).show();
////        String[] columns = new String[]{"ProName", "ProSort"};
////        Cursor cursor = db.query("T_Province", columns, null, null,null,null,null);
////        int i=0;
////        while (cursor.moveToNext()){
////            int n=cursor.getColumnIndex("ProName");
////            town.add(cursor.getString(n));
////            String str1=cursor.getString(n);
////            String str2=pro;
////            try {
////                str1=new String(str1.trim().getBytes("iso-8859-1"),"UTF-8");
////                } catch (UnsupportedEncodingException e) {
////                // TODO Auto-generated catch block
////                e.printStackTrace();
////                }
////            try {
////                str2=new String(str2.getBytes("iso-8859-1"),"UTF-8");
////                } catch (UnsupportedEncodingException e) {
////                // TODO Auto-generated catch block
////               e.printStackTrace();
////               }
////
////            if(str1.equals(str2)){
////                Toast.makeText(this,"OK",Toast.LENGTH_SHORT).show();
////                break;
////            }
////            i++;
//////            Toast.makeText(this,cursor.getString(n),Toast.LENGTH_SHORT).show();
////        }
////
////        db.close();
        String[] columns = new String[]{"CityName", "ProID"};
        String selection="ProID=?";
       String[] selectionArgs=new String[]{proid};
        Cursor cursor = db.query("T_City",columns,null, null, null,null,null,null);
       while (cursor.moveToNext()){
           if (cursor.getString(cursor.getColumnIndex("ProID")).equals(proid)){
//              Toast.makeText(this,"ID为"+cursor.getString(cursor.getColumnIndex("CityName")),Toast.LENGTH_SHORT).show();
               town.add(cursor.getString(cursor.getColumnIndex("CityName")));
//           int n=cursor.getColumnIndex("CitySort");
//              townid.add(cursor.getString(1));
//               Toast.makeText(this,n,Toast.LENGTH_SHORT).show();
           }
//
//
//
       }
////String pro=cursor.getString(cursor.getColumnIndex("ProSort"));
////        columns = new String[]{"CityName", "ProID"};
////        selection="ProID=?";
////         selectionArgs=new String[]{pro};
////        cursor = db.query("T_City",columns,null, null, null,null,null,null);
////        while (cursor.moveToNext()){
////            Toast.makeText(this,cursor.getString(cursor.getColumnIndex("CityName")),Toast.LENGTH_SHORT).show();
////        }
////        while (cursor.moveToNext()){
////            int n=cursor.getColumnIndex("ProName");
////            if (cursor.getString(n).equals(bundle.getString("省份")))
////            {
////                Toast.makeText(this,"ID为"+cursor.getString(cursor.getColumnIndex("ProSort")),Toast.LENGTH_SHORT).show();
////                break;
////            }
////        }
        db.close();

//        new getcity().start();
//        String city=cityinfo.getString("allcitys",null);
//        String[] provin=city.split(",");
//        for (int i=0;i<provin.length;i++){
//            Log.i("省份",provin[i]);
//            String[] province=provin[i].split("|");
//            townid.add(province[0]+province[1]+province[2]+province[4]);
//            String proname="";
//            for (int j=7;j<province.length;j++){
//                proname+=province[j];
//
//            }
//            town.add(proname);
//        }
//
      showtown();
    }

    class getcity extends Thread{
        @Override
        public void run() {
//            String pro=streampost();
            editor.putString("allcitys",streampost(proid));
            editor.commit();

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        data.putExtra("城市","城市");
        setResult(3,data);
        finish();
    }

    public void showtown(){
        LinearLayout showtown=(LinearLayout)findViewById(R.id.showalltown);
        LinearLayout townline=new LinearLayout(getApplicationContext());
        townline.setOrientation(LinearLayout.HORIZONTAL);
        for(int i=0;i<town.size();i++){
            if(i%4==0){
                showtown.addView(townline);
                townline=new LinearLayout(getApplicationContext());
                townline.setOrientation(LinearLayout.HORIZONTAL);
            }
            Button button=new Button(getApplicationContext());
            setBtnParams(button,i);
            townline.addView(button);
            button.setOnClickListener(this);


        }
        showtown.addView(townline);


    }
    //可在该方法里进行属性参数的相关设置
    private void setBtnParams(Button button,int n) {
        //参数：按钮的宽高
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(52, 40);
//       params.weight=1.0f;//重量级
//        button.setLayoutParams(params);
        button.setText(town.get(n));
        button.setTextSize(10);
        button.setTag(town.get(n));
//        button.setId(Integer.parseInt(townid.get(n)));
    }
    public String streampost(String proid) {
        URL infoUrl = null;

//        String remote_addr="http://mobile.weather.com.cn/data/forecast/"+citycode+".html?_=1381891660081";
        String remote_addr="http://www.weather.com.cn/data/list3/city"+proid+".xml";


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
            return new String(data.toByteArray(), "utf-8");

        } catch (MalformedURLException e) {
            e.printStackTrace();
//            Toast.makeText(this,"网络异常", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
//            Toast.makeText(this,"输出异常",Toast.LENGTH_SHORT).show();
        }

        return "异常";
    }
}
