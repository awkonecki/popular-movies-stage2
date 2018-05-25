package com.example.nebo.popular_movies;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class AppAdapter <D, B> extends RecyclerView.Adapter<B extends MovieViewHolder<B>> {

    private final AppAdapter.AppAdapterOnClickListener mListener;
    private List<D> mAdapterData = null;
    private int mLayout;

    public AppAdapter(AppAdapter.AppAdapterOnClickListener listener) {
        this.mListener = listener;
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
    public B onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        //B binding = DataBindingUtil.inflate(layoutInflater, 0, parent, false);
        //return new B(binding);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull B holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
