package com.mycoloruniverse.familyteamx.presenter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.mycoloruniverse.familyteamx.Defines;
import com.mycoloruniverse.familyteamx.FamilyTeamApp;
import com.mycoloruniverse.familyteamx.FamilyTeamDao;
import com.mycoloruniverse.familyteamx.model.Task;
import com.mycoloruniverse.familyteamx.model.TaskItem;
import com.mycoloruniverse.familyteamx.view.ITaskEditActivityView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TaskEditActivityPresenter implements Defines {
    private final String TAG = MainActivityPresenter.class.getSimpleName();
    private final FamilyTeamDao dao = FamilyTeamApp.getInstance().getDao().getDaoDatabase();

    private final ITaskEditActivityView view;
    private Task task;

    public TaskEditActivityPresenter(ITaskEditActivityView view, String taskGUID) {
        this.view = view;

        if (taskGUID == null) {
            this.task = new Task();
        } else {
            // саму задачу
            dao.rx_loadTask(taskGUID)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(item -> {
                        task = item;

                        // детальки задачи
                        dao.rx_loadTaskItems(taskGUID).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(items -> {
                                    task.setItems(items);
                                }, throwable -> {
                                    Log.e(TAG, throwable.getLocalizedMessage());
                                }).dispose();
                        // todo: Нужно еще команду подгрузить

                    }, throwable -> {
                        Log.e(TAG, throwable.getLocalizedMessage());
                    }).dispose();
        }
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

    /*
    public Task getTask() {
        return this.task;
    }

     */

    public @NonNull
    String getTaskGuid() {
        return this.task.getGuid();
    }

    public void setTask(Task task) {
        if (task == null) { // Пришел пустой объект, тогда создаем его
            this.task = new Task();
        } else {  // А если объект не пустой, то ??
            this.task = task;
        }
        view.updateView();
    }

}
