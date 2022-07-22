package com.example.svampkartan.Fragments.SignIn;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.svampkartan.DataModel.User;
import com.example.svampkartan.MapsActivity;
import com.example.svampkartan.R;



public class UserAccountFragment extends Fragment {

    private User user;
    private UserAccountListener userAccountListener;

    public UserAccountFragment(User user) {
        this.user = user;
    }

    public static UserAccountFragment newInstance(User user) {
        return new UserAccountFragment(user);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userAccountListener = (UserAccountListener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_account, container, false);
        Button signOutButton = view.findViewById(R.id.SignOutButton);
        signOutButton.setOnClickListener(v -> userAccountListener.onUserSignOut());

        // set user email text
        ((TextView) view.findViewById(R.id.UserEmailText)).setText(user.getUserEmail());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // set user data texts
        ((TextView) getActivity().findViewById(R.id.UserCreatedText)).setText(user
                .getUserCreated()
                .toLocalDateTime()
                .toString()
                .substring(0,10));

        ((TextView) getActivity().findViewById(R.id.UserMarkersText))
                .setText(String.valueOf(user.getNumberOfMarkersFound()));

        if (user.getLatestSpeciesFound() != null) {
            ((TextView) getActivity().findViewById(R.id.UserLatestSpecies))
                    .setText(user.getLatestSpeciesFound().toString());
        } else {
            ((TextView) getActivity().findViewById(R.id.UserLatestSpecies)).setText("-");
        }

        if (user.getLatestMarkerCreatedOn() != null) {
            ((TextView) getActivity().findViewById(R.id.UserLatestDate)).setText(user
                    .getLatestMarkerCreatedOn()
                    .toLocalDateTime()
                    .toString()
                    .substring(0, 10));
        } else {
            ((TextView) getActivity().findViewById(R.id.UserLatestDate)).setText("-");
        }
        applyStyle();
    }

    @Override
    public void onResume() {
        super.onResume();
        applyStyle();
    }

    private void applyStyle() {
        MapsActivity activity = (MapsActivity) getActivity();

        CardView cardView = activity.findViewById(R.id.UserAccountBg);

        TextView signedInAsText =  activity.findViewById(R.id.SignedInAs);
        TextView userCreatedOnText = activity.findViewById(R.id.UserCreatedOn);
        TextView numberMarkersText = activity.findViewById(R.id.NumberOfMarkersPlaced);
        TextView latestSpeciesText = activity.findViewById(R.id.SpeciesFoundText);
        TextView latestDateText = activity.findViewById(R.id.LatestMarkerCreatedOn);

        TextView signedInAsData =  activity.findViewById(R.id.UserEmailText);
        TextView userCreatedOnData = activity.findViewById(R.id.UserCreatedText);
        TextView numberMarkersData = activity.findViewById(R.id.UserMarkersText);
        TextView latestSpeciesData = activity.findViewById(R.id.UserLatestSpecies);
        TextView latestDateData = activity.findViewById(R.id.UserLatestDate);

        Button signOutButton = activity.findViewById(R.id.SignOutButton);

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

        signedInAsText.setTextColor(titleTextColor);
        userCreatedOnText.setTextColor(titleTextColor);
        numberMarkersText.setTextColor(titleTextColor);
        latestSpeciesText.setTextColor(titleTextColor);
        latestDateText.setTextColor(titleTextColor);

        signedInAsData.setTextColor(textColor);
        userCreatedOnData.setTextColor(textColor);
        numberMarkersData.setTextColor(textColor);
        latestSpeciesData.setTextColor(textColor);
        latestDateData.setTextColor(textColor);

        signOutButton.setTextColor(titleTextColor);
    }
}