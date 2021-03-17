package com.mycoloruniverse.familyteamx.presenter;

import com.mycoloruniverse.familyteamx.FamilyTeamApp;
import com.mycoloruniverse.familyteamx.FamilyTeamDao;
import com.mycoloruniverse.familyteamx.view.ITaskItemEditActivityView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TaskItemEditActivityPresenter {

    private final String TAG = TaskItemEditActivityPresenter.class.getSimpleName();
    private final FamilyTeamDao dao = FamilyTeamApp.getInstance().getDao().getDaoDatabase();

    private final ITaskItemEditActivityView view;

    public TaskItemEditActivityPresenter(ITaskItemEditActivityView view) {
        this.view = view;
    }

    public String[] getGoods() {
        dao.rx_loadGoodList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
