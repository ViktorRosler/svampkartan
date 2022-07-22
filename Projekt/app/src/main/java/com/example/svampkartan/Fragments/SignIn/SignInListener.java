package com.example.svampkartan.Fragments.SignIn;


import com.google.firebase.auth.FirebaseAuth;

public interface SignInListener {
    void onUserSignIn(FirebaseAuth mAuth);
}
