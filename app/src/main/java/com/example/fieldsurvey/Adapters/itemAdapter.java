package com.example.fieldsurvey.Adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fieldsurvey.Classes.Item;
import com.example.fieldsurvey.Classes.Project;
import com.example.fieldsurvey.R;

import java.util.ArrayList;

public class itemAdapter extends RecyclerView.Adapter<itemAdapter.ViewHolder>{
    ArrayList<Item> ItemList;
    private itemAdapter.OnItemClickListener mListener;
    private static int TYPE_FURNITURE = 1;
    private static int TYPE_PLANT = 2;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(itemAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public itemAdapter(ArrayList<Item> s) {
        this.ItemList = s;
    }

    @NonNull
    @Override
    public itemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if(viewType == TYPE_FURNITURE ) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.furniture_item, parent, false);
            return new itemAdapter.ViewHolder(v);
        }
        else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.plant_item, parent, false);
            return new itemAdapter.ViewHolder(v);
        }
    }
    @Override
    public int getItemViewType(int position) {
        if(ItemList.get(position).furniture.equals(null)) {
            return TYPE_PLANT;
        }
        else {
            return TYPE_FURNITURE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull itemAdapter.ViewHolder holder, int position) {
      if( getItemViewType(position) == TYPE_FURNITURE){
        holder.material.setText((ItemList.get(position).furniture.getMaterial()));
        holder.type.setText(ItemList.get(position).furniture.getType());
      }
      else {
          holder.species.setText(ItemList.get(position).plant.getPlantSpecies());
          holder.hunName.setText(ItemList.get(position).plant.getHungarianName());
          holder.latinName.setText(ItemList.get(position).plant.getLatinName());

      }
    }

    @Override
    public int getItemCount() {
        return ItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView material,type;
        TextView latinName,hunName,species;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            material = itemView.findViewById(R.id.materialTextView);
            type = itemView.findViewById(R.id.typeTextView);
            latinName = itemView.findViewById(R.id.latinNameTextView);
            hunName = itemView.findViewById(R.id.hunNameTextView);
            species = itemView.findViewById(R.id.speciesTextView);

           /* itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });*/
        }
    }
}

