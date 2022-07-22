package com.example.svampkartan.Fragments.AddMushroom;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.svampkartan.Fragments.FragmentListener;
import com.example.svampkartan.DataModel.MushroomSpecies;
import com.example.svampkartan.MapsActivity;
import com.example.svampkartan.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;


public class AddMushroomFragment extends Fragment {

    private TabLayout.OnTabSelectedListener tabSelectedListener;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ArrayList<AddMushroomViewPagerItem> addMushroomViewPagerItemArrayList;
    private TextInputEditText textInputEditText;

    private FragmentListener fragmentListener;
    private AddMushroomListener addMushroomListener;

    private MushroomSpecies selectedSpecies;
    private String comment;


    public AddMushroomFragment() {
        // Required empty public constructor
    }

    public static AddMushroomFragment newInstance() {
        return new AddMushroomFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_mushroom, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        fragmentListener = (MapsActivity) getActivity();
        addMushroomListener = (AddMushroomListener) getActivity();

        viewPager2 = view.findViewById(R.id.AddMushroomViewPager);
        tabLayout = view.findViewById(R.id.AddMushroomTabLayout);

        MushroomSpecies[] species = {MushroomSpecies.Boletus_Edulis,
                          MushroomSpecies.Cantharellus_Cibarius,
                          MushroomSpecies.Psilocybe_Semilanceata,
                          MushroomSpecies.Coprinus_Comatus,
                          MushroomSpecies.Agaricus_Campestris,
                          MushroomSpecies.Sparassis_Crispa};

        int[] images = {MushroomSpecies.Boletus_Edulis.getImageId(),
                        MushroomSpecies.Cantharellus_Cibarius.getImageId(),
                        MushroomSpecies.Psilocybe_Semilanceata.getImageId(),
                        MushroomSpecies.Coprinus_Comatus.getImageId(),
                        MushroomSpecies.Agaricus_Campestris.getImageId(),
                        MushroomSpecies.Sparassis_Crispa.getImageId()};

        addMushroomViewPagerItemArrayList = new ArrayList<>();


        for (int i = 0; i < images.length; ++i) {
            AddMushroomViewPagerItem mushroomViewPagerItem = new AddMushroomViewPagerItem(images[i]);
            addMushroomViewPagerItemArrayList.add(mushroomViewPagerItem);
            tabLayout.addTab(tabLayout.newTab().setText(species[i].toString()));
        }

        AddMushroomViewPagerAdapter mushroomViewPagerAdapter = new AddMushroomViewPagerAdapter(addMushroomViewPagerItemArrayList);

        viewPager2.setAdapter(mushroomViewPagerAdapter);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(2);

        tabSelectedListener = new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
                selectedSpecies = species[tab.getPosition()];
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        };

        tabLayout.addOnTabSelectedListener(tabSelectedListener);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tabLayout.removeOnTabSelectedListener(tabSelectedListener);
                tabLayout.selectTab(tabLayout.getTabAt(position));
                tabLayout.addOnTabSelectedListener(tabSelectedListener);
                selectedSpecies = species[position];
            }
        });

        textInputEditText = view.findViewById(R.id.AddMushroomTextInput);
        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                comment = "-";
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                comment = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Button button = view.findViewById(R.id.AddMushroomButton);
        button.setOnClickListener(v -> {
            if (comment == null || comment.length() == 0) {
                comment = "-";
            }
            addMushroomListener.addMushroom(selectedSpecies, comment);
            textInputEditText.setText("");
            comment = "";
            fragmentListener.changeToBlankFragment();
        });

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

        CardView cardView = activity.findViewById(R.id.AddMushroomBg);
        TextInputEditText textInputEditText = activity.findViewById(R.id.AddMushroomTextInput);
        TabLayout tabLayout = activity.findViewById(R.id.AddMushroomTabLayout);
        Button addMushroomButton = activity.findViewById(R.id.AddMushroomButton);

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
        textInputEditText.setTextColor(textColor);
        textInputEditText.setHintTextColor(textColor);
        textInputEditText.setBackgroundColor(bgColor);
        addMushroomButton.setTextColor(titleTextColor);
        tabLayout.setTabTextColors(textColor, titleTextColor);
        tabLayout.setBackgroundColor(bgColor);
        tabLayout.setSelectedTabIndicatorColor(titleTextColor);
    }

}