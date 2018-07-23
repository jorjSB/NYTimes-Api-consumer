package com.george.balasca.articleregistry.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.george.balasca.articleregistry.R;
import com.george.balasca.articleregistry.repository.NetworkState;
import com.george.balasca.articleregistry.repository.Status;

public class NetworkStateItemViewHolder extends RecyclerView.ViewHolder {
    private final ProgressBar progressBar;
    private final TextView errorMsg;
    private Button button;

    public NetworkStateItemViewHolder(View itemView) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.progress_bar);
        errorMsg = itemView.findViewById(R.id.error_msg);
        button = itemView.findViewById(R.id.retry_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // listItemClickListener.onRetryClick(view, getAdapterPosition());
            }
        });
    }


    public void bindView(NetworkState networkState) {
        if (networkState != null && networkState.getNetworkStatus() == Status.RUNNING) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }

        if (networkState != null && networkState.getNetworkStatus() == Status.FAILED) {
            errorMsg.setVisibility(View.VISIBLE);
            errorMsg.setText(networkState.getMessage());
        } else {
            errorMsg.setVisibility(View.GONE);
        }
    }
}
