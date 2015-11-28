package de.shutterstock.android.shutterstock.fragments;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.lang.ref.WeakReference;

import de.shutterstock.android.shutterstock.content.model.PagedResponse;

/**
 * Created by emanuele on 07/11/15.
 */
public abstract class RxJavaPagedFragment<T> extends RxJavaFragment<PagedResponse<T>> implements OnLoadNextPage {

    public static class EndlessScrollListener extends RecyclerView.OnScrollListener {

        private final RecyclerView.LayoutManager mLayoutManager;
        private final WeakReference<OnLoadNextPage> mListener;

        private int mPastVisibleItems;
        private int mVisibleItemCount;
        private int mTotalItemCount;


        public EndlessScrollListener(RecyclerView.LayoutManager layoutManager, final OnLoadNextPage loadNextPage) {
            mLayoutManager = layoutManager;
            mListener = new WeakReference<OnLoadNextPage>(loadNextPage);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            mVisibleItemCount = mLayoutManager.getChildCount();
            mTotalItemCount = mLayoutManager.getItemCount();
            if (mLayoutManager instanceof LinearLayoutManager) {
                mPastVisibleItems = ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
            } else if (mLayoutManager instanceof GridLayoutManager) {
                mPastVisibleItems = ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
            } else {
                return;
            }

            if ((mVisibleItemCount + mPastVisibleItems + THRESHOLD) >= mTotalItemCount) {
                if (mListener != null && mListener.get() != null) {
                    mListener.get().onLoadNext();
                }
            }
        }
    }

    private static final int THRESHOLD = 8;
    protected volatile boolean isAlreadyLoading = false;

    @Override
    public void onLoadNext() {
        if (isAlreadyLoading || (mResponse != null && mResponse.isLastPage())) {
            return;
        }
        isAlreadyLoading = true;
        registerObservable();
    }

    @Override
    public void onCompleted() {
        super.onCompleted();
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
}
