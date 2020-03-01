package com.android.example.friendlydetector.temgarbage;

import android.app.Application;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.android.example.friendlydetector.fragments.history.HistoryItemData;
import com.android.example.friendlydetector.utils.FirebaseUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class HistoryViewModel extends AndroidViewModel {
    private static final String LOG_TAG = "History";
    private static final long ONE_MEGABYTE = 1024 * 1024;
    public MutableLiveData<List<HistoryItemData>> historyItemDataList = new MutableLiveData<>();
    public HistoryViewModel(Application application){
        super(application);
        historyItemDataList.setValue(new ArrayList<HistoryItemData>());
        loadImagesFromDatabase();
    }

    private void loadImagesFromDatabase(){
        String userFolder = FirebaseUtils.getUserEmail();

        Log.d(LOG_TAG, userFolder);

        StorageReference listRef = FirebaseUtils.storage.getReference().child(userFolder);

        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (final StorageReference item : listResult.getItems()) {
                            // All the items under listRef.

                            //creating a new thread to handle these work in background
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    HistoryItemData historyItemData = getHistoryItemFromStorageItem(item);
                                    //historyItemDataList.getValue().add(historyItemData);
                                }
                            }).start();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                        Log.e(LOG_TAG, "Cannot read data!!");
                    }
                });
    }

    private HistoryItemData getHistoryItemFromStorageItem(StorageReference item){
        final HistoryItemData res = new HistoryItemData();
        Task textTask = item.getMetadata();
        textTask.addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                res.textItem = storageMetadata.getCustomMetadata("text");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e(LOG_TAG, "Cannot retrieve Metadata");

            }
        });

        Task imageTask = item.getBytes(ONE_MEGABYTE);
        imageTask.addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                res.imageItem = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(LOG_TAG, e.getClass().toString() + e.getMessage());
            }
        });

        try {
            Tasks.await(textTask);
            Tasks.await(imageTask);
            return res;

        } catch (Exception e){
            Log.d(LOG_TAG, e.getClass().toString() + e.getMessage());
            return res;
        }
    }
}

