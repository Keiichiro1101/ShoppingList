package hu.ait.shoppinglist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import hu.ait.shoppinglist.data.AppDatabase
import hu.ait.shoppinglist.data.ShoppingDAO
import hu.ait.shoppinglist.data.ShoppingItem

// This is a ViewModel class that provides a communication channel between the View (activity or fragment) and the Model (database)
class ShoppingViewModel(application: Application) : AndroidViewModel(application) {

    // This LiveData variable holds a list of all shopping items retrieved from the database
    val allShoppingItem: LiveData<List<ShoppingItem>>

    // This variable holds an instance of the ShoppingDAO interface for performing database operations
    private var shoppingDAO: ShoppingDAO

    // This init block is executed when the ViewModel is created and initializes the shoppingDAO variable and allShoppingItem LiveData variable
    init {
        shoppingDAO = AppDatabase.getInstance(application).shoppingDao()
        allShoppingItem = shoppingDAO.getAllTShoppingItem()
    }

    // This method is used to insert a shopping item into the database in a background thread
    fun insertShoppingItem(shoppingItem: ShoppingItem)  {
        Thread {
            shoppingDAO.addShoppingItem(shoppingItem)
        }.start()
    }

    // This method is used to delete a shopping item from the database in a background thread
    fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        Thread {
            shoppingDAO.deleteShoppingItem(shoppingItem)
        }.start()
    }

    // This method is used to delete all shopping items from the database in a background thread
    fun deleteAllTShoppingItem()  {
        Thread {
            shoppingDAO.deleteAllShoppingItem()
        }.start()
    }

    // This method is used to update a shopping item in the database in a background thread
    fun updateShoppingItem(shoppingItem: ShoppingItem) {
        Thread {
            shoppingDAO.updateShoppingItem(shoppingItem)
        }.start()
    }
}