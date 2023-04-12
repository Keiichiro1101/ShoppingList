package hu.ait.shoppinglist.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hu.ait.shoppinglist.R
import hu.ait.shoppinglist.ScrollingActivity
import hu.ait.shoppinglist.data.ShoppingItem
import hu.ait.shoppinglist.databinding.ShopItemBinding
import hu.ait.shoppinglist.touch.ShoppingItemTouchHelperCallback
import hu.ait.shoppinglist.viewmodel.ShoppingViewModel
import java.util.*

class ShoppingAdapter(
private val context: Context, // context of the activity/fragment using this adapter
private val shoppingViewModel: ShoppingViewModel // ViewModel for handling shopping item data
) : ListAdapter<ShoppingItem, ShoppingAdapter.ViewHolder>(ShoppingDiffCallback()), // A ListAdapter is used to display a list of items, and ShoppingDiffCallback is used to calculate the differences between old and new lists
ShoppingItemTouchHelperCallback { // A callback for touch events (swipe to delete, drag and drop)

    // Creates and returns a new ViewHolder object
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val shopItemBinding = ShopItemBinding.inflate(
            LayoutInflater.from(context), // Inflates the view from the specified context
            parent, false // Attaches the view to the parent but doesn't add it yet
        )
        return ViewHolder(shopItemBinding)
    }

    // Binds the data to the specified ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentTodo = getItem(holder.adapterPosition) // Gets the current item from the list
        holder.bind(currentTodo) // Binds the item to the ViewHolder
    }

    // Deletes the last item in the list
    fun deleteLast() {
        val lastShoppingItem = getItem(currentList.lastIndex)
        shoppingViewModel.deleteShoppingItem(lastShoppingItem)
    }

    // Called when an item is dismissed (swiped)
    override fun onDismissed(position: Int) {
        shoppingViewModel.deleteShoppingItem(getItem(position))
    }

    // Called when an item is moved (dragged and dropped)
    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        val tmpList = mutableListOf<ShoppingItem>()
        tmpList.addAll(currentList)
        Collections.swap(tmpList, fromPosition, toPosition) // Swaps the two items
        submitList(tmpList) // Submits the new list to the adapter
    }

    // Defines the ViewHolder class
    inner class ViewHolder(val shopItemBinding: ShopItemBinding): RecyclerView.ViewHolder(shopItemBinding.root) {

        // Binds the data to the ViewHolder's views
        fun bind(shopItem: ShoppingItem) {
            shopItemBinding.tvName.text = shopItem.name
            shopItemBinding.tvPrice.text = shopItem.price.toString()
            shopItemBinding.cbBought.isChecked = shopItem.bought

            // Sets the image for the item based on its category
            when (shopItem.category) {
                0 -> {
                    shopItemBinding.ivItemLogo.setImageResource(
                        R.drawable.food)
                }
                1 -> {
                    shopItemBinding.ivItemLogo.setImageResource(
                        R.drawable.clothes)
                }
                2 -> {
                    shopItemBinding.ivItemLogo.setImageResource(
                        R.drawable.sport)
                }
            }

            // Deletes the item from the list when the "Delete" button is clicked
            shopItemBinding.btnDelete.setOnClickListener{
                shoppingViewModel.deleteShoppingItem(shopItem)
            }

            // Shows the "Edit Item" dialog when the "Edit" button is clicked
            shopItemBinding.btnEdit.setOnClickListener{
                (context as ScrollingActivity).showEditDialog(shopItem)
            }

            // Updates the item in the database when the "Bought" checkbox is clicked
            shopItemBinding.cbBought.setOnClickListener {
                shopItem.bought = shopItemBinding.cbBought.isChecked
                shoppingViewModel.updateShoppingItem(shopItem)
            }
        }
    }

    // Defines the DiffCallback class for calculating differences between old and new lists
    class ShoppingDiffCallback : DiffUtil.ItemCallback<ShoppingItem>() {
        override fun areItemsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem.name == newItem.name
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem == newItem
        }
    }
}