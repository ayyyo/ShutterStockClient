package de.shutterstock.android.shutterstock.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import java.util.HashMap;
import java.util.Random;

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
    private static final int GRID_COLUMN_COUNT = 3;

    private ImageAdapter mAdapter;

    private String mSorting;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSorting = getArguments() == null ? "newest" : getArguments().getString(SORTING_KEY);
    }


    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.accent_material_light));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager = new GridLayoutManager(getActivity(), GRID_COLUMN_COUNT));
        mRecyclerView.setAdapter(mAdapter = new ImageAdapter());
        mRecyclerView.addOnScrollListener(new EndlessScrollListener(mLayoutManager, this));

        ((GridLayoutManager) mLayoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            final SparseArray<Integer> mSparseArray = new SparseArray<Integer>();
            final Random mRandom = new Random();

            @Override
            public int getSpanSize(int position) {
                int span;
                if (mSparseArray.get(position) == null) {
                    span = 1 + mRandom.nextInt(GRID_COLUMN_COUNT);
                    if (span == GRID_COLUMN_COUNT) {
                        mSparseArray.put(position, span);
                        return span;
                    }
                    int tmpSpan = span;
                    int i = position;
                    while (tmpSpan <= GRID_COLUMN_COUNT) {
                        mSparseArray.put(i++, span);
                        int nexRand = GRID_COLUMN_COUNT - tmpSpan == 0 ? 1 : GRID_COLUMN_COUNT - tmpSpan;
                        span = 1 + mRandom.nextInt(nexRand);
                        tmpSpan += span;
                    }
                }
                return mSparseArray.get(position);
            }
        });
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
