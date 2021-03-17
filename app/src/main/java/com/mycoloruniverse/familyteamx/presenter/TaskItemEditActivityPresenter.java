package com.mycoloruniverse.familyteamx.presenter;

import android.util.Log;

import com.mycoloruniverse.familyteamx.FamilyTeamApp;
import com.mycoloruniverse.familyteamx.FamilyTeamDao;
import com.mycoloruniverse.familyteamx.model.TaskItem;
import com.mycoloruniverse.familyteamx.view.ITaskItemEditActivityView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TaskItemEditActivityPresenter {

    private final String TAG = TaskItemEditActivityPresenter.class.getSimpleName();
    private final FamilyTeamDao dao = FamilyTeamApp.getInstance().getDao().getDaoDatabase();

    // private static TaskItemEditActivityPresenter instance;
    private final ITaskItemEditActivityView view;

    private TaskItem taskItem;
    private String task_guid;

    public TaskItemEditActivityPresenter(ITaskItemEditActivityView view, String taskGUID,
                                         String taskItemGUID) {
        if (taskItemGUID == null) { //
            this.task_guid = taskGUID;
            this.taskItem = new TaskItem(this.task_guid); // новая деталь сразу привязываем к родителю
        } else {
            dao.rx_loadTaskItem(taskItemGUID)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(item -> {
                        taskItem = item;
                    }, throwable -> {
                        Log.e(TAG, throwable.getLocalizedMessage());
                    }).dispose();
        }
        this.view = view;
    }

    /*
    public static TaskItemEditActivityPresenter getInstance(ITaskItemEditActivityView view) {
        if (instance == null) {
            instance = new TaskItemEditActivityPresenter(view);
        }

        return instance;
    }
    */

    public String[] getGoods() {
        final String[][] strarray = {new String[0]};
        dao.rx_loadProductList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items -> {
                    strarray[0] = items.toArray(new String[0]);
                }, throwable -> {
                    Log.e(TAG, throwable.getLocalizedMessage());
                }).dispose();
        return strarray[0];
    }


    public String getName() {
        return this.taskItem.getTitle();
    }

    public double getSum() {
        return this.taskItem.getSum();
    }

    public double getPrice() {
        return this.taskItem.getSum() / this.taskItem.getValue();
    }

    public double getValue() {
        return this.taskItem.getValue();
    }

    public String getUnit() {
        return this.taskItem.getUnit();
    }

    public void setTaskItem(TaskItem taskItem) {
        if (taskItem == null) {
            this.taskItem = new TaskItem();
        } else {
            this.taskItem = taskItem;
        }
        this.view.updateView();
    }

}
