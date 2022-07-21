package com.muhibbin.expensenote.Util;

public class Config {

    public static final String DATABASE_NAME = "note-db";

    //column names of note table
    public static final String NOTE_TABLE = "note";
    public static final String NOTE_ID = "_id";
    public static final String NOTE_NAME = "name";


    //column names of subject table
    public static final String DETAIL_TABLE = "detail";
    public static final String DETAIL_ID = "_id";
    public static final String COLUMN_FK_ID_NUMBER = "fk_id_no";
    public static final String DETAIL_NAME = "item_name";
    public static final String DETAIL_ACTIVATED = "activate";
    public static final String DETAIL_PRICE = "price";
    public static final String NOTE_SUB_UNIQUE = "note_sub_unique";

    //others for general purpose key-value pair data
    public static final String TITLE = "title";
    public static final String CREATE_NOTE = "create_note";
    public static final String UPDATE_NOTE = "update_note";
    public static final String CREATE_DETAIL = "create_detail";
    public static final String UPDATE_DETAIL = "update_detail";
    public static final String NOTE_ID1 = "note_id";
    public static final String NOTE_NAME1 = "note_name";
}
