package com.mycoloruniverse.familyteamx.presenter;

import android.util.Log;

import com.mycoloruniverse.familyteamx.FamilyTeamApp;
import com.mycoloruniverse.familyteamx.FamilyTeamDao;
import com.mycoloruniverse.familyteamx.model.TaskItem;
import com.mycoloruniverse.familyteamx.view.ITaskItemEditActivityView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TaskItemEditActivityPresenter {

    private final String TAG = TaskItemEditActivityPresenter.class.getSimpleName();
    private final FamilyTeamDao dao = FamilyTeamApp.getInstance().getDao().getDaoDatabase();

    // private static TaskItemEditActivityPresenter instance;
    private final ITaskItemEditActivityView view;

    private boolean isSavedItems;

    private TaskItem taskItem;
    private String task_guid;
    private Disposable disposableLoadTaskItem, disposableSaveTaskItem;

    public TaskItemEditActivityPresenter(ITaskItemEditActivityView view,
                                         String taskItemGUID) {
        if (taskItemGUID == null) { //
            Log.e(TAG, "Не получен GUID детали задачи");
        } else {
            loadTaskItem(taskItemGUID);
        }
        this.view = view;
        this.isSavedItems = false;
    }

    private void loadTaskItem(String taskItemGUID) {
        disposableLoadTaskItem = dao.rx_loadTaskItem(taskItemGUID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> {
                    taskItem = item;
                    view.updateView();
                }, throwable -> {
                    Log.e(TAG, throwable.getLocalizedMessage());
                });
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

    public void saveTaskItem() {

        disposableSaveTaskItem = dao.rx_SaveTaskItem(taskItem)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(id -> {
                    isSavedItems = true;
                    view.updateView();
                        }
                );
    }

    public String getTaskItemGUID() {
        return this.taskItem.getGuid();
    }

    public void disposeConnection() {
        disposableLoadTaskItem.dispose();
        disposableSaveTaskItem.dispose();
    }

    public boolean isDone() {
        return this.taskItem.isDone();
    }

    public boolean isCanceled() {
        return this.taskItem.isCanceled();
    }
}
