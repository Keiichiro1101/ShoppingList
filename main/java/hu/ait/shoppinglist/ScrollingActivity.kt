package hu.ait.shoppinglist

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import hu.ait.shoppinglist.adapter.ShoppingAdapter
import hu.ait.shoppinglist.data.AppDatabase
import hu.ait.shoppinglist.data.ShoppingItem
import hu.ait.shoppinglist.databinding.ActivityScrollingBinding
import hu.ait.shoppinglist.dialog.ShoppingItemDialog
import hu.ait.shoppinglist.touch.ShoppingItemRecyclerTouchCallback
import hu.ait.shoppinglist.viewmodel.ShoppingViewModel
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt

class ScrollingActivity : AppCompatActivity(),
    ShoppingItemDialog.ShoppingItemDialogHandler {

    // This section defines some constants that will be used throughout the activity.
    companion object {
        const val KEY_TODO_EDIT = "KEY_TODO_EDIT"
        const val PREF_SETTINGS = "SETTINGS"
        const val KEY_FIRST_START = "KEY_FIRST_START"
    }

    // This section declares some instance variables for later use in the activity.
    private lateinit var binding: ActivityScrollingBinding
    private lateinit var adapter: ShoppingAdapter
    private lateinit var shoppingViewModel: ShoppingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This line initializes the View Binding for the activity.
        binding = ActivityScrollingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // This line initializes the ViewModel for the activity.
        shoppingViewModel = ViewModelProvider(this)[ShoppingViewModel::class.java]

        // This line sets up the RecyclerView for displaying the shopping list items.
        initRecyclerView()

        // This line sets up the Toolbar for the activity.
        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title

        // This line sets up the "Add Item" button and its associated dialog.
        binding.fabAddItem.setOnClickListener { view ->
            val shoppingItemDialog = ShoppingItemDialog()
            shoppingItemDialog.show(supportFragmentManager, "ShoppingItemDialog")
        }

        // This line sets up the "Delete All" button and its associated action.
        binding.fabDeleteAll.setOnClickListener {
            shoppingViewModel.deleteAllTShoppingItem()
        }

        // This if block sets up the Material Tap Target Prompt for the "Add Item" button if it is the first time running the app.
        if (isItFirstStart()) {
            MaterialTapTargetPrompt.Builder(this)
                .setPrimaryText("Create item")
                .setSecondaryText("Click here to create new todo")
                .setTarget(binding.fabAddItem)
                .show()
            saveAppWasStarted()
        }
    }

    // This function sets up the options menu for the activity, including the search functionality.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_scrolling, menu)

        val searchItem: MenuItem? = menu?.findItem(R.id.action_searching)
        val searchView: SearchView? =
            searchItem?.actionView as SearchView
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // This line queries the database for items matching the search term and displays them in the RecyclerView.
                AppDatabase.getInstance(this@ScrollingActivity).shoppingDao()
                    .findShoppingItem(newText!!)
                    .observe(
                        this@ScrollingActivity, Observer { items ->
                            adapter.submitList(items)
                        })

                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    // This function saves the first start status of the app.
    fun saveAppWasStarted() {
        val sp = getSharedPreferences(PREF_SETTINGS, MODE_PRIVATE)
        val editor = sp.edit()
        editor.putBoolean(KEY_FIRST_START, false)
        editor.commit()
    }

    // This function returns whether or not it is the first start of the app.
    fun isItFirstStart(): Boolean {
        val sp = getSharedPreferences(PREF_SETTINGS, MODE_PRIVATE)
        return sp.getBoolean(KEY_FIRST_START, true)
    }

    // This function sets up the RecyclerView for displaying the shopping list items.
    fun initRecyclerView() {
        adapter = ShoppingAdapter(this, shoppingViewModel)
        binding.recyclerShopping.adapter = adapter

        val callback: ItemTouchHelper.Callback = ShoppingItemRecyclerTouchCallback(adapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(binding.recyclerShopping)

        shoppingViewModel.allShoppingItem.observe(this) { shopping ->
            adapter.submitList(shopping)
        }
    }

    // This function creates and shows a dialog for editing a shopping item.
    fun showEditDialog(shoppingToEdit: ShoppingItem) {
        // Create a new ShoppingItemDialog
        val dialog = ShoppingItemDialog()

        // Create a new Bundle to store the ShoppingItem that we want to edit
        val bundle = Bundle()
        // Add the ShoppingItem to the Bundle with a key of KEY_TODO_EDIT
        bundle.putSerializable(KEY_TODO_EDIT, shoppingToEdit)
        // Set the Bundle as arguments for the dialog
        dialog.arguments = bundle

        // Show the dialog
        dialog.show(supportFragmentManager, "TAG_ITEM_EDIT")
    }

    // This function is called when a new shopping item is created in the ShoppingItemDialog. Creates SnackBar.
    override fun shoppingItemCreated(shoppingitem: ShoppingItem) {
        // Insert the new shopping item into the ViewModel
        shoppingViewModel.insertShoppingItem(shoppingitem)

        // Show a Snackbar with a message that the item was added and an "Undo" action
        Snackbar.make(
            binding.root,
            "Item added",
            Snackbar.LENGTH_LONG
        ).setAction(
            "Undo"
        ) {
            // If the "Undo" action is clicked, delete the last item from the RecyclerView by calling deleteLast() on the adapter
            adapter.deleteLast()
        }.show()
    }

    // This function is called when an existing shopping item is updated in the ShoppingItemDialog
    override fun shoppingItemUpdated(shoppingitem: ShoppingItem) {
        // Update the shopping item in the ViewModel
        shoppingViewModel.updateShoppingItem(shoppingitem)
    }
}