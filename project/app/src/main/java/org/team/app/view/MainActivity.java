package org.team.app.view;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.team.app.presenter.TimerPresenter;
import org.team.app.presenter.SetupTaskPresenter;

import org.team.app.model.TaskStore;

public class MainActivity extends AppCompatActivity implements ActivityListener {
    protected TaskStore mTaskStore;
    protected TabInfo timerTab;
    protected TabInfo taskTab;

    @Override
    public void startSetupTaskView() {}

    @Override
    public void startTimerView() {}

    @Override
    public TabInfo getTab(int position) {
        if(position == 0) {
            return taskTab;
        } else if(position == 1) {
            return timerTab;
        } else {
            throw new RuntimeException("Tab not implemented");
        }
    }

    @Override
    public int tabCount() {
        return 2;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup Task Store
        mTaskStore = new TaskStore(getResources().getString(R.string.default_task_name));

        // Setup Tabs
        taskTab = new TabInfo() {
            final SetupTaskView view = new SetupTaskView();
            final SetupTaskPresenter presenter = new SetupTaskPresenter(view, mTaskStore);

            @Override
            public String getTitle() {
                return "Task";
            }

            @Override
            public Fragment getFragment() {
                return view;
            }
        };

        timerTab = new TabInfo() {
            final TimerView view = new TimerView();
            final TimerPresenter presenter = new TimerPresenter(view, mTaskStore);

            @Override
            public String getTitle() {
                return "Timer";
            }

            @Override
            public Fragment getFragment() {
                return view;
            }
        };

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    static class SectionsPagerAdapter extends FragmentPagerAdapter {
        private final ActivityListener mContext;

        public SectionsPagerAdapter(Context context, FragmentManager fm) {
            super(fm);
            mContext = (ActivityListener)context;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return mContext.getTab(position).getFragment();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mContext.getTab(position).getTitle();
        }

        @Override
        public int getCount() {
            return mContext.tabCount();
        }
    }
}
