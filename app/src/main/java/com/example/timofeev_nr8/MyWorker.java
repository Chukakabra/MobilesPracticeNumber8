package com.example.timofeev_nr8;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker extends Worker {
    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String taskName = getInputData().getString("task");
        Log.d("WORKER", "Starting" + taskName);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            return Result.failure();
        }
        Log.d("WORKER", "Finished" + taskName);
        return Result.success();
    }
}
