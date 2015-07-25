package com.skala.runloop_app;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.skala.runloop_app.sql.MembersSQLHelper;

/**
 * @author Skala
 */
public class TestDB extends AndroidTestCase {

    public void testCreateDB() throws Throwable {
        mContext.deleteDatabase(MembersSQLHelper.DATABASE_NAME);
        MembersSQLHelper membersSQLHelper = new MembersSQLHelper(mContext);
        SQLiteDatabase db = membersSQLHelper.getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }

    public void testInsertReadDB() {
        MembersSQLHelper membersSQLHelper = new MembersSQLHelper(mContext);
        SQLiteDatabase db = membersSQLHelper.getWritableDatabase();

        String testImageURL = "testImageURL";
        String testFullName = "testFullName";
        String testPosition = "testPosition";
        String testDescription = "testDescription";

        ContentValues values = new ContentValues();
        values.put(MembersSQLHelper.COLUMN_NAME_IMAGE_URL, testImageURL);
        values.put(MembersSQLHelper.COLUMN_NAME_FULLNAME, testFullName);
        values.put(MembersSQLHelper.COLUMN_NAME_POSITION, testPosition);
        values.put(MembersSQLHelper.COLUMN_NAME_DESCRIPTION, testDescription);

        long locationRowID = db.insert(MembersSQLHelper.TABLE_NAME, null, values);
        assertTrue(locationRowID != -1);

        Cursor cursor = db.query(MembersSQLHelper.TABLE_NAME,
                MembersSQLHelper.ALL_COLUMNS,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            String imageURL = cursor.getString(cursor.getColumnIndex(MembersSQLHelper.COLUMN_NAME_IMAGE_URL));
            String fullName = cursor.getString(cursor.getColumnIndex(MembersSQLHelper.COLUMN_NAME_FULLNAME));
            String position = cursor.getString(cursor.getColumnIndex(MembersSQLHelper.COLUMN_NAME_POSITION));
            String description = cursor.getString(cursor.getColumnIndex(MembersSQLHelper.COLUMN_NAME_DESCRIPTION));

            assertEquals(testImageURL, imageURL);
            assertEquals(testFullName, fullName);
            assertEquals(testPosition, position);
            assertEquals(testDescription, description);
        } else {
            fail("No values found");
        }

        cursor.close();

    }

}
