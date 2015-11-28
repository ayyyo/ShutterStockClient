package de.shutterstock.android.shutterstock.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.shutterstock.android.shutterstock.R;
import de.shutterstock.android.shutterstock.content.model.Category;
import de.shutterstock.android.shutterstock.content.model.PagedResponse;
import de.shutterstock.android.shutterstock.net.RestClient;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by emanuele on 01.11.15.
 */
public class CategoryFragment extends RxJavaPagedFragment<Category> {

    private static class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

        public static class CategoryViewHolder extends RecyclerView.ViewHolder {

            private Category mCategory;
            private TextView mCategoryView;

            public CategoryViewHolder(View itemView) {
                super(itemView);
                mCategoryView = (TextView) itemView.findViewById(R.id.category_name);
            }

            public void setCategory(final Category category) {
                mCategory = category;
                updateView();
            }

            private void updateView() {
                if (mCategory == null) {
                    return;
                }
                mCategoryView.setText(mCategory.name);
            }
        }

        private final List<Category> mCategories;

        CategoryAdapter() {
            mCategories = new ArrayList<>();
        }

        CategoryAdapter(final List<Category> categoryList) {
            mCategories = categoryList;
        }

        public void addAll(final List<Category> categoryList) {
            final int currentSize = mCategories.size();
            synchronized (mCategories) {
                mCategories.addAll(categoryList);
            }
            notifyItemRangeInserted(currentSize, categoryList.size());
        }

        @Override
        public CategoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new CategoryViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_item_layout, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(CategoryViewHolder categoryViewHolder, int i) {
            final Category category;
            synchronized (mCategories) {
                category = mCategories.get(i);
            }
            categoryViewHolder.setCategory(category);
        }

        @Override
        public int getItemCount() {
            return mCategories == null ? 0 : mCategories.size();
        }
    }


    private RecyclerView mRecyclerView;
    private CategoryAdapter mAdapter;
    private CompositeSubscription mSubscriptions = new CompositeSubscription();

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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter = new CategoryAdapter());
    }


    @Override
    public void onError(Throwable e) {
        super.onError(e);
        e.printStackTrace();
    }

    @Override
    public void onNext(PagedResponse<Category> categories) {
        super.onNext(categories);
        if (mAdapter == null) {
            mAdapter = new CategoryAdapter();
            mRecyclerView.setAdapter(mAdapter);
        }
        mAdapter.addAll(categories.getData());
    }

    @Override
    protected Observable<PagedResponse<Category>> getObservable() {
        return RestClient.getApiDescriptor().getCategories();
    }

}
