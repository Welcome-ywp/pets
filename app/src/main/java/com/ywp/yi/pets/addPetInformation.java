package com.ywp.yi.pets;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import static data.petContract.petEntry;

public class addPetInformation extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, View.OnTouchListener {

    private Spinner addPetGenderSpinner;
    private Button addPetOkButton;
    private Button addPetCancleButton;
    private EditText addPetNameEditText;
    private EditText addPetBreedEditText;
    private EditText addPetWeightEditText;

    private static int mGender = 0;

    private boolean isValueChange = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet_information);

        //查找控件
        findAddViews();
        //给按键绑定监听器
        addPetOkButton.setOnClickListener(this);
        addPetCancleButton.setOnClickListener(this);
        //监听是否有数据更改
        addPetNameEditText.setOnTouchListener(this);
        addPetBreedEditText.setOnTouchListener(this);
        addPetWeightEditText.setOnTouchListener(this);
/**
 * 给数据和布局给spinner
 * 为spinner绑定监听器
 */
        ArrayAdapter addPetSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.genderArray,
                R.layout.support_simple_spinner_dropdown_item);
        addPetGenderSpinner.setAdapter(addPetSpinnerAdapter);

        addPetGenderSpinner.setOnItemSelectedListener(this);

    }
    /**
     * 返回按键按下
     */
    @Override
    public void onBackPressed() {
        if (!isValueChange){//没有产生修改
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

        showValueChangeDialog(listener);
    }
    /**
     * 左上角返回被点击事件监听
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){//左上角返回被点击
            if (!isValueChange){//没有修改
                NavUtils.navigateUpFromSameTask(addPetInformation.this);
                return true;
            }
            /**
             * 有修改
             */
            Dialog.OnClickListener listener = new Dialog.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    NavUtils.navigateUpFromSameTask(addPetInformation.this);
                }
            };
            showValueChangeDialog(listener);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showValueChangeDialog(DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("是否放弃添加");
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

    /**
     * 查找控件
     */
    private void findAddViews() {
        addPetGenderSpinner = (Spinner) findViewById(R.id.addPetSpinner);

        addPetNameEditText = (EditText) findViewById(R.id.EtAddPetName);
        addPetBreedEditText = (EditText) findViewById(R.id.EtAddPetBreed);
        addPetWeightEditText = (EditText) findViewById(R.id.EtAddPetMeasurement);

        addPetOkButton = (Button) findViewById(R.id.btnAddOk);
        addPetCancleButton = (Button) findViewById(R.id.btnAddCancle);
//        addDeletePet = (Button) findViewById(R.id.addDelete);
    }


    /**
     * 往数据库中插入数据
     */
    private void insertPet() {

        String petName = addPetNameEditText.getText().toString();
        String petBreed = addPetBreedEditText.getText().toString();
        String weight = addPetWeightEditText.getText().toString();
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

        //返回包含id的uri
        Uri rowId = getContentResolver().insert(petEntry.CONTENT_URI, petValues);

        Log.w("add", String.valueOf(ContentUris.parseId(rowId)));
        isValueChange = false;

        Toast.makeText(this,"insert success",Toast.LENGTH_SHORT).show();

    }

    /**
     * 下拉栏点击事件
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
            } else if (selectPosition.equals(getString(R.string.gender_unknown))){
                Log.d("add", "unknown");
                mGender = petEntry.GENDER_UNKNOWN;
                //选中了unknown
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        mGender = petEntry.GENDER_UNKNOWN;
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
                if (!isValueChange){//没有产生修改
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

                showValueChangeDialog(listener);
            }
            break;
            default:
                break;
        }
    }

    /**
     * 有数据发生更改
     * @param view
     * @param motionEvent
     * @return
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        isValueChange = true;
        return false;
    }
}
