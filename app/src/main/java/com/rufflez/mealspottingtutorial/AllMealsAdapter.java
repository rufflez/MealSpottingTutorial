package com.rufflez.mealspottingtutorial;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseImageView;
import com.parse.ParseQueryAdapter;

/**
 * Created by rufflez on 1/9/16.
 */
public class AllMealsAdapter extends ParseRecyclerQueryAdapter<Meal, AllMealsAdapter.ViewHolder> {

    public AllMealsAdapter(ParseQueryAdapter.QueryFactory<Meal> factory, boolean hasStableIds) {
        super(factory, hasStableIds);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_items_list_view, parent, false);
        ViewHolder myViewHolder = new ViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Meal meal = getItem(position);
        holder.title.setText(meal.getTitle());
        holder.rating.setText(meal.getRating());
        holder.photo.setParseFile(meal.getPhotoFile());
        holder.photo.loadInBackground();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView rating;
        ParseImageView photo;

        ViewHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.title);
            rating = (TextView)itemView.findViewById(R.id.rating);
            photo = (ParseImageView)itemView.findViewById(android.R.id.icon);
        }
    }
}
