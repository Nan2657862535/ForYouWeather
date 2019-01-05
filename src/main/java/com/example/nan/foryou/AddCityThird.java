package com.example.nan.foryou;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class AddCityThird extends AppCompatActivity implements View.OnClickListener {

    public void onClick(View v) {
        Toast.makeText(getApplicationContext(),v.getTag().toString(),Toast.LENGTH_SHORT).show();
        Intent intent = getIntent();
        Bundle bundle=getIntent().getExtras();
        intent.putExtra("地区",v.getTag().toString());
        intent.putExtras(bundle);
        setResult(2,intent);
        finish();

    }
    ArrayList<String> zone=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city_third);

        Bundle bundle = getIntent().getExtras();

        Toast.makeText(getApplicationContext(), bundle.getString("市"), Toast.LENGTH_SHORT).show();

//        String cityid = bundle.getString("cityid");
//        //打开数据库输出流  
//        AssetsDatabaseManager s = new AssetsDatabaseManager();
//        SQLiteDatabase db = s.openDatabase(getApplicationContext());
//        if (db != null)
//            Toast.makeText(this, "有数据库", Toast.LENGTH_SHORT).show();
//        String[] columns = new String[]{"ZoneName", "CityID"};
//        Cursor cursor = db.query("T_Zone", columns, null, null, null, null, null);
//        while (cursor.moveToNext()) {
//            int n = cursor.getColumnIndex("ZoneName");
//            if (cursor.getString(cursor.getColumnIndex("CityID")).equals(cityid)) {
////                Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
//                zone.add(cursor.getString(cursor.getColumnIndex("ZoneName")));
//
//                break;
//            }
//
//            showzone();
//        }
    }

    public void showzone(){
        LinearLayout showtown=(LinearLayout)findViewById(R.id.showallzone);
        LinearLayout townline=new LinearLayout(getApplicationContext());
        townline.setOrientation(LinearLayout.HORIZONTAL);
        for(int i=0;i<zone.size();i++){
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
        button.setText(zone.get(n));
        button.setTextSize(30);
        button.setTag(zone.get(n));
    }
}
