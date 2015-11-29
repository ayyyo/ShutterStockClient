package de.shutterstock.android.shutterstock.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

import de.shutterstock.android.shutterstock.R;
import de.shutterstock.android.shutterstock.content.model.PagedResponse;

/**
 * Created by emanuele on 07/11/15.
 */
public abstract class RxJavaPagedFragment<T> extends RxJavaFragment<PagedResponse<T>> implements OnLoadNextPage {

    public static class EndlessScrollListener extends RecyclerView.OnScrollListener {

        private final RecyclerView.LayoutManager mLayoutManager;
        private final WeakReference<OnLoadNextPage> mListener;

        private int mFirstVisibleItem;
        private int mVisibleItemCount;
        private int mTotalItemCount;

        private int[] mInto;

        public EndlessScrollListener(RecyclerView.LayoutManager layoutManager, final OnLoadNextPage loadNextPage) {
            mLayoutManager = layoutManager;
            mListener = new WeakReference<>(loadNextPage);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            mVisibleItemCount = mLayoutManager.getChildCount();
            mTotalItemCount = mLayoutManager.getItemCount();
            if (mLayoutManager instanceof LinearLayoutManager) {
                mFirstVisibleItem = ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
            } else if (mLayoutManager instanceof GridLayoutManager) {
                mFirstVisibleItem = ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
            } else if (mLayoutManager instanceof StaggeredGridLayoutManager) {
                mInto = ((StaggeredGridLayoutManager) mLayoutManager).findFirstVisibleItemPositions(mInto);
                mFirstVisibleItem = mInto[0];
            } else {
                return;
            }

            if ((mVisibleItemCount + mFirstVisibleItem + THRESHOLD) >= mTotalItemCount) {
                if (mListener != null && mListener.get() != null) {
                    mListener.get().onLoadNext();
                }
            }
        }
    }

    private static final int THRESHOLD = 8;

    protected RecyclerView mRecyclerView;
    protected volatile boolean isAlreadyLoading = false;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recyclerview_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mResponse = null;
                onLoadNext();
            }
        });
    }

    @Override
    public void onLoadNext() {
        if (isAlreadyLoading || (mResponse != null && mResponse.isLastPage())) {
            return;
        }
        isAlreadyLoading = true;
        showLoadingIndicator(true);
        registerObservable();
    }

    @Override
    public void onCompleted() {
        super.onCompleted();
        showLoadingIndicator(false);
        isAlreadyLoading = false;
    }

    @Override
    public void onNext(PagedResponse<T> t) {
        mResponse = t;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected void showLoadingIndicator(final boolean show) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(show);
        }
    }
}
