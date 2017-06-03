package com.cs330.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Filip on 03-Jun-17.
 */

public class DbAdapter {

    static final String KEY_ROWID = "id";
    static final String KEY_NAME = "name";
    static final String TAG = "DbAdapter";

    static final String DATABASE_NAME = "mydb";
    static final String DATABASE_TABLE = "barcode";
    static final int DATABASE_VERSION = 3;

    static final String DATABASE_CREATE = "create table barcode (id integer primary key autoincrement, " + "name text not null);";

    final Context context;

    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DbAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Ažuriranje verzije baze podataka sa " + oldVersion + " na verziju "
                    + newVersion + ", a to će uništiti postojeće podatke");
            db.execSQL("DROP TABLE IF EXISTS barcode");
            onCreate(db);
        }
    }

    public DbAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        DBHelper.close();
    }

    public long insertBarcode(String barcode) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, barcode);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    public Cursor getAllBarcodes() {
        return db.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_NAME}, null, null, null, null, null);
    }


}
