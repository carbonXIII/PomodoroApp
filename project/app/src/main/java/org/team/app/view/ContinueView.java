package org.team.app.view;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.widget.Button;
import android.view.View;

import org.team.app.view.R;

public class ContinueView extends FragmentView {
    public ContinueView() {
        super(R.layout.screen_continue);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Button breakButton = view.findViewById(R.id.button_break);
        breakButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mActivity.startTimerView();
                }
            });

        final Button doneButton = view.findViewById(R.id.button_done);
        doneButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mActivity.startSetupTaskView();
                }
            });
    }
}
