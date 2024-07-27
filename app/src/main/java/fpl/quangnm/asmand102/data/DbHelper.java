package fpl.quangnm.asmand102.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "steps";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_STEP_COUNT = "step_count";
    private static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String DB_NAME = "AND102";
    public static final int DB_VERSION = 1;
    public DbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String NguoiDung = "CREATE TABLE nguoidung (\n" +
                "  tendangnhap TEXT PRIMARY KEY,\n" +
                "  matkhau TEXT ,\n" +
                "  hoten TEXT\n" +
                ");";
        sqLiteDatabase.execSQL(NguoiDung);
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_STEP_COUNT + " INTEGER, " +
                COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP)";
        sqLiteDatabase.execSQL(CREATE_TABLE);
        String bietOn = "CREATE TABLE todos (\n" +
                "  id integer PRIMARY KEY AUTOINCREMENT,\n" +
                "  title text,\n" +
                "  content text,\n" +
                "  dates text,\n" +
                "  type text,\n" +
                "  status integer\n" +
                ");";
        sqLiteDatabase.execSQL(bietOn);

        String nNguoiDung = "INSERT INTO nguoidung VALUES('quangnm','123','Minh Quang')";
        sqLiteDatabase.execSQL(nNguoiDung);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS todos");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS steps");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS nguoidung");
        onCreate(sqLiteDatabase);
    }
    public void insertStepCount(int stepCount) {
        // thêm dữ liệu vào bảng
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STEP_COUNT, stepCount);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    public void resetTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME);
        db.execSQL("delete from sqlite_sequence where name='" + TABLE_NAME + "' ");
    }
    public List<String> getAllSteps() {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c =  db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        Log.d("zzzzz", "getAllSteps: " + c.getCount());
        if(c!= null && c.getCount()>0){
            c.moveToFirst();
            do {
                int id = c.getInt(0);
                int step = c.getInt(1);
                String time = c.getString(2);
                list.add( id + "|" + step + "|" + time);
            }while (c.moveToNext());
        }
        return list;
    }
}
