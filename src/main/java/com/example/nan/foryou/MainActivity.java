package com.example.nan.foryou;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,PopupMenu.OnMenuItemClickListener,Handler.Callback{

    private Handler handler ;
    private MyScrollView test;

    // 滚动条初始偏移量
    private int offset = 0;
    // 当前页编号
    private int currIndex = 0;
    // 滚动条宽度
    private int bmpW;
    //一倍滚动量
    private int one;
    public ImageView leftmenu;
    public ImageView rightmenu;
    public TextView showcurrentcityname;
    SharedPreferences allcitys;//保存着已经添加的城市
    //已经添加的城市字符串数组
    public ArrayList<String> cy=new ArrayList<String>();

    SQLiteDatabase citydb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//
//        //设置初始城市
//        allcitys=getSharedPreferences("allcitys",MODE_PRIVATE);
//        SharedPreferences.Editor editor=allcitys.edit();
//        editor.putString("city","广州市 北京市 深圳市 成都市");
//        editor.commit();


        showcurrentcityname=(TextView)this.findViewById(R.id.showcurrentcityname);
        showViewpager();
        showcurrentcityname.setText(cy.get(0).toString());


        leftmenu=(ImageView)findViewById(R.id.left_menu);
        rightmenu=(ImageView)findViewById(R.id.right_menu);
        leftmenu.setOnClickListener(this);
        rightmenu.setOnClickListener(this);


          /*
        *
        * */
        handler = new Handler(getMainLooper(), this);

        test = (MyScrollView) findViewById(R.id.test);

        test.setOnRefreshListener(new MyScrollView.onRefreshListener() {

            @Override
            public void refresh() {
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        test.stopRefresh();
                    }
                }, 2000);
            }
        });

/*************************p爬取天气***************************************/
        new PaWeather(allcitys,getApplicationContext()).start();

    }

    //处理刷新停留
    @Override
    public boolean handleMessage(Message msg) {
        // TODO Auto-generated method stub
        return false;
    }




//点击事件响应
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_menu:
                break;
            case R.id.right_menu:
                popupmenu(v);
                break;
        }
    }
//展示滑动页面
    public void showViewpager(){
        ViewPager viewPager;
        viewPager=(ViewPager)findViewById(R.id.showWeather);
        final ArrayList<View> citys=new ArrayList<View>();
        allcitys=getSharedPreferences("allcitys",MODE_PRIVATE);
        String[] city;
        city=allcitys.getString("city",null).split(" ");
        for (int i=0;i<city.length;i++){
            cy.add(city[i]);
        }
int[] weathericon={R.drawable.hot,R.drawable.sunny,R.drawable.sunnymorecloud,R.drawable.littlerain,R.drawable.cloudyrain};
        LayoutInflater inflater = getLayoutInflater();
        SharedPreferences preferences=getSharedPreferences("allcitys",MODE_PRIVATE);
        for(int i=0;i<cy.size();i++){
            View view=LayoutInflater.from(this).inflate(R.layout.weather_layout,null);
            ImageView imageView=(ImageView)view.findViewById(R.id.weather_icon);
            imageView.setImageResource(weathericon[i%5]);
            TextView wea_temp=(TextView)view.findViewById(R.id.wea_temp);
            wea_temp.setText(preferences.getString(cy.get(i),null));
            citys.add(view);
        }
        PagerAdapter pagerAdapter=new PagerAdapter() {
            @Override
            public int getCount() {
                return citys.size();
            }

            @Override
            //判断是否由对象生成界面
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }

            //使从ViewGroup中移出当前View
            public void destroyItem(View arg0, int arg1, Object arg2) {
                ((ViewPager) arg0).removeView(citys.get(arg1));
            }

            //返回一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
            public Object instantiateItem(View arg0, int arg1) {
                ((ViewPager) arg0).addView(citys.get(arg1));
                return citys.get(arg1);
            }
        };
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(0);
        //添加切换界面的监听器
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());




    }
//滑动页面时改变城市
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                case 0:
                    /**
                     * TranslateAnimation的四个属性分别为
                     * float fromXDelta 动画开始的点离当前View X坐标上的差值
                     * float toXDelta 动画结束的点离当前View X坐标上的差值
                     * float fromYDelta 动画开始的点离当前View Y坐标上的差值
                     * float toYDelta 动画开始的点离当前View Y坐标上的差值
                     **/
                    animation = new TranslateAnimation(one, 0, 0, 0);
                    break;
                case 1:
                    animation = new TranslateAnimation(offset, one, 0, 0);
                    break;
                case 2:
                    animation=new TranslateAnimation(2*offset,one,0,0);
                case 3:
                    animation=new TranslateAnimation(3*offset,one,0,0);
                    break;
                default:
                    animation=new TranslateAnimation(0,0,0,0);
            }
            //arg0为切换到的页的编码
            currIndex = arg0;

            // 将此属性设置为true可以使得图片停在动画结束时的位置
            animation.setFillAfter(true);
            //动画持续时间，单位为毫秒
            animation.setDuration(200);
            showcurrentcityname.setText(cy.get(currIndex).toString());
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    }
//右边菜单点击事件相应
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.AddCity:
                addcity();
                break;
            case R.id.DeleteCity:
deletecity();
                break;
            case R.id.QueryCity:

                break;
            case R.id.Calendar:

                break;

        }
        return false;
    }
    public void popupmenu(View v){
        PopupMenu popupMenu=new PopupMenu(this,v);
        MenuInflater menuInflater=popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.popupmenu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }
//添加城市
    public void addcity(){
        Intent intent=new Intent(this,AddCityFirst.class);
        startActivityForResult(intent,1);
    }

//返回添加城市结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String a=data.getStringExtra("省份");
        String b=data.getStringExtra("市");
//        String c=data.getStringExtra("地区");
        Toast.makeText(getApplicationContext(),"已添加"+a+b,Toast.LENGTH_SHORT).show();
addonecity(b);


    }
public void addonecity(String city){
    cy.add(city);
    StringBuffer allcity=new StringBuffer();
    for (int i=0;i<cy.size();i++){
        allcity.append(cy.get(i)+" ");
    }
    allcitys=getSharedPreferences("allcitys",MODE_PRIVATE);
    SharedPreferences.Editor editor=allcitys.edit();
    editor.putString("city",allcity.toString());
    editor.commit();

    LayoutInflater inflater = getLayoutInflater();
    final ArrayList<View> citys=new ArrayList<View>();
    int i=0;
    for(i=0;i<cy.size();i++){
        View view=LayoutInflater.from(this).inflate(R.layout.weather_layout,null);
        citys.add(view);
    }
    PagerAdapter pagerAdapter=new PagerAdapter() {
        @Override
        public int getCount() {
            return citys.size();
        }

        @Override
        //判断是否由对象生成界面
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        //使从ViewGroup中移出当前View
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(citys.get(arg1));
        }

        //返回一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(citys.get(arg1));
            return citys.get(arg1);
        }
    };
    ViewPager viewPager=(ViewPager)findViewById(R.id.showWeather);
    viewPager.setAdapter(pagerAdapter);
    viewPager.setCurrentItem(i-1);
}
public  void  deletecity(){
//    AlertDialog.Builder builder = new AlertDialog.Builder(this);
//    builder.setTitle("删除城市");
//    DialogInterface.OnClickListener listener =new DialogInterface.OnClickListener() {
//        @Override
//        public void onClick(DialogInterface dialog, int which) {
//            cy.remove(1);
//            dialog.dismiss();
//        }
//    };
// for (int i=0;i<cy.size();i++){
//     builder.setPositiveButton(cy.get(i),listener);
// }
//    builder.create().show();
}

}
