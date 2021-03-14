package com.mycoloruniverse.familyteamx.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mycoloruniverse.familyteamx.Common;
import com.mycoloruniverse.familyteamx.Defines;
import com.mycoloruniverse.familyteamx.R;
import com.mycoloruniverse.familyteamx.model.Task;

import java.util.ArrayList;
import java.util.List;

// import com.daimajia.androidanimations.library.Techniques;
// import com.daimajia.androidanimations.library.YoYo;
// import com.mycoloruniverse.familyteamx.presenter.MainActivityPresenter;

/**
 * Created by Sergey on 24.12.2017.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> implements Defines {
    private final List<Task> taskList;
    private final int currentPosition = -1;
    private final Context context;

    public TaskAdapter(Context context) {
        this.context = context;
        this.taskList = new ArrayList<>();
        this.taskList.clear();
    }

    public int getCurrentPosition() {
        return this.currentPosition;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList.clear();
        this.taskList.addAll(taskList);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskAdapter.TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.task_list_layout, parent, false);
        return new TaskAdapter.TaskViewHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(final TaskAdapter.TaskViewHolder holder, int position) {

        // holder.tvDebugTask.setText(taskList.get(position).updateDone + " | " + taskList.get(position).getId() + " | " + taskList.get(position).getGuid());

        switch (taskList.get(position).getType()) {
            case TYPE_FREE_CONTENT:
                holder.task_icon.setImageDrawable(
                        holder.context.getResources().getDrawable(
                                R.drawable.ic_target_white_24
                        )
                );
                break;
            case TYPE_MARKET:
                holder.task_icon.setImageDrawable(
                        holder.context.getResources().getDrawable(
                                R.drawable.ic_market_white_24
                        )
                );
                break;
            case TYPE_TRAVEL_LIST:
                holder.task_icon.setImageDrawable(
                        holder.context.getResources().getDrawable(
                                R.drawable.ic_travel_white_24
                        )
                );
                break;
            case TYPE_UTILITIES:
                holder.task_icon.setImageDrawable(
                        holder.context.getResources().getDrawable(
                                R.drawable.ic_home_utilities_white_24
                        )
                );
                break;
        }

        if (taskList.get(position).isDone()) {
            holder.ivTaskDone.setImageDrawable(
                    holder.context.getResources().getDrawable(
                            R.drawable.ic_done_white_24
                    )
            );
        } else {
            holder.ivTaskDone.setImageDrawable(
                    null
            );
        }

        String timeCode;
        if (taskList.get(position).getStatus() == IDD_STATUS_DONE) {
            timeCode = Common.getTimeString(taskList.get(position).getClose_time(), Common.fullDateFormat);
        } else {
            timeCode = Common.getTimeString(taskList.get(position).getCreated_time(), Common.fullDateFormat);
        }

        holder.tvDateTimeTask.setText(timeCode);

        holder.titleView.setText(taskList.get(position).getTitle());
        // holder.contentView.setText( String.valueOf( taskList.get( position ).getCreated_time() ) );
        holder.contentView.setText(
                String.format("Spent sum: (%s), Items: %s, Done: %s",
                        Common.DoubleToStr(taskList.get(position).getSum(), 2),
                        Common.DoubleToStr(taskList.get(position).getActiveItemsCount(), 0),
                        Common.DoubleToStr(taskList.get(position).getItemsDone(), 0)
                )
        );

        /*
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent taskActivity = new Intent(v.getContext(), TaskEditActivity.class);
                taskActivity.putExtra(CLASS_TASK, taskList.get(itemPosition));
                currentPosition = itemPosition;

                ((Activity) v.getContext()).startActivityForResult(taskActivity, IDD_TASK_EDIT);
            }
        });


        holder.notes_list.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                currentPosition = itemPosition;
                if (taskList.get(itemPosition).isDone()) {
                    return false;
                }

                ((Activity) v.getContext()).openContextMenu(v);
                return true;
            }
        });
        */


        // animate(holder.notes_list, position);
    }

    /*
    private void animate(View viewToAnimate, int position) {
        if (viewToAnimate == null) return; // иногда срабатывает, не хочу разбираться почему.

        if (currentPosition < 0) {
            YoYo.with(Techniques.ZoomIn)
                    .duration(700)
                    .repeat(0)
                    .delay(10 * position)
                    .playOn(viewToAnimate);
        } else {
            if (position == currentPosition) {
                YoYo.with(Techniques.ZoomIn)
                        .duration(700)
                        .repeat(0)
                        .delay(10 * position)
                        .playOn(viewToAnimate);
            }
        }
    }


     */
    public void addItem(Task item) {
        taskList.add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.taskList.size();
    }

    public Task getCurrentItem() {
        return taskList.get(getCurrentPosition());
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnCreateContextMenuListener {
        protected Context context;
        /**
         * Держит единственную запись из task_list_item_layout.xml
         */

        ImageView task_icon;
        ImageView ivTaskDone;
        TextView titleView;
        TextView contentView;
        TextView tvDateTimeTask;
        LinearLayout notes_list;
        TextView tvDebugTask;

        TaskViewHolder(View itemView) {
            super(itemView);

            task_icon = itemView.findViewById(R.id.task_icon);
            ivTaskDone = itemView.findViewById(R.id.ivTaskDone);
            titleView = itemView.findViewById(R.id.content);
            contentView = itemView.findViewById(R.id.task_content);

            notes_list = itemView.findViewById(R.id.notes_list);
            tvDateTimeTask = itemView.findViewById(R.id.tvDateTimeTask);

            //tvDebugTask = (TextView) itemView.findViewById(R.id.tvDebugTask);

            context = itemView.getContext();
            // itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // отсюда неудобно доставать структкру Task

            int itemPosition = getAdapterPosition();
            //Log.d(FTEAM_LOG, "TaskViewHolder.onClick(): " + itemPosition);
            //Intent taskActivity = new Intent( v.getContext(), TaskEditActivity.class);

            // v.getContext()
            //taskActivity.putExtra( "_id",  )

            // ((Activity)v.getContext()).startActivityForResult(taskActivity, IDD_TASK_EDIT );
        }

        /**
         * Об анимации написано в
         * <p>
         * http://frogermcs.github.io/instamaterial-recyclerview-animations-done-right/
         */


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select The Action");
            //menu.add(Menu.NONE, R.id.action_delete_task,
            //         Menu.NONE, R.string.action_delete_task);
        /*
        menu.add(Menu.NONE, R.id.ctx_menu_restore_backup,
                Menu.NONE, R.string.restore_backup);
                */
        }
    }
}

