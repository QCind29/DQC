package com.food.lite.nckh.detection.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.food.lite.nckh.detection.model.Category;
import com.food.lite.nckh.detection.model.Vob;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

public class CateDB extends SQLiteAssetHelper {

    private static final String DB_NAME = "cate3.db";
    private static final int DB_VERSION = 1;


    public CateDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public ArrayList<Category> getAll() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from Category ", null);
        ArrayList<Category> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Category cate = new Category();
                cate.setCateID(cursor.getInt(cursor.getColumnIndexOrThrow("cateID")));
                cate.setLabelID(cursor.getInt(cursor.getColumnIndexOrThrow("labelID")));
                cate.setCateName(cursor.getString(cursor.getColumnIndexOrThrow("cateName")));
                cate.setCateType(cursor.getString(cursor.getColumnIndexOrThrow("cateType")));
                cate.setCateUnit(cursor.getString(cursor.getColumnIndexOrThrow("cateUnit")));
                cate.setCateCost(cursor.getInt(cursor.getColumnIndexOrThrow("cateCost")));
                cate.setCateDescription(cursor.getString(cursor.getColumnIndexOrThrow("cateDesciption")));
                cate.setCateImg(cursor.getBlob(cursor.getColumnIndexOrThrow("cateImg")));


                result.add(cate);
            } while (cursor.moveToNext());

        }
        return result;

    };

    public void addItem(String cateName,Integer labelID,String cateType, String cateUnit,Integer cost,byte[] item) {
        try {
            SQLiteDatabase db = getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("cateName", cateName);
            values.put("cateType", cateType);
            values.put("cateUnit", cateUnit);
            values.put("cateCost", cost);
            values.put("cateImg", item);
            values.put("labelID",labelID);

            db.insert("Category", null, values);//Items is table name
            db.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public ArrayList<Category> getCate (Integer id) {

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT cateName, cateType, cateUnit, cateCost, cateImg, labelID from Category  where labelID = ? ", new String[]{id + ""},null);


        ArrayList<Category> result = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                Category cate = new Category();
                cate.setCateName(cursor.getString(cursor.getColumnIndexOrThrow("cateName")));
                cate.setCateCost(cursor.getInt(cursor.getColumnIndexOrThrow("cateCost")));
                cate.setCateUnit(cursor.getString(cursor.getColumnIndexOrThrow("cateUnit")));
                cate.setCateType(cursor.getString(cursor.getColumnIndexOrThrow("cateType")));
                cate.setCateImg(cursor.getBlob(cursor.getColumnIndexOrThrow("cateImg")));
                cate.setLabelID(cursor.getInt(cursor.getColumnIndexOrThrow("labelID")));




                result.add(cate);
            } while (cursor.moveToNext());
        }
//            cursor.close();
        return result;
    }
    public String getCateName (Integer id) {

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT cateName from Category  where labelID = ? ", new String[]{id + ""},null);


        String name = null ;

        if (cursor.moveToFirst()) {
            do {
               int nameIndex = cursor.getColumnIndexOrThrow("cateName");

                name = cursor.getString(nameIndex);
            } while (cursor.moveToNext());
        }
//            cursor.close();
        return name;
    }

}
