package com.example.nan.foryou;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static android.R.attr.path;

/**
 * Created by Nan on 2017/10/25.
 */

public class AssetsDatabaseManager {
//    private static String tag = "AssetsDatabase"; // for LogCat
//    private static String databasepath = "/data/data/%s/database"; // %s is packageName
//    // A mapping from assets database file to SQLiteDatabase object
//    private Map<String, SQLiteDatabase> databases = new HashMap<String, SQLiteDatabase>();
//    // Context of application
//    private Context context = null;
//    // Singleton Pattern
//    private static AssetsDatabaseManager mInstance = null;
//    /**
//     * Initialize com.example.nan.foryou.AssetsDatabaseManager
//     * @param context, context of application
//     */
//    public static void initManager(Context context){
//        if(mInstance == null){
//            mInstance = new AssetsDatabaseManager(context);
//        }
//    }
//    /**
//     * Get a com.example.nan.foryou.AssetsDatabaseManager object
//     * @return, if success return a com.example.nan.foryou.AssetsDatabaseManager object, else return null
//     */
//    public static AssetsDatabaseManager getManager(){
//        return mInstance;
//    }
//    private AssetsDatabaseManager(Context context){
//        this.context = context;
//    }
//    /**
//     * Get a assets database, if this database is opened this method is only return a copy of the opened database
//     * @param dbfile, the assets file which will be opened for a database
//     * @return, if success it return a SQLiteDatabase object else return null
//     */
//    public SQLiteDatabase getDatabase(String dbfile) {
//        if(databases.get(dbfile) != null){
//            Log.i(tag, String.format("Return a database copy of %s", dbfile));
//            return (SQLiteDatabase) databases.get(dbfile);
//        }
//        if(context==null)
//            return null;
//        Log.i(tag, String.format("Create database %s", dbfile));
//        String spath = getDatabaseFilepath();
//        String sfile = getDatabaseFile(dbfile);
//        File file = new File(sfile);
//        SharedPreferences dbs = context.getSharedPreferences(AssetsDatabaseManager.class.toString(), 0);
//        boolean flag = dbs.getBoolean(dbfile, false); // Get Database file flag, if true means this database file was copied and valid
//        if(!flag || !file.exists()){
//            file = new File(spath);
//            if(!file.exists() && !file.mkdirs()){
//                Log.i(tag, "Create \""+spath+"\" fail!");
//                return null;
//            }
//            if(!copyAssetsToFilesystem(dbfile, sfile)){
//                Log.i(tag, String.format("Copy %s to %s fail!", dbfile, sfile));
//                return null;
//            }
//            dbs.edit().putBoolean(dbfile, true).commit();
//        }
//        SQLiteDatabase db = SQLiteDatabase.openDatabase(sfile, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
//        if(db != null){
//            databases.put(dbfile, db);
//        }
//        return db;
//    }
//    private String getDatabaseFilepath(){
//        return String.format(databasepath, context.getApplicationInfo().packageName);
//    }
//    private String getDatabaseFile(String dbfile){
//        return getDatabaseFilepath()+"/"+dbfile;
//    }
//    private boolean copyAssetsToFilesystem(String assetsSrc, String des){
//        Log.i(tag, "Copy "+assetsSrc+" to "+des);
//        InputStream istream = null;
//        OutputStream ostream = null;
//        try{
//            AssetManager am = context.getAssets();
//            istream = am.open(assetsSrc);
//            ostream = new FileOutputStream(des);
//            byte[] buffer = new byte[1024];
//            int length;
//            while ((length = istream.read(buffer))>0){
//                ostream.write(buffer, 0, length);
//            }
//            istream.close();
//            ostream.close();
//        }
//        catch(Exception e){
//            e.printStackTrace();
//            try{
//                if(istream!=null)
//                    istream.close();
//                if(ostream!=null)
//                    ostream.close();
//            }
//            catch(Exception ee){
//                ee.printStackTrace();
//            }
//            return false;
//        }
//        return true;
//    }
//    /**
//     * Close assets database
//     * @param dbfile, the assets file which will be closed soon
//     * @return, the status of this operating
//     */
//    public boolean closeDatabase(String dbfile){
//        if(databases.get(dbfile) != null){
//            SQLiteDatabase db = (SQLiteDatabase) databases.get(dbfile);
//            db.close();
//            databases.remove(dbfile);
//            return true;
//        }
//        return false;
//    }
//    /**
//     * Close all assets database
//     */
//    static public void closeAllDatabase(){
//        Log.i(tag, "closeAllDatabase");
//        if(mInstance != null){
//            for(int i=0; i<mInstance.databases.size(); ++i){
//                if(mInstance.databases.get(i)!=null){
//                    mInstance.databases.get(i).close();
//                }
//            }
//            mInstance.databases.clear();
//        }
//    }

    AssetsDatabaseManager(String oldpath){
        filePath="data/data/com.example.nan.foryou/"+oldpath/*+"citydb.db"*/;
        pathStr="data/data/com.datab.cn";
    }

    //数据库存储路径    
            String filePath;
            //数据库存放的文件夹 data/data/com.main.jh 下面    
            String pathStr;
          SQLiteDatabase database;
            public SQLiteDatabase openDatabase(Context context){
        System.out.println("filePath:"+filePath);
        File jhPath=new File(filePath);
        //查看数据库文件是否存在    
        if(jhPath.exists()){
            Log.i("test","存在数据库");
            //存在则直接返回打开的数据库    
            return SQLiteDatabase.openOrCreateDatabase(jhPath,null);
            }else{
            //不存在先创建文件夹    
            File path=new File(pathStr);
            Log.i("test","pathStr="+path);
            if(path.mkdir()){
                Log.i("test","创建成功");
                }else{
                Log.i("test","创建失败");
               };
           try{
              //得到资源    
                AssetManager am=context.getAssets();
                //得到数据库的输入流    
                InputStream is=am.open("citydb.db");
                Log.i("test",is+"");
               //用输出流写到SDcard上面      
                FileOutputStream fos=new FileOutputStream(jhPath);
               Log.i("test","fos="+fos);
                Log.i("test","jhPath="+jhPath);
                //创建byte数组  用于1KB写一次    
                byte[] buffer=new byte[1024];
                int count=0;
                while((count=is.read(buffer))>0){
                    Log.i("test","得到");
                    fos.write(buffer,0,count);
                    }
                //最后关闭就可以了    
                fos.flush();
                fos.close();
                is.close();
                }catch (IOException e){
                // TODO Auto-generated catch block    
                e.printStackTrace();
                return null;
                }
            //如果没有这个数据库  我们已经把他写到SD卡上了，然后在执行一次这个方法 就可以返回数据库了    
            return openDatabase(context);
            }
        }

}
