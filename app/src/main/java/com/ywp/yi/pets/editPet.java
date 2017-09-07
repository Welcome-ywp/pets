package com.ywp.yi.pets;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class editPet extends AppCompatActivity {

    private String mName;
    private String mBreed;
    private EditText etNameEdit;
    private EditText etBreedEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pet);

        etNameEdit = (EditText) findViewById(R.id.EtEditPetName);
        etBreedEdit = (EditText) findViewById(R.id.EtEditPetBreed);

        Intent editIntent = getIntent();
        mName = editIntent.getStringExtra("name");
        mBreed = editIntent.getStringExtra("breed");


        etNameEdit.setText(mName);
        etBreedEdit.setText(mBreed);
    }
}
