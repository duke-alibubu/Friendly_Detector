package com.android.example.friendlydetector.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.example.friendlydetector.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainMenuLoggedIn.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainMenuLoggedIn#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainMenuLoggedIn extends Fragment {

    private MainMenuLoggedIn() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static MainMenuLoggedIn newInstance() {
        MainMenuLoggedIn fragment = new MainMenuLoggedIn();
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
        return inflater.inflate(R.layout.fragment_main_menu_logged_in, container, false);
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
