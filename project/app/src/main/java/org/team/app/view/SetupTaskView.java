package org.team.app.view;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;

import org.team.app.view.R;
import org.team.app.contract.SetupTaskContract;

public class SetupTaskView extends FragmentView implements SetupTaskContract.View {
    private SetupTaskContract.Presenter mPresenter;

    public SetupTaskView() {
        super(R.layout.screen_setup_task);
    }

    @Override
    public void setPresenter(SetupTaskContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void complete() {
        mActivity.startTimerView();
    }

    @Override
    public void onResume() {
        super.onResume();

        mPresenter.start();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final EditText taskNameText = view.findViewById(R.id.editTextTaskName);
        final Button button = view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String taskName = taskNameText.getText().toString();
                    mPresenter.submitForm(taskName);
                }
            });
    }
}
