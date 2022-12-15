package hu.ait.shoppinglist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import hu.ait.shoppinglist.data.AppDatabase
import hu.ait.shoppinglist.data.ShoppingDAO
import hu.ait.shoppinglist.data.ShoppingItem

class ShoppingViewModel(application: Application) :
    AndroidViewModel(application) {

    val allShoppingItem: LiveData<List<ShoppingItem>>

    private var shoppingDAO: ShoppingDAO

    init {
        shoppingDAO = AppDatabase.getInstance(application).shoppingDao()
        allShoppingItem = shoppingDAO.getAllTShoppingItem()
    }

    fun insertShoppingItem(shoppingItem: ShoppingItem)  {
        Thread {
            shoppingDAO.addShoppingItem(shoppingItem)
        }.start()
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        Thread {
            shoppingDAO.deleteShoppingItem(shoppingItem)
        }.start()
    }

    fun deleteAllTShoppingItem()  {
        Thread {
            shoppingDAO.deleteAllShoppingItem()
        }.start()
    }

    fun updateShoppingItem(shoppingItem: ShoppingItem) {
        Thread {
            shoppingDAO.updateShoppingItem(shoppingItem)
        }.start()
    }
}