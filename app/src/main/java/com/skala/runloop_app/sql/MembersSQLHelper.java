package com.skala.runloop_app.sql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.skala.runloop_app.BuildConfig;
import com.skala.runloop_app.models.MemberModel;

import java.util.ArrayList;

/**
 * @author Skala
 */
public class MembersSQLHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Members.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "members";
    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_IMAGE_URL = "image_url";
    private static final String COLUMN_NAME_FULLNAME = "fullname";
    private static final String COLUMN_NAME_POSITION = "position";
    private static final String COLUMN_NAME_DESCRIPTION = "description";

    private String[] ALL_COLUMNS = {COLUMN_NAME_ID, COLUMN_NAME_IMAGE_URL, COLUMN_NAME_FULLNAME,
            COLUMN_NAME_POSITION, COLUMN_NAME_DESCRIPTION};

    private static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
            COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME_IMAGE_URL + " TEXT, " +
            COLUMN_NAME_FULLNAME + " TEXT, " +
            COLUMN_NAME_POSITION + " TEXT, " +
            COLUMN_NAME_DESCRIPTION + " TEXT )";


    public MembersSQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addMembers(ArrayList<MemberModel> memberList) {
        SQLiteDatabase database = getWritableDatabase();

        database.delete(TABLE_NAME, null, null); // delete old data

        if (BuildConfig.DEBUG) {
            addFakeMembers(memberList); // add more members available only in debug mode
        }

        StringBuilder command;
        command = new StringBuilder("INSERT INTO ");
        command.append(TABLE_NAME);
        command.append(" (");
        command.append(COLUMN_NAME_IMAGE_URL);
        command.append(", ");
        command.append(COLUMN_NAME_FULLNAME);
        command.append(", ");
        command.append(COLUMN_NAME_POSITION);
        command.append(", ");
        command.append(COLUMN_NAME_DESCRIPTION);
        command.append(") VALUES (?, ?, ?, ?)");

        SQLiteStatement insStmt = database.compileStatement(command.toString());
        database.beginTransaction();

        int size = memberList.size();
        for (int i = 0; i < size; i++) {
            MemberModel memberModel = memberList.get(i);

            insStmt.bindString(1, memberModel.getImageURL());
            insStmt.bindString(2, memberModel.getFullName());
            insStmt.bindString(3, memberModel.getPosition());
            insStmt.bindString(4, memberModel.getDescription());
            insStmt.executeInsert();
        }
        database.setTransactionSuccessful();
        database.endTransaction();

        database.close();
    }

    public ArrayList<MemberModel> getAllMembers() {
        SQLiteDatabase database = getReadableDatabase();

        Cursor cursor = database.query(TABLE_NAME, ALL_COLUMNS, null, null, null, null, null);

        if (cursor.getCount() == 0) {
            cursor.close();
            database.close();
            return null;
        }

        ArrayList<MemberModel> memberList = new ArrayList<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String imageURL = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_IMAGE_URL));
            String fullName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_FULLNAME));
            String position = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_POSITION));
            String description = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DESCRIPTION));

            MemberModel memberModel = new MemberModel(imageURL, fullName, position, description);
            memberList.add(memberModel);

            cursor.moveToNext();
        }

        cursor.close();
        database.close();

        return memberList;
    }

    public void addFakeMembers(ArrayList<MemberModel> memberList) {
        int sizeMember = memberList.size();
        if (sizeMember == 0) { // we can't copy URL to mock-up views
            return;
        }

        int count = 0; // count need to change avatars
        for (int i = 0; i < 10000; i++) { // we need members more than 10.000
            MemberModel memberModel = new MemberModel(memberList.get(count).getImageURL(), String.valueOf(i), String.valueOf(i), String.valueOf(i)); // we need create new model
            memberList.add(memberModel);

            count++;
            count %= sizeMember;
        }
    }

}
