package com.ywp.yi.pets;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.zip.Inflater;

import Adapter.petCursorAdapter;
import data.petContract.petEntry;
import data.petSQLite;


public class MainActivity extends AppCompatActivity implements View.OnClickListener , LoaderManager.LoaderCallbacks<Cursor>{

//    Toolbar toolbar;
    ListView lvPets;
    FloatingActionButton fab;

    private static final int PET_LOADER_ID = 0;

    private petCursorAdapter cursorAdapter;

    private LoadStates myLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("main", "log test");

        findPetsView();
        //setSupportActionBar(toolbar);
        fab.setOnClickListener(this);//绑定监听器

        //将adapter 关联ListView
        cursorAdapter = new petCursorAdapter(this, null);
        lvPets.setAdapter(cursorAdapter);
        //这是因为setEmptyView这个方法的设置是有限制的，就是设置的View必需在当前的View hierarchy里，
        // 亦即这个View需要被add到当前View hierarchy的一个结点上，如果没有添加到结点上的话，
        // 调用setEmptyView(View v)是没有任何效果的
        LayoutInflater myInflater = getLayoutInflater();//获取Inflater
        ViewGroup emptyView = (ViewGroup) myInflater.inflate(R.layout.empty_layout,null);//指定布局
        emptyView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        emptyView.setVisibility(View.GONE);//设置可见性
        ViewGroup parentLayout = (ViewGroup) lvPets.getParent();

        parentLayout.addView(emptyView);//添加到父布局
        lvPets.setEmptyView(emptyView);


        // 初始化Loader
        getLoaderManager().initLoader(PET_LOADER_ID, null, this);// 加载器Id 可选参数 LoadCallBack的实现

//        mPetArrayData = new ArrayList<>();
//        petAdapter = new petsListAdapter(this, mPetArrayData);
       // lvPets.setAdapter(petAdapter);
        //ListView点击事件
        lvPets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {

                Toast.makeText(MainActivity.this,"" + id,Toast.LENGTH_SHORT).show();
                Intent editIntent = new Intent(MainActivity.this, editPet.class);
                Uri currentItemUri = ContentUris.withAppendedId(petEntry.CONTENT_URI,id);
                editIntent.setData(currentItemUri);
                Log.w("uri", "uri ->" + petEntry.CONTENT_URI + "/" + id );
                startActivity(editIntent);
            }
        });
        //ListView长按事件
        lvPets.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int itemPosition, final long id) {
                Toast.makeText(MainActivity.this, "" + id, Toast.LENGTH_SHORT).show();
                //新建一个对话框
                AlertDialog.Builder deleteDialog = new AlertDialog.Builder(MainActivity.this);
                deleteDialog.setIcon(R.mipmap.ic_launcher);//设置对话框图标
                deleteDialog.setTitle("是否删除该宠物信息");//设置对话框信息
                //设置对话框确定按键
                deleteDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteCurrentPet(id);
                    }
                });
                //设置对话框取消按键
                deleteDialog.setNegativeButton("取消", null);
                deleteDialog.show();
                return true;
            }
        });
    }

    /**
     * 删除当前长按的宠物选项
     * @param id
     */
    private void deleteCurrentPet(long id) {
//        petsList p = (petsList) adapterView.getItemAtPosition(petPosition);
//        String whereClause = "name = ? AND breed = ?";
//        String[] whereArgs = {p.getPetName(), p.getPetBreed()};
//
//        petSQLite mSQLite = new petSQLite(MainActivity.this);
//        petData = mSQLite.getReadableDatabase();
//        petData.delete(petEntry.TABLE_NAME, whereClause, whereArgs);
        Uri currentItemUri = ContentUris.withAppendedId(petEntry.CONTENT_URI,id);
        getContentResolver().delete(currentItemUri,null,null);
        Toast.makeText(MainActivity.this, "Delete success" + id, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, "restart", Toast.LENGTH_SHORT).show();
    }

    /**
     * 查找控件
     */
    private void findPetsView() {
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
        lvPets = (ListView) findViewById(R.id.lv_pets);
        fab = (FloatingActionButton) findViewById(R.id.fab);
    }


    /**
     * 菜单创建 , 给定布局
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * 菜单中的选项点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.actionDelete) {
            //删除所有数据
            deletePetList();
        }
        if (id == R.id.actionFresh) {
            Log.d("main", "about");
            //刷新数据
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 删除所有数据
     */
    private void deletePetList() {
        getContentResolver().delete(petEntry.CONTENT_URI,null,null);
        //        petSQLite mSQLite = new petSQLite(this);
//        petData = mSQLite.getReadableDatabase();
//        petData.delete(petEntry.TABLE_NAME, null, null);
    }

    /**
     * 添加宠物按键监听
     *
     * @param view
     */
    @Override
    public void onClick(View view) {

        Intent addIntent = new Intent(this, addPetInformation.class);
        startActivity(addIntent);
//        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {

        String[] projection = {
                petEntry._ID,
                petEntry.PET_NAME,
                petEntry.PET_BREED};

        return new CursorLoader(this,petEntry.CONTENT_URI,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //Swap in a new Cursor,
        // returning the old Cursor. Unlike changeCursor(Cursor),
        // the returned old Cursor is not closed.
        cursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);//设置对话框图标
        builder.setTitle("是否退出");//设置对话框信息
        //设置对话框确定按键
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        //设置对话框取消按键
        builder.setNegativeButton("取消", null);
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
