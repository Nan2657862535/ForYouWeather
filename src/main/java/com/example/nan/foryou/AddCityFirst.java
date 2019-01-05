package com.example.nan.foryou;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class AddCityFirst extends AppCompatActivity implements View.OnClickListener{
//    String provinces[];
ArrayList<String> provinces=new ArrayList<String>();
    ArrayList<String> proid=new ArrayList<String>();

    SharedPreferences provinceinfo;
    SharedPreferences.Editor editor;
    @Override
    public void onClick(View v) {
        Bundle bundle=new Bundle();
        bundle.putString("省份",v.getTag().toString());
        bundle.putString("proid",String.valueOf(v.getId()));
        Intent intent=new Intent(getApplicationContext(),AddCitySecond.class);
        intent.putExtras(bundle);
        startActivityForResult(intent,2);
    }

    public void showproves(){
        LinearLayout showpro=(LinearLayout)findViewById(R.id.showallprovince);
        LinearLayout proline=new LinearLayout(getApplicationContext());
        proline.setOrientation(LinearLayout.HORIZONTAL);
        for(int i=0;i<provinces.size();i++){
            if(i%4==0){
                showpro.addView(proline);
                proline=new LinearLayout(getApplicationContext());
                proline.setOrientation(LinearLayout.HORIZONTAL);
            }
            Button button=new Button(getApplicationContext());
            setBtnParams(button,i);
            proline.addView(button);
            button.setOnClickListener(this);


        }
        showpro.addView(proline);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city_first);
        provinceinfo=getSharedPreferences("provinceinfo",MODE_PRIVATE);
        editor=provinceinfo.edit();
        //打开数据库输出流  
        AssetsDatabaseManager s=new AssetsDatabaseManager("citydb.db");
        SQLiteDatabase db=s.openDatabase(getApplicationContext());
//        if(db!=null)
//            Toast.makeText(this,"有数据库",Toast.LENGTH_SHORT).show();
        String[] columns = new String[]{"ProName", "ProSort"};
        Cursor cursor = db.query("T_Province", columns, null, null,null,null,null);
        int i=0;
        while (cursor.moveToNext()){
            int n=cursor.getColumnIndex("ProName");
            provinces.add(cursor.getString(n));
            proid.add(cursor.getString(cursor.getColumnIndex("ProSort")));

        }
        db.close();

//     new getprovince().start();
//        String pro=provinceinfo.getString("allprovinces",null);
//        String[] provin=pro.split(",");
//        for (int i=0;i<provin.length;i++){
//            Log.i("省份",provin[i]);
//            String[] province=provin[i].split("|");
//            proid.add(province[0]+province[1]);
//            String proname="";
//            for (int j=4;j<province.length;j++){
//                proname+=province[j];
//
//            }
//            provinces.add(proname);
//        }
        showproves();

    }

    class getprovince extends Thread{
        @Override
        public void run() {
//            String pro=streampost();
            editor.putString("allprovinces",streampost());
          editor.commit();

        }
    }

    //可在该方法里进行属性参数的相关设置
    private void setBtnParams(Button button,int n) {
        //参数：按钮的宽高
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(52, 40);
//       params.weight=1.0f;//重量级
//        button.setLayoutParams(params);
        button.setText(provinces.get(n));
        button.setTextSize(10);
        button.setTag(provinces.get(n));
        button.setId(Integer.parseInt(proid.get(n)));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        setResult(3,data);
        finish();
    }
    public String streampost() {
        URL infoUrl = null;

//        String remote_addr="http://mobile.weather.com.cn/data/forecast/"+citycode+".html?_=1381891660081";
        String remote_addr="http://www.weather.com.cn/data/list3/city.xml";


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