package org.team.app.view;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import org.team.app.view.R;

public class MainActivity extends AppCompatActivity implements ActivityListener {
    public void replaceView(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.fragmentContainerView, fragment, fragment.toString())
            .addToBackStack(fragment.toString())
            .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_main);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragmentContainerView, SetupTaskView.class, null)
                .commit();
        }
    }
}
