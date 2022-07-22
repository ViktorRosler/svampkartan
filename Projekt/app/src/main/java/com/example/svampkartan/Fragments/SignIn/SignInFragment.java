package com.example.svampkartan.Fragments.SignIn;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.svampkartan.MapsActivity;
import com.example.svampkartan.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignInFragment extends Fragment {

    private GoogleSignInClient mGoogleSignInClient;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    private SignInListener signInListener;

    private FirebaseAuth mAuth;

    private final String idToken = "845815538258-03ioe1tdcoc1gqd7n490ulqotrnd5vim.apps.googleusercontent.com";

    private View view;

    public SignInFragment() {}

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        signInListener = (SignInListener) getActivity();

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    try {
                        // Google Sign In was successful, authenticate with Firebase
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        firebaseAuthWithGoogle(account.getIdToken());
                    } catch (ApiException e) {
                        // Google Sign In failed, update UI appropriately
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        mAuth = FirebaseAuth.getInstance();
        createRequest();

        view.findViewById(R.id.google_sign_in_button).setOnClickListener(v -> googleSignIn());
        view.findViewById(R.id.EmailSignInButton).setOnClickListener(v -> emailSignIn());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        applyStyle();
    }

    @Override
    public void onResume() {
        super.onResume();
        applyStyle();
    }

    private void createRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(idToken) // put in values folder
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        activityResultLauncher.launch(signInIntent);
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Sign in success
                        signInListener.onUserSignIn(mAuth);
                    }
                });
    }

    private void emailSignIn() {
        // validate fields
        String email = ((EditText) view.findViewById(R.id.editTextEmailAddress)).getText().toString();
        if (!validateEmail(email)) {
            Toast.makeText(getActivity(), "Invalid Email.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String password = ((EditText) view.findViewById(R.id.editTextPassword)).getText().toString();
        if (!validatePassword(password)) {
            Toast.makeText(getActivity(), "Invalid Password.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // check if user exists
        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                SignInMethodQueryResult result = task.getResult();
                List<String> signInMethods = result.getSignInMethods();

                if (signInMethods.contains(GoogleAuthProvider.GOOGLE_SIGN_IN_METHOD)) {
                    googleSignIn();
                    return;
                }

                if (signInMethods.contains(EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD))
                {
                    // sign in
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), task2 -> {
                                if (task2.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    signInListener.onUserSignIn(mAuth);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(getActivity(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    // create user
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), task2 -> {
                                if (task2.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    signInListener.onUserSignIn(mAuth);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(getActivity(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }

    private boolean validateEmail(String email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private boolean validatePassword(String password) {
        String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private void applyStyle() {
        MapsActivity activity = (MapsActivity) getActivity();

        CardView cardView = activity.findViewById(R.id.SignInBg);

        TextView signInEmailText = activity.findViewById(R.id.SignInEmailLabel);
        TextView signInGoogleText = activity.findViewById(R.id.SignInGoogleLabel);

        EditText emailEdit = activity.findViewById(R.id.editTextEmailAddress);
        EditText passwordEdit = activity.findViewById(R.id.editTextPassword);

        Button signInButton = activity.findViewById(R.id.EmailSignInButton);

        int bgColor, titleTextColor, textColor;
        if (activity.defaultTheme) {
            bgColor = getResources().getColor(R.color.norm_bg, activity.getTheme());
            titleTextColor = getResources().getColor(R.color.norm_title_text, activity.getTheme());
            textColor = getResources().getColor(R.color.norm_text, activity.getTheme());
        } else {
            bgColor = getResources().getColor(R.color.xmas_bg, activity.getTheme());
            titleTextColor = getResources().getColor(R.color.xmas_title_text, activity.getTheme());
            textColor = getResources().getColor(R.color.xmas_text, activity.getTheme());
        }
        cardView.setCardBackgroundColor(bgColor);
        signInEmailText.setTextColor(titleTextColor);
        signInGoogleText.setTextColor(titleTextColor);
        emailEdit.setTextColor(textColor);
        emailEdit.setHintTextColor(textColor);
        passwordEdit.setTextColor(textColor);
        passwordEdit.setHintTextColor(textColor);
        signInButton.setTextColor(titleTextColor);
    }
}