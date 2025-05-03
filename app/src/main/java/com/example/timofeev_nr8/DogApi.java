package com.example.timofeev_nr8;

import okhttp3.Cache;
import retrofit2.Call;
import retrofit2.http.GET;

public interface DogApi {
    @GET("woof.json")
    Call<DogResponse> getRandomDog();
}
