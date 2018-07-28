package com.george.balasca.articleregistry.ui.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.george.balasca.articleregistry.R;
import com.george.balasca.articleregistry.model.DBCompleteArticle;
import com.george.balasca.articleregistry.repository.NetworkState;
import com.george.balasca.articleregistry.ui.ArticleDetailActivity;
import com.george.balasca.articleregistry.ui.ArticleDetailFragment;
import com.george.balasca.articleregistry.ui.ArticleListActivity;

import static com.george.balasca.articleregistry.model.DBCompleteArticle.DIFF_CALLBACK;

public class ArticleListAdapter extends PagedListAdapter<DBCompleteArticle, RecyclerView.ViewHolder> {

    private static final String TAG = ArticleListAdapter.class.getSimpleName();
    private final ArticleListActivity mParentActivity;
    private final Boolean mTwoPane;
    private NetworkState networkState;

    public ArticleListAdapter(ArticleListActivity parent, boolean twoPane) {
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
                ((ArticleViewHolder) holder).bindTo(getItem(position), mParentActivity);
                holder.itemView.setOnClickListener( setOnViewClickListener(getItem(position), holder.itemView));
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


    private View.OnClickListener setOnViewClickListener(DBCompleteArticle item, View itemView) {

        return new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ArticleDetailFragment.ARG_ITEM_ID, item.article.getId());
                    ArticleDetailFragment fragment = new ArticleDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.article_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(
                            (Activity) context,
                            itemView.findViewById(R.id.art_main_image),
                            itemView.findViewById(R.id.art_main_image)
                                    .getTransitionName()
                    ).toBundle();

                    Intent intent = new Intent(context, ArticleDetailActivity.class);
                    intent.putExtra(ArticleDetailFragment.ARG_ITEM_ID, item.article.getId());
                    context.startActivity(intent, bundle);
                }
            }
        };

    }


//    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            if (mTwoPane) {
//                Bundle arguments = new Bundle();
//
//                arguments.putString(ArticleDetailFragment.ARG_ITEM_ID, item.id);
//
//                ArticleDetailFragment fragment = new ArticleDetailFragment();
//                fragment.setArguments(arguments);
//                mParentActivity.getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.article_detail_container, fragment)
//                        .commit();
//            } else {
//                Context context = view.getContext();
//                Intent intent = new Intent(context, ArticleDetailActivity.class);
//
//                // intent.putExtra(ArticleDetailFragment.ARG_ITEM_ID, item.id);
//
//                context.startActivity(intent);
//            }
//        }
//    };


    /**
     * Add or remove the "loading" item based on the network state
     * @param newNetworkState
     */
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
        return networkState != null && networkState != NetworkState.LOADED;
    }
}
