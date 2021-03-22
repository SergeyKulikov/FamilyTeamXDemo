package com.mycoloruniverse.familyteamx.model;

/**
 * Класс для создания списка атрибутов по конкретной задаче.
 * Он нужен, чтобы хранить сумарную информацию, чтения деталей.
 * Сами детали не хранить в классе Task не хочется, чтобы не грузить память устройства.
 * Поэтому читаем только основные данные задачи, а потом по выборке деталей заполняем атрибуты.
 */

public class TaskAttr {
    private String task_guid;
    private double sum;
    private int done_items;
    private int cancelled_items;
    private int detail_count;

    public TaskAttr(double sum, int done_items, int cancelled_items, int detail_count) {
        this.task_guid = null;
        this.sum = sum;
        this.done_items = done_items;
        this.cancelled_items = cancelled_items;
        this.detail_count = detail_count;
    }

    public TaskAttr() {
        clear();
    }

    private void clear() {
        this.task_guid = null;
        this.sum = 0.00;
        this.done_items = 0;
        this.cancelled_items = 0;
        this.detail_count = 0;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public int getDone_items() {
        return done_items;
    }

    public void setDone_items(int done_items) {
        this.done_items = done_items;
    }

    public int getCancelled_items() {
        return cancelled_items;
    }

    public void setCancelled_items(int cancelled_items) {
        this.cancelled_items = cancelled_items;
    }

    public int getDetail_count() {
        return detail_count;
    }

    public void setDetail_count(int detail_count) {
        this.detail_count = detail_count;
    }

    public void merge(TaskItem taskItem) {
        sum += taskItem.getSum();
        done_items += taskItem.isDone() ? 1 : 0;
        cancelled_items += taskItem.isCanceled() ? 1 : 0;
        detail_count++;
    }

    public String getTask_guid() {
        return task_guid;
    }

    public void setTask_guid(String task_guid) {
        this.task_guid = task_guid;
    }
}
