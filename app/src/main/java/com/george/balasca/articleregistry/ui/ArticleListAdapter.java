package com.george.balasca.articleregistry.ui;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.george.balasca.articleregistry.R;
import com.george.balasca.articleregistry.model.apiresponse.Article;
import com.george.balasca.articleregistry.repository.NetworkState;
import com.george.balasca.articleregistry.repository.Status;

import static com.george.balasca.articleregistry.model.apiresponse.Article.DIFF_CALLBACK;

public class ArticleListAdapter extends PagedListAdapter<Article, RecyclerView.ViewHolder> {

    private static final String TAG = ArticleListAdapter.class.getSimpleName();
    private final ArticleListActivity mParentActivity;
    private final Boolean mTwoPane;
    private NetworkState networkState;

    public ArticleListAdapter(ArticleListActivity parent,  boolean twoPane) {
        super(DIFF_CALLBACK);
        mParentActivity = parent;
        mTwoPane = twoPane;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;

        if (viewType == R.layout.article_list_content) {
            view = layoutInflater.inflate(R.layout.article_list_content, parent, false);
            return new ArticleViewHolder(view);
        } else if (viewType == R.layout.network_state_item) {
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false);
            return new NetworkStateItemViewHolder(view);
        } else {
            throw new IllegalArgumentException("unknown view type");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case R.layout.article_list_content:
                ((ArticleViewHolder) holder).bindTo(getItem(position));
                holder.itemView.setOnClickListener(mOnClickListener);
                break;
            case R.layout.network_state_item:
                ((NetworkStateItemViewHolder) holder).bindView(networkState);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return R.layout.network_state_item;
        } else {
            return R.layout.article_list_content;
        }
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView articleItemView;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            articleItemView = itemView.findViewById(R.id.content);
        }

        public void bindTo(Article article) {
            articleItemView.setText(article.getWebUrl());
        }
    }


    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mTwoPane) {
                Bundle arguments = new Bundle();

                // arguments.putString(ArticleDetailFragment.ARG_ITEM_ID, item.id);

                ArticleDetailFragment fragment = new ArticleDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.article_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, ArticleDetailActivity.class);

                // intent.putExtra(ArticleDetailFragment.ARG_ITEM_ID, item.id);

                context.startActivity(intent);
            }
        }
    };


    public void setNetworkState(NetworkState newNetworkState) {
        NetworkState previousState = this.networkState;
        boolean previousExtraRow = hasExtraRow();
        this.networkState = newNetworkState;
        boolean newExtraRow = hasExtraRow();

        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(getItemCount());
            } else {
                notifyItemInserted(getItemCount());
            }
        } else if (newExtraRow && previousState != newNetworkState) {
            notifyItemChanged(getItemCount() - 1);
        }
    }

    private boolean hasExtraRow() {
        if (networkState != null && networkState != NetworkState.LOADED) {
            return true;
        } else {
            return false;
        }
    }

    static class NetworkStateItemViewHolder extends RecyclerView.ViewHolder {

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
}
