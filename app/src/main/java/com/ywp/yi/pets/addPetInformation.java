package com.ywp.yi.pets;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.view.WindowCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import data.petSQLite;

import static data.petContract.petEntry;

public class addPetInformation extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private Spinner addPetGenderSpinner;
    private Button addPetOkButton;
    private Button addPetCancleButton;
    private EditText addPetNameEditText;
    private EditText addPetBreedEditText;
    private EditText addPetMeaEditText;

    private int mGender = 0;
    private static SQLiteDatabase mPetData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet_information);

        //查找控件
        findAddViews();
        //给按键绑定监听器
        addPetOkButton.setOnClickListener(this);
        addPetCancleButton.setOnClickListener(this);
/**
 * 给数据和布局给spinner
 * 为spinner绑定监听器
 */
        ArrayAdapter addPetSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.genderArray,
                R.layout.support_simple_spinner_dropdown_item);
        addPetGenderSpinner.setAdapter(addPetSpinnerAdapter);

        addPetGenderSpinner.setOnItemSelectedListener(this);

        petSQLite mSQLite = new petSQLite(this);
        mPetData = mSQLite.getWritableDatabase();
    }

    /**
     * 查找控件
     */
    private void findAddViews() {
        addPetGenderSpinner = (Spinner) findViewById(R.id.addPetSpinner);

        addPetNameEditText = (EditText) findViewById(R.id.EtAddPetName);
        addPetBreedEditText = (EditText) findViewById(R.id.EtAddPetBreed);
        addPetMeaEditText = (EditText) findViewById(R.id.EtAddPetMeasurement);

        addPetOkButton = (Button) findViewById(R.id.btnAddOk);
        addPetCancleButton = (Button) findViewById(R.id.btnAddCancle);
//        addDeletePet = (Button) findViewById(R.id.addDelete);
    }

    /**
     * 读取数据库中的数据行数
     *
     * @return petLine
     */
    public int readPetTableLine() {
        //Cursor petCursor = mPetData.rawQuery("SELECT * FROM " + petEntry.TABLE_NAME, null);
        String[] projection = { //需要显示的行
                petEntry._ID,
                petEntry.PET_NAME,
                petEntry.PET_BREED,
                petEntry.PET_GENDER,
                petEntry.PET_WEIGHT
        };
        int petLine;
        Cursor mCursor = mPetData.query(petEntry.TABLE_NAME, projection, null, null, null, null, null);

        try {
            petLine = mCursor.getCount();
        } finally {
            mCursor.close();
        }

        return petLine;
    }

    /**
     * 往数据库中插入数据
     */
    private void insertPet() {
        ContentValues petDataValue = new ContentValues();
        petDataValue.put(petEntry.PET_NAME, String.valueOf(addPetNameEditText.getText()));
        petDataValue.put(petEntry.PET_BREED, String.valueOf(addPetBreedEditText.getText()));
        petDataValue.put(petEntry.PET_GENDER, addPetGenderSpinner.getBaseline());
        petDataValue.put(petEntry.PET_WEIGHT, String.valueOf(addPetMeaEditText.getText()));

        long rowId = mPetData.insert(petEntry.TABLE_NAME, null, petDataValue);
        if (rowId == -1) {
            Toast.makeText(this, "insert error", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "insert success", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 删除数据库中的一行数据
     */
    private void deletePetData() {
        mPetData.delete(petEntry.TABLE_NAME, "_ID=?", new String[]{"10"});
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String selectPosition = (String) adapterView.getItemAtPosition(i);
        if (!TextUtils.isEmpty(selectPosition)) {
            if (selectPosition.equals(getString(R.string.gender_female))) {
                Log.d("add", "female");
                mGender = petEntry.GENDER_FEMALE;
                //选中了female
            } else if (selectPosition.equals(R.string.gender_male)) {
                Log.d("add", "male");
                mGender = petEntry.GENDER_MALE;
                //选中了male
            } else {
                Log.d("add", "unknown");
                mGender = petEntry.GENDER_UNKNOWN;
                //选中了unknown
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        mGender = 0;
        //默认
    }

    /**
     * 按键监听
     *
     * @param view
     */
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnAddOk: {
                insertPet();//插入一组数据到数据库中
            }
            break;
            case R.id.btnAddCancle: {
                readPetTableLine();
                addPetNameEditText.setText("Table line " + readPetTableLine());
                //Log.d("add", "" + readPetTableLine());
                //deletePetData();//从数据库中删除一组数据
            }
            break;
            default:
                break;
        }
    }
}
