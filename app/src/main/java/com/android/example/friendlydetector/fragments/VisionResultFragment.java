package com.android.example.friendlydetector.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.example.friendlydetector.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;


public class VisionResultFragment extends Fragment {
    private Bitmap image;
    private String result;
    private ImageView imageResult;
    private TextView textResult;
    private Button translateButton;

    private VisionResultFragment(Bitmap image, String result) {
        this.image = image;
        this.result = result;
    }

    public static VisionResultFragment newInstance(Bitmap image, String result) {
        VisionResultFragment fragment = new VisionResultFragment(image, result);

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
        View root = inflater.inflate(R.layout.fragment_vision_result, container, false);
        imageResult = root.findViewById(R.id.image_taken);
        textResult = root.findViewById(R.id.result);
        imageResult.setImageBitmap(image);
        textResult.setText(result);

        translateButton = root.findViewById(R.id.translate);
        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translateEnglishtoChinese();
            }
        });
        return root;
    }

    private void translateEnglishtoChinese(){
        // Create an English-Chinese translator
        FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                // from language
                .setSourceLanguage(FirebaseTranslateLanguage.EN)
                // to language
                .setTargetLanguage(FirebaseTranslateLanguage.ZH)
                .build();
        final FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance()
                .getTranslator(options);

        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions
                .Builder().requireWifi().build();

        // Download translation model if needed
        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Do translation
                        translator.translate(textResult.getText().toString()).addOnSuccessListener(
                                new OnSuccessListener<String>() {
                                    @Override
                                    public void onSuccess(String s) {
                                        textResult.setText(s);
                                        textResult.setTextColor(Color.BLACK);
                                    }
                                }
                        );
                    }
                });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
