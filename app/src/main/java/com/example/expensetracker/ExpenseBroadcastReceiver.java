package com.example.expensetracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ExpenseBroadcastReceiver extends BroadcastReceiver {
    public static final String ACTION_EXPENSE_ADDED = "com.example.expensetracker.EXPENSE_ADDED";
    public static final String EXTRA_EXPENSE_TITLE = "expense_title";
    public static final String EXTRA_EXPENSE_AMOUNT = "expense_amount";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_EXPENSE_ADDED.equals(intent.getAction())) {
            String title = intent.getStringExtra(EXTRA_EXPENSE_TITLE);
            String amount = intent.getStringExtra(EXTRA_EXPENSE_AMOUNT);

            // 显示Toast提示
            String message = "收到新消费: " + title + " " + amount;
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}