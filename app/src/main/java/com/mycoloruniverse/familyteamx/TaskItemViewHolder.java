package com.mycoloruniverse.familyteamx;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Sergey on 30.12.2017.
 */

public class TaskItemViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener, Defines {
    /**
     * Держит единственную запись из task_item_layout.xml
     */

    protected ImageView task_item_icon;
    protected ImageView ivEditTaskItem;
    protected TextView content;
    protected TextView content_description;
    protected TextView values;

    protected Context context;

    RelativeLayout llNode;
    LinearLayout llSelectedITasktem;

    public TaskItemViewHolder(View itemView) {
        super(itemView);
        //super( itemView );

        context = itemView.getContext();

        task_item_icon = (ImageView) itemView.findViewById(R.id.task_item_icon);
        ivEditTaskItem = (ImageView) itemView.findViewById(R.id.ivEditTaskItem);
        content = (TextView) itemView.findViewById(R.id.content);
        // content_description = (TextView) itemView.findViewById( R.id.content_description );
        llNode = (RelativeLayout) itemView.findViewById(R.id.task_notes_list);
        // llSelectedITasktem = (LinearLayout) itemView.findViewById( R.id.llSelectedITasktem );
        // itemView.setOnClickListener(this);
        values = (TextView) itemView.findViewById(R.id.values);

    }

    @Override
    public void onClick(View v) {
        // отсюда неудобно доставать структкру Task

        // int itemPosition = v.getIAdapterPosition();
        // Log.d( FTEAM_LOG, "TaskItemViewHolder.onClick(): " + itemPosition );
        //Intent taskActivity = new Intent( v.getContext(), TaskEditActivity.class);

        // v.getContext()
        //taskActivity.putExtra( "_id",  )

        //((Activity)v.getContext()).startActivityForResult(taskActivity, IDD_TASK_EDIT );
    }

    /** Об анимации написано в
     *
     * http://frogermcs.github.io/instamaterial-recyclerview-animations-done-right/
     */


}