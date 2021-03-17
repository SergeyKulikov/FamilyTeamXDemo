package com.mycoloruniverse.familyteamx.presenter;

import com.mycoloruniverse.familyteamx.Defines;
import com.mycoloruniverse.familyteamx.FamilyTeamApp;
import com.mycoloruniverse.familyteamx.FamilyTeamDao;
import com.mycoloruniverse.familyteamx.model.Task;
import com.mycoloruniverse.familyteamx.model.TaskItem;
import com.mycoloruniverse.familyteamx.view.ITaskEditActivityView;

public class TaskEditActivityPresenter implements Defines {
    private final String TAG = MainActivityPresenter.class.getSimpleName();
    private final FamilyTeamDao dao = FamilyTeamApp.getInstance().getDao().getDaoDatabase();

    private final ITaskEditActivityView view;
    private Task task;

    public TaskEditActivityPresenter(ITaskEditActivityView view) {
        this.view = view;
    }

    public void updateTask() {
        task.setTitle(view.getTaskTitle());
        task.setType(view.getType());
        task.setStatus(view.getStatus());
        task.setDivide_sum(view.getDivideSum());
        // task.setStatus(view.getStatus());
    }

    public String getTitle() {
        return task.getTitle();
    }

    public void setTitle(String title) {
        task.setTitle(title);
        view.updateView();
    }

    public int getStatus() {
        return task.getStatus();
    }

    public void setStatus(int status) {
        this.setStatus(status);
        view.updateView();
    }

    public int getItemCount() {
        return task.getItems().size();
    }

    public TaskItem addTaskItem() {
        TaskItem item = new TaskItem();
        task.getItems().add(item);
        return item;
    }

    public double getSum() {
        return task.getSum();
    }

    public boolean isDivideSum() {
        return task.isDivide_sum();
    }

    public void setDivideSum(boolean isDivideSum) {
        task.setDivide_sum(isDivideSum);
        view.updateView();
    }

    public int getType() {
        return task.getType();
    }

    public void setType(int type) {
        task.setType(type);
    }

    public Task getTask() {
        return this.task;
    }

    public void setTask(Task task) {
        this.task = task;
        view.updateView();
    }
}
