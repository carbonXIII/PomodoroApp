package org.team.app.view;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.core.app.NotificationManagerCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.team.app.contract.TimerContract;
import org.team.app.presenter.TimerPresenter;
import org.team.app.presenter.SetupTaskPresenter;

import org.team.app.model.TaskStore;

import java.util.Calendar;

/// The main activity of the app, handles lifetimes of all other objects
public class MainActivity extends AppCompatActivity implements ActivityListener, ViewPager.OnPageChangeListener {
    protected TaskStore mTaskStore;
    protected TabInfo timerTab;
    protected TabInfo taskTab;

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

    @Override
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        View view = getCurrentFocus();
        if (view == null)
            return;

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    @Override
    public void onPageScrolled(int pos, float off, int pixelOff) {}

    @Override
    public void onPageSelected(int pos) {
        hideKeyboard();
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
        viewPager.addOnPageChangeListener(this);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    private static class SectionsPagerAdapter extends FragmentPagerAdapter {
        private final ActivityListener mContext;

        public SectionsPagerAdapter(Context context, FragmentManager fm) {
            super(fm);
            mContext = (ActivityListener)context;
        }

        @Override
        public Fragment getItem(int position) {
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

    // Notification function, called in "onCreate"
    public void Notification() {

        System.out.println(TimerPresenter.checkTimer);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar cal = Calendar.getInstance();
        // Set to immediately show notification, yet there are still delays, I believe because too much work is being done
        // on the main thread.
        cal.add(Calendar.SECOND, 0);
        // RTC_WAKEUP wakes up the device to fire the pending intent at the desired time
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);

    }

}
