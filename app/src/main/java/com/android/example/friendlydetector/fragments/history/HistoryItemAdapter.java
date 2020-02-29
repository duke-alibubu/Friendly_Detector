package com.android.example.friendlydetector.fragments.history;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;

public class HistoryItemAdapter extends ListAdapter<HistoryItemData, MyViewHolder> {

    public HistoryItemAdapter(){
        super(new HistoryItemDiffUtil());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return MyViewHolder.from(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(getItem(position));
    }
}
