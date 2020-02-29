package com.android.example.friendlydetector.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.example.friendlydetector.R;
import com.android.example.friendlydetector.fragments.MainMenuLoggedIn;
import com.android.example.friendlydetector.fragments.VisionResultFragment;
import com.android.example.friendlydetector.utils.FirebaseUtils;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationItemView mainmenuButton;
    private BottomNavigationItemView signButton;
    private Fragment mainmenuFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainmenuFragment = MainMenuLoggedIn.newInstance();
        mainmenuButton = findViewById(R.id.nav_home);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.container, mainmenuFragment).commit();

        mainmenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment mainmenuFragment = MainMenuLoggedIn.newInstance();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.container, mainmenuFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        signButton = findViewById(R.id.nav_log);
        signButton.setTitle(getText((FirebaseUtils.isSignedIn()) ? R.string.signout : R.string.signin));
        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }


    private void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    public void createVisionResultFragment(Bitmap bmp, String res){
        VisionResultFragment visionResultFragment = VisionResultFragment.newInstance(bmp, res);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, visionResultFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
