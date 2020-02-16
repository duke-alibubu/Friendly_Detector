package com.android.example.friendlydetector.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.example.friendlydetector.R;



public class VisionResultFragment extends Fragment {
    private Bitmap image;
    private String result;
    private ImageView imageResult;
    private TextView textResult;

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


}
