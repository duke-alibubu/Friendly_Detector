package com.android.example.friendlydetector.fragments.history;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

public class BindingUtils {

    @BindingAdapter("setBitmapSrc")
    public static void setImageUrl(ImageView imageView, HistoryItemData item) {
        imageView.setImageBitmap(item.imageItem);
    }
}
