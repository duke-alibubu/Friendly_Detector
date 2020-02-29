package com.android.example.friendlydetector.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;

public class FirebaseUtils {
    public static FirebaseStorage storage = FirebaseStorage.getInstance();

    public static boolean isSignedIn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user.getDisplayName() != null;
    }
    public static String getUserId(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.getDisplayName() != null){
            return user.getUid();
        }
        else return null;
    }
}
