package com.example.svampkartan.Fragments.AddMushroom;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.svampkartan.R;

import java.util.ArrayList;

public class AddMushroomViewPagerAdapter extends RecyclerView.Adapter<AddMushroomViewPagerAdapter.ViewHolder> {

    private ArrayList<AddMushroomViewPagerItem> viewPagerItemArrayList;

    public AddMushroomViewPagerAdapter(ArrayList<AddMushroomViewPagerItem> viewPagerItemArrayList) {
        this.viewPagerItemArrayList = viewPagerItemArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.add_mushroom_viewpager_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AddMushroomViewPagerItem addMushroomViewPagerItem = viewPagerItemArrayList.get(position);
        holder.tvImage.setImageResource(addMushroomViewPagerItem.imageId);
    }

    @Override
    public int getItemCount() {
        return viewPagerItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView tvImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvImage = itemView.findViewById(R.id.AddMushroomViewpagerItemImage);
        }
    }
}
