package hu.ait.shoppinglist.touch

/**
 * Interface to handle item swipe and move events in the ShoppingAdapter
 */
interface ShoppingItemTouchHelperCallback {

    /**
     * Callback when an item is swiped and dismissed from the list.
     * @param position The position of the dismissed item in the list.
     */
    fun onDismissed(position: Int)

    /**
     * Callback when an item is moved within the list.
     * @param fromPosition The original position of the item in the list.
     * @param toPosition The new position of the item in the list.
     */
    fun onItemMoved(fromPosition: Int, toPosition: Int)
}