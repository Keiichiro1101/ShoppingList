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
    private val context: Context,
    private val shoppingViewModel: ShoppingViewModel
) : ListAdapter<ShoppingItem, ShoppingAdapter.ViewHolder>(ShoppingDiffCallback()),
    ShoppingItemTouchHelperCallback {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val shopItemBinding = ShopItemBinding.inflate(
            LayoutInflater.from(context),
            parent, false
        )
        return ViewHolder(shopItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentTodo = getItem(holder.adapterPosition)
        holder.bind(currentTodo)
    }

    fun deleteLast() {
        val lastShoppingItem = getItem(currentList.lastIndex)
        shoppingViewModel.deleteShoppingItem(lastShoppingItem)
    }

    override fun onDismissed(position: Int) {
        shoppingViewModel.deleteShoppingItem(getItem(position))
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        val tmpList = mutableListOf<ShoppingItem>()
        tmpList.addAll(currentList)
        Collections.swap(tmpList, fromPosition, toPosition)
        submitList(tmpList)
    }



    inner class ViewHolder(val shopItemBinding: ShopItemBinding): RecyclerView.ViewHolder(shopItemBinding.root) {

        fun bind(shopItem: ShoppingItem) {
            shopItemBinding.tvName.text = shopItem.name
            shopItemBinding.tvPrice.text = shopItem.price.toString()
            shopItemBinding.cbBought.isChecked = shopItem.bought

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

            shopItemBinding.btnDelete.setOnClickListener{
                shoppingViewModel.deleteShoppingItem(shopItem)
            }

            shopItemBinding.btnEdit.setOnClickListener{
                (context as ScrollingActivity).showEditDialog(shopItem)
            }

            shopItemBinding.cbBought.setOnClickListener {
                shopItem.bought = shopItemBinding.cbBought.isChecked
                shoppingViewModel.updateShoppingItem(shopItem)
            }
        }
    }


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