package com.android.example.friendlydetector.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.example.friendlydetector.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


public class ImgToTextFragment extends Fragment {
    private Button loadButton;
    private Button cameraButton;
    private ImageView image;

    private String currentPhotoPath;
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
                loadImage();
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

    private void loadImage(){
        Intent loadIntent = new Intent(
                Intent.ACTION_PICK);
        loadIntent.setType("image/*");
        startActivityForResult(loadIntent, LOAD_IMAGE_REQUEST_CODE);
    }
    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.android.example.friendlydetector.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
                //galleryAddPic();
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == LOAD_IMAGE_REQUEST_CODE && null != data){
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
                try {
//                    Bundle extras = data.getExtras();
//                    Bitmap imageTaken = (Bitmap) extras.get("data");
//
//                    image.setImageBitmap(imageTaken);
                    //galleryAddPic();
                    image.setImageBitmap(scaleBitmapToScreenSize(loadScaledPicFromInternalFiles()));
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        else if (requestCode == CAMERA_REQUEST_CODE){
            try {
//                    Bundle extras = data.getExtras();
//                    Bitmap imageTaken = (Bitmap) extras.get("data");
//
//                    image.setImageBitmap(imageTaken);
                galleryAddPic();
                image.setImageBitmap(loadScaledPicFromInternalFiles());
                Log.d("Test", "Con cac");
            }
            catch (Exception e){
                e.printStackTrace();
                Log.e("Test", "Con cac");
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
        return Bitmap.createScaledBitmap(input, screenWidth, height, false);
    }

    private Bitmap loadScaledPicFromInternalFiles() {
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = 1;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        return bitmap;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
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
