package com.android.example.friendlydetector.fragments.history;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class HistoryItemDiffUtil extends DiffUtil.ItemCallback<HistoryItemData> {
    @Override
    public boolean areItemsTheSame(@NonNull HistoryItemData oldItem, @NonNull HistoryItemData newItem) {
        return false;
    }

    @Override
    public boolean areContentsTheSame(@NonNull HistoryItemData oldItem, @NonNull HistoryItemData newItem) {
        return false;
    }
}
