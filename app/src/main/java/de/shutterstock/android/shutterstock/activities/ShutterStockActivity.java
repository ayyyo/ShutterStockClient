package de.shutterstock.android.shutterstock.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

import de.shutterstock.android.shutterstock.R;
import de.shutterstock.android.shutterstock.ShutterStockApplication;
import de.shutterstock.android.shutterstock.fragments.CategoryFragment;

public class ShutterStockActivity extends AppCompatActivity {


    static class MainFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

        static class FragmentModel {
            String mTitle;
            Class<? extends Fragment> mFragment;
            Bundle bundle;
        }

        final List<FragmentModel> mDataSet;
        final SparseArray<Fragment> mFragmentCache;

        public MainFragmentStatePagerAdapter(FragmentManager fm, List<FragmentModel> dataSet) {
            super(fm);
            mDataSet = dataSet;
            mFragmentCache = new SparseArray<>();
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            if ((fragment = mFragmentCache.get(position)) == null) {
                final FragmentModel item = mDataSet.get(position);
                fragment = Fragment.instantiate(ShutterStockApplication.getContext(),
                        item.mFragment.getName(), item.bundle);
                mFragmentCache.put(position, fragment);
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return mDataSet == null ? 0 : mDataSet.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mDataSet.get(position).mTitle;
        }
    }

    private MainFragmentStatePagerAdapter mAdapter;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.shutterstock_activity_toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.shutterstock_activity_navbar);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.shutterstock_activity_viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.shutterstock_activity_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ArrayList<MainFragmentStatePagerAdapter.FragmentModel> dataset = new ArrayList<>();
        MainFragmentStatePagerAdapter.FragmentModel model = new MainFragmentStatePagerAdapter.FragmentModel();
        model.mFragment = CategoryFragment.class;
        model.mTitle = getString(R.string.categories);
        model.bundle = new Bundle();
        dataset.add(model);
        viewPager.setAdapter(mAdapter = new MainFragmentStatePagerAdapter(getSupportFragmentManager(), dataset));
    }


    public void setupDrawerContent(NavigationView upDrawerContent) {
    }
}
