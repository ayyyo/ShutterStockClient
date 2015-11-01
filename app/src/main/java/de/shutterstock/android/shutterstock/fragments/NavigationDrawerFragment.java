package de.shutterstock.android.shutterstock.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.shutterstock.android.shutterstock.R;
import de.shutterstock.android.shutterstock.content.model.User;
import de.shutterstock.android.shutterstock.net.RestClient;
import de.shutterstock.android.shutterstock.utilities.SSSharedPreferences;
import rx.Observable;

/**
 * Created by emanuele on 01.11.15.
 */
public class NavigationDrawerFragment extends RxJavaFragment<User> {

    private User mUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.navigation_drawer_header_fragment, container, false);
    }


    private void updateView() {
        final View view;
        if ((view = getView()) == null) {
            return;
        }
        view.findViewById(R.id.user_container).setVisibility(View.VISIBLE);
        ((TextView) view.findViewById(R.id.username)).setText(mUser.username);
        ((TextView) view.findViewById(R.id.full_name)).setText(mUser.full_name);
    }

    @Override
    public void onCompleted() {
        super.onCompleted();
        updateView();
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onNext(User user) {
        mUser = user;
    }

    @Override
    protected Observable<User> getObservable() {
        if (mUser != null) {
            updateView();
            return null;
        }
        return RestClient.getApiDescriptor().getUser();
    }
}
