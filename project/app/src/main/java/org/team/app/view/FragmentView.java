package org.team.app.view;

import java.util.UUID;

import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import org.team.app.view.R;
import org.team.app.contract.BasePresenter;
import org.team.app.contract.BaseView;

/// Base class for all Fragments
public abstract class FragmentView extends Fragment {
    protected final UUID uuid;
    protected ActivityListener mActivity;

    /// Construct a fragment
    /// @param layoutId: The resource id of the layout for this fragment
    public FragmentView(int layoutId) {
        super(layoutId);
        uuid = UUID.randomUUID();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mActivity = (ActivityListener) activity;
        } catch(ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement ActivityListener");
        }
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[" + uuid.toString() +  "}";
    }
}
