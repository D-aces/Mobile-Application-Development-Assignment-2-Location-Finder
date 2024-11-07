package com.example.locationfinder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static String databaseName = "LOCATIONS";
    private static int version = 1;

    public static String TABLE_TORONTO = "toronto_addresses";
    public static String COLUMN_FULL_ADDRESS = "full_address";
    public static String COLUMN_LATITUDE = "latitude";
    public static String COLUMN_LONGITUDE = "longitude";

    public DatabaseHandler(@Nullable Context context) {
        super(context, databaseName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE toronto_addresses (id INTEGER PRIMARY KEY AUTOINCREMENT, full_address TEXT, latitude NUMBER, longitude NUMBER);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    }

    public boolean queryAddress(){
        return false;
    }

    // Create operation
    public boolean newAddress(String full_address, double latitude, double longitude){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FULL_ADDRESS, full_address);
        values.put(COLUMN_LATITUDE, latitude);
        values.put(COLUMN_LONGITUDE, longitude);
        long result = db.insert(TABLE_TORONTO, null, values);
        Log.d("DatabaseHandler", "Insert result: " + result);
        db.close();
        return result != -1;
    }

    // Read operations
    public Location getLocation(String address){
        Location location = null;
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.rawQuery("SELECT * FROM toronto_addresses WHERE full_address LIKE ?", new String[]{address.toLowerCase()})) {
            if (cursor.moveToFirst()) {
                location = new Location(cursor.getInt(0),cursor.getString(1),cursor.getDouble(2), cursor.getDouble(3));
                cursor.close();
            }
            return location;
        }
    }
    public ArrayList<Double> getCoordinates(String address) {
        ArrayList<Double> coordinates = new ArrayList<Double>();
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.rawQuery("SELECT * FROM toronto_addresses WHERE full_address LIKE ?", new String[]{address.toLowerCase()})) {
            if (cursor.moveToFirst()) {
                coordinates.add(cursor.getDouble(2));
                coordinates.add(cursor.getDouble(3));
                cursor.close();
            }
            return coordinates;
        }
    }

    // Update operation
    public boolean updateLocation(Location location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FULL_ADDRESS, location.getFullAddress());
        contentValues.put(COLUMN_LATITUDE, location.getLatitude());
        contentValues.put(COLUMN_LONGITUDE, location.getLongitude());

        int rowsAffected = db.update(TABLE_TORONTO, contentValues, "id = ?", new String[]{String.valueOf(location.getId())});
        return rowsAffected > 0; // Returns true if at least one row was updated
    }

    public boolean deleteLocation(Location location){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.delete(TABLE_TORONTO, "id = ?", new String[]{String.valueOf(location.getId())}) > 0;
    }


}
