package com.ywp.yi.pets;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

import data.petContract;
import data.petContract.petEntry;
import data.petSQLite;

public class petProvider extends ContentProvider {
    public petProvider() {
    }

    private static final int PETS = 1;
    private static final int PETS_ID = 2;

    private static UriMatcher petMatcher = new UriMatcher(UriMatcher.NO_MATCH);


    static {
        //整个表格
        petMatcher.addURI(petContract.CONTENT_AUTHORITY, petContract.PATH_PETS, PETS);
        //表格中单独的行
        petMatcher.addURI(petContract.CONTENT_AUTHORITY, petContract.PATH_PETS + "/#", PETS_ID);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(@NonNull Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        return false;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        petSQLite petProviderSQL = new petSQLite(this.getContext());
        //获取一个可读的数据库
        SQLiteDatabase queryPetDatabase = petProviderSQL.getReadableDatabase();
        Cursor queryPetCursor = null;
        int matchCode = petMatcher.match(uri);//获取用于查询的编码
        switch (matchCode) {
            case PETS: {
                queryPetCursor = queryPetDatabase.query(petEntry.TABLE_NAME,projection,
                        selection,selectionArgs,null,null,null);
            }
            break;
            case PETS_ID: {

            }
            break;
            default:
                // TODO: Implement this to handle query requests from clients.
                throw new UnsupportedOperationException("Not yet implemented");
        }
        return queryPetCursor;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
