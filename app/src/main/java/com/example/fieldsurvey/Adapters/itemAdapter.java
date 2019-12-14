package com.example.fieldsurvey.Adapters;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fieldsurvey.Classes.Furniture;
import com.example.fieldsurvey.Classes.Item;
import com.example.fieldsurvey.Classes.Plant;
import com.example.fieldsurvey.Classes.Project;
import com.example.fieldsurvey.R;

import java.util.ArrayList;

public class itemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Item> ItemList;
    private itemAdapter.OnItemClickListener mListener;
    private static int TYPE_FURNITURE = 1;
    private static int TYPE_PLANT = 2;
    private static int TYPE_NOTHING =0;
    private RecyclerView.ViewHolder holder;
    private int position;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(itemAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public itemAdapter(ArrayList<Item> items) {
        this.ItemList = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == TYPE_PLANT) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.plant_item, parent, false);
            return new PlantViewHolder(v);
        } else {
            if(viewType == TYPE_FURNITURE) {

                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.furniture_item, parent, false);
                return new FurnitureViewHolder(v);
            }
            else return null;
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (ItemList.get(position).furniture == null && ItemList.get(position).plant != null) {
            return TYPE_PLANT;
        } else {
            if (ItemList.get(position).plant == null && ItemList.get(position).furniture != null) {
                return TYPE_FURNITURE;
            }
            else {
                return TYPE_NOTHING;
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == TYPE_FURNITURE) {


            ((FurnitureViewHolder )holder).material.setText((ItemList.get(position).furniture.getMaterial()));
            ((FurnitureViewHolder )holder).type.setText(ItemList.get(position).furniture.getType());
            ((FurnitureViewHolder )holder).imageView.setImageBitmap(ItemList.get(position).furniture.getBitmap());
            ((FurnitureViewHolder) holder).locationNumb.setText(ItemList.get(position).furniture.getLocationNumber());
        }
            if (getItemViewType(position) == TYPE_PLANT) {

                ((PlantViewHolder) holder).species.setText(ItemList.get(position).getPlant().getPlantSpecies());
                ((PlantViewHolder) holder).hunName.setText(ItemList.get(position).getPlant().getHungarianName());
                ((PlantViewHolder) holder).latinName.setText(ItemList.get(position).getPlant().getLatinName());
                ((PlantViewHolder) holder).imageView.setImageBitmap(ItemList.get(position).getPlant().getBitmap());
                ((PlantViewHolder) holder).locationNumb.setText(ItemList.get(position).getPlant().getLocationNumber());

            }
        }


    @Override
    public int getItemCount() {
        return ItemList.size();
    }

    class FurnitureViewHolder extends RecyclerView.ViewHolder {

        private TextView material, type, locationNumb;
        private ImageView imageView;

        FurnitureViewHolder(@NonNull View itemView) {
            super(itemView);
            material = itemView.findViewById(R.id.materialTextView);
            type = itemView.findViewById(R.id.typeTextView);
            imageView=itemView.findViewById(R.id.imageViewFurniture);
            locationNumb=itemView.findViewById(R.id.textViewLocation);
        }
    }

    class PlantViewHolder extends RecyclerView.ViewHolder {

        private TextView latinName, hunName, species,locationNumb;
        private ImageView imageView;

        PlantViewHolder(@NonNull View itemView) {
            super(itemView);
            latinName = itemView.findViewById(R.id.latinNameTextView);
            hunName = itemView.findViewById(R.id.hunNameTextView);
            species = itemView.findViewById(R.id.speciesTextView);
            imageView=itemView.findViewById(R.id.imageViewPlant);
            locationNumb=itemView.findViewById(R.id.textViewLocation);

        }
    }

}