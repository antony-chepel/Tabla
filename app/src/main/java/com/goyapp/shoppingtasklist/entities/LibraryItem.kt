package com.goyapp.shoppingtasklist.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "library")
data class LibraryItem(  // Класс будет хранит все те элементы которые человек ввел(подсказки) благодаря библиотеки
    @PrimaryKey(autoGenerate = true)
    val id : Int?,

    @ColumnInfo(name = "name")
    val name : String
)
