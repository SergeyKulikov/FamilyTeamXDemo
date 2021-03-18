package com.mycoloruniverse.familyteamx.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.TabHost;

import com.mycoloruniverse.familyteamx.Defines;
import com.mycoloruniverse.familyteamx.FamilyTeamApp;
import com.mycoloruniverse.familyteamx.FamilyTeamDao;
import com.mycoloruniverse.familyteamx.model.Task;
import com.mycoloruniverse.familyteamx.view.TaskAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.CompletableObserver;
import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
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

    private Task task;
    private Disposable loadRX;

    public MainActivityPresenter(MainActivityView view) {
        this.task = new Task();
        this.view = view;

        statusMap.put(0, IDD_STATUS_PROGRESS);
        statusMap.put(1, IDD_STATUS_DONE);
        statusMap.put(2, IDD_STATUS_CANCELLED);
    }

    private Maybe<List<Task>> loadTaskList(int status) {
        return dao.rx_loadTaskList(status);
    }

    public void updateFullName(String fullName) {
        task.setTitle(fullName); // получили данные из слоя данных
        view.updateUserInfoTextView(task.toString()); // оправили данные в вьюху

    }

    public void updateEmail(String email) {
        //user.setEmail(email);
        // view.updateUserInfoTextView(user.toString());

    }

    public void closeRX() {
        loadRX.dispose();
    }


    public void prepareDataForTab(int currentTab) {
        // Зная вкладку мы должны получить список задач из БД
        int status_id = statusMap.get(currentTab);

        loadRX = loadTaskList(status_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tasks -> {
                    view.showProgressBar();
                    view.getTaskAdapter().setTaskList(tasks);
                    prepareDataForTab(view.getTabHost().getCurrentTab());
                }, throwable -> {
                    Log.e(TAG, throwable.getLocalizedMessage());
                });

        view.hideProgressBar();
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


    public interface MainActivityView {
        // Здесь описываем методы, которые необходимо будет создать во View
        void updateUserInfoTextView(String info);

        void showProgressBar();

        void hideProgressBar();

        Context getContext();

        TaskAdapter getTaskAdapter();

        TabHost getTabHost();

        // TabHost getTabHost();
    }

}
