package com.mycoloruniverse.familyteamx;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by Sergey on 22.12.2017.
 */

public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, Defines {
    /**
     * Держит единственную запись из task_list_item_layout.xml
     */

    protected ImageView task_icon;
    protected ImageView ivTaskDone;
    protected TextView titleView;
    protected TextView contentView;

    protected Context context;

    CardView notes_list;

    public TaskViewHolder(View itemView) {
        super(itemView);

        task_icon = (ImageView) itemView.findViewById(R.id.task_icon);
        ivTaskDone = (ImageView) itemView.findViewById(R.id.ivTaskDone);
        titleView = (TextView) itemView.findViewById(R.id.content);
        contentView = (TextView) itemView.findViewById(R.id.task_content);

        notes_list = itemView.findViewById(R.id.notes_list);

        context = itemView.getContext();
        // itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // отсюда неудобно доставать структкру Task

        int itemPosition = getAdapterPosition();
        // Log.d(FTEAM_LOG, "TaskViewHolder.onClick(): " + itemPosition );
        //Intent taskActivity = new Intent( v.getContext(), TaskEditActivity.class);

        // v.getContext()
        //taskActivity.putExtra( "_id",  )

        // ((Activity)v.getContext()).startActivityForResult(taskActivity, IDD_TASK_EDIT );
    }

    /** Об анимации написано в
     *
     * http://frogermcs.github.io/instamaterial-recyclerview-animations-done-right/
     */

}
