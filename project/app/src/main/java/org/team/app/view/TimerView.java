package org.team.app.view;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;

import org.team.app.view.R;
import org.team.app.contract.TimerContract;

public class TimerView extends FragmentView implements TimerContract.View {
    protected TimerContract.Presenter mPresenter;

    public TimerView() {
        super(R.layout.screen_timer);
    }

    @Override
    public void setPresenter(TimerContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void setTitle(String title, String taskName) {
        View view = getView();

        final TextView titleText = view.findViewById(R.id.text_timer_title);
        titleText.setText(title);

        final TextView taskNameText = view.findViewById(R.id.text_task_name);
        taskNameText.setText(taskName);
    }

    @Override
    public void complete() {
        mActivity.startContinueView();
    }

    @Override
    public void onResume() {
        super.onResume();

        mPresenter.start();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Button button = view.findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mPresenter.onTimerComplete();
                }
            });
    }
}
