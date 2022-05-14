package com.example.mycellar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "MyCellar.db";
    private static final String TABLE_NAME = "Wines";
    private static final String COL_ID = "ID";
    private static final String COL_NAME = "Name";
    private static final String COL_PRODUCER = "Producer";
    private static final String COL_RATING = "Rating";
    private static final String COL_PICTURE = "Picture";
    private static final String COL_TYPE = "Type";
    private static final String COL_IsTasted = "IsTasted";

    DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_PRODUCER + " TEXT, " +
                COL_RATING + " TEXT, " +
                COL_TYPE + " TEXT, " +
                COL_PICTURE + " TEXT, " +
                COL_IsTasted + " NUMBER);";

        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("Drop table if exists " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void insertData(String name, String producer, String rating, String type, String picturePath, int isTasted){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_NAME, name);
        contentValues.put(COL_PRODUCER, producer);
        contentValues.put(COL_RATING, rating);
        contentValues.put(COL_TYPE, type);
        contentValues.put(COL_PICTURE, picturePath);
        contentValues.put(COL_IsTasted, isTasted);
        long result = database.insert(TABLE_NAME, null, contentValues);
        //Application failed to insert
        if(result == -1){
            Toast.makeText(context, "Failed to insert data", Toast.LENGTH_LONG).show();
        } else{
            Toast.makeText(context, "Data inserted successfully", Toast.LENGTH_LONG).show();
        }

    }

    public Cursor getALlData() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = null;
        if(database != null){
            cursor = database.rawQuery(query, null);
        }
        return cursor;
    }

    void updateData (String rowId, String name, String producer, String rating, String type, String picture) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME, name);
        contentValues.put(COL_PRODUCER, producer);
        contentValues.put(COL_RATING, rating);
        contentValues.put(COL_TYPE, type);
        contentValues.put(COL_PICTURE, picture);
        long result = database.update(TABLE_NAME, contentValues, "ID = ?", new String[]{rowId});
        //Application failed to updated
         if(result == -1){
            Toast.makeText(context, "Failed to update data", Toast.LENGTH_LONG).show();
        } else{
            Toast.makeText(context, "Data updated successfully", Toast.LENGTH_LONG).show();
        }
    }

    void deleteSingleRow (String rowId) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "ID = ?", new String[] {rowId});
        if(result == -1){
            Toast.makeText(context, "Failed to delete data", Toast.LENGTH_LONG).show();
        } else{
            Toast.makeText(context, "Data deleted successfully", Toast.LENGTH_LONG).show();
        }
    }

    // Sort the wine list by the selected value
    public Cursor sortByAndFilterBy(String sortByValue, String filterByValue, String collectionType) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sortBySQL = COL_NAME + " ASC";
        int isTasted = collectionType.equals("tasted") ? 1 : 0;

        Cursor res = null;
        switch (sortByValue) {
            case "Wine Name A -> Z":
                sortBySQL = COL_NAME + " ASC";
                break;
            case "Wine Name Z -> A":
                sortBySQL = COL_NAME + " DESC";
                break;
            case "Producer Name A -> Z":
                sortBySQL = COL_PRODUCER + " ASC";
                break;
            case "Producer Name Z -> A":
                sortBySQL = COL_PRODUCER + " DESC";
                break;
            case "Rating Low -> High":
                sortBySQL = COL_RATING + " ASC";
                break;
            case "Rating High -> Low":
                sortBySQL = COL_RATING + " DESC";
                break;
        }

        // Need to implement Type first:
        if(filterByValue.equals("Show All Wines")){
            res = db.rawQuery("select * from " + TABLE_NAME
                    + " where " + COL_IsTasted + " = " + isTasted
                    + " order by " + sortBySQL, null);
        }
        else{
            String filterBySQL = "Red";
            if(filterByValue.equals("Show Red Wines")){
                filterBySQL = "Red";
            }
            if(filterByValue.equals("Show White Wines")){
                filterBySQL = "White";
            }

            res = db.rawQuery("select * from " + TABLE_NAME
                    + " where " + COL_IsTasted + " = " + isTasted
                    + " and " + COL_TYPE + " like '" + filterBySQL
                    + "' order by " + sortBySQL, null);
        }

        return res;
    }

    void moveToTasted (String wineId) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_IsTasted, 1);
        long result = database.update(TABLE_NAME, contentValues, "ID = ?", new String[]{wineId});
        //Application failed to updated
        if(result == -1){
            Toast.makeText(context, "Failed to move to Tasted Collection", Toast.LENGTH_LONG).show();
        } else{
            Toast.makeText(context, "Wine moved to Tasted Collection successfully", Toast.LENGTH_LONG).show();
        }
    }
}


