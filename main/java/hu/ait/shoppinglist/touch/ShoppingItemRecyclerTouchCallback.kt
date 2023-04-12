package hu.ait.shoppinglist.touch

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

/**

A class that handles touch callbacks for a shopping item RecyclerView.
Implements the ItemTouchHelper.Callback interface to enable dragging and swiping of items.
@param shoppingTouchHelperAdapter An interface that defines the actions to be taken when a shopping item is swiped or moved.
 */
class ShoppingItemRecyclerTouchCallback (
    private val shoppingTouchHelperAdapter: ShoppingItemTouchHelperCallback) :
    ItemTouchHelper.Callback() {

    /**

    Determines whether long press drag is enabled.
    @return true if long press drag is enabled, false otherwise.
     */
    override fun isLongPressDragEnabled(): Boolean {
        return true
    }
    /**

    Determines whether item view swipe is enabled.
    @return true if item view swipe is enabled, false otherwise.
     */
    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }
    /**

    Returns movement flags for the given ViewHolder and direction.
    Sets drag and swipe flags for the item touch helper.
    @param recyclerView The RecyclerView to which the ViewHolder belongs.
    @param viewHolder The ViewHolder for which movement flags are being requested.
    @return A bitmask of movement flags that indicates the directions in which the item can be dragged and swiped.
     */
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
    }
    /**

    Called when a ViewHolder is swiped by the user.
    Notifies the adapter that the item at the given position has been dismissed.
    @param viewHolder The ViewHolder that was swiped.
    @param direction The direction in which the ViewHolder was swiped.
     */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        shoppingTouchHelperAdapter.onDismissed(viewHolder.adapterPosition)
    }
    /**

    Called when a ViewHolder is moved by the user.
    Notifies the adapter that an item has been moved from the original position to the target position.
    @param recyclerView The RecyclerView to which the ViewHolder belongs.
    @param viewHolder The ViewHolder that was moved.
    @param target The ViewHolder to which the ViewHolder was moved.
    @return true if the move was successful, false otherwise.
     */
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        shoppingTouchHelperAdapter.onItemMoved(
            viewHolder.adapterPosition,
            target.adapterPosition
        )
        return true
    }
}
