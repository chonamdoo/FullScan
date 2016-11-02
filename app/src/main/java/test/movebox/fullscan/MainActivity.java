package test.movebox.fullscan;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Author JackSparrow
 * Create Date 01/11/2016.
 */

public class MainActivity extends AppCompatActivity {
    public static final int TAB_BOOST = 0;
    public static final int TAB_CLEAN = 1;
    public static final int TAB_SECURITY = 2;

    private static final int TAB_COUNT = 3;

    private SparseArray<Fragment> fragments;
    private TabLayout tabLayout;
    private int lastSelectTab;
    private FullScanView fullScanView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        fragments = new SparseArray<>(TAB_COUNT);
        fragments.put(TAB_BOOST, new Fragment());
        fragments.put(TAB_CLEAN, new Fragment());
        fragments.put(TAB_SECURITY, new Fragment());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(TAB_COUNT);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return TAB_COUNT;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case TAB_BOOST:
                        return getString(R.string.main_tab_boost);
                    case TAB_CLEAN:
                        return getString(R.string.main_tab_clean);
                    case TAB_SECURITY:
                        return getString(R.string.main_tab_security);
                    default:
                        return null;
                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setUnselectedTab(lastSelectTab);
                setSelectedTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        for (int i = 0; i < TAB_COUNT; i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            View view = loadUnselectedTabView(i);
            tab.setCustomView(view);
            tab.setTag(view);
        }

        viewPager.setCurrentItem(lastSelectTab);

        setSelectedTab(lastSelectTab);

        fullScanView = (FullScanView) findViewById(R.id.full_scan);
        fullScanView.setTabLayout(tabLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_full_scan:
                fullScanView = (FullScanView) findViewById(R.id.full_scan);
                fullScanView.startAnimation(lastSelectTab);

                tabLayout.setVisibility(View.INVISIBLE);
                toolbar.animate().alpha(0).setDuration(195).translationY(-DisplayUtil.dpToPx(8)).start();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private View loadUnselectedTabView(int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.custom_tab_main, null);
        int screenWidth = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth / TAB_COUNT, 180);
        view.setLayoutParams(layoutParams);

        TextView textView = (TextView) view.findViewById(R.id.title);
        AppCompatImageView icon = (AppCompatImageView) view.findViewById(R.id.tab_icon);

        switch (position) {
            case TAB_BOOST:
                textView.setText(getString(R.string.main_tab_boost));
                icon.setImageResource(R.drawable.ic_main_boost);
                break;
            case TAB_CLEAN:
                textView.setText(getString(R.string.main_tab_clean));
                icon.setImageResource(R.drawable.ic_main_clean);
                break;
            case TAB_SECURITY:
                textView.setText(getString(R.string.main_tab_security));
                icon.setImageResource(R.drawable.ic_main_security);
                break;
        }

        return view;
    }

    private void setSelectedTab(int position) {
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        View tabView = (View) tab.getTag();

        TextView title = (TextView) tabView.findViewById(R.id.title);
        AppCompatImageView icon = (AppCompatImageView) tabView.findViewById(R.id.tab_icon);
        icon.setColorFilter(getResources().getColor(R.color.primary_blue));

        title.setTextColor(getResources().getColor(R.color.primary_blue));
        lastSelectTab = position;
    }

    private void setUnselectedTab(int position) {
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        View tabView = (View) tab.getTag();

        TextView title = (TextView) tabView.findViewById(R.id.title);
        AppCompatImageView icon = (AppCompatImageView) tabView.findViewById(R.id.tab_icon);
        icon.setColorFilter(getResources().getColor(R.color.tab_icon_color));
        title.setTextColor(getResources().getColor(R.color.tab_icon_color));
    }
}
