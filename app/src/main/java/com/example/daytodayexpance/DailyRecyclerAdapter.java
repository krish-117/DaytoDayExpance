package com.example.daytodayexpance;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daytodayexpance.databinding.ListdailyBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DailyRecyclerAdapter extends RecyclerView.Adapter<DailyRecyclerAdapter.ViewHolder> {
    private Map<String, String> data;
    private List<String> keys;

    // Constructor
    public DailyRecyclerAdapter() {
        data = new HashMap<>();
        keys = new ArrayList<>();
    }

    // Method to set data
    public void setData(Map<String, String> newData, List<String> newKeys) {
        data.clear();
        keys.clear();

        data.putAll(newData);
        keys.addAll(newKeys);

        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    @NonNull
    @Override
    public DailyRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listdaily, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull DailyRecyclerAdapter.ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return keys.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ListdailyBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super (itemView);
            binding=ListdailyBinding.bind(itemView);
        }

        public void setData(int position) {
                binding.tvtext.setText(keys.get(position).toString());
                binding.tvammount.setText(data.get(keys.get(position)));
        }
    }
}
