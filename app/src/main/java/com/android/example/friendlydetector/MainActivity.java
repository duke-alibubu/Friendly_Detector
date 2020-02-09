package com.android.example.friendlydetector;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.android.example.friendlydetector.fragments.MainMenuLoggedIn;

public class MainActivity extends AppCompatActivity {


    private Fragment mainmenuFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainmenuFragment = MainMenuLoggedIn.newInstance();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.container, mainmenuFragment).commit();
    }
}
