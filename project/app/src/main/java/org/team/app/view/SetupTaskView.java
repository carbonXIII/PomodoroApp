package org.team.app.view;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import org.team.app.view.R;
import org.team.app.contract.SetupTaskContract;

/// The fragment for the Task Setup screen
public class SetupTaskView extends FragmentView implements SetupTaskContract.View {
    private SetupTaskContract.Presenter mPresenter;

    protected EditText taskNameText;

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
    public void onResume() {
        super.onResume();

        mPresenter.start();
    }

    private void submitTaskName() {
        String taskName = taskNameText.getText().toString();
        mPresenter.setTaskName(taskName);
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
                        submitTaskName();
                        return true;
                    }

                    return false;
                }
            });

        final Button button = view.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    submitTaskName();
                    // Toast pop up message to clarify that the task name has been updated
                    Toast.makeText(getActivity(), "Task Name Updated", Toast.LENGTH_SHORT).show();
                }
            });
    }


}
