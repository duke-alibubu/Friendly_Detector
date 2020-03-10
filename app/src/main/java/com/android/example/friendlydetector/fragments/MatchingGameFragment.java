package com.android.example.friendlydetector.fragments;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.example.friendlydetector.R;
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
import java.util.Collections;
import java.util.List;


public class MatchingGameFragment extends Fragment {
    private static final String LOG_TAG = "MatchingGame";
    private static final long ONE_MEGABYTE = 1024 * 1024;
    private List<HistoryItemData> gameData;

    private MatchingGameFragment() {
        gameData = new ArrayList<>();
    }

    public static MatchingGameFragment newInstance() {
        MatchingGameFragment fragment = new MatchingGameFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        loadImagesFromDatabase();
        return inflater.inflate(R.layout.fragment_matching_game, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void loadImagesFromDatabase(){
        String userFolder = FirebaseUtils.getUserEmail();

        Log.d(LOG_TAG, userFolder);

        StorageReference listRef = FirebaseUtils.storage.getReference().child(userFolder);

        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        List<Integer> playIndices = randomIndex(listResult.getItems().size());
                        Log.d(LOG_TAG, playIndices.toString());
                        for (int i: playIndices) {
                            // All the items under listRef.

                            //creating a new thread to handle these work in background
                            final StorageReference item = listResult.getItems().get(i);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    gameData.add(getMatchingItemFromStorageItem(item));
                                    //adapter.submitList(historyItemDataList.getValue());
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

    private HistoryItemData getMatchingItemFromStorageItem(StorageReference item){
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

    private List<Integer> randomIndex(int total){
        //randomize a list of indices between 0 and total - 1 . If total < 4, just take them all
        ArrayList<Integer> list = new ArrayList<>();
        for (int i=1; i<total; i++) {
            list.add(i);
        }
        if (total <= 4){
            return list;
        }
        else {
            Collections.shuffle(list);
            ArrayList<Integer> ans = new ArrayList<>(4);
            for (int i=0; i<3; i++) {
                ans.add(list.get(i));

            }
            return ans;
        }
    }
}
