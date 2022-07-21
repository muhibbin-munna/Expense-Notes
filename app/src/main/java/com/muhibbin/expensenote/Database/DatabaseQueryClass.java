package com.muhibbin.expensenote.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import com.muhibbin.expensenote.Features.DetailsCRUD.CreateDetails.DetailNote;
import com.muhibbin.expensenote.Features.NotesCRUD.CreateNote.Note;
import com.muhibbin.expensenote.Util.Config;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DatabaseQueryClass {

    private Context context;

    public DatabaseQueryClass(Context context){
        this.context = context;
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public long insertNote(Note note){

        long id = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.NOTE_NAME, note.getName());


        try {
            id = sqLiteDatabase.insertOrThrow(Config.NOTE_TABLE, null, contentValues);
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return id;
    }

    public List<Note> getAllNote(){

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {

            cursor = sqLiteDatabase.query(Config.NOTE_TABLE, null, null, null, null, null, null, null);


            if(cursor!=null)
                if(cursor.moveToFirst()){
                    List<Note> noteList = new ArrayList<>();
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex(Config.NOTE_ID));
                        String name = cursor.getString(cursor.getColumnIndex(Config.NOTE_NAME));


                        noteList.add(new Note(id, name));
                    }   while (cursor.moveToNext());

                    return noteList;
                }
        } catch (Exception e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return Collections.emptyList();
    }

    public Note getNoteByRegNum(long registrationNum){

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        Note note = null;
        try {

            cursor = sqLiteDatabase.query(Config.NOTE_TABLE, null,
                    Config.NOTE_ID + " = ? ", new String[]{String.valueOf(registrationNum)},
                    null, null, null);


            if(cursor.moveToFirst()){
                int id = cursor.getInt(cursor.getColumnIndex(Config.NOTE_ID));
                String name = cursor.getString(cursor.getColumnIndex(Config.NOTE_NAME));

                note = new Note(id, name);
            }
        } catch (Exception e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return note;
    }

    public long updateNoteInfo(Note note){

        long rowCount = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.NOTE_NAME, note.getName());


        try {
            rowCount = sqLiteDatabase.update(Config.NOTE_TABLE, contentValues,
                    Config.NOTE_ID + " = ? ",
                    new String[] {String.valueOf(note.getId())});
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return rowCount;
    }

    public long deleteNoteByRegNum(long registrationNum) {
        long deletedRowCount = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            deletedRowCount = sqLiteDatabase.delete(Config.NOTE_TABLE,
                                    Config.NOTE_ID + " = ? ",
                                    new String[]{ String.valueOf(registrationNum)});
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return deletedRowCount;
    }

    public boolean deleteAllNotes(){
        boolean deleteStatus = false;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            //for "1" delete() method returns number of deleted rows
            //if you don't want row count just use delete(TABLE_NAME, null, null)
            sqLiteDatabase.delete(Config.NOTE_TABLE, null, null);

            long count = DatabaseUtils.queryNumEntries(sqLiteDatabase, Config.NOTE_TABLE);

            if(count==0)
                deleteStatus = true;

        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return deleteStatus;
    }

    public long getNumberOfNote(){
        long count = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            count = DatabaseUtils.queryNumEntries(sqLiteDatabase, Config.NOTE_TABLE);
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return count;
    }

    // subjects
    public long insertDetail(DetailNote detailNote, long idNo){
        long rowId = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.DETAIL_NAME, detailNote.getName());
        contentValues.put(Config.DETAIL_PRICE, detailNote.getPrice());
        contentValues.put(Config.COLUMN_FK_ID_NUMBER, idNo);
        contentValues.put(Config.DETAIL_ACTIVATED, detailNote.getActivated());

        try {
            rowId = sqLiteDatabase.insertOrThrow(Config.DETAIL_TABLE, null, contentValues);
        } catch (SQLiteException e){
            Logger.d(e);
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return rowId;
    }

    public DetailNote getDetailById(long detailId){
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        DetailNote detailNote = null;

        Cursor cursor = null;
        try{
            cursor = sqLiteDatabase.query(Config.DETAIL_TABLE, null,
                    Config.DETAIL_ID + " = ? ", new String[] {String.valueOf(detailId)},
                    null, null, null);

            if(cursor!=null && cursor.moveToFirst()){
                String detailName = cursor.getString(cursor.getColumnIndex(Config.DETAIL_NAME));
                int activated = cursor.getInt(cursor.getColumnIndex(Config.DETAIL_ACTIVATED));
                double detailPrice = cursor.getDouble(cursor.getColumnIndex(Config.DETAIL_PRICE));

                detailNote = new DetailNote(detailId, detailName, detailPrice, activated);
            }
        } catch (SQLiteException e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return detailNote;
    }

    public long updateSubjectInfo(DetailNote detailNote){

        long rowCount = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.DETAIL_NAME, detailNote.getName());
        contentValues.put(Config.DETAIL_ACTIVATED, detailNote.getActivated());
        contentValues.put(Config.DETAIL_PRICE, detailNote.getPrice());
        try {
            rowCount = sqLiteDatabase.update(Config.DETAIL_TABLE, contentValues,
                    Config.DETAIL_ID + " = ? ",
                    new String[] {String.valueOf(detailNote.getId())});
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return rowCount;
    }

    public List<DetailNote> getAllSubjectsByRegNo(long registrationNo){
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        List<DetailNote> detailNoteList = new ArrayList<>();
        Cursor cursor = null;
        try{
            cursor = sqLiteDatabase.query(Config.DETAIL_TABLE,
                    new String[] {Config.DETAIL_ID, Config.DETAIL_NAME,Config.DETAIL_ACTIVATED, Config.DETAIL_PRICE},
                    Config.COLUMN_FK_ID_NUMBER + " = ? ",
                    new String[] {String.valueOf(registrationNo)},
                    null, null, null);


            if(cursor!=null && cursor.moveToFirst()){
                detailNoteList = new ArrayList<>();
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(Config.DETAIL_ID));
                    String subjectName = cursor.getString(cursor.getColumnIndex(Config.DETAIL_NAME));
                    int activated = cursor.getInt(cursor.getColumnIndex(Config.DETAIL_ACTIVATED));
                    double subjectCredit = cursor.getDouble(cursor.getColumnIndex(Config.DETAIL_PRICE));

                    detailNoteList.add(new DetailNote(id, subjectName,  subjectCredit, activated));
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return detailNoteList;
    }

    public boolean deleteSubjectById(long subjectId) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        int row = sqLiteDatabase.delete(Config.DETAIL_TABLE,
                Config.DETAIL_ID + " = ? ", new String[]{String.valueOf(subjectId)});

        return row > 0;
    }

    public boolean deleteAllSubjectsByRegNum(long registrationNum) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        int row = sqLiteDatabase.delete(Config.DETAIL_TABLE,
                Config.COLUMN_FK_ID_NUMBER + " = ? ", new String[]{String.valueOf(registrationNum)});

        return row > 0;
    }

    public long getNumberOfSubject(){
        long count = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            count = DatabaseUtils.queryNumEntries(sqLiteDatabase, Config.DETAIL_TABLE);
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return count;
    }


}