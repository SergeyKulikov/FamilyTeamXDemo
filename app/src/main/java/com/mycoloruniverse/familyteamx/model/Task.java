package com.mycoloruniverse.familyteamx.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.room.Entity;
import androidx.room.Ignore;

import com.mycoloruniverse.familyteamx.Defines;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergey on 21.12.2017.
 */

@Entity
public class Task extends BaseRecord implements Parcelable, Defines {

    private String creator_guid;  // Кто создал запись
    private long created_time;    // Дата создания
    private long modified_time;   // Дата модификации
    private long close_time;      // Дата закрытия (если не NULL изменение не возможно)
    private boolean done;         // TODO: Выполнен. Должен автоматом внести close_time.
    private boolean canceled;
    private double sum;           // Сумма всей задачи. Берется или из позиций или ставится руками
    private boolean divide_sum;   // Разделять или не разделять сумму чека по позициям
    private int type;             // Тип задачи. В настоящий момент 4 типа
    private int status;           // Статус проставляется в зависимости от
    private String currency_id;
    private double currency_rate;
    private boolean updateDone;           // Сделано обновление
    private boolean hasChange;            // Произошли изменения
    private String weather;
    private String location;

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
    @Ignore
    private String[] members;    // Члены команды ()
    @Ignore
    private final static String TAG = Task.class.getSimpleName();
    @Ignore
    private final int TEAM_LIMIT = 5;
    @Ignore
    private final List<TaskItem> items; // Позиции задачи

    public Task(String creator_guid, String title, int type) {
        this.creator_guid = com.mycoloruniverse.familyteamx.Preferences.getUserGuid();
        this.setTitle(title);
        this.created_time = System.currentTimeMillis();
        this.modified_time = 0;
        this.done = false;
        this.canceled = false;
        this.close_time = 0;
        this.sum = 0.00;
        this.divide_sum = false;
        this.type = type;
        this.currency_id = com.mycoloruniverse.familyteamx.Preferences.getCurrencyDefault();
        this.currency_rate = 1;

        this.items = new ArrayList<>();
        this.hasChange = false;
        this.members = new String[TEAM_LIMIT];

        this.weather = "";
        this.location = "";
    }


    public Task() {
        this.creator_guid = com.mycoloruniverse.familyteamx.Preferences.getUserGuid();
        this.setTitle(null);
        this.created_time = System.currentTimeMillis();
        this.modified_time = 0;
        this.done = false;
        this.canceled = false;
        this.close_time = 0;
        this.sum = 0.00;
        this.divide_sum = false;
        this.type = com.mycoloruniverse.familyteamx.Preferences.getContentTypeDefault();
        this.currency_id = com.mycoloruniverse.familyteamx.Preferences.getCurrencyDefault();
        this.currency_rate = 1;

        this.items = new ArrayList<>();
        this.hasChange = false;
        this.members = new String[TEAM_LIMIT];

        this.weather = "";
        this.location = "";
    }


    public static String getTAG() {
        return TAG;
    }

    public String getCreator_guid() {
        return creator_guid;
    }

    public void setCreator_guid(String creater_guid) {
        this.creator_guid = creater_guid;
    }

    public long getCreated_time() {
        return created_time;
    }

    public void setCreated_time(long created_time) {
        this.created_time = created_time;
    }

    public long getModified_time() {
        return modified_time;
    }

    public void setModified_time(long modified_time) {
        this.modified_time = modified_time;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public long getClose_time() {
        return close_time;
    }

    public void setClose_time(long close_time) {
        this.close_time = close_time;
    }

    public List<TaskItem> getItems() {
        return items;
    }

    void setItems(List<TaskItem> items) {
        this.items.clear();
        this.items.addAll(items);
    }

    public int resetItem(TaskItem item) {
        // Это попытка заменить уже имеющуюся запись
        int index = -1;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getGuid().equals(item.getGuid())) {
                items.get(i).set(item);
                index = i;
                break;
            } else {
                Log.d(TAG, items.get(i).getGuid() + " != " + item.getGuid());
            }
        }

        return index;
    }

    public double getSum() {
        double summ = 0.00;

        if (divide_sum) {
            for (int i = 0; i < items.size(); i++) {
                summ += items.get(i).getSum();
            }
        } else {
            summ = this.sum;
        }

        return summ;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public boolean isDivide_sum() {
        return divide_sum;
    }

    public void setDivide_sum(boolean devide_sum) {
        this.divide_sum = devide_sum;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    boolean verifyDone() {
        if (items.size() == 0) return isDone();

        boolean rez = true;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isCanceled()) continue;
            rez &= items.get(i).isDone();
        }
        return rez;
    }

    Task copyHaventDone() {
        Task newTask = new Task();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isCanceled()) continue;
            if (!items.get(i).isDone()) {
                newTask.getItems().add(items.get(i));
            }
        }

        if (newTask.getItems().size() == 0) return null;

        newTask.setTitle(this.getTitle() + " (new)");
        newTask.setDivide_sum(this.isDivide_sum());
        newTask.setType(this.getType());
        return newTask;
    }

    void deleteHaventDone() {
        // физическое удаление нод из памяти
        for (int i = items.size() - 1; i >= 0; i--) {
            if (items.get(i).isCanceled()) continue;
            if (!items.get(i).isDone()) {
                items.remove(i);
            }
        }
    }

    ArrayList<String> getItemListToDeleteHaventDone() {
        // Получаем стписок нод на удаление

        ArrayList<String> list = new ArrayList<>();
        for (int i = items.size() - 1; i >= 0; i--) {
            if (items.get(i).isCanceled()) continue;
            if (!items.get(i).isDone()) {
                // items.remove(i);
                list.add(items.get(i).getGuid());
            }
        }

        return list;
    }

    public int getStatus() {
        return this.status;
    }

    public Task(Parcel in) {
        setGuid(in.readString());
        creator_guid = in.readString();
        setTitle(in.readString());
        created_time = in.readLong();
        modified_time = in.readLong();
        close_time = in.readLong();
        done = in.readByte() != 0;
        canceled = in.readByte() != 0;
        sum = in.readDouble();
        divide_sum = in.readByte() != 0;
        type = in.readInt();
        status = in.readInt();
        currency_id = in.readString();
        currency_rate = in.readDouble();
        updateDone = in.readByte() != 0;
        hasChange = in.readByte() != 0;
        weather = in.readString();
        location = in.readString();
        members = in.createStringArray();
        items = in.createTypedArrayList(TaskItem.CREATOR);
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCurrency_id() {
        return currency_id;
    }

    public void setCurrency_id(String currency_id) {
        this.currency_id = currency_id;
    }

    public double getCurrency_rate() {
        return currency_rate;
    }

    public void setCurrency_rate(double currency_rate) {
        this.currency_rate = currency_rate;
    }

    public int getTEAM_LIMIT() {
        return TEAM_LIMIT;
    }

    public boolean isUpdateDone() {
        return updateDone;
    }

    public void setUpdateDone(boolean updateDone) {
        this.updateDone = updateDone;
    }

    public boolean isHasChange() {
        return hasChange;
    }

    public void setHasChange(boolean hasChange) {
        this.hasChange = hasChange;
    }

    public String[] getMembers() {
        return members;
    }

    public void setMembers(String[] members) {
        this.members = members;
    }

    JSONObject asJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("guid", getGuid());
            jsonObject.put("creator_guid", getCreator_guid());
            jsonObject.put("created_time", getCreated_time());
            jsonObject.put("modified_time", getModified_time());
            jsonObject.put("close_time", getClose_time());
            jsonObject.put("title", getTitle());
            jsonObject.put("sum", getSum());
            jsonObject.put("divide_sum", isDivide_sum());
            jsonObject.put("type", getType());
            jsonObject.put("status", getStatus());

            JSONArray membersArr = new JSONArray();

            for (int i = 0; i < TEAM_LIMIT; i++) {
                membersArr.put(i, members[i]);
            }
            jsonObject.put("members", membersArr);

            jsonObject.put("currency_id", currency_id);
            jsonObject.put("currency_rate", currency_rate);

            JSONArray array = new JSONArray();
            for (int i = 0; i < items.size(); i++) {
                array.put(i, items.get(i).asJSON());
            }
            jsonObject.put("items", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*
        private boolean done;         // TODO: Выполнен. Должен автоматом внести close_time.
        private boolean canceled;
        boolean updateDone;           // Сделано обновление
        boolean hasChange;            // Произошли изменения
        */

        return jsonObject;
    }

    public void setCreator(String creator) {
        // todo: Оставить только один вариант setCreator, а setCreator_guid убрать или сделать невидимым
        this.setCreator_guid(creator);
        this.members[0] = creator;
    }

    public double getItemsDone() {
        double rez = 0;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isDone()) {
                rez++;
            }
        }
        return rez;
    }

    public double getActiveItemsCount() {
        double rez = 0;
        for (int i = 0; i < items.size(); i++) {
            if (!items.get(i).isCanceled()) {
                rez++;
            }
        }
        return rez;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void setStatus(int status) {
        this.status = status;
        switch (status) {
            case IDD_STATUS_PROGRESS:
                this.done = false;
                this.canceled = false;
                break;
            case IDD_STATUS_DONE:
                this.done = true;
                this.canceled = false;
                break;
            case IDD_STATUS_CANCELLED:
                this.done = false;
                this.canceled = true;
                break;
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getGuid());
        dest.writeString(creator_guid);
        dest.writeString(getTitle());
        dest.writeLong(created_time);
        dest.writeLong(modified_time);
        dest.writeLong(close_time);
        dest.writeByte((byte) (done ? 1 : 0));
        dest.writeByte((byte) (canceled ? 1 : 0));
        dest.writeDouble(sum);
        dest.writeByte((byte) (divide_sum ? 1 : 0));
        dest.writeInt(type);
        dest.writeInt(status);
        dest.writeString(currency_id);
        dest.writeDouble(currency_rate);
        dest.writeByte((byte) (updateDone ? 1 : 0));
        dest.writeByte((byte) (hasChange ? 1 : 0));
        dest.writeString(weather);
        dest.writeString(location);
        dest.writeStringArray(members);
        dest.writeTypedList(items);
    }


}
