package com.android.example.friendlydetector.fragments.history;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class HistoryViewModelFactory implements ViewModelProvider.Factory {
    private Application application;
    public HistoryViewModelFactory(Application application){
        super();
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HistoryViewModel.class)){
            return (T)new HistoryViewModel(application) ;
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

