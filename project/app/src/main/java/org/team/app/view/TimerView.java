package org.team.app.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;

import org.team.app.contract.TimerContract;

public class TimerView extends FragmentView implements TimerContract.View {
    protected TimerContract.Presenter mPresenter;
    protected TextView titleText;
    protected TextView taskNameText;

    public TimerView() {
        super(R.layout.screen_timer);
    }

    @Override
    public void setPresenter(TimerContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void setTaskName(String taskName) {
        taskNameText.setText(taskName);
    }

    @Override
    public void setTimerType(TimerContract.TimerType type) {
        switch(type) {
        case WORK: titleText.setText("Work Time"); break;
        case BREAK: titleText.setText("Break Time"); break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titleText = view.findViewById(R.id.text_task_title);
        taskNameText = view.findViewById(R.id.text_task_name);
    }
}
