package org.team.app.view;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageButton;
import android.view.LayoutInflater;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.content.Context;

import org.team.app.contract.ListTaskContract;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ListTaskView extends FragmentView implements ListTaskContract.View {
    protected ListTaskContract.Presenter mPresenter;

    protected RecyclerView mRecycler;
    protected LinearLayoutManager mLayoutManager;
    protected Adapter mAdapter;

    protected List<UUID> taskList;
    protected int selectedPos = -1;

    protected int colorItemBase;
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
        colorItemSelected = view.getResources().getColor(R.color.colorItemSelected);

        mRecycler = view.findViewById(R.id.view_recycler);

        mLayoutManager = new LinearLayoutManager((Context)mActivity);
        mRecycler.setLayoutManager(mLayoutManager);

        mAdapter = new Adapter();
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void addTask(UUID task) {
        taskList.add(task);
        mAdapter.notifyItemInserted(taskList.size() - 1);
    }

    @Override
    public void removeTask(UUID task) {
        int pos = taskList.indexOf(task);
        if(pos < 0)
            return;

        taskList.remove(pos);
        mAdapter.notifyItemRemoved(pos);
    }

    @Override
    public void updateTask(UUID task) {
        int pos = taskList.indexOf(task);
        if(pos < 0)
            return;

        mAdapter.notifyItemChanged(pos);
    }

    @Override
    public void selectCurrentTask(UUID task) {
        selectedPos = taskList.indexOf(task);
        mAdapter.notifyItemChanged(selectedPos);
    }

    class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
        class ViewHolder extends RecyclerView.ViewHolder {
            protected final TextView taskNameText;
            protected final ImageButton editButton;

            protected UUID task = null;

            public ViewHolder(View v) {
                super(v);

                this.taskNameText = v.findViewById(R.id.text_item_name);

                this.editButton = v.findViewById(R.id.button_edit);
                this.editButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            if (task == null)
                                return;

                            getParentFragmentManager().beginTransaction()
                                .replace(R.id.frame_list_task, mActivity.getSetupTaskFragment(task))
                                .addToBackStack(null)
                                .commit();
                        }
                });
            }

            public void setTask(UUID task) {
                this.task = task;
            }

            public void setName(String name) {
                taskNameText.setText(name);
            }

            public void setColor(int color) {
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
            holder.setColor(position == selectedPos ? colorItemSelected : colorItemBase);
        }

        @Override
        public int getItemCount() {
            return taskList.size();
        }
    }
}
