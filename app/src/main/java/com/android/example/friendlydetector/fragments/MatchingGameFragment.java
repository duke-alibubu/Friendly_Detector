package com.android.example.friendlydetector.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
    private String answer;
    private int indexOfWord;
    private Handler handler;
    private List<HistoryItemData> gameData;
    private ImageView imageToShow;
    private Button option1;
    private Button option2;
    private Button option3;
    private Button option4;
    private TextView screenText;
    private String[] objectList;

    private MatchingGameFragment() {
        gameData = new ArrayList<>();
        objectList = new String[]{"screw",
                "sailboat",
                "fork",
                "bed",
                "bracelet",
                "USB drive", "house",
                "paper",
                "hair brush",
                "table",
                "key chain",
                "chalk",
                "camera",
                "headphones", "glasses", "computer", "sofa", "puddle", "deodorant", "blanket",
                "beef",
                "hanger",
                "bow",
                "sponge",
                "apple",
                "greeting card",
                "mouse pad",
                "tooth picks",
                "shawl",
                "leg warmers",
                "doll",
                "model", "car",
                "desk",
                "washing machine",
                "cork",
                "tree", "book",
                "mirror",
                "picture frame",
                "sand paper",
                "sidewalk",
                "toilet",
                "thermometer",
                "controller",
                "soap",
                "rusty nail",
                "Skirt",
                "Overalls",
                "Thong",
                "Shawl",
                "Nightgown", "Lion", "Dragon", "Pool", "Poppy",
                "Sweet Pea",
                "Pinks",
                "Broom",
                "Bougainvillea",
                "Lavender"};
        handler = new Handler();
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
        final View root = inflater.inflate(R.layout.fragment_matching_game, container, false);
        imageToShow = root.findViewById(R.id.img_source);
        option1 = root.findViewById(R.id.option_1);
        option2 = root.findViewById(R.id.option_2);
        option3 = root.findViewById(R.id.option_3);
        option4 = root.findViewById(R.id.option_4);
        screenText = root.findViewById(R.id.question);
        loadImagesFromDatabase();

        View.OnClickListener optionClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                if (answer != null && ((Button)button).getText().toString().equals(answer)){
                    //correct answer! Do the recreate word
                    Toast.makeText(getContext(), "Well Done!", Toast.LENGTH_SHORT).show();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadNewGame();
                        }
                    }, 1000);
                }
            }
        };

        option1.setOnClickListener(optionClickListener);
        option2.setOnClickListener(optionClickListener);
        option3.setOnClickListener(optionClickListener);
        option4.setOnClickListener(optionClickListener);

        //wait to make sure things are loaded
        handler.postDelayed(new Runnable() {
            public void run() {
                // yourMethod();
                loadNewGame();
                ProgressBar progressBar = root.findViewById(R.id.progressBar_cyclic);
                progressBar.setVisibility(View.GONE);
            }
        }, 4000);

        return root;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void loadImagesFromDatabase() {
        String userFolder = FirebaseUtils.getUserEmail();

        Log.d(LOG_TAG, userFolder);

        StorageReference listRef = FirebaseUtils.storage.getReference().child(userFolder);

        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        List<Integer> playIndices = randomIndex(listResult.getItems().size());
                        Log.d(LOG_TAG, playIndices.toString());
                        for (int i : playIndices) {
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

    private HistoryItemData getMatchingItemFromStorageItem(StorageReference item) {
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

        } catch (Exception e) {
            Log.d(LOG_TAG, e.getClass().toString() + e.getMessage());
            return res;
        }
    }

    private List<Integer> randomIndex(int total) {
        //randomize a list of indices between 0 and total - 1 . If total < 4, just take them all
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < total; i++) {
            list.add(i);
        }
        if (total <= 4) {
            return list;
        } else {
            Collections.shuffle(list);
            ArrayList<Integer> ans = new ArrayList<>(4);
            for (int i = 0; i < 4; i++) {
                ans.add(list.get(i));

            }
            return ans;
        }
    }

    private void loadNewGame(){
        if (gameData != null && indexOfWord < gameData.size()){
            imageToShow.setImageBitmap(scaleBitmapToHalfScreenHeight(gameData.get(indexOfWord).imageItem));
            List<String> wordsToUse = new ArrayList<>(4);
            int randomIndex1 = (int)(Math.random() * objectList.length);
            int randomIndex2 = (int)(Math.random() * objectList.length);
            int randomIndex3 = (int)(Math.random() * objectList.length);
            answer = gameData.get(indexOfWord).textItem;
            wordsToUse.add(answer);
            wordsToUse.add(objectList[randomIndex1]);
            wordsToUse.add(objectList[randomIndex2]);
            wordsToUse.add(objectList[randomIndex3]);
            Collections.shuffle(wordsToUse);
            option1.setText(wordsToUse.get(0));
            option2.setText(wordsToUse.get(1));
            option3.setText(wordsToUse.get(2));
            option4.setText(wordsToUse.get(3));
            indexOfWord++;
        }
        else if (indexOfWord == gameData.size())
            endGame();
    }

    private void endGame(){
        option1.setVisibility(View.GONE);
        option2.setVisibility(View.GONE);
        option3.setVisibility(View.GONE);
        option4.setVisibility(View.GONE);
        imageToShow.setImageResource(R.drawable.gamefinish);
        screenText.setText(R.string.congrats);
    }

    private Bitmap scaleBitmapToHalfScreenHeight(Bitmap input){
        //get screen width
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int allowedHeight = displayMetrics.heightPixels / 2;

        //scale the image size down
        int width = allowedHeight * input.getWidth() / input.getHeight();

        return Bitmap.createScaledBitmap(input, width, allowedHeight, true);
    }

}
