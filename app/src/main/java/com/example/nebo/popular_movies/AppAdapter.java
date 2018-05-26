package com.example.nebo.popular_movies;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.nebo.popular_movies.views.MovieViewHolder;
import com.example.nebo.popular_movies.views.ViewHolderFactory;

import java.util.List;

public class AppAdapter <D> extends RecyclerView.Adapter<MovieViewHolder> {

    private final AppAdapter.AppAdapterOnClickListener mListener;
    private final int mLayout;
    private List<D> mAdapterData = null;

    public AppAdapter(AppAdapter.AppAdapterOnClickListener listener, int layout) {
        this.mListener = listener;
        this.mLayout = layout;
    }

    public interface AppAdapterOnClickListener {
        public void onClick(int position);
    }

    public void setAdapterData(List<D> adapterData) {
        this.mAdapterData = adapterData;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return ViewHolderFactory.createView(this.mLayout,
                DataBindingUtil.inflate(layoutInflater,
                    this.mLayout,
                    parent,
                    false), this.mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        if (position >= 0 && this.mAdapterData != null && !this.mAdapterData.isEmpty()) {
            holder.bind(this.mAdapterData.get(position).toString());
        }
        else {
            holder.onBindDefault();
        }
    }

    @Override
    public int getItemCount() {
        if (this.mAdapterData == null) {
            return 0;
        }
        return this.mAdapterData.size();
    }
}
