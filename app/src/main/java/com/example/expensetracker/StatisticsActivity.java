package com.example.expensetracker;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StatisticsActivity extends AppCompatActivity {

    private TextView tvSelectedDate;
    private TextView tvIncome;
    private TextView tvExpense;
    private TextView tvBalance;
    private EditText etIncome;
    private EditText etExpense;
    private ImageView ivBack;

    private double income = 0.0;
    private double expense = 0.0;
    private double balance = 0.0;
    private String selectedDate;

    private DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // 获取传递的日期
        selectedDate = getIntent().getStringExtra("SELECTED_DATE");
        if (selectedDate == null) {
            selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        }

        // 初始化视图
        initViews();

        // 设置点击事件
        setupClickListeners();

        // 设置文本变化监听
        setupTextWatchers();

        // 更新显示
        updateDisplay();
    }

    private void initViews() {
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        tvIncome = findViewById(R.id.tvIncome);
        tvExpense = findViewById(R.id.tvExpense);
        tvBalance = findViewById(R.id.tvBalance);
        etIncome = findViewById(R.id.etIncome);
        etExpense = findViewById(R.id.etExpense);
        ivBack = findViewById(R.id.ivBack);

        // 设置日期
        tvSelectedDate.setText(selectedDate);
    }

    private void setupClickListeners() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 返回主页面
            }
        });
    }

    private void setupTextWatchers() {
        etIncome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String incomeStr = s.toString().trim();
                    if (!incomeStr.isEmpty()) {
                        income = Double.parseDouble(incomeStr);
                        if (income < 0) {
                            income = 0;
                            etIncome.setText("0");
                        }
                    } else {
                        income = 0;
                    }
                    calculateBalance();
                    updateDisplay();
                } catch (NumberFormatException e) {
                    income = 0;
                    calculateBalance();
                    updateDisplay();
                }
            }
        });

        etExpense.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String expenseStr = s.toString().trim();
                    if (!expenseStr.isEmpty()) {
                        expense = Double.parseDouble(expenseStr);
                        if (expense < 0) {
                            expense = 0;
                            etExpense.setText("0");
                        }
                    } else {
                        expense = 0;
                    }
                    calculateBalance();
                    updateDisplay();
                } catch (NumberFormatException e) {
                    expense = 0;
                    calculateBalance();
                    updateDisplay();
                }
            }
        });
    }

    private void calculateBalance() {
        balance = income - expense;
    }

    private void updateDisplay() {
        tvIncome.setText("¥ " + decimalFormat.format(income));
        tvExpense.setText("¥ " + decimalFormat.format(expense));
        tvBalance.setText("¥ " + decimalFormat.format(balance));

        // 如果结余为负数，改变颜色为红色
        if (balance < 0) {
            tvBalance.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        } else {
            tvBalance.setTextColor(getResources().getColor(android.R.color.white));
        }
    }
}
