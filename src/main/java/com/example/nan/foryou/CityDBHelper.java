package com.example.nan.foryou;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Nan on 2017/10/25.
 */

public class CityDBHelper extends SQLiteOpenHelper{
    private Context mycontext;

    public CityDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public CityDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public String getDatabaseName() {
        return super.getDatabaseName();
    }

    @Override
    public void setWriteAheadLoggingEnabled(boolean enabled) {
        super.setWriteAheadLoggingEnabled(enabled);
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }

    private String DB_PATH=mycontext.getApplicationContext().getPackageName()+"/databases/";
    private static String DB_NAME="citydb.sqlite";//拓展名可以是.sqlite 或者是 .db
    public SQLiteDatabase myDataBase;
/*private String DB_PATH = "/data/data/"
                    + mycontext.getApplicationContext().getPackageName()
                    + "/databases/";*/

    public CityDBHelper(Context context) throws IOException{
        super(context,DB_NAME,null,1);
        this.mycontext=context;
        boolean dbexist=checkdatabase();//首先判断下db是不是存在 存在的话就直接使用了
        if(dbexist){
            //System.out.println("Database exists");
            opendatabase();//直接打开
            }else{
           // System.out.println("Database doesn't exist");
            createdatabase();//否则的话才去创建新的db
            }
    }

    public void createdatabase() throws IOException{
       boolean dbexist=checkdatabase();
        if(dbexist){
            //System.out.println(" Database exists.");
            }else{
            this.getReadableDatabase();
            try{
                copydatabase();
                }catch(IOException e){
                throw new Error("Error copying database");
                }
            }
    }

    private boolean checkdatabase(){
        //SQLiteDatabase checkdb = null;
        boolean checkdb=false;
        try{
            String myPath=DB_PATH+DB_NAME;
            File dbfile=new File(myPath);
            //checkdb = SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READWRITE);
            checkdb=dbfile.exists();
            }catch(SQLiteException e){
           //System.out.println("Database doesn't exist");
           }
       return checkdb;
    }

    private void copydatabase()throws IOException{
        //Open your local db as the input stream
        InputStream myinput=mycontext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outfilename=DB_PATH+DB_NAME;

        //Open the empty db as the output stream
        OutputStream myoutput=new FileOutputStream("/data/data/(packagename)/databases   /(datbasename).sqlite");

        // transfer byte to inputfile to outputfile
        byte[] buffer=new byte[1024];
        int length;
        while((length=myinput.read(buffer))>0){
            myoutput.write(buffer,0,length);
            }

        //Close the streams
        myoutput.flush();
        myoutput.close();
      myinput.close();
    }

    public void opendatabase()throws SQLException{
        //Open the database
        String mypath=DB_PATH+DB_NAME;
        myDataBase=SQLiteDatabase.openDatabase(mypath,null,SQLiteDatabase.OPEN_READWRITE);
    }

    public synchronized void close(){
        if(myDataBase!=null){
            myDataBase.close();
            }
        super.close();
    }
}
