package com.android.example.friendlydetector.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.android.example.friendlydetector.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;


public class ImgToTextFragment extends Fragment {
    private Button loadButton;
    private Button cameraButton;
    private ImageView image;

    private static final int LOAD_IMAGE_REQUEST_CODE = 322;
    private static final int CAMERA_REQUEST_CODE = 69;
    private ImgToTextFragment() {

    }

    public static ImgToTextFragment newInstance() {
        ImgToTextFragment fragment = new ImgToTextFragment();

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
        View view = inflater.inflate(R.layout.fragment_img_to_text, container, false);
        loadButton = view.findViewById(R.id.load);
        cameraButton = view.findViewById(R.id.camera);

        image = view.findViewById(R.id.image_holder);

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loadIntent = new Intent(
                        Intent.ACTION_PICK);
                loadIntent.setType("image/*");
                startActivityForResult(loadIntent, LOAD_IMAGE_REQUEST_CODE);
            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });

        return view;
    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && null != data) {
            if (requestCode == LOAD_IMAGE_REQUEST_CODE){
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                    image.setImageBitmap(scaleBitmapToScreenSize(selectedImage));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else if (requestCode == CAMERA_REQUEST_CODE){
                Bundle extras = data.getExtras();
                Bitmap imageTaken = (Bitmap) extras.get("data");

                image.setImageBitmap(scaleBitmapToScreenSize(imageTaken));
            }
        }
    }

    private Bitmap scaleBitmapToScreenSize(Bitmap input){
        //get screen width
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;

        //scale the image size down
        int height = screenWidth * input.getHeight() / input.getWidth();
        return Bitmap.createScaledBitmap(input, screenWidth, height, true);
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
