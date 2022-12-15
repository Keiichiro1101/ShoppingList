package hu.ait.shoppinglist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shoppingitem")
data class ShoppingItem (
    @PrimaryKey(autoGenerate = false) var name: String,
    @ColumnInfo(name = "price") var price: Int,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "isbought") var bought: Boolean,
    @ColumnInfo(name = "category") var category: Int
): java.io.Serializable