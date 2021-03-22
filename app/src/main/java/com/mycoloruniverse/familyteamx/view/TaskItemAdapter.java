package com.mycoloruniverse.familyteamx.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.text.Html;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.mycoloruniverse.familyteamx.Defines;
import com.mycoloruniverse.familyteamx.R;
import com.mycoloruniverse.familyteamx.model.Task;
import com.mycoloruniverse.familyteamx.model.TaskItem;

import java.util.ArrayList;
import java.util.List;

// import com.daimajia.androidanimations.library.Techniques;
// import com.daimajia.androidanimations.library.YoYo;

/**
 * Created by Sergey on 24.12.2017.
 */

public class TaskItemAdapter extends RecyclerView.Adapter<TaskItemAdapter.TaskItemViewHolder>
        implements Defines {
    private final List<TaskItem> taskItemList;
    private final int lastPosition = -1;
    private final Context context;
    public boolean isDivideSum;
    private int currentPosition;
    private String taskGuid;
    private int currentTaskType;

    public TaskItemAdapter(Task currentTask, Context context) {
        this.taskItemList = new ArrayList<>();
        this.taskItemList.addAll(currentTask.getItems());

        this.taskGuid = currentTask.getGuid();
        this.context = context;
        this.isDivideSum = currentTask.isDivide_sum();
        this.currentTaskType = currentTask.getType();
    }

    public TaskItemAdapter(Context context) {
        this.taskItemList = new ArrayList<>();
        this.context = context;
    }

    public static Activity getActivityFromView(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setData(Task currentTask) {
        this.taskItemList.clear();
        this.taskItemList.addAll(currentTask.getItems());

        this.taskGuid = currentTask.getGuid();
        this.currentTaskType = currentTask.getType();
    }

    public void setDivideSum(boolean isDivideSum) {
        this.isDivideSum = isDivideSum;
    }

    @Override
    public TaskItemAdapter.TaskItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.task_list_item_layout, parent, false);

        return new TaskItemAdapter.TaskItemViewHolder(view);
    }

    @SuppressLint({"ResourceAsColor", "DefaultLocale"})
    @Override
    public void onBindViewHolder(final TaskItemAdapter.TaskItemViewHolder holder, final int position) {
        //final int itemPosition = position;

        changeIcon(holder, position);

        if (taskItemList.get(position).isDone()) {
            holder.content.setText(Html.fromHtml("<strike>" + taskItemList.get(position).getContent() + "</strike>"));
        } else {
            holder.content.setText(taskItemList.get(position).getContent());
        }
        // holder.content_description.setText( taskItemList.get( position ).getDescription() );
        // holder.llSelectedITasktem.setBackgroundColor(Color.TRANSPARENT);


        switch (currentTaskType) {
            case TYPE_MARKET:
                if (isDivideSum) {
                    holder.values.setText(
                            String.format("%s %s\n∑ %s",
                                    taskItemList.get(position).getVal(),
                                    taskItemList.get(position).getUnit(),
                                    taskItemList.get(position).getSum()
                            )
                    );
                } else {
                    holder.values.setText("");
                }
                break;
            case TYPE_TRAVEL_LIST:
                holder.values.setText(
                        String.format("%s %s",
                                taskItemList.get(position).getVal(),
                                taskItemList.get(position).getUnit()
                        )
                );
                break;
        }

        holder.task_item_icon.setOnLongClickListener(new View.OnLongClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public boolean onLongClick(View v) {
                // Заменяем статус на Done
                taskItemList.get(position).setDone(true);
                taskItemList.get(position).setCanceled(false);
                changeIcon(holder, position);

                holder.content.setText(Html.fromHtml("<strike>" +
                        taskItemList.get(position).getContent() + "</strike>")
                );

                // if (!saveCurrentTaskItem(position)) {
                //    Log.e(FTEAM_LOG, "saveCurrentTaskItem() failed");
                //}

                // animate(holder.llNode, position);

                Log.d("test", "=> ((Activity) v.getContext()), IDD_TASK_ITEM_EDIT");
                return true;
            }
        });

        /*
        holder.ivEditTaskItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPosition = position;
                TaskItem item = taskItemList.get(position);
                Intent taskItemActivity = new Intent(v.getContext(), TaskItemEditActivity.class);
                taskItemActivity.putExtra(CLASS_TASK_ITEM, item);
                taskItemActivity.putExtra(TASK_DIVIDE_SUM, isDivideSum);
                taskItemActivity.putExtra(TASK_TYPE, currentTaskType);

                Activity activity = getActivityFromView(v);
                if (activity != null) {
                    activity.startActivityForResult(taskItemActivity, IDD_TASK_ITEM_EDIT);
                } else {
                    Log.e(FTEAM_LOG, this.getClass().getName()+": getActivityFromView(v) is null...");
                }
            }
        });

         */


        // animate(holder.llNode, position);
    }

    /*
    private boolean saveCurrentTaskItem(int position) {
        if (database == null) {
            database = new FamilyTeamDB(
                    context.getApplicationContext(), FTEAM_DB_NAME, null, FTEAM_DB_VER
            ); // Открываем БД, если ее нет, то она создается
        }

        return database.saveTaskItem(taskItemList.get(position), taskGuid).updateDone;
    }
    *
     */

    private void changeIcon(final TaskItemAdapter.TaskItemViewHolder holder, final int position) {
        if (taskItemList.get(position).isDone()) {
            holder.task_item_icon.setImageDrawable(
                    holder.context.getResources().getDrawable(
                            R.drawable.ic_done_white_24
                    )
            );
        } else {
            if (taskItemList.get(position).isCanceled()) {
                holder.task_item_icon.setImageDrawable(
                        holder.context.getResources().getDrawable(
                                R.drawable.ic_cancel_white_24
                        )
                );
            } else {
                holder.task_item_icon.setImageDrawable(
                        holder.context.getResources().getDrawable(
                                R.drawable.ic_new_white_24
                        )
                );
            }
        }

    }

    /*
    private void animate(View viewToAnimate, int position) {
        if (viewToAnimate == null) return;

        if (position > lastPosition) {
            YoYo.with(Techniques.ZoomIn)
                    .duration(700)
                    .repeat(0)
                    .delay(10)
                    .playOn(viewToAnimate);

            lastPosition = position;
            //lastPosition++;
        }

    }

     */

    @Override
    public int getItemCount() {
        return taskItemList.size();
    }


    static class TaskItemViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, Defines, View.OnCreateContextMenuListener {
        /**
         * Держит единственную запись из task_item_layout.xml
         */

        ImageView task_item_icon;
        ImageView ivEditTaskItem;
        TextView content;
        // TextView content_description;
        TextView values;
        TextView tvDebugTask;

        Context context;

        ConstraintLayout llNode; //RelativeLayout llNode;
        LinearLayout llSelectedITasktem;

        TaskItemViewHolder(View itemView) {
            super(itemView);

            //tvDebugTask = (TextView) itemView.findViewById(R.id.tvDebugTask);

            context = itemView.getContext();

            task_item_icon = itemView.findViewById(R.id.task_item_icon);
            ivEditTaskItem = itemView.findViewById(R.id.ivEditTaskItem);
            content = itemView.findViewById(R.id.content);
            // content_description = (TextView) itemView.findViewById(R.id.content_description);
            llNode = itemView.findViewById(R.id.task_notes_list);
            // llSelectedITasktem = (LinearLayout) itemView.findViewById(R.id.llSelectedITasktem);
            values = itemView.findViewById(R.id.values);

            // itemView.setOnCreateContextMenuListener(this); // необходимо для контекстного меню

        }

        @Override
        public void onClick(View v) {
            // отсюда неудобно доставать структкру Task

            int itemPosition = getAdapterPosition();
            // Log.d(FTEAM_LOG, "TaskItemViewHolder.onClick(): " + itemPosition);
            //Intent taskActivity = new Intent( v.getContext(), TaskEditActivity.class);

            // v.getContext()
            //taskActivity.putExtra( "_id",  )

            //((Activity)v.getContext()).startActivityForResult(taskActivity, IDD_TASK_EDIT );
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select The Action ddddd");
            //menu.add(Menu.NONE, R.id.action_delete_task,
            //        Menu.NONE, R.string.action_delete_task);
        /*
        menu.add(Menu.NONE, R.id.ctx_menu_restore_backup,
                Menu.NONE, R.string.restore_backup);
                */
        }


        /** Об анимации написано в
         *
         * http://frogermcs.github.io/instamaterial-recyclerview-animations-done-right/
         */


    }

}
