package com.mycoloruniverse.familyteamx;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mycoloruniverse.familyteamx.model.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TaskPropertiesActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private final Calendar calendar = Calendar.getInstance();
    private final SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy");
    private final SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm");
    private TextView tvTaskName;
    private EditText etTaskName;
    private TextView tvCreatedTime, tvCreatedTimeDate, tvCreatedTimeTime;
    private TextView tvModifiedTime, tvModifiedTimeDate, tvModifiedTimeTime;
    private TextView tvStatus;
    private Spinner sprStatus;
    private Switch swDivideSum;
    private TextView tvTeam;
    private ImageView ivTeam1, ivTeam2, ivTeam3, ivTeam4, ivTeam5;
    private TextView tvTotalSum;
    private EditText etTotalSum;
    private DatePickerDialog dpd;
    private TimePickerDialog tpd;
    private ImageButton ibtnOk;
    private Task currentTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_task_properties);

        Intent inputIntent = getIntent();
        currentTask = (Task) inputIntent.getSerializableExtra("classTask");

        findUI();
        locateUI();
        initUI();

        createdTimeDateClick();
        createdTimeClick();

        onOkButtonClick();

        intiSpinnerAdaper();
        if (currentTask.isDone()) {
            sprStatus.setSelection(1);
        } else {
            if (currentTask.isCanceled()) {
                sprStatus.setSelection(1);
            } else {
                sprStatus.setSelection(0);
            }
        }

    }

    private void findUI() {
        /*
        // line 1
        tvTaskName = (TextView) findViewById(R.id.tvTaskName);
        etTaskName = (EditText) findViewById(R.id.etTaskName);

        // line 2
        tvCreatedTime = (TextView) findViewById(R.id.tvCreatedTime);
        tvCreatedTimeDate = (TextView) findViewById(R.id.tvCreatedTimeDate);
        tvCreatedTimeTime = (TextView) findViewById(R.id.tvCreatedTimeTime);

        // line 3
        tvModifiedTime = (TextView) findViewById(R.id.tvModifiedTime);
        tvModifiedTimeDate = (TextView) findViewById(R.id.tvModifiedTimeDate);
        tvModifiedTimeTime = (TextView) findViewById(R.id.tvModifiedTimeTime);

        // line 4
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        sprStatus = (Spinner) findViewById(R.id.sprStatus);

        // line 5
        swDivideSum = (Switch) findViewById(R.id.swDivideSum);

        // line 6
        tvTeam = (TextView) findViewById(R.id.tvTeam);
        ivTeam1 = (ImageView) findViewById(R.id.ivTeam1);
        ivTeam2 = (ImageView) findViewById(R.id.ivTeam2);
        ivTeam3 = (ImageView) findViewById(R.id.ivTeam3);
        ivTeam4 = (ImageView) findViewById(R.id.ivTeam4);
        ivTeam5 = (ImageView) findViewById(R.id.ivTeam5);

        // line 7
        tvTotalSum = (TextView) findViewById(R.id.tvTotalSum);
        etTotalSum = (EditText) findViewById(R.id.etTotalSum);

        ibtnOk = (ImageButton) findViewById(R.id.ibtnOk);

         */
    }

    private void locateUI() {
        tvCreatedTime.setText(R.string.created_time);
    }

    private void initUI() {
        if (currentTask == null) {
            return;
        }

        etTaskName.setText(currentTask.getTitle());

        calendar.setTimeInMillis(currentTask.getCreated_time());
        tvCreatedTimeDate.setText(formatDate.format(calendar.getTime()));
        tvCreatedTimeTime.setText(formatTime.format(calendar.getTime()));

        calendar.setTimeInMillis(currentTask.getModified_time());
        tvModifiedTimeDate.setText(formatDate.format(calendar.getTime()));
        tvModifiedTimeTime.setText(formatTime.format(calendar.getTime()));
    }

    private void createdTimeDateClick() {
        tvCreatedTimeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String stringDate = tvCreatedTimeDate.getText().toString();

                Date parsed;
                try {
                    parsed = formatDate.parse(stringDate);
                    calendar.setTime(parsed);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }

                dpd = new DatePickerDialog(
                        TaskPropertiesActivity.this,
                        TaskPropertiesActivity.this,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dpd.setCancelable(true);
                dpd.show();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        tvCreatedTimeDate.setText(formatDate.format(calendar.getTime()));
    }

    private void createdTimeClick() {
        tvCreatedTimeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringDate = tvCreatedTimeTime.getText().toString();

                Date parsed;
                try {
                    parsed = formatDate.parse(stringDate);
                    calendar.setTime(parsed);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }

                tpd = new TimePickerDialog(
                        TaskPropertiesActivity.this,
                        TaskPropertiesActivity.this,
                        calendar.get(Calendar.HOUR),
                        calendar.get(Calendar.MINUTE),
                        true);
                tpd.setCancelable(true);
                tpd.show();
            }
        });
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        tvCreatedTimeTime.setText(formatTime.format(calendar.getTime()));
    }

    private void onOkButtonClick() {
        ibtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // setResult(RESULT_OK, currentTask);

            }
        });
    }

    private void intiSpinnerAdaper() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"In process", "Done", "Canceled"}
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sprStatus.setAdapter(adapter);
        // загоspinnerловок
        sprStatus.setPrompt("Title");
        // выделяем элемент

        sprStatus.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);

        // sprStatus.setSelection( getUnitIndexByName(currentTaskItem.getUnit()) );
        // устанавливаем обработчик нажатия
        sprStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    }
}
