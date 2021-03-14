package com.mycoloruniverse.familyteamx.view;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mycoloruniverse.familyteamx.Common;
import com.mycoloruniverse.familyteamx.Defines;
import com.mycoloruniverse.familyteamx.R;
import com.mycoloruniverse.familyteamx.TaskItemEditActivity;
import com.mycoloruniverse.familyteamx.model.Task;
import com.mycoloruniverse.familyteamx.model.TaskItem;

public class TaskEditActivity extends AppCompatActivity implements Defines {

    private FloatingActionButton fabDetail;

    private EditText etTaskTitle;
    private TextView tvTaskTitle;
    private EditText etTaskSum;
    private CheckBox chbDivideSum;
    private Spinner sprTaskType;

    // private LinearLayout llFamilyList;

    private RadioGroup rgProcess;
    private RadioButton rbProcess, rbDone;

    private Intent intentInput;
    private Task currentTask;
    // private Common common = new Common();

    private TaskItemAdapter taskItemAdapter;

    private RecyclerView taskItemsRecycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


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


        intentInput = getIntent(); // получили данные
        //currentTask = (Task) getIntent().getSerializableExtra( "classTask" );
        currentTask = getIntent().getParcelableExtra(TASK_OBJECT);

        /*
        if (currentTask == null) {
            currentTask = new Task( common.genGuid(), "", TYPE_FREE_CONTENT );
        }
        */

        etTaskTitle = (EditText) findViewById(R.id.etTaskTitle);
        etTaskTitle.setText(currentTask.getTitle());
        //etTaskTitle.setSelectAllOnFocus(true);
        etTaskTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    hideKeyboard();
                }
            }
        });


        /* Скрываем группу если есть детали. Иначе показываем группу. */
        rgProcess = (RadioGroup) findViewById(R.id.rgProcess);
        rbProcess = (RadioButton) findViewById(R.id.rbProcessTask);
        rbDone = (RadioButton) findViewById(R.id.rbDoneTask);

        rbProcess.setChecked(!currentTask.isDone());
        rbDone.setChecked(currentTask.isDone());

        if (currentTask.getItems().size() == 0) {
            rgProcess.setVisibility(View.VISIBLE);

        } else {
            rgProcess.setVisibility(View.GONE);
        }


        // tvTaskTitle = (TextView) findViewById( R.id.tvTaskTitle );
        // tvTaskTitle.setText( currentTask.getTitle() );

        /*
        if (currentTask.getTitle().equals("")) {
            //etTaskTitle.setVisibility(View.INVISIBLE);
            tvTaskTitle.setVisibility(View.VISIBLE);
        } else {
            //etTaskTitle.setVisibility(View.VISIBLE);
            tvTaskTitle.setVisibility(View.INVISIBLE);
        }
        */

        // Log.d( "FTEAM", "ID: " + currentTask.getId() );

        // llFamilyList = (LinearLayout) findViewById( R.id.llFamilyList );

        /*

        btnOk = (Button) findViewById( R.id.btnOk );
        btnOk.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Подтверждаем, что изменения приняты и отправляем data
                setResult( RESULT_OK, prepateTaskIntent() );
                finish();
            }
        } );

        btnCancel = (Button) findViewById( R.id.btnCancelYesNoDlg );
        btnCancel.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult( RESULT_CANCELED );
                finish();
            }
        } );

         */


        fabDetail = findViewById(R.id.fabAddTaskItem);
        fabDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskItem item = new TaskItem("", null, "-");
                currentTask.getItems().add(item);

                // сразу запускаем редактирование детальки
                Intent taskItemActivity = new Intent(getApplicationContext(),
                        TaskItemEditActivity.class
                );
                taskItemActivity.putExtra("classTaskItem", item);
                startActivityForResult(taskItemActivity, IDD_TASK_ITEM_EDIT);

                refreshDetails();
                /*
                taskItemAdapter.setTaskItemList( currentTask );
                taskItemAdapter.notifyDataSetChanged();
                taskItemAdapter.notifyItemChanged( taskItemAdapter.getCurrentPosition() );
                */
            }
        });

        chbDivideSum = (CheckBox) findViewById(R.id.chbDivideSum);
        chbDivideSum.setChecked(currentTask.isDivide_sum());
        chbDivideSum.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                currentTask.setDivide_sum(isChecked);
                etTaskSum.setText(Common.DoubleToStr(currentTask.getSum(), 2));
                etTaskSum.setEnabled(!currentTask.isDivide_sum());

                /*
                taskItemAdapter.setTaskItemList( currentTask );
                taskItemAdapter.setDevideSumm( currentTask.isDevide_sum() );
                taskItemAdapter.notifyDataSetChanged();
                */
                refreshDetails();

                // saveCurrentTask();
                // taskItemAdapter.notifyItemChanged( taskItemAdapter.getCurrentPosition() );
            }
        });

        ArrayAdapter<String> adapter_type = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Free content", "Market place", "Utilities"}
        );
        adapter_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sprTaskType = (Spinner) findViewById(R.id.sprTaskType);
        sprTaskType.setAdapter(adapter_type);
        // загоspinnerловок
        sprTaskType.setPrompt("Title");
        // выделяем элемент

        sprTaskType.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);

        sprTaskType.setSelection((currentTask.getType() - 1));
        // устанавливаем обработчик нажатия
        sprTaskType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
                Toast.makeText(getBaseContext(), "Position = " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        switch (currentTask.getType()) {
            case TYPE_FREE_CONTENT:
                //sprTaskType.setChecked( currentTask.getType() );
                break;
            case TYPE_MARKET:
                break;
            case TYPE_UTILITIES:
                break;
        }

        etTaskSum = (EditText) findViewById(R.id.etTaskSum);
        etTaskSum.setText(Common.DoubleToStr(currentTask.getSum(), 2));
        etTaskSum.setEnabled(!currentTask.isDivide_sum());
        etTaskSum.setSelectAllOnFocus(true);
        etTaskSum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ((EditText) v).selectAll();
                }
            }
        });
        etTaskSum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!currentTask.isDivide_sum()) {
                    currentTask.setSum(Common.StrToDouble(etTaskSum.getText().toString()));
                    // saveCurrentTask();
                }
            }
        });

        // передали в адаптер детальки из currentTask
        taskItemAdapter = new TaskItemAdapter(currentTask, getApplicationContext());

        taskItemsRecycleView = (RecyclerView) findViewById(R.id.taskItemsRecicleView);
        taskItemsRecycleView.setLayoutManager(new LinearLayoutManager(this));
        taskItemsRecycleView.setAdapter(taskItemAdapter);


    }

    /*
    private void saveCurrentTask() {
        if (database == null) {
            database = new FamilyTeamDB(
                    getApplicationContext(), FTEAM_DB_NAME, null, FTEAM_DB_VER
            ); // Открываем БД, если ее нет, то она создается
        }

        database.saveTask( currentTask );
    }

     */

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

                    TaskItem currentTaskItem = (TaskItem) data.getSerializableExtra("classTaskItem");

                    // Log.d( FTEAM_LOG, "Got new item title: " + currentTaskItem.getContent() );


                    // Заменяем деталь
                    if (currentTask.resetItem(currentTaskItem) == -1) {
                        // если не заменилось, то выводим дебуг
                        //Log.e( FTEAM_LOG, "Rewrite item of task has failed!\n   => Item ID: " +
                        //        currentTaskItem.getGuid() +
                        //        "; Master ID: " + currentTask.getGuid() );
                        break;
                    } //else {
                    //  saveCurrentTask();
                    //}
                    refreshDetails();
                }
                break;
        }
    }


    private void refreshDetails() {
        taskItemAdapter.setTaskItemList(currentTask);
        taskItemAdapter.notifyDataSetChanged();
        taskItemAdapter.setDivideSum(currentTask.isDivide_sum());
        etTaskSum.setText(Common.DoubleToStr(currentTask.getSum(), 2));
        taskItemAdapter.notifyItemChanged(taskItemAdapter.getCurrentPosition());
    }

    @Override
    protected void onPause() {
        super.onPause();

        setResult(RESULT_OK, prepateTaskIntent());

    }

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_OK, prepateTaskIntent());
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
