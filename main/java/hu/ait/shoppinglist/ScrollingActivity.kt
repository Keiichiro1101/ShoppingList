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

    companion object {
        const val KEY_TODO_EDIT = "KEY_TODO_EDIT"
        const val PREF_SETTINGS = "SETTINGS"
        const val KEY_FIRST_START = "KEY_FIRST_START"
    }

    private lateinit var binding: ActivityScrollingBinding
    private lateinit var adapter: ShoppingAdapter

    private lateinit var shoppingViewModel: ShoppingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScrollingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        shoppingViewModel = ViewModelProvider(this)[ShoppingViewModel::class.java]

        initRecyclerView()

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title

        binding.fabAddItem.setOnClickListener { view ->
            val shoppingItemDialog = ShoppingItemDialog()
            shoppingItemDialog.show(supportFragmentManager, "ShoppingItemDialog")
        }

        binding.fabDeleteAll.setOnClickListener{
            shoppingViewModel.deleteAllTShoppingItem()
        }

        if (isItFirstStart()) {
            MaterialTapTargetPrompt.Builder(this)
                .setPrimaryText("Create item")
                .setSecondaryText("Click here to create new todo")
                .setTarget(binding.fabAddItem)
                .show()
            saveAppWasStarted()
        }
    }

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
                AppDatabase.getInstance(this@ScrollingActivity).shoppingDao().findShoppingItem(newText!!)
                    .observe(
                        this@ScrollingActivity, Observer { items ->
                            adapter.submitList(items)
                        })

                return true
            }
        })


        return super.onCreateOptionsMenu(menu)
    }


    fun saveAppWasStarted() {
        val sp =getSharedPreferences(PREF_SETTINGS, MODE_PRIVATE)
        val editor = sp.edit()
        editor.putBoolean(KEY_FIRST_START, false)
        editor.commit()
    }

    fun isItFirstStart() : Boolean {
        val sp =getSharedPreferences(PREF_SETTINGS, MODE_PRIVATE)
        return sp.getBoolean(KEY_FIRST_START, true)
    }

    fun initRecyclerView() {
        adapter = ShoppingAdapter(this, shoppingViewModel)
        binding.recyclerShopping.adapter = adapter

        val callback: ItemTouchHelper.Callback = ShoppingItemRecyclerTouchCallback(adapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(binding.recyclerShopping)

        shoppingViewModel.allShoppingItem.observe(this) { shopping->
            adapter.submitList(shopping)
        }
    }


    fun showEditDialog(shoppingToEdit: ShoppingItem) {
        val dialog = ShoppingItemDialog()

        val bundle = Bundle()
        bundle.putSerializable(KEY_TODO_EDIT, shoppingToEdit)
        dialog.arguments = bundle

        dialog.show(supportFragmentManager, "TAG_ITEM_EDIT")
    }

    override fun shoppingItemCreated(shoppingitem: ShoppingItem) {
        shoppingViewModel.insertShoppingItem(shoppingitem)

        Snackbar.make(
            binding.root,
            "Item added",
            Snackbar.LENGTH_LONG
        ).setAction(
            "Undo"
        ) {
            // remove last todo from RecyclerView..
            adapter.deleteLast()
        }.show()
    }

    override fun shoppingItemUpdated(shoppingitem: ShoppingItem) {
        shoppingViewModel.updateShoppingItem(shoppingitem)
    }




}