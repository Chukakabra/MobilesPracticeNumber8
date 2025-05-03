package com.example.timofeev_nr8;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private ImageView dogImageView;
    private Button loadImageButton;
    private Button sequentialTasksButton;
    private Button parallelTasksButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        dogImageView = findViewById(R.id.dogImageView);
        loadImageButton = findViewById(R.id.loadImageButton);
        sequentialTasksButton = findViewById(R.id.sequentialTasksButton);
        parallelTasksButton = findViewById(R.id.parallelTasksButton);

        loadImageButton.setOnClickListener(v -> {
            OneTimeWorkRequest downloadRequest = new OneTimeWorkRequest.Builder(DownloadImageWorker.class)
                    .build();
            WorkManager.getInstance(this).getWorkInfoByIdLiveData(downloadRequest.getId())
                    .observe(this, workInfo -> {
                        if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                            String imageUrl = workInfo.getOutputData().getString("image_url");
                            if (imageUrl != null) {
                                Glide.with(MainActivity.this).load(imageUrl).into(dogImageView);
                            }
                        }
                    });
            WorkManager.getInstance(this).enqueue(downloadRequest);
        });

        sequentialTasksButton.setOnClickListener(v -> {
            OneTimeWorkRequest workRequest1 = new OneTimeWorkRequest.Builder(MyWorker.class)
                    .setInputData(new Data.Builder().putString("task", "Task 1").build()).build();
            OneTimeWorkRequest workRequest2 = new OneTimeWorkRequest.Builder(MyWorker.class)
                    .setInputData(new Data.Builder().putString("task", "Task 2").build()).build();
            OneTimeWorkRequest workRequest3 = new OneTimeWorkRequest.Builder(MyWorker.class)
                    .setInputData(new Data.Builder().putString("task", "Task 3").build()).build();
            WorkManager.getInstance(this).beginWith(workRequest1).then(workRequest2).then(workRequest3)
                    .enqueue();
        });

        parallelTasksButton.setOnClickListener(v -> {
            OneTimeWorkRequest parallelTask1 = new OneTimeWorkRequest.Builder(MyWorker.class)
                    .setInputData(new Data.Builder().putString("task", "Parallel Task 1").build()).build();
            OneTimeWorkRequest parallelTask2 = new OneTimeWorkRequest.Builder(MyWorker.class)
                    .setInputData(new Data.Builder().putString("task", "Parallel Task 2").build()).build();
            WorkManager.getInstance(this).enqueue(Arrays.asList(parallelTask1, parallelTask2));
        });
    }
}