package com.ywp.yi.pets;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.IntentFilter;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.util.Log;

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

        petSQLite updatePetDatabase = new petSQLite(this.getContext());
        SQLiteDatabase petDeleteDatabase = updatePetDatabase.getWritableDatabase();
        int match = petMatcher.match(uri);
        switch (match){
            case PETS :{
                int deleteRow = petDeleteDatabase.delete(petEntry.TABLE_NAME,selection,selectionArgs);
                getContext().getContentResolver().notifyChange(uri,null);
                return deleteRow;

            }
            case PETS_ID :{
                selection = petEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                int deleteRow = petDeleteDatabase.delete(petEntry.TABLE_NAME,selection,selectionArgs);

                getContext().getContentResolver().notifyChange(uri,null);

                return deleteRow;
            }
            default:
                // Implement this to handle requests to delete one or more rows.
                throw new UnsupportedOperationException("Not yet implemented");

        }

    }

    @Override
    public String getType(@NonNull Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        int match = petMatcher.match(uri);
        switch (match) {
            case PETS:
                return insertPet(uri, values);
            default:
                // TODO: Implement this to handle requests to insert a new row.
                throw new UnsupportedOperationException("Not yet implemented");
        }

    }

    /**
     * 判断输入的宠物性别是有效
     *
     * @param gender
     * @return
     */
    private boolean isGenderValid(int gender) {
        if (gender == petEntry.GENDER_FEMALE || gender == petEntry.GENDER_MALE || gender == petEntry.GENDER_UNKNOWN) {
            return true;
        }
        return false;
    }

    /**
     * 插入新宠物数据
     *
     * @param uri
     * @param values
     * @return id
     */

    @Nullable
    private Uri insertPet(Uri uri, ContentValues values) {
        //添加的宠物信息 ,名字是否为空
        if ((values.getAsString(petEntry.PET_NAME).isEmpty())) {
            throw new IllegalArgumentException("pet name can not null");
        }
        //添加 的宠物信息 品种 是否为空 , 或者有效值
        if (!isGenderValid(values.getAsInteger(petEntry.PET_GENDER))) {
            throw new IllegalArgumentException("gender error");
        }
        //添加的宠物信息 体重 是否小于0
        Integer weight = values.getAsInteger(petEntry.PET_WEIGHT);
        if (weight != null && weight < 0) {
            throw new IllegalArgumentException("weight error");
        }

        petSQLite petProviderSQL = new petSQLite(this.getContext());
        //获取一个可读的数据库
        SQLiteDatabase insertPetDatabase = petProviderSQL.getWritableDatabase();
        long newPetId = insertPetDatabase.insert(petEntry.TABLE_NAME, null, values);
        if (newPetId == -1) {
            Log.d("provider", "insertPet: error " + uri);
            return null;
        } else {
            Log.d("provider", "insertPet: success " + uri);
            System.out.print("insertPet: success " + uri);
        }
        Uri insertUri =  ContentUris.withAppendedId(uri, newPetId);
        getContext().getContentResolver().notifyChange(uri,null);
        Log.w("notify", "insertPet: ");
        //返回uri/id
        return insertUri;
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
                queryPetCursor = queryPetDatabase.query(petEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, null);
                queryPetCursor.setNotificationUri(getContext().getContentResolver(),uri);
                return queryPetCursor;
            }
            case PETS_ID: {
                selection = petEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                queryPetCursor = queryPetDatabase.query(petEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, null);
                queryPetCursor.setNotificationUri(getContext().getContentResolver(),uri);
                return queryPetCursor;
            }
            default:

                // TODO: Implement this to handle query requests from clients.
                throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    /**
     * 输入完整性检验
     * @param uri
     * @param values
     * @param selection
     * @param selectionArgs
     * @return
     */
    private int checkPetValueValid(Uri uri, ContentValues values, String selection,
                                   String[] selectionArgs){
        if (values.containsKey(petEntry.PET_NAME)){//判断输入时候含有 PET_NAME 键值
            String petName = values.getAsString(petEntry.PET_NAME);
            if (petName == null){//名字是否为空

            }
        }
        if (values.containsKey(petEntry.PET_GENDER)){//判断输入时候含有 PET_GENDER 键值
            Integer petGender = values.getAsInteger(petEntry.PET_GENDER);
            if (petGender == null){//品种是否为空

            }

        }
        if (values.containsKey(petEntry.PET_WEIGHT)){//判断输入时候含有 PET_WEIGHT 键值
            Integer petWeight = values.getAsInteger(petEntry.PET_WEIGHT);
            if (petWeight != null && petWeight < 0){//重量输入错误

            }
        }

        petSQLite petProviderSQL = new petSQLite(this.getContext());
        //获取一个可写的数据库
        SQLiteDatabase updatePetDatabase = petProviderSQL.getWritableDatabase();
        int updateRow = updatePetDatabase.update(petEntry.TABLE_NAME,values,selection,selectionArgs);

        getContext().getContentResolver().notifyChange(uri,null);
        return updateRow;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {


        int matchCode = petMatcher.match(uri);//获取用于查询的编码

        switch (matchCode){
            case PETS :{
                int updateRow = checkPetValueValid(uri,values,selection,selectionArgs);
                getContext().getContentResolver().notifyChange(uri,null);
                return updateRow;
            }
            case PETS_ID :{
                selection = petEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                int updateRow = checkPetValueValid(uri,values,selection,selectionArgs);

                getContext().getContentResolver().notifyChange(uri,null);
                return updateRow;
            }
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }

        // TODO: Implement this to handle requests to update one or more rows.
    }
}
