package com.example.expensetracker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Expense {
    private String id;
    private String title;
    private double amount;
    private Date date;
    private String note;

    public Expense(String title, double amount, Date date, String note) {
        this.id = String.valueOf(System.currentTimeMillis());
        this.title = title;
        this.amount = amount;
        this.date = date;
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(date);
    }

    public String getFormattedAmount() {
        return String.format(Locale.getDefault(), "¥ %.2f", amount);
    }

    // 新增：根据标题获取图标资源ID
    public int getIconResourceId() {
        switch (title) {
            case "教育":
                return R.drawable.education;
            case "娱乐":
                return R.drawable.entertainment;
            case "交通":
                return R.drawable.transportation;
            case "生活":
                return R.drawable.living;
            default:
                return R.drawable.living; // 默认图标
        }
    }
}