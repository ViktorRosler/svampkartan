package com.example.svampkartan.Fragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.example.svampkartan.MapsActivity;
import com.example.svampkartan.R;


public class SettingsFragment extends Fragment {

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Switch themeSwitch = view.findViewById(R.id.ThemeSwitch);
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ((MapsActivity) getActivity()).defaultTheme = !isChecked;
            applyStyle();
        });

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


    private void applyStyle() {
        MapsActivity activity = (MapsActivity) getActivity();

        CardView cardView = activity.findViewById(R.id.SettingsBg);

        TextView settingsText = activity.findViewById(R.id.SettingsText);
        TextView themeText = activity.findViewById(R.id.ThemeText);
        TextView themeOptionsText = activity.findViewById(R.id.ThemeOptionsText);

        int bgColor, titleTextColor, textColor;
        if (activity.defaultTheme) {
            ((Switch) activity.findViewById(R.id.ThemeSwitch)).setChecked(false);
            bgColor = getResources().getColor(R.color.norm_bg, activity.getTheme());
            titleTextColor = getResources().getColor(R.color.norm_title_text, activity.getTheme());
            textColor = getResources().getColor(R.color.norm_text, activity.getTheme());
        } else {
            ((Switch) activity.findViewById(R.id.ThemeSwitch)).setChecked(true);
            bgColor = getResources().getColor(R.color.xmas_bg, activity.getTheme());
            titleTextColor = getResources().getColor(R.color.xmas_title_text, activity.getTheme());
            textColor = getResources().getColor(R.color.xmas_text, activity.getTheme());
        }

        cardView.setCardBackgroundColor(bgColor);
        settingsText.setTextColor(titleTextColor);
        themeText.setTextColor(titleTextColor);
        themeOptionsText.setTextColor(textColor);
    }

}