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
import android.content.SharedPreferences;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.preference.PreferenceManager;

import org.team.app.contract.TimerContract;
import org.team.app.presenter.TimerPresenter;
import org.team.app.presenter.SetupTaskPresenter;
import org.team.app.presenter.ListTaskPresenter;

import org.team.app.model.TaskStore;

import java.util.Calendar;
import java.util.UUID;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/// The main activity of the app, handles lifetimes of all other objects
public class MainActivity extends AppCompatActivity implements ActivityListener, ViewPager.OnPageChangeListener {
    protected TaskStore mTaskStore = null;
    protected TabInfo timerTab;
    protected TabInfo taskTab;
    protected ViewPager mPager;

    public final static String TASK_STORE_LATEST = "task_store_latest";

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
    public Fragment getSetupTaskFragment(UUID task) {
        final SetupTaskView view = new SetupTaskView();
        final SetupTaskPresenter presenter = new SetupTaskPresenter(view, mTaskStore, task);
        return (Fragment) view;
    }

    @Override
    public void closeFragment(Fragment frag) {
        onBackPressed();
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    @Override
    public void onPageScrolled(int pos, float off, int pixelOff) {}

    @Override
    public void onPageSelected(int pos) {
        hideKeyboard();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = (Fragment) getSupportFragmentManager()
                .findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + this.mPager.getCurrentItem());
        if (fragment != null) {
            if (fragment.getView() != null) {
                // Pop the backstack on the ChildManager if there is any. If not, close this
                // activity as normal.
                if(!fragment.getChildFragmentManager().popBackStackImmediate()) {
                    // Back button shouldn't take us back to the login screen
                }
            }
        }
    }

    private String serializeTaskStore() {
        if(mTaskStore == null)
            return null;

        System.out.println("Attempting to serialize task store.");
        String name = null;
        while(name == null) {
            name = "task_store.ser." + System.currentTimeMillis();
            File file = new File(getFilesDir(), name);
            if(file.exists())
                name = null;
        }

        System.out.println("Found available file name: " + name);

        try {
            ObjectOutputStream os = new ObjectOutputStream(openFileOutput(name, MODE_PRIVATE));
            os.writeObject(mTaskStore);
            os.close();
        } catch(IOException e) {
            System.err.println("Failed to serialize task store: ");
            e.printStackTrace();
            return null;
        }

        System.out.println("Successfully serialized task store");

        return name;
    }

    private TaskStore deserializeTaskStore(String path) {
        System.out.println("Attempting to deserialize task store @" + path);
        if(path == null)
            return null;

        TaskStore ret;

        try {
            ObjectInputStream is = new ObjectInputStream(openFileInput(path));
            ret = (TaskStore) is.readObject();
        } catch(IOException|ClassCastException|ClassNotFoundException e) {
            System.err.println("Failed to deserialize task store: ");
            e.printStackTrace();
            return null;
        }

        System.out.println("Successfully deserialized task store");

        return ret;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        String updatedPath = serializeTaskStore();
        if(updatedPath == null)
            return;

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        settings.edit()
            .putString(TASK_STORE_LATEST, updatedPath)
            .apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup Task Store
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        mTaskStore = deserializeTaskStore(settings.getString(TASK_STORE_LATEST, null));

        if (mTaskStore == null) {
            mTaskStore = new TaskStore(getResources().getString(R.string.default_task_name),
                                       getResources().getString(R.string.default_task_category));
        }

        // Setup Tabs
        taskTab = new TabInfo() {
            final ListTaskView view = new ListTaskView();
            final ListTaskPresenter presenter = new ListTaskPresenter(view, mTaskStore);

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
        this.mPager = findViewById(R.id.view_pager);
        mPager.addOnPageChangeListener(this);
        mPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(mPager);
    }

    private static class SectionsPagerAdapter extends FragmentPagerAdapter {
        private final ActivityListener mContext;

        public SectionsPagerAdapter(Context context, FragmentManager fm) {
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
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
    public void notification() {
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
