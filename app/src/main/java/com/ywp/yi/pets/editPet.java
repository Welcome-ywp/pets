package com.ywp.yi.pets;

import android.app.Dialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import data.petContract.petEntry;

public class editPet extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener, View.OnTouchListener {

    private EditText etNameEdit;
    private EditText etBreedEdit;
    private EditText etWeightEdit;
    private Spinner spinnerEdit;

    private Button btnCancle;
    private Button btnSave;

    private Uri currentPetUri;

    private int mGender = 0;
    private boolean isAnythingChange = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pet);

        etNameEdit = (EditText) findViewById(R.id.EtEditPetName);
        etBreedEdit = (EditText) findViewById(R.id.EtEditPetBreed);
        etWeightEdit = (EditText) findViewById(R.id.EtEditPet);

        spinnerEdit = (Spinner) findViewById(R.id.spinnerEditPet);

        btnCancle = (Button) findViewById(R.id.btnEditCancle);
        btnSave = (Button) findViewById(R.id.btnEditSave);

        btnCancle.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        etNameEdit.setOnTouchListener(this);
        etBreedEdit.setOnTouchListener(this);
        etWeightEdit.setOnTouchListener(this);
        spinnerEdit.setOnTouchListener(this);

        Intent editIntent = getIntent();
        currentPetUri = editIntent.getData();

        spinnerEdit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * 下拉栏选项监听
             * @param adapterView
             * @param view
             * @param i
             * @param l
             */
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectPosition = (String) adapterView.getItemAtPosition(i);//获取当前选中的值
                if (!TextUtils.isEmpty(selectPosition)) {
                    //Toast.makeText(this,selectPosition,Toast.LENGTH_SHORT).show();
                    if (selectPosition.equals(getString(R.string.gender_female))) {
                        Log.d("add", "female");
                        mGender = petEntry.GENDER_FEMALE;
                        //选中了female
                    } else if (selectPosition.equals(getString(R.string.gender_male))) {
                        Log.d("add", "male");
                        mGender = petEntry.GENDER_MALE;
                        //选中了male
                    } else if (selectPosition.equals(getString(R.string.gender_unknown))) {
                        Log.d("add", "unknown");
                        mGender = petEntry.GENDER_UNKNOWN;
                        //选中了unknown
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mGender = 0;
            }
        });
        //初始Loader
        getLoaderManager().initLoader(1, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                petEntry._ID,
                petEntry.PET_NAME,
                petEntry.PET_BREED,
                petEntry.PET_GENDER,
                petEntry.PET_WEIGHT
        };
        /**
         * 返回一个Cursor
         */
        return new CursorLoader(this, currentPetUri, projection, null, null, null);
    }

    /**
     * 处理读出的数据
     *
     * @param loader
     * @param cursor
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor.moveToFirst()) {
            /**
             * 读出cursor中的数据
             */
            int nameColumnIndex = cursor.getColumnIndex(petEntry.PET_NAME);
            int breedColumnIndex = cursor.getColumnIndex(petEntry.PET_BREED);
            int genderColumnIndex = cursor.getColumnIndex(petEntry.PET_GENDER);
            int weightColumnIndex = cursor.getColumnIndex(petEntry.PET_WEIGHT);

            String name = cursor.getString(nameColumnIndex);
            String breed = cursor.getString(breedColumnIndex);
            int gender = cursor.getInt(genderColumnIndex);
            int weight = cursor.getInt(weightColumnIndex);


            etNameEdit.setText(name);
            etBreedEdit.setText(breed);
            etWeightEdit.setText(Integer.toString(weight));

            switch (gender) {
                case petEntry.GENDER_UNKNOWN:
                    spinnerEdit.setSelection(0);
                    break;
                case petEntry.GENDER_MALE: {
                    spinnerEdit.setSelection(1);
                }
                break;
                case petEntry.GENDER_FEMALE: {
                    spinnerEdit.setSelection(2);
                }
                default:
                   // spinnerEdit.setSelection(0);
                    break;
            }

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    /**
     * 按键监听
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnEditCancle: {
                Toast.makeText(this,"pet information not update",Toast.LENGTH_SHORT).show();
                finish();
            }
            break;
            case R.id.btnEditSave: {
                savePetInformation();//保存更新数据
            }
            break;
            default:
                break;
        }
    }

    /**
     * 保存宠物信息
     */
    private void savePetInformation() {

        String petName = etNameEdit.getText().toString();
        String petBreed = etBreedEdit.getText().toString();
        String weight = etWeightEdit.getText().toString();
        int petWeight = 0;

        if (TextUtils.isEmpty(petName)) {//名字输入是否为空
            Toast.makeText(this,"please input petName",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(petBreed)) {//品种输入是否为空
            Toast.makeText(this,"please input petBreed",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.isEmpty(weight)){//判断输入的体重是否有效
            petWeight = Integer.parseInt(weight);
            //Toast.makeText(this,"please input correct petWeight",Toast.LENGTH_SHORT).show();
            //return;
        }

        ContentValues petValues = new ContentValues();
        //存入键值对
        petValues.put(petEntry.PET_NAME, petName);
        petValues.put(petEntry.PET_BREED, petBreed);
        petValues.put(petEntry.PET_GENDER, mGender);
        petValues.put(petEntry.PET_WEIGHT, petWeight);

        Log.w("dataUriWeight","" + etWeightEdit.getText());

        getContentResolver().update(currentPetUri, petValues, null, null);
        isAnythingChange = false;
        Toast.makeText(this,"update success",Toast.LENGTH_SHORT).show();

    }
    /**
     * 监听是否产生修改
     * @param view
     * @param motionEvent
     * @return
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        isAnythingChange = true;
        return false;
    }

    /**
     * 返回按键按下
     */
    @Override
    public void onBackPressed() {
        if (!isAnythingChange){//没有产生修改
            super.onBackPressed();//返回
            return;
        }
        /**
         * 有修改
         */
        Dialog.OnClickListener listener = new Dialog.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        };

        showUnChangeDialog(listener);
    }

    /**
     * 监听返回上一级
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :{
                if (!isAnythingChange){//没有产生修改
                    NavUtils.navigateUpFromSameTask(editPet.this);
                    return true;
                }
                /**
                 * 有修改
                 */
                Dialog.OnClickListener listener = new Dialog.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NavUtils.navigateUpFromSameTask(editPet.this);
                    }
                };

                showUnChangeDialog(listener);
                return true;

            }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showUnChangeDialog(DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("是否放弃修改");
        builder.setPositiveButton("确认",listener);//确认按键
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {//取消按键
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialogInterface != null){
                    dialogInterface.dismiss();
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
