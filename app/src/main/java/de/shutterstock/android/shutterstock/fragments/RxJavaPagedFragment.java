package de.shutterstock.android.shutterstock.fragments;

import de.shutterstock.android.shutterstock.content.model.PagedResponse;

/**
 * Created by emanuele on 07/11/15.
 */
public abstract class RxJavaPagedFragment<T> extends RxJavaFragment<PagedResponse<T>> {


    protected void loadNextPage() {
        registerObservable();
    }


    @Override
    public void onNext(PagedResponse<T> t) {
        mResponse = t;
    }
}
