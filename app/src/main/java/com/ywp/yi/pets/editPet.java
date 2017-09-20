package com.ywp.yi.pets;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import data.petContract.petEntry;

public class editPet extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    private EditText etNameEdit;
    private EditText etBreedEdit;
    private EditText etWeightEdit;
    private Spinner spinnerEdit;

    private Button btnCancle;
    private Button btnSave;

    private Uri currentPetUri;

    private int mGender = 0;

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

        Intent editIntent = getIntent();
        currentPetUri = editIntent.getData();

        spinnerEdit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                case petEntry.GENDER_MALE: {
                    spinnerEdit.setSelection(1);
                }
                break;
                case petEntry.GENDER_FEMALE: {
                    spinnerEdit.setSelection(2);
                }
                default:
                    spinnerEdit.setSelection(0);
                    break;
            }

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

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

    private void savePetInformation() {

        if (TextUtils.isEmpty(etNameEdit.getText())) {//名字输入是否为空
            Toast.makeText(this,"please input petName",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(etBreedEdit.getText())) {//品种输入是否为空
            Toast.makeText(this,"" +
                    "please input petBreed",Toast.LENGTH_SHORT).show();
            return;
        }
//        if (TextUtils.isEmpty(etWeightEdit.getText())){//判断输入的体重是否有效
//
//        }else {
//            int weight = Integer.getInteger(String.valueOf(etWeightEdit.getText()));
//            if (weight < 0){
//                Toast.makeText(this,"please input correct petWeight",Toast.LENGTH_SHORT).show();
//                return;
//            }
//        }

        ContentValues petValues = new ContentValues();
        //存入键值对
        petValues.put(petEntry.PET_NAME, String.valueOf(etNameEdit.getText()));
        petValues.put(petEntry.PET_BREED, String.valueOf(etBreedEdit.getText()));
        petValues.put(petEntry.PET_GENDER, mGender);
        petValues.put(petEntry.PET_WEIGHT, String.valueOf(etWeightEdit.getText()));

        Log.w("dataUriWeight","" + etWeightEdit.getText());

        getContentResolver().update(currentPetUri, petValues, null, null);

    }
}
