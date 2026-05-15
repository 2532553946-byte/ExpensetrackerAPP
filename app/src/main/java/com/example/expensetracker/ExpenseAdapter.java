package com.example.expensetracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    private List<Expense> expenseList;

    public ExpenseAdapter(List<Expense> expenseList) {
        this.expenseList = expenseList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expense, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Expense expense = expenseList.get(position);

        holder.tvExpenseTitle.setText(expense.getTitle());
        holder.tvExpenseAmount.setText(expense.getFormattedAmount());
        holder.tvExpenseDate.setText(expense.getFormattedDate());
        holder.tvExpenseNote.setText(expense.getNote());

        // 设置图标
        holder.ivExpenseIcon.setImageResource(expense.getIconResourceId());
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public void updateData(List<Expense> newExpenseList) {
        expenseList = newExpenseList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivExpenseIcon;
        TextView tvExpenseTitle;
        TextView tvExpenseAmount;
        TextView tvExpenseDate;
        TextView tvExpenseNote;

        ViewHolder(View itemView) {
            super(itemView);
            ivExpenseIcon = itemView.findViewById(R.id.ivExpenseIcon);
            tvExpenseTitle = itemView.findViewById(R.id.tvExpenseTitle);
            tvExpenseAmount = itemView.findViewById(R.id.tvExpenseAmount);
            tvExpenseDate = itemView.findViewById(R.id.tvExpenseDate);
            tvExpenseNote = itemView.findViewById(R.id.tvExpenseNote);
        }
    }
}
