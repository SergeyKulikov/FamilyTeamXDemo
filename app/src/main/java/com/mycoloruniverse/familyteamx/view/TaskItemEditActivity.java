package com.mycoloruniverse.familyteamx.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.mycoloruniverse.familyteamx.Common;
import com.mycoloruniverse.familyteamx.Defines;
import com.mycoloruniverse.familyteamx.R;
import com.mycoloruniverse.familyteamx.SpinnerAction;
import com.mycoloruniverse.familyteamx.presenter.TaskItemEditActivityPresenter;

import java.util.Arrays;

public class TaskItemEditActivity extends AppCompatActivity implements ITaskItemEditActivityView, Defines {
    private final String TAG = TaskItemEditActivity.class.getSimpleName();
    private TaskItemEditActivityPresenter presenter;

    private final Common common = new Common();
    // private MultiAutoCompleteTextView actvItemName;
    private AutoCompleteTextView actvItemName;
    private EditText etItemValue;
    private EditText etItemSum;
    private EditText etItemPrice;
    private Spinner sprItemUnit;
    private RadioGroup rgProgressTaskItem;
    private RadioButton rbProcessItem, rbDoneItem, rbCanceledItem;
    //private ExpandableListView elvItemUnit;
    private Button btnOkItem, btnCancelItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // создаем презентер
        presenter = new TaskItemEditActivityPresenter(this,
                getIntent().getStringExtra(TASK_ITEM_GUID)
        );

        switch (getIntent().getIntExtra(TASK_TYPE, TYPE_FREE_CONTENT)) {
            case TYPE_FREE_CONTENT:
                setContentView(R.layout.activity_task_item_free_edit);
                break;
            case TYPE_MARKET:
                setContentView(R.layout.activity_task_item_market_edit);
                break;
            case TYPE_UTILITIES:
                setContentView(R.layout.activity_task_item_utility_edit);
                break;
        }

        InitUI_market();
        // updateView();


        /*
        intentIn = getIntent();
        currentTaskItem = (TaskItem) getIntent().getSerializableExtra( "classTaskItem" );

        if (currentTaskItem == null) {
            currentTaskItem = new TaskItem();
        }


        */

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);


            //actionBar.setSubtitle(
            //        Calculation.getSubTitleByLayout(intent.getIntExtra("requestCode", 0),
            //                getResources()).toUpperCase()
            //);

        }
    }

    private void InitUI_market() {
        etItemSum = findViewById(R.id.etItemSum);
        etItemValue = findViewById(R.id.etItemValue);
        etItemPrice = findViewById(R.id.etItemPrice);

        // Единицы измерения
        sprItemUnit = findViewById(R.id.sprItemUnit);
        new SpinnerAction("Units",
                Arrays.asList(getApplicationContext().getResources().getStringArray(R.array.units_goods)),
                sprItemUnit
        );

        actvItemName = findViewById(R.id.actvItemName);
        String[] goodList = presenter.getGoods();
        ArrayAdapter<String> product_list_adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                goodList
        );

        actvItemName.setAdapter(product_list_adapter);
        actvItemName.setThreshold(1);


        etItemValue = findViewById(R.id.etItemValue);
        etItemValue.setSelectAllOnFocus(true);
        etItemValue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ((EditText) v).selectAll();
                }
            }
        });


        etItemSum = findViewById(R.id.etItemSum);
        etItemSum.setSelectAllOnFocus(true);
        etItemSum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ((EditText) v).selectAll();
                }
            }
        });


        rgProgressTaskItem = findViewById(R.id.rgProgressTaskItem);
        rbDoneItem = findViewById(R.id.rbDoneTaskItem);
        rbCanceledItem = findViewById(R.id.rbCancelledTaskItem);
        rbProcessItem = findViewById(R.id.rbInProgressTaskItem);


        /*
        btnOkItem = findViewById(R.id.btnOkItem);
        btnOkItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();

                // Запоняем объект Task измененными данными
                currentTaskItem.setContent(actvItemName.getText().toString());

 s               currentTaskItem.setUnit(elvItemUnit.getSelectedItem().toString());

                //currentTaskItem.setModified_time( common.getCurre1ntDateTimeLong() );

                // Засовываем Task в data
                data.putExtra("classTaskItem", currentTaskItem);

                // Подтверждаем, что изменения приняты и отправляем data
                setResult(RESULT_OK, data);
                finish();
            }
        } );

        btnCancelItem = findViewById(R.id.btnCancelItem);
        btnCancelItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
       */

        /*
        sprItemUnit = findViewById(R.id.sprItemUnit);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getUnits()
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sprItemUnit.setAdapter(adapter);
        sprItemUnit.setPrompt("Title");
        // выделяем элемент

        sprItemUnit.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);

        /*
        sprItemUnit.setSelection( getUnitIndexByName(currentTaskItem.getUnit()) );
        // устанавливаем обработчик нажатия
        sprItemUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
                Toast.makeText( getBaseContext(), "Position = " + position, Toast.LENGTH_SHORT ).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        */

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_item_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_taskItemCommit:
                // Пишем изменения в базу
                // TODO: для сетевой работы придется вынести в отдельный класс


                presenter.saveTaskItem(); // В презентере забираем данные b пишем

                //Intent intent = new Intent();
                //intent.putExtra(TASK_ITEM_GUID, presenter.getTaskItemGUID());
                //setResult(RESULT_OK, intent );
                onBackPressed();
                break;
            default:
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void updateView() {
        actvItemName.setText(presenter.getName());
        sprItemUnit.setSelection(
                ((ArrayAdapter) sprItemUnit.getAdapter()).getPosition(presenter.getUnit())
        );

        etItemSum.setText(String.format("%.2f", presenter.getSum()));
        etItemPrice.setText(String.format("%.2f", presenter.getPrice()));
        etItemValue.setText(String.format("%f", presenter.getValue()));
        actvItemName.setText(presenter.getName());
        rbDoneItem.setChecked(presenter.isDone());
        rbCanceledItem.setChecked(presenter.isCanceled());
        rbProcessItem.setChecked(!presenter.isDone() && !presenter.isCanceled());
    }

    @Override
    public double getSum() {
        return Common.StrToDouble(etItemSum.getText().toString());
    }

    @Override
    public double getValue() {
        return Common.StrToDouble(etItemValue.getText().toString());
    }

    @Override
    public String getName() {
        return actvItemName.getText().toString();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public int getStatus() {
        int id = rgProgressTaskItem.getCheckedRadioButtonId();
        switch (id) {
            case R.id.rbInProgressTaskItem:
                return IDD_STATUS_PROGRESS;
            case R.id.rbDoneTaskItem:
                return IDD_STATUS_DONE;
            case R.id.rbCancelledTaskItem:
                return IDD_STATUS_CANCELLED;
            default:
                return IDD_STATUS_NON;
        }
    }

    @Override
    public String getUnit() {
        return sprItemUnit.getPrompt().toString();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.disposeConnection();
    }
}
