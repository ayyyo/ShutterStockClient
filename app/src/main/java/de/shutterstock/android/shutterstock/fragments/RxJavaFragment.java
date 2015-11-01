package de.shutterstock.android.shutterstock.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import de.shutterstock.android.shutterstock.R;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by emanuele on 01.11.15.
 */
public abstract class RxJavaFragment<T> extends Fragment implements Observer<T> {

    final protected CompositeSubscription mSubscriptions = new CompositeSubscription();

    protected abstract Observable<T> getObservable();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerObservable();
    }

    protected void registerObservable() {
        final Observable<T> observable = getObservable();
        if (observable == null) {
            return;
        }
        setLoadingIndicatorVisibility(View.VISIBLE);
        mSubscriptions.add(observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(this));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }

    @Override
    public void onCompleted() {
        setLoadingIndicatorVisibility(View.GONE);
    }

    public void setLoadingIndicatorVisibility(final int visibility) {
        final View view;
        if (getView() == null || (view = getView().findViewById(R.id.loader_container)) == null) {
            return;
        }
        view.setVisibility(visibility);
    }
}
