package org.team.app.view;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.view.LayoutInflater;
import android.text.Editable;
import android.text.TextWatcher;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.content.Context;
import android.widget.Toast;

import org.team.app.contract.ListTaskContract;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.UUID;

public class ListTaskView extends FragmentView implements ListTaskContract.View {
    protected ListTaskContract.Presenter mPresenter;

    protected RecyclerView mRecycler;
    protected LinearLayoutManager mLayoutManager;
    protected Adapter mAdapter;

    protected List<UUID> taskList;
    protected UUID selectedTask = null;

    protected int colorItemBase;
    protected int colorItemAlt;
    protected int colorItemSelected;

    public ListTaskView() {
        super(R.layout.screen_list_task);

        taskList = new ArrayList<UUID>();
    }

    @Override
    public void setPresenter(ListTaskContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.pause();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        colorItemBase = view.getResources().getColor(R.color.colorItemBase);
        colorItemAlt = view.getResources().getColor(R.color.colorItemAlt);
        colorItemSelected = view.getResources().getColor(R.color.colorItemSelected);

        final Button button = view.findViewById(R.id.button_new);
        button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UUID task = mPresenter.createNewTask();
                    showSetupTaskFragment(task);
                }
            });

        final EditText search = view.findViewById(R.id.text_search);
        search.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    mPresenter.updateFilter(search.getText().toString());
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
        });

        mRecycler = view.findViewById(R.id.view_recycler);

        mLayoutManager = new LinearLayoutManager((Context)mActivity);
        mRecycler.setLayoutManager(mLayoutManager);

        mAdapter = new Adapter();
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void updateTaskList(Collection<UUID> task) {
        taskList = new ArrayList<UUID>(task);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void selectCurrentTask(UUID task) {
        int prevPos = -1;
        if(this.selectedTask != null)
            prevPos = taskList.indexOf(this.selectedTask);

        this.selectedTask = task;
        int pos = taskList.indexOf(this.selectedTask);

        // Only update the relevant positions, rather than the entire data set
        if (prevPos < 0)
            mAdapter.notifyItemChanged(pos);
        if(pos >= 0)
            mAdapter.notifyItemChanged(pos);
    }

    public void showSetupTaskFragment(UUID task) {
        mActivity.hideKeyboard();

        // Replace the view of the list layout, instead of the frame of the parent layout,
        // or the view of the view pager, to avoid annoying view pager manipulation to
        // show the setup screen.
        getParentFragmentManager().beginTransaction()
            .replace(getView().getId(), mActivity.getSetupTaskFragment(task))
            .addToBackStack(null)
            .commit();
    }

    class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
        class ViewHolder extends RecyclerView.ViewHolder {
            protected final TextView taskNameText;
            protected final ImageButton editButton;

            protected UUID task = null;

            public ViewHolder(View v) {
                super(v);

                v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(task == null)
                                return;

                            if(mPresenter.selectCurrentTask(task))
                                Toast.makeText(getActivity(), "Selected New Task", Toast.LENGTH_SHORT).show();
                        }
                    });

                this.taskNameText = v.findViewById(R.id.text_item_name);

                this.editButton = v.findViewById(R.id.button_edit);
                this.editButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (task == null)
                                return;

                            showSetupTaskFragment(task);
                        }
                });
            }

            public void setTask(UUID task) {
                this.task = task;
            }

            public void setName(String name) {
                taskNameText.setText(name);
            }

            public void updateColor(boolean selected, int position) {
                int color;
                if(selected) {
                    color = colorItemSelected;
                } else if(position % 2 == 0) {
                    color = colorItemBase;
                } else {
                    color = colorItemAlt;
                }

                itemView.setBackgroundColor(color);
            }
        }

        public Adapter() {
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup view, int viewType) {
            View v = LayoutInflater.from(view.getContext()).inflate(R.layout.item_task, view, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.setTask(taskList.get(position));
            holder.setName(mPresenter.getTaskName(taskList.get(position)));
            holder.updateColor(taskList.get(position) == selectedTask, position);
        }

        @Override
        public int getItemCount() {
            return taskList.size();
        }
    }
}
