package com.mycoloruniverse.familyteamx.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.mycoloruniverse.familyteamx.Common;
import com.mycoloruniverse.familyteamx.Defines;
import com.mycoloruniverse.familyteamx.R;
import com.mycoloruniverse.familyteamx.SpinnerAction;
import com.mycoloruniverse.familyteamx.model.TaskItem;
import com.mycoloruniverse.familyteamx.presenter.TaskItemEditActivityPresenter;

import java.util.Arrays;
import java.util.List;

public class TaskItemEditActivity extends AppCompatActivity implements ITaskItemEditActivityView, Defines {
    private final String TAG = TaskItemEditActivity.class.getSimpleName();
    private TaskItemEditActivityPresenter presenter;

    private final Common common = new Common();
    private Intent intentIn;
    private TaskItem currentTaskItem;
    // private MultiAutoCompleteTextView actvItemName;
    private AutoCompleteTextView actvItemName;
    private EditText etItemValue;
    private EditText etItemSum;
    private EditText etItemPrice;
    private Spinner sprItemUnit;
    private RadioButton rbProcessItem, rbDoneItem, rbCanceledItem;
    //private ExpandableListView elvItemUnit;
    private Button btnOkItem, btnCancelItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // presenter = TaskItemEditActivityPresenter.getInstance(this);
        presenter = new TaskItemEditActivityPresenter(this,
                getIntent().getStringExtra(TASK_GUID),
                getIntent().getStringExtra(TASK_ITEM_GUID)
        );

        if (currentTaskItem == null) {
            currentTaskItem = new TaskItem();
        }

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
        updateView();


        /*
        intentIn = getIntent();
        currentTaskItem = (TaskItem) getIntent().getSerializableExtra( "classTaskItem" );

        if (currentTaskItem == null) {
            currentTaskItem = new TaskItem();
        }

         *

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);


            actionBar.setSubtitle(
                    Calculation.getSubTitleByLayout(intent.getIntExtra("requestCode", 0),
                            getResources()).toUpperCase()
            );

        }
        */
    }

    private void InitUI_market() {
        // Единицы измерения
        Spinner sprItemUnit = findViewById(R.id.sprItemUnit);
        new SpinnerAction("Units",
                getApplicationContext().getResources().getStringArray(R.array.units_goods),
                sprItemUnit
        );

        actvItemName = findViewById(R.id.actvItemName);
        String[] goodList = presenter.getGoods();
        ArrayAdapter<String> product_list_adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                goodList
        );
        actvItemName.setText(currentTaskItem.getContent());
        actvItemName.setAdapter(product_list_adapter);
        actvItemName.setThreshold(1);


        etItemValue = findViewById(R.id.etItemValue);
        etItemValue.setText(Common.DoubleToStr(currentTaskItem.getValue(), 3));
        etItemValue.setSelectAllOnFocus(true);
        etItemValue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ((EditText) v).selectAll();
                }
            }
        });


        etItemSum = findViewById(R.id.etItemSum);
        etItemSum.setText(Common.DoubleToStr(currentTaskItem.getSum(), 2));
        etItemSum.setSelectAllOnFocus(true);
        etItemSum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ((EditText) v).selectAll();
                }
            }
        });

        rbDoneItem = findViewById(R.id.rbDoneTaskItem);
        rbCanceledItem = findViewById(R.id.rbCancelledTaskItem);
        rbProcessItem = findViewById(R.id.rbInProgressTaskItem);

        rbDoneItem.setChecked(currentTaskItem.isDone());
        rbCanceledItem.setChecked(currentTaskItem.isCanceled());
        rbProcessItem.setChecked(!currentTaskItem.isDone() && !currentTaskItem.isCanceled());

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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getUnits()
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sprItemUnit = findViewById(R.id.sprItemUnit);
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

    private String[] getUnits() {
        return getApplicationContext().getResources().getStringArray(R.array.units_goods);
    }

    private int getUnitIndexByName(String name) {
        List<String> myResArrayList = Arrays.asList(getUnits());
        int index = -1;

        for (int i = 0; i < myResArrayList.size(); i++) {
            if (myResArrayList.get(i).equals(name)) {
                index = i;
                break;
            }
        }

        return index;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // setResult(RESULT_OK, prepateTaskIntent() );
                onBackPressed();
                break;
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
    }


}
