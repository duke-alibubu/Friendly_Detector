package com.android.example.friendlydetector.fragments.history;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.android.example.friendlydetector.databinding.ListHistoryBinding;

import androidx.recyclerview.widget.RecyclerView;

class MyViewHolder extends RecyclerView.ViewHolder {
    private ListHistoryBinding binding;
    private MyViewHolder(ListHistoryBinding binding){
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(HistoryItemData item){
        binding.setHistoryItem(item);
        binding.executePendingBindings();
    }

    public static MyViewHolder from(ViewGroup parent){
        LayoutInflater layoutInflater= LayoutInflater.from(parent.getContext());
        ListHistoryBinding binding = ListHistoryBinding.inflate(layoutInflater, parent, false);
        return new MyViewHolder(binding);
    }
}
