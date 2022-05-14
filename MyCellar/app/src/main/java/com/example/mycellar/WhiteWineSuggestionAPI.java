package com.example.mycellar;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface WhiteWineSuggestionAPI {
    String BASE_URL = "https://api.sampleapis.com/wines/";

    @GET("whites")
    Call<List<WineSuggestion>> getWhiteWines();
}
