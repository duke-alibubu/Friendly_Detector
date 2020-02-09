package com.android.example.friendlydetector;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.example.friendlydetector.fragments.MainMenuLoggedIn;
import com.android.example.friendlydetector.fragments.MatchingGameFragment;

public class MainActivity extends AppCompatActivity {


    private Button mainmenuButton;
    private Fragment mainmenuFragment;
    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainmenuFragment = MainMenuLoggedIn.newInstance();
        mainmenuButton = findViewById(R.id.main_button);

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


    }

}
