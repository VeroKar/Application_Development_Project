package com.example.mycellar;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
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
import android.widget.Toast;

public class EditWineDetails extends AppCompatActivity {
    EditText editTextName, editTextProducer;
    Button buttonEdit, buttonDelete, buttonMove, buttonEditPicture;
    String id, name, producer, rating, type;
    RadioGroup radioGroupType;
    TextView textViewRating;
    Spinner spinnerRating;
    String collectionType;
    AlertDialog.Builder builder;
    ImageView imageViewWine;
    String picturePath;

    //for the picture
    public static final int PERMISSION_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_wine_details);

        Intent intent = getIntent();
        collectionType = intent.getStringExtra("collectionType");

        editTextName = (EditText) findViewById(R.id.editWineNameTextView);
        editTextProducer = (EditText)findViewById(R.id.editWineProducerTextView);
        buttonEdit = (Button) findViewById(R.id.editWineButton);
        buttonDelete = (Button) findViewById(R.id.deleteWineButton);
        buttonMove = (Button) findViewById(R.id.moveWineButton);
        radioGroupType = (RadioGroup) findViewById(R.id.radioGroup);
        textViewRating = (TextView) findViewById(R.id.ratingTextView2);
        builder = new AlertDialog.Builder(this);
        imageViewWine = (ImageView) findViewById(R.id.imageView2);

        //For the picture of the item
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
        }

        // Setting the rating spinner
        spinnerRating = (Spinner) findViewById(R.id.spinnerRating2);
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

        // If in untasted collection, don't show rating textView and spinner, and move button
        if(collectionType.equals("untasted")) {
            textViewRating.setVisibility(View.GONE);
            spinnerRating.setVisibility(View.GONE);
        }
        if(collectionType.equals("tasted"))
            buttonMove.setVisibility(View.GONE);

        //First, we get and set the data, then update it
        getAndSetIntendData();

        //Set the title of the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(name);

        buttonEditPicture = (Button) findViewById(R.id.editImageButton);
        buttonEditPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);
            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectId = radioGroupType.getCheckedRadioButtonId();
                RadioButton selectedType = (RadioButton) findViewById(selectId);

                name = editTextName.getText().toString().trim();
                producer = editTextProducer.getText().toString().trim();
                type = selectedType.getText().toString().trim();

                DatabaseHelper databaseHelper = new DatabaseHelper(EditWineDetails.this);
                databaseHelper.updateData(id, name, producer, rating, type, picturePath);

                Intent resultIntent = new Intent(EditWineDetails.this, Collection.class);
                resultIntent.putExtra("collectionType", collectionType);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDeleteData();
            }
        });

        buttonMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getIntent().hasExtra("id") && getIntent().hasExtra("name") &&
                        getIntent().hasExtra("producer") &&  getIntent().hasExtra("rating")){
                    //Get data
                    id = getIntent().getStringExtra("id");
                    DatabaseHelper databaseHelper = new DatabaseHelper(EditWineDetails.this);
                    databaseHelper.moveToTasted(id);

                    builder.setMessage("Please select a rating for the wine and click the 'UPDATE' button.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("Select wine rating");
                    alert.show();

                    textViewRating.setVisibility(View.VISIBLE);
                    spinnerRating.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(getApplicationContext(), "No data has been found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void getAndSetIntendData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("name") &&
                getIntent().hasExtra("producer") &&  getIntent().hasExtra("rating")){
            //Get data
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("name");
            producer = getIntent().getStringExtra("producer");
            type = getIntent().getStringExtra("type");
            rating = getIntent().getStringExtra("rating");

            //Set data
            editTextName.setText(name);
            editTextProducer.setText(producer);
            if(type.equals("Red"))
                radioGroupType.check(R.id.editRedCheckBox);
            else
                radioGroupType.check(R.id.editWhiteCheckBox);
            if(!rating.equals("null"))
                spinnerRating.setSelection(Integer.parseInt(rating));
        }else{
            Toast.makeText(this, "No data has been found", Toast.LENGTH_SHORT).show();
        }
    }

    //Verify that user wants to remove the specific item
    void confirmDeleteData(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + name + " ?");
        builder.setMessage("Are you sure you want to delete " + name + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseHelper databaseHelper = new DatabaseHelper(EditWineDetails.this);
                databaseHelper.deleteSingleRow (id);
                //close current activity and get back to main one
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();
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