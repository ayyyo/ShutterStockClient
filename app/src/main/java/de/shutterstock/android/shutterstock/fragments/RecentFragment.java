package de.shutterstock.android.shutterstock.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

import de.shutterstock.android.shutterstock.R;
import de.shutterstock.android.shutterstock.adapters.ImageAdapter;
import de.shutterstock.android.shutterstock.content.model.Image;
import de.shutterstock.android.shutterstock.content.model.PagedResponse;
import de.shutterstock.android.shutterstock.net.RestClient;
import rx.Observable;

/**
 * Created by emanuele on 06/11/15.
 */
public class RecentFragment extends RxJavaPagedFragment<Image> {

    private final class SSGOnScrollListener extends RecyclerView.OnScrollListener {

        private RecyclerView.LayoutManager mLayoutManager;

        SSGOnScrollListener(RecyclerView.LayoutManager layoutManager) {
            mLayoutManager = layoutManager;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            mVisibleItemCount = mLayoutManager.getChildCount();
            mTotalItemCount = mLayoutManager.getItemCount();
            //mPastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

            if (mResponse != null) {
                if (mResponse.isLastPage() || isAlreadyLoading) {
                    return;
                }

                if ((mVisibleItemCount + mPastVisibleItems + THRESHOLD) >= mTotalItemCount) {
                    isAlreadyLoading = true;
                    loadNextPage();
                }
            }
        }
    }

    public static final String SORTING_KEY = "SORTING_KEY";
    private static final int THRESHOLD = 8;

    private int mPastVisibleItems;
    private int mVisibleItemCount;
    private int mTotalItemCount;
    private volatile boolean isAlreadyLoading = false;
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;

    private String mSorting;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSorting = getArguments() == null ? "newest" : getArguments().getString(SORTING_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recyclerview_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter = new ImageAdapter());
    }

    @Override
    protected Observable<PagedResponse<Image>> getObservable() {
        return RestClient.getApiDescriptor().getImages(
                getSearchFilters());
    }

    protected HashMap<String, String> getSearchFilters() {
        HashMap<String, String> query = new HashMap<>();
        query.put("page", mResponse == null ? String.valueOf(1) : String.valueOf(mResponse.getCurrentPage() + 1));
        query.put("per_page", "30");
        query.put("view", "full");
        query.put("sort", mSorting );
        return query;
    }


    @Override
    public void onNext(PagedResponse<Image> image) {
        if (mAdapter != null) {
            mAdapter.addAll(image.getData());
        }
    }
}
