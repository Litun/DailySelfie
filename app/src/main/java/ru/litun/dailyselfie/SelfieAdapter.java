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
    private OnClick listener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(View v, final OnClick listener) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.small_selfie);
            textView = (TextView) v.findViewById(R.id.selfie_text);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null)
                        listener.itemClicked(view, getAdapterPosition());
                }
            });
        }
    }

    public void setListener(OnClick listener) {
        this.listener = listener;
    }

    public SelfieAdapter(List<Selfie> data) {
        this.data = data;
    }

    @Override
    public SelfieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.selfie_item, parent, false);
        return new ViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Selfie selfie = data.get(position);
        holder.imageView.setImageBitmap(selfie.getThumbnail());
        holder.textView.setText(selfie.getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    interface OnClick {
        void itemClicked(View view, int position);
    }
}
