package com.example.mycellar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class WineSuggestionsRecyclerViewAdapter extends RecyclerView.Adapter<WineSuggestionsRecyclerViewAdapter.ViewHolder>{

    private List<WineSuggestion> wineSuggestionList;
    private Context context;

    public WineSuggestionsRecyclerViewAdapter(List<WineSuggestion> wineSuggestionList, Context context) {
        this.wineSuggestionList = wineSuggestionList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.wine_suggestion_row, parent,
                false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WineSuggestion wineSuggestion = wineSuggestionList.get(position);

        Glide.with(context).asBitmap().load(wineSuggestion.getImage()).into(holder.image);
        holder.wine.setText(wineSuggestion.getWine());
        holder.winery.setText(wineSuggestion.getWinery());
        holder.location.setText(wineSuggestion.getLocation());
        holder.average.setText(wineSuggestion.getRating().getAverage());
        holder.reviews.setText(wineSuggestion.getRating().getReviews());
    }

    @Override
    public int getItemCount() {
        return wineSuggestionList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView wine, winery, location, average, reviews;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.wineSuggestionImageView);
            wine = itemView.findViewById(R.id.wineSuggestionWineTextView);
            winery = itemView.findViewById(R.id.wineSuggestionWineryTextView);
            location = itemView.findViewById(R.id.wineSuggestionLocationTextView);
            average = itemView.findViewById(R.id.wineSuggestionAverageTextView);
            reviews = itemView.findViewById(R.id.wineSuggestionReviewsTextView);
        }
    }
}
