package com.mycoloruniverse.familyteamx.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.mycoloruniverse.familyteamx.Common;
import com.mycoloruniverse.familyteamx.Defines;
import com.mycoloruniverse.familyteamx.R;
import com.mycoloruniverse.familyteamx.SpinnerAction;
import com.mycoloruniverse.familyteamx.model.TaskItem;
import com.mycoloruniverse.familyteamx.presenter.TaskItemEditActivityPresenter;

import java.util.Arrays;
import java.util.List;

public class TaskItemEditActivity extends AppCompatActivity implements Defines {
    private final String TAG = TaskItemEditActivity.class.getSimpleName();
    private TaskItemEditActivityPresenter presenter;

    private final Common common = new Common();
    private Intent intentIn;
    private TaskItem currentTaskItem;
    // private MultiAutoCompleteTextView actvItemName;
    private AutoCompleteTextView actvItemName;
    private EditText etItemValue;
    private EditText etItemSum;
    private Spinner elvItemUnit;
    private RadioButton rbProcessItem, rbDoneItem, rbCanceledItem;
    //private ExpandableListView elvItemUnit;
    private Button btnOkItem, btnCancelItem;
    // private List<Good> goodList;
    private String[] goodList;


    private int currentTaskType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intentIn = getIntent();
        currentTaskType = intentIn.getIntExtra(TASK_TYPE, TYPE_FREE_CONTENT);
        currentTaskItem = intentIn.getParcelableExtra(TASK_ITEM_OBJECT);
        if (currentTaskItem == null) {
            currentTaskItem = new TaskItem();
        }

        switch (currentTaskType) {
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

            /*
            actionBar.setSubtitle(
                    Calculation.getSubTitleByLayout(intent.getIntExtra("requestCode", 0),
                            getResources()).toUpperCase()
            );
            */
        }


        actvItemName = findViewById(R.id.actvItemName);
        actvItemName.setText(currentTaskItem.getContent());


        goodList = presenter.getGoods(); // getPrivateGoodList();

        ArrayAdapter goood_list_adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1,
                goodList
        );

        goodList = presenter.getGoods();

        actvItemName.setAdapter(goood_list_adapter);
        actvItemName.setThreshold(1);

        // это для варианта multy
        //actvItemName.setAdapter(goood_list_adapter);
        //actvItemName.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

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

        rbDoneItem = findViewById(R.id.rbDoneItem);
        rbCanceledItem = findViewById(R.id.rbCanceledItem);
        rbProcessItem = findViewById(R.id.rbProcessItem);

        rbDoneItem.setChecked(currentTaskItem.isDone());
        rbCanceledItem.setChecked(currentTaskItem.isCanceled());
        rbProcessItem.setChecked(!currentTaskItem.isDone() && !currentTaskItem.isCanceled());

        btnOkItem = findViewById(R.id.btnOkItem);
        btnOkItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();

                // Запоняем объект Task измененными данными
                currentTaskItem.setContent(actvItemName.getText().toString());

                currentTaskItem.setDone(rbDoneItem.isChecked());
                currentTaskItem.setCanceled(rbCanceledItem.isChecked());
                currentTaskItem.setSum(Common.StrToDouble(etItemSum.getText().toString()));
                currentTaskItem.setValue(Common.StrToDouble(etItemValue.getText().toString()));
                currentTaskItem.setModify_time(Common.getCurrentDateTimeLong());
                currentTaskItem.setUnit(elvItemUnit.getSelectedItem().toString());

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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getUnits()
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        elvItemUnit = findViewById(R.id.elvItemUnit);
        elvItemUnit.setAdapter(adapter);
        // загоspinnerловок
        elvItemUnit.setPrompt("Title");
        // выделяем элемент

        elvItemUnit.setTextAlignment( View.TEXT_ALIGNMENT_TEXT_END );

        elvItemUnit.setSelection( getUnitIndexByName(currentTaskItem.getUnit()) );
        // устанавливаем обработчик нажатия
        elvItemUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private void InitUI_market() {
        // Единицы измерения
        Spinner sprItemUnit = findViewById(R.id.sprItemUnit);
        new SpinnerAction("Units",
                getApplicationContext().getResources().getStringArray(R.array.units_goods),
                sprItemUnit
        );
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
}
