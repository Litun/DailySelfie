package ru.litun.dailyselfie;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Litun on 21.08.2015.
 */
public class SelfieAdapter extends RecyclerView.Adapter<SelfieAdapter.ViewHolder> {
    private List<Selfie> data;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(View v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.small_selfie);
            textView = (TextView) v.findViewById(R.id.selfie_text);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SelfieAdapter(List<Selfie> data) {
        this.data = data;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SelfieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.selfie_item, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Selfie selfie = data.get(position);
        holder.imageView.setImageBitmap(selfie.getThumbnail());
        holder.textView.setText(selfie.getName());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return data.size();
    }
}
