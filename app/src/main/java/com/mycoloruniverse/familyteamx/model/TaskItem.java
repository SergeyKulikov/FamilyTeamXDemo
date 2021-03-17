package com.mycoloruniverse.familyteamx.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sergey on 21.12.2017.
 */

@Entity
public class TaskItem extends BaseRecord implements Parcelable {
    @NonNull
    private String task_guid;
    private boolean updateDone;
    private String description;
    private String unit;
    private double value;
    private double sum;
    private boolean done;
    private long modify_time;
    private long close_time;
    private boolean canceled;
    private String close_maker_guid;
    private int utilities_ident;
    private int status;
    private double[] startUtilValue;
    private double[] finishUtilValue;
    private double[] valueUtil;
    private double[] priceUtil;
    private double[] sumUtil;
    private String location;
    private String weather;

    public static final Creator<TaskItem> CREATOR = new Creator<TaskItem>() {
        @Override
        public TaskItem createFromParcel(Parcel in) {
            return new TaskItem(in);
        }

        @Override
        public TaskItem[] newArray(int size) {
            return new TaskItem[size];
        }
    };

    @Ignore
    public TaskItem(String content, String description, String unit) {
        // this.guid = Common.genGuid();
        this.setTitle(content);
        this.description = description;
        this.unit = unit;
        this.value = 0.00;
        this.sum = 0.00;
        this.done = false;
        this.modify_time = 0;
        this.canceled = false;
        this.close_maker_guid = "";
        this.utilities_ident = 0;
    }

    public TaskItem() {
        // this.guid = Common.genGuid();
        this.setTitle("");
        this.description = "";
        this.unit = "-";
        this.value = 0.00;
        this.sum = 0.00;
        this.done = false;
        this.modify_time = 0;
        this.canceled = false;
        this.close_maker_guid = "";
        this.utilities_ident = 0;
    }

    public TaskItem(String guid) {
        this.setTask_guid(guid);
        this.setTitle("");
        this.description = "";
        this.unit = "-";
        this.value = 0.00;
        this.sum = 0.00;
        this.done = false;
        this.modify_time = 0;
        this.canceled = false;
        this.close_maker_guid = "";
        this.utilities_ident = 0;
    }


    public int getUtilities_ident() {
        return utilities_ident;
    }

    public void setUtilities_ident(int utilities_ident) {
        this.utilities_ident = utilities_ident;
    }

    public String getContent() {
        return getTitle();
    }

    public void setContent(String content) {
        setTitle(content);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double summ) {
        this.sum = summ;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public long getModify_time() {
        return modify_time;
    }

    public void setModify_time(long modify_time) {
        this.modify_time = modify_time;
    }

    public String getClose_maker_guid() {
        return close_maker_guid;
    }

    public void setClose_maker_guid(String close_maker_guid) {
        if (close_maker_guid == null) close_maker_guid = "";
        this.close_maker_guid = close_maker_guid;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public void set(TaskItem item) {
        setContent(item.getContent());
        this.description = item.getDescription();
        this.unit = item.getUnit();
        this.value = item.getValue();
        this.sum = item.getSum();
        this.done = item.isDone();
        this.modify_time = item.getModify_time();
        this.canceled = item.isCanceled();
        this.close_maker_guid = item.getClose_maker_guid();
        this.utilities_ident = item.getUtilities_ident();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double[] getStartUtilValue() {
        return startUtilValue;
    }

    public void setStartUtilValue(double[] startUtilValue) {
        this.startUtilValue = startUtilValue;
    }

    public double[] getFinishUtilValue() {
        return finishUtilValue;
    }

    public void setFinishUtilValue(double[] finishUtilValue) {
        this.finishUtilValue = finishUtilValue;
    }

    public double[] getValueUtil() {
        return valueUtil;
    }

    public void setValueUtil(double[] valueUtil) {
        this.valueUtil = valueUtil;
    }

    public double[] getPriceUtil() {
        return priceUtil;
    }

    public void setPriceUtil(double[] priceUtil) {
        this.priceUtil = priceUtil;
    }

    public double[] getSumUtil() {
        return sumUtil;
    }

    public void setSumUtil(double[] sumUtil) {
        this.sumUtil = sumUtil;
    }

    public boolean isUpdateDone() {
        return updateDone;
    }

    public void setUpdateDone(boolean updateDone) {
        this.updateDone = updateDone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public long getClose_time() {
        return close_time;
    }

    public void setClose_time(long close_time) {
        this.close_time = close_time;
    }

    JSONObject asJSON() {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("guid", getGuid());
            jsonObject.put("content", getContent());
            jsonObject.put("description", getDescription());
            jsonObject.put("unit", getUnit());
            jsonObject.put("value", getValue());
            jsonObject.put("sum", getSum());
            jsonObject.put("done", isDone());
            jsonObject.put("modify_time", getModify_time());
            jsonObject.put("canceled", isCanceled());
            jsonObject.put("close_maker_guid", getClose_maker_guid());
            jsonObject.put("utilities_ident", getUtilities_ident());
            jsonObject.put("status", status);
            jsonObject.put("close_time", close_time);

            for (int i = 0; i < 3; i++) {
                JSONObject utilities = new JSONObject();
                utilities.put("startUtilValue", startUtilValue[i]);
                utilities.put("finishUtilValue", finishUtilValue[i]);
                utilities.put("valueUtil", valueUtil[i]);
                utilities.put("priceUtil", priceUtil[i]);
                utilities.put("sumUtil", sumUtil[i]);

                jsonObject.put("utilities", utilities);
            }
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getGuid());
        parcel.writeString(getContent());
        parcel.writeString(description);
        parcel.writeString(unit);
        parcel.writeDouble(value);
        parcel.writeDouble(sum);
        parcel.writeByte((byte) (done ? 1 : 0));
        parcel.writeLong(modify_time);
        parcel.writeLong(close_time);
        parcel.writeByte((byte) (canceled ? 1 : 0));
        parcel.writeString(close_maker_guid);
        parcel.writeInt(utilities_ident);
        parcel.writeInt(status);
        parcel.writeDoubleArray(startUtilValue);
        parcel.writeDoubleArray(finishUtilValue);
        parcel.writeDoubleArray(valueUtil);
        parcel.writeDoubleArray(priceUtil);
        parcel.writeDoubleArray(sumUtil);
        parcel.writeByte((byte) (updateDone ? 1 : 0));
        parcel.writeString(location);
        parcel.writeString(weather);
    }

    protected TaskItem(Parcel in) {
        setGuid(in.readString());
        setContent(in.readString());
        description = in.readString();
        unit = in.readString();
        value = in.readDouble();
        sum = in.readDouble();
        done = in.readByte() != 0;
        modify_time = in.readLong();
        close_time = in.readLong();
        canceled = in.readByte() != 0;
        close_maker_guid = in.readString();
        utilities_ident = in.readInt();
        status = in.readInt();
        startUtilValue = in.createDoubleArray();
        finishUtilValue = in.createDoubleArray();
        valueUtil = in.createDoubleArray();
        priceUtil = in.createDoubleArray();
        sumUtil = in.createDoubleArray();
        updateDone = in.readByte() != 0;
        location = in.readString();
        weather = in.readString();
    }

    @NonNull
    public String getTask_guid() {
        return task_guid;
    }

    public void setTask_guid(@NonNull String task_guid) {
        this.task_guid = task_guid;
    }
}
