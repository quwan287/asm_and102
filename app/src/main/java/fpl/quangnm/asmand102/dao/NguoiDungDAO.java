package fpl.quangnm.asmand102.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import fpl.quangnm.asmand102.LogIn_Screen;
import fpl.quangnm.asmand102.data.DbHelper;

public class NguoiDungDAO {
    private DbHelper dbHelper;

    public NguoiDungDAO(Context context) {
        this.dbHelper = dbHelper;
    }
    public boolean CheckLogin(String username, String password){
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM nguoidung WHERE tendangnhap = ? AND matkhau = ?",
                                                     new String[]{username,password});
        return cursor.getCount() > 0;
    }

    public boolean Register(String username, String password, String hoTen){
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("tendangnhap",username);
        values.put("matkhau",password);
        values.put("hoten",hoTen);

        long check = sqLiteDatabase.insert("nguoidung",null,values);
        return check != -1;
    }
}
