package com.android.example.friendlydetector.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.example.friendlydetector.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;


public class TranslateFragment extends Fragment {

    private EditText sourceText;
    private Button translateButton;
    private TextView translatedText;

    private TranslateFragment() {

    }

    public static TranslateFragment newInstance() {
        TranslateFragment fragment = new TranslateFragment();

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
        View view = inflater.inflate(R.layout.fragment_translate, container, false);
        sourceText = view.findViewById(R.id.source_text);
        translateButton = view.findViewById(R.id.translate);
        translatedText = view.findViewById(R.id.translated_text);

        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                                    translator.translate(sourceText.getText().toString()).addOnSuccessListener(
                                            new OnSuccessListener<String>() {
                                                @Override
                                                public void onSuccess(String s) {
                                                    translatedText.setText(s);
                                                    translatedText.setTextColor(Color.BLACK);
                                                }
                                            }
                                    );
                                }
                        });
            }
        });

        return view;
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
