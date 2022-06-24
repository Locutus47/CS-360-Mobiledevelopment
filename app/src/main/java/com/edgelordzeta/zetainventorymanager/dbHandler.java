package com.edgelordzeta.zetainventorymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import java.util.ArrayList;
import java.util.List;

public class dbHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "zetaInvDB.db";

    private static dbHandler zetaInvDB;

    public enum SubjectSortOrder { ALPHABETIC, UPDATE_DESC, UPDATE_ASC };

    public static dbHandler getInstance(Context context) {
        if (zetaInvDB == null) {
            zetaInvDB = new dbHandler(context);
        }
        return zetaInvDB;
    }

    private dbHandler(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }
//region Table classes
    private  final class userTable {
        private static final String TABLE = "users";
        private static final String userID = "User";
        private static final String password = "password";
    }

    private  final class itemTable {
        private static final String TABLE = "itemList";
        private static final String itemID = "_id";
        private static final String itemUPC = "UPC";
        private static final String itemCount = "Count";
        private static final String itemDesc = "Description";
    }
//endregion
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create user table
        db.execSQL("create table " + userTable.TABLE + " (" +
                userTable.userID + " primary key, " +
                userTable.password + "String)");

        // Create item tables
        db.execSQL("create table " + itemTable.TABLE + " (" +
                itemTable.itemID + " integer primary key autoincrement, " +
                itemTable.itemUPC + ", " +
                itemTable.itemCount + ", " +
                itemTable.itemDesc + "String)");

        // add the administrator account
        String[] users = { "Zeta" };
        for (String user1: users) {
            user user = new user(users);
            ContentValues values = new ContentValues();
            values.put(userTable.userID, user.getUser());
            values.put(userTable.password, "admin_password");
            db.insert(userTable.TABLE, null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + userTable.TABLE);
        db.execSQL("drop table if exists " + itemTable.TABLE);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                db.execSQL("pragma foreign_keys = on;");
            } else {
                db.setForeignKeyConstraintsEnabled(true);
            }
        }
    }


//region user database functions
    public boolean addUser(user userName) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(userTable.userID, userName.getUser());
        values.put(userTable.password, userName.getPassword());
        long id = db.insert(userTable.TABLE, null, values);
        return id != -1;
    }

    public void updateUser(user userName) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(userTable.userID, userName.getUser());
        values.put(userTable.password, userName.getPassword());
        db.update(userTable.TABLE, values,
                userTable.password + " = ?", new String[] { userName.getPassword() });
    }

    public void deleteUser(user userName) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(userTable.TABLE,
                userTable.userID + " = ?", new String[] { userName.getUser() });
    }

    public List<item> getItems() {
        List<item> items = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from " + itemTable.TABLE;
                ;
        Cursor cursor = db.rawQuery(sql,null);
        if (cursor.moveToFirst()) {
            do {
                item item = new item();
                item.setId(cursor.getInt(0));
                item.setUPC(cursor.getString(1));
                item.setCount(cursor.getInt(2));
                item.setDescription(cursor.getString(3));
                items.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return items;
    }
    //endregion
    public item getItem(long itemID) {
        item item = null;

        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from " + itemTable.TABLE +
                " where " + itemTable.itemID + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[] { Float.toString(itemID) });

        if (cursor.moveToFirst()) {
            item = new item();
            item.setId(cursor.getInt(0));
            item.setUPC(cursor.getString(1));
            item.setCount(cursor.getInt(2));
            item.setDescription(cursor.getString(3));
        }

        return item;
    }

    public void addQuestion(item item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(itemTable.itemUPC, item.getUPC());
        values.put(itemTable.itemCount, item.getCount());
        values.put(itemTable.itemDesc, item.getDescription());
        long questionId = db.insert(itemTable.TABLE, null, values);
        item.setId(questionId);


    }

    public void updateQuestion(item item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(itemTable.itemID, item.getId());
        values.put(itemTable.itemUPC, item.getUPC());
        values.put(itemTable.itemCount, item.getCount());
        values.put(itemTable.itemDesc, item.getDescription());
        db.update(itemTable.TABLE, values,
                itemTable.itemID + " = " + item.getId(), null);


    }

    public void deleteQuestion(long questionId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(itemTable.TABLE,
                itemTable.itemID + " = " + questionId, null);
    }
}