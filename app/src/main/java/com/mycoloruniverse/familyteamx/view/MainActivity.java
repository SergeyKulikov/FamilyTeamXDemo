package com.mycoloruniverse.familyteamx.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TabHost;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mycoloruniverse.familyteamx.R;
import com.mycoloruniverse.familyteamx.presenter.MainActivityPresenter;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 1. Проект расчитан на создание команды, которая выполняет какие-то действия и отмечает что сделано
 * 2. Команда может состоять из списка людей, кторые устновили приложение и (следовательно) автоматом зарегестрировались в базе данных.
 * 3. Чат не предусмотрен. Каманде ставятся задачи, они сихронизируются по сети.
 * 4. Исполнение задач также синхронизируется по сети.
 * 5. Если несколько человек выполнили одну задачу, то можно посмотреть кто это сделал.
 * 6. После выполнения всех пунктов задачи все участники группы информируются, что зада выполнены.
 * 7. Если участника добавили в задачу, то ему приходит уведомление, что он включен в задачу и сама задача.
 * 8. Добавление и удаление из задачи происходит только создателем?
 * <p>
 * пишем с использованием:
 * Room
 * RxJava2
 * Picasso
 * Retrofit2
 * <p>
 * Модель: MVP
 */

public class MainActivity extends AppCompatActivity implements MainActivityPresenter.MainActivityView {
    private final String TAG = MainActivity.class.getSimpleName();

    private ProgressBar progressBar;
    private MainActivityPresenter presenter;
    private FloatingActionButton fabAddTask;

    private TabHost tabHost;
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private Disposable tabChanger;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskAdapter = new TaskAdapter(getContext()); // список задач в адаптере
        // TODO: Сделать выбор зачачи из RecycleView
        initUI();

        presenter = new MainActivityPresenter(this);
        createTabs();  // Запрашиваем данные для вкладок

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                RecyclerView.VERTICAL);
        //dividerItemDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.ic_date_black_24dp));
        //dividerItemDecoration.

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(taskAdapter);
        recyclerView.setClickable(true);
        recyclerView.setLongClickable(true);
        recyclerView.setOnCreateContextMenuListener(this); // необходимо для контекстного меню для RecycledView
        recyclerView.addItemDecoration(dividerItemDecoration);

        /*
        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TaskEditActivity.class);
                // создали новую задау и передаем GUID задачи
                intent.putExtra(TASK_GUID, presenter.addTask());

                startActivity(intent);
                // startActivityForResult(intent, IDD_TASK_ADD);
            }
        });

         */


        presenter.prepareDataForTab(tabHost.getCurrentTab());

        // recyclerView.getAdapter().notifyDataSetChanged();

        // initProgressBar();

        //presenter.loadTaskList().subscribeOn(Schedule.io());
    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tabChanger.dispose();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void createTabs() {
        int[] folders_id = {R.string.active_tab_name, R.string.done_tab_name,
                R.string.canceled_tab_name};

        for (int i = 0; i < folders_id.length; i++) {

            TabHost.TabSpec tabSpec = tabHost.newTabSpec(String.valueOf(i));
            tabSpec.setIndicator(
                    getContext().getString(folders_id[i]),
                    getContext().getResources().getDrawable(R.drawable.tab_icon_selector)
            );
            tabSpec.setContent(R.id.recycle_view);
            tabHost.addTab(tabSpec);
        }

        tabChanger = onChangeTabListener().subscribe(); // отследить изменение вкладок

        // Смена вкладок вызывает обработчик (см. выше)
        // Почему-то прямой вызов "presenter.prepareDataForTab(0);" но не отображает в recycleView
        tabHost.setCurrentTab(1);
        tabHost.setCurrentTab(0);

    }

    private Observable<View> onChangeTabListener() {
        // Обработчик смены вкладок
        return Observable.create(
                (ObservableOnSubscribe<View>) e -> tabHost.setOnTabChangedListener(tabId -> {
                    presenter.prepareDataForTab(tabHost.getCurrentTab());
                })).
                subscribeOn(Schedulers.io()).  // автомматическое создание потоков
                observeOn(AndroidSchedulers.mainThread()); // венуть всё в главный поток

    }

    private void initProgressBar() {
        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleSmall);
        progressBar.setIndeterminate(true);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(Resources.getSystem().getDisplayMetrics().widthPixels,
                250);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        this.addContentView(progressBar, params);
        // showProgressBar();
    }


    @Override
    public void updateUserInfoTextView(String info) {

    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void initUI() {
        progressBar = findViewById(R.id.progressBar);

        tabHost = findViewById(R.id.tabHost);
        tabHost.setup();

        recyclerView = findViewById(R.id.recycle_view);
        fabAddTask = findViewById(R.id.fabAddTask);
    }

    @Override
    public TabHost getTabHost() {
        return tabHost;
    }


    @Override
    public TaskAdapter getTaskAdapter() {
        return taskAdapter;
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public FloatingActionButton getFab() {
        return fabAddTask;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        //запись делаем там

        // presenter.addTask(task);

        /*
        switch (requestCode) {
            case IDD_TASK_ADD:
                if (data == null) {
                    return;
                }

                Task task = data.getParcelableExtra(TASK_OBJECT);
                presenter.addTask(task);
                break;
            default:
                Log.e(TAG, "Unexpected result on request code: IDD_TASK_ADD");
        }

         */
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.closeRX();
    }
}