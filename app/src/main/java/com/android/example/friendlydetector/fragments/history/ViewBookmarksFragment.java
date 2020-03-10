package com.android.example.friendlydetector.fragments.history;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.example.friendlydetector.R;
import com.android.example.friendlydetector.databinding.FragmentViewBookmarksBinding;
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


public class ViewBookmarksFragment extends Fragment {
    private static final String LOG_TAG = "History";
    private static final long ONE_MEGABYTE = 1024 * 1024;
    private List<HistoryItemData> historyItemDataList;

    //private MutableLiveData<List<HistoryItemData>> historyItemDataList = new MutableLiveData<>();
    private FragmentViewBookmarksBinding binding;

    private ViewBookmarksFragment() {
        historyItemDataList = new ArrayList<>();
    }

    public static ViewBookmarksFragment newInstance() {
        ViewBookmarksFragment fragment = new ViewBookmarksFragment();

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_bookmarks, container, false);
        loadImagesFromDatabase();
        final HistoryItemAdapter adapter = new HistoryItemAdapter();
        binding.history.setAdapter(adapter);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // yourMethod();
                adapter.submitList(historyItemDataList);
            }
        }, 5000);

//        historyItemDataList.observe(getViewLifecycleOwner(), new Observer<List<HistoryItemData>>() {
//
//            @Override
//            public void onChanged(List<HistoryItemData> historyItemData) {
//                adapter.submitList(historyItemData);
//                Log.d(LOG_TAG, "History Data Changed");
//            }
//        });

        return binding.getRoot();
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
                        for (final StorageReference item : listResult.getItems()) {
                            // All the items under listRef.

                            //creating a new thread to handle these work in background
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    historyItemDataList.add(getHistoryItemFromStorageItem(item));
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
