package com.mycoloruniverse.familyteamx.view;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabHost;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mycoloruniverse.familyteamx.Common;
import com.mycoloruniverse.familyteamx.Defines;
import com.mycoloruniverse.familyteamx.R;
import com.mycoloruniverse.familyteamx.SpinnerAction;
import com.mycoloruniverse.familyteamx.presenter.TaskEditActivityPresenter;

import java.util.Arrays;

public class TaskEditActivity extends AppCompatActivity implements ITaskEditActivityView, Defines {
    private final String TAG = TaskEditActivity.class.getSimpleName();

    private TaskEditActivityPresenter presenter;

    private FloatingActionButton fabAddTaskDetailItem;
    private EditText etTaskTitle;
    private EditText etTaskSum;
    private CheckBox chbDivideSum;
    private Spinner sprTaskType;
    private RadioGroup rgProgress;
    private RadioButton rbInProgress, rbDone, rbCancelled;
    private Button btnTaskCommit;

    private TaskItemAdapter taskItemAdapter;
    private RecyclerView taskItemsRecycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_task_edit);
        setContentView(R.layout.activity_property_task);

        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // получили на вход GUID задачи и читаем ее в TaskEditActivityPresenter из локальной базы
        presenter = new TaskEditActivityPresenter(this, getIntent().getStringExtra(TASK_GUID));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);

            /*
            actionBar.setSubtitle(
                    Calculation.getSubTitleByLayout(intent.getIntExtra("requestCode", 0),
                            getResources()).toUpperCase()
            );
            */
        }

        initUI();

        createTabs();
        // updateView();


        // выделяем элемент
        // sprTaskType.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        // sprTaskType.setSelection((currentTask.getType() - 1));
        // устанавливаем обработчик нажатия


        //etTaskSum = (EditText) findViewById(R.id.etTaskSum);
        // etTaskSum.setText(Common.DoubleToStr(currentTask.getSum(), 2));
        // etTaskSum.setEnabled(!currentTask.isDivide_sum());
        // etTaskSum.setSelectAllOnFocus(true);


        // передали в адаптер детальки из currentTask


    }

    private void createTabs() {
        // Посмотрел в сторону ViewPage2 и остановился на TabHost
        // Очень удобное формирование различного контента на странце, а в ViewPage2
        // придется искуственно целое разделить на форагменты. Решил не создавать трудности
        // на ровном месте. Свайп закладок тоже здесь не к месту.

        TabHost tabHost = findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec;

        // Эта закладка первоначально должна быть скрыта
        // она появлется только если Task сохранена в базе данных
        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setContent(R.id.content_tab0);
        tabSpec.setIndicator("List");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setContent(R.id.content_tab1);
        tabSpec.setIndicator("Main");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setContent(R.id.content_tab2);
        tabSpec.setIndicator("Team");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag4");
        tabSpec.setContent(R.id.content_tab3);
        tabSpec.setIndicator("Money");
        tabHost.addTab(tabSpec);


        tabHost.setCurrentTab(1);
    }

    private void initUI() {
        etTaskTitle = findViewById(R.id.etTaskTitle);
        //etTaskTitle.setSelectAllOnFocus(true);
        etTaskTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    hideKeyboard();
                }
            }
        });

        rgProgress = findViewById(R.id.rgProgress);

        rbInProgress = findViewById(R.id.rbInProgress);
        rbDone = findViewById(R.id.rbDoneTask);
        rbCancelled = findViewById(R.id.rbDoneTask);


        fabAddTaskDetailItem = findViewById(R.id.fabAddTaskItem);
        fabAddTaskDetailItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent taskEditItemActivity = new Intent(getApplicationContext(),
                        TaskItemEditActivity.class
                );
                taskEditItemActivity.putExtra(TASK_GUID, presenter.getTaskGuid()); // guid задачи
                taskEditItemActivity.putExtra(TASK_ITEM_GUID, presenter.addTaskItem()); // guid детальки
                taskEditItemActivity.putExtra(TASK_TYPE, presenter.getType()); // guid детальки = null

                //startActivityForResult(taskItemActivity, IDD_TASK_ITEM_ADD);
                startActivity(taskEditItemActivity);
            }
        });


        chbDivideSum = findViewById(R.id.chbDivideSum);
        chbDivideSum.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // btnTaskCommit.setEnabled(isChecked);
                presenter.setDivideSum(isChecked);
                updateView();

                // presenter.setDivideSum(isChecked);

                // etTaskSum.setText(Common.DoubleToStr(presenter.getSum(), 2));
                // etTaskSum.setEnabled(!presenter.isDivideSum());

                /*
                taskItemAdapter.setTaskItemList( currentTask );
                taskItemAdapter.setDevideSumm( currentTask.isDevide_sum() );
                taskItemAdapter.notifyDataSetChanged();
                */
                // refreshDetails();

                // saveCurrentTask();
                // taskItemAdapter.notifyItemChanged( taskItemAdapter.getCurrentPosition() );
            }
        });


        // Тип задачи
        sprTaskType = findViewById(R.id.sprTaskType);
        new SpinnerAction("Select your task type",
                Arrays.asList(
                        getString(R.string.free_content),
                        getString(R.string.market),
                        getString(R.string.utilities)
                ),
                sprTaskType
        );


        sprTaskType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {


                // TODO: Пишет не тот тип. Обработать ошибку здесь и исправить в момент записи.
                presenter.setType(getType());
                // показываем позиция нажатого элемента
                // Toast.makeText(getBaseContext(), "Position = " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        etTaskSum = findViewById(R.id.etTaskSum);
        etTaskSum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ((EditText) v).selectAll();
                }
            }
        });


        /*
        btnTaskCommit = findViewById(R.id.btnTaskCommit);
        btnTaskCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // presenter.saveTask(); //
                presenter.updateTask();
                Intent intent = new Intent();
                intent.putExtra(TASK_OBJECT, presenter.getTask());

                setResult(RESULT_OK, intent);
                finish();
            }
        });
        */


        taskItemAdapter = new TaskItemAdapter(getApplicationContext());

        taskItemsRecycleView = findViewById(R.id.taskItemsRecycleView);
        taskItemsRecycleView.setLayoutManager(new LinearLayoutManager(this));
        taskItemsRecycleView.setAdapter(taskItemAdapter);

        //

    }


    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

        //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /**
         * Здесь мы обрабытываем добавление или изменение детальки
         * Само изменение или добавление (открытие TaskItemEditActivity)
         * вызывается в TaskItemAdapter
         */

        // Log.d("test", ",<== ((Activity) v.getContext()), IDD_TASK_ITEM_EDIT");
        if (requestCode == RESULT_CANCELED) return;

        ContentValues contentValues = new ContentValues();

        switch (requestCode) {
            case IDD_TASK_ITEM_EDIT:
                if (data == null) return;
                if (data.getExtras() != null) {
                    contentValues.clear();


                    // presenter.loadTask();

                    // Log.d( FTEAM_LOG, "Got new item title: " + currentTaskItem.getContent() );


                    // Заменяем деталь
                    //if (currentTask.resetItem(currentTaskItem) == -1) {
                    // если не заменилось, то выводим дебуг
                    //Log.e( FTEAM_LOG, "Rewrite item of task has failed!\n   => Item ID: " +
                    //        currentTaskItem.getGuid() +
                    //        "; Master ID: " + currentTask.getGuid() );
                    //    break;
                    //} //else {
                    //  saveCurrentTask();
                    //}
                    // refreshDetails();
                }
                break;
        }
    }


    private void refreshDetails() {
        /*
        taskItemAdapter.setData(currentTask);
        taskItemAdapter.notifyDataSetChanged();
        taskItemAdapter.setDivideSum(currentTask.isDivide_sum());
        etTaskSum.setText(Common.DoubleToStr(currentTask.getSum(), 2));
        */
    }

    @Override
    protected void onPause() {
        super.onPause();
        // отписались от загрузки данных по задаче
        presenter.disposeBaseConnection();
    }

    /*
    private Intent prepateTaskIntent() {
        currentTask.setTitle(etTaskTitle.getText().toString());
        currentTask.setModified_time(Common.getCurrentDateTimeLong());
        currentTask.setDivide_sum(chbDivideSum.isChecked());
        currentTask.setType((sprTaskType.getSelectedItemPosition() + 1));
        currentTask.setDone(rbDone.isChecked());

        Intent intent = new Intent();
        // intent.putExtra( "classTask", currentTask );
        return intent;
    }

     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                // setResult(RESULT_OK, );
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateView() {
        etTaskTitle.setText(presenter.getTitle());

        etTaskSum.setEnabled(presenter.isDivideSum());
        if (presenter.isDivideSum()) {
            etTaskSum.setText(Common.DoubleToStr(presenter.getSum(), 2));
        } else {
            // Не обнуляем суммы по позициям, а то вдруг человек передумал
            etTaskSum.setText(Common.DoubleToStr(0.00, 2));
        }

        sprTaskType.setSelection(presenter.getType());

        switch (presenter.getStatus()) {
            case IDD_STATUS_PROGRESS:
                rgProgress.check(R.id.rbInProgress);
                break;
            case IDD_STATUS_DONE:
                rgProgress.check(R.id.rbDoneTask);
                break;
            case IDD_STATUS_CLOSED:
                rgProgress.check(R.id.rbCancelledTask);
                break;
            default:
                rgProgress.clearCheck();
        }


        /*
        if (presenter.getItemCount() == 0) {
            rgProcess.setVisibility(View.VISIBLE);
        } else {
            rgProcess.setVisibility(View.GONE);
        }
         */

    }

    @Override
    public FloatingActionButton getFab() {
        return fabAddTaskDetailItem;
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @SuppressLint("NonConstantResourceId")

    public int getStatus() {
        int id = rgProgress.getCheckedRadioButtonId();
        switch (id) {
            case R.id.rbInProgress:
                return IDD_STATUS_PROGRESS;
            case R.id.rbDoneTask:
                return IDD_STATUS_DONE;
            case R.id.rbCancelledTask:
                return IDD_STATUS_CANCELLED;
            default:
                return IDD_STATUS_NON;
        }
    }

    @Override
    public int getType() {
        int id = sprTaskType.getSelectedItemPosition();
        switch (id) {
            case 0:
                return TYPE_FREE_CONTENT;
            case 1:
                return TYPE_MARKET;
            case 2:
                return TYPE_UTILITIES;
            default:
                return TYPE_NON;
        }
    }

    @Override
    public boolean getDivideSum() {
        return chbDivideSum.isChecked();
    }

    @Override
    public TaskItemAdapter getTaskItemAdapter() {
        return taskItemAdapter;
    }

    @Override
    public String getTaskTitle() {
        return etTaskTitle.getText().toString();
    }
}

