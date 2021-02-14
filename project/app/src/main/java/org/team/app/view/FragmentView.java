package org.team.app.view;

import java.util.UUID;

import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import org.team.app.view.R;

public abstract class FragmentView extends Fragment {
    protected UUID uuid;
    protected ActivityListener parent;

    public FragmentView(int layoutId) {
        super(layoutId);
        uuid = UUID.randomUUID();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            parent = (ActivityListener) activity;
        } catch(ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement ActivityListener");
        }
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[" + uuid.toString() +  "}";
    }
}
