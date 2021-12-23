package com.goyapp.shoppingtasklist.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "notes_table")
data class NoteItem(
    @PrimaryKey(autoGenerate = true)
    val  id : Int?,


    @ColumnInfo(name = "title")
    val title : String,

    @ColumnInfo(name = "desc_content")
    val desc_content : String,


    @ColumnInfo(name = "note_time")
    val note_time : String,

    @ColumnInfo(name = "note_category")
    val note_category : String

):Serializable
