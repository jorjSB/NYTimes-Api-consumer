package com.george.balasca.articleregistry.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.george.balasca.articleregistry.R;
import com.george.balasca.articleregistry.repository.NetworkState;
import com.george.balasca.articleregistry.repository.Status;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NetworkStateItemViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.progress_bar)  ProgressBar progressBar;

    public NetworkStateItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }


    public void bindView(NetworkState networkState) {
        if (networkState != null && networkState.getNetworkStatus() == Status.RUNNING) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
