package com.mycoloruniverse.familyteamx;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mycoloruniverse.familyteamx.model.Task;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable rx_saveTask(Task task);

    @Query("SELECT DISTINCT title FROM TaskItem ORDER BY title")
    Flowable<List<String>> rx_loadGoodList();

//    @Query("SELECT * FROM TaskItem WHERE receipt_id = :receipt_id")//
//    Flowable<List<CartItem>> rx_loadCartItems(Long receipt_id);

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    Maybe<Long> rx_saveCart(Cart cart);

//    @Delete()
//    Maybe<Void> rx_DeleteCartItems(List<CartItem> path);

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    Completable rx_saveCartItems(List<CartItem> items);

}