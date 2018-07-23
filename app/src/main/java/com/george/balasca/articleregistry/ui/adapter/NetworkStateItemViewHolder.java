package com.george.balasca.articleregistry.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.george.balasca.articleregistry.R;
import com.george.balasca.articleregistry.repository.NetworkState;
import com.george.balasca.articleregistry.repository.Status;

public class NetworkStateItemViewHolder extends RecyclerView.ViewHolder {
    private final ProgressBar progressBar;


    public NetworkStateItemViewHolder(View itemView) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.progress_bar);
    }


    public void bindView(NetworkState networkState) {
        if (networkState != null && networkState.getNetworkStatus() == Status.RUNNING) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
