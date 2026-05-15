package com.example.expensetracker;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;
import androidx.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExpenseService extends Service {
    private Handler handler;
    private Runnable taskRunnable;
    private boolean isRunning = false;
    private int taskCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();

        taskRunnable = new Runnable() {
            @Override
            public void run() {
                if (isRunning) {
                    taskCount++;
                    String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                    String message = "后台任务执行中... 第" + taskCount + "次 (" + time + ")";

                    // 显示Toast（仅用于演示）
                    Toast.makeText(ExpenseService.this, message, Toast.LENGTH_SHORT).show();

                    // 每5秒执行一次
                    handler.postDelayed(this, 5000);
                }
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isRunning) {
            isRunning = true;
            taskCount = 0;
            handler.post(taskRunnable);
            Toast.makeText(this, "后台服务已启动", Toast.LENGTH_SHORT).show();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        handler.removeCallbacks(taskRunnable);
        Toast.makeText(this, "后台服务已停止", Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}