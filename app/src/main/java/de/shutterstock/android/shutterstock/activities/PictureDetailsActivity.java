package de.shutterstock.android.shutterstock.activities;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import de.shutterstock.android.shutterstock.R;
import de.shutterstock.android.shutterstock.content.model.Image;
import de.shutterstock.android.shutterstock.fragments.ContributorFragment;

/**
 * Created by emanuele on 29/11/15.
 */
public class PictureDetailsActivity extends AppCompatActivity {


    public static final String IMAGE_KEY = "IMAGE_KEY";
    private static final String CONTRIBUTOR_TAG = "CONTRIBUTOR_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pictures_details_fragment);

        final Image image = getIntent().getParcelableExtra(IMAGE_KEY);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbarCollapse);
        toolbarLayout.setTitle(image.description);


        Picasso.with(this).load(image.assets.preview.url).into((ImageView) findViewById(R.id.image));

        if (savedInstanceState == null) {
            Fragment fragment = new ContributorFragment();
            Bundle bundle = new Bundle();
            bundle.putString(ContributorFragment.CONTRIBUTOR_KEY, image.contributor.id);
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.contributor_container, fragment, CONTRIBUTOR_TAG).commit();
        }
    }
}
