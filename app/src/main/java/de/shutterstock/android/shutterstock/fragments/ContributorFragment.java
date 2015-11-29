package de.shutterstock.android.shutterstock.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.shutterstock.android.shutterstock.R;
import de.shutterstock.android.shutterstock.content.model.Contributor;
import de.shutterstock.android.shutterstock.net.RestClient;
import rx.Observable;

/**
 * Created by emanuele on 29/11/15.
 */
public class ContributorFragment extends RxJavaFragment<Contributor> {

    public static final java.lang.String CONTRIBUTOR_KEY = "CONTRIBUTOR_KEY";
    private String mContributorId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contributor_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() == null) {
            return;
        }
        mContributorId = getArguments().getString(CONTRIBUTOR_KEY);
    }

    @Override
    protected Observable<Contributor> getObservable() {
        return RestClient.getApiDescriptor().getContributor(mContributorId);
    }

    @Override
    public void onNext(Contributor contributor) {
        setupContributor(contributor);
    }

    private void setupContributor(final Contributor contributor) {
        final View view;
        if ((view = getView()) == null || isWrongStatus()) {
            return;
        }
        ((TextView) view.findViewById(R.id.contributor_name)).setText(contributor.displayName);
        ((TextView) view.findViewById(R.id.contributor_location)).setText(getString(R.string.location, contributor.location));
        ((TextView) view.findViewById(R.id.contributor_about)).setText(contributor.about);
        ((TextView) view.findViewById(R.id.contributor_portfolio)).setText(contributor.portfolioUrl);
    }
}
