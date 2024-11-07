package com.example.locationfinder;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.database.sqlite.SQLiteDatabase;

public class DataImport {
    private static final String PREFS_NAME = "importPrefs";
    private static final String KEY_IMPORT_DONE = "csvImportDone";
    private final DatabaseHandler databaseHandler;
    private final Context context;

    public DataImport(Context context) {
        this.context = context;
        this.databaseHandler = new DatabaseHandler(context);
    }

    public void importCsv(String csvFileName) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean isImportDone = prefs.getBoolean(KEY_IMPORT_DONE, false);

        if (isImportDone) {
            Log.i("DataImport", "CSV data has already been imported.");
            return;
        }

        SQLiteDatabase db = databaseHandler.getWritableDatabase();
        int entryCount = 0;

        try (InputStream is = context.getAssets().open(csvFileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            db.beginTransaction();
            String line;

            reader.readLine();

            while ((line = reader.readLine()) != null && entryCount < 200) {
                String[] columns = line.split(",");

                String fullAddress = columns[1].trim();
                String longitude = columns[2].trim();
                String latitude = columns[3].trim();

                ContentValues values = new ContentValues();
                values.put(DatabaseHandler.COLUMN_FULL_ADDRESS, fullAddress);
                values.put(DatabaseHandler.COLUMN_LATITUDE, latitude);
                values.put(DatabaseHandler.COLUMN_LONGITUDE, longitude);

                db.insert(DatabaseHandler.TABLE_TORONTO, null, values);
                entryCount++;
            }

            db.setTransactionSuccessful();
            Log.i("CsvImporter", "Data imported successfully. Total entries: " + entryCount);

            prefs.edit().putBoolean(KEY_IMPORT_DONE, true).apply();

        } catch (Exception e) {
            Log.e("CsvImporter", "Error importing CSV", e);
        } finally {
            db.endTransaction();
        }
    }
}
