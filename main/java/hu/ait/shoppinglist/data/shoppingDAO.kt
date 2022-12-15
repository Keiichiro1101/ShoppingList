package hu.ait.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ShoppingDAO {

    @Query("SELECT * FROM shoppingitem")
    fun getAllTShoppingItem() : LiveData<List<ShoppingItem>>

    @Query("SELECT * FROM shoppingitem WHERE name LIKE '%' || :text || '%'")
    fun findShoppingItem(text: String) : LiveData<List<ShoppingItem>>

    @Insert
    fun addShoppingItem(shoppingItem: ShoppingItem)

    @Delete
    fun deleteShoppingItem(shoppingItem: ShoppingItem)

    @Query("DELETE FROM shoppingitem")
    fun deleteAllShoppingItem()

    @Update
    fun updateShoppingItem(shoppingItem: ShoppingItem)
}