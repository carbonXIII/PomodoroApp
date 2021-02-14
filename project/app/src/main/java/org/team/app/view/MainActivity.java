package org.team.app.view;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import org.team.app.presenter.SetupTaskPresenter;
import org.team.app.presenter.TimerPresenter;

import org.team.app.model.TaskStore;

import org.team.app.view.R;

public class MainActivity extends AppCompatActivity implements ActivityListener {
    protected TaskStore mTaskStore;

    private void replaceView(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.fragmentContainerView, fragment, fragment.toString())
            .addToBackStack(fragment.toString())
            .commit();
    }

    @Override
    public void startSetupTaskView() {
        SetupTaskView view = new SetupTaskView();
        SetupTaskPresenter presenter = new SetupTaskPresenter(view, mTaskStore);
        replaceView(view);
    }

    @Override
    public void startTimerView() {
        TimerView view = new TimerView();
        TimerPresenter presenter = new TimerPresenter(view, mTaskStore);
        replaceView(view);
    }

    @Override
    public void startContinueView() { replaceView(new ContinueView()); }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_main);

        mTaskStore = new TaskStore();

        if(savedInstanceState == null) {
            startSetupTaskView();
        }
    }
}
