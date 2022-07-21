package com.muhibbin.expensenote.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.muhibbin.expensenote.Util.Config;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper databaseHelper;

    // All Static variables
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = Config.DATABASE_NAME;

    // Constructor
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public static DatabaseHelper getInstance(Context context) {
        if(databaseHelper==null){
            synchronized (DatabaseHelper.class) {
                if(databaseHelper==null)
                    databaseHelper = new DatabaseHelper(context);
            }
        }
        return databaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create tables SQL execution
        String CREATE_Note_TABLE = "CREATE TABLE " + Config.NOTE_TABLE + "("
                + Config.NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Config.NOTE_NAME + " TEXT NOT NULL"+
                ")";

        String CREATE_DETAILS_TABLE = "CREATE TABLE " + Config.DETAIL_TABLE + "("
                + Config.DETAIL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Config.COLUMN_FK_ID_NUMBER + " INTEGER NOT NULL, "
                + Config.DETAIL_NAME + " TEXT NOT NULL, "
                + Config.DETAIL_ACTIVATED + " INTEGER NOT NULL, "
                + Config.DETAIL_PRICE + " REAL, " //nullable
                + "FOREIGN KEY (" + Config.COLUMN_FK_ID_NUMBER + ") REFERENCES " + Config.NOTE_TABLE + "(" + Config.NOTE_ID + ") ON UPDATE CASCADE ON DELETE CASCADE, "
                + "CONSTRAINT " + Config.NOTE_SUB_UNIQUE + " UNIQUE (" + Config.COLUMN_FK_ID_NUMBER + "," + Config.DETAIL_ID + ")"
                + ")";

        db.execSQL(CREATE_Note_TABLE);
        db.execSQL(CREATE_DETAILS_TABLE);

        Logger.d("DB created!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Config.NOTE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Config.DETAIL_TABLE);

        // Create tables again
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        //enable foreign key constraints like ON UPDATE CASCADE, ON DELETE CASCADE
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

}
