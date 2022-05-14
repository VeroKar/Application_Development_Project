package com.example.mycellar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Collection extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    DatabaseHelper databaseHelper;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    Spinner spinnerSort, spinnerFilter;
    FloatingActionButton floatingActionButtonAdd, floatingActionButtonView;
    ArrayList<String> wineId, wineName, wineProducer, wineRating, wineType, winePicture;
    TextView collectionTitleTextView;
    String collectionType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        Intent intent = getIntent();
        collectionType = intent.getStringExtra("collectionType");

        // Setting the correct title
        collectionTitleTextView = findViewById(R.id.collectionTitleTextView);
        String collectionTitle = collectionType.equals("tasted") ? "Tasted Wine Collection"
                : "Untasted Wine Collection";
        collectionTitleTextView.setText(collectionTitle);

        recyclerView = findViewById(R.id.collectionRecyclerView);
        databaseHelper = new DatabaseHelper(Collection.this);
        wineId = new ArrayList<>();
        wineName = new ArrayList<>();
        wineProducer = new ArrayList<>();
        wineRating = new ArrayList<>();
        wineType = new ArrayList<>();
        winePicture = new ArrayList<>();

        //Setting the FAB Add (add wine to collection)
        floatingActionButtonAdd = findViewById(R.id.addWineFloatingActionButton);
        floatingActionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //imageViewEmpty.setVisibility(View.GONE);
                //noData.setVisibility(View.GONE);
                Intent intent = (new Intent(Collection.this, AddWine.class));
                intent.putExtra("collectionType", collectionType);
                startActivityForResult(intent, 1);
            }
        });

        // Setting the Sort By spinner
        spinnerSort = (Spinner) findViewById(R.id.spinner);
        // checking what collection type it is to set the correct spinner list
        int arrayType =  collectionType.equals("tasted") ? R.array.tastedSortBy : R.array.untastedSortBy;
        ArrayAdapter<CharSequence> sortArrayAdapter = ArrayAdapter.createFromResource(this,
                arrayType, android.R.layout.simple_spinner_item);
        sortArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(sortArrayAdapter);
        spinnerSort.setOnItemSelectedListener(this);

        // Setting the Filter type spinner
        spinnerFilter = (Spinner) findViewById(R.id.spinner3);
        ArrayAdapter<CharSequence> filterArrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.filterType, android.R.layout.simple_spinner_item);
        filterArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(filterArrayAdapter);
        spinnerFilter.setOnItemSelectedListener(this);

        // TODO if there is time
        // setting the floatingActionButtonView (change the view, list or view)
        floatingActionButtonView = findViewById(R.id.floatingActionButton2);

        // Setting action when floatingActionButtonView is clicked
        floatingActionButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if (resultCode == RESULT_OK)
                collectionType = data.getStringExtra("collectionType");
            recreate();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // Clear the arraylist before sort and filter, otherwise the entries will accumulate
        wineId.clear();
        wineName.clear();
        wineProducer.clear();
        wineRating.clear();
        wineType.clear();
        winePicture.clear();

        String sortByValue = "";
        String filterByValue = "";

        sortByValue = spinnerSort.getSelectedItem().toString();
        filterByValue = spinnerFilter.getSelectedItem().toString();

        Cursor cursor = databaseHelper.sortByAndFilterBy(sortByValue, filterByValue, collectionType);

        if(cursor.getCount() == 0) {
            Toast.makeText(adapterView.getContext(), "No data found", Toast.LENGTH_SHORT).show();
        } else{
            while(cursor.moveToNext()){
                wineId.add(cursor.getString(0));
                wineName.add(cursor.getString(1));
                wineProducer.add(cursor.getString(2));
                wineRating.add(cursor.getString(3));
                wineType.add(cursor.getString(4));
                winePicture.add(cursor.getString(5));
            }
        }

        recyclerViewAdapter = new RecyclerViewAdapter(this, Collection.this,
                wineId, wineName, wineProducer, wineRating, wineType, collectionType, winePicture);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Collection.this));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}