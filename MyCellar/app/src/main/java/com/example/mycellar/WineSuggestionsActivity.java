package com.example.mycellar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WineSuggestionsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    List<WineSuggestion> wineSuggestionList = new ArrayList<>();
    RecyclerView recyclerView;
    WineSuggestionsRecyclerViewAdapter myAdapter;
    Spinner typeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wine_suggestions);

        recyclerView = findViewById(R.id.wineSuggestionsRecyclerView);
        typeSpinner = findViewById(R.id.typeSpinner);

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.wineType, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(arrayAdapter);
        typeSpinner.setOnItemSelectedListener(this);


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String type = adapterView.getItemAtPosition(i).toString();
        if(type.equals("Red"))
            getRedWines();
        else
            getWhiteWines();
    }

    private void getRedWines() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RedWineSuggestionAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RedWineSuggestionAPI api = retrofit.create(RedWineSuggestionAPI.class);

        Call<List<WineSuggestion>> call = api.getRedWines();

        call.enqueue(new Callback<List<WineSuggestion>>() {
            @Override
            public void onResponse(Call<List<WineSuggestion>> call, Response<List<WineSuggestion>> response) {
                wineSuggestionList = response.body();

                myAdapter = new WineSuggestionsRecyclerViewAdapter(wineSuggestionList, getApplicationContext());
                recyclerView.setAdapter(myAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }

            @Override
            public void onFailure(Call<List<WineSuggestion>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getWhiteWines() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WhiteWineSuggestionAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WhiteWineSuggestionAPI api = retrofit.create(WhiteWineSuggestionAPI.class);

        Call<List<WineSuggestion>> call = api.getWhiteWines();

        call.enqueue(new Callback<List<WineSuggestion>>() {
            @Override
            public void onResponse(Call<List<WineSuggestion>> call, Response<List<WineSuggestion>> response) {
                wineSuggestionList = response.body();

                myAdapter = new WineSuggestionsRecyclerViewAdapter(wineSuggestionList, getApplicationContext());
                recyclerView.setAdapter(myAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }

            @Override
            public void onFailure(Call<List<WineSuggestion>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // nothing is here on
    }
}