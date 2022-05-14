package com.example.mycellar;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RedWineSuggestionAPI {
    String BASE_URL = "https://api.sampleapis.com/wines/";

    @GET("reds")
    Call<List<WineSuggestion>> getRedWines();

}
