package hu.ait.shoppinglist.touch

interface ShoppingItemTouchHelperCallback {
    fun onDismissed(position: Int)
    fun onItemMoved(fromPosition: Int, toPosition: Int)
}