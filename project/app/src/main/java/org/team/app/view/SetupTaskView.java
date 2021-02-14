package org.team.app.view;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.widget.Button;
import android.view.View;

import org.team.app.view.R;

public class SetupTaskView extends FragmentView {
    public SetupTaskView() {
        super(R.layout.screen_setup_task);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Button button = view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    parent.replaceView(new TimerView());
                }
            });
    }
}
