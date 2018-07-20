package com.george.balasca.articleregistry.ui;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.george.balasca.articleregistry.R;
import com.george.balasca.articleregistry.model.apiresponse.Article;

import static com.george.balasca.articleregistry.model.apiresponse.Article.DIFF_CALLBACK;

public class ArticleListAdapter extends PagedListAdapter<Article, ArticleListAdapter.ArticleViewHolder> {

    private static final String TAG = ArticleListAdapter.class.getSimpleName();
    private final ArticleListActivity mParentActivity;
    private final Boolean mTwoPane;

    public ArticleListAdapter(ArticleListActivity parent,  boolean twoPane) {
        super(DIFF_CALLBACK);
        mParentActivity = parent;
        mTwoPane = twoPane;
    }


    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.article_list_content, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        Article article = getItem(position);
        if(article != null) {
            holder.bindTo(article);
            holder.itemView.setOnClickListener(mOnClickListener);
        }
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView articleItemView;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            articleItemView = itemView.findViewById(R.id.content);
        }

        public void bindTo(Article article) {
            articleItemView.setText(article.getHeadline().getMain());
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

//    public static DiffUtil.ItemCallback<ArticleWithHeadlineAndMultimedia> DIFF_CALLBACK = new DiffUtil.ItemCallback<ArticleWithHeadlineAndMultimedia>() {
//        @Override
//        public boolean areItemsTheSame(@NonNull ArticleWithHeadlineAndMultimedia oldItem, @NonNull ArticleWithHeadlineAndMultimedia newItem) {
//            Boolean comparator = oldItem.article == newItem.article;
//
////            Log.d(TAG, oldItem.article.getHeadline().getHeadline_main()+ "<-------->" + newItem.article.getHeadline().getHeadline_main());
////            Log.d(TAG, "areItemsTheSame " + comparator);
//            return comparator;
//        }
//
//        @Override
//        public boolean areContentsTheSame(@NonNull ArticleWithHeadlineAndMultimedia oldItem, @NonNull ArticleWithHeadlineAndMultimedia newItem) {
//            Boolean comparator = oldItem.equals(newItem);
////            Log.d(TAG, "areContentsTheSame " + comparator);
//            return comparator;
//        }
//    };
}
