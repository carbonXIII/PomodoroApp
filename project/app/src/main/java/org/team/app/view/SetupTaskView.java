package org.team.app.view;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.view.View;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import org.team.app.model.TimerType;
import org.team.app.view.R;
import org.team.app.contract.SetupTaskContract;

import javax.xml.datatype.Duration;

/// The fragment for the Task Setup screen
public class SetupTaskView extends FragmentView implements SetupTaskContract.View {
    private SetupTaskContract.Presenter mPresenter;

    protected EditText taskNameText;
    protected EditText taskWorkTimeText;
    protected EditText taskBreakTimeText;

    public SetupTaskView() {
        super(R.layout.screen_setup_task);
    }

    @Override
    public void setPresenter(SetupTaskContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void setTaskName(String name) {
        taskNameText.setText(name);
    }

    @Override
    public void setTaskTime(TimerType type, long duration)
    {
        // Convert Duration to minutes
        duration = duration / (60 * 1000);

        switch(type) {
            case WORK:
                taskWorkTimeText.setText(String.valueOf(duration));
                break;
            case BREAK:
                taskBreakTimeText.setText(String.valueOf(duration));
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    private void submitTaskName(boolean toast) {
        String taskName = taskNameText.getText().toString();
        mPresenter.setTaskName(taskName);

        if(toast)
            Toast.makeText(getActivity(), "Task Details Updated", Toast.LENGTH_SHORT).show();
    }

    public void submitTaskTime(TimerType type, boolean toast){
        long duration = 0;
        switch(type) {
            case WORK:
                duration = Long.parseLong(taskWorkTimeText.getText().toString());
                break;
            case BREAK:
                duration = Long.parseLong(taskBreakTimeText.getText().toString());
                break;
        }
        mPresenter.setTaskTime(type, duration * 60 * 1000);

        if(toast)
            Toast.makeText(getActivity(), "Task Details Updated", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        taskNameText = view.findViewById(R.id.editTextTaskName);
        taskNameText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if(actionId == EditorInfo.IME_ACTION_DONE
                       || (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN)) {
                        mActivity.hideKeyboard();
                        submitTaskName(true);
                        return true;
                    }

                    return false;
                }
            });

        taskWorkTimeText = view.findViewById(R.id.editTextNumberWork);
        taskWorkTimeText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE
                        || (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN)) {
                    mActivity.hideKeyboard();
                    submitTaskTime(TimerType.WORK, true);
                    return true;
                }

                return false;
            }
        });


        taskBreakTimeText = view.findViewById(R.id.editTextNumberBreak);
        taskBreakTimeText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE
                        || (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN)) {
                    mActivity.hideKeyboard();
                    submitTaskTime(TimerType.BREAK, true);
                    return true;
                }

                return false;
            }
        });

        final Switch settingsSwitch = view.findViewById(R.id.settingsSwitch);
        final androidx.constraintlayout.helper.widget.Layer settingsLayer = view.findViewById(R.id.settingsLayer);
        settingsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    settingsLayer.setVisibility(View.VISIBLE);
                else
                    settingsLayer.setVisibility(View.GONE);
            }
        });

        final Button saveButton = view.findViewById(R.id.button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submitTaskName(true);
                }
            });

        final Button doneButton = view.findViewById(R.id.button_done);

        final Fragment parentRef = (Fragment) this;
        doneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submitTaskTime(TimerType.WORK, false);
                    submitTaskTime(TimerType.BREAK, false);
                    submitTaskName(true);

                    mActivity.closeFragment(parentRef);
                }
        });
    }


}
