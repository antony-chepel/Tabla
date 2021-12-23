package com.goyapp.shoppingtasklist.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "list_item_table")
data class ShopListItem(
    @PrimaryKey(autoGenerate = true)
    val id:Int?,

    @ColumnInfo(name = "name")
    val name :String,

    @ColumnInfo(name = "item_info")
    val item_info :String = "",  // Равно null, на случай, если пользователь не хочет вносить дополнительной инфы


    @ColumnInfo(name = "item_bought")
    val item_bought :Boolean = false, // Равно 0, потому что изначально значение не будет выбрано, что item куплен

    @ColumnInfo(name = "list_id") // Индефикатор нашего списка к кторому принадлежит данный элемент
    val list_id :Int,



    @ColumnInfo(name = "item_type")
    val item_type :Int = 0  // ! Проверка если это то что выбрал пользователь


)
