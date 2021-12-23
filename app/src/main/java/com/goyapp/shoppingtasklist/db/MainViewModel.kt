package com.goyapp.shoppingtasklist.db

import androidx.lifecycle.*
import com.goyapp.shoppingtasklist.entities.LibraryItem
import com.goyapp.shoppingtasklist.entities.NoteItem
import com.goyapp.shoppingtasklist.entities.ShopListItem
import com.goyapp.shoppingtasklist.entities.ShopListName
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class MainViewModel(database: MainDatabase) : ViewModel() { // Создали ViewModel который будет последником между view и model
    val dao = database.getDao() // Инстанция интерфейса где происходит считывание, запись, удаление,обновление в базе данных
    val libraryItems = MutableLiveData<List<LibraryItem>>() // Создание списка из списков библиотеки, livedata следит за обновлениями
    val allNotes : LiveData<List<NoteItem>> = dao.getAllNotes().asLiveData()   // Когда у нас будет что то менятся в списке, то оно будет автоматически обновляться с помощью LiveData, прослушиваем изменение
    val allShopListName : LiveData<List<ShopListName>> = dao.getAllShopListName().asLiveData()   // Когда у нас будет что то менятся в списке, то оно будет автоматически обновляться с помощью LiveData, прослушиваем изменение

    fun getAllItemsFromList(listid: Int) : LiveData<List<ShopListItem>>{ // Делаем функцию, чтобы слежение за обновлениями было корректным и отсортированым, по точным list id к которому относится item,
      return dao.getAllShopListItem(listid).asLiveData()
    }

    fun getAllLibraryItems(name: String) = viewModelScope.launch{ // Делаем функцию, чтобы слежение за обновлениями было корректным и отсортированым, по точным list id к которому относится item,
         libraryItems.postValue(dao.getAllLibrayItems(name)) // Передача в observer список libraryitems из базы данных, через который будут обновлятся данные
    }



    fun InsertNote(noteItem: NoteItem) = viewModelScope.launch { //Запуск функции на второстепенном потоке, так как это трудоемкая операция
        dao.insertNote(noteItem)
    }

    fun DeleteNote(id: Int) = viewModelScope.launch { //Запуск функции на второстепенном потоке, так как это трудоемкая операция
        dao.DeleteNote(id)
    }

    fun DeleteLibraryItem(id: Int) = viewModelScope.launch { //Запуск функции на второстепенном потоке, так как это трудоемкая операция
        dao.DeleteLibItem(id)
    }
    fun DeleteShopList(id: Int,deleteList : Boolean) = viewModelScope.launch { //Запуск функции на второстепенном потоке, так как это трудоемкая операция, удаление с проверкой, если удаляем только внутри списка элементы или весь список
        if(deleteList)dao.DeleteListName(id)
        dao.deleteShopItemsById(id)
    }

    fun UpdateNote(noteItem: NoteItem) = viewModelScope.launch {
        dao.updateNote(noteItem)
    }

    fun UpdateLibraryItem(libraryItem: LibraryItem) = viewModelScope.launch {
        dao.updateLibraryItem(libraryItem)
    }
    fun UpdateListName(shopListName: ShopListName) = viewModelScope.launch {
        dao.updateListName(shopListName)
    }

    fun InsertShopListName(shopListName: ShopListName) = viewModelScope.launch {
        dao.insertShopList(shopListName)
    }


    fun InsertShopListItem(shopListItem: ShopListItem) = viewModelScope.launch {
        dao.InsertItem(shopListItem)
        if(!isLibraryItemExist(shopListItem.name)) dao.InsertLibraryItem(LibraryItem(null,shopListItem.name)) // Если нет элементов записанных ранее такого же имени, тогда добавляем
    }

    fun UpdateShopListItem(shopListItem: ShopListItem) = viewModelScope.launch {
        dao.updateListItem(shopListItem)
    }

     private suspend fun isLibraryItemExist(name :String) : Boolean{ // Функция которая будет сверять есть ли уже такое название item в библиотеке
        return dao.getAllLibrayItems(name).isNotEmpty() // Если в библиотеке что то есть выдает true, усли не то false
    }


    class MainViewModelFactory( val database: MainDatabase) : ViewModelProvider.Factory{ // Этот класс инициализирует наш MainViewModel, нужно делать так постоянно следуя документации
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(MainViewModel::class.java)){ // Проверка на наш ViewModel
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(database) as T

            }
            throw IllegalArgumentException("Unknown ViewModelClass")
        }

    }
}