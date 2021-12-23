package com.goyapp.shoppingtasklist.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "shopping_list_names")   // Указали что это модель и сама таблица для создания базы данных
data class ShopListName(
    @PrimaryKey(autoGenerate = true)  // Указали автогенерацию для id
    val id : Int?, // Ставим знак вопроса, указывая что переменная null, чтобы автогенерация сработала

    @ColumnInfo(name = "name") // Создание колонны на основе переменной которая создается ниже
    val name : String,

    @ColumnInfo(name = "time")
    val  time : String,

    @ColumnInfo(name = "total_item_count")
    val  countItemAll : Int,

    @ColumnInfo(name = "bought_item_count")
    val  countItemBought : Int,


    @ColumnInfo(name = "items_id")
    val  items_id : String,   // Индификаторы внутри самого списка



): Serializable  // Чтобы передавать как целый класс
