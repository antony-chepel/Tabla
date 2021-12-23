package com.goyapp.shoppingtasklist.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.goyapp.shoppingtasklist.entities.LibraryItem
import com.goyapp.shoppingtasklist.entities.NoteItem
import com.goyapp.shoppingtasklist.entities.ShopListItem
import com.goyapp.shoppingtasklist.entities.ShopListName
import kotlinx.coroutines.flow.Flow

@Dao   // Dao означает что это класс(интерфейс) где будут добавлены функции для добавления, удаления, считывания базы данных
interface Dao {
    @Query("SELECT * FROM notes_table")      // Аннотация для считывания с базы данных, также прописывать SQl запросы
    fun getAllNotes(): Flow<List<NoteItem>>// Берем все заметки, Flow автоматически обновляет список, если мы что то добавляем, удаляем

    @Query("SELECT * FROM shopping_list_names")
    fun getAllShopListName() : Flow<List<ShopListName>>


    @Query("SELECT * FROM list_item_table WHERE list_id LIKE :listId") // Берем все items где list_id будет равен тому, что будем передавать, то есть конкретному созданному списку
    fun getAllShopListItem(listId:Int) : Flow<List<ShopListItem>>


    @Query("SELECT * FROM library WHERE name LIKE :name") // Берем все item которые есть, чтобы в дальнейшем сверять, добавлено в библиотеку или нет
     suspend fun getAllLibrayItems(name:String) : List<LibraryItem> // Без Flow, потому что будет просто проверятся сколько item добавляено


    @Query("DELETE FROM list_item_table WHERE list_id LIKE :listId") // Удаление еще элементов внутри списка по id списка на который нажали
    suspend fun deleteShopItemsById(listId:Int)

    @Query("DELETE FROM notes_table WHERE id IS :id") // Удаление из таблицы по id, которое будет равна нашему id которое передаем в функцию delete
    suspend fun DeleteNote(id: Int) // Функция удаления, также в корутине suspend, так как эта операция может занять какое то время

    @Query("DELETE FROM shopping_list_names WHERE id IS :id") // Удаление из таблицы по id, которое будет равна нашему id которое передаем в функцию delete
    suspend fun DeleteListName(id: Int) // Функция удаления, также в корутине suspend, так как эта операция может занять какое то время


    @Query("DELETE FROM library WHERE id IS:id" ) // Удаление Libraryitem по id
    suspend fun  DeleteLibItem(id:Int)

    @Insert
    suspend fun insertNote(note: NoteItem)   // Запуск функции внутри корутины, потому что этот процесс может занять какое то время

    @Insert
    suspend fun insertShopList(shopListName: ShopListName) // Сохранение названия нашего списка

    @Update
    suspend fun updateNote(note: NoteItem) // Функция для обновления

    @Insert
    suspend fun InsertItem(shoplistnameItem:ShopListItem)

    @Insert
    suspend fun InsertLibraryItem(libraryItem:LibraryItem) // Функция для добавления в библиотеку item

    @Update
    suspend fun updateListName(shopListName: ShopListName) // Функция для обновления

    @Update
    suspend fun updateLibraryItem(libraryItem: LibraryItem) // Функция для обновления

    @Update
    suspend fun updateListItem(shopListItem: ShopListItem) // Функция для обновления


}