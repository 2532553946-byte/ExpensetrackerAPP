package com.example.expensetracker;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView tvTotalExpense;
    private TextView tvExpenseCount;
    private TextView tvBroadcastStatus;
    private Button btnAddExpense;
    private Button btnStartService;
    private Button btnStopService;
    private RecyclerView rvExpenses;
    private ExpenseAdapter expenseAdapter;
    private List<Expense> expenseList;
    private double totalExpense = 0.0;

    private ExpenseBroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化视图
        initViews();

        // 初始化数据
        initData();

        // 设置点击事件
        setupClickListeners();

        // 设置日历图标点击事件
        setupCalendarClickListener();

        // 更新总消费显示
        updateTotalExpense();

        // 注册广播接收器
        registerBroadcastReceiver();
        // 发送账单按钮
        Button btnSendBill = findViewById(R.id.btnSendBill);
        btnSendBill.setOnClickListener(v -> {
            // 弹出提示：只显示文字，无其他功能
            android.widget.Toast.makeText(MainActivity.this, "发送账单成功", android.widget.Toast.LENGTH_SHORT).show();
        });
    }

    private void initViews() {
        tvTotalExpense = findViewById(R.id.tvTotalExpense);
        tvExpenseCount = findViewById(R.id.tvExpenseCount);
        tvBroadcastStatus = findViewById(R.id.tvBroadcastStatus);
        btnAddExpense = findViewById(R.id.btnAddExpense);
        btnStartService = findViewById(R.id.btnStartService);
        btnStopService = findViewById(R.id.btnStopService);
        rvExpenses = findViewById(R.id.rvExpenses);

        // 设置RecyclerView
        rvExpenses.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initData() {
        // 使用ExpenseProvider获取数据
        expenseList = ExpenseProvider.getInstance().getAllExpenses();
        totalExpense = ExpenseProvider.getInstance().getTotalExpense();

        // 设置适配器
        expenseAdapter = new ExpenseAdapter(expenseList);
        rvExpenses.setAdapter(expenseAdapter);
    }

    private void setupClickListeners() {
        btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 显示添加消费对话框
                showAddExpenseDialog();
            }
        });

        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 启动后台服务
                Intent serviceIntent = new Intent(MainActivity.this, ExpenseService.class);
                startService(serviceIntent);
                Toast.makeText(MainActivity.this, "后台任务已启动", Toast.LENGTH_SHORT).show();
            }
        });

        btnStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 停止后台服务
                Intent serviceIntent = new Intent(MainActivity.this, ExpenseService.class);
                stopService(serviceIntent);
                Toast.makeText(MainActivity.this, "后台任务已停止", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupCalendarClickListener() {
        ImageView ivCalendar = findViewById(R.id.ivCalendar);
        ivCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    private void registerBroadcastReceiver() {
        broadcastReceiver = new ExpenseBroadcastReceiver();
        IntentFilter filter = new IntentFilter(ExpenseBroadcastReceiver.ACTION_EXPENSE_ADDED);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, filter);
    }

    private void unregisterBroadcastReceiver() {
        if (broadcastReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        }
    }

    private void showAddExpenseDialog() {
        // 创建自定义对话框布局
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_expense, null);

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle("添加消费记录");

        // 获取对话框中的视图
        android.widget.EditText etAmount = dialogView.findViewById(R.id.etAmount);
        android.widget.Spinner spCategory = dialogView.findViewById(R.id.spCategory);
        android.widget.EditText etNote = dialogView.findViewById(R.id.etNote);

        // 设置分类选项
        String[] categories = {"教育", "娱乐", "交通", "生活"};
        android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter);

        builder.setPositiveButton("完成", null); // 先设置为null，稍后处理
        builder.setNegativeButton("取消", null);

        androidx.appcompat.app.AlertDialog dialog = builder.create();

        // 显示对话框
        dialog.show();

        // 重写确定按钮的点击事件，以便验证输入
        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取输入值
                String amountStr = etAmount.getText().toString().trim();
                String category = spCategory.getSelectedItem().toString();
                String note = etNote.getText().toString().trim();

                // 验证输入
                if (amountStr.isEmpty()) {
                    etAmount.setError("请输入金额");
                    return;
                }

                double amount;
                try {
                    amount = Double.parseDouble(amountStr);
                    if (amount <= 0) {
                        etAmount.setError("金额必须大于0");
                        return;
                    }
                } catch (NumberFormatException e) {
                    etAmount.setError("请输入有效的数字");
                    return;
                }

                // 创建新的消费记录
                Expense newExpense = new Expense(category, amount, new Date(), note.isEmpty() ? "无备注" : note);

                // 使用ExpenseProvider添加记录
                ExpenseProvider.getInstance().addExpense(newExpense);

                // 更新本地数据
                expenseList.add(0, newExpense);
                totalExpense = ExpenseProvider.getInstance().getTotalExpense();

                // 更新UI
                expenseAdapter.notifyItemInserted(0);
                rvExpenses.scrollToPosition(0);
                updateTotalExpense();

                // 显示通知
                NotificationHelper.showExpenseAddedNotification(MainActivity.this,
                        newExpense.getTitle(), newExpense.getFormattedAmount());

                // 发送广播
                Intent broadcastIntent = new Intent(ExpenseBroadcastReceiver.ACTION_EXPENSE_ADDED);
                broadcastIntent.putExtra(ExpenseBroadcastReceiver.EXTRA_EXPENSE_TITLE, newExpense.getTitle());
                broadcastIntent.putExtra(ExpenseBroadcastReceiver.EXTRA_EXPENSE_AMOUNT, newExpense.getFormattedAmount());
                LocalBroadcastManager.getInstance(MainActivity.this)
                        .sendBroadcast(broadcastIntent);

                // 更新广播状态
                tvBroadcastStatus.setText("广播状态: 已发送");
                tvBroadcastStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

                // 显示提示
                Toast.makeText(MainActivity.this,
                        "添加消费：" + newExpense.getTitle() + " " + newExpense.getFormattedAmount(),
                        Toast.LENGTH_SHORT).show();

                // 关闭对话框
                dialog.dismiss();
            }
        });
    }

    private void showDatePickerDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_date_picker, null);

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle("选择日期");

        ListView lvDates = dialogView.findViewById(R.id.lvDates);

        // 生成近5天的日期列表
        final List<String> dateList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < 5; i++) {
            dateList.add(sdf.format(calendar.getTime()));
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, dateList);
        lvDates.setAdapter(adapter);

        androidx.appcompat.app.AlertDialog dialog = builder.create();

        // 设置日期点击事件
        lvDates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedDate = dateList.get(position);

                // 跳转到统计页面
                Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
                intent.putExtra("SELECTED_DATE", selectedDate);
                startActivity(intent);

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void updateTotalExpense() {
        tvTotalExpense.setText(String.format(Locale.getDefault(), "¥ %.2f", totalExpense));
        tvExpenseCount.setText(String.format(Locale.getDefault(), "%d 笔记录", expenseList.size()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterBroadcastReceiver();
    }
}