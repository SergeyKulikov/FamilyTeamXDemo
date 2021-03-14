package com.mycoloruniverse.familyteamx;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by Sergey on 26.12.2017.
 */

public class Common implements Defines {
    public static final SimpleDateFormat fullDateTimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
    public static final SimpleDateFormat fullDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
    public static final Calendar calendar = Calendar.getInstance();
    public int alertDialogSelectedItem;
    private AlertDialog.Builder ad;

    public static String genGuid() {
        return UUID.randomUUID().toString();
    }

    public static String getTimeString(long ms, SimpleDateFormat cf) {
        return cf.format(ms);
    }

    public static long getCurrentDateTimeLong() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        return calendar.getTimeInMillis();
    }

    public static double StrToDouble(String string) {
        double value = 0.00;

        string = string.replace(",", ".");

        try {
            value = Double.valueOf(string);
        } catch (Exception ex) {
            Log.e("error", ex.getMessage());
        }

        Log.d("test", "got value = " + value);

        return value;
    }

    public static String DoubleToStr(double value, int digits) {
        if (digits < 0) {
            return String.format(Locale.US, "%f", value);
        } else {
            return String.format(Locale.US, "%." + digits + "f", value);
        }
    }

    public static void getContactsPublic(final Context context) {
        /*
        String phoneNumber = null;

        //Связываемся с контактными данными и берем с них значения id контакта, имени контакта и его номера:
        Uri CONTENT_URI = Contacts.CONTENT_URI;
        String _ID = Contacts._ID;
        String DISPLAY_NAME = Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = CommonDataKinds.Phone.NUMBER;


        StringBuffer output = new StringBuffer();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query( CONTENT_URI, null, null, null, null );

        //Запускаем цикл обработчик для каждого контакта:
        if (cursor.getCount() > 0) {

            //Если значение имени и номера контакта больше 0 (то есть они существуют) выбираем
            //их значения в приложение привязываем с соответствующие поля "Имя" и "Номер":
            while (cursor.moveToNext()) {
                String contact_id = cursor.getString( cursor.getColumnIndex( _ID ) );
                String name = cursor.getString( cursor.getColumnIndex( DISPLAY_NAME ) );
                int hasPhoneNumber = Integer.parseInt( cursor.getString( cursor.getColumnIndex( HAS_PHONE_NUMBER ) ) );

                //Получаем имя:
                if (hasPhoneNumber > 0) {
                    output.append( "\n Имя: " + name );
                    Cursor phoneCursor = contentResolver.query( PhoneCONTENT_URI, null,
                            Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null );

                    //и соответствующий ему номер:
                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString( phoneCursor.getColumnIndex( NUMBER ) );
                        output.append( "\n Телефон: " + phoneNumber );
                    }
                }
                output.append( "\n" );
            }
        }
        */
    }

    static int findStringIndex(String[] str, String line) {
        int index = -1;
        for (int i = 0; i < str.length; i++) {
            if (str[i].startsWith(line)) {
                return i;
            }
        }

        return index;
    }

    public ContentValues toContentValuesByFieldsName(String[] fieldList, Bundle bundle) {
        ContentValues contentValues = new ContentValues();
        contentValues.clear();

        for (int f = 0; f < fieldList.length; f++) {
            contentValues.put(fieldList[f], bundle.getString(fieldList[f]));
        }
        return contentValues;
    }

    boolean IntToBool(int value) {
        return (value != 0);
    }

    public void startDialog(final Context context) {
        String title = "Выбор есть всегда";
        String message = "Удалить запись";
        String button1String = "OK";
        String button2String = "Отмена";

        ad = new AlertDialog.Builder(context);
        ad.setTitle(title);  // заголовок
        ad.setMessage(message); // сообщение
        ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                Toast.makeText(context, "Вы сделали правильный выбор",
                        Toast.LENGTH_LONG).show();
            }
        });
        ad.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                Toast.makeText(context, "Возможно вы правы", Toast.LENGTH_LONG)
                        .show();
            }
        });
        /*
        ad.setCancelable(true);
        ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                Toast.makeText(context, "Вы ничего не выбрали",
                        Toast.LENGTH_LONG).show();
            }
        });
        */

        ad.show();

    }

    @SuppressLint("ResourceType")
    public void alertDialog(final int idd, String[] mListActionName, String title,
                            final Context context) {
        alertDialogSelectedItem = -1;

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title); // заголовок для диалога
        builder.setCancelable(true);

        /*
        builder.setPositiveButton(
                context.getResources().getString( R.string.buttonOk ), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                Toast.makeText(context, "Вы сделали правильный выбор",
                        Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton(
                context.getResources().getString( R.string.buttonCancel ),new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                Toast.makeText(context, "Возможно вы правы", Toast.LENGTH_LONG)
                        .show();
            }
        });
        */

        builder.setItems(
                mListActionName, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        alertDialogSelectedItem = item;
                        /*
                        switch (idd) {
                            case DIVIDE_TASK_REQUEST:
                                newTask = oldTask.copyHaventDone();

                                oldTask.deleteHaventDone();
                                break;
                        }
                        */
                    }
                }
        );

        builder.show();
    }

    public int getDayDistance(long startDate, long finishDate) {
        long difference = finishDate - startDate;
        int days = (int) (difference / (24 * 60 * 60 * 1000));
        return days;
    }

    public String[] getMonthNames(boolean longNames) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, 0, 1);

        String[] list = new String[12];
        for (int m = 0; m < 12; m++) {
            calendar.set(Calendar.MONTH, m);
            list[m] = calendar.getDisplayName(
                    Calendar.MONTH,
                    (longNames ? Calendar.LONG : Calendar.SHORT),
                    Locale.getDefault()
            );
        }

        return list;
    }

}
