package com.example.mycellar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    Activity activity;
    private ArrayList wineId, wineName, wineProducer, wineRating, wineType, picturePath;
    String collectionType;

    //Constructor
    public RecyclerViewAdapter(Context context, Activity activity, ArrayList wineId, ArrayList wineName,
                               ArrayList wineProducer, ArrayList wineRating, ArrayList wineType,
                               String collectionType, ArrayList picturePath) {
        this.context = context;
        this.activity = activity;
        this.wineId = wineId;
        this.wineName = wineName;
        this.wineProducer = wineProducer;
        this.wineRating = wineRating;
        this.wineType = wineType;
        this.collectionType = collectionType;
        this.picturePath = picturePath;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.winelist_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.id.setText(String.valueOf(wineId.get(position)));
        holder.name.setText(String.valueOf(wineName.get(position)));
        holder.producer.setText(String.valueOf(wineProducer.get(position)));
        if(String.valueOf(wineRating.get(position)).equals("null"))
            holder.rating.setText("");
        else
            holder.rating.setText(String.valueOf(wineRating.get(position)));
        holder.type.setText(String.valueOf(wineType.get(position)));
        //holder.picture.setImageURI(uriArrayList.get(position));

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Redirect from Collection to EditWineDetails
                Intent intent = new Intent(context, EditWineDetails.class);
                //Show to user the inputed data when item for update is clicked
                intent.putExtra("id", String.valueOf(wineId.get(holder.getAdapterPosition())));
                intent.putExtra("name", String.valueOf(wineName.get(position)));
                intent.putExtra("producer", String.valueOf(wineProducer.get(position)));
                intent.putExtra("rating", String.valueOf(wineRating.get(position)));
                intent.putExtra("type", String.valueOf(wineType.get(position)));
                intent.putExtra("collectionType", collectionType);
                //When we navigate from tasted collection to editwinedetails, we want to start activity
                // for result and we want to get back some result from this activity
                activity.startActivityForResult(intent, 1);
            }
        });
                Glide.with(context)
                .asBitmap()
                .load(picturePath.get(position))
                .centerCrop()
                .override(200, 200)
                .into(holder.picture);
    }

    @Override
    public int getItemCount() {
        return wineId.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        TextView id, name, producer, rating, type;
        ImageView picture;
        LinearLayout mainLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.idTextView);
            name = itemView.findViewById(R.id.wineNameTextView);
            producer = itemView.findViewById(R.id.wineProducerTextView);
            rating = itemView.findViewById(R.id.ratingTextView);
            type = itemView.findViewById(R.id.typeTextView);
            picture = itemView.findViewById(R.id.imageView);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
