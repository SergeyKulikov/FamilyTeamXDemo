package com.mycoloruniverse.familyteamx.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mycoloruniverse.familyteamx.Defines;
import com.mycoloruniverse.familyteamx.FamilyTeamApp;
import com.mycoloruniverse.familyteamx.FamilyTeamDao;
import com.mycoloruniverse.familyteamx.model.Task;
import com.mycoloruniverse.familyteamx.model.TaskAttr;
import com.mycoloruniverse.familyteamx.view.IClickListener;
import com.mycoloruniverse.familyteamx.view.TaskAdapter;
import com.mycoloruniverse.familyteamx.view.TaskEditActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.CompletableObserver;
import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Presenter делает свою работу
 */

public class MainActivityPresenter implements Defines {
    private final String TAG = MainActivityPresenter.class.getSimpleName();
    private final FamilyTeamDao dao = FamilyTeamApp.getInstance().getDao().getDaoDatabase();

    private final MainActivityView view;
    private final Map<Integer, Integer> statusMap = new HashMap<>();
    private final Intent intent;

    private Task task;
    private Disposable loadRX;

    public MainActivityPresenter(MainActivityView view) {
        this.task = new Task();
        this.view = view;
        this.intent = new Intent(view.getContext(), TaskEditActivity.class);
        setAdapterClick();

        this.view.getFab().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // создали новую задау и передаем GUID задачи
                intent.putExtra(TASK_GUID, addTask());
                (view.getContext()).startActivity(intent);
            }
        });

        statusMap.put(0, IDD_STATUS_PROGRESS);
        statusMap.put(1, IDD_STATUS_DONE);
        statusMap.put(2, IDD_STATUS_CANCELLED);
    }

    public void updateFullName(String fullName) {
        task.setTitle(fullName); // получили данные из слоя данных
        view.updateUserInfoTextView(task.toString()); // оправили данные в вьюху

    }

    public void closeRX() {
        loadRX.dispose();
    }

    public void prepareDataForTab(int currentTab) {
        // Зная вкладку мы должны получить список задач из БД
        int status_id = statusMap.get(currentTab);
        view.showProgressBar();

        loadRX = loadTasks(status_id); // Загрузка задач
        loadAttrs(status_id); // Загрузили атрибуты для задач
    }

    private Disposable loadTasks(int status_id) {
        return dao.rx_loadTaskListWithCount(status_id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tasks -> {
                    view.getTaskAdapter().setTaskList(tasks);
                    view.hideProgressBar();
                    // prepareDataForTab(view.getTabHost().getCurrentTab());
                }, throwable -> {
                    Log.e(TAG, throwable.getLocalizedMessage());
                    view.hideProgressBar();
                });
    }

    private void loadAttrs(int status) {
        Map<String, TaskAttr> mapAttr = new HashMap<>();

        // TODO: Еще раз глянуть https://nurlandroid.com/?p=443
        dao.rx_1loadAllTaskItems(status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<List<TaskAttr>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<TaskAttr> taskAttrs) {
                        for (int i = 0; i < taskAttrs.size(); i++) {
                            mapAttr.put(taskAttrs.get(i).getTask_guid(), taskAttrs.get(i)); // taskAttrs.get(i).getTask_guid();
                        }
                        view.getTaskAdapter().setTaskAttr(mapAttr);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public String addTask() {
        task = new Task();
        // добавляем новую запись
        dao.rx_saveTask(task)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "Что-то сохранили в базе");
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Log.e(TAG, e.getLocalizedMessage());
                    }
                });
        return task.getGuid();
    }

    private void setAdapterClick() {
        view.getTaskAdapter().setOnItemClickListener(new IClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.d(TAG, "onItemClick position: " + position);
                // запускаем редактирование записи
                intent.putExtra(TASK_GUID, view.getTaskAdapter().getTaskList().get(position).getGuid());
                (v.getContext()).startActivity(intent);
            }

            @Override
            public void onItemLongClick(int position, View v) {
                Log.d(TAG, "onItemLongClick pos = " + position);
            }
        });
    }

    // TODO: Вынести обработчики добавления записи в Presenter

    public interface MainActivityView {
        // Здесь описываем методы, которые необходимо будет создать во View
        void updateUserInfoTextView(String info);
        void showProgressBar();
        void hideProgressBar();
        Context getContext();
        TaskAdapter getTaskAdapter();
        FloatingActionButton getFab();
        TabHost getTabHost();
    }

}
