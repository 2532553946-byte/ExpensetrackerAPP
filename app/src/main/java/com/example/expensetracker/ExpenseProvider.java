package com.example.expensetracker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExpenseProvider {
    private static ExpenseProvider instance;
    private List<Expense> expenseList;

    private ExpenseProvider() {
        expenseList = new ArrayList<>();
        // 初始化示例数据
        expenseList.add(new Expense("教育", 68.50, new Date(), "购买教材"));
        expenseList.add(new Expense("交通", 15.00, new Date(), "地铁"));
        expenseList.add(new Expense("生活", 299.00, new Date(), "超市购物"));
        expenseList.add(new Expense("娱乐", 80.00, new Date(), "电影票"));
    }

    public static synchronized ExpenseProvider getInstance() {
        if (instance == null) {
            instance = new ExpenseProvider();
        }
        return instance;
    }

    // 获取所有消费记录
    public List<Expense> getAllExpenses() {
        return new ArrayList<>(expenseList);
    }

    // 添加消费记录
    public void addExpense(Expense expense) {
        expenseList.add(0, expense);
    }

    // 获取总消费金额
    public double getTotalExpense() {
        double total = 0;
        for (Expense expense : expenseList) {
            total += expense.getAmount();
        }
        return total;
    }

    // 获取记录数量
    public int getExpenseCount() {
        return expenseList.size();
    }

    // 根据ID获取消费记录
    public Expense getExpenseById(String id) {
        for (Expense expense : expenseList) {
            if (expense.getId().equals(id)) {
                return expense;
            }
        }
        return null;
    }

    // 删除消费记录
    public boolean deleteExpense(String id) {
        for (int i = 0; i < expenseList.size(); i++) {
            if (expenseList.get(i).getId().equals(id)) {
                expenseList.remove(i);
                return true;
            }
        }
        return false;
    }
}