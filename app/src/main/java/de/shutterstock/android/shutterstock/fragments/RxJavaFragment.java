package de.shutterstock.android.shutterstock.fragments;

import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.Snackbar;
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
    protected T mResponse;

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
                .unsubscribeOn(Schedulers.computation())
                .subscribe(this));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSubscriptions != null) {
            mSubscriptions.unsubscribe();
        }
    }



    @Override
    public void onError(Throwable e) {
        showSnackBar(e.getMessage() + " ");
    }

    private void showSnackBar(final int message) {
        showSnackBar(getString(message));
    }

    private void showSnackBar(final String message) {
        if (!isAdded() || !isResumed() || isRemoving() || getActivity() == null) {
            return;
        }
        View view;
        if ((view = getActivity().findViewById(R.id.shutterstock_activity_container)) == null) {
            view = getActivity().findViewById(android.R.id.content);
        }
        if (view != null) {
            final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
            if (Looper.getMainLooper() == Looper.myLooper()) {
                snackbar.show();
            } else {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        snackbar.show();
                    }
                });
            }
        }
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
