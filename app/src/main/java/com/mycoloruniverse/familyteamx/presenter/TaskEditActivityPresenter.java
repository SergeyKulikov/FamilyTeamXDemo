package com.mycoloruniverse.familyteamx.presenter;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.mycoloruniverse.familyteamx.Defines;
import com.mycoloruniverse.familyteamx.FamilyTeamApp;
import com.mycoloruniverse.familyteamx.FamilyTeamDao;
import com.mycoloruniverse.familyteamx.model.Task;
import com.mycoloruniverse.familyteamx.model.TaskItem;
import com.mycoloruniverse.familyteamx.view.IClickListener;
import com.mycoloruniverse.familyteamx.view.ITaskEditActivityView;
import com.mycoloruniverse.familyteamx.view.TaskItemEditActivity;

import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TaskEditActivityPresenter implements Defines {
    private final String TAG = MainActivityPresenter.class.getSimpleName();
    private final FamilyTeamDao dao = FamilyTeamApp.getInstance().getDao().getDaoDatabase();

    private final Intent intent;

    private final ITaskEditActivityView view;
    private Task task;
    private Disposable disposableTask, disposableTaskItems;

    public TaskEditActivityPresenter(ITaskEditActivityView view, String taskGUID) {
        this.view = view;
        intent = new Intent(view.getContext(), TaskItemEditActivity.class);
        setAdapterClick();

        if (taskGUID == null) {
            Log.e(TAG, "GUID задачи не получен");
        } else {
            // читаем задачу из локальной базы вместе со всеми детальнаями
            // таже подписываемся на изменения
            loadTask(taskGUID);
        }
    }

    private void loadTask(String taskGUID) {
        disposableTask = dao.rx_loadTask(taskGUID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> {
                    task = item;
                    // view.getTaskItemAdapter().setData(task);
                }, throwable -> {
                    Log.e(TAG, throwable.getLocalizedMessage());
                });

        LoadTaskItems(taskGUID);

    }

    private void setAdapterClick() {
        view.getTaskItemAdapter().setOnItemClickListener(new IClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.d(TAG, "onItemClick position: " + position);
                // запускаем редактирование записи
                intent.putExtra(TASK_GUID, view.getTaskItemAdapter().getTaskItemsList().get(position).getGuid());
                (v.getContext()).startActivity(intent);

            }

            @Override
            public void onItemLongClick(int position, View v) {
                Log.d(TAG, "onItemLongClick pos = " + position);
            }
        });
    }

    private void LoadTaskItems(String taskGUID) {
        // детальки задачи
        disposableTaskItems = dao.rx_loadTaskItems(taskGUID.trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items -> {
                    task.setItems(items);
                    view.updateView();
                    view.getTaskItemAdapter().setData(task);
                }, throwable -> {
                    Log.e(TAG, throwable.getLocalizedMessage());
                });
        // todo: Нужно еще команду подгрузить
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

    public String addTaskItem() {
        TaskItem taskItem = new TaskItem(task.getGuid());
        this.task.getItems().add(taskItem);
        this.saveTaskItem(taskItem);

        return taskItem.getGuid();
    }

    public void saveTaskItem(TaskItem taskItem) {


        view.updateView();

        dao.rx_SaveTaskItem(taskItem)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<Long>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                        Log.d(TAG, "Task Item has Disposable: " + d.isDisposed());
                    }

                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull Long aLong) {
                        Log.d(TAG, "Task Item has written to SQLite: " + aLong);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Log.e(TAG, "TasItem save error: " + e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "Task Item has done to SQLite: ");
                    }
                });
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

    public void disposeBaseConnection() {
        disposableTaskItems.dispose();
        disposableTask.dispose();
    }

}
