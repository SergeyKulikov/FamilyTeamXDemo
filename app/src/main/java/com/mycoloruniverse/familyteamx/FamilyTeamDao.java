package com.mycoloruniverse.familyteamx;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mycoloruniverse.familyteamx.model.Task;
import com.mycoloruniverse.familyteamx.model.TaskItem;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface FamilyTeamDao {
    /**
     * Исключительно для создания продукта при первом запуске в MainActivity
     */


//    @Query("SELECT * FROM CategoryItem ORDER BY name")
//    Flowable<List<CategoryItem>> rx_loadCategoryList();

//    @Query("SELECT * FROM ProductItem ORDER BY name")
//    Flowable<List<ProductItem>> rx_loadProductList();
//
    @Query("SELECT * FROM Task WHERE status = :status ORDER BY created_time DESC")
    Maybe<List<Task>> rx_loadTaskList(int status);

    @Query("SELECT t.*, " +
            "(SELECT count(*) FROM TaskItem WHERE task_guid = t.guid) as detail_count, " +
            "(SELECT count(*) FROM TaskItem WHERE task_guid = t.guid AND done = 0 AND canceled = 0) as detail_active_count " +
            "FROM Task t WHERE status = :status ORDER BY created_time DESC")
    Maybe<List<Task>> rx_loadTaskListWithCount(int status);

    @Query("SELECT * FROM Task WHERE guid = :guid")
    Maybe<Task> rx_loadTask(String guid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable rx_saveTask(Task task);

    @Query("SELECT DISTINCT title FROM TaskItem ORDER BY title")
    Flowable<List<String>> rx_loadProductList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable rx_SaveTaskItems(List<TaskItem> items);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Maybe<Long> rx_SaveTaskItem(TaskItem items);

    @Query("SELECT * FROM TaskItem WHERE task_guid = :task_guid")
    Maybe<List<TaskItem>> rx_loadTaskItems(String task_guid);

    @Query("SELECT * FROM TaskItem WHERE guid = :taskItem_guid")
    Single<TaskItem> rx_loadTaskItem(String taskItem_guid);


//    @Query("SELECT * FROM TaskItem WHERE receipt_id = :receipt_id")//
//    Flowable<List<CartItem>> rx_loadCartItems(Long receipt_id);

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    Maybe<Long> rx_saveCart(Cart cart);

//    @Delete()
//    Maybe<Void> rx_DeleteCartItems(List<CartItem> path);

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    Completable rx_saveCartItems(List<CartItem> items);

}