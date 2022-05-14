package com.example.mycellar;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class AddWine extends AppCompatActivity {
    EditText editTextName, editTextProducer;
    RadioGroup radioGroupType;
    RadioButton checkBoxRed, checkBoxWhite;
    Button buttonAdd, buttonLoadPicture;
    TextView textViewType, textViewRating;
    Spinner spinnerRating;
    String rating;
    String collectionType;
    ImageView imageViewWine;
    String picturePath;

    //for the picture
    public static final int PERMISSION_REQUEST = 0;
    public static final int RESULT_LOAD_IMAGE = 1;
    ActivityResultLauncher<String> mGetContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wine);

        Intent intent = getIntent();
        collectionType = intent.getStringExtra("collectionType");

        editTextName = (EditText) findViewById(R.id.addWineNameTextView);
        editTextProducer = (EditText) findViewById(R.id.addWineProducerTextView);
        radioGroupType = (RadioGroup) findViewById(R.id.radioGroup);
        checkBoxRed = (RadioButton) findViewById(R.id.redCheckBox);
        checkBoxWhite = (RadioButton) findViewById(R.id.whiteCheckBox);
        textViewType = (TextView) findViewById(R.id.typeTextView);
        textViewRating = (TextView) findViewById(R.id.ratingTextView);
        imageViewWine = (ImageView) findViewById(R.id.addImageView);

        //For the picture of the item
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
        }

//        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
//            @Override
//            public void onActivityResult(Uri result) {
//                imageViewWine.setImageURI(result);
//            }
//        });

        // Setting the rating spinner
        spinnerRating = (Spinner) findViewById(R.id.spinnerRating);
        ArrayAdapter<CharSequence> ratingArrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.wineRating, android.R.layout.simple_spinner_item);
        ratingArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRating.setAdapter(ratingArrayAdapter);
        spinnerRating.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                rating = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Nothing to implement
            }
        });
        // If in untasted collection, don't show rating textView and spinner
        if(collectionType.equals("untasted")) {
            textViewRating.setVisibility(View.GONE);
            spinnerRating.setVisibility(View.GONE);
        }

        // Loading a picture
        buttonLoadPicture = (Button) findViewById(R.id.loadImageButton);
        buttonLoadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                //have to launch the mGetContent
//                mGetContent.launch("image/*"); //what you want to pick from the gallery
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);
            }
        });

        buttonAdd = (Button) findViewById(R.id.addWineButton);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextName.getText().toString().trim().isEmpty()){
                    editTextName.setError("Name cannot be empty. Please fill the area");
                    editTextName.requestFocus();
                    return;
                }
                else if(editTextProducer.getText().toString().trim().isEmpty()){
                    editTextProducer.setError("Producer cannot be empty. Please fill the area");
                    editTextProducer.requestFocus();
                    return;
                }

                else
                {
                    int selectId = radioGroupType.getCheckedRadioButtonId();
                    RadioButton selectedType = (RadioButton) findViewById(selectId);

                    Intent intent = getIntent();
                    DatabaseHelper databaseHelper = new DatabaseHelper(AddWine.this);


                    // If the current section is the tasted collection
                    if(collectionType.equals("tasted")) {
                    databaseHelper.insertData(editTextName.getText().toString().trim(),
                            editTextProducer.getText().toString().trim(),
                            rating, selectedType.getText().toString().trim(),
                            picturePath, 1);
                    // else if the current section is the untasted collection
                    }
                    else{
                        databaseHelper.insertData(editTextName.getText().toString().trim(),
                                editTextProducer.getText().toString().trim(),
                                null , selectedType.getText().toString().trim(),
                                picturePath, 0);
                    }
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("collectionType", collectionType);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        picturePath = cursor.getString(columnIndex);
        cursor.close();
    }
}