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

import com.google.android.material.textfield.TextInputLayout;

import org.team.app.contract.ListTaskContract;
import org.team.app.contract.ListTaskContract.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.UUID;

public class ListTaskView extends FragmentView implements ListTaskContract.View {
    protected ListTaskContract.Presenter mPresenter;

    protected RecyclerView mRecycler;
    protected LinearLayoutManager mLayoutManager;
    protected Adapter mAdapter;

    protected List<Element> taskList;
    protected UUID selectedTask = null;

    protected int colorItemBase;
    protected int colorItemAlt;
    protected int colorItemSelected;

    public ListTaskView() {
        super(R.layout.screen_list_task);

        taskList = new ArrayList<>();
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

        final EditText search = ((TextInputLayout)view.findViewById(R.id.outlinedSearch)).getEditText();
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
    public void updateTaskList(Collection<Element> list) {
        taskList = new ArrayList<>(list);
        mAdapter.notifyDataSetChanged();
    }

    private int getTaskIndex(UUID task) {
        for(int i = 0; i < taskList.size(); i++)
            if(taskList.get(i).task == task)
                return i;
        return -1;
    }

    @Override
    public void selectCurrentTask(UUID task) {
        int prevPos = -1;
        if(this.selectedTask != null)
            prevPos = getTaskIndex(task);

        this.selectedTask = task;
        int pos = getTaskIndex(this.selectedTask);

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
        getChildFragmentManager().beginTransaction()
            .replace(getView().getId(), mActivity.getSetupTaskFragment(task))
            .addToBackStack(null)
            .commit();
    }

    class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        class TaskViewHolder extends RecyclerView.ViewHolder {
            protected final TextView taskNameText;
            protected final ImageButton editButton;

            protected UUID task = null;

            public TaskViewHolder(View v) {
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

        class CategoryViewHolder extends RecyclerView.ViewHolder {
            protected final TextView categoryNameText;

            public CategoryViewHolder(View v) {
                super(v);

                this.categoryNameText = v.findViewById(R.id.text_item_name);
            }

            public void setName(String name) {
                categoryNameText.setText(name);
            }
        }

        public Adapter() {
        }

        @Override
        public int getItemViewType(int position) {
            return taskList.get(position).task == null ? 1 : 0;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup view, int viewType) {
            if(viewType == 0) {
                View v = LayoutInflater.from(view.getContext()).inflate(R.layout.item_task, view, false);
                return new TaskViewHolder(v);
            } else {
                View v = LayoutInflater.from(view.getContext()).inflate(R.layout.item_category, view, false);
                return new CategoryViewHolder(v);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder _holder, final int position) {
            if(_holder.getItemViewType() == 0) {
                TaskViewHolder holder = (TaskViewHolder) _holder;
                UUID task = taskList.get(position).task;
                holder.setTask(task);
                holder.setName(mPresenter.getTaskName(task));
                holder.updateColor(task == selectedTask, position);
            } else {
                CategoryViewHolder holder = (CategoryViewHolder) _holder;
                holder.setName(taskList.get(position).category);
            }
        }

        @Override
        public int getItemCount() {
            return taskList.size();
        }
    }
}
