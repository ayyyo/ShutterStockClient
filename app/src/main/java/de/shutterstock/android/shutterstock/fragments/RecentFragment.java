package de.shutterstock.android.shutterstock.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

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


    public static final String SORTING_KEY = "SORTING_KEY";

    private ImageAdapter mAdapter;

    private String mSorting;
    private GridLayoutManager mGridLayoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSorting = getArguments() == null ? "newest" : getArguments().getString(SORTING_KEY);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mGridLayoutManager = new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter = new ImageAdapter());
        mRecyclerView.addOnScrollListener(new EndlessScrollListener(mGridLayoutManager, this));
    }

    @Override
    protected Observable<PagedResponse<Image>> getObservable() {
        return RestClient.getApiDescriptor().getImages(
                getSearchFilters());
    }

    protected HashMap<String, String> getSearchFilters() {
        HashMap<String, String> query = new HashMap<>();
        final String page = mResponse == null ? String.valueOf(1) : String.valueOf(mResponse.getCurrentPage() + 1);
        Log.i(getClass().getSimpleName(), " loading page " + page);
        query.put("page", page);
        query.put("per_page", "30");
        query.put("view", "full");
        query.put("sort", mSorting);
        return query;
    }


    @Override
    public void onNext(PagedResponse<Image> image) {
        super.onNext(image);
        if (mAdapter != null) {
            mAdapter.addAll(image.getData());
        }
    }
}
