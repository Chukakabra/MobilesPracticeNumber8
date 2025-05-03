package com.example.timofeev_nr8;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DownloadImageWorker extends Worker {
    public DownloadImageWorker(@NonNull Context context, @NonNull WorkerParameters workerParameters) {
        super(context, workerParameters);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            Retrofit retrofit = new Retrofit.Builder().baseUrl("https://random.org/")
                    .addConverterFactory(GsonConverterFactory.create()).build();
            DogApi api = retrofit.create(DogApi.class);
            Call<DogResponse> call = api.getRandomDog();
            Response<DogResponse> response = call.execute();

            if (response.isSuccessful() && response.body() != null) {
                String imageUrl = response.body().getUrl();
                Data outputData = new Data.Builder().putString("image_url", imageUrl).build();
                return Result.success(outputData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.failure();
    }
}
