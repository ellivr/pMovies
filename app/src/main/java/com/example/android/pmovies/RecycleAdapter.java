package com.example.android.pmovies;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.pmovies.tools.Movie;
import com.squareup.picasso.Picasso;

/**
 * Created by ref on 7/2/2017.
 */

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.NumberViewHolder> {

    private Movie[] pMovies;
    Context context;

    public RecycleAdapter(){

    }

    @Override
    public RecycleAdapter.NumberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        int layoutIdForListItem = R.layout.recyclerview_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        NumberViewHolder viewHolder = new NumberViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecycleAdapter.NumberViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if(pMovies == null){
            return 0;
        }
        return pMovies.length;
    }

    public void swapData(Movie[] list) {
        pMovies= list;
        notifyDataSetChanged();
    }

    class NumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageItem;

        public NumberViewHolder(View itemView) {
            super(itemView);
            imageItem = (ImageView) itemView.findViewById(R.id.movie_image);
            imageItem.setOnClickListener(this);
        }

        void bind(int pos){
            Picasso.with(context).load(pMovies[pos].getImagePath(185)).placeholder(R.mipmap.ic_autorenew_black_24dp).error(R.mipmap.ic_warning_black_24dp).into(imageItem);
        }

        @Override
        //When clicked, start detail activity showing this specific movie
        public void onClick(View v) {
            Intent  detailActivity = new Intent(context, DetailActivity.class);
                    detailActivity.putExtra(Intent.EXTRA_TEXT, String.valueOf(getAdapterPosition()));
            context.startActivity(detailActivity);
        }
    }
}
