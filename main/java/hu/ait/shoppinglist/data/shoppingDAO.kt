package hu.ait.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao // This annotation marks the interface as a DAO
interface ShoppingDAO {

    // This method queries the database to retrieve all shopping items and returns them as LiveData
    @Query("SELECT * FROM shoppingitem")
    fun getAllTShoppingItem() : LiveData<List<ShoppingItem>>

    // This method queries the database to find shopping items with names containing the provided text and returns them as LiveData
    @Query("SELECT * FROM shoppingitem WHERE name LIKE '%' || :text || '%'")
    fun findShoppingItem(text: String) : LiveData<List<ShoppingItem>>

    // This method inserts a shopping item into the database
    @Insert
    fun addShoppingItem(shoppingItem: ShoppingItem)

    // This method deletes a shopping item from the database
    @Delete
    fun deleteShoppingItem(shoppingItem: ShoppingItem)

    // This method deletes all shopping items from the database
    @Query("DELETE FROM shoppingitem")
    fun deleteAllShoppingItem()

    // This method updates a shopping item in the database
    @Update
    fun updateShoppingItem(shoppingItem: ShoppingItem)
}